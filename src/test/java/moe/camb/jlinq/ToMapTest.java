package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ToMapTest {

    @Test
    void toMap_basic() {
        Map<Integer, String> result = Linq.from(List.of("a", "bb", "ccc"))
                .toMap(String::length, x -> x);
        assertEquals(Map.of(1, "a", 2, "bb", 3, "ccc"), result);
    }

    @Test
    void toMap_keyAndValueSelector() {
        Map<String, Integer> result = Linq.from(List.of("apple", "banana", "cherry"))
                .toMap(x -> x, String::length);
        assertEquals(3, result.size());
        assertEquals(5, result.get("apple"));
        assertEquals(6, result.get("banana"));
        assertEquals(6, result.get("cherry"));
    }

    @Test
    void toMap_empty() {
        Map<Integer, String> result = Linq.from(List.<String>of())
                .toMap(String::length, x -> x);
        assertTrue(result.isEmpty());
    }

    @Test
    void toMap_duplicateKeyThrows() {
        assertThrows(IllegalStateException.class,
                () -> Linq.from(List.of("aa", "bb"))
                        .toMap(String::length, x -> x));
    }

    @Test
    void toMap_isMutable() {
        Map<Integer, String> result = Linq.from(List.of("a"))
                .toMap(String::length, x -> x);
        result.put(99, "test");
        assertEquals(2, result.size());
    }

    @Test
    void toMap_afterWhere() {
        Map<String, Integer> result = Linq.from(List.of("a", "bb", "ccc", "dddd"))
                .where(s -> s.length() > 1)
                .toMap(x -> x, String::length);
        assertEquals(Map.of("bb", 2, "ccc", 3, "dddd", 4), result);
    }

    @Test
    void toMap_afterSelect() {
        Map<Integer, Integer> result = Linq.from(List.of(1, 2, 3))
                .select(x -> x * 10)
                .toMap(x -> x, x -> x * 2);
        assertEquals(Map.of(10, 20, 20, 40, 30, 60), result);
    }

    @Test
    void toMap_afterOrderBy() {
        Map<String, Integer> result = Linq.from(List.of("cherry", "apple", "banana"))
                .orderBy(x -> x)
                .toMap(x -> x, String::length);
        assertEquals(5, result.get("apple"));
    }
}
