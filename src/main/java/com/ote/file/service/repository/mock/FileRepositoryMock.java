package com.ote.file.service.repository.mock;

import com.ote.file.api.model.File;
import com.ote.file.api.model.Folder;
import com.ote.file.spi.IFileRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Profile("mock")
public class FileRepositoryMock implements IFileRepository {

    private final Map<Key, Set<Folder>> foldersMap = new HashMap<>();

    @PostConstruct
    public void init() {
        foldersMap.put(new Key("SLA", "GLE"), Collections.singleton("target").stream().map(p -> new Folder(p)).collect(Collectors.toSet()));
    }

    @Override
    public Set<Folder> getFolders(String application, String perimeter) {
        Key key = new Key(application, perimeter);
        return Optional.ofNullable(foldersMap.get(key)).orElse(Collections.emptySet());
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
        StringBuilder sb = new StringBuilder().append(new String(read(file))).append(new String(content));
        write(file, sb.toString().getBytes());
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
