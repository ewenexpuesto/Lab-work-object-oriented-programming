package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import expressions.Expression;
import expressions.binary.AdditionExpression;
import expressions.binary.AssignmentExpression;
import expressions.binary.BinaryExpression;
import expressions.terminal.ConstantExpression;
import expressions.terminal.VariableExpression;
import parser.exceptions.IllegalAssignmentException;
import parser.exceptions.IllegalPostParsingStateException;
import parser.exceptions.IndirectParserException;
import parser.exceptions.MissingContextException;
import parser.exceptions.MissingLeftOperandException;
import parser.exceptions.MissingOperatorException;
import parser.exceptions.MissingRightOperandException;
import parser.exceptions.MultipleAssignmentsException;
import parser.exceptions.ParserException;
import parser.exceptions.TrailingLeftOperandException;
import parser.exceptions.UnfinishedSubContextException;
import parser.exceptions.UnknownTokenException;
import parser.exceptions.UnsupportedNumberClassException;
import parser.exceptions.UnsupportedUnaryOperatorException;

/**
 * Parser to parse expressions in a string separated by ";" into a list of
 * {@link Expression}.
 * @param <E> The type of numbers in parsed expressions
 * @version 2.0 This version supports expressions with parenthesis such as
 * "(a + b) * (c + d)"
 * @author davidroussel
 */
public class ExpressionParser<E extends Number>
{
	/**
	 * The current context to interpret.
	 * The current context should be available only when parsing since
	 * only parsing should provide a new context
	 */
	private Context<E> currentContext;

	/**
	 * The stack of contexts to parse multiple contexts (especially when
	 * parenthesis are encountered) because expressions enclosed in "()"
	 * should be interpreted with their own context. Hence the current context
	 * should be pushed in {@link #contexts} while the sub-context whithin "()"
	 * is interpreted. Then the old context can be popped and expressions
	 * resulting from the sub-context parsing pushed to its
	 * {@link Context#getOperandsStack()}
	 */
	private Stack<Context<E>> contexts;

	/**
	 * A Specimen's class in order to parse context string and produce the right
	 * Number sub-classes expressions
	 */
	private Class<? extends Number> numberClass;

	/**
	 * Constructor
	 * @param specimen A specimen to provide the Number class for expressions
	 * @apiNote Caution : the specimen's class must match the type parameter
	 * E of this expression parser, otherwise bad things will happend during
	 * parsing.
	 */
	public ExpressionParser(Number specimen)
	{
		numberClass = specimen.getClass();
		contexts = new Stack<Context<E>>();
	}

	/**
	 * Determine if a character is a digit
	 * @param c the character to examine
	 * @return true if character c is a digit
	 */
	private boolean isDigit(char c)
	{
		boolean result = (c >= '0') && (c <= '9');
		// Adds decimal point for floating points numbers
		if (numberClass != Integer.class)
		{
			result |= (c == '.');
		}

		return result;
	}

	/**
	 * Determine if a character is a letter
	 * @param c the character to examine
	 * @return true if character c is a letter or underscore
	 */
	private boolean isLetter(char c)
	{
		return ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'))
		    || (c == '_');
	}

	/**
	 * Determine if a character is an operator among (=, +, -, *, /, ^)
	 * @param c the character to examine
	 * @return true if character c is an operator
	 */
	private boolean isOperator(char c)
	{
		return (c == '=') || (c == '+') || (c == '-') || (c == '*')
		    || (c == '/') || (c == '^');
	}

	/**
	 * Utility method to parse Number in context string
	 * @param context the context to interpret number from
	 * @return the resulting number (instance of one of the subclasses of
	 * {@link Number} depending on the {@link #numberClass}).
	 */
	@SuppressWarnings("unchecked")
	private E parseNumber(String context)
	{
		if (numberClass == Integer.class)
		{
			return (E) Integer.valueOf(context);
		}
		else if (numberClass == Float.class)
		{
			return (E) Float.valueOf(context);
		}
		else
		{
			return (E) Double.valueOf(context);
		}
	}

