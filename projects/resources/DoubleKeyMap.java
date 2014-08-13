package cs601.collections;

import java.util.List;

/** A Map (Dictionary) just like Java's Map<K,V> interface except that
 * this interface has 2 keys instead of just one. Otherwise it works
 * the same way.
 */
public interface DoubleKeyMap<K1,K2,V> {
    /** Add (key1,key2,value) to dictionary, overwriting previous
     *  value if any.  key1, key2, and value must all be non-null.
     *  If any is null, throw IllegalArgumentException.
     *  @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * 
     */
    V put(K1 key1, K2 key2, V value);

    /** Return the value associated with (key1, key2). Return null if
     *  no value exists for those keys.  key1, key2 must be non-null.
     *  If any is null, throw IllegalArgumentException.  The value
     *  should be just the value added by the user via put(), and
     *  should not contain any of your internal bookkeeping
     *  data/records.
     */
    V get(K1 key1, K2 key2);

    /** Remove a value if present. Return previous value if any.
     *  Do nothing if not present.
     */
    V remove(K1 key1, K2 key2);

    /** Return true if there is a value associated with the 2 keys
     *  else return false.
     *  key1, key2 must be non-null. If either is null, return false.
     */
    boolean containsKey(K1 key1, K2 key2);

    /** Return list of a values in the map/dictionary.  Return an
     *  empty list if there are no values.  The values should be just
     *  the values added by the user via put(), and should not contain
     *  any of your internal bookkeeping data/records.
     */
    List<V> values();

    /** Return how many elements there are in the dictionary. */
    int size();

    /** Reset the dictionary so there are no elements. */
    void clear();
}
