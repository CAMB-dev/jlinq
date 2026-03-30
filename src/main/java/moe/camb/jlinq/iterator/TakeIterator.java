package moe.camb.jlinq.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TakeIterator<T> implements Iterator<T> {
    private final Iterator<T> source;
    private final int count;
    private int taken = 0;

    public TakeIterator(Iterator<T> source, int count) {
        this.source = source;
        this.count = count;
    }

    @Override
    public boolean hasNext() {
        return taken < count && source.hasNext();
    }

    @Override
    public T next() {
        if(!hasNext()) {
            throw new NoSuchElementException();
        }
        taken++;
        return source.next();
    }
}
