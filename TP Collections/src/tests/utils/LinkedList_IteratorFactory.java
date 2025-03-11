/**
 *
 */
package tests.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A Factory to obtain {@link LinkedList}'s {@link Iterator}s
 * @param <E> The type of elements to iterate over
 */
public class LinkedList_IteratorFactory<E> implements IteratorFactory<E>
{
	/**
	 * Factory method to obtain an empty {@link Iterator}.
	 * @return an empty {@link Iterator}
	 */
	@Override
	public Iterator<E> emptyIterator()
	{
		LinkedList<E> al = new LinkedList<>();
		return al.iterator();
	}

	/**
	 * Factory method to obtain an {@link Iterator} iterating over a single
	 * element.
	 * @param element the single element to iterate over
	 * @return a {@link Iterator} on a single element
	 */
	@Override
	public Iterator<E> singleIterator(E element)
	{
		LinkedList<E> al = new LinkedList<>();
		al.add(element);
		return al.iterator();
	}

	/**
	 * Factory method to obtain an {@link Iterator} iterating over multiple
	 * elements.
	 * @param elements the list of elements to iterate over
	 * @return a {@link Iterator} on multiples elements
	 */
	@Override
	public Iterator<E> multipleIterator(Collection<E> elements)
	{
		LinkedList<E> al = new LinkedList<>();
		for (E element : elements)
		{
			al.add(element);
		}
		return al.iterator();
	}
}
