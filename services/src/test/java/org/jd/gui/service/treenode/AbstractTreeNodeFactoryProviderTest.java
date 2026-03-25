package org.jd.gui.service.treenode;

import org.jd.gui.api.API;
import org.jd.gui.api.feature.ContainerEntryGettable;
import org.jd.gui.api.feature.UriGettable;
import org.jd.gui.api.model.Container;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Properties;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AbstractTreeNodeFactoryProviderTest {

    @Mock
    private API api;

    @Mock
    private Container.Entry entry;

    /**
     * Concrete subclass for testing the abstract class methods.
     */
    private static class TestTreeNodeFactoryProvider extends AbstractTreeNodeFactoryProvider {
        TestTreeNodeFactoryProvider() {
            // Default constructor attempts to load properties (none found for test class)
        }

        @Override
        public String[] getSelectors() {
            return appendSelectors("*:file:*.test");
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends DefaultMutableTreeNode & ContainerEntryGettable & UriGettable> T make(API api, Container.Entry entry) {
            return null; // Not testing make() here
        }

        // Expose protected methods for testing
        public String[] testAppendSingleSelector(String selector) {
            return appendSelectors(selector);
        }

        public String[] testAppendMultipleSelectors(String... selectors) {
            return appendSelectors(selectors);
        }

        public void testInit(Properties properties) {
            init(properties);
        }
    }

    @Test
    void testAppendSelectors_singleSelector_noExternalSelectors() {
        TestTreeNodeFactoryProvider provider = new TestTreeNodeFactoryProvider();

        String[] selectors = provider.testAppendSingleSelector("*:dir:*");
        assertNotNull(selectors);
        assertEquals(1, selectors.length);
        assertEquals("*:dir:*", selectors[0]);
    }

    @Test
    void testAppendSelectors_multipleSelectors_noExternalSelectors() {
        TestTreeNodeFactoryProvider provider = new TestTreeNodeFactoryProvider();

        String[] selectors = provider.testAppendMultipleSelectors("*:file:*.java", "*:file:*.class");
        assertNotNull(selectors);
        assertEquals(2, selectors.length);
        assertEquals("*:file:*.java", selectors[0]);
        assertEquals("*:file:*.class", selectors[1]);
    }

    @Test
    void testGetPathPattern_defaultIsNull() {
        TestTreeNodeFactoryProvider provider = new TestTreeNodeFactoryProvider();

        Pattern pattern = provider.getPathPattern();
        assertNull(pattern, "Path pattern should be null when no external properties file is found");
    }

    @Test
    void testInit_withSelectorsAndPathRegExp() {
        TestTreeNodeFactoryProvider provider = new TestTreeNodeFactoryProvider();

        Properties props = new Properties();
        props.setProperty("selectors", "custom1,custom2,custom3");
        props.setProperty("pathRegExp", ".*\\.xml$");
        provider.testInit(props);

        // After init, appendSelectors should prepend external selectors
        String[] selectors = provider.testAppendSingleSelector("*:file:*.test");
        assertEquals(4, selectors.length);
        assertEquals("custom1", selectors[0]);
        assertEquals("custom2", selectors[1]);
        assertEquals("custom3", selectors[2]);
        assertEquals("*:file:*.test", selectors[3]);

        // Path pattern should match .xml files
        Pattern pathPattern = provider.getPathPattern();
        assertNotNull(pathPattern);
        assertTrue(pathPattern.matcher("config.xml").matches());
        assertFalse(pathPattern.matcher("config.json").matches());
    }

    @Test
    void testInit_withSelectorsOnly() {
        TestTreeNodeFactoryProvider provider = new TestTreeNodeFactoryProvider();

        Properties props = new Properties();
        props.setProperty("selectors", "selectorA,selectorB");
        provider.testInit(props);

        // External selectors should be set, but no path pattern
        String[] selectors = provider.testAppendMultipleSelectors("s1", "s2");
        assertEquals(4, selectors.length);
        assertEquals("selectorA", selectors[0]);
        assertEquals("selectorB", selectors[1]);
        assertEquals("s1", selectors[2]);
        assertEquals("s2", selectors[3]);

        assertNull(provider.getPathPattern());
    }
}
