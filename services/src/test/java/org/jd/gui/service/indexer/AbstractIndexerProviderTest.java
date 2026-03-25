package org.jd.gui.service.indexer;

import org.jd.gui.api.API;
import org.jd.gui.api.model.Container;
import org.jd.gui.api.model.Indexes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AbstractIndexerProviderTest {

    @Mock
    private API api;

    @Mock
    private Container.Entry entry;

    @Mock
    private Indexes indexes;

    /**
     * Concrete subclass for testing the abstract class methods.
     */
    private static class TestIndexerProvider extends AbstractIndexerProvider {
        TestIndexerProvider() {
            // Default constructor loads properties (none found for test class)
        }

        @Override
        public String[] getSelectors() {
            return appendSelectors("*:file:*.test");
        }

        @Override
        public void index(API api, Container.Entry entry, Indexes indexes) {
            // No-op for testing
        }

        // Expose protected methods for testing
        public String[] testAppendSingleSelector(String selector) {
            return appendSelectors(selector);
        }

        public String[] testAppendMultipleSelectors(String... selectors) {
            return appendSelectors(selectors);
        }
    }

    @Test
    void testAppendSelectors_singleSelector_noExternalSelectors() {
        TestIndexerProvider provider = new TestIndexerProvider();

        String[] selectors = provider.testAppendSingleSelector("*:file:*.java");
        assertNotNull(selectors);
        assertEquals(1, selectors.length);
        assertEquals("*:file:*.java", selectors[0]);
    }

    @Test
    void testAppendSelectors_multipleSelectors_noExternalSelectors() {
        TestIndexerProvider provider = new TestIndexerProvider();

        String[] selectors = provider.testAppendMultipleSelectors("*:file:*.java", "*:file:*.class");
        assertNotNull(selectors);
        assertEquals(2, selectors.length);
        assertEquals("*:file:*.java", selectors[0]);
        assertEquals("*:file:*.class", selectors[1]);
    }

    @Test
    void testGetSelectors_returnsExpectedSelector() {
        TestIndexerProvider provider = new TestIndexerProvider();

        String[] selectors = provider.getSelectors();
        assertNotNull(selectors);
        assertEquals(1, selectors.length);
        assertEquals("*:file:*.test", selectors[0]);
    }

    @Test
    void testGetPathPattern_defaultIsNull() {
        TestIndexerProvider provider = new TestIndexerProvider();

        Pattern pattern = provider.getPathPattern();
        assertNull(pattern, "Path pattern should be null when no external properties file is found");
    }

    @Test
    void testInit_withProperties() {
        TestIndexerProvider provider = new TestIndexerProvider();

        // Manually initialize with properties that have selectors and pathRegExp
        java.util.Properties props = new java.util.Properties();
        props.setProperty("selectors", "ext1,ext2");
        props.setProperty("pathRegExp", ".*\\.txt$");
        provider.init(props);

        // After init with external selectors, appendSelectors should prepend them
        String[] selectors = provider.testAppendSingleSelector("*:file:*.test");
        assertEquals(3, selectors.length);
        assertEquals("ext1", selectors[0]);
        assertEquals("ext2", selectors[1]);
        assertEquals("*:file:*.test", selectors[2]);

        // Path pattern should now be set
        Pattern pathPattern = provider.getPathPattern();
        assertNotNull(pathPattern);
        assertTrue(pathPattern.matcher("somefile.txt").matches());
        assertFalse(pathPattern.matcher("somefile.java").matches());
    }
}
