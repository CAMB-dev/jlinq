package moe.camb.jlinq;

import java.util.List;

/**
 * Represents a group of elements that share a common key.
 *
 * <p>Created by {@link Enumerable#groupBy}. Each {@code Grouping}
 * contains a key and the list of elements associated with that key.</p>
 *
 * <pre>{@code
 * Linq.from(List.of("apple", "avocado", "banana"))
 *     .groupBy(s -> s.charAt(0))
 *     .select(g -> g.getKey() + ": " + g.getElements())
 *     .toList();
 * // ["a: [apple, avocado]", "b: [banana]"]
 * }</pre>
 *
 * @param <K> the type of the grouping key
 * @param <T> the type of elements in the group
 * @see Enumerable#groupBy
 */
public class Grouping<K, T> {
    private final K key;
    private final List<T> elements;

    public Grouping(K key, List<T> elements) {
        this.key = key;
        this.elements = elements;
    }

    /**
     * Returns the key shared by all elements in this group.
     *
     * @return the group key
     */
    public K getKey() {
        return key;
    }

    /**
     * Returns the elements in this group as a {@link List}.
     *
     * @return the list of grouped elements
     */
    public List<T> getElements() {
        return elements;
    }

    /**
     * Returns the elements in this group as an {@link Enumerable}
     * for further chainable operations.
     *
     * <pre>{@code
     * Linq.from(users)
     *     .groupBy(User::getDepartment)
     *     .select(g -> g.asEnumerable()
     *         .orderBy(User::getName)
     *         .first())
     *     .toList();
     * }</pre>
     *
     * @return an {@code Enumerable} wrapping the grouped elements
     */
    public Enumerable<T> asEnumerable() {
        return elements::iterator;
    }
}
