package core.utils;

import java.time.Duration;

public class StringUtils {

    public static Duration parseDuration(final String value) throws ArithmeticException {
        if (value == null) return null;
        final String lower = value.trim().toLowerCase();
        final String text = lower.substring(0, lower.length() - 1);
        if (lower.endsWith("ms")) {
            return Duration.ofMillis(Long.parseLong(text));
        } else if (lower.endsWith("s")) {
            return Duration.ofSeconds(Long.parseLong(text));
        } else if (lower.endsWith("m")) {
            return Duration.ofMinutes(Long.parseLong(text));
        } else if (lower.endsWith("h")) {
            return Duration.ofHours(Long.parseLong(text));
        } else if (lower.endsWith("d")) {
            return Duration.ofDays(Long.parseLong(text));
        }
        throw new IllegalArgumentException("Invalid duration: " + value);
    }

}
