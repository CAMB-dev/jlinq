package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import moe.camb.jlinq.exception.EmptySequenceException;
import moe.camb.jlinq.exception.LinqException;
import moe.camb.jlinq.exception.MultipleElementsException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SingleTest {

    @Test
    void single_shouldReturnOnlyElement() {
        int result = Linq.from(List.of(42)).single();
        assertEquals(42, result);
    }

    @Test
    void single_emptyThrows() {
        assertThrows(EmptySequenceException.class,
                () -> Linq.from(List.<Integer>of()).single());
    }

    @Test
    void single_multipleThrows() {
        assertThrows(MultipleElementsException.class,
                () -> Linq.from(List.of(1, 2)).single());
    }

    @Test
    void single_afterWhere_oneMatch() {
        int result = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> x == 3)
                .single();
        assertEquals(3, result);
    }

    @Test
    void single_afterWhere_multipleMatchThrows() {
        assertThrows(MultipleElementsException.class,
                () -> Linq.from(List.of(1, 2, 2, 3))
                        .where(x -> x == 2)
                        .single());
    }

    @Test
    void single_withPredicate_oneMatch() {
        int result = Linq.from(List.of(1, 2, 3, 4, 5))
                .single(x -> x == 3);
        assertEquals(3, result);
    }

    @Test
    void single_withPredicate_noMatchThrows() {
        assertThrows(EmptySequenceException.class,
                () -> Linq.from(List.of(1, 2, 3)).single(x -> x > 100));
    }

    @Test
    void multipleElementsException_isCatchableAsLinqException() {
        assertThrows(LinqException.class,
                () -> Linq.from(List.of(1, 2)).single());
    }

    @Test
    void single_withPredicate_multipleMatchThrows() {
        assertThrows(MultipleElementsException.class,
                () -> Linq.from(List.of(1, 2, 3, 4, 5)).single(x -> x > 3));
    }
}
