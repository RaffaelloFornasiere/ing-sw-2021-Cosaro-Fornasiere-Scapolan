package it.polimi.ingsw.utilities;


public class LockWrap<T> {
    private T item;
    private final T lockingState;

    public LockWrap(T item) {
        this.item = item;
        lockingState = null;
    }

    public LockWrap(T item, T lockingState) {
        this.item = item;
        this.lockingState = lockingState;
    }

    public T getWaitIfLocked() {
        T res;
        synchronized (this) {
            while (item == lockingState) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            res = item;
        }
        return res;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        synchronized (this) {
            this.item = item;
            notifyAll();
        }
    }


    public void waitIfSet()
    {
        synchronized (this) {
            while (item != lockingState) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}