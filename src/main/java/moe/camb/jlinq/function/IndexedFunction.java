package moe.camb.jlinq.function;

import moe.camb.jlinq.Enumerable;

/**
 * Represents a function that also receives the element's zero-based index.
 *
 * <p>Used with {@link Enumerable#select(IndexedFunction)} for index-aware transformation.</p>
 *
 * <pre>{@code
 * Linq.from(List.of("Alice", "Bob"))
 *     .select((name, i) -> (i + 1) + ". " + name)
 *     .toList();
 * // ["1. Alice", "2. Bob"]
 * }</pre>
 *
 * @param <T> the type of the input element
 * @param <R> the type of the result
 * @see Enumerable#select(IndexedFunction)
 */
@FunctionalInterface
public interface IndexedFunction<T, R> {

    /**
     * Applies this function to the given element and index.
     *
     * @param element the input element
     * @param index   the zero-based index of the element
     * @return the result
     */
    R apply(T element, int index);
}