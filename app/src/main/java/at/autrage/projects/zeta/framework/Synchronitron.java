package at.autrage.projects.zeta.framework;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

import at.autrage.projects.zeta.event.Event;
import at.autrage.projects.zeta.exception.ArgumentNullException;

public class Synchronitron<T> implements Collection<T> {
    private Class<T> tClass;

    private List<T> elements;
    private ConcurrentLinkedQueue<T> elementsPendingAddition;
    private ConcurrentLinkedQueue<T> elementsPendingRemoval;

    public Event<T> elementAdded;
    public Event<T> elementRemoved;

    public Synchronitron(Class<T> tClass) {
        this.tClass = tClass;

        this.elements = new ArrayList<>();
        this.elementsPendingAddition = new ConcurrentLinkedQueue();
        this.elementsPendingRemoval = new ConcurrentLinkedQueue<>();

        elementAdded = new Event<>();
        elementRemoved = new Event<>();
    }

    public Synchronitron(Class<T> tClass, int capacity) {
        this.tClass = tClass;

        this.elements = new ArrayList<>(capacity);
        this.elementsPendingAddition = new ConcurrentLinkedQueue();
        this.elementsPendingRemoval = new ConcurrentLinkedQueue<>();

        elementAdded = new Event<>();
        elementRemoved = new Event<>();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return elements.contains(o);
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return elements.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return elements.toArray();
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(T1[] a) {
        return elements.toArray(a);
    }

    @Override
    public boolean add(T t) {
        if (t == null) {
            return false;
        }

        if (elements.contains(t) || elementsPendingAddition.contains(t)) {
            return false;
        }

        elementsPendingAddition.add(t);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }

        if (!elements.contains(o) || elementsPendingRemoval.contains(o)) {
            return false;
        }

        if (!tClass.isAssignableFrom(o.getClass())) {
            return false;
        }

        elementsPendingRemoval.add((T) o);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new ArgumentNullException();
        }

        return elements.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c == null) {
            throw new ArgumentNullException();
        }

        boolean result = false;
        for (T t : c) {
            result |= add(t);
        }

        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new ArgumentNullException();
        }

        boolean result = false;
        for (Object o : c) {
            result |= remove(o);
        }

        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new ArgumentNullException();
        }

        boolean result = false;
        for (T element : elements) {
            if (!c.contains(element)) {
                result |= remove(element);
            }
        }

        return result;
    }

    @Override
    public void clear() {
        elementsPendingAddition.clear();
        elementsPendingRemoval.clear();
        elementsPendingRemoval.addAll(elements);
    }

    public void synchronize() {
        for (T element = elementsPendingRemoval.poll(); element != null; element = elementsPendingRemoval.poll()) {
            elements.remove(element);
            elementRemoved.call(element);
        }

        for (T element = elementsPendingAddition.poll(); element != null; element = elementsPendingAddition.poll()) {
            elements.add(element);
            elementAdded.call(element);
        }
    }

    public T get(int index) {
        return elements.get(index);
    }
}
