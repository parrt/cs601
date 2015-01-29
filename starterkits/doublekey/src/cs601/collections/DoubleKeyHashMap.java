package cs601.collections;

import java.util.ArrayList;
import java.util.List;

public class DoubleKeyHashMap<K1,K2,V> implements cs601.collections.DoubleKeyMap<K1,K2,V> {
	public static final int DEFAULT_BUCKETS = 101;

	class Entry {
		K1 key1; K2 key2; V value;
		Entry(K1 key1, K2 key2, V value) {
			this.key1 = key1;
			this.key2 = key2;
			this.value = value;
		}

		Entry(K1 key1, K2 key2) {
			this.key1 = key1;
			this.key2 = key2;
		}

		/** If keys are the same then consider an entry equal */
		@Override
		public boolean equals(Object o) {
			Entry e = (Entry)o;
			return key1.equals(e.key1) &&
				   key2.equals(e.key2);
		}
	}

	private List<Entry>[] buckets = null;
	private int numBuckets = 101;

	public DoubleKeyHashMap() {
		this(DEFAULT_BUCKETS);
	}

	public DoubleKeyHashMap(int numBuckets) {
		this.numBuckets = numBuckets;
		buckets = (List<Entry>[])new List[numBuckets];
		for (int i=0; i< numBuckets; i++) {
			buckets[i] = new ArrayList<Entry>();
		}
	}

	private int hash(String key) {
		int s = 0;
		for (int i=0; i<key.length(); i++) {
			s += (int)key.charAt(i);
		}
		return s;
	}

	@Override
	public V put(K1 key1, K2 key2, V value) {
		if ( key1==null || key2==null || value==null ) {
			throw new IllegalArgumentException();
		}
		Entry old = null;
		List<Entry> entries = getBucket(key1, key2);
		Entry newEntry = new Entry(key1, key2, value);
		int ei = entries.indexOf(newEntry);
		if ( ei < 0 ) {
			entries.add(newEntry);
		}
		else {
			old = entries.get(ei);
			entries.set(ei, newEntry);
		}
		return old==null ? null : old.value;
	}

	@Override
	public V get(K1 key1, K2 key2) {
		if ( key1==null || key2==null ) {
			throw new IllegalArgumentException();
		}
		List<Entry> entries = getBucket(key1, key2);
		int ei = entries.indexOf(new Entry(key1, key2));
		if ( ei >= 0 ) {
			Entry e = entries.get(ei);
			return e.value;
		}
		return null;
	}

	public List<Entry> getBucket(K1 key1, K2 key2) {
		int compoundHashCode = DoubleKeyHashMap.compoundHashCode(key1, key2);
		int bucketIndex = compoundHashCode % numBuckets;
		return buckets[bucketIndex];
	}

	@Override
	public V remove(K1 key1, K2 key2) {
		List<Entry> entries = getBucket(key1, key2);
		V old = get(key1, key2);
		entries.remove(new Entry(key1,key2));
		return old;
	}

	@Override
	public boolean containsKey(K1 key1, K2 key2) {
		List<Entry> entries = getBucket(key1, key2);
		Entry newEntry = new Entry(key1, key2);
		int ei = entries.indexOf(newEntry);
		return ei >= 0;
	}

	@Override
	public List<V> values() {
		List<V> values = new ArrayList<V>();
		for (List<Entry> bucket : buckets) {
			for (Entry e : bucket) {
				values.add(e.value);
			}
		}
		return values;
	}

	@Override
	public int size() {
		return values().size();
	}

	@Override
	public void clear() {
		for (int i=0; i< numBuckets; i++) {
			buckets[i].clear();
		}
	}

	static int compoundHashCode(Object key1, Object key2) {
		return Math.abs(key1.hashCode() * 37 + key2.hashCode());
	}
}
