package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SelectManyTest {

    @Test
    void selectMany_basic() {
        List<Integer> result = Linq.from(List.of(
                        List.of(1, 2),
                        List.of(3, 4),
                        List.of(5)
                ))
                .selectMany(x -> x)
                .toList();
        assertEquals(List.of(1, 2, 3, 4, 5), result);
    }

    @Test
    void selectMany_strings() {
        List<Character> result = Linq.from(List.of("ab", "cd"))
                .selectMany(s -> {
                    List<Character> chars = new ArrayList<>();
                    for (char c : s.toCharArray()) {
                        chars.add(c);
                    }
                    return chars;
                })
                .toList();
        assertEquals(List.of('a', 'b', 'c', 'd'), result);
    }

    @Test
    void selectMany_empty() {
        List<Integer> result = Linq.from(List.<List<Integer>>of())
                .selectMany(x -> x)
                .toList();
        assertTrue(result.isEmpty());
    }

    @Test
    void selectMany_someEmpty() {
        List<Integer> result = Linq.from(List.of(
                        List.of(1, 2),
                        List.<Integer>of(),
                        List.of(3)
                ))
                .selectMany(x -> x)
                .toList();
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void selectMany_allEmpty() {
        List<Integer> result = Linq.from(List.of(
                        List.<Integer>of(),
                        List.<Integer>of()
                ))
                .selectMany(x -> x)
                .toList();
        assertTrue(result.isEmpty());
    }

    @Test
    void selectMany_withLinqFrom() {
        List<Integer> result = Linq.from(List.of(
                        List.of(1, 2, 3),
                        List.of(4, 5, 6)
                ))
                .selectMany(list -> Linq.from(list).where(x -> x % 2 == 0))
                .toList();
        assertEquals(List.of(2, 4, 6), result);
    }

    @Test
    void selectMany_afterWhere() {
        List<Integer> result = Linq.from(List.of(
                        List.of(1, 2),
                        List.of(3, 4),
                        List.of(5, 6)
                ))
                .where(list -> list.contains(3) || list.contains(5))
                .selectMany(x -> x)
                .toList();
        assertEquals(List.of(3, 4, 5, 6), result);
    }

    @Test
    void selectMany_then_where() {
        List<Integer> result = Linq.from(List.of(
                        List.of(1, 2, 3),
                        List.of(4, 5, 6)
                ))
                .selectMany(x -> x)
                .where(x -> x > 3)
                .toList();
        assertEquals(List.of(4, 5, 6), result);
    }

    @Test
    void selectMany_then_distinct() {
        List<Integer> result = Linq.from(List.of(
                        List.of(1, 2, 3),
                        List.of(2, 3, 4)
                ))
                .selectMany(x -> x)
                .distinct()
                .toList();
        assertEquals(List.of(1, 2, 3, 4), result);
    }

    @Test
    void selectMany_count() {
        int count = Linq.from(List.of(
                        List.of(1, 2),
                        List.of(3),
                        List.of(4, 5, 6)
                ))
                .selectMany(x -> x)
                .count();
        assertEquals(6, count);
    }

    @Test
    void selectMany_isLazy() {
        List<String> visited = new ArrayList<>();
        Enumerable<Integer> lazy = Linq.from(List.of(
                        List.of(1, 2),
                        List.of(3, 4)
                ))
                .selectMany(list -> {
                    visited.add("expand:" + list);
                    return list;
                });

        assertTrue(visited.isEmpty());
        lazy.toList();
        assertFalse(visited.isEmpty());
    }
}
