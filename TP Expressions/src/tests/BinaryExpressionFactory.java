package tests;

import expressions.AbstractExpression;
import expressions.binary.AdditionExpression;
import expressions.binary.AssignmentExpression;
import expressions.binary.BinaryExpression;
import expressions.terminal.VariableExpression;

/**
 * Factory to build {@link BinaryExpression}s
 * @param <E> the type of number in expressions
 */
public class BinaryExpressionFactory<E extends Number>
{
	/**
	 * Create a {@link BinaryExpression} based on
	 * @param <E> the type of numbers used in expressions
	 * @param type the type of binary expression to generate
	 * @return a new {@link BinaryExpression} with no operands
	 * @throws IllegalArgumentException if an argument prevents the expression
	 * from being constructed
	 */
	public static <E extends Number> BinaryExpression<E>
	    get(Class<? extends BinaryExpression<E>> type)
	        throws IllegalArgumentException
	{
		if (type == AssignmentExpression.class)
		{
			return new AssignmentExpression<>();
		}
		if (type == AdditionExpression.class)
		{
			return new AdditionExpression<>();
		}
		// TODO uncomment when SubtractionExpression is ready
//		if (type == SubtractionExpression.class)
//		{
//			return new SubtractionExpression<>();
//		}
		// TODO uncomment when MultiplicationExpression is ready
//		if (type == MultiplicationExpression.class)
//		{
//			return new MultiplicationExpression<>();
//		}
		// TODO uncomment when DivisionExpression is ready
//		if (type == DivisionExpression.class)
//		{
//			return new DivisionExpression<>();
//		}
		// TODO uncomment when PowerExpression is ready
//		if (type == PowerExpression.class)
//		{
//			return new PowerExpression<>();
//		}
		return null;
	}

	/**
	 * Create a {@link BinaryExpression} based on
	 * @param <E> the type of numbers used in expressions
	 * @param type the type of binary expression to generate
	 * @param left the left operand
	 * @param right the right operand
	 * @return a new {@link BinaryExpression} with the provided operands
	 * @throws IllegalArgumentException if an argument prevents the expression
	 * from being constructed
	 */
	public static <E extends Number> BinaryExpression<E>
	    get(Class<? extends BinaryExpression<E>> type,
	    	AbstractExpression<E> left,
	    	AbstractExpression<E> right)
	        throws IllegalArgumentException
	{
		if (type == AssignmentExpression.class)
		{
			if (!(left instanceof VariableExpression<?>))
			{
				throw new IllegalArgumentException("Can't create assignment without left side variable");
			}
			return new AssignmentExpression<>((VariableExpression<E>) left,
			                                  right);
		}
		if (type == AdditionExpression.class)
		{
			return new AdditionExpression<>(left, right);
		}
		// TODO uncomment when SubtractionExpression is ready
//		if (type == SubtractionExpression.class)
//		{
//			return new SubtractionExpression<>(left, right);
//		}
		// TODO uncomment when MultiplicationExpression is ready
//		if (type == MultiplicationExpression.class)
//		{
//			return new MultiplicationExpression<>(left, right);
//		}
		// TODO uncomment when DivisionExpression is ready
//		if (type == DivisionExpression.class)
//		{
//			return new DivisionExpression<>(left, right);
//		}
		// TODO uncomment when PowerExpression is ready
//		if (type == PowerExpression.class)
//		{
//			return new PowerExpression<>(left, right);
//		}
		return null;
	}
}
