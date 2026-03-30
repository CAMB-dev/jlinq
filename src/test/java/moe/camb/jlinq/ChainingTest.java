package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ChainingTest {

    @Test
    void where_then_select() {
        List<String> result = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> x > 2)
                .select(x -> "Item " + x)
                .toList();
        assertEquals(List.of("Item 3", "Item 4", "Item 5"), result);
    }

    @Test
    void fullChain_whereSelectConcat() {
        List<String> result = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> x > 2)
                .select(x -> "Item " + x)
                .concat(Linq.from(List.of("Extra")))
                .toList();
        assertEquals(List.of("Item 3", "Item 4", "Item 5", "Extra"), result);
    }

    @Test
    void chain_isLazy() {
        List<String> sideEffects = new ArrayList<>();

        Enumerable<Integer> lazy = Linq.from(List.of(1, 2, 3))
                .where(x -> {
                    sideEffects.add("where:" + x);
                    return x > 1;
                })
                .select(x -> {
                    sideEffects.add("select:" + x);
                    return x * 10;
                });

        assertTrue(sideEffects.isEmpty());

        List<Integer> result = lazy.toList();
        assertEquals(List.of(20, 30), result);
        assertFalse(sideEffects.isEmpty());
    }

    @Test
    void multipleIteration_shouldWorkCorrectly() {
        Enumerable<Integer> query = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> x % 2 == 0);

        assertEquals(List.of(2, 4), query.toList());
        assertEquals(List.of(2, 4), query.toList());
        assertEquals(2, query.count());
    }
    @Test
    void where_indexed_then_select_indexed() {
        List<String> result = Linq.from(List.of("a", "b", "c", "d", "e"))
                .where((item, i) -> i >= 2)
                .select((item, i) -> i + ":" + item)
                .toList();
        assertEquals(List.of("0:c", "1:d", "2:e"), result);
    }

    @Test
    void where_plain_then_select_indexed() {
        List<String> result = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> x % 2 != 0)
                .select((item, i) -> "#" + i + "=" + item)
                .toList();
        assertEquals(List.of("#0=1", "#1=3", "#2=5"), result);
    }

    @Test
    void where_indexed_then_select_plain() {
        List<String> result = Linq.from(List.of("apple", "banana", "cherry"))
                .where((item, i) -> i != 1)
                .select(item -> item.toUpperCase())
                .toList();
        assertEquals(List.of("APPLE", "CHERRY"), result);
    }

    @Test
    void where_indexed_then_select_indexed_then_concat() {
        List<String> result = Linq.from(List.of(10, 20, 30, 40, 50))
                .where((item, i) -> i < 3)
                .select((item, i) -> "[" + i + "]" + item)
                .concat(Linq.from(List.of("[extra]")))
                .toList();
        assertEquals(List.of("[0]10", "[1]20", "[2]30", "[extra]"), result);
    }

    @Test
    void indexed_multipleIteration_indexResetsEachTime() {
        Enumerable<String> query = Linq.from(List.of("a", "b", "c"))
                .select((item, i) -> i + item);

        assertEquals(List.of("0a", "1b", "2c"), query.toList());
        assertEquals(List.of("0a", "1b", "2c"), query.toList());
    }

    @Test
    void indexed_isLazy() {
        List<String> sideEffects = new ArrayList<>();

        Enumerable<String> lazy = Linq.from(List.of("a", "b", "c"))
                .where((item, i) -> {
                    sideEffects.add("where:" + i + ":" + item);
                    return i < 2;
                })
                .select((item, i) -> {
                    sideEffects.add("select:" + i + ":" + item);
                    return item.toUpperCase();
                });

        assertTrue(sideEffects.isEmpty());

        List<String> result = lazy.toList();
        assertEquals(List.of("A", "B"), result);
        assertFalse(sideEffects.isEmpty());
    }

    @Test
    void indexed_count() {
        int count = Linq.from(List.of(1, 2, 3, 4, 5))
                .where((item, i) -> i % 2 == 0)
                .count();
        assertEquals(3, count);
    }

    @Test
    void indexed_toSet() {
        Set<Integer> result = Linq.from(List.of("aaa", "b", "cc", "d", "eee"))
                .select((item, i) -> item.length())
                .toSet();
        assertEquals(Set.of(1, 2, 3), result);
    }

    @Test
    void sumInt_noArg_afterWhereAndSelect() {
        int result = Linq.from(List.of("a", "bb", "ccc", "dddd"))
                .where(s -> s.length() > 1)
                .select(String::length)
                .sumInt();
        assertEquals(9, result);
    }

    @Test
    void average_noArg_afterWhereAndSelect() {
        double result = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> x > 2)
                .select(x -> x * 10)
                .average();
        assertEquals(40.0, result, 0.0001);
    }

    @Test
    void min_afterSelectChangesType() {
        int result = Linq.from(List.of("apple", "hi", "banana"))
                .select(String::length)
                .min();
        assertEquals(2, result);
    }

    @Test
    void skip_then_take_pagination() {
        List<Integer> source = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        assertEquals(List.of(1, 2, 3), Linq.from(source).skip(0).take(3).toList());
        assertEquals(List.of(4, 5, 6), Linq.from(source).skip(3).take(3).toList());
        assertEquals(List.of(7, 8, 9), Linq.from(source).skip(6).take(3).toList());
        assertEquals(List.of(10),      Linq.from(source).skip(9).take(3).toList());
    }

    @Test
    void skipWhile_then_takeWhile() {
        List<Integer> result = Linq.from(List.of(1, 2, 3, 4, 5, 6))
                .skipWhile(x -> x < 3)
                .takeWhile(x -> x < 5)
                .toList();
        assertEquals(List.of(3, 4), result);
    }

    @Test
    void takeWhile_then_count() {
        int count = Linq.from(List.of(1, 2, 3, 4, 5))
                .takeWhile(x -> x < 4)
                .count();
        assertEquals(3, count);
    }

    @Test
    void skipWhile_then_first() {
        int result = Linq.from(List.of(1, 2, 3, 4, 5))
                .skipWhile(x -> x < 3)
                .first();
        assertEquals(3, result);
    }

    @Test
    void distinct_then_take() {
        List<Integer> result = Linq.from(List.of(1, 1, 2, 2, 3, 3, 4, 4))
                .distinct().take(3).toList();
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void where_distinct_skip_take() {
        List<Integer> result = Linq.from(List.of(5, 3, 3, 1, 4, 1, 2, 5, 4))
                .where(x -> x > 1)
                .distinct()
                .skip(1)
                .take(2)
                .toList();
        assertEquals(List.of(3, 4), result);
    }

    @Test
    void fullChain_allNewOperations() {
        boolean result = Linq.from(List.of(1, 2, 2, 3, 3, 4, 5, 5))
                .where(x -> x > 1)
                .distinct()
                .skip(1)
                .take(2)
                .all(x -> x >= 3);
        assertTrue(result);
    }

    @Test
    void single_afterDistinct() {
        int result = Linq.from(List.of(5, 5, 5)).distinct().single();
        assertEquals(5, result);
    }

    @Test
    void first_last_afterSkipTake() {
        Enumerable<Integer> query = Linq.from(List.of(1, 2, 3, 4, 5))
                .skip(1).take(3);
        assertEquals(2, query.first());
        assertEquals(4, query.last());
    }
}
