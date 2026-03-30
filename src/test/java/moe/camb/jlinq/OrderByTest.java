package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderByTest {

    @Test
    void orderBy_integers() {
        List<Integer> result = Linq.from(List.of(3, 1, 4, 1, 5)).orderBy(x -> x).toList();
        assertEquals(List.of(1, 1, 3, 4, 5), result);
    }

    @Test
    void orderBy_strings() {
        List<String> result = Linq.from(List.of("banana", "apple", "cherry")).orderBy(x -> x).toList();
        assertEquals(List.of("apple", "banana", "cherry"), result);
    }

    @Test
    void orderBy_byProperty() {
        List<String> result = Linq.from(List.of("banana", "hi", "apple")).orderBy(String::length).toList();
        assertEquals(List.of("hi", "apple", "banana"), result);
    }

    @Test
    void orderBy_empty() {
        List<Integer> result = Linq.from(List.<Integer>of()).orderBy(x -> x).toList();
        assertTrue(result.isEmpty());
    }

    @Test
    void orderBy_singleElement() {
        List<Integer> result = Linq.from(List.of(42)).orderBy(x -> x).toList();
        assertEquals(List.of(42), result);
    }

    @Test
    void orderBy_alreadySorted() {
        List<Integer> result = Linq.from(List.of(1, 2, 3)).orderBy(x -> x).toList();
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void orderByDescending_integers() {
        List<Integer> result = Linq.from(List.of(3, 1, 4, 1, 5)).orderByDescending(x -> x).toList();
        assertEquals(List.of(5, 4, 3, 1, 1), result);
    }

    @Test
    void orderByDescending_strings() {
        List<String> result = Linq.from(List.of("banana", "apple", "cherry")).orderByDescending(x -> x).toList();
        assertEquals(List.of("cherry", "banana", "apple"), result);
    }

    @Test
    void orderBy_withComparator() {
        List<String> result = Linq.from(List.of("banana", "hi", "apple")).orderBy(Comparator.comparingInt(String::length)).toList();
        assertEquals(List.of("hi", "apple", "banana"), result);
    }

    @Test
    void orderBy_thenBy() {
        List<String> result = Linq.from(List.of("bb", "aa", "ccc", "a", "cc")).orderBy(String::length).thenBy(x -> x).toList();
        assertEquals(List.of("a", "aa", "bb", "cc", "ccc"), result);
    }

    @Test
    void orderBy_thenByDescending() {
        List<String> result = Linq.from(List.of("bb", "aa", "ccc", "a", "cc"))
                .orderBy(String::length)
                .thenByDescending(x -> x)
                .toList();
        assertEquals(List.of("a", "cc", "bb", "aa", "ccc"), result);
    }

    @Test
    void orderBy_thenBy_multiple() {
        List<String> source = List.of("b2", "a1", "a2", "b1", "a1");
        List<String> result = Linq.from(source).orderBy(s -> s.charAt(0))
                .thenBy(s -> s.charAt(1))
                .thenBy(s -> s)
                .toList();
        assertEquals(List.of("a1", "a1", "a2", "b1", "b2"), result);
    }

    @Test
    void orderBy_thenBy_withComparator() {
        List<String> result = Linq.from(List.of("bb", "aa", "ccc", "a", "cc")).orderBy(String::length).thenBy(Comparator.naturalOrder()).toList();
        assertEquals(List.of("a", "aa", "bb", "cc", "ccc"), result);
    }

    @Test
    void orderBy_isLazy() {
        List<String> visited = new ArrayList<>();
        Enumerable<Integer> lazy = Linq.from(List.of(3, 1, 2)).where(x -> {
            visited.add("" + x);
            return true;
        }).orderBy(x -> x);

        assertTrue(visited.isEmpty());

        lazy.toList();
        assertFalse(visited.isEmpty());
    }

    @Test
    void orderBy_multipleIteration() {
        Enumerable<Integer> query = Linq.from(List.of(3, 1, 2)).orderBy(x -> x);

        assertEquals(List.of(1, 2, 3), query.toList());
        assertEquals(List.of(1, 2, 3), query.toList());
    }

    @Test
    void orderBy_isStable() {
        List<String> result = Linq.from(List.of("bb", "aa", "bc", "ab")).orderBy(s -> s.charAt(0))
                .toList();
        assertEquals(List.of("aa", "ab", "bb", "bc"), result);
    }
}
