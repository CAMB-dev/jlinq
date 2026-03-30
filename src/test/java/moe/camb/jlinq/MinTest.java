package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import moe.camb.jlinq.exception.EmptySequenceException;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MinTest {

    @Test
    void min_shouldReturnSmallest() {
        int result = Linq.from(List.of(3, 1, 4, 1, 5)).min();
        assertEquals(1, result);
    }

    @Test
    void min_strings() {
        String result = Linq.from(List.of("cherry", "apple", "banana")).min();
        assertEquals("apple", result);
    }

    @Test
    void min_singleElement() {
        int result = Linq.from(List.of(42)).min();
        assertEquals(42, result);
    }

    @Test
    void min_emptyThrows() {
        assertThrows(EmptySequenceException.class,
                () -> Linq.from(List.<Integer>of()).min());
    }

    @Test
    void min_withComparator() {
        String result = Linq.from(List.of("apple", "hi", "banana"))
                .min(Comparator.comparingInt(String::length));
        assertEquals("hi", result);
    }

    @Test
    void min_withComparator_emptyThrows() {
        assertThrows(EmptySequenceException.class,
                () -> Linq.from(List.<String>of()).min(Comparator.naturalOrder()));
    }

    @Test
    void min_afterWhere() {
        int result = Linq.from(List.of(5, 3, 8, 1, 9))
                .where(x -> x > 3)
                .min();
        assertEquals(5, result);
    }
}
