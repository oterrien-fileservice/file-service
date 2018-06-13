package com.ote.file.service.controller;

import com.ote.file.api.IFileService;
import com.ote.file.api.model.File;
import com.ote.file.api.model.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/storage")
public class FileController {

    @Autowired
    private IFileService fileServiceAdapter;

    /**
     * This endpoint aims to return all folders for a given application and a given perimeter
     *
     * @param user
     * @param application
     * @param perimeter
     * @return
     * @throws Exception
     */
    @GetMapping("/folders")
    public Set<String> getFolders(@RequestParam("user") String user,
                                  @RequestParam("application") String application,
                                  @RequestParam("perimeter") String perimeter) throws Exception {

        return fileServiceAdapter.getFolders(user, application, perimeter).
                stream().
                map(p -> p.getPath()).
                collect(Collectors.toSet());
    }

    /**
     * This endpoint aims to return all files for a given application and a given perimeter
     *
     * @param user
     * @param application
     * @param perimeter
     * @param folder
     * @return
     * @throws Exception
     */
    @GetMapping("/files")
    public Set<String> getFiles(@RequestParam("user") String user,
                                @RequestParam("application") String application,
                                @RequestParam("perimeter") String perimeter,
                                @RequestParam(value = "folder", required = false) String folder) throws Exception {
        if (folder == null) {
            return fileServiceAdapter.getFiles(user, application, perimeter).
                    stream().
                    map(p -> p.getPath()).
                    collect(Collectors.toSet());
        } else {
            return fileServiceAdapter.getFiles(user, application, perimeter, new Folder(folder)).
                    stream().
                    map(p -> p.getPath()).
                    collect(Collectors.toSet());
        }
    }

    /**
     * This endpoint aims to download a file
     *
     * @param user
     * @param application
     * @param perimeter
     * @param folderPath
     * @param fileName
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/files/content", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> readFile(@RequestParam("user") String user,
                                             @RequestParam("application") String application,
                                             @RequestParam("perimeter") String perimeter,
                                             @RequestParam("folder") String folderPath,
                                             @RequestParam("file") String fileName) throws Exception {

        byte[] content = fileServiceAdapter.read(user, application, perimeter, new File(new Folder(folderPath), fileName));

        ByteArrayResource resource = new ByteArrayResource(content);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    /**
     * This endpoint aims to create the file if not exist or to replace it
     *
     * @param user
     * @param application
     * @param perimeter
     * @param folderPath
     * @param file
     * @param replaceIfNotFound
     * @throws Exception
     */
    @PutMapping("/files/content")
    @ResponseStatus(HttpStatus.OK)
    public void createOrReplace(@RequestParam("user") String user,
                                @RequestParam("application") String application,
                                @RequestParam("perimeter") String perimeter,
                                @RequestParam("folder") String folderPath,
                                @RequestParam("file") MultipartFile file,
                                @RequestParam(value = "replaceIfFound", defaultValue = "${service.upload.create.replace-if-found}") boolean replaceIfNotFound) throws Exception {


        File nFile = new File(new Folder(folderPath), file.getOriginalFilename());
        fileServiceAdapter.save(user, application, perimeter, nFile, file.getBytes(), replaceIfNotFound);
    }

    /**
     * This endpoint aims to append the file to the existing one
     *
     * @param user
     * @param application
     * @param perimeter
     * @param folderPath
     * @param file
     * @param createIfNotFound
     * @throws Exception
     */
    @PatchMapping("/files/content")
    @ResponseStatus(HttpStatus.OK)
    public void append(@RequestParam("user") String user,
                       @RequestParam("application") String application,
                       @RequestParam("perimeter") String perimeter,
                       @RequestParam("folder") String folderPath,
                       @RequestParam("file") MultipartFile file,
                       @RequestParam(value = "createIfNotFound", defaultValue = "${service.upload.append.create-if-not-found}") boolean createIfNotFound) throws Exception {

        File nFile = new File(new Folder(folderPath), file.getOriginalFilename());
        fileServiceAdapter.append(user, application, perimeter, nFile, file.getBytes(), createIfNotFound);
    }
}
