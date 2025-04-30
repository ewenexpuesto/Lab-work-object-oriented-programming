package parser.exceptions;

/**
 * Exception to raise in {@link parser.ExpressionParser} when an assignment
 * operator is already present in the parsed expression and a new assignment is
 * encountered.
 * e.g. expression = "a = b = c"
 * @author davidroussel
 */
public class MultipleAssignmentsException extends ParserException
{
	/**
	 * Serial number for serializable classes
	 */
	private static final long serialVersionUID = -2327737888418621994L;

	/**
	 * Default Constructor
	 */
	public MultipleAssignmentsException()
	{
		super("Multiple Assignment Encountered");
	}
}
