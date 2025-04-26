package expressions.terminal;

import expressions.ValueHolder;

/**
 * Interface for all expressions holding a variable.
 * <ul>
 * 	<li>Variables have a name</li>
 * 	<li>Variables have a value</li>
 * 	<li>One can set a value on Variables</li>
 * 	<li>Variables are comparable in term of name but not in terms of values</li>
 * </ul>
 * @param <E> The type of value held by this variable
 */
public interface Variable<E extends Number> extends ValueHolder<E>, Comparable<Variable<E>>
{
	/*
	 * ValueHolder<E> provides
	 * 	- public abstract E value() throws IllegalStateException;
	 * 	- public abstract boolean hasValue();
	 * Comparable<Variable<E>> provides
	 * 	- public abstract int compareTo(Variable<E> v)
	 */

	/**
	 * Name of the variable
	 * @return the name of the variable
	 */
	public abstract String getName();

	/**
	 * Value of this variable
	 * @return the numeric value of this variable
	 * @throws IllegalStateException if a value can't be provided right now
	 * @implSpec Default implementation only throws an {@link IllegalStateException}
	 * whenever {@link #hasValue()} is false
	 */
	@Override
	public abstract E value() throws IllegalStateException;

	/**
	 * Indicate if this variable's value can be evaluated right now.
	 * @return true if variable can provide a value
	 * and calling {@link #value()} is legal. False otherwise
	 */
	@Override
	public abstract boolean hasValue();

	/**
	 * Set new value
	 * @param value the value to set
	 * @throws NullPointerException if we try to set a null value
	 */
	public abstract void setValue(E value) throws NullPointerException;

	/**
	 * Reset current value to "no value"
	 */
	public abstract void clearValue();

	/**
	 * Comparison to another variable.
	 * Compare variable solely in terms of names
	 * @return the comparison to another variable by comparing names only.
	 */
	@Override
	public default int compareTo(Variable<E> v)
	{
		return getName().compareTo(v.getName());
	}
}
