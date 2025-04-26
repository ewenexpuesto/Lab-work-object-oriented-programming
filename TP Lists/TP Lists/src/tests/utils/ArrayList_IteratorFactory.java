/**
 *
 */
package tests.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A Factory to obtain {@link ArrayList}'s {@link Iterator}s
 * @param <E> The type of elements to iterate over
 */
public class ArrayList_IteratorFactory<E> implements IteratorFactory<E>
{

	/**
	 * Factory method to obtain an empty {@link Iterator}.
	 * @return an empty {@link Iterator}
	 */
	@Override
	public Iterator<E> emptyIterator()
	{
		ArrayList<E> al = new ArrayList<>();
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
		ArrayList<E> al = new ArrayList<>();
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
		ArrayList<E> al = new ArrayList<>();
		for (E element : elements)
		{
			al.add(element);
		}
		return al.iterator();
	}

}
