package com.ote.file.service.repository.mock;

import com.ote.file.spi.IUserRightRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Profile("mock")
public class UserRightRepositoryMock implements IUserRightRepository {

    @Override
    public boolean isAuthorized(String user, String application, String perimeter, Privilege privilege) {
        return true;
    }

    @Override
    public Set<Privilege> getPrivileges(String user, String application, String perimeter) {
        return new HashSet<>();
    }


}
