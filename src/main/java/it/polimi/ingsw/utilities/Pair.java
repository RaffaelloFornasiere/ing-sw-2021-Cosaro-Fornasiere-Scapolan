package it.polimi.ingsw.utilities;

import java.util.Objects;

/**
 * Class representing a pair of object
 * @param <K> The type of the first object
 * @param <V> The type of the second object
 */
public class Pair<K, V> {
    K key;
    V value;

    /**
     * Constructor for the pair
     * @param key The first value
     * @param value The second value
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    public Pair(Pair<K,V> p) {
        this.key = p.key;
        this.value = p.value;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(key, pair.key) && Objects.equals(value, pair.value);
    }

    /**
     * Getter for the first value
     * @return The first value
     */
    public K getKey() {
        return key;
    }

    /**
     * Getter for the second value
     * @return The second value
     */
    public V getValue() {
        return value;
    }
}
