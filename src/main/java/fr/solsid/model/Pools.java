package fr.solsid.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Arnaud on 07/06/2017.
 */
public class Pools<T> implements Iterable<Pool<T>> {

    private final List<Pool<T>> pools = new ArrayList<>();
    private int maxPoolSize;

    public Pools(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void add(T element) {
        for (Pool<T> pool : pools) {
            if (pool.add(element)) {
                return;
            }
        }
        Pool<T> newPool = new Pool<>(maxPoolSize);
        newPool.add(element);
        pools.add(newPool);
    }

    public boolean isEmpty() {
        if (pools.isEmpty())
            return true;
        boolean empty = true;
        for (Pool<T> pool : pools) {
            if (!pool.isEmpty()) {
                empty = false;
                break;
            }
        }
        return empty;
    }

    public Pool<T> getPool(int index) {
        return pools.get(index);
    }

    @Override
    public Iterator<Pool<T>> iterator() {
        return pools.iterator();
    }

    public int size() {
        return pools.size();
    }
}
