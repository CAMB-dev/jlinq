package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import moe.camb.jlinq.exception.EmptySequenceException;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaxTest {

    @Test
    void max_shouldReturnLargest() {
        int result = Linq.from(List.of(3, 1, 4, 1, 5)).max();
        assertEquals(5, result);
    }

    @Test
    void max_strings() {
        String result = Linq.from(List.of("cherry", "apple", "banana")).max();
        assertEquals("cherry", result);
    }

    @Test
    void max_singleElement() {
        int result = Linq.from(List.of(42)).max();
        assertEquals(42, result);
    }

    @Test
    void max_emptyThrows() {
        assertThrows(EmptySequenceException.class,
                () -> Linq.from(List.<Integer>of()).max());
    }

    @Test
    void max_withComparator() {
        String result = Linq.from(List.of("apple", "hi", "banana"))
                .max(Comparator.comparingInt(String::length));
        assertEquals("banana", result);
    }

    @Test
    void max_withComparator_emptyThrows() {
        assertThrows(EmptySequenceException.class,
                () -> Linq.from(List.<String>of()).max(Comparator.naturalOrder()));
    }

    @Test
    void max_afterWhereAndSelect() {
        int result = Linq.from(List.of("a", "bbb", "cc", "dddd"))
                .where(s -> s.length() > 1)
                .select(String::length)
                .max();
        assertEquals(4, result);
    }
}
