package moe.camb.jlinq;

import moe.camb.jlinq.exception.EmptySequenceException;
import moe.camb.jlinq.exception.MultipleElementsException;
import moe.camb.jlinq.function.IndexedFunction;
import moe.camb.jlinq.function.IndexedPredicate;
import moe.camb.jlinq.impl.OrderedEnumerableImpl;
import moe.camb.jlinq.iterator.*;

import java.util.*;
import java.util.function.*;

/**
 * Represents a lazy, chainable sequence of elements.
 *
 * <p>{@code Enumerable} is the core abstraction of JLinq, inspired by C# LINQ's
 * {@code IEnumerable<T>}. It provides chainable query operators for filtering,
 * transforming, sorting, and aggregating data from any {@link Iterable} source.</p>
 *
 * <p>Use {@link Linq#from(Iterable)} or {@link Linq#of(Object[])} to create an instance:</p>
 *
 * <pre>{@code
 * List<String> result = Linq.from(List.of(1, 2, 3, 4, 5))
 *     .where(x -> x > 2)
 *     .select(x -> "No." + x)
 *     .toList();
 * // ["No.3", "No.4", "No.5"]
 * }</pre>
 *
 * @param <T> the type of elements in this sequence
 * @see Linq
 * @see OrderedEnumerable
 */
@FunctionalInterface
public interface Enumerable<T> extends Iterable<T> {

    @Override
    Iterator<T> iterator();

    /**
     * Returns the number of elements in the sequence as a {@code long}.
     *
     * @return the element count
     */
    default long longCount() {
        long count = 0;
        for (T ignored : this) {
            count++;
        }
        return count;
    }

    /**
     * Returns the number of elements in the sequence.
     *
     * <pre>{@code
     * int count = Linq.from(List.of(1, 2, 3)).count();
     * // 3
     * }</pre>
     *
     * @return the element count
     * @throws ArithmeticException if the count exceeds {@link Integer#MAX_VALUE}
     */
    default int count() {
        long count = longCount();
        if (count > Integer.MAX_VALUE) {
            throw new ArithmeticException("Element count exceeds Integer.MAX_VALUE, count: " + count);
        }
        return (int) count;
    }

    /**
     * Collects all elements into a mutable {@link ArrayList}.
     *
     * <pre>{@code
     * List<Integer> list = Linq.from(List.of(1, 2, 3)).toList();
     * list.add(4); // OK, list is mutable
     * }</pre>
     *
     * @return a new mutable {@code List} containing all elements
     */
    default List<T> toList() {
        List<T> list = new ArrayList<>();
        for (T item : this) {
            list.add(item);
        }
        return list;
    }

    /**
     * Collects all elements into an unmodifiable {@link List}.
     *
     * <p>The returned list throws {@link UnsupportedOperationException}
     * on any modification attempt.</p>
     *
     * <pre>{@code
     * List<Integer> list = Linq.from(List.of(1, 2, 3)).toImmutableList();
     * list.add(4); // throws UnsupportedOperationException
     * }</pre>
     *
     * @return an unmodifiable {@code List} containing all elements
     */
    default List<T> toImmutableList() {
        return List.copyOf(toList());
    }


    /**
     * Collects all elements into a mutable {@link HashSet}.
     * Duplicate elements are removed.
     *
     * <pre>{@code
     * Set<Integer> set = Linq.from(List.of(1, 2, 2, 3)).toSet();
     * // {1, 2, 3}
     * }</pre>
     *
     * @return a new mutable {@code Set} containing all distinct elements
     */
    default Set<T> toSet() {
        HashSet<T> set = new HashSet<>();
        for (T item : this) {
            set.add(item);
        }
        return set;
    }

    /**
     * Computes the sum of {@code int} values projected from each element.
     *
     * <p>Returns 0 for an empty sequence.</p>
     *
     * <p>Using method reference:</p>
     * <pre>{@code
     * int totalLength = Linq.from(List.of("a", "bb", "ccc"))
     *     .sumInt(String::length);
     * // 6
     *
     * int totalAge = Linq.from(users).sumInt(User::getAge);
     * }</pre>
     *
     * @param selector function to extract an {@code int} value from each element
     * @return the sum of the projected values
     * @throws NullPointerException if selector is null
     */
    default int sumInt(ToIntFunction<? super T> selector) {
        Objects.requireNonNull(selector, "Selector cannot be null");
        int sum = 0;
        for (T item : this) {
            sum += selector.applyAsInt(item);
        }
        return sum;
    }

    /**
     * Computes the sum of elements as {@code int}.
     *
     * <p>Elements must be {@link Number} instances. Returns 0 for an empty sequence.</p>
     *
     * <pre>{@code
     * int total = Linq.from(List.of(1, 2, 3)).sumInt();
     * // 6
     * }</pre>
     *
     * @return the sum of all elements
     * @throws ClassCastException if elements are not {@link Number} instances
     */
    default int sumInt() {
        return sumInt(x -> ((Number) x).intValue());
    }

