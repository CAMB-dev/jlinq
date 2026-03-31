package moe.camb.jlinq;

import java.util.List;

public class Grouping<K,T> {
    private final K key;
    private final List<T> elements;

    public Grouping(K key, List<T> elements) {
        this.key = key;
        this.elements = elements;
    }

    public K getKey() {
        return key;
    }

    public List<T> getElements() {
        return elements;
    }

    public Enumerable<T> asEnumerable() {
        return elements::iterator;
    }
}
