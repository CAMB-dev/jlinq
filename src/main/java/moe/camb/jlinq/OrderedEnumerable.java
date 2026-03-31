package moe.camb.jlinq;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

/**
 * An {@link Enumerable} whose elements have been sorted.
 *
 * <p>Provides {@link #thenBy} and {@link #thenByDescending} methods for
 * specifying secondary sort criteria. These methods are only available
 * after calling {@link Enumerable#orderBy} or {@link Enumerable#orderByDescending}.</p>
 *
 * <pre>{@code
 * Linq.from(users)
 *     .orderBy(User::getDepartment)
 *     .thenBy(User::getName)
 *     .thenByDescending(User::getAge)
 *     .toList();
 * }</pre>
 *
 * @param <T> the type of elements
 * @see Enumerable#orderBy
 */
public interface OrderedEnumerable<T> extends Enumerable<T> {

    /**
     * Returns the comparator representing the current sort order.
     *
     * @return the current comparator
     */
    Comparator<? super T> comparator();

    /**
     * Creates a new {@code OrderedEnumerable} with the given comparator.
     * Used internally by {@link #thenBy} to build composite sort orders.
     *
     * @param newComparator the combined comparator
     * @return a new {@code OrderedEnumerable} with the updated sort order
     */
    OrderedEnumerable<T> createOrdered(Comparator<? super T> newComparator);

    /**
     * Adds a secondary sort using the specified comparator.
     *
     * @param comparator the comparator for secondary sorting
     * @return a new {@code OrderedEnumerable} with the added sort criteria
     * @throws NullPointerException if comparator is null
     */
    @SuppressWarnings("unchecked")
    default OrderedEnumerable<T> thenBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        Comparator<? super T> combined = ((Comparator<T>) comparator()).thenComparing(comparator);
        return createOrdered(combined);
    }

    /**
     * Adds a secondary ascending sort by the specified key.
     *
     * <p>Elements that are equal according to previous sort criteria
     * are further sorted by this key.</p>
     *
     * <pre>{@code
     * Linq.from(List.of("bb", "aa", "ccc", "a", "cc"))
     *     .orderBy(String::length)
     *     .thenBy(x -> x)
     *     .toList();
     * // ["a", "aa", "bb", "cc", "ccc"]
     * }</pre>
     *
     * <p>Using method reference:</p>
     * <pre>{@code
     * Linq.from(users)
     *     .orderBy(User::getDepartment)
     *     .thenBy(User::getName)
     *     .toList();
     * }</pre>
     *
     * @param keySelector function to extract the secondary sort key
     * @param <K>         the type of the sort key, must be {@link Comparable}
     * @return a new {@code OrderedEnumerable} with the added sort criteria
     * @throws NullPointerException if keySelector is null
     */
    default <K extends Comparable<? super K>> OrderedEnumerable<T> thenBy(Function<? super T, ? extends K> keySelector) {
        Objects.requireNonNull(keySelector, "KeySelector cannot be null");
        return thenBy(Comparator.comparing(keySelector));
    }

    /**
     * Adds a secondary descending sort by the specified key.
     *
     * <pre>{@code
     * Linq.from(users)
     *     .orderBy(User::getDepartment)
     *     .thenByDescending(User::getAge)
     *     .toList();
     * }</pre>
     *
     * @param keySelector function to extract the secondary sort key
     * @param <K>         the type of the sort key, must be {@link Comparable}
     * @return a new {@code OrderedEnumerable} with the added sort criteria
     * @throws NullPointerException if keySelector is null
     */
    default <K extends Comparable<? super K>> OrderedEnumerable<T> thenByDescending(Function<? super T, ? extends K> keySelector) {
        Objects.requireNonNull(keySelector, "KeySelector cannot be null");
        return thenBy(Comparator.comparing(keySelector).reversed());
    }
}
