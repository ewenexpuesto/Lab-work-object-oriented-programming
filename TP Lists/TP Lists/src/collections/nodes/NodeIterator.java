package collections.nodes;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * List iterator over {@link Node}s
 * @param <E> The type of elements to iterate over
 */
public class NodeIterator<E> implements Iterator<E>
{
	/**
	 * The holder of head node (iff any)
	 * @implNote Obtaining the first node for iteration requires the use of
	 * {@link Headed#getHead()}
	 * @implNote Changing the first node of iteration requires the use of
	 * {@link Headed#setHead(Node)}
	 * @implSpec {@link #headed} shall <b>never<b/> be null.
	 * However it can hold a null head.
	 */
	protected Headed<E> headed = null;

	/**
	 * Next node during iteration
	 */
	protected Node<E> next;

	/**
	 * Last return node always points to the last element
	 * returned by {@link #next()} (or {@link java.util.ListIterator#previous()})
	 */
	protected Node<E> lastReturned;

	/**
	 * Flag indicating {@link #next()} has been called
	 */
	protected boolean nextCalled;

	/**
	 * Constructor from a head holder and an index
	 * @param headed The head holder to obtain first {@link Node} from
	 * @param index the index of the node in the node chain to start from with
	 * this iterator.
	 * @throws NullPointerException if the provided head holder is null,
	 * however the head holder can hold a null head.
	 * @throws IndexOutOfBoundsException if index is invalid (< 0 or > size)
	 * @implSpec provided index can't be negative or greater than the number of
	 * nodes (size) otherwise a exception should be thrown. However if provided
	 * index is equal to size, this iterator shall not have any more elements to
	 * iterate.
	 * @see #node(int)
	 */
	public NodeIterator(Headed<E> headed, int index)
	    throws NullPointerException,
	    IndexOutOfBoundsException
	{
		// DONE 101 Complete NodeIterator(Headed<E> headed, int index)...
		// If headed == null throw a NullPointerException
		Objects.requireNonNull(headed);
		this.headed = headed;

		// If index if invalid (< 0 or > size) throw an IndexOutOfBoundsException
		int size = 0;
		Node<E> current = headed.getHead();
		while (current != null)
		{
			current = current.getNext();
			size++;
		}
		if ((index < 0) || (index > size))
		{
			throw new IndexOutOfBoundsException(index);
		}

		// Set #next node to indexth node, lastReturned & nextCalled
		next = (index == size) ? null : node(index);
		lastReturned = null;
		nextCalled = false;
	}

	/**
	 * Constructor from a head holder alone
	 * @param headed The head holder to obtain first {@link Node} from
	 */
	public NodeIterator(Headed<E> headed)
	{
		this(headed, 0);
	}

	/**
	 * Access to node of index index.
	 * @param index the index of the desired node
	 * @return the desired node
	 * @implNote This method doesn't check index validity
	 */
	protected Node<E> node(int index)
	{
		// DONE 100 Complete node(int) to fetch node at index...
		Node<E> current = headed.getHead();
		for (int i = 0; i < index; i++)
		{
			current = current.getNext();
		}
		return current;
	}

	/**
	 * Collapses non-null node x.
	 * Collapses the provided node by relinking its previous and next nodes
	 * bypassing the provided node.
	 * @param x the node to unlink
	 * @return the value of the unlinked node
	 * @implNote Whenever {@code x.getPrevious()} is null, it means we're
	 * collapsing the first node of the chain, and therefore head node needs to
	 * be updated using {@link Headed#setHead(Node)} to the next node.
	 */
	protected E collapse(Node<E> x)
	{
		final E element = x.getData();
		final Node<E> next = x.getNext();
		final Node<E> prev = x.getPrevious();

		// DONE 102 Complete unlink(Node<E> x) ...
		// prev :
	 	// 	if prev is null then change head to next
		if (prev == null)
		{
			headed.setHead(next);
		}
		//	otherwise just set next as its next node
		else
		{
			prev.setNext(next);
		}

		// next :
		//	if non null then set prev as its previous node
		if (next != null)
		{
			next.setPrevious(prev);
		}

		// Set x's previous and next to null to help GC.
		x.setPrevious(null);
		x.setNext(null);

		return element;
	}

	/**
	 * Returns {@code true} if this list iterator has more elements when
	 * traversing the list in the forward direction. (In other words,
	 * returns {@code true} if {@link #next} would return an element rather
	 * than throwing an exception.)
	 * @return {@code true} if the iterator has more elements when
	 * traversing the list in the forward direction
	 */
	@Override
	public boolean hasNext()
	{
		return next != null;
	}

	/**
	 * Returns the next element in the list and advances the cursor position.
	 * This method may be called repeatedly to iterate through the list.
	 * @return the next element in the list
	 * @throws NoSuchElementException if the iteration has no next element
	 */
	@Override
	public E next() throws NoSuchElementException
	{
		if (!hasNext())
		{
			throw new NoSuchElementException("No next element");
		}
		// DONE 103 Complete next()...
		// Store next's data (returned later)
		E value = next.getData();
		// set lastReturned to next
		lastReturned = next;
		// move next to its next
		next = next.getNext();
		// set nextCalled
		nextCalled = true;
		// return stored value
		return value;
	}

	/**
	 * Removes from the list the last element that was returned by {@link
	 * #next}.
	 * This call can only be made once per call to {@code next}.
	 * @throws IllegalStateException if {@code next} have been called,
	 * or {@code remove} have been called after the last call to {@code next}
	 * @implSpec The goal here is to collapse {@link #lastReturned} node if
	 * possible
	 */
	@Override
	public void remove() throws IllegalStateException
	{
		// DONE 104 Complete remove()...
		// if next() has not been called yet throw an IllegalStateException
		if (!nextCalled)
		{
			throw new IllegalStateException("next() not called yet");
		}

		// if there is no lastReturned to remove throw an IllegalStateException
		if (lastReturned == null)
		{
			throw new IllegalStateException("null last returned node");
		}

		// collapse lastReturned node (using  unlink(...))
		collapse(lastReturned);
		// reset lastReturned to null since it has been unlinked
		lastReturned = null;
		// set nextCalled to false
		nextCalled = false;
	}

	// DONE 105 Run test/NodeIteratorTest JUnit test
	// DONE 106 Run test/IteratorTest JUnit test
}
