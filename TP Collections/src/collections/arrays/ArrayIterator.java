package collections.arrays;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator over and array
 * @param <E> the type of elements in the array
 */
public class ArrayIterator<E> implements Iterator<E>
{
	/**
	 * The object containing the array we're iterating over
	 * @implNote Obtaining the array requires {@link Capacity#getArray()}
	 */
	protected Capacity<E> holder = null;

	/**
	 * The current index of the iterator in the array
	 */
	protected int index;

	/**
	 * Number of non null elements in the array (might be smaller than
	 * array.length)
	 */
	protected int size;

	/**
	 * Index of the last element returned by {@link #next()} and also index
	 * of the element to remove with {@link #remove()}.
	 * @implSpec Whenever there is no last returned element (when next hasn't
	 * been called yet or after removing an element) the default value shall be
	 * -1.
	 */
	protected int lastReturnedIndex;

	/**
	 * Default constructor
	 * @param holder the holder of the array to iterate over
	 * @throws NullPointerException if the provided holder is null
	 */
	public ArrayIterator(Capacity<E> holder) throws NullPointerException
	{
		// TODO 400 Complete ArrayIterator(Capacity<E> holder)
		// Throw NullPointerException if holder is null
		// Set holder
		// Set index to 0
		// Compute size by exploring the array of holder
		// Set lastReturnedIndex to its default value (-1)
	}

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     * @return {@code true} if the iteration has more elements
     */
	@Override
	public boolean hasNext()
	{
		return index < size;
	}

    /**
     * Returns the next element in the iteration.
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
	@Override
	public E next() throws NoSuchElementException
	{
		// TODO 401 Complete next()...
		// Check with hasNext() if there is a next element
		// Throw NoSuchElementException if there is no next element
		// Set lastReturnedIndex to index
		// Return the element at index in array
		// Increment index
		return null;
	}

	/**
	 * Removes from the underlying collection the last element returned
	 * by this iterator (optional operation). This method can be called
	 * only once per call to {@link #next}.
	 * @throws IllegalStateException if the {@code next} method has not
	 * yet been called, or the {@code remove} method has already
	 * been called after the last call to the {@code next}
	 * method
	 */
	@Override
	public void remove() throws IllegalStateException
	{
		// TODO 402 Complete remove()...
		// Throw IllegalStateException if next() has not been called yet (see lastReturnedIndex's value)
		// remove lastReturnedIndex elt in array
		//	by shifting all following elts one step back
		//	and setting array's last non null elt to null
		// decrease size
		// update index to lastReturnedIndex
		// reset lastReturnedIndex to its default value
	}

	// TODO 403 Run test/IteratorTest JUnit test
	// TODO 500 Create ArrayCollection class
	// TODO 502 Run tests/CollectionTest JUnit test
}
