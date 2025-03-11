package tests.utils;

import java.util.Collection;
import java.util.Iterator;

import collections.arrays.ArrayIterator;
import collections.arrays.Capacity;

/**
 * A Factory to obtain {@link ArrayIterator}s
 * @param <E> The type of elements to iterate over
 */
public class ArrayIteratorFactory<E> implements IteratorFactory<E>
{
	/**
	 * Factory method to obtain an empty {@link Iterator}.
	 * @return an empty {@link Iterator}
	 */
	@Override
	public Iterator<E> emptyIterator()
	{
		@SuppressWarnings("unchecked")
		E[] array = (E[]) new Object[0];
		return new ArrayIterator<>(new ArrayHolder<>(array));
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
		@SuppressWarnings("unchecked")
		E[] array = (E[]) new Object[Capacity.DEFAULT_CAPACITY];
		array[0] = element;
		return new ArrayIterator<>(new ArrayHolder<E>(array));
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
		int size = elements.size();
		int capacity = Capacity.DEFAULT_CAPACITY;
		while (capacity < size)
		{
			capacity += Capacity.DEFAULT_CAPACITY;
		}

		@SuppressWarnings("unchecked")
		E[] array = (E[]) new Object[capacity];
		int i = 0;
		Iterator<E> it = elements.iterator();
		while (it.hasNext())
		{
			array[i++] = it.next();
		}
		return new ArrayIterator<>(new ArrayHolder<E>(array));
	}
}
