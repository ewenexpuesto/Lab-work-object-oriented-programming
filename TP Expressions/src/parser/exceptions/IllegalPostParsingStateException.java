package parser.exceptions;

import parser.Context;

/**
 * Exception to raise when at the end of parsing a single expression from
 * context the {@link Context#getOperandsStack()} stack has more than one expression
 * in it. Since the operand stack is supposed to be empty aty the end of parsing.
 * @author davidroussel
 */
public class IllegalPostParsingStateException extends ParserException
{
	/**
	 * Serial number for serializable classes
	 */
	private static final long serialVersionUID = -5095891107495325414L;

	/**
	 * Default constructor
	 */
	public IllegalPostParsingStateException()
	{
		super("Multiple expressions in operands stack");
	}
}
