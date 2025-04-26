package expressions;

/**
 * Base interface for arithmetic expressions.
 * Expressions needs to be evaluated, but sometimes they can't be evaluated
 * without further information. So {@link #hasValue()} should be used prior
 * to {@link #value()} otherwise the expression can't be evaluated right
 * now and will throw an {@link IllegalStateException}.
 * @param <E> The type of numbers in this expression
 * @author davidroussel
 */
public interface Expression<E extends Number> extends ValueHolder<E>,
    ParentHolder<Expression<E>>, Container<Expression<E>>
{
	/**
	 * Indicate if this expression can be evaluated right now to procude a value
	 * @return true if expression can produce a value
	 * and calling {@link #value()} is legal. False otherwise
	 */
	@Override
	public abstract boolean hasValue();

	/**
	 * Numeric value of this expression
	 * @return the numeric value of this expression
	 * @throws IllegalStateException if a value can't be evaluated right now
	 */
	@Override
	public abstract E value() throws IllegalStateException;

	/**
	 * Accessor to parent expression (if any)
	 * @return a reference to the parent expression or null if there is no
	 * parent expression
	 * @implNote Accessing to parent expression will facilitate iteration on
	 * complex expressions
	 */
	@Override
	public abstract Expression<E> getParent();

	/**
	 * Set new parent to expression.
	 * @param parent The parent to set
	 * @throws IllegalArgumentException if the provided parent is not a legal
	 * parent.
	 * @implNote Typically, terminal expressions (constants & variables) will
	 * have non terminal expressions as parents. But non terminal expressions
	 * can't have terminal expressions as parents.
	 */
	@Override
	public abstract void setParent(Expression<E> parent) throws IllegalArgumentException;

	/**
	 * Test containment of another expression
	 * @param expr the expression to test
	 * @return true if the provided expression can be found in this expression.
	 * @implSpec for terminal expressions, containing an expression means being
	 * equal to another expression.
	 * @implSpec for non terminal expressions containing an expression really
	 * means that the provided expression might be equal to one of its
	 * sub-expressions.
	 */
	@Override
	public abstract boolean contains(Expression<E> expr);

	/**
	 * Comparison with another object.
	 * This method is abstract because: A default method cannot override a
	 * method from {@link Object}
	 * @param obj the object to compare
	 * @return true if obj is a {@link Expression} which can be evaluated
	 * with the same evaluation as this expression
	 */
	@Override
	public abstract boolean equals(Object obj);

	/**
	 * String representation of this expression
	 * @return a String representation of this expression
	 */
	@Override
	public abstract String toString();

	/**
	 * Hashcode of this expression (for future use in {@link java.util.HashMap})
	 * @return the hashcode of this expression
	 */
	@Override
	public abstract int hashCode();
}
