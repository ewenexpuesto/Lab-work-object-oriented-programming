package collections.lists;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;

import collections.nodes.Headed;
import collections.nodes.Node;

/**
 * {@link List} using a {@link Node} chain.
 * @param <E> The type of data in this {@link List}
 */
public class NodeList<E> extends AbstractList<E> implements Headed<E>
{
	/**
	 * Head node of this list
	 */
	private Node<E> head;

	/**
	 * Default constructor.
	 * Builds an empty list
	 * @see collections.NodeCollection#NodeCollection()
	 */
	public NodeList()
	{
		// TODO 300 Complete NodeList()...
		this.head = null;
	}

	/**
	 * Copy constructor from collection
	 * @param c the collection to copy
	 * @see collections.NodeCollection#NodeCollection(Collection)
	 * @see #add(Object)
	 */
	public NodeList(Collection<E> c)
	{
		// TODO 301 Complete NodeList(Collection<E> c)...
		this();
		for (E e : c)
		{
			add(e);
		}
	}

	/**
	 * Access to node of index index
	 * @param index the index of the desired node
	 * @return the desired node or null if there are no nodes in this list.
	 */
	private Node<E> node(int index)
	{
		Node<E> current = head;
		// TODO 302 Complete node(int index)...
		if (index < 0) {
			throw new IndexOutOfBoundsException("Index must be non-negative: " + index);
		}
		else if (index == 0) {
			if (current == null) {
				throw new IndexOutOfBoundsException("Index out of bounds: " + index);
			}
			else {
				return current;
			}
			
		}
		else {
			int i = 0;
			while (current != null && i < index) {
				current = current.getNext();
				i++;
			}
		}
		return current;
	}

	/**
	 * Access to last node
	 * @return the last non null node or null if there are no nodes in this list.
	 */
	private Node<E> lastNode()
	{
		Node<E> current = head;
		// TODO 303 Complete lastNode()...
		if (head == null) return null;
		while (current.getNext() != null) {
			current = current.getNext();
		}
		return current;
	}

	/**
	 * Returns the number of elements in this list.
	 * @return the number of elements in this list
	 */
	@Override
	public int size()
	{
		int count = 0;
		// TODO 304 Complete size() ...
		Node<E> current = head;
		while (current != null) {
			count++;
			current = current.getNext();
		}
		return count;
	}