	/**
	 * Parse a constant value from tokens and push it to operands stack
	 * @param tokens the char array to parse
	 * @param start the index where we start parsing tokens
	 * @return the index in tokens where we stopped parsing constant - 1
	 * @post The resulting {@link ConstantExpression} has been pushed to
	 * {@link Context#getOperandsStack()} stack.
	 * @throws MissingContextException when {@link #currentContext} is null
	 */
	private int parseConstant(char[] tokens, int start)
		throws MissingContextException
	{
		int i = start;
		StringBuilder sb = new StringBuilder();
		while ((i < tokens.length) && isDigit(tokens[i]))
		{
			sb.append(tokens[i++]);
		}
		if (currentContext != null)
		{
			currentContext.getOperandsStack()
			    .push(new ConstantExpression<E>(parseNumber(sb.toString())));
		}
		else
		{
			throw new MissingContextException();
		}
		return i-1; // -1 because we will increment i in the for loop
	}

	/**
	 * Parse a variable value from tokens
	 * @param tokens the char array to parse
	 * @param start the index where we start parsing tokens
	 * @return the index in tokens where we stopped parsing variable - 1
	 * @post The resulting {@link VariableExpression} has been pushed to
	 * {@link Context#getOperandsStack()} stack.
	 * @throws MissingContextException when {@link #currentContext} is null
	 */
	private int parseVariable(char[] tokens, int start)
		throws MissingContextException
	{
		int i = start;
		StringBuilder sb = new StringBuilder();
		while ((i < tokens.length) && isLetter(tokens[i]))
		{
			sb.append(tokens[i++]);
		}
		if (currentContext != null)
		{
			currentContext.getOperandsStack()
			    .push(new VariableExpression<E>(sb.toString()));
		}
		else
		{
			throw new MissingContextException();
		}
		return i-1; // -1 because we will increment i in the for loop
	}

	/**
	 * Parse a complete sub-context enclosed in parenthesis starting at "start"
	 * using chars in tokens and push the resulting expression into operands
	 * stack.
	 * @param tokens the char array to parse
	 * @param start the index where we start parsing sub-context
	 * @return the index in tokens where we stopped parsing the sub-context
	 * (at the matching closing brace)
	 * @throws ParserException just like {@link #parse(String)} since it is used
	 * to parse the sub-context
	 * @throws UnfinishedSubContextException when no matching ")" has been found
	 * @throws MissingContextException if the current context is null
	 * @post the sub-context resulting expression have been pushed to
	 * {@link Context#getOperandsStack()}
	 * @implNote Nested sub contexts (such as "(a + (b - 36))") must be
	 * considered
	 */
	private int parseSubContext(char[] tokens, int start) throws ParserException
	{
		if (tokens[start] == '(')
		{
			int end = tokens.length - 1;
			/*
			 * Search matching closing brace
			 */
			int braceCount = 0;
			for (int i = start; i < tokens.length; i++)
			{
				if (tokens[i] == '(')
				{
					braceCount++;
				}
				if (tokens[i] == ')')
				{
					braceCount--;
				}
				if (braceCount == 0)
				{
					end = i;
					break;
				}
			}
			/*
			 * Build sub-context between matching braces
			 */
			String subContext = String.copyValueOf(tokens,
			                                       (start  + 1),
			                                       (end - start - 1));
			if (braceCount == 0) // We found a matching brace : subContext is ok
			{
				Expression<E> expression = parseSingleContext(subContext);
				if (currentContext != null)
				{
					currentContext.getOperandsStack().push(expression);
				}
				else
				{
					throw new MissingContextException();
				}
				return end; // right at matching ")"
			}
			else
			{
				throw new UnfinishedSubContextException(subContext);
			}
		}
		return start;
	}

