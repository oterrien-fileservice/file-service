package com.ote.file.service.mock;

import com.ote.file.spi.ILock;
import com.ote.file.spi.ILockRepository;
import lombok.Getter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Profile("test")
public class LockRepositoryMock implements ILockRepository {

    private final List<FileLock> keyList = Collections.synchronizedList(new ArrayList<>());

    @Getter
    private final ILock lock = new SynchronizedMock();

    @Override
    public Optional<FileLock> getFileLock(KeyFileLock key) {
        return keyList.stream().
                filter(p -> p.getKey().equals(key)).
                findAny();
    }

    @Override
    public void lockFile(FileLock key) {
        keyList.add(key);
    }

    @Override
    public void unlockFile(KeyFileLock key) {
        keyList.removeIf(p -> p.getKey().equals(key));
    }

    class SynchronizedMock implements ILock {

        private final java.util.concurrent.locks.Lock lock = new ReentrantLock();

        @Override
        public boolean tryLock(long timeout, TimeUnit timeUnit) {

            try {
                return lock.tryLock(timeout, timeUnit);
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public void unlock() {
            try {
                lock.unlock();
            } catch (Exception ignored) {
                // Avoid error when lock had not been acquired
            }
        }
    }
}

