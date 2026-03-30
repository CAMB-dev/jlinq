package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TakeWhileTest {

    @Test
    void takeWhile_shouldTakeWhileConditionMet() {
        List<Integer> result = Linq.from(List.of(1, 2, 3, 4, 5))
                .takeWhile(x -> x < 4)
                .toList();
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void takeWhile_stopsAtFirstFailure() {
        List<Integer> result = Linq.from(List.of(1, 5, 2, 4, 3))
                .takeWhile(x -> x < 3)
                .toList();
        assertEquals(List.of(1), result);
    }

    @Test
    void takeWhile_allMatch() {
        List<Integer> result = Linq.from(List.of(1, 2, 3))
                .takeWhile(x -> x < 100)
                .toList();
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void takeWhile_noneMatch() {
        List<Integer> result = Linq.from(List.of(5, 6, 7))
                .takeWhile(x -> x < 3)
                .toList();
        assertTrue(result.isEmpty());
    }

    @Test
    void takeWhile_empty() {
        List<Integer> result = Linq.from(List.<Integer>of())
                .takeWhile(x -> x < 3)
                .toList();
        assertTrue(result.isEmpty());
    }

    @Test
    void takeWhile_isLazy() {
        List<String> visited = new ArrayList<>();
        List<Integer> result = Linq.from(List.of(1, 2, 3, 4, 5))
                .takeWhile(x -> {
                    visited.add("" + x);
                    return x < 3;
                })
                .toList();
        assertEquals(List.of(1, 2), result);
        assertEquals(List.of("1", "2", "3"), visited);
    }

    @Test
    void takeWhile_afterWhere() {
        List<Integer> result = Linq.from(List.of(2, 4, 6, 1, 8, 10))
                .where(x -> x % 2 == 0)
                .takeWhile(x -> x < 8)
                .toList();
        assertEquals(List.of(2, 4, 6), result);
    }

    @Test
    void takeWhile_vs_where_difference() {
        List<Integer> source = List.of(1, 2, 5, 3, 4);

        List<Integer> whereResult = Linq.from(source)
                .where(x -> x < 5).toList();
        assertEquals(List.of(1, 2, 3, 4), whereResult);

        List<Integer> takeWhileResult = Linq.from(source)
                .takeWhile(x -> x < 5).toList();
        assertEquals(List.of(1, 2), takeWhileResult);
    }
}
