package com.ote.file.service.mock;

import com.ote.file.spi.IUserRightRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Profile("test")
public class UserRightRepositoryMock implements IUserRightRepository {

    private final Map<Key, Set<Privilege>> map = new HashMap<>();

    public void add(String user, String application, String perimeter, Privilege privilege) {

        Set<Privilege> privileges = getPrivileges(user, application, perimeter);
        privileges.add(privilege);
        map.put(new Key(user, application, perimeter), privileges);
    }

    public void reset() {
        map.clear();
    }

    @Override
    public Set<Privilege> getPrivileges(String user, String application, String perimeter) {
        return Optional.ofNullable(map.get(new Key(user, application, perimeter))).orElse(new HashSet<>());
    }

    @Data
    @RequiredArgsConstructor
    public class Key {
        private final String user;
        private final String application;
        private final String perimeter;
    }

}
