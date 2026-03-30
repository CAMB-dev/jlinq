package moe.camb.jlinq.iterator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class DistinctIterator<T> implements Iterator<T> {
    private final Iterator<T> source;
    private final Set<T> seen = new HashSet<T>();
    private T nextItem = null;
    private boolean found = false;

    public DistinctIterator(Iterator<T> source) {
        this.source = source;
    }

    @Override
    public boolean hasNext() {
        while (!found && source.hasNext()) {
            T candidate = source.next();
            if (seen.add(candidate)) {
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
