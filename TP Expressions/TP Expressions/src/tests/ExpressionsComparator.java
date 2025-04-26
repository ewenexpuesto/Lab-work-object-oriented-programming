package tests;

import expressions.Expression;
import expressions.binary.BinaryExpression;
import expressions.terminal.ConstantExpression;
import expressions.terminal.TerminalExpression;
import expressions.terminal.VariableExpression;

/**
 * Special class to assess if two expressions are composed of the same elements
 * @author davidroussel
 * @param <E> The type of number used in expressions
 */
public class ExpressionsComparator<E extends Number>
{
	/**
	 * Compare two {@link Expression}s
	 * @param <E> the type of number of the expressions
	 * @param e1 the first expression to compare
	 * @param e2 the second expression to compare
	 * @return true if the two expressions are composed of the same elements
	 * with the same values, false otherwise.
	 * @implNote Two null expressions are considered equal.
	 */
	public static <E extends Number> boolean compare(Expression<E> e1,
	                                                 Expression<E> e2)
	{
		if ((e1 == null) && (e2 == null))
		{
			return true;
		}

		if (e1 == e2)
		{
			return true;
		}

		if (compareTypes(e1, e2))
		{
			if ((e1 instanceof BinaryExpression<?>)
			    && (e2 instanceof BinaryExpression<?>))
			{
				return compare((BinaryExpression<E>) e1,
				               (BinaryExpression<E>) e2);
			}
			else if ((e1 instanceof TerminalExpression<?>)
			    && (e2 instanceof TerminalExpression<?>))
			{
				return compare((TerminalExpression<E>) e1,
				               (TerminalExpression<E>) e2);
			}
		}

		return false;
	}

	/**
	 * Compare two {@link Expression}s types
	 * @param <E> the type of number of the expressions
	 * @param e1 the first expression to compare
	 * @param e2 the second expression to compare
	 * @return true if the two expressions are not null and share the same class
	 */
	private static <E extends Number> boolean
	    compareTypes(Expression<E> e1, Expression<E> e2)
	{
		if ((e1 != null) && (e2 != null))
		{
			return e1.getClass() == e2.getClass();
		}

		return false;
	}

	/**
	 * Compares two binary expressions
	 * @param <E> the type of number of the expressions
	 * @param e1 the first expression to compare
	 * @param e2 the second expression to compare
	 * @return true if the two {@link BinaryExpression}s are the same operators
	 * with the same operands with the same values
	 * @pre e1 and e2 are not null
	 * @pre e1 and e2 classes are the same
	 */
	private static <E extends Number> boolean compare(BinaryExpression<E> e1,
	                                                  BinaryExpression<E> e2)
	{
		/*
		 * e1 && e2 classes are already the same so we only have to compare
		 * the operands
		 */
		Expression<E> left1 = e1.getLeft();
		Expression<E> right1 = e1.getRight();
		Expression<E> left2 = e1.getLeft();
		Expression<E> right2 = e1.getRight();

		boolean leftEqual = false;
		if ((left1 == null) && (left2 == null))
		{
			leftEqual = true;
		}
		else
		{
			if (compareTypes(left1, left2))
			{
				if ((left1 instanceof BinaryExpression<?>)
				    && (left2 instanceof BinaryExpression<?>))
				{
					leftEqual = compare((BinaryExpression<E>) left1,
					                    (BinaryExpression<E>) left2);
				}
				else if ((left1 instanceof TerminalExpression<?>)
				    && (left2 instanceof TerminalExpression<?>))
				{
					leftEqual = compare((TerminalExpression<E>) left1,
					                    (TerminalExpression<E>) left2);
				}
				// else leftEqual remains false;
			}
		}

		boolean rightEqual = false;
		if (leftEqual)
		{
			if ((right1 == null) && (right2 == null))
			{
				rightEqual = true;
			}
			else
			{
				if (compareTypes(right1, right2))
				{
					if ((right1 instanceof BinaryExpression<?>)
					    && (right2 instanceof BinaryExpression<?>))
					{
						rightEqual = compare((BinaryExpression<E>) right1,
						                     (BinaryExpression<E>) right2);
					}
					else if ((right1 instanceof TerminalExpression<?>)
					    && (right2 instanceof TerminalExpression<?>))
					{
						rightEqual = compare((TerminalExpression<E>) right1,
						                     (TerminalExpression<E>) right2);
					}
					// else rightEqual remains false;
				}
			}
		}

		return leftEqual && rightEqual;
	}

	/**
	 * Compares two terminal expressions
	 * @param <E> the type of number of the expressions
	 * @param e1 the first expression to compare
	 * @param e2 the second expression to compare
	 * @return true if the two {@link TerminalExpression}s are the same in terms
	 * of values [and names]
	 * @pre e1 and e2 are not null
	 * @pre e1 and e2 classes are the same
	 */
	private static <E extends Number> boolean compare(TerminalExpression<E> e1,
	                                                  TerminalExpression<E> e2)
	{
		if ((e1 instanceof ConstantExpression<?>)
		    && (e2 instanceof ConstantExpression<?>))
		{
			return compare((ConstantExpression<E>) e1,
			               (ConstantExpression<E>) e2);
		}
		if ((e1 instanceof VariableExpression<?>)
		    && (e2 instanceof VariableExpression<?>))
		{
			return compare((VariableExpression<E>) e1,
			               (VariableExpression<E>) e2);
		}

		return false;
	}

	/**
	 * Compares two constant expressions
	 * @param <E> the type of number of the expressions
	 * @param e1 the first expression to compare
	 * @param e2 the second expression to compare
	 * @return true if the two {@link TerminalExpression}s are the same in terms
	 * of values
	 * @pre e1 and e2 are not null
	 */
	private static <E extends Number> boolean compare(ConstantExpression<E> e1,
	                                                  ConstantExpression<E> e2)
	{
		return e1.value().equals(e2.value());
	}

	/**
	 * Compares two variable expressions
	 * @param <E> the type of number of the expressions
	 * @param e1 the first expression to compare
	 * @param e2 the second expression to compare
	 * @return true if the two {@link VariableExpression}s are the same in terms
	 * of names and (if evaluable) values
	 * @pre e1 and e2 are not null
	 */
	private static <E extends Number> boolean compare(VariableExpression<E> e1,
	                                                  VariableExpression<E> e2)
	{
		if (e1.getName().equals(e2.getName()))
		{
			if (e1.hasValue() && e2.hasValue())
			{
				return e1.value().equals(e2.value());
			}

			if (!e1.hasValue() && !e2.hasValue())
			{
				return true;
			}
		}
		return false;
	}
}
