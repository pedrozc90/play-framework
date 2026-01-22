package core.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

    public static Timestamp toTimestamp(final Instant value) {
        if (value == null) return null;
        return new Timestamp(value.toEpochMilli());
    }

    public static Instant toInstant(final Timestamp value) {
        if (value == null) return null;
        return value.toInstant();
    }

    public static Instant toInstant(final String value) {
        if (value == null) return null;
        return Instant.parse(value);
    }

    public static String toISOString(final Instant value) {
        if (value == null) return null;
        return formatter.format(value);
    }

}
