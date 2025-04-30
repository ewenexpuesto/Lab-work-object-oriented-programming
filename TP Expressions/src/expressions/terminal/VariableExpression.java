package expressions.terminal;

import java.lang.StackWalker.Option;
import java.util.Optional;

/**
 * Variable expression.
 * a Variable expression is (like {@link ConstantExpression}) a numerical
 * value except its value needs to be set first before it can be evaluated.
 * Typically variables are set using the "=" operator : a = 2;
 * @param <E> The type of numbers in this expression
 * @author davidroussel
 */
public class VariableExpression<E extends Number> extends TerminalExpression<E>
    implements Variable<E>
{
	/**
	 * Variable's name.
	 * The name of the variable is used so that when multiple epressions
	 * reference a variable's name, such a variable can be updated.
	 */
	protected String name;

	/**
	 * Default protected constructor.
	 * Designed to be used only in sub-classes.
	 */
	protected VariableExpression()
	{
		this(null, null);
	}

	/**
	 * Valued constructor.
	 * Builds a variable with a name and a value (which can be null)
	 * @param name the name of this variable
	 * @param value the value to provide to this variable (can evt be null)
	 */
	public VariableExpression(String name, E value)
	{
		// DONE 300 Complete ...
		this.name = name;
		if (value == null)
			this.value = Optional.empty();
		else
			this.value = Optional.of(value);
	}

	/**
	 * Constructor with only name and no value
	 * @param name the name of this variable
	 */
	public VariableExpression(String name)
	{
		this(name, null);
	}

	/**
	 * Name accessor for this variable
	 * @return the name of this variable
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * Merge value with another variable or constant
	 * @param term the terminal expression to take value from.
	 * @return true if a value has been set, false otherwise.
	 * @implSpec If the provided expression or doesn't have a value to set then
	 * the result will always be false;
	 */
	public boolean setValue(TerminalExpression<E> term)
	{
		// DONE 301 Replace with correct implementation
		// Only set value if the terminal expression has a value
		if (term != null && term.hasValue()) {
			E termValue = term.value();
			value = Optional.of(termValue);
			return true;
		}
		return false;
	}

	/**
	 * Set value to this variable
	 * @param value the value to set to this variable
	 * @throws NullPointerException if we try to set a null value
	 * @see Optional#of(Object)
	 */
	@Override
	public void setValue(E value) throws NullPointerException
	{
		// DONE 302 Replace with correct implementation
		if (value == null)
			throw new NullPointerException("Variable expression must have a value");
		this.value = Optional.of(value);
	}

	/**
	 * Reset current value to "no value"
	 */
	@Override
	public void clearValue()
	{
		value = Optional.empty();
	}

	/**
	 * String representation of this VariableExpression
	 * @return return the "name" of this variable
	 */
	@Override
	public String toString()
	{
		// DONE 303 Replace with correct implementation
		return name;
	}
}
