package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import expressions.Expression;
import expressions.ValueHolder;
import expressions.binary.AdditionExpression;
import expressions.binary.AssignmentExpression;
import expressions.binary.BinaryExpression;
import expressions.terminal.ConstantExpression;
import expressions.terminal.Variable;
import expressions.terminal.VariableExpression;

/**
 * Test class for all {@link Expression}
 * @author davidroussel
 */
@DisplayName("Expression<E>")
@TestMethodOrder(OrderAnnotation.class)
public class ExpressionTest
{
	/**
	 * Les différentes natures d'expressions à tester
	 */
	@SuppressWarnings("unchecked")
	private static final Class<? extends Expression<?>>[] expressionClasses =
	(Class<? extends Expression<?>>[]) new Class<?>[]
	{
		ConstantExpression.class,
		VariableExpression.class,
		AssignmentExpression.class,
		AdditionExpression.class,
		// TODO uncomment when SubtractionExpression is ready
//		SubtractionExpression.class,
		// TODO uncomment when MultiplicationExpression is ready
//		MultiplicationExpression.class,
		// TODO uncomment when DivisionExpression is ready
//		DivisionExpression.class,
		// TODO uncomment when PowerExpression is ready
//		PowerExpression.class
	};

	/**
	 * Index d'un type d'expression particulier dans les différents tableaux:
	 * @param type le type d'expression à tester
	 * @return l'index du type d'expression dans les différents tableaux
	 * sus-mentionnés ou bien -1 si ce type d'expression ne fait pas partie
	 * des tableaux
	 */
	private static int indexOf(Class<? extends Expression<?>> type)
	{
		if (type == ConstantExpression.class)
		{
			return 0;
		}
		else if (type == VariableExpression.class)
		{
			return 1;
		}
		else if (type == AssignmentExpression.class)
		{
			return 2;
		}
		else if (type == AdditionExpression.class)
		{
			return 3;
		}
		// TODO uncomment when SubtractionExpression is ready
//		else if (type == SubtractionExpression.class)
//		{
//			return 4;
//		}
		// TODO uncomment when MultiplicationExpression is ready
//		else if (type == MultiplicationExpression.class)
//		{
//			return 5;
//		}
		// TODO uncomment when DivisionExpression is ready
//		else if (type == DivisionExpression.class)
//		{
//			return 6;
//		}
		// TODO uncomment when PowerExpression is ready
//		else if (type == PowerExpression.class)
//		{
//			return 7;
//		}
		else
		{
			return -1;
		}
	}

	/**
	 * Expression under test
	 */
	private Expression<Integer> testExpression = null;

	/**
	 * Expression under test class
	 */
	private Class<? extends Expression<Integer>> testExpressionClass = null;

	/**
	 * the name of the expression under test
	 */
	private String testExpressionName = null;

	/**
	 * Array of all possible expressions
	 */
	@SuppressWarnings("unchecked")
	private static final Expression<Integer>[] expressions =
		(Expression<Integer>[])
		new Expression<?>[Math.max(expressionClasses.length, 8)];

	/**
	 * A Map to get an {@link Expression} from its class.
	 * Built using {@link #expressionClasses} and {@link #expressions}
	 */
	private static Map<Class<? extends Expression<?>>,
		Expression<?>> expressionsMap =
		new ConcurrentHashMap<Class<? extends Expression<?>>, Expression<?>>();

	/**
	 * An other Map to get an {@link Expression} from its class.
	 * altExpressionsMap contains expressions equals to {@link #expressionsMap}
	 * Built using {@link #expressionClasses} and {@link #expressions}
	 */
	private static Map<Class<? extends Expression<?>>,
		Expression<?>> altExpressionsMap =
		new ConcurrentHashMap<Class<? extends Expression<?>>, Expression<?>>();

