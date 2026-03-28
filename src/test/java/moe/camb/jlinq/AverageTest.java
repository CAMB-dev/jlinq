package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class AverageTest {

    @Test
    void average_withSelector() {
        double result = Linq.from(List.of("a", "bb", "ccc"))
                .average(s -> s.length());
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    void average_withSelector_emptyThrows() {
        assertThrows(NoSuchElementException.class,
                () -> Linq.from(List.<Integer>of()).average(x -> x));
    }

    @Test
    void average_withSelector_afterWhere() {
        double result = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> x > 2)
                .average(x -> x);
        assertEquals(4.0, result, 0.0001);
    }

    @Test
    void average_noArg() {
        double result = Linq.from(List.of(10, 20, 30)).average();
        assertEquals(20.0, result, 0.0001);
    }

    @Test
    void average_noArg_singleElement() {
        double result = Linq.from(List.of(42)).average();
        assertEquals(42.0, result, 0.0001);
    }

    @Test
    void average_noArg_emptyThrows() {
        assertThrows(NoSuchElementException.class,
                () -> Linq.from(List.<Integer>of()).average());
    }

    @Test
    void average_noArg_doubles() {
        double result = Linq.from(List.of(1.0, 2.0, 3.0)).average();
        assertEquals(2.0, result, 0.0001);
    }
}
