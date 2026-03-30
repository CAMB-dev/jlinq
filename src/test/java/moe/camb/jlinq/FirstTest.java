package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import moe.camb.jlinq.exception.EmptySequenceException;
import moe.camb.jlinq.exception.LinqException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FirstTest {

    @Test
    void first_shouldReturnFirstElement() {
        int result = Linq.from(List.of(3, 1, 4)).first();
        assertEquals(3, result);
    }

    @Test
    void first_singleElement() {
        int result = Linq.from(List.of(42)).first();
        assertEquals(42, result);
    }

    @Test
    void first_emptyThrows() {
        assertThrows(EmptySequenceException.class,
                () -> Linq.from(List.<Integer>of()).first());
    }

    @Test
    void first_afterWhere() {
        int result = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> x > 3)
                .first();
        assertEquals(4, result);
    }

    @Test
    void first_withPredicate() {
        int result = Linq.from(List.of(1, 2, 3, 4, 5))
                .first(x -> x > 3);
        assertEquals(4, result);
    }

    @Test
    void first_withPredicate_firstElementMatches() {
        int result = Linq.from(List.of(10, 20, 30))
                .first(x -> x > 5);
        assertEquals(10, result);
    }

    @Test
    void first_withPredicate_noMatchThrows() {
        assertThrows(EmptySequenceException.class,
                () -> Linq.from(List.of(1, 2, 3)).first(x -> x > 100));
    }

    @Test
    void emptySequenceException_isCatchableAsLinqException() {
        assertThrows(LinqException.class,
                () -> Linq.from(List.<Integer>of()).first());
    }

    @Test
    void first_isLazy_stopsAfterFound() {
        List<String> visited = new ArrayList<>();
        int result = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> {
                    visited.add("" + x);
                    return x > 2;
                })
                .first();
        assertEquals(3, result);
        assertEquals(List.of("1", "2", "3"), visited);
    }
}