	/**
	 * An other other Map to get an {@link Expression} from its class.
	 * otherExpressionsMap contains expressions not equals to {@link #expressionsMap}
	 * Built using {@link #expressionClasses} and {@link #expressions}
	 */
	private static Map<Class<? extends Expression<?>>,
		Expression<?>> otherExpressionsMap =
		new ConcurrentHashMap<Class<? extends Expression<?>>, Expression<?>>();

	/**
	 * expected toString for different expressions
	 */
	private static String[] toStrings = new String[] {
		"2",		// ConstantExpression
		"a",		// VariableExpression
		"a = 2",	// AssignmentExpression
		"2 + 3",	// AdditionExpression
		"2 - 3",	// SubtractionExpression
		"2 * 3",	// MultiplicationExpression
		"2 / 3",	// DivisionExpression
		"2 ^ 3",	// PowerExpression
	};

	/**
	 * Expected evaluations for different expressions
	 */
	private static Integer[] expectedEvaluations = new Integer [] {
		2,	// ConstantExpression we set operands[0]
		2,	// VariableExpression we set 2 in the test
		2,	// AssignmentExpression we assign operands[0]
		5,	// AdditionExpression
		-1,	// SubtractionExpression
		6,	// MultiplicationExpression
		0,	// DivisionExpression
		8,	// PowerExpression
	};

	/**
	 * A constant value used in expressions
	 */
	private static ConstantExpression<Integer> c21 = null;

	/**
	 * Another constant value used in expressions
	 */
	private static ConstantExpression<Integer> c22 = null;

	/**
	 * A Variable with no value used in expressions
	 */
	private static VariableExpression<Integer> a0 = null;

	/**
	 * A Variable with a value used in expressions
	 */
	private static VariableExpression<Integer> a2 = null;

	/**
	 * A constant that will never be used in any expression
	 */
	private static ConstantExpression<Integer> c0 = null;

	/**
	 * Operands for {@link BinaryExpression}s
	 */
	@SuppressWarnings("unchecked")
	private static Expression<Integer>[] operands =
	    (Expression<Integer>[]) new Expression<?>[] {
	        new ConstantExpression<Integer>(2),
	        new ConstantExpression<Integer>(3),
	        new VariableExpression<Integer>("c", 2),
	        new VariableExpression<Integer>("b")
	};

	/**
	 * Map relating expressions to their expected String representation
	 */
	private static Map<Class<? extends Expression<?>>, String> toStringsMap =
		new HashMap<Class<? extends Expression<?>>, String>();

	/**
	 * Expression class provider used for each parameterized test
	 * @return a stream of expression classes to use in each @ParameterizedTest
	 */
	private static Stream<Class<? extends Expression<?>>> expressionClassesProvider()
	{
		return Stream.of(expressionClasses);
	}

	/**
	 * Expression provider used in each Parameterized test
	 * @return a stream of {@link Expression} to use in each @ParameterizedTest
	 */
	private static Stream<Expression<?>> expressionProvider()
	{
		return expressionsMap.values().stream();
	}

