package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TakeTest {

    @Test
    void take_shouldTakeFirstN() {
        List<Integer> result = Linq.from(List.of(1, 2, 3, 4, 5))
                .take(3).toList();
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void take_moreThanAvailable() {
        List<Integer> result = Linq.from(List.of(1, 2))
                .take(10).toList();
        assertEquals(List.of(1, 2), result);
    }

    @Test
    void take_zero() {
        List<Integer> result = Linq.from(List.of(1, 2, 3))
                .take(0).toList();
        assertTrue(result.isEmpty());
    }

    @Test
    void take_fromEmpty() {
        List<Integer> result = Linq.from(List.<Integer>of())
                .take(5).toList();
        assertTrue(result.isEmpty());
    }

    @Test
    void take_negativeThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> Linq.from(List.of(1, 2, 3)).take(-1));
    }

    @Test
    void take_afterWhere() {
        List<Integer> result = Linq.from(List.of(1, 2, 3, 4, 5, 6, 7, 8))
                .where(x -> x % 2 == 0)
                .take(2).toList();
        assertEquals(List.of(2, 4), result);
    }

    @Test
    void take_isLazy() {
        List<String> visited = new ArrayList<>();
        List<Integer> result = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> { visited.add("" + x); return true; })
                .take(2).toList();
        assertEquals(List.of(1, 2), result);
        assertEquals(List.of("1", "2"), visited);
    }
}
