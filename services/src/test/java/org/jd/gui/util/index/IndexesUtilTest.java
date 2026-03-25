package org.jd.gui.util.index;

import org.jd.gui.api.model.Container;
import org.jd.gui.api.model.Indexes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IndexesUtilTest {

    @Mock
    private Indexes indexes;

    @Mock
    private Container.Entry entry;

    @Test
    void testContains_found() {
        Map<String, Collection<?>> index = new HashMap<>();
        index.put("java/lang/String", Collections.singletonList(entry));
        when(indexes.getIndex("typeDeclarations")).thenReturn(index);

        Future<Indexes> future = CompletableFuture.completedFuture(indexes);
        Collection<Future<Indexes>> futures = Collections.singletonList(future);

        assertTrue(IndexesUtil.contains(futures, "typeDeclarations", "java/lang/String"));
    }

    @Test
    void testContains_notFound() {
        Map<String, Collection<?>> index = new HashMap<>();
        index.put("java/lang/String", Collections.singletonList(entry));
        when(indexes.getIndex("typeDeclarations")).thenReturn(index);

        Future<Indexes> future = CompletableFuture.completedFuture(indexes);
        Collection<Future<Indexes>> futures = Collections.singletonList(future);

        assertFalse(IndexesUtil.contains(futures, "typeDeclarations", "java/lang/Integer"));
    }

    @Test
    void testContainsInternalTypeName_delegates() {
        Map<String, Collection<?>> index = new HashMap<>();
        index.put("com/example/MyClass", Collections.singletonList(entry));
        when(indexes.getIndex("typeDeclarations")).thenReturn(index);

        Future<Indexes> future = CompletableFuture.completedFuture(indexes);
        Collection<Future<Indexes>> futures = Collections.singletonList(future);

        assertTrue(IndexesUtil.containsInternalTypeName(futures, "com/example/MyClass"));
        assertFalse(IndexesUtil.containsInternalTypeName(futures, "com/example/OtherClass"));
    }

    @Test
    void testFind_returnsEntries() {
        List<Container.Entry> entryList = new ArrayList<>();
        entryList.add(entry);
        Map<String, Collection<?>> index = new HashMap<>();
        index.put("java/lang/String", entryList);
        when(indexes.getIndex("typeDeclarations")).thenReturn(index);

        Future<Indexes> future = CompletableFuture.completedFuture(indexes);
        Collection<Future<Indexes>> futures = Collections.singletonList(future);

        List<Container.Entry> result = IndexesUtil.find(futures, "typeDeclarations", "java/lang/String");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(entry, result.get(0));
    }

    @Test
    void testFind_emptyWhenKeyMissing() {
        Map<String, Collection<?>> index = new HashMap<>();
        when(indexes.getIndex("typeDeclarations")).thenReturn(index);

        Future<Indexes> future = CompletableFuture.completedFuture(indexes);
        Collection<Future<Indexes>> futures = Collections.singletonList(future);

        List<Container.Entry> result = IndexesUtil.find(futures, "typeDeclarations", "nonexistent/Type");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testContains_nullIndex() {
        when(indexes.getIndex("typeDeclarations")).thenReturn(null);

        Future<Indexes> future = CompletableFuture.completedFuture(indexes);
        Collection<Future<Indexes>> futures = Collections.singletonList(future);

        assertFalse(IndexesUtil.contains(futures, "typeDeclarations", "java/lang/String"));
    }
}
