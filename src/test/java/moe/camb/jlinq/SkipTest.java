package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SkipTest {

    @Test
    void skip_shouldSkipFirstN() {
        List<Integer> result = Linq.from(List.of(1, 2, 3, 4, 5))
                .skip(2).toList();
        assertEquals(List.of(3, 4, 5), result);
    }

    @Test
    void skip_moreThanAvailable() {
        List<Integer> result = Linq.from(List.of(1, 2))
                .skip(10).toList();
        assertTrue(result.isEmpty());
    }

    @Test
    void skip_zero() {
        List<Integer> result = Linq.from(List.of(1, 2, 3))
                .skip(0).toList();
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void skip_negativeThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> Linq.from(List.of(1, 2, 3)).skip(-1));
    }

    @Test
    void skip_afterWhere() {
        List<Integer> result = Linq.from(List.of(1, 2, 3, 4, 5, 6))
                .where(x -> x % 2 == 0)
                .skip(1).toList();
        assertEquals(List.of(4, 6), result);
    }
}
