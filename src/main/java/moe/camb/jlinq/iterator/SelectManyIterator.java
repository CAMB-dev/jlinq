package moe.camb.jlinq.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SelectManyIterator<T, R> implements Iterator<R> {

    private final Iterator<T> source;
    private final java.util.function.Function<? super T, ? extends Iterable<? extends R>> selector;
    private Iterator<? extends R> current = null;

    public SelectManyIterator(Iterator<T> source,
                              java.util.function.Function<? super T, ? extends Iterable<? extends R>> selector) {
        this.source = source;
        this.selector = selector;
    }

    @Override
    public boolean hasNext() {
        // 如果当前子迭代器还有元素，直接返回 true
        while (current == null || !current.hasNext()) {
            // 当前子迭代器耗尽，去拿下一个源元素
            if (!source.hasNext()) {
                return false;
            }
            current = selector.apply(source.next()).iterator();
        }
        return true;
    }

    @Override
    public R next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return current.next();
    }
}