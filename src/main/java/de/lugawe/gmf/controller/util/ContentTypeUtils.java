package de.lugawe.gmf.controller.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ContentTypeUtils {

    private ContentTypeUtils() {}

    public static String getContentType(String path) {
        try {
            return Files.probeContentType(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
