import java.util.*;
import java.awt.Color;

/* An immutable FTree class 
   INVARIANT: FTree and the left and right children are 
   binary search trees with a total order according to the comparator 
   INVARIANT: The amount of black nodes in any path from root to tree 
   is the same 
   INVARIANT: No red node can have a red child */

public abstract class FTree<K,V> extends FMap<K,V> {

    // Create an empty FTree
    public static <K,V> FTree<K,V> 
    emptyTree(java.util.Comparator<? super K> c) {
        return new EmptyTree<K,V>(c);
    }

    // Create a node of an FTree
    private static <K,V> Node<K,V>
    node(K key, V value, FTree<K,V> f1, FTree<K,V> f2, Color color) {
        return new Node<K,V>(key, value, f1, f2, color);
    }

    // Add the key / value pair to the FTree
    public abstract FTree<K,V> add(K key, V value);

    // Insert the given key / value pair into the FTree
    public abstract FTree<K,V> ins(K key, V value);

    // Accept the given visitor
    public abstract FTree<K,V> accept(Visitor<K,V> vis);

    // Balance the given Node according to Red Black invariants
    public static <K,V> Node<K,V> balance(Node<K,V> f) {   
	// Check if f is black and it has a left child
        if (!(f.isRed()) && !(f.left.isEmpty())) {
            FTree<K,V> left = f.left;
	    // Check if f.left and f.left.right are red
            if (left.isRed()) {
                if (!(left.getRight().isEmpty())) {
                    if (left.getRight().isRed()) {
                        FTree<K,V> lowRight = left.getRight();
			// If true rearrange 
                        return  FTree.node(lowRight.getKey(),lowRight.getValue(),
					   FTree.node(left.getKey(), left.getValue(), left.getLeft(),
						      lowRight.getLeft(), Color.BLACK),
					   FTree.node(f.getKey(), f.getValue(), lowRight.getRight(),
						      f.getRight(), Color.BLACK),
					   Color.RED);
                    }
                } 
		// We know f.left is res so check if f.left.left is red
                if (!(left.getLeft().isEmpty())) {
                    if (left.getLeft().isRed()) {
                        FTree<K,V> lowLeft = left.getLeft();
			// If true rearrange
                        return  FTree.node(left.getKey(), left.getValue(),
					   FTree.node(lowLeft.getKey(), lowLeft.getValue(), lowLeft.getLeft(),
						      lowLeft.getRight(), Color.BLACK),
					   FTree.node(f.getKey(), f.getValue(), left.getRight(), 
						      f.getRight(), Color.BLACK),
					   Color.RED);

                    }
                }

            } 
        }
	// Check if f is black and it has a right child
        if (!(f.isRed()) && !(f.right.isEmpty())) {
            FTree<K,V> right = f.right;
	    // Check if f.right and f.right.right are red
            if (right.isRed()) {
                if (!(right.getRight().isEmpty())) {
                    if (right.getRight().isRed()) {
                        FTree<K,V> lowRight = right.getRight();
			// If so rearrange
                        return FTree.node(right.getKey(), right.getValue(),
					  FTree.node(f.getKey(), f.getValue(), f.getLeft(), 
						     right.getLeft(), Color.BLACK),
					  FTree.node(lowRight.getKey(), lowRight.getValue(), lowRight.getLeft(), 
						     lowRight.getRight(), Color.BLACK),
					  Color.RED);
                    }
                }
		// We know f.right is red so check f.right.left is red
                if (!(right.getLeft().isEmpty())) {
                    if (right.getLeft().isRed()) {
                        FTree<K,V> lowLeft = right.getLeft();
			// If true rearrange
                        return FTree.node(lowLeft.getKey(), lowLeft.getValue(),
					  FTree.node(f.getKey(), f.getValue(), f.getLeft(), 
						     lowLeft.getLeft(), Color.BLACK),
					  FTree.node(right.getKey(), right.getValue(),lowLeft.getRight(),
						     right.getRight(), Color.BLACK),
					  Color.RED);
                    }
                }
            }
        }
	
	// If none of the cases are true return the given Node
        return f;       
    }

    // Return true iff the node is Red
    abstract boolean isRed();

    // Get the comparator for this FTree
    abstract Comparator<? super K> getComp();

    // Get the left child of this FTree, throw an exception if used on an
    // empty FTree
    abstract FTree<K,V> getLeft();

    // Get the right child of this FTree, throw an exception if used on an
    // empty FTree
    abstract FTree<K,V> getRight();

    // Get the key of this FTree, throw an exception if used on an empty FTree
    abstract K getKey();

    // Get the value of this FTree throw an exception if used on an empty FTree
    abstract V getValue();

    // Get the color of this FTree
    abstract Color getColor();

    // Add the key (if any) of the Node to the given map
    abstract void addTo(Set<K> map);


    // An empty FTree
    private static class EmptyTree<K,V> extends FTree<K,V> {
        // The comparator that this binary search tree is ordered by
        private Comparator<? super K> c;
        private int size;
        EmptyTree(Comparator<? super K> c) {
            this.c = c;
            this.size = 0;
        }

