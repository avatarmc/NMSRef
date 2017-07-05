package com.avatarmc.ref.v1_9_R1.nms;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

public class RefLoadTest {
    private static final String NMS_PACKAGE = RefLoadTest.class.getPackage()
            .getName();
    private static final String NMS_SRC_DIR = "target/generated-sources/nms-ref/"
            + NMS_PACKAGE.replace('.', '/');

    @Test
    public void test() throws IOException {
        Files.list(new File(NMS_SRC_DIR).toPath()).forEach(this::test);
    }

    private void test(Path p) {
        String name = p.getFileName().toFile().getName();
        name = name.replace(".java", "");

        try {
            Class.forName(
                    NMS_PACKAGE + "." + name,
                    true,
                    getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
