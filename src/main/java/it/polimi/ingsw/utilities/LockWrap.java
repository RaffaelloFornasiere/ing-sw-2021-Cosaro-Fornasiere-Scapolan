package it.polimi.ingsw.utilities;

/**
 * Class that wraps an object into a lock
 * @param <T> The type of the object being wrapped
 */
public class LockWrap<T> {
    private T item;
    private final T lockingState;

    /**
     * Constructor for the class. Sets the locking state to null
     * @param item the object being wrapped
     */
    public LockWrap(T item) {
        this.item = item;
        lockingState = null;
    }

    /**
     * Constructor for the class
     * @param item the object being wrapped
     * @param lockingState the value the object has to assume for it to be considered locked
     */
    public LockWrap(T item, T lockingState) {
        this.item = item;
        this.lockingState = lockingState;
    }

    /**
     * Getter for the wrapped object if it is different from the locking state. Waits until it gets changed if it's equal to the locking state
     * @return the wrapped object
     */
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

    /**
     * Getter for the wrapped object
     * @return the wrapped object
     */
    public T getItem() {
        return item;
    }

    /**
     * Sets the wrapped object
     * @param item the new value for the wrapped object
     */
    public void setItem(T item) {
        synchronized (this) {
            this.item = item;
            notifyAll();
        }
    }


    /**
     * Waits if the stored value is different from the locking state. Waits until changed
     */
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