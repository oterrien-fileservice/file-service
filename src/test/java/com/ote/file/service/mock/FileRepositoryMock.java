package com.ote.file.service.mock;

import com.ote.file.api.model.File;
import com.ote.file.api.model.Folder;
import com.ote.file.service.repository.mock.Utils;
import com.ote.file.spi.IFileRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Profile("test")
public class FileRepositoryMock implements IFileRepository {

    private final Map<Key, Set<Folder>> map = new HashMap<>();

    public void add(String application, String perimeter, Folder folder) {

        Set<Folder> folders = getFolders(application, perimeter);
        folders.add(folder);
        map.put(new Key(application, perimeter), folders);
    }

    public void reset() {
        map.clear();
    }


    @Override
    public Set<Folder> getFolders(String application, String perimeter) {
        Key key = new Key(application, perimeter);
        return Optional.ofNullable(map.get(key)).orElse(new HashSet<>());
    }

    @Override
    public Set<File> getFiles(String application, String perimeter, Folder folder) {
        try {
            return Files.list(Paths.get(folder.getPath())).map(p -> new File(folder, p.getFileName().toString())).collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(File file, byte[] content) {
        Utils.write(file.getPath(), content);
    }

    @Override
    public void append(File file, byte[] content) {
        Utils.append(file.getPath(), content);
    }

    @Override
    public byte[] read(File file) {
        return Utils.readFile(file.getPath());
    }

    @Data
    @RequiredArgsConstructor
    public static class Key {
        private final String application;
        private final String perimeter;
    }
}
