package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AllTest {

    @Test
    void all_allMatch() {
        assertTrue(Linq.from(List.of(2, 4, 6)).all(x -> x % 2 == 0));
    }

    @Test
    void all_notAllMatch() {
        assertFalse(Linq.from(List.of(2, 3, 6)).all(x -> x % 2 == 0));
    }

    @Test
    void all_empty_returnsTrue() {
        assertTrue(Linq.from(List.<Integer>of()).all(x -> x > 100));
    }

    @Test
    void all_isLazy_stopsAfterFail() {
        List<String> visited = new ArrayList<>();
        boolean result = Linq.from(List.of(2, 4, 5, 8, 10))
                .all(x -> {
                    visited.add("" + x);
                    return x % 2 == 0;
                });
        assertFalse(result);
        assertEquals(List.of("2", "4", "5"), visited);
    }

    @Test
    void all_afterWhere() {
        boolean result = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> x > 2)
                .all(x -> x >= 3);
        assertTrue(result);
    }
}
