package org.jd.gui.util.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class TextReaderTest {

    @Test
    void testGetText_simpleString() {
        String input = "Hello, World!";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        String result = TextReader.getText(is);
        assertEquals("Hello, World!", result);
    }

    @Test
    void testGetText_emptyInput() {
        InputStream is = new ByteArrayInputStream(new byte[0]);

        String result = TextReader.getText(is);
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    void testGetText_multilineInput() {
        String input = "Line 1\nLine 2\nLine 3";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        String result = TextReader.getText(is);
        assertEquals("Line 1\nLine 2\nLine 3", result);
    }

    @Test
    void testGetText_largeInput() {
        // Create a string larger than the internal buffer (8192 chars)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append('A');
        }
        String input = sb.toString();
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        String result = TextReader.getText(is);
        assertEquals(10000, result.length());
        assertEquals(input, result);
    }

    @Test
    void testGetText_unicodeContent() {
        String input = "Unicode: \u00e9\u00e0\u00fc\u00f1 \u4e16\u754c";
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        String result = TextReader.getText(is);
        assertEquals(input, result);
    }
}
