package collections;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

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
		// DONE 200 Complete NodeCollection(Collection<? extends E> c)...
		this();
		// Check c is non null otherwise throw NullPointerException
		Objects.requireNonNull(c);
		// Add all elements in c
		for (E elt : c)
		{
			add(elt);
		}
	}

	/**
	 * Go to last (non null) node starting from {@link #head}
	 * @return The last node starting from {@link #head} or null if there
	 * is no nodes.
	 */
	protected Node<E> lastNode()
	{
		// DONE 201 Complete lastNode()...
		// Set a node to head
		Node<E> current = head;
		Node<E> previous = null;
		// Navigate to last non null node (if there are nodes)
		while (current != null)
		{
			previous = current;
			current = current.getNext();
		}
		// Return the node
		return previous;
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
		// DONE 202 Complete add(E e)...
		// Throw NullPointerException if e is null
		Objects.requireNonNull(e);
		// Fetch last non null node
		Node<E> lastNode = lastNode();
		// Create a new node with e
		Node <E> newNode = new Node<E>(e);
		// If last node is null then set a new head
		if (lastNode == null)
		{
			head = newNode;
		}
		else // Otherwise insert new node after last
		{
			lastNode.insertNext(newNode);
		}
		return true;
	}

    /**
     * Returns an iterator over the elements contained in this collection.
     * @return an new {@link NodeIterator} over the elements contained in this
     * collection
     */
	@Override
	public Iterator<E> iterator()
	{
		// DONE 203 Complete iterator()...
		return new NodeIterator<>(this);
	}

	/**
	 * Number of elements in this collection
	 * @return the number of elements in this collection
	 */
	@Override
	public int size()
	{
		// DONE 204 Complete size()...
		if (head == null)
		{
			return 0;
		}

		return head.nextLength();
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
		// DONE 205 Complete hashCode()...
		final int prime = 31;
		int hash = 1;
		for (E elt : this)
		{
			hash = (prime * hash) + elt.hashCode();
		}
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
		// DONE 206 Complete equals(Object obj)...
		if (obj == null)
		{
			return false;
		}

		if (obj == this)
		{
			return true;
		}

		if (!(obj instanceof Iterable<?>))
		{
			return false;
		}

		Iterable<?> iterable = (Iterable<?>) obj;
		Iterator<E> it1 = iterator();
		Iterator<?> it2 = iterable.iterator();
		while (it1.hasNext() && it2.hasNext())
		{
			if (!it1.next().equals(it2.next()))
			{
				return false;
			}
		}
		return it1.hasNext() == it2.hasNext();
	}
}
