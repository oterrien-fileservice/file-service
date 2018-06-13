package com.ote.file.service.repository.mock;

import com.ote.file.spi.IUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mock")
public class UserRepositoryMock implements IUserRepository {

    @Override
    public boolean isFound(String user) {
        return true;
    }
}
