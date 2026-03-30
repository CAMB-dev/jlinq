package moe.camb.jlinq;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public final class Linq {
    private Linq() {
    }

    public static <T> Enumerable<T> from(Iterable<T> source) {
        Objects.requireNonNull(source, "source cannot be null");
        return source::iterator;
    }

    public static <T> Enumerable<T> from(T[] source) {
        Objects.requireNonNull(source, "source cannot be null");
        return Arrays.asList(source)::iterator;
    }

    public static <T> Enumerable<T> from(Stream<T> source) {
        Objects.requireNonNull(source, "source cannot be null");
        return source.toList()::iterator;
    }

    @SafeVarargs
    public static <T> Enumerable<T> of(T... items) {
        Objects.requireNonNull(items, "items cannot be null");
        return () -> Arrays.asList(items).iterator();
    }
}
