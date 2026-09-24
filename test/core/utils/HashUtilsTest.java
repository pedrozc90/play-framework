package core.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HashUtilsTest {

    @Test
    public void md5MatchesKnownDigest() {
        assertEquals("5f4dcc3b5aa765d61d8327deb882cf99", HashUtils.md5("password"));
    }

    @Test
    public void md5IsDeterministicForSameInput() {
        final String value = "regression-check";
        assertEquals(HashUtils.md5(value), HashUtils.md5(value));
    }

}
