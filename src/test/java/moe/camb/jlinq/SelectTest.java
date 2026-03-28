package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SelectTest {
    @Test
    void select_shouldTransformElements() {
        List<String> result = Linq.from(List.of(1, 2, 3))
                .select(x -> "No." + x)
                .toList();
        assertEquals(List.of("No.1", "No.2", "No.3"), result);
    }

    @Test
    void select_typeChange() {
        List<Integer> result = Linq.from(List.of("a", "bb", "ccc"))
                .select(String::length)
                .toList();
        assertEquals(List.of(1, 2, 3), result);
    }
    @Test
    void select_indexed_shouldProvideIndex() {
        List<String> result = Linq.from(List.of("Alice", "Bob", "Carol"))
                .select((name, i) -> (i + 1) + ". " + name)
                .toList();
        assertEquals(List.of("1. Alice", "2. Bob", "3. Carol"), result);
    }

    @Test
    void select_indexed_transformToIndex() {
        List<Integer> result = Linq.from(List.of("a", "b", "c"))
                .select((item, i) -> i)
                .toList();
        assertEquals(List.of(0, 1, 2), result);
    }

    @Test
    void select_indexed_emptySource() {
        List<String> result = Linq.from(List.<Integer>of())
                .select((item, i) -> i + ": " + item)
                .toList();
        assertTrue(result.isEmpty());
    }

    @Test
    void select_indexed_combineIndexAndValue() {
        List<String> result = Linq.from(List.of(10, 20, 30))
                .select((item, i) -> "index=" + i + ",value=" + item)
                .toList();
        assertEquals(List.of("index=0,value=10", "index=1,value=20", "index=2,value=30"), result);
    }

    @Test
    void select_indexed_indexStartsFromZero() {
        List<Integer> indices = new ArrayList<>();
        Linq.from(List.of("x", "y", "z"))
                .select((item, i) -> {
                    indices.add(i);
                    return item;
                })
                .toList();
        assertEquals(List.of(0, 1, 2), indices);
    }
}
