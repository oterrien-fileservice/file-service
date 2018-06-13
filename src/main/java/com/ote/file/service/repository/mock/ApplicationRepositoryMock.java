package com.ote.file.service.repository.mock;

import com.ote.file.spi.IApplicationRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mock")
public class ApplicationRepositoryMock implements IApplicationRepository {

    @Override
    public boolean isFound(String application) {
        return true;
    }

    @Override
    public boolean isFound(String application, String perimeter) {
        return true;
    }
}
