package org.jd.gui.service.sourcesaver;

import org.jd.gui.api.model.Container;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectorySourceSaverProviderTest {

    @Mock
    private Container.Entry entry;

    @Test
    void testGetSelectors_containsDirSelector() {
        DirectorySourceSaverProvider provider = new DirectorySourceSaverProvider();

        String[] selectors = provider.getSelectors();
        assertNotNull(selectors);
        assertTrue(selectors.length >= 1);

        boolean found = false;
        for (String selector : selectors) {
            if ("*:dir:*".equals(selector)) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Selectors should contain '*:dir:*'");
    }

    @Test
    void testGetPathPattern_defaultIsNull() {
        DirectorySourceSaverProvider provider = new DirectorySourceSaverProvider();

        assertNull(provider.getPathPattern(),
                "Path pattern should be null when no external properties are configured");
    }

    @Test
    void testGetSourcePath_appendsSrcZip() {
        when(entry.getPath()).thenReturn("com/example/mypackage");

        DirectorySourceSaverProvider provider = new DirectorySourceSaverProvider();

        String sourcePath = provider.getSourcePath(entry);
        assertEquals("com/example/mypackage.src.zip", sourcePath);
    }

    @Test
    void testGetSourcePath_rootDirectory() {
        when(entry.getPath()).thenReturn("");

        DirectorySourceSaverProvider provider = new DirectorySourceSaverProvider();

        String sourcePath = provider.getSourcePath(entry);
        assertEquals(".src.zip", sourcePath);
    }

    @Test
    void testGetSourcePath_singleDirectory() {
        when(entry.getPath()).thenReturn("src");

        DirectorySourceSaverProvider provider = new DirectorySourceSaverProvider();

        String sourcePath = provider.getSourcePath(entry);
        assertEquals("src.src.zip", sourcePath);
    }
}
