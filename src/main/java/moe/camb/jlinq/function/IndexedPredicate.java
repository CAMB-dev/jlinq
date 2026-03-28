package moe.camb.jlinq.function;

@FunctionalInterface
public interface IndexedPredicate<T> {
    boolean test(T element, int index);
}