    /**
     * Computes the sum of {@code long} values projected from each element.
     *
     * <p>Returns 0 for an empty sequence.</p>
     *
     * <p>Using method reference:</p>
     * <pre>{@code
     * long totalId = Linq.from(orders).sumLong(Order::getId);
     * }</pre>
     *
     * @param selector function to extract a {@code long} value from each element
     * @return the sum of the projected values
     * @throws NullPointerException if selector is null
     */
    default long sumLong(ToLongFunction<? super T> selector) {
        Objects.requireNonNull(selector, "Selector cannot be null");
        long sum = 0;
        for (T item : this) {
            sum += selector.applyAsLong(item);
        }
        return sum;
    }

    /**
     * Computes the sum of elements as {@code long}.
     *
     * <p>Elements must be {@link Number} instances. Returns 0 for an empty sequence.</p>
     *
     * @return the sum of all elements
     * @throws ClassCastException if elements are not {@link Number} instances
     */
    default long sumLong() {
        return sumLong(x -> ((Number) x).longValue());
    }

    /**
     * Computes the sum of {@code double} values projected from each element.
     *
     * <p>Returns 0.0 for an empty sequence.</p>
     *
     * <p>Using method reference:</p>
     * <pre>{@code
     * double revenue = Linq.from(orders).sumDouble(Order::getTotal);
     *
     * double totalWeight = Linq.from(packages).sumDouble(Package::getWeight);
     * }</pre>
     *
     * @param selector function to extract a {@code double} value from each element
     * @return the sum of the projected values
     * @throws NullPointerException if selector is null
     */
    default double sumDouble(ToDoubleFunction<? super T> selector) {
        Objects.requireNonNull(selector, "Selector cannot be null");
        double sum = 0;
        for (T item : this) {
            sum += selector.applyAsDouble(item);
        }
        return sum;
    }

    /**
     * Computes the sum of elements as {@code double}.
     *
     * <p>Elements must be {@link Number} instances. Returns 0.0 for an empty sequence.</p>
     *
     * @return the sum of all elements
     * @throws ClassCastException if elements are not {@link Number} instances
     */
    default double sumDouble() {
        return sumDouble(x -> ((Number) x).doubleValue());
    }

    /**
     * Computes the average of {@code double} values projected from each element.
     *
     * <p>Using method reference:</p>
     * <pre>{@code
     * double avgLen = Linq.from(List.of("a", "bb", "ccc"))
     *     .average(String::length);
     * // 2.0
     *
     * double avgAge = Linq.from(users).average(User::getAge);
     * }</pre>
     *
     * @param selector function to extract a {@code double} value from each element
     * @return the average of the projected values
     * @throws EmptySequenceException if the sequence is empty
     * @throws NullPointerException   if selector is null
     */
    default double average(ToDoubleFunction<? super T> selector) {
        Objects.requireNonNull(selector, "Selector cannot be null");
        double sum = 0;
        long count = 0;
        for (T item : this) {
            sum += selector.applyAsDouble(item);
            count++;
        }
        if (count == 0) {
            throw new EmptySequenceException();
        }
        return sum / count;
    }

    /**
     * Computes the average of elements as {@code double}.
     *
     * <p>Elements must be {@link Number} instances.</p>
     *
     * <pre>{@code
     * double avg = Linq.from(List.of(10, 20, 30)).average();
     * // 20.0
     * }</pre>
     *
     * @return the average of all elements
     * @throws EmptySequenceException if the sequence is empty
     * @throws ClassCastException     if elements are not {@link Number} instances
     */
    default double average() {
        return average(x -> ((Number) x).doubleValue());
    }

