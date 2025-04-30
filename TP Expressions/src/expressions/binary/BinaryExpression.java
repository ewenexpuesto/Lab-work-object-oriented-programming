package expressions.binary;

import expressions.AbstractExpression;
import expressions.Expression;
import expressions.terminal.TerminalExpression;

/**
 * Binary expression to evaluate an operator between two expressions (i.e. a + b)
 * A Binary expression is composed of a left side expression, a right side
 * expression, and and operation type {@link BinaryOperatorRules} which can be
 * used to determine operation priority, hashCode and toString for the concrete
 * operation.
 * Since the operator is not specified yet this class is abstract.
 * The subclasses will specify the operation preformed between sides.
 * @param <E> Type of numbers in this expression
 * @implNote Caution: due to the way binary expression will be parsed
 * we need to support left and right null expressions during construction.
 * And be able to set left and right expressions later.
 */
public abstract class BinaryExpression<E extends Number> extends AbstractExpression<E>
{
	/**
	 * Left expression
	 */
	protected Expression<E> left;

	/**
	 * Right expression
	 */
	protected Expression<E> right;

	/**
	 * Binary operator rules applicable to this binary expression
	 * these rules are used to determine the priority of this binary expression
	 * as well as the existence of a unary variant of this operator.
	 * @see BinaryOperatorRules#priority()
	 * @see BinaryOperatorRules#hasUnary()
	 */
	protected final BinaryOperatorRules rules;

	/**
	 * Valued constructor (to be used by subclasses)
	 * @param left left part of this binary expression
	 * @param right right part if this binary expression
	 * @param rules binary expression type
	 */
	protected BinaryExpression(Expression<E> left,
	                           Expression<E> right,
	                           BinaryOperatorRules rules)
	{
		// DONE 400 Complete ...
		if (rules == null) {
			throw new IllegalArgumentException("Operator rules must not be null");
		}
		this.rules = rules;
		this.left = left;
		this.right = right;
	}

	/**
	 * Constructor with only the operation type
	 * @param rules the rules of operation to perform
	 * @implSpec left and right side expressions shall be set to null
	 */
	protected BinaryExpression(BinaryOperatorRules rules)
	{
		this(null, null, rules);
	}

	/**
	 * Left accessor
	 * @return the left expression
	 */
	public Expression<E> getLeft()
	{
		return left;
	}

	/**
	 * Right accessor
	 * @return the right expression
	 */
	public Expression<E> getRight()
	{
		return right;
	}

	/**
	 * Left side expression setter
	 * @param left the left expression to set
	 * @implSpec if existing left expression is non null it should be properly
	 * detached (removing parent) before being replaced.
	 * @implSpec Once left expression has been set it should have this as its
	 * parent.
	 */
	public void setLeft(Expression<E> left)
		// DONE 401 Complete ...
	{
		// Detach existing left expression if it exists
		if (this.left != null)
			this.left.setParent(null);
		
		// Set the new left expression field
		this.left = left;
		
		// If the new left expression is not null, set its parent to this
		if (left != null)
		{
			left.setParent(this);  // Set this as the parent directly
		}
	}

	/**
	 * Right side expression setter
	 * @param right the right expression to set
	 * @implSpec if existing right expression is non null it should be properly
	 * detached (removing parent) before being replaced.
	 * @implSpec Once right expression has been set it should have this as its
	 * parent.
	 */
	public void setRight(Expression<E> right)
		// DONE 402 Complete ...
	{
		// Detach existing right expression if it exists
		if (this.right != null)
			this.right.setParent(null);
		
		// Set the new right expression field
		this.right = right;
		
		// If the new right expression is not null, set its parent to this
		if (right != null)
		{
			right.setParent(this);  // Set this as the parent directly
		}
	}

	/**
	 * Accessor to operator rules
	 * @return the binary expression rules
	 */
	public BinaryOperatorRules getRules()
	{
		return rules;
	}

