package expressions;

/**
 * Base abstract class for all expressions
 * @param <E> The type of numbers in this expression
 * @author davidroussel
 */
public abstract class AbstractExpression<E extends Number>
    implements Expression<E>
{
	/**
	 * Concrete reference to parent expression.
	 */
	protected Expression<E> parent;

	/**
	 * Default protected constructor
	 */
	protected AbstractExpression()
	{
		parent = null; // no parent yet.
	}

	/**
	 * Numeric value of this expression
	 * @return the numeric value of this expression
	 * @throws IllegalStateException if a value can't be evaluated right now
	 * @implSpec Default implementation only throws an {@link IllegalStateException}
	 * whenever {@link #hasValue()} is false
	 */
	@Override
	public abstract E value() throws IllegalStateException;

	/**
	 * Indicate if this expression can be evaluated right now to procude a value
	 * @return true if expression can produce a value
	 * and calling {@link #value()} is legal. False otherwise
	 */
	@Override
	public abstract boolean hasValue();

	/**
	 * Accessor to parent expression (if any)
	 * @return a reference to the parent expression of null if there is no
	 * parent expression
	 * @implNote Accessing to parent expression will facilitate iteration on
	 * complex expressions
	 */
	@Override
	public Expression<E> getParent()
	{
		return parent;
	}

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
	 * the exact same other expression. This implementation enforces this policy.
	 * @implSpec for binary expressions, containing an expression means the
	 * same thing as for non terminal expressions but also if the provided expression
	 * can be found on the left or right side
	 */
	@Override
	public boolean contains(Expression<E> expr)
	{
		// DONE 001 Replace with correct implementation
		// Remarques : Optional contient .empty() équivalent de None, .of(...) équivalent de Some ..., o.isPresent() , o.get() équivalents du match o with | Some  _ -> true | _ -> false
		return this == expr;
	}

	/**
	 * Checks if the provided expression has the same type of expression as this
	 * and if both have the same {@link Number}s (only if a value can be estimated)
	 * @param expr the expression to assess
	 * @return true if expr has the same type as this and both use the same
	 * {@link Number}s (iff it can be established with certainty).
	 */
	protected boolean sameTypes(Expression<? extends Number> expr)
	{
		if (expr == null) // can't state
		{
			return false;
		}

		if (getClass() != expr.getClass())
		{
			return false;
		}

		if (hasValue() && expr.hasValue())
		{
			E selfValue = value();
			Number otherValue = expr.value();
			if (selfValue.getClass() != otherValue.getClass())
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Equality to test if:
	 * <ul>
	 * <li>obj is not null</li>
	 * <li>obj is not self</li>
	 * <li>obj is indeeed an {@link Expression}</li>
	 * <li>both expressions have the same type (and Numbers)</li>
	 * <li>both expressions are written the same way (using
	 * {@link #toString()})</li>
	 * </ul>
	 * .
	 * @param obj the object
	 * @return true if provided object is self or is a non null expression with
	 * the same type and number as this expression and both expression are
	 * written (as in {@link #toString()}) the same way
	 * @see #sameTypes(Expression)
	 */
	@Override
	public boolean equals(Object obj)
	{
		// DONE 002 Replace with correct implementation
		if (obj == null)
		{
			return false;
		}
		if (this == obj)
		{
			return true;
		}
		if (obj instanceof Expression<?>)
		{
			Expression<?> expr = (Expression<?>) obj;
			if (!sameTypes(expr))
			{
				return false;
			}
			return toString().equals(expr.toString());
		}
		return false;
	}

	/**
	 * String representation of this expression
	 * @return a String representation of this expression
	 */
	@Override
	public abstract String toString();

	/**
	 * Hashcode for this expression.
	 * Since {@link #equals(Object)} is solely based on {@link #toString()}
	 * comparison, so does this method.
	 * @return the hashcode of this expression
	 */
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
}
