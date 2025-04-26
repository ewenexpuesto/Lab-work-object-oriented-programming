package tests.utils;

import java.util.Collection;
import java.util.ListIterator;

/**
 * Factory model to build {@link ListIterator}s
 * @param <E> The type of elements to iterate over
 */
public interface ListIteratorFactory<E> extends IteratorFactory<E>
{
	/**
	 * Factory method to obtain an empty {@link ListIterator}.
	 * @return an empty {@link ListIterator}
	 */
	public abstract ListIterator<E> emptyListIterator();

	/**
	 * Factory method to obtain an {@link ListIterator} iterating over a single
	 * element.
	 * @param element the single element to iterate over
	 * @return a {@link ListIterator} on a single element
	 */
	public abstract ListIterator<E> singleListIterator(E element);

	/**
	 * Factory method to obtain an {@link ListIterator} iterating over multiple
	 * elements.
	 * @param elements the list of elements to iterate over
	 * @return a {@link ListIterator} on multiples elements
	 */
	public abstract ListIterator<E> multipleListIterator(Collection<E> elements);
}
