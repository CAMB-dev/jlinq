package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupByTest {

    @Test
    void groupBy_basic() {
        List<Grouping<Integer, String>> result = Linq.from(List.of("a", "bb", "c", "dd", "eee"))
                .groupBy(String::length)
                .toList();

        assertEquals(3, result.size());

        assertEquals(1, result.get(0).getKey());
        assertEquals(List.of("a", "c"), result.get(0).getElements());

        assertEquals(2, result.get(1).getKey());
        assertEquals(List.of("bb", "dd"), result.get(1).getElements());

        assertEquals(3, result.get(2).getKey());
        assertEquals(List.of("eee"), result.get(2).getElements());
    }

    @Test
    void groupBy_preservesKeyOrder() {
        List<Grouping<String, String>> result =
                Linq.from(List.of("banana", "apple", "blueberry", "avocado", "cherry"))
                        .groupBy(s -> String.valueOf(s.charAt(0)))
                        .toList();

        assertEquals("b", result.get(0).getKey());
        assertEquals("a", result.get(1).getKey());
        assertEquals("c", result.get(2).getKey());
    }

    @Test
    void groupBy_preservesElementOrder() {
        List<Grouping<Integer, String>> result =
                Linq.from(List.of("bb", "a", "cc", "d", "eee"))
                        .groupBy(String::length)
                        .toList();

        Grouping<Integer, String> len2 = result.stream()
                .filter(g -> g.getKey() == 2)
                .findFirst().orElseThrow();
        assertEquals(List.of("bb", "cc"), len2.getElements());
    }

    @Test
    void groupBy_empty() {
        List<Grouping<Integer, String>> result = Linq.from(List.<String>of())
                .groupBy(String::length)
                .toList();
        assertTrue(result.isEmpty());
    }

    @Test
    void groupBy_singleGroup() {
        List<Grouping<Integer, Integer>> result = Linq.from(List.of(2, 4, 6))
                .groupBy(x -> x % 2)
                .toList();
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getKey());
        assertEquals(List.of(2, 4, 6), result.get(0).getElements());
    }

    @Test
    void groupBy_allDifferent() {
        List<Grouping<Integer, Integer>> result = Linq.from(List.of(1, 2, 3))
                .groupBy(x -> x)
                .toList();
        assertEquals(3, result.size());
    }

    @Test
    void groupBy_then_select() {
        List<String> result = Linq.from(List.of("a", "bb", "c", "dd"))
                .groupBy(String::length)
                .select(g -> g.getKey() + ":" + g.getElements().size())
                .toList();
        assertEquals(List.of("1:2", "2:2"), result);
    }

    @Test
    void groupBy_then_where() {
        List<Grouping<Integer, String>> result =
                Linq.from(List.of("a", "bb", "c", "dd", "eee"))
                        .groupBy(String::length)
                        .where(g -> g.getElements().size() > 1)
                        .toList();
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getKey());
        assertEquals(2, result.get(1).getKey());
    }

    @Test
    void groupBy_then_count() {
        int count = Linq.from(List.of("a", "bb", "c", "dd", "eee"))
                .groupBy(String::length)
                .count();
        assertEquals(3, count);
    }

    @Test
    void groupBy_then_first() {
        Grouping<Integer, String> first = Linq.from(List.of("a", "bb", "c"))
                .groupBy(String::length)
                .first();
        assertEquals(1, first.getKey());
        assertEquals(List.of("a", "c"), first.getElements());
    }

    @Test
    void groupBy_asEnumerable() {
        List<String> result = Linq.from(List.of("apple", "avocado", "banana", "blueberry"))
                .groupBy(s -> s.charAt(0))
                .select(g -> g.asEnumerable()
                        .select(s -> s.toUpperCase())
                        .first())
                .toList();
        assertEquals(List.of("APPLE", "BANANA"), result);
    }

    @Test
    void groupBy_isLazy() {
        List<String> visited = new ArrayList<>();
        Enumerable<Grouping<Integer, Integer>> lazy = Linq.from(List.of(1, 2, 3))
                .where(x -> {
                    visited.add("" + x);
                    return true;
                })
                .groupBy(x -> x % 2);

        assertTrue(visited.isEmpty());
        lazy.toList();
        assertFalse(visited.isEmpty());
    }

    @Test
    void groupBy_afterWhere() {
        List<Grouping<Integer, Integer>> result = Linq.from(List.of(1, 2, 3, 4, 5, 6))
                .where(x -> x > 2)
                .groupBy(x -> x % 2)
                .toList();

        assertEquals(2, result.size());
        Grouping<Integer, Integer> odd = result.stream()
                .filter(g -> g.getKey() == 1)
                .findFirst().orElseThrow();
        assertEquals(List.of(3, 5), odd.getElements());
    }
}
