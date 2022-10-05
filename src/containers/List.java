package containers;

import java.util.Objects;
import java.util.function.Consumer;

public interface List<T> {


    default void add(T t) {
        insert(size(), t);
    }

    default void addFirst(T t) {
        insert(0, t);
    }

    default void addLast(T t) {
        add(t);
    }


    void set(int index, T t);


    void insert(int index, T t);


    void remove(int index);

    default void removeFirst() {
        remove(0);
    }

    default void removeLast() {
        remove(size() - 1);
    }


    T get(int index);

    default T getFirst() {
        return get(0);
    }

    default T getLast() {
        return get(size() - 1);
    }


    default T popFirst() {
        T t = getFirst();
        removeFirst();
        return t;
    }

    default T popLast() {
        T t = getLast();
        removeLast();
        return t;
    }


    default boolean contains(T t) {
        for (int i = 0; i < size(); i++)
            if (Objects.equals(get(i), t))
                return true;
        return false;
    }


    default void forEach(Consumer<T> action) {
        for (int i = 0; i < size(); i++)
            action.accept(get(i));
    }


    void clear();


    int size();

    default boolean empty() {
        return size() == 0;
    }
}