	/**
	 * Numeric value of this expression
	 * @return the numeric value of this expression as computed by
	 * {@link #operate(Number, Number)}
	 * @throws IllegalStateException if a value can't be evaluated right now
	 * @see #operate(Number, Number)
	 */
	@Override
	public E value() throws IllegalStateException
	{
		// DONE 403 Replace with correct implementation
		if (left == null || right == null) {
			throw new IllegalStateException("Left or right expression is null");
		}
		if (!left.hasValue()) {
			throw new IllegalStateException("Left expression has no value");
		}
		if (!right.hasValue()) {
			throw new IllegalStateException("Right expression has no value");
		}
		E leftValue = left.value();
		E rightValue = right.value();
		return operate(leftValue, rightValue);
	}

	/**
	 * Indicate if both {@link #left} and {@link #right} side have values so
	 * that the operation can produce a value
	 * @return true if expression can produce a value
	 * and calling {@link #value()} is legal. False otherwise
	 */
	@Override
	public boolean hasValue()
	{
		// DONE 404 Replace with correct implementation
		return left != null && right != null && left.hasValue() && right.hasValue();
	}

	/**
	 * Operate the concrete operation performed by this expression on operands
	 * @param value1 first operand's value
	 * @param value2 second operand's value
	 * @return the actual value resulting from this binary expression
	 * or throws an exception if the operation can't be performed
	 * @throws UnsupportedOperationException if the type E of the operands
	 * is not one of {@link Integer}, {@link Float} or {@link Double}
	 * @implSpec it is assumed only value1 is tested to check for either
	 * {@link Integer}, {@link Float} or {@link Double}.
	 */
	protected abstract E operate(E value1, E value2)
		throws UnsupportedOperationException;

	/**
	 * Set new parent to expression.
	 * @param parent The parent to set
	 * @throws IllegalArgumentException if the provided parent is a
	 * {@link TerminalExpression} since {@link TerminalExpression} can't have
	 * children.
	 * @throws IllegalArgumentException if the provided new parent is contained
	 * in this expression (which includes the case where the proposed parent is self)
	 */
	@Override
	public void setParent(Expression<E> parent) throws IllegalArgumentException
	{
		// DONE 405 Complete ...

		// Check if parent is a TerminalExpression
		if (parent != null && parent instanceof TerminalExpression)
			throw new IllegalArgumentException("A TerminalExpression cannot have children");

		// Check if parent is this expression or contained within this expression
		if (parent != null && (parent == this || contains(parent)))
			throw new IllegalArgumentException("Parent cannot be contained in this expression");
		
		// Set the new parent
		this.parent = parent;
		
	}

	/**
	 * Test containment of another expression
	 * @param expr the expression to test
	 * @return true if the provided expression can be found in this expression.
	 * @implSpec for non terminal expressions, containing an expression means the
	 * same thing as for non terminal expressions but also if the provided expression
	 * can be found on the left or right side
	 */
	@Override
	public boolean contains(Expression<E> expr)
	{
		// DONE 406 Replace with correct implementation
		
		// If expr is null, it cannot be contained
			if (expr == null)
			return false;
		
		// Check if this expression is the same as expr
		if (this == expr)
			return true;
		
		// Check if expr is contained in the left subtree
		if (left != null && left.contains(expr))
			return true;
		
		// Check if expr is contained in the right subtree
		if (right != null && right.contains(expr))
			return true;
		
		// If none of the above, expr is not contained in this expression
		return false;
	}

	/**
	 * String representation of this binary expression :
	 * left side expressions string + operation string + right side expression
	 * string
	 * @return a string representation of this binary operation
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		// DONE 407 Complete ...

		sb.append(left);
		sb.append(" ");  // Add a space before the operator
		sb.append(rules);
		sb.append(" ");  // Add a space after the operator
		sb.append(right);
		
		return sb.toString();
	}

	/**
	 * Method to determine if an operand has operators with lower priority
	 * than the current operator
	 * @param operand the operand to test for lower priority operators
	 * @return true if the operand contains operators with lower priority than
	 * the current operator
	 * @see #toString()
	 */
	private boolean hasLowerPriority(Expression<E> operand)
	{
		if (operand instanceof BinaryExpression<?>)
		{
			BinaryExpression<E> binop  = (BinaryExpression<E>) operand;
			return binop.rules.priority() <= rules.priority();
		}
		return false;
	}
}
