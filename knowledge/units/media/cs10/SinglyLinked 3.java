/**
 * A singly-linked list implementation of the SimpleList interface
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012, 
 * based on a more feature-ful version by Tom Cormen and Scot Drysdale
 * @author CBK, Spring 2016, cleaned up inner class
 */
public class SinglyLinked<T> implements SimpleList<T> {
	private Element head;	// front of the linked list
	private int size;		// # elements in the list

	/**
	 * The linked elements in the list: each has a piece of data and a next pointer
	 */
	private class Element {
		private T data;
		private Element next;

		private Element(T data, Element next) {
			this.data = data;
			this.next = next;
		}
	}

	public SinglyLinked() {
		head = null;
		size = 0;
	}

	public int size() {
		return size;
	}

	/**
	 * Helper function, advancing to the nth Element in the list and returning it
	 * (exception if not that many elements)
	 */
	private Element advance(int n) throws Exception {
		Element e = head;
		while (n > 0) {
			// Just follow the next pointers
			e = e.next;
			if (e == null) throw new Exception("invalid index");
			n--;
		}
		return e;
	}

	public void add(int idx, T item) throws Exception { 
		if (idx < 0) {
			throw new Exception("invalid index");			
		}
		else if (idx == 0) {
			// Insert at head
			head = new Element(item, head); //new item gets next pointer set to head
		}
		else {
			// It's the next thing after element # idx-1
			Element e = advance(idx-1);
			// Splice it in
			e.next = new Element(item, e.next);	//create new element with next pointing at prior element's next 
												//and prior element's next updated to point to this item
		}
		size++;
	}

	public void remove(int idx) throws Exception {
		if (idx < 0) {
			throw new Exception("invalid index");			
		}
		else if (idx == 0) {
			// Just pop off the head
			if (head == null) throw new Exception("invalid index");
			head = head.next;
		}
		else {
			// It's the next thing after element # idx-1
			Element e = advance(idx-1);
			if (e.next == null) throw new Exception("invalid index");
			// Splice it out
			e.next = e.next.next;  //nice!
		}
		size--;
	}

	public T get(int idx) throws Exception {
		if (idx < 0) {
			throw new Exception("invalid index");			
		}
		Element e = advance(idx);
		return e.data;
	}

	public void set(int idx, T item) throws Exception {
		if (idx < 0) {
			throw new Exception("invalid index");			
		}
		Element e = advance(idx);
		e.data = item;
	}

	public String toString() {
		String result = "";
		for (Element x = head; x != null; x = x.next) 
			result += x.data + "->"; 
		result += "[/]";

		return result;
	}
}