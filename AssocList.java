import java.util.*;

public abstract class AssocList<K,V> extends FMap<K,V> {
    
    // Create an empty Association List implementation of FMap
    public static <K,V> FMap<K,V> empty() {
        return new Empty<K,V>();
    }

    // An empty FMap
    private static class Empty<K,V> extends AssocList<K, V> {
        // An empty FMap is always empty
        public boolean isEmpty() {
            return true;
        }

        // Add the value to this empty FMap at the given key
        public FMap<K,V> add(K key, V value) {
            return new NonEmptyMap<K,V>(this, key, value);
        }

        // The size of an empty FMap is 0
        public int size() {
            return 0;
        }

        // An empty FMap does not contain any keys
        public boolean containsKey(K key) {
            return false;
        }

        // You cannot get the value at the given key from this FMap because
        // an empty FMap contains no keys. So throw an exception
        public V get(K key) {
            throw new RuntimeException("Cannot get from an Empty FMap");
        }

        // Compute a string representation of this Empty FMap
        public String toString() {
            return "{...(" + this.size() + " entries)...}";
        }

        // Compute the hash code for this Empty FMap
        public int hashCode() {
            return 0;
        }

        // An Empty FMap is only equal to another Empty FMap
        @SuppressWarnings(value="unchecked")
        public boolean equals(Object f) {
            if (!(f instanceof FMap<?,?>)) {
                return false;
            } else {
                FMap<K,V> x = (FMap<K,V>) f;
                return x.isEmpty();
            }
        }

        // The given FMap always contains all the keys (none) in this FMap
        boolean containsAllKeys(FMap<K,V> f2) {
            return true;
        }

        // Determine if all the values contained at the keys in this FMap
        // are the same as in the given
        boolean allKeysSame(FMap<K,V> f2) {
            return true;
        }

        // An empty FMap contains no keys so return an empty ArrayList
        Set<K> getKeys() {
            return new HashSet<K>();
        }

        // Return an empty iterator
        public Iterator<K> iterator() {
            return new FMapIterator<K>(new ArrayList<K>(), 0);
        }

        // You cannot sort the keys because there are none so return
        // this empty iterator
        public Iterator<K> iterator(java.util.Comparator<? super K> c) {
            return new FMapIterator<K>(new ArrayList<K>(), 0);
        }

        // Accept the given visitor and visit each key / value
        // pair in this FMap and return an FMap with these new values
        public FMap<K,V> accept(Visitor<K,V> vis) {
            FMap<K,V> m2 = FMap.emptyMap();
            for (K k : this) {
                V v = vis.visit (k, this.get (k));
                m2 = m2.add (k, v);
            }
            return m2;
        }
    }

    // A non empty FMap
    private static class NonEmptyMap <K, V> extends AssocList<K, V> {
        // The rest of this FMap
        private FMap<K,V> f;
        // The key
        private K       key;
        // The value
        private V     value;
        NonEmptyMap(FMap<K,V> f, K key, V value)  {  
            this.f = f;
            this.key = key;
            this.value = value;
        }

        // A non empty FMap is never empty
        public boolean isEmpty() {
            return false;
        }

        /* Add the given value to this FMap under the given key to this FMap
       If this FMap all ready contains the given key return this FMap */
        public FMap<K,V> add(K key, V value) {
            return new NonEmptyMap<K,V>(this, key, value);

        }

        // Compute the size of this non empty FMap
        public int size() {
            if (this.f.containsKey(this.key)) {
                return this.f.size();
            } else {
                return 1 + this.f.size();
            }
        }

        // Determine if this FMap contains the given key
        public boolean containsKey(K key) {
            // If this key is the key we are searching for return true
            if (this.key.equals(key)) {
                return true;
            } else {
                // Otherwise search the rest of this map for the key
                return this.f.containsKey(key);
            }
        }

        // Get the value at the given key from this FMap
        public V get(K key) {
            // If this map does not contain the key throw an exception
            if (!this.containsKey(key)) {
                throw new RuntimeException
                ("This FMap does not contain the specified key");

                // If this key is the one we are searching for return this value
            } else if (this.key.equals(key)) {
                return this.value;
            } else {
                // Otherwise search the rest of the map for the key
                return this.f.get(key);
            }
        }

        // Compute a string representation of this FMap
        public String toString() {
            return "{...(" + this.size() + " entries)...}";
        }

        // Compute the hash code for this FMap
        public int hashCode() {
            int hashcode = 0;

            // The hash code for an FMap is the hash code of all
            // it's values * 31 ^ the key associated with it summed up
            for (K key: this.getKeys()) {
                hashcode += (this.get(key).hashCode() * 31) ^ key.hashCode();
            }

            return hashcode;
        }

        // Determine if all the keys in this FMap are contained in the given FMap
        boolean containsAllKeys(FMap<K,V> f2) {
            // Check if the given FMap contains this key
            if (!(f2.containsKey(this.key))) {
                return false;   
            } else {
                // Check if it contains all the keys of the rest
                return this.f.containsAllKeys(f2);
            }
        }

        // Get all the keys from this FMap
        Set<K> getKeys() {
            Set<K> keys = new HashSet<K>();

            // The keys of this FMap is the set of this key
            // and all the keys in the rest of this FMap
            keys.addAll(this.f.getKeys());
            keys.add(this.key);

            return keys;      
        }

        // Determine if every value at the keys of this FMap are the same as 
        // all the values of the given FMap at the same key
        boolean allKeysSame(FMap<K,V> f2) {

            for (K key : this.getKeys()) {
                if (!this.get(key).equals(f2.get(key))) {
                    return false;
                }
            }

            return true;
        }

        // Determine if this FMap equals the given object
        @SuppressWarnings(value = "unchecked")
        public boolean equals(Object x) {

            // If x is not a NonEmptyMap they are not equal
            if (!(x instanceof FMap<?,?>)){
                return false;
            } else {
                // x is an FMap so cast it to one      
                FMap<K,V> f2 = (FMap<K,V>) x;
                FMap<K,V> f1 = this;

                // A NonEmpty FMap cannot be equal to an empty FMap
                if (f2.isEmpty()) {
                    return false;
                }


                /* Make sure both FMaps contain the same
               keys and the same values at all of these keys */
                if (!(f1.containsAllKeys(f2) &&
                        (f2.containsAllKeys(f1)) &&
                        (f1.allKeysSame(f2)) &&
                        (f2.allKeysSame(f1)))) {
                    return false;
                }                
            }

            return true;
        }

        // Return an iterator representing this FMap
        public Iterator<K> iterator() {
            Iterator<K> it =
                    new FMapIterator<K>(new ArrayList<K>(this.getKeys()), this.size());
            return it;
        }

        // Return an iterator representing this FMap sorted by a given comparator
        public Iterator<K> iterator(java.util.Comparator<? super K> c) {
            ArrayList<K> keys = new ArrayList<K>(this.getKeys());
            Collections.sort(keys , c);
            Collections.reverse(keys);
            Iterator<K> it = new FMapIterator<K>(keys, this.size());
            return it;
        }

        // Accept the given visitor and visit each key / value
        // pair in this FMap and return an FMap with these new values
        public FMap<K,V> accept(Visitor<K,V> vis) {
            FMap<K,V> m2 = FMap.emptyMap();
            for (K k : this) {
                V v = vis.visit (k, this.get (k));
                m2 = m2.add (k, v);
            }
            return m2;
        }

    }
}
