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
}
