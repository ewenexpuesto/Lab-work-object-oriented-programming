package parser.exceptions;

/**
 * Parser exceptions mother class when parsing a context string in
 * {@link parser.ExpressionParser}
 * @author davidroussel
 */
public class ParserException extends Exception
{
	/**
	 * Serial number for serializable classes.
	 * Since {@link Exception} class implements {@link java.io.Serializable}
	 * its subclasses should provide a serial number
	 */
	private static final long serialVersionUID = -1345939766121696323L;

//	/**
//	 * The message for this exception
//	 */
//	protected String message;

	/**
	 * Constructor with message
	 * @param message the message to set for this exception
	 * @implNote protected constructor so only sub-classes can use it
	 */
	protected ParserException(String message)
	{
		super(message);
	}

	/**
	 * Constructor from message and another exception
	 * @param message the message to set for this exception
	 * @param cause the other exception causing this one
	 * @implNote protected constructor so only sub-classes can use it
	 */
	protected ParserException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Constructor from another exception
	 * @param cause the other exception causing this one
	 * @implNote protected constructor so only sub-classes can use it
	 */
	protected ParserException(Throwable cause)
	{
		super(cause);
	}
}
