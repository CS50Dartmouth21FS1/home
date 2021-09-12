/**
 * A basic interface for a generic list ADT
 */
public interface SimpleList<T> {
	/**
	 * Returns # elements in the list (they are indexed 0..size-1)
	 */
	public int size();
	
	/**
	 * Adds the item at the index, which must be between 0 and size
	 * (since the current elements are 0..size-1, idx = size grows the list)
	 */
	public void add(int idx, T item) throws Exception;
	
	/**
	 * Removes the item at the index, which must be between 0 and size-1
	 */
	public void remove(int idx) throws Exception;
	
	/**
	 * Returns the item at the index, which must be between 0 and size-1
	 */
	public T get(int idx) throws Exception;
	
	/**
	 * Replaces the item at the index, which must be between 0 and size-1
	 */	
	public void set(int idx, T item) throws Exception;
}
