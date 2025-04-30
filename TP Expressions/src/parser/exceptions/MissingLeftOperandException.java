package parser.exceptions;

/**
 * Exception to raise when processing a binary operator in
 * {@link parser.ExpressionParser} when there is no more
 * operands to pop from the values stack to define the right operand.
 * e.g. expression = "+ b"
 * @author davidroussel
 */
public class MissingLeftOperandException extends ParserException
{
	/**
	 * Serial number for serializable classes
	 */
	private static final long serialVersionUID = -7250733332092276259L;

	/**
	 * Default constructor
	 */
	public MissingLeftOperandException()
	{
		super("Missing Right Operand");
	}

}
