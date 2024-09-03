package com.SuperBank.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.File;
import java.io.IOException;

@SpringBootTest
public class FilePermissionTest {

    @Test
    public void testFilePermissions() throws IOException {
        String tempDirectory = "C:\\Users\\luizc\\Documents\\temp";
        File tempDir = new File(tempDirectory);

        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        File testFile = new File(tempDir, "testFile.txt");
        boolean created = testFile.createNewFile();
        if (created) {
            boolean canWrite = testFile.canWrite();
            boolean canRead = testFile.canRead();
            boolean canDelete = testFile.delete();

            assert canWrite : "No write permission on file";
            assert canRead : "No read permission on file";
            assert canDelete : "File cannot be deleted";
        } else {
            assert false : "Failed to create test file";
        }
    }
}
