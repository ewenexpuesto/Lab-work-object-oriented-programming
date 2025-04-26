package expressions;

/**
 * Common Interface for all classes holding an optional value.
 * Meaning these classes should be able to provide a value with {@link #value()}
 * and the {@link #hasValue()} indicate if a value can be provided right now.
 * @param <E> the type of number to hold
 */
 public interface ValueHolder<E extends Number>
{
	/**
	 * Value of this holder
	 * @return the numeric value of this holder
	 * @throws IllegalStateException if a value can't be provided right now
	 * @implSpec Default implementation only throws an {@link IllegalStateException}
	 * whenever {@link #hasValue()} is false
	 */
	public abstract E value() throws IllegalStateException;

	/**
	 * Indicate if this holder's value can be evaluated right now.
	 * @return true if holder can provide a value
	 * and calling {@link #value()} is legal. False otherwise
	 */
	public abstract boolean hasValue();
}