	/**
	 * Returns the element at the specified position in this list.
	 * @param index the index of the element to return
	 * @return the element at the specified position in this list
	 * @throws IndexOutOfBoundsException if the index is out of range
	 * ({@code index < 0 || index >= size()})
	 * @see #node(int)
	 */
	@Override
	public E get(int index) throws IndexOutOfBoundsException
	{
		// TODO 305 Complete get(int index) ...
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
		}
		return node(index).getData();
	}

	/**
	 * Replaces the element at the specified position in this list with the
	 * specified element (optional operation).
	 * @param index index of the element to replace
	 * @param element element to be stored at the specified position
	 * @return the element previously at the specified position
	 * @throws ClassCastException if the class of the specified element
	 * prevents it from being added to this list
	 * @throws NullPointerException if the specified element is null and
	 * this list does not permit null elements
	 * @throws IllegalArgumentException if some property of the specified
	 * element prevents it from being added to this list
	 * @throws IndexOutOfBoundsException if the index is out of range
	 * ({@code index < 0 || index >= size()})
	 */
	@Override
	public E set(int index, E element)
	{
		// TODO 306 Complete set(int index, E element)...
		// Guard clauses to check element and index
		// Search for node to be replaced
		// Store replaced node data, previous and next
		// Create new node with element data, and replaced previous & next
		// If index == 0 then also update #head with new node
		// Return replaced node's value
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException("Index out of range: " + index);
		}
		if (element == null) {
			throw new NullPointerException();
		}
		if (index != 0) {
			if (index != size()-1) {
				Node<E> e = node(index);  // Find the node at the given index
				E oldData = e.getData();  // Store the previous value
				Node<E> previous = e.getPrevious(); // throws exception if null
				Node<E> next = e.getNext();
				Node<E> new_node = new Node<>(element, previous, next);
				next.setPrevious(new_node);
				previous.setNext(new_node);
				return oldData;  // Return the old element
			}
			else {
				Node<E> e = node(index);  // Find the node at the given index
				E oldData = e.getData();  // Store the previous value
				Node<E> previous = e.getPrevious();
				Node<E> next = null;
				Node<E> new_node = new Node<>(element, previous, next);
				previous.setNext(new_node);
				return oldData;
			}
		}
		else {
			Node<E> e = node(index);  // Find the node at the given index
			E oldData = e.getData();
			Node<E> next = e.getNext();
			Node<E> new_node = new Node<>(element, null, next);
			next.setPrevious(new_node);
			setHead(e);
			return oldData;
		}
	}

	/**
	 * Inserts the specified element at the specified position in this list
	 * Shifts the element currently at that position
	 * (if any) and any subsequent elements to the right (adds one to their
	 * indices).
	 * @param index index at which the specified element is to be inserted
	 * @param element element to be inserted
	 * @throws ClassCastException if the class of the specified element
	 * prevents it from being added to this list
	 * @throws NullPointerException if the specified element is null and
	 * this list does not permit null elements
	 * @throws IllegalArgumentException if some property of the specified
	 * element prevents it from being added to this list
	 * @throws IndexOutOfBoundsException if the index is out of range
	 * ({@code index < 0 || index > size()})
	 * @implNote Can't use {@code listIterator(index).add(element);} here since
	 * it would trigger a call to this method leading to a stack overflow error
	 */
	@Override
	public void add(int index, E element)
	{
		// TODO 307 Complete add(int index, E element) ...
		// Guard clauses to check index & element
		// Search previous and next nodes
		//	With special cases :
		//		- if index == 0 then next node is head
		//		- if index == size then previous node is last node
		// Otherwise next is node at index and previous next's previous
		// Insert new node with element data between found previous and next nodes
		// If index == 0 then also update #head with new node
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException("Index out of range: " + index);
		}
		if (element == null) {
			throw new NullPointerException("This list does not permit null elements.");
		}
	
		// Case 1: Inserting at the beginning (index == 0)
		if (index == 0) {
			Node<E> newNode = new Node<>(element, null, head);
			if (head != null) {
				head.setPrevious(newNode);
			}
			head = newNode;
			return;
		}
	
		// Case 2: Inserting at the end (index == size())
		if (index == size()) {
			Node<E> lastNode = node(size() - 1);
			Node<E> newNode = new Node<>(element, lastNode, null);
			lastNode.setNext(newNode);
			return;
		}
	
		// Case 3: Inserting in the middle
		Node<E> nextNode = node(index);   // Get the node at the given index
		Node<E> prevNode = nextNode.getPrevious();
		Node<E> newNode = new Node<>(element, prevNode, nextNode);
		
		prevNode.setNext(newNode);
		nextNode.setPrevious(newNode);
	}

	/**
	 * Removes the element at the specified position in this list.
	 * Shifts any subsequent elements to the left (subtracts one from their
	 * indices). Returns the element that was removed from the list.
	 * @param index the index of the element to be removed
	 * @return the element previously at the specified position
	 * @throws IndexOutOfBoundsException if the index is out of range
	 * ({@code index < 0 || index >= size()})
	 * @see #node(int)
	 * @see Node#remove()
	 */
	@Override
	public E remove(int index)
	{
		// TODO 308 Complete remove(int index) ...
		// Guard Clause to check index
		// Search for node to remove
		// Store removed node's value and next node
		// Collapse removed node (cross link its previous to its next)
		// If removed node was #head then update #head to next node
		// Return removed node value
    // Guard clause: Ensure index is within valid bounds
    if (index < 0 || index >= size()) {
        throw new IndexOutOfBoundsException("Index out of range: " + index);
    }

    // Locate the node to be removed
    Node<E> removedNode = node(index);
    E removedData = removedNode.getData();  // Store the data to return

    // If the node to remove is the head, update head reference
    if (removedNode == head) {
        head = removedNode.getNext();
        if (head != null) {
            head.setPrevious(null);  // Unlink the new head's previous reference
        }
    } else {
        // Otherwise, collapse the node properly
        removedNode.remove();
    }

    return removedData;  // Return the removed element
	}

	/**
	 * Accessor to the head node
	 * @return the head node
	 */
	@Override
	public Node<E> getHead()
	{
		return head;
	}

	/**
	 * Mutator of the head node
	 * @param head the new head to set
	 */
	@Override
	public void setHead(Node<E> head)
	{
		this.head = head;
	}

	// TODO 309 Run tests/ListTest
}