	/**
	 * Copy an expression so that the two expressions can be compared
	 * @param <E> Type of numbers in expression
	 * @param expression the expression to copy
	 * @return a distrinct copy of the provided expression
	 */
	private static <E extends Number> Expression<E> copyExpression(Expression<E> expression)
	{
		if (expression instanceof ConstantExpression<?>)
		{
			return new ConstantExpression<>(((ConstantExpression<E>)expression).value());
		}

		if (expression instanceof VariableExpression<?>)
		{
			VariableExpression<E> exp = (VariableExpression<E>) expression;
			if (exp.hasValue())
			{
				return new VariableExpression<>(exp.getName(), exp.value());
			}
			else
			{
				return new VariableExpression<>(exp.getName());
			}

		}

		if (expression instanceof BinaryExpression<?>)
		{
			BinaryExpression<E> binop = (BinaryExpression<E>) expression;
			Expression<E> left = binop.getLeft();
			Expression<E> right = binop.getRight();

			if (expression instanceof AssignmentExpression<?>)
			{
				if ((left != null) || (right != null))
				{
					return new AssignmentExpression<E>((VariableExpression<E>)left, right);
				}
				else
				{
					return new AssignmentExpression<>();
				}
			}

			if (expression instanceof AdditionExpression<?>)
			{
				if ((left != null) || (right != null))
				{
					return new AdditionExpression<E>(left, right);
				}
				else
				{
					return new AdditionExpression<>();
				}
			}

			// TODO uncomment when SubtractionExpression is ready
//			if (expression instanceof SubtractionExpression<?>)
//			{
//				if ((left != null) || (right != null))
//				{
//					return new SubtractionExpression<E>(left, right);
//				}
//				else
//				{
//					return new SubtractionExpression<>();
//				}
//			}

			// TODO uncomment when MultiplicationExpression is ready
//			if (expression instanceof MultiplicationExpression<?>)
//			{
//				if ((left != null) || (right != null))
//				{
//					return new MultiplicationExpression<E>(left, right);
//				}
//				else
//				{
//					return new MultiplicationExpression<>();
//				}
//			}

			// TODO uncomment when DivisionExpression is ready
//			if (expression instanceof DivisionExpression<?>)
//			{
//				if ((left != null) || (right != null))
//				{
//					return new DivisionExpression<E>(left, right);
//				}
//				else
//				{
//					return new DivisionExpression<>();
//				}
//			}

			// TODO uncomment when PowerExpression is ready
//			if (expression instanceof PowerExpression<?>)
//			{
//				if ((left != null) || (right != null))
//				{
//					return new PowerExpression<E>(left, right);
//				}
//				else
//				{
//					return new PowerExpression<>();
//				}
//			}
		}

		return null;
	}

	/**
	 * Clear values recursively in an expression
	 * @param <E> the type of numbers in expressions
	 * @param expression the expression to process
	 * @return true if at least one value has been cleared in provided expression
	 */
	private static <E extends Number> boolean clearValues(Expression<E> expression)
	{
		boolean result = false;
		if (expression == null)
		{
			return false;
		}

		if (expression instanceof ConstantExpression<?>)
		{
			return false;
		}

		if (expression instanceof BinaryExpression<?>)
		{
			BinaryExpression<E> binop = (BinaryExpression<E>) expression;
			return clearValues(binop.getLeft()) || clearValues(binop.getRight());
		}

		if (expression instanceof Variable<?>)
		{
			@SuppressWarnings("unchecked")
			Variable<E> var = (Variable<E>) expression;
			var.clearValue();
			return true;
		}

		return result;
	}

	/**
	 * Fills {@link #expressions} and {@link #expressionsMap}
	 */
	private static void fillExpressions()
	{
		c21 = new ConstantExpression<Integer>(2);
		c22 = new ConstantExpression<Integer>(2);
		a0 = new VariableExpression<>("a");
		a2 = new VariableExpression<>("a", 2);
		c0 = new ConstantExpression<>(0);

		// Fill maps from arrays
		for (int i = 0; i < expressionClasses.length; i++)
		{
			Class<? extends Expression<?>> currentExpressionClass =
			    expressionClasses[i];
			int index = indexOf(currentExpressionClass);
			if (index == -1)
			{
				fail("Unkown index for " + currentExpressionClass.getSimpleName());
			}
			if (currentExpressionClass == ConstantExpression.class)
			{
				expressions[index] = c21;
			}
			else if (currentExpressionClass == VariableExpression.class)
			{
				expressions[index] = a2;
			}
			else if (currentExpressionClass == AssignmentExpression.class)
			{
				expressions[index] = new AssignmentExpression<Integer>(a0, c22);

			}
			else if (currentExpressionClass == AdditionExpression.class)
			{
				expressions[index] = new AdditionExpression<>(
					operands[0],
					operands[1]);
			}
			// TODO uncomment when SubtractionExpression is ready
//			else if (currentExpressionClass == SubtractionExpression.class)
//			{
//				expressions[index] = new SubtractionExpression<>(
//					operands[0],
//					operands[1]);
//			}
			// TODO uncomment when MultiplicationExpression is ready
//			else if (currentExpressionClass == MultiplicationExpression.class)
//			{
//				expressions[index] = new MultiplicationExpression<>(
//					operands[0],
//					operands[1]);
//			}
			// TODO uncomment when DivisionExpression is ready
//			else if (currentExpressionClass == DivisionExpression.class)
//			{
//				expressions[index] = new DivisionExpression<Integer>(
//					operands[0],
//					operands[1]);
//			}
			// TODO uncomment when PowerExpression is ready
//			else if (currentExpressionClass == PowerExpression.class)
//			{
//				expressions[index] = new PowerExpression<>(
//					operands[0],
//					operands[1]);
//			}
			else
			{
				index = 0; // safe but useless
				fail("Unknown expression type : " + currentExpressionClass.getSimpleName());
			}

			expressionsMap.put(currentExpressionClass, expressions[index]);
			toStringsMap.put(currentExpressionClass, toStrings[index]);
		}
	}