	/**
	 * Pops an operator from operators stack, pops the
	 * two operands from operands stack (right, then left) and set values to
	 * the operator.
	 * Then push back the operator with operands to the operands  stack.
	 * If operators or operands stacks can't be popped as
	 * expected then they should be left in their initial states before raising
	 * an {@link IllegalStateException}.
	 * @throws ParserException when parsing irregularities occur
	 * @throws MissingContextException if {@link #currentContext} is null
	 * @throws MissingOperatorException if we can't pop an operator from
	 * {@link Context#getOperatorsStack()} stack.
	 * @throws MissingRightOperandException if we can't perform the first pop
	 * from {@link Context#getOperandsStack()} stack to get the right operand.
	 * @throws MissingLeftOperandException if we can't perform the second pop
	 * from {@link Context#getOperandsStack()} stack to get the left operand.
	 */
	private void popBinaryOperatorAndPush2Operands() throws ParserException
	{
		if (currentContext == null)
		{
			throw new MissingContextException();
		}
		Stack<BinaryExpression<E>> operators = currentContext.getOperatorsStack();
		Stack<Expression<E>> operands = currentContext.getOperandsStack();
		if (operators.empty())
		{
			// No operators left in Context#getOperators()
			throw new MissingOperatorException();
		}
		BinaryExpression<E> operator = operators.pop();
		if (operands.empty())
		{
			// Revert to initial state
			operators.push(operator);
			throw new MissingRightOperandException();
		}
		Expression<E> oldRight = operator.getRight(); // should be null
		Expression<E> right = operands.pop();
		operator.setRight(right);
		if (operands.empty())
		{
			// Revert to initial state
			operands.push(right);
			operator.setRight(oldRight);
			operators.push(operator);
			throw new MissingLeftOperandException();
		}
		Expression<E> left = operands.pop();
		boolean leftSetSuccessful = false;
		try
		{
			operator.setLeft(left);
			leftSetSuccessful = true;
			if (operator instanceof AssignmentExpression<?>)
			{
				/*
				 * Assignment special case : left should not be part
				 * of right, e.g. a = a ^ 5 otherwise the resulting
				 * expression would be recursive!
				 */
				if (findIn(right, (VariableExpression<E>) left))
				{
					throw new IllegalStateException();
				}
			}
			// Success
			operands.push(operator);
		}
		catch (IllegalStateException | IllegalArgumentException e)
		{
			operands.push(left);
			operands.push(right);
			operators.push(operator);
			if (leftSetSuccessful)
			{
				throw new IllegalAssignmentException(left, right);
			}
			else
			{
				throw new IllegalAssignmentException(left);
			}
		}
	}

	/**
	 * Pops an operator from operators stack, then pop a single
	 * operand from operands stack and sets it as the right sub-expression
	 * of the operator. Then creates a 0 value constant and sets it to the
	 * left side of the operator in order to simulate an unary operator such as
	 * "+2" or "-3" or "-a";
	 * @throws ParserException When parsing irregularities occur
	 * @throws MissingOperatorException if we can't pop an operator from
	 * operators stack
	 * @throws UnsupportedUnaryOperatorException if the operators popped
	 * operator is neither "+" nor "-".
	 * @throws MissingRightOperandException if we can't pop a right operand from
	 * operands stack to populate the unary operator right side.
	 * @throws TrailingLeftOperandException if after popping a right operand
	 * from operands stack there are still operands left in operands stack
	 * @throws UnsupportedNumberClassException if the number class is not one of
	 * {@link Integer}, or {@link Float} or {@link Double} preventing us to
	 * instanciate a 0 constant to turn "-2" into "0 - 2" for instance.
	 */
	@SuppressWarnings("unchecked") // Cause we might have to cast things into ConstantExpression<E>
	private void popUnaryOperatorAndPush2Operands() throws ParserException
	{
		if (currentContext == null)
		{
			throw new MissingContextException();
		}

		Stack<BinaryExpression<E>> operators =
		    currentContext.getOperatorsStack();
		Stack<Expression<E>> operands = currentContext.getOperandsStack();
		if (operators.empty())
		{
			// No operators left in Context#getOperators()
			throw new MissingOperatorException();
		}
		BinaryExpression<E> operator = operators.pop();
		if (!operator.getRules().hasUnary())
		{
			// Operation does not support unary operator
			throw new UnsupportedUnaryOperatorException(operator.toString());
		}
		if (operands.empty())
		{
			// Revert to initial state
			operators.push(operator);
			throw new MissingRightOperandException();
		}
		Expression<E> oldRight = operator.getRight(); // should be null
		Expression<E> right = operands.pop();
		operator.setRight(right);
		if (!operands.empty()) // There is a left operand ==> illegal
		{
			// Revert to initial state
			operands.push(right);
			operator.setRight(oldRight);
			operators.push(operator);
			throw new TrailingLeftOperandException();
		}
		else
		{
			ConstantExpression<E> zero = null;
			// Creates a 0 valued left operand
			if (numberClass == Integer.class)
			{
				zero = (ConstantExpression<E>) ConstantExpression
				    .getConstant(Integer.valueOf(0));
			}
			else if (numberClass == Float.class)
			{
				zero = (ConstantExpression<E>) ConstantExpression
				    .getConstant(Float.valueOf(0));
			}
			else if (numberClass == Double.class)
			{
				zero = (ConstantExpression<E>) ConstantExpression
				    .getConstant(Double.valueOf(0));
			}
			else
			{
				// Revert to initial state
				operands.push(right);
				operator.setRight(oldRight);
				operators.push(operator);
				throw new UnsupportedNumberClassException(numberClass);
			}

			// Success
			operator.setLeft(zero);
			operands.push(operator);
		}
	}

