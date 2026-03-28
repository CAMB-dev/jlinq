package moe.camb.jlinq.iterator;

import moe.camb.jlinq.function.IndexedPredicate;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class WhereIterator<T> implements Iterator<T> {
    private final Iterator<T> source;
    private final IndexedPredicate<? super T> predicate;
    private int index = 0;
    private T nextItem;
    private boolean found = false;

    public WhereIterator(Iterator<T> source, IndexedPredicate<? super T> predicate) {
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public boolean hasNext() {
        while (!found && source.hasNext()) {
            T candidate = source.next();
            if (predicate.test(candidate, index++)) {
                nextItem = candidate;
                found = true;
            }
        }
        return found;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        found = false;
        return nextItem;
    }
}
