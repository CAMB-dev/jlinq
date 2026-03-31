package moe.camb.jlinq;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Entry point for creating {@link Enumerable} sequences.
 *
 * <p>Use {@code Linq.from()} to wrap an existing collection, or {@code Linq.of()}
 * to create a sequence from individual values:</p>
 *
 * <pre>{@code
 * // From existing collection
 * Linq.from(userList).where(User::isActive).toList();
 *
 * // From individual values
 * Linq.of(1, 2, 3).where(x -> x > 1).toList();
 *
 * // From array
 * Linq.from(myArray).select(String::valueOf).toList();
 *
 * // From Stream (materializes immediately)
 * Linq.from(myStream).where(x -> x > 0).toList();
 * }</pre>
 *
 * @see Enumerable
 */
public final class Linq {
    private Linq() {
    }

    /**
     * Creates an {@link Enumerable} from any {@link Iterable} source.
     *
     * <p>This is the primary entry point for JLinq. Works with any {@code List},
     * {@code Set}, {@code Collection}, or custom {@code Iterable} implementation.</p>
     *
     * <pre>{@code
     * // From List (e.g. MyBatis query result)
     * Linq.from(userMapper.selectAll()).where(User::isActive).toList();
     *
     * // From Set
     * Linq.from(mySet).select(String::length).toList();
     * }</pre>
     *
     * @param source the iterable to wrap
     * @param <T>    the type of elements
     * @return an {@code Enumerable} wrapping the source
     * @throws NullPointerException if source is null
     */
    public static <T> Enumerable<T> from(Iterable<T> source) {
        Objects.requireNonNull(source, "source cannot be null");
        return source::iterator;
    }

    /**
     * Creates an {@link Enumerable} from an array.
     *
     * <pre>{@code
     * Integer[] arr = {1, 2, 3};
     * Linq.from(arr).where(x -> x > 1).toList();
     * }</pre>
     *
     * @param source the array to wrap
     * @param <T>    the type of elements
     * @return an {@code Enumerable} wrapping the array
     * @throws NullPointerException if source is null
     */
    public static <T> Enumerable<T> from(T[] source) {
        Objects.requireNonNull(source, "source cannot be null");
        return Arrays.asList(source)::iterator;
    }

    /**
     * Creates an {@link Enumerable} from a {@link Stream}.
     *
     * <p><strong>Note:</strong> The stream is consumed immediately and collected
     * into a list, so the resulting {@code Enumerable} can be iterated multiple times.
     * This differs from {@link #from(Iterable)}, which wraps lazily.</p>
     *
     * @param source the stream to consume
     * @param <T>    the type of elements
     * @return an {@code Enumerable} containing all elements from the stream
     * @throws NullPointerException if source is null
     */
    public static <T> Enumerable<T> from(Stream<T> source) {
        Objects.requireNonNull(source, "source cannot be null");
        return source.toList()::iterator;
    }

    /**
     * Creates an {@link Enumerable} from individual values.
     *
     * <pre>{@code
     * List<Integer> result = Linq.of(1, 2, 3, 4, 5)
     *     .where(x -> x > 2)
     *     .toList();
     * // [3, 4, 5]
     * }</pre>
     *
     * @param items the values to include in the sequence
     * @param <T>   the type of elements
     * @return an {@code Enumerable} containing the specified values
     * @throws NullPointerException if items is null
     */
    @SafeVarargs
    public static <T> Enumerable<T> of(T... items) {
        Objects.requireNonNull(items, "items cannot be null");
        return () -> Arrays.asList(items).iterator();
    }
}
