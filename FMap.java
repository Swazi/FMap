import java.util.*;

// The immutable FMap ADT
public abstract class FMap<K, V> implements Iterable<K> {

    // Crease an empty FMap
    public static <K,V> FMap<K,V> emptyMap() {
        return AssocList.empty();
    }

    // Create an empty FMap with a comparator
    public static <K,V> FMap<K,V> emptyMap(java.util.Comparator<? super K> c) {
        return FTree.emptyTree(c);      
    }

    // Determine if this FMap is empty
    public abstract boolean isEmpty();

    // Add the given value to the FMap under the given key
    public abstract FMap<K,V> add(K key, V value);

    // Determine the size of this FMap
    public abstract int size();

    // Determine if this FMap contains the given key
    public abstract boolean containsKey(K key);

    // Get the value located at the given key in this FMap
    public abstract V get(K key);

    // Compute a string representation of this FMap
    public abstract String toString();

    // Compute the hash code of this FMap
    public abstract int hashCode();
    // Determine if this FMap equals the given object
    public abstract boolean equals(Object x);

    // Determine if the given FMap contains all the keys that this FMap does
    abstract boolean containsAllKeys(FMap<K,V> f2);

    /* Determine if all the keys of this FMap
    contain the same values as the keys
    of the given FMap */
    abstract boolean allKeysSame(FMap<K,V> f2);

    // Get all the keys in this FMap
    abstract Set<K> getKeys();

    // Return an iterator of type K that contains all the keys
    // in this FMap
    public abstract Iterator<K> iterator();

    // Return an iterator of type K that contains all the keys
    // in this FMap sorted by the given comparator
    public abstract Iterator<K> iterator(java.util.Comparator<? super K> c);

    // Accept the given visitor and visit each key / value
    // pair in this FMap and return an FMap with these new values
    public abstract FMap<K,V> accept(Visitor<K,V> v);

}