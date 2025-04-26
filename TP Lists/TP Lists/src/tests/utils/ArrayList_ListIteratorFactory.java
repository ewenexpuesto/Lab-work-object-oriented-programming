/**
 *
 */
package tests.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;

/**
 * A Factory to obtain {@link ArrayList}'s {@link ListIterator}s
 * @param <E> The type of elements to iterate over
 */
public class ArrayList_ListIteratorFactory<E>
    extends ArrayList_IteratorFactory<E> implements ListIteratorFactory<E>
{
	/**
	 * Factory method to obtain an empty {@link ListIterator}.
	 * @return an empty {@link ListIterator}
	 */
	@Override
	public ListIterator<E> emptyListIterator()
	{
		ArrayList<E> al = new ArrayList<>();
		return al.listIterator();
	}

	/**
	 * Factory method to obtain an {@link ListIterator} iterating over a single
	 * element.
	 * @param element the single element to iterate over
	 * @return a {@link ListIterator} on a single element
	 */
	@Override
	public ListIterator<E> singleListIterator(E element)
	{
		ArrayList<E> al = new ArrayList<>();
		al.add(element);
		return al.listIterator();
	}

	/**
	 * Factory method to obtain an {@link ListIterator} iterating over multiple
	 * elements.
	 * @param elements the list of elements to iterate over
	 * @return a {@link ListIterator} on multiples elements
	 */
	@Override
	public ListIterator<E> multipleListIterator(Collection<E> elements)
	{
		ArrayList<E> al = new ArrayList<>();
		for (E element : elements)
		{
			al.add(element);
		}
		return al.listIterator();
	}
}
