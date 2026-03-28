package moe.camb.jlinq;

import moe.camb.jlinq.function.IndexedFunction;
import moe.camb.jlinq.function.IndexedPredicate;
import moe.camb.jlinq.iterator.ConcatIterator;
import moe.camb.jlinq.iterator.SelectIterator;
import moe.camb.jlinq.iterator.WhereIterator;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface Enumerable<T> extends Iterable<T> {

    @Override
    Iterator<T> iterator();

    default long longCount() {
        long count = 0;
        for (T ignored : this) {
            count++;
        }
        return count;
    }

    default int count() {
        long count = longCount();
        if (count > Integer.MAX_VALUE) {
            throw new ArithmeticException("Element count exceeds Integer.MAX_VALUE, count: " + count);
        }
        return (int) count;
    }

    default List<T> toList() {
        List<T> list = new ArrayList<>();
        for (T item : this) {
            list.add(item);
        }
        return list;
    }

    default Set<T> toSet() {
        HashSet<T> set = new HashSet<>();
        for (T item : this) {
            set.add(item);
        }
        return set;
    }

    default List<T> toImmutableList() {
        return List.copyOf(toList());
    }

    default Enumerable<T> where(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate cannot be null");
        return () -> new WhereIterator<>(this.iterator(), (item, i) -> predicate.test(item));
    }

    default Enumerable<T> where(IndexedPredicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate cannot be null");
        return () -> new WhereIterator<>(this.iterator(), predicate);
    }

    default <R> Enumerable<R> select(Function<? super T, ? extends R> selector) {
        Objects.requireNonNull(selector, "selector cannot be null");
        return () -> new SelectIterator<>(this.iterator(), (item, i) -> selector.apply(item));
    }

    default <R> Enumerable<R> select(IndexedFunction<? super T, ? extends R> selector) {
        Objects.requireNonNull(selector, "selector cannot be null");
        return () -> new SelectIterator<>(this.iterator(), selector);
    }

    default Enumerable<T> concat(Enumerable<T> other) {
        Objects.requireNonNull(other, "other cannot be null");
        return () -> new ConcatIterator<>(this.iterator(), other.iterator());
    }

}
