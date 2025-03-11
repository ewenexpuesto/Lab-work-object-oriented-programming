package tests.utils;

import collections.arrays.Capacity;

/**
 * An internal class to contain an array
 * @param <E> the element type in the array
 */
class ArrayHolder<E> implements Capacity<E>
{
	/**
	 * The array containing the elements
	 */
	private E[] array;

	/**
	 * Constructor from array
	 * @param array the array to use
	 */
	public ArrayHolder(E[] array)
	{
		this.array = array;
	}

	@Override
	public int getCapacity()
	{
		return array.length;
	}

	@Override
	public int getCapacityIncrement()
	{
		return Capacity.DEFAULT_CAPACITY_INCREMENT;
	}

	@Override
	public void grow(int amount) throws IllegalArgumentException
	{
		if (amount < 0)
		{
			throw new IllegalArgumentException("Amount must be positive");
		}
		array = Capacity.resizeArray(array, array.length + amount);
	}

	@Override
	public E[] getArray()
	{
		return array;
	}
}
