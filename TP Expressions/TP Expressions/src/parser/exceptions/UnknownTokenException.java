package parser.exceptions;

/**
 * Exception to raise in {@link parser.ExpressionParser} when an unknown token
 * char is encountered.
 * e.g. expression = "a ! b"
 * @author davidroussel
 */
public class UnknownTokenException extends ParserException
{
	/**
	 * Serial number for serializable classes
	 */
	private static final long serialVersionUID = -6133989039322038764L;

	/**
	 * Valued constructor
	 * @param token the token character encoutered which could not be
	 * interpreted
	 */
	public UnknownTokenException(char token)
	{
		super("Unknown token: " + token);
	}
}
