package containers;

public class UnmodifiableArrayList<T> extends ArrayList<T> {

    @SafeVarargs
    public UnmodifiableArrayList(T... objs) {
        super(objs);
    }

    public UnmodifiableArrayList(ArrayList<T> other) {
        super(other);
    }

    @Override
    public void add(T t) {
        throw new RuntimeException();
    }

    @Override
    public void set(int index, T t) {
        throw new RuntimeException();
    }

    @Override
    public void insert(int index, T t) {
        throw new RuntimeException();
    }

    @Override
    public void remove(int index) {
        throw new RuntimeException();
    }

    @Override
    public void clear() {
        throw new RuntimeException();
    }
}