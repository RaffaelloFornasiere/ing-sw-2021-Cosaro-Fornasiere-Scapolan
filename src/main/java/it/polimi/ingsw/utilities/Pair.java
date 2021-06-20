package it.polimi.ingsw.utilities;

import java.util.Objects;

public class Pair<K, V> {
    K key;
    V value;

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

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
