package com.ote.file.service;

import com.ote.file.api.exception.FileFoundException;
import com.ote.file.api.exception.FileNotFoundException;
import com.ote.file.api.exception.UnauthorizedException;
import com.ote.file.api.model.File;
import com.ote.file.api.model.Folder;
import com.ote.file.service.mock.FileRepositoryMock;
import com.ote.file.service.mock.FileRestControllerMock;
import com.ote.file.service.mock.UserRightRepositoryMock;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static com.ote.file.spi.IUserRightRepository.Privilege;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FileServiceRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileControllerTest {

    private static final String APPLICATION = "myApp";
    private static final String PERIMETER = "myPerimeter";
    private static final Folder FOLDER = new Folder("./target/test");

    @Autowired
    private FileRestControllerMock fileRestController;

    @Autowired
    private UserRightRepositoryMock userRightRepository;

    @Autowired
    private FileRepositoryMock fileRepository;

    @Before
    public void setUp() throws Exception {

        String folder = FOLDER.getPath();
        if (!Files.exists(Paths.get(folder))) {
            Files.createDirectory(Paths.get(folder));
        }
    }

    @After
    public void tearDown() throws Exception {

        String folder = FOLDER.getPath();
        if (Files.exists(Paths.get(folder))) {
            Files.list(Paths.get(folder)).forEach(p -> deleteFile(p));
        }

        userRightRepository.reset();
        fileRepository.reset();
    }

    @Test(expected = UnauthorizedException.class)
    public void testGetFoldersUnauthorized() throws Exception {
        String user = "any";
        fileRestController.getFolders(user, APPLICATION, PERIMETER);
    }

    @Test
    public void testGetFolders() throws Exception {

        String user = "any";

        userRightRepository.add(user, APPLICATION, PERIMETER, Privilege.READ);
        fileRepository.add(APPLICATION, PERIMETER, FOLDER);

        Set<String> folders = fileRestController.getFolders(user, APPLICATION, PERIMETER);
        Assertions.assertThat(folders).contains(FOLDER.getPath());
    }

    @Test
    public void testGetFiles() throws Exception {

        String user = "any";
        File file = new File(FOLDER, "test1.txt");
        String content = "test1";

        Utils.write(file.getPath(), content.getBytes());

        userRightRepository.add(user, APPLICATION, PERIMETER, Privilege.READ);
        fileRepository.add(APPLICATION, PERIMETER, FOLDER);

        Set<String> files = fileRestController.getFiles(user, APPLICATION, PERIMETER, FOLDER.getPath());
        Assertions.assertThat(files).contains(file.getPath());
    }

    @Test
    public void testRead() throws Exception {

        String user = "any";
        File file = new File(FOLDER, "test1.txt");
        String content = "test1";

        Utils.write(file.getPath(), content.getBytes());

        userRightRepository.add(user, APPLICATION, PERIMETER, Privilege.READ);
        fileRepository.add(APPLICATION, PERIMETER, FOLDER);

        byte[] actualContent = fileRestController.readFile(user, APPLICATION, PERIMETER, file);
        Assertions.assertThat(new String(actualContent)).isEqualTo(content);
    }

    @Test
    public void testCreate() throws Exception {

        String user = "any";
        File file = new File(FOLDER, "test1.txt");
        String content = "test1";

        userRightRepository.add(user, APPLICATION, PERIMETER, Privilege.WRITE);
        fileRepository.add(APPLICATION, PERIMETER, FOLDER);

        fileRestController.createOrReplaceFile(user, APPLICATION, PERIMETER, file, content.getBytes(), true);

        byte[] actualContent = Utils.readFile(file.getPath());
        Assertions.assertThat(new String(actualContent)).isEqualTo(content);
    }

    @Test(expected = FileFoundException.class)
    public void testCreate_DoNotReplaceIfFound() throws Exception {

        String user = "any";
        File file = new File(FOLDER, "test1.txt");
        String content = "test1";

        Utils.write(file.getPath(), content.getBytes());

        userRightRepository.add(user, APPLICATION, PERIMETER, Privilege.WRITE);
        fileRepository.add(APPLICATION, PERIMETER, FOLDER);

        fileRestController.createOrReplaceFile(user, APPLICATION, PERIMETER, file, content.getBytes(), false);
    }

    @Test
    public void testCreate_ReplaceIfFound() throws Exception {

        String user = "any";
        File file = new File(FOLDER, "test1.txt");
        String oldContent = "oldContent";

        Utils.write(file.getPath(), oldContent.getBytes());

        String newContent = "newContent";

        userRightRepository.add(user, APPLICATION, PERIMETER, Privilege.WRITE);
        fileRepository.add(APPLICATION, PERIMETER, FOLDER);

        fileRestController.createOrReplaceFile(user, APPLICATION, PERIMETER, file, newContent.getBytes(), true);

        byte[] actualContent = Utils.readFile(file.getPath());
        Assertions.assertThat(new String(actualContent)).isEqualTo(newContent);
    }

    @Test
    public void testAppend() throws Exception {

        String user = "any";
        File file = new File(FOLDER, "test1.txt");
        String oldContent = "oldContent";

        Utils.write(file.getPath(), oldContent.getBytes());

        String newContent = "newContent";

        userRightRepository.add(user, APPLICATION, PERIMETER, Privilege.WRITE);
        fileRepository.add(APPLICATION, PERIMETER, FOLDER);

        fileRestController.appendFile(user, APPLICATION, PERIMETER, file, newContent.getBytes(), true);

        byte[] actualContent = Utils.readFile(file.getPath());
        Assertions.assertThat(new String(actualContent)).isEqualTo(oldContent+newContent);
    }

    @Test(expected = FileNotFoundException.class)
    public void testAppend_DoNotCreateIfNotFound() throws Exception {

        String user = "any";
        File file = new File(FOLDER, "test1.txt");
        String newContent = "newContent";

        userRightRepository.add(user, APPLICATION, PERIMETER, Privilege.WRITE);
        fileRepository.add(APPLICATION, PERIMETER, FOLDER);

        fileRestController.appendFile(user, APPLICATION, PERIMETER, file, newContent.getBytes(), false);
    }

    @Test
    public void testAppend_CreateIfNotFound() throws Exception {

        String user = "any";
        File file = new File(FOLDER, "test1.txt");
        String newContent = "newContent";

        userRightRepository.add(user, APPLICATION, PERIMETER, Privilege.WRITE);
        fileRepository.add(APPLICATION, PERIMETER, FOLDER);

        fileRestController.appendFile(user, APPLICATION, PERIMETER, file, newContent.getBytes(), true);

        byte[] actualContent = Utils.readFile(file.getPath());
        Assertions.assertThat(new String(actualContent)).isEqualTo(newContent);
    }

    private static void deleteFile(Path path) {
        try {
            Files.delete(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
