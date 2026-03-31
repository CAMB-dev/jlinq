package moe.camb.jlinq.function;

import moe.camb.jlinq.Enumerable;

/**
 * Represents a predicate (boolean-valued function) that also receives
 * the element's zero-based index.
 *
 * <p>Used with {@link Enumerable#where(IndexedPredicate)} for index-aware filtering.</p>
 *
 * <pre>{@code
 * Linq.from(List.of("a", "b", "c", "d"))
 *     .where((item, i) -> i < 2)
 *     .toList();
 * // ["a", "b"]
 * }</pre>
 *
 * @param <T> the type of the input element
 * @see Enumerable#where(IndexedPredicate)
 */
@FunctionalInterface
public interface IndexedPredicate<T> {

    /**
     * Evaluates this predicate on the given element and index.
     *
     * @param element the input element
     * @param index   the zero-based index of the element
     * @return {@code true} if the element matches the condition
     */
    boolean test(T element, int index);
}