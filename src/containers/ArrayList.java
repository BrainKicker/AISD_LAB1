package containers;

public class ArrayList<T> implements List<T> {


    private static final int DEFAULT_INITIAL_CAPACITY = 10;
    private static final double CAPACITY_MULTIPLIER = 1.73205080757;


    private Object[] arr;
    private int size = 0;


    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
    }

    private void ensureCapacity() {
        ensureCapacity(size + 1);
    }

    private void ensureCapacity(int minCapacity) {
        if (capacity() >= minCapacity)
            return;
        setCapacity(Math.max((int) (capacity() * CAPACITY_MULTIPLIER), minCapacity));
    }

    private void setCapacity(int newCapacity) {
        if (newCapacity < size)
            throw new IllegalArgumentException();
        if (arr == null) {
            arr = new Object[newCapacity];
            return;
        }
        if (newCapacity == capacity())
            return;
        Object[] newArr = new Object[newCapacity];
        System.arraycopy(arr, 0, newArr, 0, size);
        arr = newArr;
    }


    public ArrayList() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public ArrayList(int initialCapacity) {
        arr = new Object[initialCapacity];
    }

    @SafeVarargs
    public ArrayList(T... objs) {
        arr = new Object[objs.length];
        System.arraycopy(objs, 0, arr, 0, objs.length);
        size = arr.length;
    }

    public ArrayList(ArrayList<T> other) {
        arr = new Object[other.arr.length];
        System.arraycopy(other.arr, 0, arr, 0, other.arr.length);
        size = arr.length;
    }


    @Override
    public void add(T t) {
        ensureCapacity();
        arr[size] = t;
        size++;
    }

    @Override
    public void set(int index, T t) {
        checkIndex(index);
        arr[index] = t;
    }

    @Override
    public void insert(int index, T t) {
        if (index < 0 || index > size) // > size, not >= size
            throw new IndexOutOfBoundsException();
        ensureCapacity();
        System.arraycopy(arr, index, arr, index + 1, size - index);
        arr[index] = t;
        size++;
    }

    @Override
    public void remove(int index) {
        checkIndex(index);
        System.arraycopy(arr, index + 1, arr, index, size - index - 1);
        arr[size()-1] = null;
        size--;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) arr[index];
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            arr[i] = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    public int capacity() {
        return arr.length;
    }

    public void minimizeCapacity() {
        setCapacity(size);
    }
}