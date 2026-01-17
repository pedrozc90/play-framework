package core.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {

    public static final String MD5 = "MD5";
    public static final String SHA_256 = "SHA-256";

    private static byte[] hash(final byte[] value, final String algorithm) throws IllegalArgumentException {
        try {
            final MessageDigest md = MessageDigest.getInstance(algorithm);
            return md.digest(value);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Algorithm " + algorithm + " is not supported", e);
        }
    }

    private static String toHex(final byte[] bytes) {
        final StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String md5(final byte[] value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("Input value cannot be null");
        }
        final byte[] hash = hash(value, MD5);
        return toHex(hash);
    }

    public static String md5(final String value) throws IllegalArgumentException {
        final byte[] bytes = (value != null) ? value.getBytes(StandardCharsets.UTF_8) : null;
        return md5(bytes);
    }

    public static String sha256(final byte[] value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("Input value cannot be null");
        }
        final byte[] hash = hash(value, SHA_256);
        return toHex(hash);
    }

    public static String sha256(final String value) throws IllegalArgumentException {
        final byte[] bytes = (value != null) ? value.getBytes(StandardCharsets.UTF_8) : null;
        return sha256(bytes);
    }

}
