package moe.camb.jlinq.function;

@FunctionalInterface
public interface IndexedFunction<T, R> {
    R apply(T element, int index);
}
