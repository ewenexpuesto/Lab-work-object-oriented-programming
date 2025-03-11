package collections;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

import collections.nodes.Headed;
import collections.nodes.Node;
import collections.nodes.NodeIterator;

/**
 * A Collection implementation based on {@link Node}s
 * @param <E> the type of elements in this collection
 * @implNote this Collection does not allow null elements. Attempts to add null
 * elements should trigger {@link NullPointerException}
 */
public class NodeCollection<E> extends AbstractCollection<E> implements Headed<E>
{
	/**
	 * The Head node of this collection
	 */
	private Node<E> head;

	/**
	 * Default constructor.
	 * Creates an empty collection (with null head)
	 */
	public NodeCollection()
	{
		head = null;
	}

	/**
	 * Copy constructor
	 * @param c the collection to copy in this collection
	 * @throws NullPointerException if the provided collection contains null
	 * elements or if provided collection is null.
	 */
	public NodeCollection(Collection<? extends E> c) throws NullPointerException
	{
		this();
		// TODO 200 Complete NodeCollection(Collection<? extends E> c)...
		// Check c is non null otherwise throw NullPointerException
		// Add all elements in c
	}

	/**
	 * Go to last (non null) node starting from {@link #head}
	 * @return The last node starting from {@link #head} or null if there
	 * is no nodes.
	 */
	protected Node<E> lastNode()
	{
		// TODO 201 Complete lastNode()...
		// Set a node to head
		// Navigate to last non null node (if there are nodes)
		// Return the node
		return null;
	}

    /**
     * {@inheritDoc}
     * @implSpec This implementation throws a {@link NullPointerException} if
     * provided element is null since this collection does not allow null
     * elements.
     * @throws NullPointerException if the specified element is null because
     * this collection does not permit null elements.
     */
	@Override
	public boolean add(E e) throws NullPointerException
	{
		// TODO 202 Complete add(E e)...
		// Throw NullPointerException if e is null
		// Fetch last non null node
		// Create a new node with e
		// If last node is null then set a new head
		// Otherwise insert new node after last
		return false;
	}

    /**
     * Returns an iterator over the elements contained in this collection.
     * @return an new {@link NodeIterator} over the elements contained in this
     * collection
     */
	@Override
	public Iterator<E> iterator()
	{
		// TODO 203 Complete iterator()...
		return null;
	}

	/**
	 * Number of elements in this collection
	 * @return the number of elements in this collection
	 */
	@Override
	public int size()
	{
		// TODO 204 Complete size()...
		return -1;
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

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     * {@link java.util.HashMap}.
     * @return  a hash code value for this object.
     */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int hash = 1;
		// TODO 205 Complete hashCode()...
		return hash;
	}

	/**
	 * Deep comparison with another object
	 * @param obj the other object to compare to
	 * @return true if the other object is a {@link Iterable} with the same
	 * objects in the same order.
	 */
	@Override
	public boolean equals(Object obj)
	{
		// TODO 206 Complete equals(Object obj)...
		return false;
	}
}