	/**
	 * Pops an operator from operators stack, then pops 2 operands from operands
	 * stack, apply these operands on operator and push the operator on the
	 * operands stack.
	 * If popping 2 operands from operands stack fails by missing a left operand
	 * then try to pop only 1 operand to build a (simulated) unary operator
	 * (such as -a, -2, +b or +3).
	 * @throws ParserException Whenever some part of this operation fails
	 * @see #popBinaryOperatorAndPush2Operands()
	 * @see #popUnaryOperatorAndPush2Operands()
	 */
	private void popOperatorAndPush2Operands() throws ParserException
	{
		try
		{
			popBinaryOperatorAndPush2Operands();
		}
		catch (MissingLeftOperandException e)
		{
			/*
			 * If there is a missing left operand then
			 * we'll try to parse the operator as unary
			 * (such as -a, -2, +b or +3)
			 */
			popUnaryOperatorAndPush2Operands();
		}
		catch (IllegalStateException e)
		{
			throw new IndirectParserException(e);
		}
	}

	/**
	 * Search variable "variable" into expression "expression"
	 * @param <E> the type of Number used in expressions
	 * @param expression the expression to search in
	 * @param variable the variable expression to search
	 * @return true if expression "variable" is part of expression "expression"
	 */
	private static <E extends Number> boolean findIn(Expression<E> expression,
	                                                 VariableExpression<E> variable)
	{
		if (expression == null)
		{
			return false;
		}
		if (variable == null)
		{
			return false;
		}

		if (variable == expression)
		{
			return true;
		}
		else
		{
			if (expression instanceof VariableExpression<?>)
			{
				VariableExpression<E> var = (VariableExpression<E>) expression;
				if (var.getName().equals(variable.getName()))
				{
					return true;
				}
			}
			else
			{
				if (expression instanceof BinaryExpression<?>)
				{
					BinaryExpression<E> binop =
					    (BinaryExpression<E>) expression;
					Expression<E> left = binop.getLeft();
					boolean found = false;
					found = findIn(left, variable);
					if (!found)
					{
						Expression<E> right = binop.getRight();
						found = findIn(right, variable);
					}
					return found;
				}
			}
		}

		return false;
	}

	/**
	 * Clears contexts before throwing an exception.
	 * In order to discard invalid contexts upon Parsing errors
	 */
	private void clearContexts()
	{
		contexts.clear();
		currentContext = null;
	}


	/**
	 * Parse multiple infix expressions from string
	 * (e.g. "100 * b + 12 / 14; b = 13") into a list of
	 * {@link Expression}.
	 * @param context the context string to interpret
	 * @return a list of expressions corresponding to each part o the context
	 * @throws ParserException One of the various sub-classes of
	 * {@link ParserException} if there was a non recoverable parser error.
	 */
	public List<Expression<E>> parse(String context) throws ParserException
	{
		List<Expression<E>> expressionList = new ArrayList<>();
		String[] subContexts = context.split(";");

		for (int s = 0; s < subContexts.length; s++)
		{
			try
			{
				Expression<E> expression = parseSingleContext(subContexts[s]);
				expressionList.add(expression);
			}
			catch (ParserException pe)
			{
				clearContexts();
				throw pe;
			}
		}

		if (!contexts.isEmpty())
		{
			throw new IllegalPostParsingStateException();
		}

		return expressionList;
	}

