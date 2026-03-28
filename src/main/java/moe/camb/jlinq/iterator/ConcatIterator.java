package moe.camb.jlinq.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ConcatIterator<T> implements Iterator<T> {

    private final Iterator<T> first;
    private final Iterator<T> second;

    public ConcatIterator(Iterator<T> first, Iterator<T> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean hasNext() {
        return first.hasNext() || second.hasNext();
    }

    @Override
    public T next() {
        if (first.hasNext()) return first.next();
        if (second.hasNext()) return second.next();
        throw new NoSuchElementException();
    }
}
