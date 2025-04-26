package parser.exceptions;

import parser.Context;

/**
 * Exception to raise when {@link Context} is null while parsing
 * @author davidroussel
 */
public class MissingContextException extends ParserException
{
	/**
	 * Serial number for serializable classes
	 */
	private static final long serialVersionUID = 8790671877974437234L;

	/**
	 * Default constructor
	 */
	public MissingContextException()
	{
		super("Null Context");
	}
}
