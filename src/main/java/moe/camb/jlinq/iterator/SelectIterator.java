package moe.camb.jlinq.iterator;

import moe.camb.jlinq.function.IndexedFunction;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SelectIterator<T, R> implements Iterator<R> {

    private final Iterator<T> source;
    private final IndexedFunction<? super T, ? extends R> selector;
    private int index = 0;

    public SelectIterator(Iterator<T> source, IndexedFunction<? super T, ? extends R> selector) {
        this.source = source;
        this.selector = selector;
    }

    @Override
    public boolean hasNext() {
        return source.hasNext();
    }

    @Override
    public R next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return selector.apply(source.next(), index++);
    }
}