	/**
	 * Fills {@link #expressions} and {@link #expressionsMap}
	 */
	private static void fillAltExpressions()
	{
		// Fill maps from arrays
		for (int i = 0; i < expressionClasses.length; i++)
		{
			Class<? extends Expression<?>> currentExpressionClass =
			    expressionClasses[i];
			if (currentExpressionClass == ConstantExpression.class)
			{
				altExpressionsMap.put(currentExpressionClass,
				                      new ConstantExpression<Integer>(2));
				otherExpressionsMap.put(currentExpressionClass,
				                        new ConstantExpression<Integer>(3));
			}
			else if (currentExpressionClass == VariableExpression.class)
			{
				altExpressionsMap.put(currentExpressionClass,
				                      new VariableExpression<Integer>("a", 2));
				otherExpressionsMap.put(currentExpressionClass,
				                        new VariableExpression<Integer>("b", 2));
			}
			else if (currentExpressionClass == AssignmentExpression.class)
			{
				altExpressionsMap.put(currentExpressionClass,
				                      new AssignmentExpression<Integer>(
				                    	  new VariableExpression<Integer>("a"),
				                    	  new ConstantExpression<Integer>(2)));
				otherExpressionsMap.put(currentExpressionClass,
				                        new AssignmentExpression<Integer>(
				                        	new VariableExpression<Integer>("b"),
				                        	new ConstantExpression<Integer>(2)));
			}
			else if (currentExpressionClass == AdditionExpression.class)
			{
				altExpressionsMap.put(currentExpressionClass,
				                      new AdditionExpression<Integer>(
				                    	  operands[0],
				                    	  operands[1]));
				otherExpressionsMap.put(currentExpressionClass,
				                        new AdditionExpression<Integer>(
				                        	operands[2],
				                        	operands[3]));
			}
			// TODO uncomment when SubtractionExpression is ready
//			else if (currentExpressionClass == SubtractionExpression.class)
//			{
//				altExpressionsMap.put(currentExpressionClass,
//				                      new SubtractionExpression<Integer>(
//				                    	  operands[0],
//				                    	  operands[1]));
//				otherExpressionsMap.put(currentExpressionClass,
//				                        new SubtractionExpression<Integer>(
//				                        	operands[2],
//				                        	operands[3]));
//			}
			// TODO uncomment when MultiplicationExpression is ready
//			else if (currentExpressionClass == MultiplicationExpression.class)
//			{
//				altExpressionsMap.put(currentExpressionClass,
//				                      new MultiplicationExpression<Integer>(
//				                    	  operands[0],
//				                    	  operands[1]));
//				otherExpressionsMap.put(currentExpressionClass,
//				                        new MultiplicationExpression<Integer>(
//				                        	operands[2],
//				                        	operands[3]));
//			}
			// TODO uncomment when DivisionExpression is ready
//			else if (currentExpressionClass == DivisionExpression.class)
//			{
//				altExpressionsMap.put(currentExpressionClass,
//				                      new DivisionExpression<Integer>(
//				                    	  operands[0],
//				                    	  operands[1]));
//				otherExpressionsMap.put(currentExpressionClass,
//				                        new DivisionExpression<Integer>(
//				                        	operands[2],
//				                        	operands[3]));
//			}
			// TODO uncomment when PowerExpression is ready
//			else if (currentExpressionClass == PowerExpression.class)
//			{
//				altExpressionsMap.put(currentExpressionClass,
//				                      new PowerExpression<Integer>(
//				                    	  operands[0],
//				                    	  operands[1]));
//				otherExpressionsMap.put(currentExpressionClass,
//				                        new PowerExpression<Integer>(
//				                        	operands[2],
//				                        	operands[3]));
//			}
			else
			{
				fail("Unknown expression type : " + currentExpressionClass.getSimpleName());
			}
		}
	}

