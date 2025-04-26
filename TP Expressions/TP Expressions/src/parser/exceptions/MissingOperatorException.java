package parser.exceptions;

/**
 * Exception to raise when processing an operator in {@link parser.ExpressionParser}
 * when there is no more operators to pop from the operators stack
 * e.g. expression = "a b"
 * @author davidroussel
 */
public class MissingOperatorException extends ParserException
{
	/**
	 * Serial number for serializable classes
	 */
	private static final long serialVersionUID = 5516047643404763514L;

	/**
	 * Default constructor
	 */
	public MissingOperatorException()
	{
		super("Missing Operator");
	}
}
