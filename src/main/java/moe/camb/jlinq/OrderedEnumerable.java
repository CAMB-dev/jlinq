package moe.camb.jlinq;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

public interface OrderedEnumerable<T> extends Enumerable<T> {
    Comparator<? super T> comparator();

    OrderedEnumerable<T> createOrdered(Comparator<? super T> newComparator);

    @SuppressWarnings("unchecked")
    default OrderedEnumerable<T> thenBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        Comparator<? super T> combined = ((Comparator<T>) comparator()).thenComparing(comparator);
        return createOrdered(combined);
    }

    default <K extends Comparable<? super K>> OrderedEnumerable<T> thenBy(Function<? super T, ? extends K> keySelector) {
        Objects.requireNonNull(keySelector, "KeySelector cannot be null");
        return thenBy(Comparator.comparing(keySelector));
    }

    default <K extends Comparable<? super K>> OrderedEnumerable<T> thenByDescending(Function<? super T, ? extends K> keySelector) {
        Objects.requireNonNull(keySelector, "KeySelector cannot be null");
        return thenBy(Comparator.comparing(keySelector).reversed());
    }
}
