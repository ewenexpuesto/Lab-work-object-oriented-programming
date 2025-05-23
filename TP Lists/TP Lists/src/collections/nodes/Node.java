package collections.nodes;

import java.util.Objects;

/**
 * A Data node class with previous and next node
 * @param <E> The type of data in this node
 */
public class Node<E>
{
	/**
	 * Data of the node
	 * @implSpec this data should be immutable: i.e. when created the node
	 * should never change its value. Changing value should be done by
	 * creating a new node with the new value and replacing the old node.
	 */
	private E data;

	/**
	 * Previous node.
	 * @implSpec might be null if there is no previous node
	 */
	private Node<E> previous;

	/**
	 * Next node
	 * @implSpec might be null if there is no next node
	 */
	private Node<E> next;

	/**
	 * Valued constructor
	 * @param data the data to set in this node
	 * @param previous the next node (or null if no next node)
	 * @param next the previous node (or null if no previous node)
	 * @throws NullPointerException if provided data is null
	 */
	public Node(E data, Node<E> previous, Node<E> next)
	    throws NullPointerException
	{
		Objects.requireNonNull(data);
		this.data = data;
		this.previous = previous;
		if (previous != null)
		{
			previous.next = this;
		}
		this.next = next;
		if (next != null)
		{
			next.previous = this;
		}
	}

	/**
	 * Constructor with data only. #previous and #next nodes are considered null
	 * @param data the data to set in this node
	 * @throws NullPointerException if provided data is null
	 */
	public Node(E data) throws NullPointerException
	{
		// DONE 001 Complete Node(E data) : use Node(E data, Node<E> previous, Node<E> next)
		this(data, null, null);
	}

	/**
	 * Data accessor
	 * @return the data
	 */
	public E getData()
	{
		return data;
	}

	/**
	 * Previous node accessor
	 * @return the previous node
	 */
	public Node<E> getPrevious()
	{
		return previous;
	}

	/**
	 * Next node accessor
	 * @return the next node
	 */
	public Node<E> getNext()
	{
		return next;
	}

	/**
	 * Indicates if there is a next node
	 * @return true if there is a next node, false otherwise
	 */
	public boolean hasNext()
	{
		return next != null;
	}

	/**
	 * Indicates if there is a previous node
	 * @return true if there is a next node, false otherwise
	 */
	public boolean hasPrevious()
	{
		return previous != null;
	}

	/**
	 * Previous node setter
	 * @param node the previous node to set
	 */
	public void setPrevious(Node<E> node)
	{
		// DONE 002 Set node as previous (and also this as node's next)...
		previous = node;
		if (node != null)
		{
			node.next = this;
		}
	}

	/**
	 * Next node setter
	 * @param node the next node to set
	 */
	public void setNext(Node<E> node)
	{
		// DONE 003 Set node as next (and also this as node's previous)...
		next = node;
		if (node != null)
		{
			node.previous = this;
		}
	}

	/**
	 * Insert node after current
	 * @param node the node to insert
	 * @throws NullPointerException if provided node is null
	 */
	public void insertNext(Node<E> node) throws NullPointerException
	{
		if (node == null)
		{
			throw new NullPointerException("insertNext(null)");
		}

		// DONE 004 Complete insertNext(Node<E>)...
		// Insert provided node between this and this.next:
		// Store current next as oldNext
		Node<E> oldNext = next;
		// Set new next as node
		next = node;
		// Set node's next as oldNext
		node.next = oldNext;
		// Set node's previous as this
		node.previous = this;
		// Set oldNext's previous as node (iff oldNext is non null)
		if (oldNext != null)
		{
			oldNext.previous = node;
		}
	}

	/**
	 * Insert node before current
	 * @param node the node to insert
	 * @throws NullPointerException if provided node is null
	 */
	public void insertPrevious(Node<E> node) throws NullPointerException
	{
		if (node == null)
		{
			throw new NullPointerException("insertPrevious(null)");
		}

		// DONE 005 Complete insertPrevious(Node<E>)...
		// Insert provided node between this.previous and this:
		// Store current previous as oldPrevious
		Node<E> oldPrevious = previous;
		// Set new previous as node
		previous = node;
		// Set node's previous as oldPrevious
		node.previous = oldPrevious;
		// Set node's next as this
		node.next = this;
		// Set oldPrevious's next as node (iff oldPrevious is non null)
		if (oldPrevious != null)
		{
			oldPrevious.next = node;
		}
	}

