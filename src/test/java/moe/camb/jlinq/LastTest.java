package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import moe.camb.jlinq.exception.EmptySequenceException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LastTest {

    @Test
    void last_shouldReturnLastElement() {
        int result = Linq.from(List.of(3, 1, 4)).last();
        assertEquals(4, result);
    }

    @Test
    void last_singleElement() {
        int result = Linq.from(List.of(42)).last();
        assertEquals(42, result);
    }

    @Test
    void last_emptyThrows() {
        assertThrows(EmptySequenceException.class,
                () -> Linq.from(List.<Integer>of()).last());
    }

    @Test
    void last_afterWhere() {
        int result = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> x > 3)
                .last();
        assertEquals(5, result);
    }

    @Test
    void last_withPredicate() {
        int result = Linq.from(List.of(1, 2, 3, 4, 5))
                .last(x -> x > 3);
        assertEquals(5, result);
    }

    @Test
    void last_withPredicate_multipleMatches() {
        int result = Linq.from(List.of(2, 4, 6, 8, 10))
                .last(x -> x < 7);
        assertEquals(6, result);
    }

    @Test
    void last_withPredicate_noMatchThrows() {
        assertThrows(EmptySequenceException.class,
                () -> Linq.from(List.of(1, 2, 3)).last(x -> x > 100));
    }

    @Test
    void first_and_last_differ() {
        Enumerable<Integer> query = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> x > 1);
        assertEquals(2, query.first());
        assertEquals(5, query.last());
    }
}
