package moe.camb.jlinq.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SkipIterator<T> implements Iterator<T> {
    private final Iterator<T> source;
    private final int count;
    private boolean skipped = false;

    public SkipIterator(Iterator<T> source, int count) {
        this.source = source;
        this.count = count;
    }

    private void ensureSkipped() {
        if (!skipped) {
            int remaining = count;
            while (remaining > 0 && source.hasNext()) {
                source.next();
                remaining--;
            }
            skipped = true;
        }
    }

    @Override
    public boolean hasNext() {
        ensureSkipped();
        return source.hasNext();
    }

    @Override
    public T next() {
        ensureSkipped();
        if (!source.hasNext()) {
            throw new NoSuchElementException();
        }
        return source.next();
    }
}
