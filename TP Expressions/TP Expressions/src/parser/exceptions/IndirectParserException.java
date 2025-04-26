package parser.exceptions;

/**
 * Exception raised during parsing but due to another kind of exception
 * such as {@link NumberFormatException}
 */
public class IndirectParserException extends ParserException
{
	/**
	 * Serial number for serializable classes
	 */
	private static final long serialVersionUID = -7561626738059859262L;

	/**
	 * Valued constructor with message and cause
	 * @param message the message to set for this exception
	 * @param cause the other exception causing this exception
	 */
	public IndirectParserException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Constructor with external cause only
	 * @param cause the external exception causing this exception
	 */
	public IndirectParserException(Throwable cause)
	{
		super(cause);
	}
}
