package tests.utils;

import java.util.Collection;
import java.util.Iterator;

/**
 * Factory model to build {@link Iterator}s
 * @param <E> The type of elements to iterate over
 */
public interface IteratorFactory<E>
{
	/**
	 * Factory method to obtain an empty {@link Iterator}.
	 * @return an empty {@link Iterator}
	 */
	public abstract Iterator<E> emptyIterator();

	/**
	 * Factory method to obtain an {@link Iterator} iterating over a single
	 * element.
	 * @param element the single element to iterate over
	 * @return an {@Iterator} on a single element
	 */
	public abstract Iterator<E> singleIterator(E element);

	/**
	 * Factory method to obtain an {@link Iterator} iterating over multiple
	 * elements.
	 * @param elements the list of elements to iterate over
	 * @return a {@link Iterator} on multiples elements
	 */
	public abstract Iterator<E> multipleIterator(Collection<E> elements);
}
