package moe.camb.jlinq.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class SkipWhileIterator<T> implements Iterator<T> {
    private final Iterator<T> source;
    private final Predicate<? super T> predicate;
    private T nextItem;
    private boolean found = false;
    private boolean doneSkipping = false;

    public SkipWhileIterator(Iterator<T> source, Predicate<? super T> predicate) {
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public boolean hasNext() {
        if (found) {
            return true;
        }
        while (source.hasNext()) {
            T candidate = source.next();
            if (doneSkipping) {
                nextItem = candidate;
                found = true;
                return true;
            }
            if (!predicate.test(candidate)) {
                doneSkipping = true;
                nextItem = candidate;
                found = true;
                return true;
            }
        }
        return false;
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
