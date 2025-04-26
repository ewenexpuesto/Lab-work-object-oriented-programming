package parser;

import java.util.Stack;

import expressions.Expression;
import expressions.binary.BinaryExpression;

/**
 * A context to interpret by {@link ExpressionParser}
 * @param <E> The type of numbers in this context
 * @author davidroussel
 */
public class Context<E extends Number>
{
	/**
	 * The context String to interpret
	 */
	private String context;

	/**
	 * The operands stack associated with this context
	 */
	private Stack<Expression<E>> operands;

	/**
	 * The operators stack associated with this context
	 */
	private Stack<BinaryExpression<E>> operators;

	/**
	 * Constructor from String
	 * @param context the context to interpret
	 */
	public Context(String context)
	{
		this.context = context;
		operands = new Stack<Expression<E>>();
		operators = new Stack<BinaryExpression<E>>();
	}

//	/**
//	 * Constructor from String and delimiters in this String.
//	 * Creates a sub-context from full context
//	 * @param fullContext the full context to build on
//	 * @param start starting point in the full context
//	 * @param end ending point in the full context
//	 * @post a context has been created with sub string
//	 * fullContext[start ... end]
//	 * @throws IndexOutOfBoundsException if start or end are invalids
//	 */
//	public Context(String fullContext, int start, int end)
//		throws IndexOutOfBoundsException
//	{
//		context = null;
//		boolean reversedIndexes = false;
//		boolean invalidStart = false;
//		boolean invalidEnd = false;
//		if (start < end)
//		{
//
//			if (start >= 0)
//			{
//				if (end < fullContext.length())
//				{
//					context = fullContext.substring(start, end);
//				}
//				else
//				{
//					invalidEnd = true;
//				}
//			}
//			else
//			{
//				invalidStart = true;
//			}
//		}
//		else
//		{
//			reversedIndexes = true;
//		}
//		if (context == null)
//		{
//			if (reversedIndexes)
//			{
//				throw new IndexOutOfBoundsException(start + " not <= " + end);
//			}
//			else if (invalidStart)
//			{
//				throw new IndexOutOfBoundsException("invalid start  = " + start);
//			}
//			else if (invalidEnd)
//			{
//				throw new IndexOutOfBoundsException("invalid end  = " + end);
//			}
//			else
//			{
//				throw new IndexOutOfBoundsException("Unknown reason");
//			}
//		}
//		values = new Stack<AbstractExpression<E>>();
//		operators = new Stack<BinaryExpression<E>>();
//	}

	/**
	 * Context String accessor
	 * @return the context string
	 */
	public String getContext()
	{
		return context;
	}

	/**
	 * Operands Stack accessor
	 * @return the values Stack
	 */
	public Stack<Expression<E>> getOperandsStack()
	{
		return operands;
	}

	/**
	 * Operators Stack accessor
	 * @return the operators Stack
	 */
	public Stack<BinaryExpression<E>> getOperatorsStack()
	{
		return operators;
	}

	/**
	 * Gets the context string as a char array
	 * @return a char array of the context string
	 */
	public char[] getTokens()
	{
		/*
		 * Spaces should not be removed from context otherwise
		 * wrong expressions like "a b" would be translated to "ab" which is
		 * a valid expression
		 */
//		char[] chars = context.toCharArray();
//		int nbSpaces = 0;
//		for (int i = 0; i < chars.length; i++)
//		{
//			if (chars[i] == ' ')
//			{
//				nbSpaces++;
//			}
//		}
//
//		char[] tokens = new char[chars.length - nbSpaces];
//		int j = 0;
//		for (int i = 0; i < chars.length; i++)
//		{
//			if (chars[i] != ' ')
//			{
//				tokens[j++] = chars[i];
//			}
//		}
//
//		return tokens;
		return context.toCharArray();
	}
}
