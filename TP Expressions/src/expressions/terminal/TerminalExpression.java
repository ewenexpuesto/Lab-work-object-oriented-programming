package expressions.terminal;

import java.util.Optional;

import expressions.AbstractExpression;
import expressions.Expression;

/**
 * Base class for all terminal expressions such as constants (5) or variables (a)
 * holding a value.
 * @param <E> The type of numbers in this expression
 * @author davidroussel
 */
public abstract class TerminalExpression<E extends Number> extends AbstractExpression<E>
{
	/**
	 * The value hold by this terminal expression.
	 */
	protected Optional<E> value;


	/**
	 * Default protected constructor
	 * Designed to be used only in subclasses
	 */
	protected TerminalExpression()
	{
		value = Optional.empty();
	}

	/**
	 * Valued constructor with a provided value (which can be null);
	 * @param value the value to provide to this terminal expression
	 * @implSpec If provided value is null then {@link Optional} {@link #value}
	 * will be empty.
	 * @see Optional#of(Object)
	 */
	protected TerminalExpression(E value)
	{
		// DONE 100 Replace with correct implementation
		if (value == null)
			this.value = Optional.empty();
		else
			this.value = Optional.of(value);
	}

	/**
	 * Numeric value of this expression
	 * @return the numeric value of this expression
	 * @throws IllegalStateException if a value can't be evaluated right now
	 * @see Optional#get()
	 */
	@Override
	public E value() throws IllegalStateException
	{
		// DONE 101 Replace with correct implementation
		if (!hasValue())
			throw new IllegalStateException("Expression can't be evaluated right now");
		return value.get();
	}

	/**
	 * Indicate if this expression can be evaluated right now to procude a value
	 * @return true if expression can produce a value
	 * and calling {@link #value()} is legal. False otherwise
	 * @see Optional#isPresent()
	 */
	@Override
	public boolean hasValue()
	{
		// DONE 102 Replace with correct implementation
		return value.isPresent();
	}

	/**
	 * Set new parent to expression.
	 * @param parent The parent to set
	 * @throws IllegalArgumentException if the provided parent is also a
	 * {@link TerminalExpression} since {@link TerminalExpression}s can't have
	 * childrens.
	 */
	@Override
	public void setParent(Expression<E> parent) throws IllegalArgumentException
	{
		// DONE 103 Complete ...
		if (parent instanceof TerminalExpression)
			throw new IllegalArgumentException("Terminal expressions can't have a children");
		this.parent = parent;
	}
}
