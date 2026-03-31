package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContainsTest {

    @Test
    void contains_found() {
        assertTrue(Linq.from(List.of(1, 2, 3)).contains(2));
    }

    @Test
    void contains_notFound() {
        assertFalse(Linq.from(List.of(1, 2, 3)).contains(99));
    }

    @Test
    void contains_empty() {
        assertFalse(Linq.from(List.<Integer>of()).contains(1));
    }

    @Test
    void contains_null() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        list.add("b");
        assertTrue(Linq.from(list).contains(null));
    }

    @Test
    void contains_nullNotPresent() {
        assertFalse(Linq.from(List.of("a", "b")).contains(null));
    }

    @Test
    void contains_afterWhere() {
        boolean result = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> x > 3)
                .contains(4);
        assertTrue(result);
    }

    @Test
    void contains_afterWhere_notFound() {
        boolean result = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> x > 3)
                .contains(2);
        assertFalse(result);
    }

    @Test
    void contains_strings() {
        assertTrue(Linq.from(List.of("apple", "banana", "cherry")).contains("banana"));
        assertFalse(Linq.from(List.of("apple", "banana", "cherry")).contains("grape"));
    }

    @Test
    void contains_isLazy_stopsAfterFound() {
        List<String> visited = new ArrayList<>();
        boolean result = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> {
                    visited.add("" + x);
                    return true;
                })
                .contains(2);
        assertTrue(result);
        assertEquals(List.of("1", "2"), visited);
    }
}
