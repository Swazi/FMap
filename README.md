FMap
----
A functional (immutable) FMap ADT

Signature
---------

  Static methods:


    emptyMap     :                                  ->  FMap<K,V>
    emptyMap     : java.util.Comparator<? super K>  ->  FMap<K,V>

  Dynamic methods (for which the receiver is an FMap<K,V>):
  
    add          :  K x V                           ->  FMap<K,V>
    isEmpty      :                                  ->  boolean
    size         :                                  ->  int
    containsKey  :  K                               ->  boolean
    get          :  K                               ->  V
    toString     :                                  ->  String
    equals       :  Object                          ->  boolean
    hashCode     :                                  ->  int
    iterator     :                                  ->  Iterator<K>
    iterator     : java.util.Comparator<? super K>  ->  Iterator<K>
    accept       :  Visitor<K,V>                    ->  FMap<K,V>
    
Performance requirements
-------------------------

  Suppose c is a comparator that runs in `O(1)` time, m is an
  `FMap<K,V>` that has been created by adding random key-value
  pairs to `FMap.emptyMap(c)`, iter is an iterator obtained by
  evaluating `m.iterator()`, n is `m.size()`, and v is a `Visitor<K,V>`
  such that `v.visit(K,V)` runs in constant time.  Then

        m.add(k,v)            should run in O(lg n) time
        m.isEmpty()           should run in O(1) time
        m.size()              should run in O(1) time
        m.containsKey(k)      should run in O(lg n) time
        m.get(k)              should run in O(lg n) time
        m.iterator()          should run in O(n) time
        iter.hasNext()        should run in O(1) time  
        iter.next()           should run in O(1) time
        m.accept(v)           should run in O(n) time                         

   where all of those times are for the worst case.