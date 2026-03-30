package moe.camb.jlinq.impl;

import moe.camb.jlinq.Enumerable;
import moe.camb.jlinq.OrderedEnumerable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class OrderedEnumerableImpl<T> implements OrderedEnumerable<T> {
    private final Enumerable<T> source;
    private final Comparator<? super T> comparator;

    public OrderedEnumerableImpl(Enumerable<T> source, Comparator<? super T> comparator) {
        this.source = source;
        this.comparator = comparator;
    }

    @Override
    public Iterator<T> iterator() {
        List<T> list = new ArrayList<>();
        for (T item : source) {
            list.add(item);
        }
        list.sort(comparator);
        return list.iterator();
    }

    @Override
    public Comparator<? super T> comparator() {
        return comparator;
    }

    @Override
    public OrderedEnumerable<T> createOrdered(Comparator<? super T> newComparator) {
        return new OrderedEnumerableImpl<>(source, newComparator);
    }
}
