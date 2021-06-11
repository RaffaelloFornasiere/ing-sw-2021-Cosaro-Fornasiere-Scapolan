package it.polimi.ingsw.utilities;

public class LockWrap<T> {
    private T item;

    public LockWrap(T item) {
        this.item = item;
    }

    public T getWaitIfNull() throws InterruptedException {
        T res;
        synchronized (this) {
            while (item == null) {

                System.out.println("start waiting");
                this.wait();
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