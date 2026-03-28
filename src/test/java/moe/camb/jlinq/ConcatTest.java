package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConcatTest {

    @Test
    void concat_shouldCombineTwoSequences() {
        List<Integer> result = Linq.from(List.of(1, 2))
                .concat(Linq.from(List.of(3, 4)))
                .toList();
        assertEquals(List.of(1, 2, 3, 4), result);
    }

    @Test
    void concat_withEmptySource() {
        List<Integer> result = Linq.from(List.of(1, 2))
                .concat(Linq.from(List.of()))
                .toList();
        assertEquals(List.of(1, 2), result);
    }

    @Test
    void concat_emptyWithNonEmpty() {
        List<Integer> result = Linq.from(List.<Integer>of())
                .concat(Linq.from(List.of(1, 2)))
                .toList();
        assertEquals(List.of(1, 2), result);
    }
}