	/**
	 * Remove the next node (if any)
	 */
	public void removeNext()
	{
		Node<E> removed = next;
		if (removed == null)
		{
			return;
		}

		// DONE 006 Complete removeNext()...
		// link this <-> removed's next bidirectionnaly:
		// Store removed next as removedNext
		Node<E> removedNext = removed.next;
		// Set next as removedNext
		next = removedNext;
		// Set removedNext's previous as this (iff removedNext is non null)
		if (removedNext != null)
		{
			removedNext.previous = this;
		}

		/*
		 * Make GB's job easier by clearing links
		 */
		removed.unlink();
	}

	/**
	 * Remove the previous node (if any)
	 */
	public void removePrevious()
	{
		Node<E> removed = previous;
		if (removed == null)
		{
			return;
		}

		// DONE 007 Complete removePrevious()...
		// Store removed's previous as removedPrevious
		Node<E> removedPrevious = removed.previous;
		// Set new previous as removedPrevious
		previous = removedPrevious;
		// Set removedPrevious's next as this (iff removedPrevious is non null)
		if (removedPrevious != null)
		{
			removedPrevious.next = this;
		}

		/*
		 * Make GB's job easier by clearing links
		 */
		removed.unlink();
	}

	/**
	 * Collapse node by linking previous and next together
	 * and then {@link #unlink()} current node.
	 */
	public void remove()
	{
		// DONE 008 Complete remove()...
		if (previous != null)
		{
			previous.next = next;
		}
		if (next != null)
		{
			next.previous = previous;
		}
		/*
		 * Make GB's job easier by clearing links
		 */
		unlink();
	}

	/**
	 * Reset {@link #next} and {@link #previous} to null
	 * @implNote This helps the garbage collector
	 */
	protected void unlink()
	{
		previous = null;
		next = null;
	}

	/**
	 * Check that the previous and following links are reciprocal.
	 * That is to say that previous.next == this and next.previous == this.
	 * @return true if this node has dangling links, false otherwise
	 */
	public boolean isDangling()
	{
		if ((previous == null) && (next == null))
		{
			return false;
		}
		boolean danglingPrevious = previous == null ? false : previous.next != this;
		boolean danglingNext = next == null ? false : next.previous != this;
		return danglingPrevious || danglingNext;
	}

	/**
	 * Number of next nodes chain starting at this node
	 * @return the number of next nodes + the current node
	 */
	public int nextLength()
	{
		int count = 0;
		Node<E> current = this;
		// DONE 009 complete nextLength()...
		// Count the number of next nodes starting from this (included)
		while (current != null)
		{
			current = current.getNext();
			count++;
		}
		return count;
	}

	/**
	 * Number of previous nodes chain starting at this node
	 * @return the number of previous nodes + the current node
	 */
	public int previousLength()
	{
		int count = 0;
		// DONE 010 complete previousLength()...
		Node<E> current = this;
		// Count the number of previous nodes starting from this (included)
		while (current != null)
		{
			current = current.getPrevious();
			count++;
		}
		return count;
	}

	/**
	 * Compare with another object
	 * @param obj the other object to compare with
	 * @return true if other object has the same data and links
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}

		if (obj == this)
		{
			return true;
		}

		if (!(obj instanceof Node<?>))
		{
			return false;
		}

		Node<?> node = (Node<?>) obj;
		return data.equals(node.data) && (previous == node.previous)
		    && (next == node.next);
	}

	/**
	 * Hash code for this node based on
	 * <ul>
	 * 	<li>the data it holds</li>
	 * 	<li>wether it has a previous node</li>
	 * 	<li>wether it has a next node</li>
	 * </ul>
	 * @return the hash value of this node
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + data.hashCode();
		result = (prime * result) + (hasPrevious() ? 1231 : 1237);
		result = (prime * result) + (hasNext() ? 1231 : 1237);
		return result;
	}

	/**
	 * Clean node before garbage collecting
	 */
	@Override
	protected void finalize() throws Throwable
	{
		unlink();
	}

	/**
	 * String representation of this node
	 * @return a string representation of this node
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		if (hasPrevious())
		{
			sb.append("<-");
		}

		sb.append('[');
		sb.append(data.toString());
		sb.append(']');

		if (hasNext())
		{
			sb.append("->");
		}

		return sb.toString();
	}

	// DONE 011 Run test/NodeTest JUnit test...
}