	/**
	 * Setup before all tests
	 * @throws java.lang.Exception if setup fails
	 */
	@BeforeAll
	private static void setUpBeforeClass() throws Exception
	{
		fillExpressions();
		fillAltExpressions();
	}

	/**
	 * Tear down after all tests
	 * @throws java.lang.Exception if tear down fails
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception
	{
		expressionsMap.clear();
		toStringsMap.clear();
	}

	/**
	 * Setup variables for a specific test
	 * @param expression the expression to test
	 */
	@SuppressWarnings("unchecked")
	void setupTest(Expression<Integer> expression)
	{
		testExpression = expression;
		testExpressionClass = (Class<? extends Expression<Integer>>)
			expression.getClass();
		testExpressionName = testExpressionClass.getSimpleName();
	}

	/**
	 * Setup before each test
	 * @throws java.lang.Exception if setup fails
	 */
	@BeforeEach
	void setUp() throws Exception
	{
	}

	/**
	 * Teardown after each test
	 * @throws java.lang.Exception if teardown fails
	 */
	@AfterEach
	void tearDown() throws Exception
	{
		testExpression = null;
		testExpressionClass = null;
		testExpressionName = null;

		/*
		 * Reset the expressionsMap and expressions because instances
		 * might have been modified
		 */
		expressionsMap.clear();
		toStringsMap.clear();
		// Fill maps from arrays
		fillExpressions();
	}

	/**
	 * Test default constructor on each expression (except
	 * {@link ConstantExpression} and {@link VariableExpression} which
	 * don't have default constructors.
	 * @param type the type of expression to test
	 */
	@ParameterizedTest
	@MethodSource("expressionClassesProvider")
	@DisplayName("Default Constructors")
	@Order(1)
	final void testExpressionConstructor(Class<? extends Expression<Integer>> type)
	{
		String testName = new String(type.getSimpleName());

		Constructor<? extends Expression<Integer>> defaultConstructor = null;
		Class<?>[] constructorsArgs = null;
		if ((type != ConstantExpression.class) &&
			(type != VariableExpression.class))
		{
			// All non terminal expression should have a default constructor
			constructorsArgs = new Class<?>[0];
		}
		else
		{
			// All terminal expressions should have a valued constructor
			constructorsArgs = new Class<?>[1];
			if (type == ConstantExpression.class)
			{
				constructorsArgs[0] = Number.class;
//				System.out.println(testName + "(Number value)");
			}
			else if (type == VariableExpression.class)
			{
				constructorsArgs[0] = String.class;
//				System.out.println(testName + "(String name)");
			}
		}

		try
		{
			defaultConstructor = type.getConstructor(constructorsArgs);
		}
		catch (SecurityException e)
		{
			fail(testName +  " constructor security exception");
		}
		catch (NoSuchMethodException e)
		{
			fail(testName +  " constructor not found");
		}

		if (defaultConstructor != null)
		{
			Object instance = null;
			try
			{
				Object[] args = null;
				if ((type != ConstantExpression.class) &&
					(type != VariableExpression.class))
				{
					args = new Object[0];
				}
				else
				{
					args = new Object[1];
					if (type == ConstantExpression.class)
					{
						/*
						 * ConstantExpression class have a constructor
						 * with Number argument for the value
						 */
						args[0] = Integer.valueOf(2);
					}
					else if (type == VariableExpression.class)
					{
						/*
						 * VariableExpression class have constructor
						 * with String argument for the variable name
						 */
						args[0] = new String("a");
					}
				}

				instance = defaultConstructor.newInstance(args);
			}
			catch (IllegalArgumentException e)
			{
				fail(testName + " Illegal constructor arguments");
			}
			catch (InstantiationException e)
			{
				fail(testName + " instanciation exception");
			}
			catch (IllegalAccessException e)
			{
				fail(testName + " illegal access");
			}
			catch (InvocationTargetException e)
			{
				fail(testName + " invocation target exception");
			}
			assertNotNull(instance, testName);
//			assertEquals(instance, instance, testName + " self equality");
		}
	}

