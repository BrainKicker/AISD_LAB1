package containers;

public class Stack<T> {

    private final ArrayList<T> list = new ArrayList<>();

    public void push(T t) {
        list.addLast(t);
    }

    public T pop() {
        return list.popLast();
    }

    public T top() {
        return list.getLast();
    }

    public void clear() {
        list.clear();
    }

    public int size() {
        return list.size();
    }

    public boolean empty() {
        return list.empty();
    }
}