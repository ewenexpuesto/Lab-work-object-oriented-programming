package parser.exceptions;

/**
 * Exception to raise when processing an Unary operator in
 * {@link parser.ExpressionParser} which is not either a
 * {@link expressions.binary.AdditionExpression} nor a
 * {@link expressions.binary.SubtractionExpression}.
 * e.g. expression = "a ** b"
 * @author davidroussel
 */
public class UnsupportedUnaryOperatorException extends ParserException
{
	/**
	 * Serial number for serializable classes
	 */
	private static final long serialVersionUID = -1401911771470837727L;

	/**
	 * Constructor
	 * @param operatorString the string representing this un supported
	 * unary operator
	 */
	public UnsupportedUnaryOperatorException(String operatorString)
	{
		super("Unsupported Unary operator: " + operatorString);
	}
}
