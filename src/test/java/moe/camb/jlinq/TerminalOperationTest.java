package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TerminalOperationTest {

    @Test
    void count_shouldReturnElementCount() {
        int count = Linq.from(List.of(1, 2, 3)).count();
        assertEquals(3, count);
    }

    @Test
    void count_emptySource() {
        int count = Linq.from(List.of()).count();
        assertEquals(0, count);
    }

    @Test
    void longCount_shouldReturnLong() {
        long count = Linq.from(List.of(1, 2, 3)).longCount();
        assertEquals(3L, count);
    }

    @Test
    void toList_shouldReturnMutableArrayList() {
        List<Integer> result = Linq.from(List.of(1, 2, 3)).toList();
        assertEquals(List.of(1, 2, 3), result);
        result.add(4);
        assertEquals(4, result.size());
    }

    @Test
    void toSet_shouldRemoveDuplicates() {
        Set<Integer> result = Linq.from(List.of(1, 2, 2, 3, 3)).toSet();
        assertEquals(Set.of(1, 2, 3), result);
    }

    @Test
    void toImmutableList_shouldReturnAllElements() {
        List<Integer> result = Linq.from(List.of(1, 2, 3)).toImmutableList();
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void toImmutableList_shouldBeUnmodifiable() {
        List<Integer> result = Linq.from(List.of(1, 2, 3)).toImmutableList();
        assertThrows(UnsupportedOperationException.class, () -> result.add(4));
        assertThrows(UnsupportedOperationException.class, () -> result.remove(0));
        assertThrows(UnsupportedOperationException.class, () -> result.set(0, 99));
    }

    @Test
    void toImmutableList_empty() {
        List<Integer> result = Linq.from(List.<Integer>of()).toImmutableList();
        assertTrue(result.isEmpty());
    }

    @Test
    void toImmutableList_afterWhereAndSelect() {
        List<String> result = Linq.from(List.of(1, 2, 3, 4, 5))
                .where(x -> x > 2)
                .select(x -> "No." + x)
                .toImmutableList();
        assertEquals(List.of("No.3", "No.4", "No.5"), result);
        assertThrows(UnsupportedOperationException.class, () -> result.add("No.6"));
    }

    @Test
    void toList_and_toImmutableList_differ() {
        Enumerable<Integer> query = Linq.from(List.of(1, 2, 3));

        List<Integer> mutable = query.toList();
        mutable.add(4);
        assertEquals(4, mutable.size());

        List<Integer> immutable = query.toImmutableList();
        assertThrows(UnsupportedOperationException.class, () -> immutable.add(4));
        assertEquals(3, immutable.size());
    }
}
