package core.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FileUtilsTest {

    @Test
    public void testGetContentType() {
        final String filename = "test.txt";
        final String contentType = FileUtils.getContentType(filename);
        assertEquals("text/plain", contentType);
    }

}
