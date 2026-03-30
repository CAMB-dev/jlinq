package moe.camb.jlinq.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class TakeWhileIterator<T> implements Iterator<T> {
    private final Iterator<T> source;
    private final Predicate<? super T> predicate;
    private T nextItem;
    private boolean stopped = false;
    private boolean found = false;

    public TakeWhileIterator(Iterator<T> source, Predicate<? super T> predicate) {
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public boolean hasNext() {
        if (stopped) {
            return false;
        }
        if (!found && source.hasNext()) {
            T candidate = source.next();
            if (predicate.test(candidate)) {
                nextItem = candidate;
                found = true;
            } else {
                stopped = true;
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
