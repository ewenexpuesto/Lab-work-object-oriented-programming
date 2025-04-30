package parser.exceptions;

/**
 * Exception to raise when processing a binary operator in
 * {@link parser.ExpressionParser} and there is no more operands to pop from the
 * values stack to define the left operand.
 * e.g. expression = "a +"
 * @author davidroussel
 */
public class MissingRightOperandException extends ParserException
{
	/**
	 * Serial number for serializable classes
	 */
	private static final long serialVersionUID = 3477563900198540049L;

	/**
	 * Default constructor
	 */
	public MissingRightOperandException()
	{
		super("Missing Left Operand");
	}
}
