package parser.exceptions;

/**
 * Exception to raise when parsing a sub-context starting with a "("
 * with no matching ")" found
 * @author davidroussel
 */
public class UnfinishedSubContextException extends ParserException
{
	/**
	 * Serial number for serializable classes
	 */
	private static final long serialVersionUID = 9196382089910708312L;

	/**
	 * Constructor with faulty expression
	 * @param context the faulty context
	 */
	public UnfinishedSubContextException(String context)
	{
		super("Invalid sub context:\"" + context + "\"");
	}
}
