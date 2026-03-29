package moe.camb.jlinq;

import moe.camb.jlinq.function.IndexedFunction;
import moe.camb.jlinq.function.IndexedPredicate;
import moe.camb.jlinq.iterator.ConcatIterator;
import moe.camb.jlinq.iterator.SelectIterator;
import moe.camb.jlinq.iterator.WhereIterator;

import java.util.*;
import java.util.function.*;

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
            throw new ArithmeticException("jlinq -> count: Element count exceeds Integer.MAX_VALUE, count: " + count);
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

    default List<T> toImmutableList() {
        return List.copyOf(toList());
    }


    default Set<T> toSet() {
        HashSet<T> set = new HashSet<>();
        for (T item : this) {
            set.add(item);
        }
        return set;
    }

    default int sumInt(ToIntFunction<? super T> selector) {
        Objects.requireNonNull(selector, "jlinq -> sumInt: selector cannot be null");
        int sum = 0;
        for (T item : this) {
            sum += selector.applyAsInt(item);
        }
        return sum;
    }

    default int sumInt() {
        return sumInt(x -> ((Number) x).intValue());
    }

    default long sumLong(ToLongFunction<? super T> selector) {
        Objects.requireNonNull(selector, "jlinq -> sumLong: selector cannot be null");
        long sum = 0;
        for (T item : this) {
            sum += selector.applyAsLong(item);
        }
        return sum;
    }

    default long sumLong() {
        return sumLong(x -> ((Number) x).longValue());
    }

    default double sumDouble(ToDoubleFunction<? super T> selector) {
        Objects.requireNonNull(selector, "jlinq -> sumDouble: selector cannot be null");
        double sum = 0;
        for (T item : this) {
            sum += selector.applyAsDouble(item);
        }
        return sum;
    }

    default double sumDouble() {
        return sumDouble(x -> ((Number) x).doubleValue());
    }

    default double average(ToDoubleFunction<? super T> selector) {
        Objects.requireNonNull(selector, "jlinq -> average: selector cannot be null");
        double sum = 0;
        long count = 0;
        for (T item : this) {
            sum += selector.applyAsDouble(item);
            count++;
        }
        if (count == 0) {
            throw new NoSuchElementException("jlinq -> average: sequence contains no elements");
        }
        return sum / count;
    }

    default double average() {
        return average(x -> ((Number) x).doubleValue());
    }

    default T min(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "jlinq -> min: comparator cannot be null");
        Iterator<T> it = this.iterator();
        if (!it.hasNext()) {
            throw new NoSuchElementException("jlinq -> min: sequence contains no elements");
        }

        T result = it.next();
        while (it.hasNext()) {
            T candidate = it.next();
            if (comparator.compare(candidate, result) < 0) {
                result = candidate;
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    default T min() {
        return min((Comparator<? super T>) Comparator.naturalOrder());
    }

    default T max(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "jlinq -> max: comparator cannot be null");
        Iterator<T> it = this.iterator();
        if (!it.hasNext()) {
            throw new NoSuchElementException("jlinq -> max: sequence contains no elements");
        }

        T result = it.next();
        while (it.hasNext()) {
            T candidate = it.next();
            if (comparator.compare(candidate, result) > 0) {
                result = candidate;
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    default T max() {
        return max((Comparator<? super T>) Comparator.naturalOrder());
    }

    default T first() {
        Iterator<T> it = this.iterator();
        if (!it.hasNext()) {
            throw new NoSuchElementException("jlinq -> first: sequence contains no elements");
        }

        return it.next();
    }

    default T first(Predicate<? super T> predicate) {
        Iterator<T> it = this.iterator();
        if (!it.hasNext()) {
            throw new NoSuchElementException("jlinq -> first: sequence contains no elements");
        }

        for (T item : this) {
            if (predicate.test(item)) {
                return item;
            }
        }

        throw new NoSuchElementException("jlinq -> first: sequence contains no matching element");
    }

    default T last() {
        Iterator<T> it = this.iterator();
        if (!it.hasNext()) {
            throw new NoSuchElementException("jlinq -> last: sequence contains no elements");
        }

        T result = it.next();
        while (it.hasNext()) {
            result = it.next();
        }
        return result;
    }

    default T last(Predicate<T> predicate) {
        boolean found = false;
        T result = null;
        Iterator<T> it = this.iterator();
        if (!it.hasNext()) {
            throw new NoSuchElementException("jlinq -> last: sequence contains no elements");
        }

        for (T item : this) {
            if (predicate.test(item)) {
                result = item;
                found = true;
            }
        }
        if (!found) {
            throw new NoSuchElementException("jlinq -> last: sequence contains no matching element");
        }
        return result;
    }

    default T single() {
        Iterator<T> it = this.iterator();
        if (!it.hasNext()) {
            throw new NoSuchElementException("jlinq -> single: sequence contains no elements");
        }

        T item = it.next();
        if (it.hasNext()) {
            throw new IllegalStateException("jlinq -> single: sequence contains more than one element");
        }
        return item;
    }

    default T single(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "jlinq -> single: predicate cannot be null");
        boolean found = false;
        T result = null;
        Iterator<T> it = this.iterator();
        if (!it.hasNext()) {
            throw new NoSuchElementException("jlinq -> single: sequence contains no elements");
        }

        for (T item : this) {
            if (predicate.test(item)) {
                if (found) {
                    throw new IllegalStateException("jlinq -> single: sequence contains more than one matching element");
                }
                result = item;
                found = true;
            }
        }
        if (!found) {
            throw new NoSuchElementException("jlinq -> single: sequence contains no matching element");
        }
        return result;
    }

    default Enumerable<T> where(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "jlinq -> where: predicate cannot be null");
        return () -> new WhereIterator<>(this.iterator(), (item, i) -> predicate.test(item));
    }

    default Enumerable<T> where(IndexedPredicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "jlinq -> where: predicate cannot be null");
        return () -> new WhereIterator<>(this.iterator(), predicate);
    }

    default <R> Enumerable<R> select(Function<? super T, ? extends R> selector) {
        Objects.requireNonNull(selector, "jlinq -> select: selector cannot be null");
        return () -> new SelectIterator<>(this.iterator(), (item, i) -> selector.apply(item));
    }

    default <R> Enumerable<R> select(IndexedFunction<? super T, ? extends R> selector) {
        Objects.requireNonNull(selector, "jlinq -> select: selector cannot be null");
        return () -> new SelectIterator<>(this.iterator(), selector);
    }

    default Enumerable<T> concat(Enumerable<T> other) {
        Objects.requireNonNull(other, "jlinq -> concat: other cannot be null");
        return () -> new ConcatIterator<>(this.iterator(), other.iterator());
    }

}
