package parser.exceptions;

/**
 * Exception to raise in {@link parser.ExpressionParser} when we need
 * to generate a {@link expressions.terminal.ConstantExpression} to complete an
 * unary operator such as "-2" which should be turned into "0 - 2" and the
 * Number type is not one of {@link Integer}, {@link Float} or {@link Double}.
 * @author davidroussel
 */
public class UnsupportedNumberClassException extends ParserException
{
	/**
	 * Serial number for serializable classes
	 */
	private static final long serialVersionUID = -7857955816040697033L;

	/**
	 * Constructor
	 * @param numberClass the class of the unsupported Number type
	 */
	public UnsupportedNumberClassException(Class<?> numberClass)
	{
		super("Unsupported Number Class : " + numberClass.getSimpleName());
	}
}
