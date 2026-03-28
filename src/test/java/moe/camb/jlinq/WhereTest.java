package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WhereTest {
    @Test
    void where_shouldFilterElements() {
        List<Integer> result = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> x > 3)
                .toList();
        assertEquals(List.of(4, 5), result);
    }

    @Test
    void where_noMatch() {
        List<Integer> result = Linq.from(List.of(1, 2, 3))
                .where(x -> x > 100)
                .toList();
        assertTrue(result.isEmpty());
    }

    @Test
    void where_allMatch() {
        List<Integer> result = Linq.from(List.of(1, 2, 3))
                .where(x -> x > 0)
                .toList();
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void where_chained() {
        List<Integer> result = Linq.from(List.of(1, 2, 3, 4, 5, 6))
                .where(x -> x > 2)
                .where(x -> x < 6)
                .toList();
        assertEquals(List.of(3, 4, 5), result);
    }
    @Test
    void where_indexed_shouldProvideIndex() {
        List<String> result = Linq.from(List.of("a", "b", "c", "d"))
                .where((item, i) -> i < 2)
                .toList();
        assertEquals(List.of("a", "b"), result);
    }

    @Test
    void where_indexed_filterByIndexAndValue() {
        List<String> result = Linq.from(List.of("apple", "banana", "cherry", "date", "elderberry"))
                .where((item, i) -> i % 2 == 0 && item.length() > 4)
                .toList();
        assertEquals(List.of("apple", "cherry", "elderberry"), result);
    }

    @Test
    void where_indexed_emptySource() {
        List<String> result = Linq.from(List.<String>of())
                .where((item, i) -> i < 5)
                .toList();
        assertTrue(result.isEmpty());
    }

    @Test
    void where_indexed_noMatch() {
        List<Integer> result = Linq.from(List.of(1, 2, 3))
                .where((item, i) -> i > 100)
                .toList();
        assertTrue(result.isEmpty());
    }

    @Test
    void where_indexed_allMatch() {
        List<Integer> result = Linq.from(List.of(1, 2, 3))
                .where((item, i) -> i >= 0)
                .toList();
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void where_indexed_indexStartsFromZero() {
        List<Integer> indices = new ArrayList<>();
        Linq.from(List.of("a", "b", "c"))
                .where((item, i) -> {
                    indices.add(i);
                    return true;
                })
                .toList();
        assertEquals(List.of(0, 1, 2), indices);
    }

    @Test
    void where_indexed_thenWhereIndexed_indexResetsPerOperator() {
        List<String> result = Linq.from(List.of("a", "b", "c", "d", "e"))
                .where((item, i) -> i < 4)
                .where((item, i) -> i % 2 == 0)
                .toList();
        assertEquals(List.of("a", "c"), result);
    }
}
