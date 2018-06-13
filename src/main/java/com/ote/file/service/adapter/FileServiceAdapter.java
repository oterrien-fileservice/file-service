package com.ote.file.service.adapter;

import com.ote.file.api.IFileService;
import com.ote.file.api.ServiceProvider;
import com.ote.file.api.exception.*;
import com.ote.file.api.model.File;
import com.ote.file.api.model.Folder;
import com.ote.file.spi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class FileServiceAdapter implements IFileService {

    private final IFileService fileService;

    public FileServiceAdapter(@Autowired IUserRepository userRepository,
                              @Autowired IApplicationRepository applicationRepository,
                              @Autowired IUserRightRepository userRightRepository,
                              @Autowired IFileRepository fileRepository,
                              @Autowired ILockRepository lockRepository,
                              @Value("${service.lock.timeout.seconds}") long lockTimeoutSecond) {

        fileService = ServiceProvider.getInstance().
                getFileServiceFactory().
                createFileService(userRepository, applicationRepository, userRightRepository, fileRepository, lockRepository, lockTimeoutSecond, TimeUnit.SECONDS);
    }

    @Override
    public Set<Folder> getFolders(String user, String application, String perimeter) throws UserNotFoundException, ApplicationNotFoundException, PerimeterNotFoundException, UnauthorizedException {
        return fileService.getFolders(user, application, perimeter);
    }

    @Override
    public Set<File> getFiles(String user, String application, String perimeter, Folder folder) throws UserNotFoundException, ApplicationNotFoundException, PerimeterNotFoundException, FolderNotFoundException, UnauthorizedException {
        return fileService.getFiles(user, application, perimeter, folder);
    }

    @Override
    public Set<File> getFiles(String user, String application, String perimeter) throws UserNotFoundException, ApplicationNotFoundException, PerimeterNotFoundException, UnauthorizedException {
        return fileService.getFiles(user, application, perimeter);
    }

    @Override
    public byte[] read(String user, String application, String perimeter, File file) throws UserNotFoundException, ApplicationNotFoundException, PerimeterNotFoundException, FolderNotFoundException, FileNotFoundException, UnauthorizedException, LockException {
        return fileService.read(user, application, perimeter, file);
    }

    @Override
    public void save(String user, String application, String perimeter, File file, byte[] content, boolean replaceIfFound) throws UserNotFoundException, ApplicationNotFoundException, PerimeterNotFoundException, FolderNotFoundException, FileFoundException, UnauthorizedException, LockException {
        fileService.save(user, application, perimeter, file, content, replaceIfFound);
    }

    @Override
    public void append(String user, String application, String perimeter, File file, byte[] content, boolean createIfNotFound) throws UserNotFoundException, ApplicationNotFoundException, PerimeterNotFoundException, FolderNotFoundException, FileNotFoundException, UnauthorizedException, LockException {
        fileService.append(user, application, perimeter, file, content, createIfNotFound);
    }
}
