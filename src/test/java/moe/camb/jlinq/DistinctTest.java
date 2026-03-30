package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DistinctTest {

    @Test
    void distinct_shouldRemoveDuplicates() {
        List<Integer> result = Linq.from(List.of(1, 2, 2, 3, 3, 3))
                .distinct().toList();
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void distinct_noDuplicates() {
        List<Integer> result = Linq.from(List.of(1, 2, 3))
                .distinct().toList();
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void distinct_allSame() {
        List<Integer> result = Linq.from(List.of(5, 5, 5, 5))
                .distinct().toList();
        assertEquals(List.of(5), result);
    }

    @Test
    void distinct_empty() {
        List<Integer> result = Linq.from(List.<Integer>of())
                .distinct().toList();
        assertTrue(result.isEmpty());
    }

    @Test
    void distinct_preservesOrder() {
        List<String> result = Linq.from(List.of("banana", "apple", "banana", "cherry", "apple"))
                .distinct().toList();
        assertEquals(List.of("banana", "apple", "cherry"), result);
    }

    @Test
    void distinct_afterSelect() {
        List<Integer> result = Linq.from(List.of("a", "bb", "c", "dd", "eee"))
                .select(String::length)
                .distinct().toList();
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void distinct_count() {
        int count = Linq.from(List.of(1, 1, 2, 2, 3, 3)).distinct().count();
        assertEquals(3, count);
    }

    @Test
    void distinct_isLazy() {
        List<String> visited = new ArrayList<>();
        Enumerable<Integer> lazy = Linq.from(List.of(1, 2, 2, 3))
                .where(x -> { visited.add("" + x); return true; })
                .distinct();
        assertTrue(visited.isEmpty());
        lazy.toList();
        assertFalse(visited.isEmpty());
    }
}
