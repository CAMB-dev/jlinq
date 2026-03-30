package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnyTest {

    @Test
    void any_nonEmpty_returnsTrue() {
        assertTrue(Linq.from(List.of(1, 2, 3)).any());
    }

    @Test
    void any_empty_returnsFalse() {
        assertFalse(Linq.from(List.of()).any());
    }

    @Test
    void any_afterWhere_hasResult() {
        assertTrue(Linq.from(List.of(1, 2, 3)).where(x -> x > 2).any());
    }

    @Test
    void any_afterWhere_noResult() {
        assertFalse(Linq.from(List.of(1, 2, 3)).where(x -> x > 100).any());
    }

    @Test
    void any_withPredicate_found() {
        assertTrue(Linq.from(List.of(1, 2, 3, 4, 5)).any(x -> x > 3));
    }

    @Test
    void any_withPredicate_notFound() {
        assertFalse(Linq.from(List.of(1, 2, 3)).any(x -> x > 100));
    }

    @Test
    void any_withPredicate_empty() {
        assertFalse(Linq.from(List.<Integer>of()).any(x -> x > 0));
    }

    @Test
    void any_isLazy_stopsAfterFound() {
        List<String> visited = new ArrayList<>();
        boolean result = Linq.from(List.of(1, 2, 3, 4, 5))
                .any(x -> {
                    visited.add("" + x);
                    return x == 2;
                });
        assertTrue(result);
        assertEquals(List.of("1", "2"), visited);
    }
}