    /**
     * Returns the minimum element using the specified comparator.
     *
     * <p>Using method reference in comparator:</p>
     * <pre>{@code
     * // Shortest string
     * String shortest = Linq.from(List.of("apple", "hi", "banana"))
     *     .min(Comparator.comparingInt(String::length));
     * // "hi"
     *
     * // Cheapest product
     * Product cheapest = Linq.from(products)
     *     .min(Comparator.comparing(Product::getPrice));
     *
     * // Youngest user
     * User youngest = Linq.from(users)
     *     .min(Comparator.comparing(User::getAge));
     * }</pre>
     *
     * @param comparator the comparator to determine element order
     * @return the minimum element
     * @throws EmptySequenceException if the sequence is empty
     * @throws NullPointerException   if comparator is null
     */
    default T min(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        Iterator<T> it = this.iterator();
        if (!it.hasNext()) {
            throw new EmptySequenceException();
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

    /**
     * Returns the minimum element using natural ordering.
     *
     * <p>Elements must implement {@link Comparable}.</p>
     *
     * <pre>{@code
     * int min = Linq.from(List.of(3, 1, 4, 1, 5)).min();
     * // 1
     *
     * String first = Linq.from(List.of("cherry", "apple", "banana")).min();
     * // "apple"
     * }</pre>
     *
     * @return the minimum element
     * @throws EmptySequenceException if the sequence is empty
     * @throws ClassCastException     if elements do not implement {@link Comparable}
     */
    @SuppressWarnings("unchecked")
    default T min() {
        return min((Comparator<? super T>) Comparator.naturalOrder());
    }

    /**
     * Returns the maximum element using the specified comparator.
     *
     * <p>Using method reference in comparator:</p>
     * <pre>{@code
     * // Longest string
     * String longest = Linq.from(List.of("apple", "hi", "banana"))
     *     .max(Comparator.comparingInt(String::length));
     * // "banana"
     *
     * // Most expensive product
     * Product expensive = Linq.from(products)
     *     .max(Comparator.comparing(Product::getPrice));
     * }</pre>
     *
     * @param comparator the comparator to determine element order
     * @return the maximum element
     * @throws EmptySequenceException if the sequence is empty
     * @throws NullPointerException   if comparator is null
     */
    default T max(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        Iterator<T> it = this.iterator();
        if (!it.hasNext()) {
            throw new EmptySequenceException();
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

    /**
     * Returns the maximum element using natural ordering.
     *
     * <p>Elements must implement {@link Comparable}.</p>
     *
     * <pre>{@code
     * int max = Linq.from(List.of(3, 1, 4, 1, 5)).max();
     * // 5
     * }</pre>
     *
     * @return the maximum element
     * @throws EmptySequenceException if the sequence is empty
     * @throws ClassCastException     if elements do not implement {@link Comparable}
     */
    @SuppressWarnings("unchecked")
    default T max() {
        return max((Comparator<? super T>) Comparator.naturalOrder());
    }

    /**
     * Returns the first element of the sequence.
     *
     * <pre>{@code
     * int result = Linq.from(List.of(3, 1, 4)).first();
     * // 3
     * }</pre>
     *
     * @return the first element
     * @throws EmptySequenceException if the sequence is empty
     */
    default T first() {
        Iterator<T> it = this.iterator();
        if (!it.hasNext()) {
            throw new EmptySequenceException();
        }

        return it.next();
    }

    /**
     * Returns the first element that satisfies the predicate.
     *
     * <p>This is a short-circuit operation — it stops iterating
     * as soon as a matching element is found.</p>
     *
     * <pre>{@code
     * int result = Linq.from(List.of(1, 2, 3, 4, 5))
     *     .first(x -> x > 3);
     * // 4
     * }</pre>
     *
     * <p>Using method reference:</p>
     * <pre>{@code
     * // Find first active user
     * User active = Linq.from(users).first(User::isActive);
     * }</pre>
     *
     * <p>Using lambda:</p>
     * <pre>{@code
     * // Find first non-empty string
     * String s = Linq.from(strings).first(s -> !s.isEmpty());
     * }</pre>
     *
     * @param predicate condition to test each element
     * @return the first matching element
     * @throws EmptySequenceException if no element satisfies the predicate
     * @throws NullPointerException   if predicate is null
     */
    default T first(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "Predicate cannot be null");
        Iterator<T> it = this.iterator();
        if (!it.hasNext()) {
            throw new EmptySequenceException();
        }

        for (T item : this) {
            if (predicate.test(item)) {
                return item;
            }
        }

        throw new EmptySequenceException("Sequence contains no matching element");
    }

    /**
     * Returns the last element of the sequence.
     *
     * <p>This requires iterating through the entire sequence.</p>
     *
     * <pre>{@code
     * int result = Linq.from(List.of(3, 1, 4)).last();
     * // 4
     * }</pre>
     *
     * @return the last element
     * @throws EmptySequenceException if the sequence is empty
     */
    default T last() {
        Iterator<T> it = this.iterator();
        if (!it.hasNext()) {
            throw new EmptySequenceException();
        }

        T result = it.next();
        while (it.hasNext()) {
            result = it.next();
        }
        return result;
    }

    /**
     * Returns the last element that satisfies the predicate.
     *
     * <p>This requires iterating through the entire sequence.</p>
     *
     * <pre>{@code
     * int result = Linq.from(List.of(1, 2, 3, 4, 5))
     *     .last(x -> x < 4);
     * // 3
     * }</pre>
     *
     * <p>Using method reference:</p>
     * <pre>{@code
     * // Find last active user
     * User lastActive = Linq.from(users).last(User::isActive);
     * }</pre>
     *
     * @param predicate condition to test each element
     * @return the last matching element
     * @throws EmptySequenceException if no element satisfies the predicate
     * @throws NullPointerException   if predicate is null
     */
    default T last(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "Predicate cannot be null");
        boolean found = false;
        T result = null;
        Iterator<T> it = this.iterator();
        if (!it.hasNext()) {
            throw new EmptySequenceException();
        }

        for (T item : this) {
            if (predicate.test(item)) {
                result = item;
                found = true;
            }
        }
        if (!found) {
            throw new EmptySequenceException("Sequence contains no matching element");
        }
        return result;
    }

    /**
     * Returns the only element of the sequence.
     * Throws if the sequence is empty or contains more than one element.
     *
     * <pre>{@code
     * int result = Linq.from(List.of(42)).single();
     * // 42
     * }</pre>
     *
     * @return the single element
     * @throws EmptySequenceException    if the sequence is empty
     * @throws MultipleElementsException if the sequence contains more than one element
     */
    default T single() {
        Iterator<T> it = this.iterator();
        if (!it.hasNext()) {
            throw new EmptySequenceException();
        }

        T item = it.next();
        if (it.hasNext()) {
            throw new MultipleElementsException();
        }
        return item;
    }

    /**
     * Returns the only element that satisfies the predicate.
     * Throws if no element matches or more than one element matches.
     *
     * <pre>{@code
     * int result = Linq.from(List.of(1, 2, 3, 4, 5))
     *     .single(x -> x == 3);
     * // 3
     * }</pre>
     *
     * <p>Using method reference (when exactly one element matches):</p>
     * <pre>{@code
     * // Find the unique admin (throws if zero or multiple admins)
     * User admin = Linq.from(users).single(User::isAdmin);
     * }</pre>
     *
     * @param predicate condition to test each element
     * @return the single matching element
     * @throws EmptySequenceException    if no element satisfies the predicate
     * @throws MultipleElementsException if more than one element satisfies the predicate
     * @throws NullPointerException      if predicate is null
     */
    default T single(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "Predicate cannot be null");
        boolean found = false;
        T result = null;
        Iterator<T> it = this.iterator();
        if (!it.hasNext()) {
            throw new EmptySequenceException();
        }

        for (T item : this) {
            if (predicate.test(item)) {
                if (found) {
                    throw new MultipleElementsException("Sequence contains more than one matching element");
                }
                result = item;
                found = true;
            }
        }
        if (!found) {
            throw new EmptySequenceException("Sequence contains no matching element");
        }
        return result;
    }

    /**
     * Determines whether the sequence contains any elements.
     *
     * <p>This is a short-circuit operation — it returns {@code true}
     * as soon as one element is found.</p>
     *
     * <pre>{@code
     * boolean hasItems = Linq.from(List.of(1, 2, 3)).any();
     * // true
     *
     * boolean empty = Linq.from(List.of()).any();
     * // false
     * }</pre>
     *
     * @return {@code true} if the sequence contains at least one element
     */
    default boolean any() {
        return iterator().hasNext();
    }

    /**
     * Determines whether any element satisfies the predicate.
     *
     * <p>This is a short-circuit operation — it returns {@code true}
     * as soon as a matching element is found.</p>
     *
     * <pre>{@code
     * boolean hasEven = Linq.from(List.of(1, 2, 3)).any(x -> x % 2 == 0);
     * // true
     * }</pre>
     *
     * <p>Using method reference:</p>
     * <pre>{@code
     * // Check if any user is active
     * boolean hasActive = Linq.from(users).any(User::isActive);
     *
     * // Check if any string is empty
     * boolean hasEmpty = Linq.from(strings).any(String::isEmpty);
     * }</pre>
     *
     * @param predicate condition to test each element
     * @return {@code true} if any element satisfies the predicate
     * @throws NullPointerException if predicate is null
     */
    default boolean any(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "Predicate cannot be null");
        for (T item : this) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether all elements satisfy the predicate.
     *
     * <p>This is a short-circuit operation — it returns {@code false}
     * as soon as a non-matching element is found.</p>
     *
     * <p>Returns {@code true} for an empty sequence.</p>
     *
     * <pre>{@code
     * boolean allPositive = Linq.from(List.of(1, 2, 3)).all(x -> x > 0);
     * // true
     * }</pre>
     *
     * <p>Using method reference:</p>
     * <pre>{@code
     * // Check if all users are active
     * boolean allActive = Linq.from(users).all(User::isActive);
     *
     * // Check if all tasks are completed
     * boolean allDone = Linq.from(tasks).all(Task::isCompleted);
     * }</pre>
     *
     * @param predicate condition to test each element
     * @return {@code true} if all elements satisfy the predicate, or the sequence is empty
     * @throws NullPointerException if predicate is null
     */
    default boolean all(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "Predicate cannot be null");
        for (T item : this) {
            if (!predicate.test(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether the sequence contains the specified element.
     *
     * <p>Comparison is done using {@link Objects#equals}, which is null-safe.</p>
     *
     * <pre>{@code
     * boolean has2 = Linq.from(List.of(1, 2, 3)).contains(2);
     * // true
     *
     * boolean hasNull = Linq.from(listWithNulls).contains(null);
     * // true if list contains null
     * }</pre>
     *
     * @param item the element to search for (may be null)
     * @return {@code true} if the element is found in the sequence
     */
    default boolean contains(T item) {
        for (T element : this) {
            if (Objects.equals(element, item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Collects all elements into a mutable {@link HashMap} using the specified
     * key and value selector functions.
     *
     * <p>Simple example:</p>
     * <pre>{@code
     * Map<Integer, String> map = Linq.from(List.of("a", "bb", "ccc"))
     *     .toMap(String::length, x -> x);
     * // {1: "a", 2: "bb", 3: "ccc"}
     * }</pre>
     *
     * <p>Using method references for both key and value:</p>
     * <pre>{@code
     * // User ID -> User name
     * Map<Integer, String> nameMap = Linq.from(users)
     *     .toMap(User::getId, User::getName);
     *
     * // User ID -> User object
     * Map<Integer, User> userMap = Linq.from(users)
     *     .toMap(User::getId, Function.identity());
     * }</pre>
     *
     * @param keySelector   function to extract the map key from each element
     * @param valueSelector function to extract the map value from each element
     * @param <K>           the type of map keys
     * @param <V>           the type of map values
     * @return a new mutable {@code Map}
     * @throws IllegalStateException if duplicate keys are encountered
     * @throws NullPointerException  if keySelector or valueSelector is null
     */
    default <K, V> Map<K, V> toMap(
            Function<? super T, ? extends K> keySelector,
            Function<? super T, ? extends V> valueSelector
    ) {
        Objects.requireNonNull(keySelector, "keySelector cannot be null");
        Objects.requireNonNull(valueSelector, "valueSelector cannot be null");
        Map<K, V> map = new HashMap<>();
        for (T item : this) {
            K key = keySelector.apply(item);
            V value = valueSelector.apply(item);
            if (map.containsKey(key)) {
                throw new IllegalStateException("Duplicate key found: " + key);
            }
            map.put(key, value);
        }
        return map;
    }

    /**
     * Projects each element to an {@link Iterable} and flattens the resulting sequences into one.
     *
     * <p>This is the equivalent of C# LINQ's {@code SelectMany} or Java Stream's {@code flatMap}.</p>
     *
     * <p>Flatten nested lists:</p>
     * <pre>{@code
     * List<List<Integer>> source = List.of(
     *     List.of(1, 2),
     *     List.of(3, 4),
     *     List.of(5)
     * );
     * List<Integer> result = Linq.from(source)
     *     .selectMany(x -> x)
     *     .toList();
     * // [1, 2, 3, 4, 5]
     * }</pre>
     *
     * <p>Empty inner sequences are safely skipped:</p>
     * <pre>{@code
     * List<List<Integer>> source = List.of(
     *     List.of(1, 2),
     *     List.of(),
     *     List.of(3)
     * );
     * List<Integer> result = Linq.from(source)
     *     .selectMany(x -> x)
     *     .toList();
     * // [1, 2, 3]
     * }</pre>
     *
     * <p>Using method reference to extract a collection field:</p>
     * <pre>{@code
     * // Get all orders from all users
     * Linq.from(users).selectMany(User::getOrders).toList();
     *
     * // Get all tags from all articles
     * Linq.from(articles).selectMany(Article::getTags).toList();
     * }</pre>
     *
     * <p>Flatten with further filtering:</p>
     * <pre>{@code
     * List<Order> expensiveOrders = Linq.from(users)
     *     .selectMany(User::getOrders)
     *     .where(order -> order.getTotal() > 100)
     *     .toList();
     * }</pre>
     *
     * <p><strong>Note on type inference:</strong> When inline nested lists contain
     * {@code List.of()} (empty list with no type hint), Java cannot infer the
     * element type. In that case, declare the source variable with an explicit type:</p>
     * <pre>{@code
     * // This does not compile — List.of() gives no type information:
     * // Linq.from(List.of(List.of(1, 2), List.of(), List.of(3)))
     * //     .selectMany(x -> x)
     * //     .toList();
     *
     * // Fix: declare source type explicitly
     * List<List<Integer>> source = List.of(
     *     List.of(1, 2),
     *     List.of(),
     *     List.of(3)
     * );
     * List<Integer> result = Linq.from(source)
     *     .selectMany(x -> x)
     *     .toList();
     * // [1, 2, 3]
     *
     * // Or use
     * Linq.from(List.of(
     *     List.of(1, 2),
     *     List.<Integer>of(),
     *     List.of(3)
     * ))
     *     .selectMany(x -> x)
     *     .toList();
     * // [1, 2, 3]
     * }</pre>
     *
     * @param selector function that maps each element to an {@code Iterable}
     * @param <R>      the type of elements in the resulting flattened sequence
     * @return a new {@code Enumerable} containing all elements from all inner sequences
     * @throws NullPointerException if selector is null
     */
    default <R> Enumerable<R> selectMany(
            Function<? super T, ? extends Iterable<? extends R>> selector) {
        Objects.requireNonNull(selector, "selector cannot be null");
        return () -> new SelectManyIterator<>(this.iterator(), selector);
    }

    /**
     * Groups elements by a key.
     *
     * <p>Returns a sequence of {@link Grouping} objects, each containing a key
     * and the elements that share that key. Group order follows first-key-appearance
     * order, and element order within each group is preserved.</p>
     *
     * <p><strong>Note:</strong> Internally collects all elements into memory when iteration
     * begins, as grouping requires seeing all elements first.</p>
     *
     * <p>Group and count:</p>
     * <pre>{@code
     * Linq.from(List.of("apple", "avocado", "banana", "blueberry"))
     *     .groupBy(s -> s.charAt(0))
     *     .select(g -> g.getKey() + ": " + g.getElements().size())
     *     .toList();
     * // ["a: 2", "b: 2"]
     * }</pre>
     *
     * <p>Group by property using method reference:</p>
     * <pre>{@code
     * // Group users by department
     * Linq.from(users).groupBy(User::getDepartment).toList();
     *
     * // Group orders by status
     * Linq.from(orders).groupBy(Order::getStatus).toList();
     *
     * // Group strings by length
     * Linq.from(strings).groupBy(String::length).toList();
     * }</pre>
     *
     * @param keySelector function to extract the grouping key
     * @param <K>         the type of the grouping key
     * @return a new {@code Enumerable} of {@link Grouping} objects
     * @throws NullPointerException if keySelector is null
     */
    default <K> Enumerable<Grouping<K, T>> groupBy(Function<? super T, ? extends K> keySelector) {
        Objects.requireNonNull(keySelector, "keySelector cannot be null");
        return () -> {
            Map<K, List<T>> map = new LinkedHashMap<>();
            for (T item : this) {
                K key = keySelector.apply(item);
                map.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
            }
            return map.entrySet().stream().map(e -> new Grouping<>(e.getKey(), e.getValue())).iterator();
        };
    }

    /**
     * Filters elements based on a predicate.
     *
     * <p>Simple single-line predicate:</p>
     * <pre>{@code
     * List<Integer> result = Linq.from(List.of(1, 2, 3, 4, 5))
     *     .where(x -> x > 3)
     *     .toList();
     * // [4, 5]
     * }</pre>
     *
     * <p>Multi-line predicate with complex logic:</p>
     * <pre>{@code
     * List<User> result = Linq.from(users)
     *     .where(user -> {
     *         if (user.getAge() < 18) {
     *             return false;
     *         }
     *         return user.isActive() && user.getRole().equals("admin");
     *     })
     *     .toList();
     * }</pre>
     *
     * <p>Chaining multiple where calls:</p>
     * <pre>{@code
     * List<Integer> result = Linq.from(List.of(1, 2, 3, 4, 5, 6))
     *     .where(x -> x > 2)
     *     .where(x -> x < 6)
     *     .toList();
     * // [3, 4, 5]
     * }</pre>
     *
     * <p>Using method reference:</p>
     * <pre>{@code
     * // Filter non-null elements
     * Linq.from(list).where(Objects::nonNull).toList();
     *
     * // Filter active users
     * Linq.from(users).where(User::isActive).toList();
     * }</pre>
     *
     * @param predicate condition to test each element
     * @return a new {@code Enumerable} containing only elements that satisfy the predicate
     * @throws NullPointerException if predicate is null
     */
    default Enumerable<T> where(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "Predicate cannot be null");
        return () -> new WhereIterator<>(this.iterator(), (item, i) -> predicate.test(item));
    }

    /**
     * Filters elements based on a predicate that receives the element's index.
     *
     * <p>The index starts at 0 and increments for each element evaluated,
     * regardless of whether the element passes the filter.</p>
     *
     * <p>Simple index-based filtering:</p>
     * <pre>{@code
     * List<String> result = Linq.from(List.of("a", "b", "c", "d"))
     *     .where((item, i) -> i < 2)
     *     .toList();
     * // ["a", "b"]
     * }</pre>
     *
     * <p>Using both element and index:</p>
     * <pre>{@code
     * List<String> result = Linq.from(List.of("apple", "banana", "cherry"))
     *     .where((item, i) -> {
     *         // only take items at even indices with length > 3
     *         return i % 2 == 0 && item.length() > 3;
     *     })
     *     .toList();
     * // ["apple", "cherry"]
     * }</pre>
     *
     * @param predicate condition to test each element with its index
     * @return a new {@code Enumerable} containing only elements that satisfy the predicate
     * @throws NullPointerException if predicate is null
     */
    default Enumerable<T> where(IndexedPredicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "Predicate cannot be null");
        return () -> new WhereIterator<>(this.iterator(), predicate);
    }

    /**
     * Transforms each element into a new form.
     *
     * <p>Simple transformation:</p>
     * <pre>{@code
     * List<String> result = Linq.from(List.of(1, 2, 3))
     *     .select(x -> "No." + x)
     *     .toList();
     * // ["No.1", "No.2", "No.3"]
     * }</pre>
     *
     * <p>Using method reference:</p>
     * <pre>{@code
     * // Extract string length
     * Linq.from(List.of("a", "bb", "ccc")).select(String::length).toList();
     * // [1, 2, 3]
     *
     * // Get user names
     * Linq.from(users).select(User::getName).toList();
     *
     * // Convert to string
     * Linq.from(List.of(1, 2, 3)).select(String::valueOf).toList();
     * // ["1", "2", "3"]
     * }</pre>
     *
     * <p>Using lambda when method reference is ambiguous or not available:</p>
     * <pre>{@code
     * // Convert to uppercase (String::toUpperCase is ambiguous due to overload)
     * Linq.from(List.of("hello", "world")).select(s -> s.toUpperCase()).toList();
     * // ["HELLO", "WORLD"]
     * }</pre>
     *
     * <p>Multi-line transformation:</p>
     * <pre>{@code
     * List<UserDTO> result = Linq.from(users)
     *     .select(user -> {
     *         UserDTO dto = new UserDTO();
     *         dto.setName(user.getName());
     *         dto.setAge(user.getAge());
     *         return dto;
     *     })
     *     .toList();
     * }</pre>
     *
     * @param selector function to transform each element
     * @param <R>      the type of elements in the resulting sequence
     * @return a new {@code Enumerable} containing the transformed elements
     * @throws NullPointerException if selector is null
     */
    default <R> Enumerable<R> select(Function<? super T, ? extends R> selector) {
        Objects.requireNonNull(selector, "Selector cannot be null");
        return () -> new SelectIterator<>(this.iterator(), (item, i) -> selector.apply(item));
    }

    /**
     * Transforms each element into a new form using a function that receives the element's index.
     *
     * <pre>{@code
     * List<String> result = Linq.from(List.of("Alice", "Bob", "Carol"))
     *     .select((name, i) -> (i + 1) + ". " + name)
     *     .toList();
     * // ["1. Alice", "2. Bob", "3. Carol"]
     * }</pre>
     *
     * @param selector function to transform each element with its index
     * @param <R>      the type of elements in the resulting sequence
     * @return a new {@code Enumerable} containing the transformed elements
     * @throws NullPointerException if selector is null
     */
    default <R> Enumerable<R> select(IndexedFunction<? super T, ? extends R> selector) {
        Objects.requireNonNull(selector, "Selector cannot be null");
        return () -> new SelectIterator<>(this.iterator(), selector);
    }

    /**
     * Concatenates another {@link Iterable} to the end of this sequence.
     *
     * <p>Accepts any {@code Iterable} directly, including {@code List}, {@code Set},
     * and {@code Enumerable}.</p>
     *
     * <pre>{@code
     * // Append a List directly
     * List<Integer> result = Linq.from(List.of(1, 2))
     *     .concat(List.of(3, 4))
     *     .toList();
     * // [1, 2, 3, 4]
     *
     * // Append an Enumerable (with further filtering)
     * Linq.from(List.of(1, 2))
     *     .concat(Linq.from(List.of(3, 4, 5)).where(x -> x > 3))
     *     .toList();
     * // [1, 2, 4, 5]
     *
     * // Append any Iterable (e.g. MyBatis result)
     * Linq.from(localUsers).concat(userMapper.selectAll()).toList();
     * }</pre>
     *
     * @param other the iterable to append
     * @return a new {@code Enumerable} that yields elements from this sequence followed by elements from other
     * @throws NullPointerException if other is null
     */
    default Enumerable<T> concat(Iterable<T> other) {
        Objects.requireNonNull(other, "Other cannot be null");
        return () -> new ConcatIterator<>(this.iterator(), other.iterator());
    }

    /**
     * Returns the first {@code count} elements from the sequence.
     *
     * <p>If the sequence contains fewer than {@code count} elements,
     * all elements are returned without error.</p>
     *
     * <pre>{@code
     * List<Integer> result = Linq.from(List.of(1, 2, 3, 4, 5))
     *     .take(3)
     *     .toList();
     * // [1, 2, 3]
     * }</pre>
     *
     * @param count the maximum number of elements to take
     * @return a new {@code Enumerable} containing at most {@code count} elements
     * @throws IllegalArgumentException if count is negative
     */
    default Enumerable<T> take(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot be negative");
        }
        return () -> new TakeIterator<>(this.iterator(), count);
    }

    /**
     * Returns elements from the start of the sequence as long as the predicate is satisfied.
     * Stops at the first element that does not satisfy the predicate, and does not
     * evaluate any further elements.
     *
     * <p>This differs from {@link #where}: {@code where} checks all elements,
     * while {@code takeWhile} stops at the first failure.</p>
     *
     * <pre>{@code
     * List<Integer> result = Linq.from(List.of(1, 2, 5, 2, 3))
     *     .takeWhile(x -> x < 5)
     *     .toList();
     * // [1, 2] — stops at 5, does not include the later 2 and 3
     * }</pre>
     *
     * <p>Using lambda:</p>
     * <pre>{@code
     * // Take while strings are non-empty
     * Linq.from(List.of("a", "b", "", "c"))
     *     .takeWhile(s -> !s.isEmpty())
     *     .toList();
     * // ["a", "b"]
     * }</pre>
     *
     * @param predicate condition to test each element
     * @return a new {@code Enumerable} containing elements from the start while the predicate holds
     * @throws NullPointerException if predicate is null
     */
    default Enumerable<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "Predicate cannot be null");
        return () -> new TakeWhileIterator<>(this.iterator(), predicate);
    }

    /**
     * Skips the first {@code count} elements and returns the rest.
     *
     * <p>If the sequence contains fewer than {@code count} elements,
     * an empty sequence is returned.</p>
     *
     * <pre>{@code
     * List<Integer> result = Linq.from(List.of(1, 2, 3, 4, 5))
     *     .skip(2)
     *     .toList();
     * // [3, 4, 5]
     * }</pre>
     *
     * <p>Combine with {@link #take} for pagination:</p>
     * <pre>{@code
     * int pageSize = 10;
     * int page = 2; // zero-based
     * List<User> pageData = Linq.from(users)
     *     .skip(page * pageSize)
     *     .take(pageSize)
     *     .toList();
     * }</pre>
     *
     * @param count the number of elements to skip
     * @return a new {@code Enumerable} with the first {@code count} elements removed
     * @throws IllegalArgumentException if count is negative
     */
    default Enumerable<T> skip(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot be negative");
        }
        return () -> new SkipIterator<>(this.iterator(), count);
    }

    /**
     * Skips elements from the start of the sequence as long as the predicate is satisfied,
     * then returns all remaining elements (even if they would satisfy the predicate).
     *
     * <p>This differs from {@link #where}: {@code where} excludes non-matching elements
     * throughout the entire sequence, while {@code skipWhile} only skips the initial
     * consecutive matching elements, then takes everything after.</p>
     *
     * <pre>{@code
     * List<Integer> result = Linq.from(List.of(1, 2, 5, 1, 6))
     *     .skipWhile(x -> x < 3)
     *     .toList();
     * // [5, 1, 6] — 1 appears again but is included because skipping already stopped
     * }</pre>
     *
     * @param predicate condition to test each element
     * @return a new {@code Enumerable} with the initial matching elements removed
     * @throws NullPointerException if predicate is null
     */
    default Enumerable<T> skipWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "Predicate cannot be null");
        return () -> new SkipWhileIterator<>(this.iterator(), predicate);
    }

    /**
     * Returns distinct elements from the sequence, removing duplicates.
     *
     * <p>Uniqueness is determined by {@link Object#equals} and {@link Object#hashCode}.
     * The order of first appearance is preserved.</p>
     *
     * <pre>{@code
     * List<Integer> result = Linq.from(List.of(1, 2, 2, 3, 3, 3))
     *     .distinct()
     *     .toList();
     * // [1, 2, 3]
     * }</pre>
     *
     * <p>Combined with select for unique derived values:</p>
     * <pre>{@code
     * // Get unique string lengths
     * Linq.from(List.of("a", "bb", "c", "dd"))
     *     .select(String::length)
     *     .distinct()
     *     .toList();
     * // [1, 2]
     * }</pre>
     *
     * @return a new {@code Enumerable} containing only distinct elements
     */
    default Enumerable<T> distinct() {
        return () -> new DistinctIterator<>(this.iterator());
    }

    /**
     * Sorts elements using the specified {@link Comparator}.
     *
     * <pre>{@code
     * List<String> result = Linq.from(List.of("banana", "hi", "apple"))
     *     .orderBy(Comparator.comparingInt(String::length))
     *     .toList();
     * // ["hi", "apple", "banana"]
     * }</pre>
     *
     * @param comparator the comparator to determine element order
     * @return an {@link OrderedEnumerable} with elements sorted by the comparator
     * @throws NullPointerException if comparator is null
     */
    default OrderedEnumerable<T> orderBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        return new OrderedEnumerableImpl<>(this, comparator);
    }

