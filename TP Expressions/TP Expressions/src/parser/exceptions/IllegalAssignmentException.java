package parser.exceptions;

import expressions.Expression;
import expressions.binary.AssignmentExpression;
import expressions.terminal.VariableExpression;

/**
 * Exception to raise when processing a binary operator in
 * {@link parser.ExpressionParser} when we try to set the left side of
 * an {@link AssignmentExpression} to something other than a
 * {@link VariableExpression}. e.g. : "a + b + c = d", or when the left side
 * of the assignment is also in the right side e.g. "a = a ^ 5"
 * @author davidroussel
 */
public class IllegalAssignmentException extends ParserException
{
	/**
	 * Serial number for serializable classes
	 */
	private static final long serialVersionUID = 7299521232134758694L;

	/**
	 * Constructor with faulty expression
	 * @param expression the faulty expression
	 */
	public IllegalAssignmentException(Expression<?> expression)
	{
		super("Illegal left side \"" + expression.getClass().getSimpleName()
		    + "\" in assignment \"" + expression.toString() + "\"");
	}

	/**
	 * Constructor with faulty left and right expressions
	 * @param left the left faulty expression
	 * @param right the right faulty expression
	 */
	public IllegalAssignmentException(Expression<?> left,
	                                  Expression<?> right)
	{
		super("Left side \"" + left.toString() + "\" found in right side \""
		    + right.toString() + "\"");
	}
}