	/**
	 * Test method for {@link expressions.Expression#hasValue()}.
	 * @param expression the expression to test provided by
	 * {@link #expressionProvider()}
	 * @param info Test Infos
	 */
	@ParameterizedTest(name = "{0}.hasValue()")
	@MethodSource("expressionProvider")
	@DisplayName("hasValue()")
	@Order(2)
	final void testHasValue(Expression<Integer> expression, TestInfo info)
	{
		setupTest(expression);
		String testName = new String(testExpressionName + "." + info.getDisplayName());
		System.out.println(testName);

		assertTrue(testExpression.hasValue(),
		           testName + "unexpected hasValue status");


		// renders testExpression unevaluable (except for ConstantExpressions)
		boolean cleared = clearValues(testExpression);
		if (testExpression instanceof BinaryExpression<?>)
		{
			BinaryExpression<Integer> binop = (BinaryExpression<Integer>) testExpression;
			Expression<Integer> left = binop.getLeft();
			if ((left instanceof ValueHolder<?>) && !(left instanceof Variable<?>))
			{
				binop.setLeft(null);
			}
			else
			{
				Expression<Integer> right = binop.getRight();
				if (right instanceof ValueHolder<?>)
				{
					binop.setRight(null);
				}
			}
			cleared = true;
		}

		assertEquals(!cleared,
		             testExpression.hasValue(),
		             testName + " unexpected hasValue status");
	}

	/**
	 * Test method for {@link expressions.Expression#value()}.
	 * @param expression the expression to test provided by
	 * {@link #expressionProvider()}
	 * @param info Test infos
	 */
	@ParameterizedTest
	@MethodSource("expressionProvider")
	@DisplayName("value()")
	@Order(3)
	final void testValue(Expression<Integer> expression, TestInfo info)
	{
		setupTest(expression);
		String testName = new String(testExpressionName + "." + info.getDisplayName());
		System.out.println(testName);

		try
		{
			assertEquals(expectedEvaluations[indexOf(testExpressionClass)],
			             testExpression.value(),
			             testName + " failed with wrong expression evaluation");
		}
		catch (IllegalStateException e)
		{
			fail(testName + " unexpected value() failure: "
			    + e.getLocalizedMessage());
		}

		// renders testExpression unevaluable (except for ConstantExpressions)
		boolean cleared = clearValues(testExpression);
		if (testExpression instanceof BinaryExpression<?>)
		{
			BinaryExpression<Integer> binop = (BinaryExpression<Integer>) testExpression;
			Expression<Integer> left = binop.getLeft();
			if ((left instanceof ValueHolder<?>) && !(left instanceof Variable<?>))
			{
				binop.setLeft(null);
			}
			else
			{
				Expression<Integer> right = binop.getRight();
				if (right instanceof ValueHolder<?>)
				{
					binop.setRight(null);
				}
			}
			cleared = true;
		}

		if (cleared)
		{
			assertThrows(IllegalStateException.class, () -> {
				testExpression.value();
			}, testName + " unevaluable expression unexpected value()");
		}
		else
		{
			try
			{
				testExpression.value();
			}
			catch (IllegalStateException e)
			{
				fail(testName + " unexpected value() failure: "
				    + e.getLocalizedMessage());
			}
		}
	}