    /**
     * Sorts elements in ascending order by the specified key.
     *
     * <p><strong>Note:</strong> Internally collects all elements into memory when iteration
     * begins, as sorting requires seeing all elements first.</p>
     *
     * <p>Returns an {@link OrderedEnumerable}, which allows further sorting
     * with {@link OrderedEnumerable#thenBy}.</p>
     *
     * <p>Simple sort:</p>
     * <pre>{@code
     * List<String> result = Linq.from(List.of("banana", "apple", "cherry"))
     *     .orderBy(x -> x)
     *     .toList();
     * // ["apple", "banana", "cherry"]
     * }</pre>
     *
     * <p>Sort by property using method reference:</p>
     * <pre>{@code
     * // Sort users by name
     * Linq.from(users).orderBy(User::getName).toList();
     *
     * // Sort strings by length
     * Linq.from(strings).orderBy(String::length).toList();
     *
     * // Sort products by price
     * Linq.from(products).orderBy(Product::getPrice).toList();
     * }</pre>
     *
     * @param keySelector function to extract the sort key
     * @param <K>         the type of the sort key, must be {@link Comparable}
     * @return an {@link OrderedEnumerable} with elements sorted by the key
     * @throws NullPointerException if keySelector is null
     */
    default <K extends Comparable<? super K>> OrderedEnumerable<T> orderBy(Function<? super T, ? extends K> keySelector) {
        Objects.requireNonNull(keySelector, "KeySelector cannot be null");
        return orderBy(Comparator.comparing(keySelector));
    }

    /**
     * Sorts elements in descending order by the specified key.
     *
     * <pre>{@code
     * List<Integer> result = Linq.from(List.of(3, 1, 4, 1, 5))
     *     .orderByDescending(x -> x)
     *     .toList();
     * // [5, 4, 3, 1, 1]
     * }</pre>
     *
     * <p>Using method reference:</p>
     * <pre>{@code
     * // Most recent orders first
     * Linq.from(orders).orderByDescending(Order::getCreatedAt).toList();
     *
     * // Highest price first
     * Linq.from(products).orderByDescending(Product::getPrice).toList();
     * }</pre>
     *
     * @param keySelector function to extract the sort key
     * @param <K>         the type of the sort key, must be {@link Comparable}
     * @return an {@link OrderedEnumerable} with elements sorted in descending order
     * @throws NullPointerException if keySelector is null
     */
    default <K extends Comparable<? super K>> OrderedEnumerable<T> orderByDescending(Function<? super T, ? extends K> keySelector) {
        Objects.requireNonNull(keySelector, "KeySelector cannot be null");
        return orderBy(Comparator.comparing(keySelector).reversed());
    }
}
