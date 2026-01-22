package core.utils;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FileUtils {

    private static final Set<String> _set = new HashSet<>();

    static {
        _set.add("text/plain");
        _set.add("text/html");
        _set.add("text/css");
        _set.add("text/javascript");
        _set.add("text/csv");
        _set.add("text/xml");
        _set.add("application/json");
        _set.add("application/xml");
        _set.add("application/xhtml+xml");
        _set.add("application/javascript");
        _set.add("application/x-www-form-urlencoded");
        _set.add("application/sql");
        _set.add("message/rfc822");
        _set.add("message/http");
    }

    public static String getExtension(final String filename) {
        Objects.requireNonNull(filename, "Filename must not be null");
        final int dotIndex = filename.lastIndexOf(".");
        return (dotIndex > 0) ? filename.substring(dotIndex + 1) : null;
    }

    public static String getBasename(final String filename) {
        Objects.requireNonNull(filename, "Filename must not be null");
        final int dotIndex = filename.lastIndexOf(".");
        return (dotIndex > 0) ? filename.substring(0, dotIndex) : filename;
    }

    public static String getContentType(final String filename) {
        if (filename != null) {
            final String v = URLConnection.guessContentTypeFromName(filename);
            if (v != null) return v;

            try {
                final String s = Files.probeContentType(Paths.get(filename));
                if (s != null) return s;
            } catch (IOException ignored) {
                // silent
            }
        }
        return null;
    }

    public static boolean isText(final String contentType) {
        if (contentType == null) return false;
        return _set.contains(contentType);
    }

}
