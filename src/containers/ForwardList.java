package containers;

import java.util.Objects;
import java.util.function.Consumer;

public class ForwardList<T> implements List<T> {


    protected static class Node <T> {
        Node<T> next;
        T data;
        Node(Node<T> n, T t) { next = n; data = t; }
    }


    private Node<T> head;

    private int size = 0;


    @SafeVarargs
    public ForwardList(T... objs) {
        for (int i = objs.length - 1; i >= 0; i--)
            addFirst(objs[i]);
    }


    private Node<T> findNode(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        Node<T> cur = head;
        while (index-- > 0)
            cur = cur.next;
        return cur;
    }


    @Override
    public void set(int index, T t) {
        findNode(index).data = t;
    }

    @Override
    public void insert(int index, T t) {
        if (index == 0) {
            head = new Node<>(head, t);
        } else {
            Node<T> prev = findNode(index - 1);
            prev.next = new Node<>(prev.next, t);
        }
        size++;
    }

    @Override
    public void remove(int index) {
        if (size == 0)
            throw new IndexOutOfBoundsException();
        if (index == 0) {
            head = head.next;
        } else {
            Node<T> prev = findNode(index - 1);
            prev.next = prev.next.next;
        }
        size--;
    }

    @Override
    public T get(int index) {
        return findNode(index).data;
    }

    @Override
    public boolean contains(T t) {
        for (Node<T> cur = head; cur != null; cur = cur.next)
            if (Objects.equals(cur.data, t))
                return true;
        return false;
    }

    @Override
    public void forEach(Consumer<T> action) {
        for (Node<T> cur = head; cur != null; cur = cur.next)
            action.accept(cur.data);
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }
}