package application.auth;

import core.utils.HashUtils;

import java.util.Objects;

public class PasswordService {

    private static PasswordService instance;

    public static PasswordService getInstance() {
        if (instance == null) {
            synchronized (PasswordService.class) {
                if (instance == null) {
                    instance = new PasswordService();
                }
            }
        }
        return instance;
    }

    /**
     * Hashes a plaintext password for storage.
     */
    public String hash(final String plainPassword) {
        return HashUtils.md5(plainPassword);
    }

    /**
     * Compares a plaintext password against a previously hashed one.
     */
    public boolean matches(final String plainPassword, final String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        return Objects.equals(hash(plainPassword), hashedPassword);
    }

}