        // Create a Node containing the key value pair and two empty trees
        // Color the result black
        public Node<K,V> add(K key, V value) {
            // The result is the same as inserting in the value
            Node<K,V> result  = this.ins(key, value);
            // Color the result black
            result = 
                    FTree.node(result.key, result.value, 
                            result.left, result.right, 
                            Color.BLACK);

            return result;
        }

        // Create a Node containing the key value pair and two empty trees
        // Color the result red
        public Node<K,V> ins(K key, V value) {
            Node<K,V> result = FTree.node(key, value,
                    new EmptyTree<K,V>(this.c), 
                    new EmptyTree<K,V>(this.c), Color.RED);
            result.c = this.c;
            return result;
        }

        // The empty tree contains no keys
        public boolean containsKey(K key) {
            return false;
        }

        // Cannot get from an empty tree
        public V get(K key) {
            throw new RuntimeException("Cannot get from an empty tree");
        }

        // The size for an empty tree is 0
        public int size() {
            return this.size;
        }

        // An empty tree is always empty
        public boolean isEmpty() {
            return true;
        }

        // Compute a string representation of this Empty FTree
        public String toString() {
            return "{...(" + this.size() + " entries)...}";
        }

        // Compute the hash code for this Empty FTree
        public int hashCode() {
            return 0;
        }

        // There are no keys in an Empty FTree
        Set<K> getKeys() {
            return new HashSet<K>();
        }

        // The given FTree always contains all the key5s (none) in this FMap
        boolean containsAllKeys(FMap<K,V> f2) {
            return true;
        }

        // Determine if all the keys in this FMap have the same
        // values as the keys in the given FMap
        boolean allKeysSame(FMap<K,V> f2) {
            return true;
        }


        // An empty FTree equals the given object if the object is an
        // empty FMap
        @SuppressWarnings(value="unchecked")
        public boolean equals(Object f) {
            if (!(f instanceof FMap<?,?>)) {
                return false;
            } else {
                FMap<K,V> x = (FMap<K,V>) f;
                return x.isEmpty();
            }
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



        // Return the comparator of this Empty Tree
        Comparator<? super K> getComp() {
            return this.c;
        }

        // Accept the given visitor and visit each key / value
        // pair in this FMap and return an FMap with these new values
        public FTree<K,V> accept(Visitor<K,V> vis) {
            return this;
        }

        // Throw an exception because a empty FTree has no left child
        FTree<K,V> getLeft() {
            throw new RuntimeException("An empty FTree has no left child");
        }

        // Throw an exception because an empty FTree has no right child
        FTree<K,V> getRight() {
            throw new RuntimeException("An empty FTree has no right child");
        }

        // Throw an exception because an empty FTree has no key
        K getKey() {
            throw new RuntimeException("An empty FTree has no key");           
        }

        // Throw an exception because an empty FTree has no value
        V getValue() {
            throw new RuntimeException("An empty FTree has no valye");
        }

        // Throw an exception because an empty FTree has no color
        Color getColor() {
            throw new RuntimeException("An empty FTree ha no color");
        }

        // An empty Tree contains to key to add
        void addTo(Set<K> set) {        
        }

        // An empty Tree is not red
        boolean isRed() {
            return false;
        }

    }

    // A Node in an FTree
    private static class Node<K,V> extends FTree<K,V> {
        // The key 
        private K key;
        // The Value
        private V value;
        // The children of this FTree
        private FTree<K,V> left;
        private FTree<K,V> right;
        // The comparator that this binary search tree is ordered by
        private Comparator<? super K> c;
        // The size of the FTree
        private int size;
        // The color of this node either Red or Black
        private Color color;
        Node(K key, V value, FTree<K,V> left, FTree<K,V> right, Color color) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
            this.size = 1 + this.left.size() +
                    this.right.size();
            this.c = this.left.getComp();
            this.color = color;
        }

        // Add the key value pair to this FTree
        // Color the result black
        public Node<K,V> add(K key, V value) {
            // The result is the same as inserting the key / value
            Node<K,V> result = this.ins(key, value);

            // Color the result black
            result = 
                    FTree.node(result.key, result.value, 
                            result.left, result.right, Color.BLACK);

            return result;

        }

        // Insert the given key value pair into the FTree
        // balancing as you go
        public Node<K,V> ins(K key, V value) {
            if (c.compare(this.key, key) == 0) {
                return  FTree.node(key, value, this.left, 
				   this.right, this.color);
            } else if (c.compare(this.key, key) < 0) {
                return FTree.balance(FTree.node
				     (this.key, this.value,
				      this.left.ins(key, value), 
				      this.right, this.color));
            } else {
                return FTree.balance(FTree.node
				     (this.key, this.value, 
				      this.left, this.right.ins(key, value), 
				      this.color));
            }
        }

        // Return the size of this FTree
        public int size() {
            return this.size;
        }

