package fr.solsid.model;

import fr.solsid.exception.PoolMaxSizeExceededExcepton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Arnaud on 07/06/2017.
 */
public class Pool<T> implements Iterable<T> {

    private final List<T> elements = new ArrayList<>();
    private int maxSize;

    public Pool(int maxSize) {
        this.maxSize = maxSize;
    }

    public boolean add(T element) {
        if (elements.size() == maxSize)
            return false;
        return this.elements.add(element);
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return elements.iterator();
    }

    public List<T> asList() {
        return new ArrayList<>(elements);
    }
}