	/**
	 * Test method for {@link expressions.Expression#getParent()}.
	 * @param expression the expression to test provided by
	 * {@link #expressionProvider()}
	 * @param info Test infos
	 */
	@ParameterizedTest
	@MethodSource("expressionProvider")
	@DisplayName("getParent()")
	@Order(4)
	final void testGetParent(Expression<Integer> expression, TestInfo info)
	{
		setupTest(expression);
		String testName = new String(testExpressionName + "." + info.getDisplayName());
		System.out.println(testName);

		assertNotNull(expression, testName + " unexpected null expression");
		assertNull(expression.getParent(),
		           testName + " unexpected non null parent");
		BinaryExpression<Integer> validParent = new AdditionExpression<>();
		try
		{
			validParent.setLeft(expression);
			assertNotNull(expression.getParent(),
			              testName + " unexpected null parent");
			assertSame(validParent,
			           expression.getParent(),
			           testName + " unexpected parent");
		}
		catch (IllegalArgumentException e)
		{
			if (!(expression instanceof AssignmentExpression<Integer>))
			{
				fail(testName + " unexpected setLEft failure");
			}
		}
	}

	/**
	 * Test method for {@link expressions.Expression#setParent(Expression)}.
	 * @param expression the expression to test provided by
	 * {@link #expressionProvider()}
	 * @param info Test infos
	 */
	@ParameterizedTest
	@MethodSource("expressionProvider")
	@DisplayName("setParent(Expression)")
	@Order(5)
	final void testSetParent(Expression<Integer> expression, TestInfo info)
	{
		setupTest(expression);
		String testName = new String(testExpressionName + "." + info.getDisplayName());
		System.out.println(testName);

		assertNotNull(expression, testName + " unexpected null expression");
		assertNull(expression.getParent(),
		           testName + " unexpected non null parent");
		/*
		 * BinaryExpression are valid parents
		 */
		BinaryExpression<Integer> validParent = new AdditionExpression<>();
		try
		{
			validParent.setLeft(expression); // calls setParent
			assertNotNull(expression.getParent(),
			              testName + " unexpected null parent");
			assertSame(validParent,
			           expression.getParent(),
			           testName + " unexpected parent");
		}
		catch (IllegalArgumentException e)
		{
			if (!(expression instanceof AssignmentExpression<Integer>))
			{
				fail(testName + " unexpected setLeft failure");
			}
		}

		/*
		 * TerminalExpression are not valid parents
		 */
		VariableExpression<Integer> invalidParent = new VariableExpression<>("impossible");
		assertThrows(IllegalArgumentException.class, () -> {
			expression.setParent(invalidParent);
		}, testName + " setParent didn't threw exception");
	}

	/**
	 * Test method for {@link expressions.Expression#contains(Expression)}.
	 * @param expression the expression to test provided by
	 * {@link #expressionProvider()}
	 * @param info Test infos
	 */
	@ParameterizedTest
	@MethodSource("expressionProvider")
	@DisplayName("contains(Expression) ")
	@Order(6)
	final void testContains(Expression<Integer> expression, TestInfo info)
	{
		setupTest(expression);
		String testName = new String(testExpressionName + "." + info.getDisplayName());
		System.out.println(testName);

		// Never contains null
		assertFalse(testExpression.contains(null),
		            testName + " unexpected null content");

		// Never contains c0
		assertFalse(testExpression.contains(c0),
		            testName + " unexpectedly contains constant(0)");

		// Binary expressions contains
		//	- left operand
		// 	- right operand
		// 	- and self
		if (testExpression instanceof BinaryExpression<?>)
		{
			Expression<Integer> left;
			Expression<Integer> right;
			if (testExpression instanceof AssignmentExpression<?>)
			{
				left = a0;
				right = c22;
			}
			else
			{
				left = operands[0];
				right = operands[1];
			}
			assertTrue(testExpression.contains(left),
			           testName + " didn't found left operand of " + testExpression + " : " + left);
			assertTrue(testExpression.contains(right),
			           testName + " didn't found right operand of " + testExpression + " : " + right);
			assertTrue(testExpression.contains(testExpression),
			           testName + " didn't found self");
		}

		// Constant expression contains c2
		if (testExpression instanceof ConstantExpression<?>)
		{
			assertTrue(testExpression.contains(c21),
			           testName + " unexpectedly didn't found constant");
		}

		// Variable expression contains a2
		if (testExpression instanceof VariableExpression<?>)
		{
			assertTrue(testExpression.contains(a2),
			           testName + " enexpectedly didn't found variable");
		}

	}

