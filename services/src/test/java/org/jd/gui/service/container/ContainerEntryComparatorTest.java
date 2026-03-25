package org.jd.gui.service.container;

import org.jd.gui.api.model.Container;
import org.jd.gui.model.container.ContainerEntryComparator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContainerEntryComparatorTest {

    @Mock
    private Container.Entry entry1;

    @Mock
    private Container.Entry entry2;

    @Test
    void testDirectoryBeforeFile() {
        when(entry1.isDirectory()).thenReturn(true);
        when(entry2.isDirectory()).thenReturn(false);

        int result = ContainerEntryComparator.COMPARATOR.compare(entry1, entry2);
        assertTrue(result < 0, "Directory should come before file");
    }

    @Test
    void testFileAfterDirectory() {
        when(entry1.isDirectory()).thenReturn(false);
        when(entry2.isDirectory()).thenReturn(true);

        int result = ContainerEntryComparator.COMPARATOR.compare(entry1, entry2);
        assertTrue(result > 0, "File should come after directory");
    }

    @Test
    void testTwoDirectories_sortedByPath() {
        when(entry1.isDirectory()).thenReturn(true);
        when(entry2.isDirectory()).thenReturn(true);
        when(entry1.getPath()).thenReturn("aaa");
        when(entry2.getPath()).thenReturn("bbb");

        int result = ContainerEntryComparator.COMPARATOR.compare(entry1, entry2);
        assertTrue(result < 0, "Directory 'aaa' should come before directory 'bbb'");
    }

    @Test
    void testTwoFiles_sortedByPath() {
        when(entry1.isDirectory()).thenReturn(false);
        when(entry2.isDirectory()).thenReturn(false);
        when(entry1.getPath()).thenReturn("zebra.java");
        when(entry2.getPath()).thenReturn("alpha.java");

        int result = ContainerEntryComparator.COMPARATOR.compare(entry1, entry2);
        assertTrue(result > 0, "File 'zebra.java' should come after file 'alpha.java'");
    }

    @Test
    void testSamePathEntries() {
        when(entry1.isDirectory()).thenReturn(false);
        when(entry2.isDirectory()).thenReturn(false);
        when(entry1.getPath()).thenReturn("same/path.java");
        when(entry2.getPath()).thenReturn("same/path.java");

        int result = ContainerEntryComparator.COMPARATOR.compare(entry1, entry2);
        assertEquals(0, result, "Entries with the same path should be equal");
    }
}
