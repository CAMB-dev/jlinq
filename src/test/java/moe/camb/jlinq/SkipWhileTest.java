package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SkipWhileTest {

    @Test
    void skipWhile_shouldSkipWhileConditionMet() {
        List<Integer> result = Linq.from(List.of(1, 2, 3, 4, 5))
                .skipWhile(x -> x < 3)
                .toList();
        assertEquals(List.of(3, 4, 5), result);
    }

    @Test
    void skipWhile_takesRemainingEvenIfConditionMetAgain() {
        List<Integer> result = Linq.from(List.of(1, 2, 5, 1, 6))
                .skipWhile(x -> x < 3)
                .toList();
        assertEquals(List.of(5, 1, 6), result);
    }

    @Test
    void skipWhile_allMatch() {
        List<Integer> result = Linq.from(List.of(1, 2, 3))
                .skipWhile(x -> x < 100)
                .toList();
        assertTrue(result.isEmpty());
    }

    @Test
    void skipWhile_noneMatch() {
        List<Integer> result = Linq.from(List.of(5, 6, 7))
                .skipWhile(x -> x < 3)
                .toList();
        assertEquals(List.of(5, 6, 7), result);
    }

    @Test
    void skipWhile_empty() {
        List<Integer> result = Linq.from(List.<Integer>of())
                .skipWhile(x -> x < 3)
                .toList();
        assertTrue(result.isEmpty());
    }

    @Test
    void skipWhile_afterWhere() {
        List<Integer> result = Linq.from(List.of(1, 2, 3, 4, 5, 6))
                .where(x -> x % 2 == 0)
                .skipWhile(x -> x < 5)
                .toList();
        assertEquals(List.of(6), result);
    }

    @Test
    void skipWhile_vs_where_difference() {
        List<Integer> source = List.of(1, 2, 5, 3, 4);

        List<Integer> whereResult = Linq.from(source)
                .where(x -> x >= 3).toList();
        assertEquals(List.of(5, 3, 4), whereResult);

        List<Integer> skipWhileResult = Linq.from(source)
                .skipWhile(x -> x < 3).toList();
        assertEquals(List.of(5, 3, 4), skipWhileResult);
    }
}
