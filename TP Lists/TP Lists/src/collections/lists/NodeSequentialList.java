package collections.lists;

import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import collections.nodes.Headed;
import collections.nodes.Node;
import collections.nodes.NodeListIterator;

/**
 * An implementation of a {@link List} based on {@link Node}s
 * and a partial implementation provided by {@link AbstractSequentialList}
 * backed up by a {@link NodeListIterator}
 * @param <E> the type of elements in this list
 */
public class NodeSequentialList<E> extends AbstractSequentialList<E>
    implements Headed<E>
{
	/**
	 * the head node of this list
	 */
	private Node<E> head;

	/**
	 * Default constructor
	 */
	public NodeSequentialList()
	{
		// TODO 500 Complete NodeSequentialList()...
		this.head = null;
	}

	/**
	 * Copy constructor
	 * @param c the collection to copy in this list
	 */
	public NodeSequentialList(Collection<E> c)
	{
		// TODO 501 Complete NodeSequentialList(Collection<E> c)...
		if (c == null) {
			throw new NullPointerException("Collection cannot be null.");
		}
	
		if (c.isEmpty()) {
			head = null; // Explicitly setting head to null for clarity
			return; 
		}
	
		Node<E> prev = null;
		for (E element : c) {
			if (element == null) {
				throw new NullPointerException("Null elements are not allowed.");
			}
			Node<E> newNode = new Node<>(element, prev, null);
			if (prev == null) {
				head = newNode; // First element becomes head
			} else {
				prev.setNext(newNode);
			}
			prev = newNode;
		}
	}

	/**
	 * Returns the number of elements in this list.
	 * @return the number of elements in this list
	 */
	@Override
	public int size()
	{
		int count = 0;
		// TODO 502 Complete size()...
		Node<E> current = head;
		while (current != null) {
			count++;
			current = current.getNext();
		}
		return count;
	}

	/**
	 * Returns a list iterator over the elements in this list (in proper
	 * sequence), starting at the specified position in the list.
	 * The specified index indicates the first element that would be
	 * returned by an initial call to {@link ListIterator#next next}.
	 * An initial call to {@link ListIterator#previous previous} would
	 * return the element with the specified index minus one.
	 * @param index index of the first element to be returned from the
	 * list iterator (by a call to {@link ListIterator#next next})
	 * @return a list iterator over the elements in this list (in proper
	 * sequence), starting at the specified position in the list
	 * @throws IndexOutOfBoundsException if the index is out of range
	 * ({@code index < 0 || index > size()})
	 */
	@Override
	public ListIterator<E> listIterator(int index)
	    throws IndexOutOfBoundsException
	{
		// TODO 503 Complete listIterator(int index)...
		// Guard clause : Check index validity in [0...size()]
		// Then return a new NodeListIterator on this list at index
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException("Index out of range: " + index);
		}
		return new NodeListIterator<>(this, index);
	}

	/**
	 * Replaces the element at the specified position in this list with the
	 * specified element (optional operation).
	 * <p>
	 * This implementation first gets a list iterator pointing to the
	 * indexed element (with {@code listIterator(index)}). Then, it gets
	 * the current element using {@code ListIterator.next} and replaces it
	 * with {@code ListIterator.set}.
	 * <p>
	 * Note that this implementation will throw an
	 * {@code UnsupportedOperationException} if the list iterator does not
	 * implement the {@code set} operation.
	 * @throws ClassCastException if the class of the specified element
	 * prevents it from being added to this list
	 * @throws NullPointerException if the specified element is null and
	 * this list does not permit null elements. This implementation ensures
	 * {@link NullPointerException} is thrown first if element is null.
	 * @throws IllegalArgumentException if some property of the specified
	 * element prevents it from being added to this list
	 * @throws IndexOutOfBoundsException if the index is out of range
	 * ({@code index < 0 || index >= size()})
	 */
	@Override
	public E set(int index, E element)
	{
		// TODO 504 Complete set(int index, E element)...
		// If element is null then throw NullPointerException
		// Return super.set
		if (element == null) {
			throw new NullPointerException("Element cannot be null.");
		}
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		ListIterator<E> iterator = listIterator(index);
		E oldValue = iterator.next();
		iterator.set(element);
		return oldValue;
	}

	/**
	 * Inserts the specified element at the specified position in this list
	 * (optional operation). Shifts the element currently at that position
	 * (if any) and any subsequent elements to the right (adds one to their
	 * indices).
	 * <p>
	 * This implementation first gets a list iterator pointing to the
	 * indexed element (with {@code listIterator(index)}). Then, it
	 * inserts the specified element with {@code ListIterator.add}.
	 * <p>
	 * Note that this implementation will throw an
	 * {@code UnsupportedOperationException} if the list iterator does not
	 * implement the {@code add} operation.
	 * @throws ClassCastException if the class of the specified element
	 * prevents it from being added to this list
	 * @throws NullPointerException if the specified element is null and
	 * this list does not permit null elements. This implementation ensures
	 * {@link NullPointerException} is thrown first if element is null.
	 * @throws IllegalArgumentException if some property of the specified
	 * element prevents it from being added to this list
	 * @throws IndexOutOfBoundsException if the index is out of range
	 * ({@code index < 0 || index > size()})
	 */
	@Override
	public void add(int index, E element)
	{
		// TODO 505 Complete add(int index, E element)...
		// If element is null then throw NullPointerException
		// Call super.add
		if (element == null) {
			throw new NullPointerException("Element cannot be null.");
		}
		ListIterator<E> iterator = listIterator(index);
		iterator.add(element);
	}

	/**
	 * Get the current head {@link Node} of this list
	 */
	@Override
	public Node<E> getHead()
	{
		return head;
	}

	/**
	 * Set the current head {@link Node} of this list
	 */
	@Override
	public void setHead(Node<E> head)
	{
		this.head = head;
	}

	// TODO 506 Run tests/ListTest
}
