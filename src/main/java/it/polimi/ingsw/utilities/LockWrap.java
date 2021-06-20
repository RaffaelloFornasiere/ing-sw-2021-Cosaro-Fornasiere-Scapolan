package it.polimi.ingsw.utilities;



public class LockWrap<T> {
    private T item;
    private T lockingState;

    public LockWrap(T item) {
        this.item = item;
        lockingState = null;
    }
    public LockWrap(T item, T lockingState) {
        this.item = item;
        this.lockingState = lockingState;
    }

    public T getWaitIfLocked()  {
        T res;
        synchronized (this) {
            while (item == lockingState) {

                System.out.println("start waiting");
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("end waiting for :" + item.getClass().getSimpleName());
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
}