package moe.camb.jlinq;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinqFactoryTest {

    @Test
    void fromIterable() {
        List<Integer> source = List.of(1, 2, 3);
        Enumerable<Integer> enumerable = Linq.from(source);

        List<Integer> result = new ArrayList<>();
        for (int item : enumerable) {
            result.add(item);
        }

        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void fromArray() {
        Integer[] source = {1, 2, 3};
        Enumerable<Integer> enumerable = Linq.from(source);

        List<Integer> result = new ArrayList<>();
        for (int item : enumerable) {
            result.add(item);
        }

        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void fromNull() {
        assertThrows(NullPointerException.class, () -> Linq.from((Iterable<Object>) null));
        assertThrows(NullPointerException.class, () -> Linq.from((Object[]) null));
    }
}
