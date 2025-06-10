package core.utils;

import java.util.Objects;

public class FileUtils {

    private static FileUtils instance;

    public static FileUtils getInstance() {
        if (instance == null) {
            instance = new FileUtils();
        }
        return instance;
    }

    public String getExtension(final String filename) {
        Objects.requireNonNull(filename, "Filename must not be null");
        final int dotIndex = filename.lastIndexOf(".");
        return (dotIndex > 0) ? filename.substring(dotIndex + 1) : null;
    }

    public String getBasename(final String filename) {
        Objects.requireNonNull(filename, "Filename must not be null");
        final int dotIndex = filename.lastIndexOf(".");
        return (dotIndex > 0) ? filename.substring(0,dotIndex) : filename;
    }

}
