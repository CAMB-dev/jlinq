package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SumTest {

    @Test
    void sumInt_withSelector() {
        int result = Linq.from(List.of("a", "bb", "ccc"))
                .sumInt(String::length);
        assertEquals(6, result);
    }

    @Test
    void sumInt_withSelector_emptyReturnsZero() {
        int result = Linq.from(List.<Integer>of())
                .sumInt(x -> x);
        assertEquals(0, result);
    }

    @Test
    void sumInt_withSelector_afterWhere() {
        int result = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> x % 2 == 0)
                .sumInt(x -> x);
        assertEquals(6, result);
    }

    @Test
    void sumInt_noArg() {
        int result = Linq.from(List.of(1, 2, 3, 4, 5)).sumInt();
        assertEquals(15, result);
    }

    @Test
    void sumInt_noArg_emptyReturnsZero() {
        int result = Linq.from(List.<Integer>of()).sumInt();
        assertEquals(0, result);
    }

    @Test
    void sumInt_noArg_negativeNumbers() {
        int result = Linq.from(List.of(-1, 2, -3, 4)).sumInt();
        assertEquals(2, result);
    }

    @Test
    void sumInt_noArg_afterSelect() {
        int result = Linq.from(List.of("a", "bb", "ccc"))
                .select(String::length)
                .sumInt();
        assertEquals(6, result);
    }

    @Test
    void sumLong_withSelector() {
        long result = Linq.from(List.of("a", "bb", "ccc"))
                .sumLong(s -> (long) s.length());
        assertEquals(6L, result);
    }

    @Test
    void sumLong_noArg() {
        long result = Linq.from(List.of(1L, 2L, 3L)).sumLong();
        assertEquals(6L, result);
    }

    @Test
    void sumLong_noArg_emptyReturnsZero() {
        long result = Linq.from(List.<Long>of()).sumLong();
        assertEquals(0L, result);
    }

    @Test
    void sumDouble_withSelector() {
        double result = Linq.from(List.of(1, 2, 3))
                .sumDouble(x -> x * 1.5);
        assertEquals(9.0, result, 0.0001);
    }

    @Test
    void sumDouble_noArg() {
        double result = Linq.from(List.of(1.5, 2.5, 3.0)).sumDouble();
        assertEquals(7.0, result, 0.0001);
    }

    @Test
    void sumDouble_noArg_emptyReturnsZero() {
        double result = Linq.from(List.<Double>of()).sumDouble();
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    void sumDouble_noArg_afterWhere() {
        double result = Linq.from(List.of(1.0, 2.0, 3.0, 4.0))
                .where(x -> x > 2)
                .sumDouble();
        assertEquals(7.0, result, 0.0001);
    }
}
