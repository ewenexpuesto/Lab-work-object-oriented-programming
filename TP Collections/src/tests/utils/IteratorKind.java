package tests.utils;

import java.util.ListIterator;

/**
 * Kind of provided {@link java.util.Iterator} or {@link java.util.ListIterator}
 */
public enum IteratorKind
{
	/**
	 * Value used to describe an iterator iterating on nothing
	 */
	EMPTY,
	/**
	 * Value used to describe an iterator iterating on a single element
	 */
	SINGLE,
	/**
	 * Value used to describe and iterator iterating on multiple elements
	 */
	MULTIPLE;

	/**
	 * String representation
	 * @return a new string for this kind of {@link ListIterator}
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		switch (this)
		{
			case EMPTY:
				sb.append("empty");
				break;
			case SINGLE:
				sb.append("single");
				break;
			case MULTIPLE:
				sb.append("multiple");
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: "
				    + this);
		}
		return sb.toString();
	}
}
