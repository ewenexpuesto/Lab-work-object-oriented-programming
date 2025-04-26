package parser.exceptions;

/**
 * Exception to raise when processing an unary operator in
 * {@link parser.ExpressionParser} such as -2, -a or +b when a remaining operand
 * subsist in operands stack
 * @author davidroussel
 */
public class TrailingLeftOperandException extends ParserException
{
	/**
	 * Serial number for serializable classes
	 */
	private static final long serialVersionUID = -7578042288503265913L;

	/**
	 * Default constructor
	 */
	public TrailingLeftOperandException()
	{
		super("Trailing Left Operand with UnaryOperator");
	}
}