	/**
	 * Parse an infix expression string (e.g. "100 * b + 12 / 14")
	 * into an {@link Expression}
	 * @param content the context string to interpret
	 * @return a single expression corresponding to the context
	 * @throws ParserException One of the various sub-classes of
	 * {@link ParserException} if there was a non recoverable parser error.
	 * @see #isDigit(char)
	 * @see #isLetter(char)
	 * @see #isOperator(char)
	 * @see #parseConstant(char[], int)
	 * @see #parseVariable(char[], int)
	 * @see #popOperatorAndPush2Operands()
	 */
	private Expression<E> parseSingleContext(String content)
		throws ParserException
	{
		if (currentContext != null)
		{
			contexts.push(currentContext);
		}
		currentContext = new Context<E>(content);
		char[] tokens = currentContext.getTokens();
		Stack<BinaryExpression<E>> operators = currentContext.getOperatorsStack();
		Stack<Expression<E>> operands = currentContext.getOperandsStack();
		Expression<E> expression = null;
		boolean assignmentFound = false;

		for (int i = 0; i < tokens.length; i++)
		{
			// Current token is space ==> skip
			if (tokens[i] == ' ')
			{
				continue;
			}
			// Current token is a number ==> parse constant and push it to operands
			if (isDigit(tokens[i]))
			{
				// TODO 700 parse constant and push it to operands
				// i = parse...
				continue;
			}
			// Current token is a variable name ==> parse variable and push it to operands
			if (isLetter(tokens[i]))
			{
				// TODO 701 parse variable and push it to operands
				// i = parse...
				continue;
			}
			// Current token is an operator ==> parse operator and push it to operators
			if (isOperator(tokens[i]))
			{
				BinaryExpression<E> binop = null;
				switch (tokens[i])
				{
					case '=':
						if (!assignmentFound)
						{
							binop = new AssignmentExpression<E>();
							assignmentFound = true;
						}
						else
						{
							throw new MultipleAssignmentsException();
						}
						break;
					case '+':
						binop = new AdditionExpression<E>();
						break;
					case '-':
					case '–': // Caution there are several -
						// TODO 702 Uncomment when SubtractionExpression is ready
						// binop = new SubtractionExpression<E>();
						break;
					case '*':
						// TODO 703 uncomment when MultiplicationExpression is ready
						// binop = new MultiplicationExpression<E>();
						break;
					case '/':
						// TODO 704 uncomment when DivisionExpression is ready
						// binop = new DivisionExpression<E>();
						break;
					case '^':
						// TODO 705 uncomment when PowerExpression is ready
						// binop = new PowerExpression<E>();
						break;
				}
				// While operators contains same or greater precedence operators.
				// Pop operator top from operators stack and
				// Pop 2 top operands from operands stack and apply them to operator
				// Then push the composed operator to operands stack
				while (!operators.empty() && (operators.peek().getRules()
				    .priority() >= binop.getRules().priority()))
				{
					popOperatorAndPush2Operands();
				}

				// TODO 706 Push newly created binop to operators.
				// ...
				continue;
			}

			// Current token is an opening brace ==> parse sub-context and push it to operands
			if (tokens[i] == '(')
			{
				// TODO 707 parse sub-context and push it to operands
				// i = parse...
				continue;
			}

			// If we reached that point something went wrong
			throw new UnknownTokenException(tokens[i]);
		}

		// Pops all remaining operators from operators stack and
		// push assembled operators to operands stack
		while (!operators.empty())
		{
			popOperatorAndPush2Operands();
		}

		// There should be exactly 1 operand left in operands stack
		// which is the resulting expression
		// If there is still an operator left in operators stack
		// then it means that we have a missing operator
		if (operands.empty())
		{
			throw new MissingRightOperandException();
		}
		if (operands.size() > 1)
		{
			throw new IllegalPostParsingStateException();
		}
		expression = operands.pop();

		if (contexts.isEmpty())
		{
			currentContext = null;
		}
		else
		{
			currentContext = contexts.pop();
		}

		return expression;
	}
}