	// TODO add test for contains(expr) method

	/**
	 * Test method for {@link expressions.Expression#equals(java.lang.Object)}.
	 * @param expression the expression to test provided by
	 * {@link #expressionProvider()}
	 * @param info Test infos
	 */
	@ParameterizedTest(name = "{0}.equals(Object)")
	@MethodSource("expressionProvider")
	@DisplayName("equals(Object)")
	@Order(7)
	final void testEqualsObject(Expression<Integer> expression, TestInfo info)
	{
		setupTest(expression);
		String testName = new String(testExpressionName + ": " + info.getDisplayName());
		System.out.println(testName);

		// Non equality to null
		assertNotEquals(null,
		                testExpression,
		                testName + " failed with equality to null");

		// Equality to self
		assertEquals(testExpression,
		             testExpression,
		             testName + " failed with non self equality");

		// Non equality with other class
		assertNotEquals(new Object(),
		                testExpression,
		                testName + " failed with equality to Object");

		// Distinct Equality with explicit copy
		Expression<Integer> copyExpression = copyExpression(testExpression);
		assertNotSame(copyExpression,
		              testExpression,
		              testName + " unexpected non distinct copy");
		assertEquals(copyExpression,
		             testExpression,
		             testName + " unexpected non equality with copy");

		// Distinct Equality to other with same content
		Expression<?> altExpression = altExpressionsMap.get(testExpressionClass);
		assertNotSame(altExpression,
		              testExpression,
		              testName + " unexpected same expression");
		assertEquals(altExpression,
		             testExpression,
		             testName + " unexpected non equality with alt expressions");

		// Non equality to other with same class but different content
		Expression<?> otherExpression = otherExpressionsMap.get(testExpressionClass);
		assertNotEquals(otherExpression,
		                testExpression,
		                testName + " unexpected equality with other expression");
	}

	/**
	 * Test method for {@link java.lang.Object#toString()}.
	 * @param expression the expression to test provided by
	 * {@link #expressionProvider()}
	 * @param info Test infos
	 */
	@ParameterizedTest
	@MethodSource("expressionProvider")
	@DisplayName("toString()")
	@Order(8)
	final void testToString(Expression<Integer> expression, TestInfo info)
	{
		setupTest(expression);
		String testName = new String(testExpressionName + "." + info.getDisplayName());
		System.out.println(testName);

		assertEquals(toStringsMap.get(testExpressionClass),
		             testExpression.toString(),
		             testName + " failed with wrong expression evaluation");
	}

	/**
	 * Test method for {@link expressions.Expression#hashCode()}.
	 * @param expression the expression to test provided by
	 * {@link #expressionProvider()}
	 * @param info Test infos
	 */
	@ParameterizedTest
	@MethodSource("expressionProvider")
	@DisplayName("hashCode()")
	@Order(9)
	final void testHashCode(Expression<Integer> expression, TestInfo info)
	{
		setupTest(expression);
		String testName = new String(testExpressionName + "." + info.getDisplayName());
		System.out.println(testName);

		int expectedHash = expression.toString().hashCode();
		assertEquals(expectedHash,
		             expression.hashCode(),
		             testName + " unexpected hash code");
	}
}
