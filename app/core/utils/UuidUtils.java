package core.utils;

import java.util.UUID;

public class UuidUtils {

    public static UUID toUuid(final String value) {
        return value == null ? null : UUID.fromString(value);
    }

    public static String toUuidString(final UUID value) {
        return value == null ? null : value.toString();
    }

}