        // Determine if this FTree contains the given Key
        public boolean containsKey(K key) {
            if (this.c.compare(this.key, key) == 0) {
                return true;
            } else if (this.c.compare(this.key, key) < 0) {
                return this.left.containsKey(key);
            } else {
                return this.right.containsKey(key);
            }
        }

        // Get the value at the given key
        public V get(K key) {
            if (this.c.compare(this.key, key) == 0) {
                return this.value;
            } else if (this.c.compare(this.key, key) < 0) {
                return this.left.get(key);
            } else {
                return this.right.get(key);
            }
        }

        // A Node is never empty
        public boolean isEmpty() {
            return false;
        }

        // Compute a string representation of this Node
        public String toString() {
            return "{...(" + this.size() + " entries)...}";
        }

        // Compute the hash code for this Node
        public int hashCode() {
            int hash = 0;

            for (K key : this.getKeys()) {
                hash += (this.get(key).hashCode() * 31) ^ key.hashCode();
            }

            return hash;
        }

        // Determine if all the keys in this 
        // Node are contained in the given FMap
        boolean containsAllKeys(FMap<K,V> f2) {
            // Check if the given Node contains this key
            if (!(f2.containsKey(this.key))) {
                return false;   
            } else {
                // Check if it contains all the keys of the children
                return this.left.containsAllKeys(f2) &&
		       this.right.containsAllKeys(f2);
            }
        }

        // Get all the keys from this FMap
        Set<K> getKeys() {
            Set<K> keys = new HashSet<K>();

            // The keys of this Node is the set of this key
            // and all the keys in the rest of this FTree
            this.left.addTo(keys);
            this.right.addTo(keys);
            keys.add(this.key);

            return keys;      
        }

        // Determine if every value at the keys of this NOde are the same as 
        // all the values of the given FMap at the same key
        boolean allKeysSame(FMap<K,V> f2) {
	    // For all keys in this FTree
            for (K key : this.getKeys()) {
		// See if the get of that key on both this and
		// f2 are the same
                if (!this.get(key).equals(f2.get(key))) {
                    return false;
                }
            }

            return true;
        }

        // Determine if this Node equals the given object
        @SuppressWarnings(value = "unchecked")
        public boolean equals(Object x) {

            // If x is not a FMap they are not equal
            if (!(x instanceof FMap<?,?>)){
                return false;
            } else {
                // x is an FMap so cast it to an FMap        
                FMap<K,V> f2 = (FMap<K,V>) x;
                FMap<K,V> f1 = this;

                // A Node is not equal to an empty FMap
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

        // Return an iterator<K> that iterates over the keys of this FTree
        public Iterator<K> iterator() {
            Iterator<K> it = new FMapIterator<K>(new ArrayList<K>(this.getKeys()), 
						 this.size);
            return it;
        }

        // Return an iterator<K> that iterates over the keys of this FTree
        // sorted by the given comparator
        public Iterator<K> iterator(Comparator<? super K> c) {            
            ArrayList<K> keys = new ArrayList<K>(this.getKeys());
            // Sort and reverse the keys to get proper functionality
            Collections.sort(keys, c);
            Collections.reverse(keys);
            Iterator<K> it = new FMapIterator<K>(keys, this.size);
            return it;

        }

        // Accept the given visitor and visit each key / value
        // pair in this FMap and return an FMap with these new values
        public FTree<K,V> accept(Visitor<K,V> vis) {
            return FTree.node(this.key, vis.visit(this.key, this.value), 
			      this.left.accept(vis), 
			      this.right.accept(vis), 
			      this.color);
        }


        // Get the comparator for this Node2
        Comparator<? super K> getComp() {
            return this.c;
        }

        // Get this nodes left child
        FTree<K,V> getLeft() {
            return this.left;
        }

        // Get this nodes right child
        FTree<K,V> getRight() {
            return this.right;
        }

        // Get this node's key
        K getKey() {
            return this.key;
        }

        // Get this node's value
        V getValue() {
            return this.value;
        }

        // Get this node's color
        Color getColor() {
            return this.color;
        }

        // Add this Node's key to the given set and then 
        // recur on it's children
        void addTo(Set<K> set) {
            set.add(this.key);
            this.left.addTo(set);
            this.right.addTo(set);
        }

        // Return true iff the node's color is red
        boolean isRed() {
            return this.color == Color.RED;
        }
    }      
}

// An Iterator<K> for FMaps
class FMapIterator<K> implements Iterator<K> {
    // The keys to iterate over   
    ArrayList<K> keys;
    // The iterator's state
    Integer state;
    FMapIterator(ArrayList<K> keys, Integer state) {
        this.keys = keys;
        this.state = state;
    }

    // The iterator has next if the state is > 0
    public boolean hasNext() {
        return this.state > 0;
    }

    // Return the next key and update the state
    public K next() {
        if (this.hasNext()) {
            K key = keys.get(state - 1);
            state--;
            return key;
        } else {
            throw new NoSuchElementException();
        }
    }

    // Remove is not supported
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
