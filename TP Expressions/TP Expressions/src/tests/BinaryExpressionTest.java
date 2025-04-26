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
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import expressions.Expression;
import expressions.binary.AdditionExpression;
import expressions.binary.AssignmentExpression;
import expressions.binary.BinaryExpression;
import expressions.binary.BinaryOperatorRules;
import expressions.terminal.ConstantExpression;
import expressions.terminal.VariableExpression;

/**
 * Test Class for all {@link BinaryExpression}s
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("BinaryExpression<E>")
class BinaryExpressionTest
{

	/**
	 * The types of {@link BinaryExpression}s to test
	 */
	@SuppressWarnings("unchecked")
	private static final Class<? extends BinaryExpression<Double>>[] expressionClasses =
	(Class<? extends BinaryExpression<Double>>[]) new Class<?>[]
	{
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
	 * Constants used to build {@link BinaryExpression}s
	 */
	@SuppressWarnings("unchecked")
	private static ConstantExpression<Double>[] constants =
	    (ConstantExpression<Double>[]) new ConstantExpression<?>[] {
	        new ConstantExpression<Double>(1.0),
	        new ConstantExpression<Double>(2.0),
	        new ConstantExpression<Double>(3.0),
	        new ConstantExpression<Double>(4.0)
	};

	/**
	 * Variables used to build {@link BinaryExpression}s
	 */
	@SuppressWarnings("unchecked")
	private static VariableExpression<Double>[] variables =
	    (VariableExpression<Double>[]) new VariableExpression<?>[] {
	        new VariableExpression<Double>("a"),
	        new VariableExpression<Double>("b"),
	        new VariableExpression<Double>("c", 3.0),
	        new VariableExpression<Double>("d", 4.0)
	};

	/**
	 * Index associated to {@link BinaryExpression} type within all possible
	 * expressions array
	 * @param type the type of {@link BinaryExpression}
	 * @return the index of a specific {@link BinaryExpression} within all
	 * possible expressions, or -1 if there is no such expression type
	 */
	private static int indexOf(Class<? extends BinaryExpression<Double>> type)
	{
		if (type == AssignmentExpression.class)
		{
			return 0;
		}
		else if (type == AdditionExpression.class)
		{
			return 1;
		}
		// TODO uncomment when SubtractionExpression is ready
//		else if (type == SubtractionExpression.class)
//		{
//			return 2;
//		}
		// TODO uncomment when MultiplicationExpression is ready
//		else if (type == MultiplicationExpression.class)
//		{
//			return 3;
//		}
		// TODO uncomment when DivisionExpression is ready
//		else if (type == DivisionExpression.class)
//		{
//			return 4;
//		}
		// TODO uncomment when PowerExpression is ready
//		else if (type == PowerExpression.class)
//		{
//			return 5;
//		}
		else
		{
			return -1;
		}
	}


	/**
	 * Expression class provider used for each parameterized tests
	 * requiring a class
	 * @return a stream of expression classes to use in each @ParameterizedTest
	 */
	private static Stream<Class<? extends BinaryExpression<Double>>> expressionClassesProvider()
	{
		return Stream.of(expressionClasses);
	}


	/**
	 * Expression provider used for each parameterized tests
	 * requiring an instance
	 * @return a stream of expression to use in each @ParameterizedTest
	 */
	private static Stream<BinaryExpression<Double>> emptyExpressionsProvider()
	{
		return expressionClassesProvider().map(clazz -> BinaryExpressionFactory.<Double>get(clazz));
	}

	/**
	 * Argument Stream provider
	 * Where each argument is composed of
 	 * @return A Stream of arguments
	 */
	private static Stream<Arguments> expressionsClassAndOperandsProvider()
	{
		return expressionClassesProvider().flatMap(clazz -> {
			if (clazz == AssignmentExpression.class)
			{
				return Stream.of(Arguments.of(clazz, null, null),
				                 Arguments.of(clazz, variables[0], constants[0]),
				                 Arguments.of(clazz, variables[1], constants[2]));
			}
			else
			{
				return Stream.of(Arguments.of(clazz, null, null),
				                 Arguments.of(clazz, constants[0], constants[1]),
				                 Arguments.of(clazz, constants[2], variables[0]),
				                 Arguments.of(clazz, variables[1], constants[3]),
				                 Arguments.of(clazz, variables[2], variables[3]));
			}
		});
	}

	/**
	 * Expression provider used for each parameterized tests
	 * requiring an instance
	 * @return a stream of expression to use in each ParameterizedTest
	 */
	private static Stream<BinaryExpression<Double>> expressionsProvider()
	{

		return expressionClassesProvider().flatMap(clazz -> {
			if (clazz == AssignmentExpression.class)
			{
				return Stream.of(BinaryExpressionFactory.<Double> get(clazz,
				                                                      variables[0],
				                                                      null),
				                 BinaryExpressionFactory.<Double> get(clazz,
				                                                      variables[1],
				                                                      constants[0]),
				                 BinaryExpressionFactory.<Double> get(clazz,
				                                                      variables[2],
				                                                      variables[3]));
			}
			else
			{
				return Stream.of(BinaryExpressionFactory.<Double>get(clazz, null, null),
				                 BinaryExpressionFactory.<Double>get(clazz,
				                                                     constants[0],
				                                                     constants[1]),
				                 BinaryExpressionFactory.<Double>get(clazz,
				                                                     constants[2],
				                                                     variables[0]),
				                 BinaryExpressionFactory.<Double>get(clazz,
				                                                     variables[1],
				                                                     constants[3]),
				                 BinaryExpressionFactory.<Double>get(clazz,
				                                                     variables[2],
				                                                     variables[3]));
			}
		});
	}

	/**
	 * Expression provider used for each parameterized tests
	 * requiring an instance
	 * @return a stream of expression to use in each ParameterizedTest
	 */
	private static Stream<Arguments> expressionsAndOperandsProvider()
	{

		return expressionClassesProvider().flatMap(clazz -> {
			if (clazz == AssignmentExpression.class)
			{
				return Stream.of(Arguments.of(BinaryExpressionFactory.<Double> get(clazz,
				                                                                   variables[0],
				                                                                   null),
				                              variables[0],
				                              null),
				                 Arguments.of(BinaryExpressionFactory.<Double> get(clazz,
				                                                                   variables[1],
				                                                                   constants[0]),
				                              variables[1],
				                              constants[0]),
				                 Arguments.of(BinaryExpressionFactory.<Double> get(clazz,
				                                                                   variables[2],
				                                                                   variables[3]),
				                              variables[2],
				                              variables[3]));
			}
			else
			{
				return Stream.of(Arguments.of(BinaryExpressionFactory.<Double>get(clazz, null, null),
				                              null,
				                              null),
				                 Arguments.of(BinaryExpressionFactory.<Double>get(clazz,
				                                                                  constants[0],
				                                                                  constants[1]),
				                              constants[0],
				                              constants[1]),
				                 Arguments.of(BinaryExpressionFactory.<Double>get(clazz,
				                                                                  constants[2],
				                                                                  variables[0]),
				                              constants[2],
				                              variables[0]),
				                 Arguments.of(BinaryExpressionFactory.<Double>get(clazz,
				                                                                  variables[1],
				                                                                  constants[3]),
				                              variables[1],
				                              constants[3]),
				                 Arguments.of(BinaryExpressionFactory.<Double>get(clazz,
				                                                                  variables[2],
				                                                                  variables[3]),
				                              variables[2],
				                              variables[3]));
			}
		});
	}
	/**
	 * Copy an expression so that the two expressions can be compared
	 * @param <E> the type of numbers in expressions
	 * @param expression thebinary  expression to copy
	 * @return a distrinct copy of the provided expression
	 */
	private static <E extends Number> BinaryExpression<E> copyExpression(BinaryExpression<E> expression)
	{
		BinaryExpression<E> binop = expression;
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
//		if (expression instanceof SubtractionExpression<?>)
//		{
//			if ((left != null) || (right != null))
//			{
//				return new SubtractionExpression<E>(left, right);
//			}
//			else
//			{
//				return new SubtractionExpression<>();
//			}
//		}
		// TODO uncomment when MultiplicationExpression is ready
//		if (expression instanceof MultiplicationExpression<?>)
//		{
//			if ((left != null) || (right != null))
//			{
//				return new MultiplicationExpression<E>(left, right);
//			}
//			else
//			{
//				return new MultiplicationExpression<>();
//			}
//		}

		// TODO uncomment when DivisionExpression is ready
//		if (expression instanceof DivisionExpression<?>)
//		{
//			if ((left != null) || (right != null))
//			{
//				return new DivisionExpression<E>(left, right);
//			}
//			else
//			{
//				return new DivisionExpression<>();
//			}
//		}

		// TODO uncomment when PowerExpression is ready
//		if (expression instanceof PowerExpression<?>)
//		{
//			if ((left != null) || (right != null))
//			{
//				return new PowerExpression<E>(left, right);
//			}
//			else
//			{
//				return new PowerExpression<>();
//			}
//		}

		return null;
	}

	/**
	 * Setup before all tests
	 * @throws java.lang.Exception if setup before all tests fails
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
	}

	/**
	 * Teardown after all tests
	 * @throws java.lang.Exception if teardown after all tests fails
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception
	{
	}

	/**
	 * Setup before each test
	 * @param testInfo Test infos used to setup
	 * @throws java.lang.Exception if setup before each test fails
	 */
	@BeforeEach
	void setUp(TestInfo testInfo) throws Exception
	{
		if (testInfo.getTags().contains("mytag"))
		{
			// Performs special setup according to @Tag("mytag") annotation
		}
	}

	/**
	 * Teardown after each test
	 * @throws java.lang.Exception if teardown after each test fails
	 */
	@AfterEach
	void tearDown() throws Exception
	{
	}

	/**
	 * Test method for {@link BinaryExpression}s constructors with two
	 * {@link Expression} arguments
	 * @param type The type of Binary expression to construct
	 * @param exp1 The expression to assign to the left of binary expression
	 * @param exp2 The expression to assign to the right of binary expression
	 * @param testInfo Test info
	 */
	@Order(1)
	@ParameterizedTest(name ="[{index}] {0}({1}, {2})")
	@MethodSource("expressionsClassAndOperandsProvider")
	@DisplayName("Constructor(Expression<E> exp1, Expression<E> exp2)")
	final void
	    testBinaryExpressionExpressionExpression(Class<? extends BinaryExpression<Double>> type,
	                                             Expression<Double> exp1,
	                                             Expression<Double> exp2,
	                                             TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		Constructor<? extends BinaryExpression<Double>> constructor = null;
		Class<?>[] argumentTypes = new Class<?>[2];
		if (type == AssignmentExpression.class)
		{
			// Assignment left side must be a variable
			argumentTypes[0] = VariableExpression.class;
		}
		else
		{
			argumentTypes[0] = Expression.class;
		}
		argumentTypes[1] = Expression.class;
		try
		{
			constructor = type.getConstructor(argumentTypes);
		}
		catch (NoSuchMethodException e)
		{
			fail(testName + " missing constructor " + type.getSimpleName()
			    + "(Expression, Expression): " + e.getLocalizedMessage());
		}
		catch (SecurityException e)
		{
			fail(testName + " inaccessible valued constructor "
			    + e.getLocalizedMessage());
		}

		assertNotNull(constructor,
		              testName + " unexpected null valued constructor");

		Object[] arguments = new Object[2];
		arguments[0] = exp1;
		arguments[1] = exp2;
		Object instance = null;
		try
		{
			instance = constructor.newInstance(arguments);
		}
		catch (InstantiationException e)
		{
			fail(testName + " instantiation exception : Abstract class "
			    + e.getLocalizedMessage());
		}
		catch (IllegalAccessException e)
		{
			fail(testName + " valued constructor is inaccessible "
			    + e.getLocalizedMessage());
		}
		catch (IllegalArgumentException e)
		{
			fail(testName + " valued constructor illegal argument "
			    + e.getLocalizedMessage());
		}
		catch (InvocationTargetException e)
		{
			fail(testName + " invoked valued constructor threw an exception "
			    + e.getLocalizedMessage());
		}

		assertNotNull(instance, testName + " unexpected null instance");
		assertEquals(type,
		             instance.getClass(),
		             testName + " unexpected instance class");
		@SuppressWarnings("unchecked")
		BinaryExpression<Double> binop = (BinaryExpression<Double>) instance;
		Expression<Double> left = binop.getLeft();
		Expression<Double> right = binop.getRight();
		if (exp1 != null)
		{
			assertSame(exp1, left, testName + " unexpected left");
			assertSame(binop, exp1.getParent(), testName + " unexpected parent on left");
		}
		if (exp2 != null)
		{
			assertSame(exp2, right, testName + " unexpected left");
			assertSame(binop, exp2.getParent(), testName + " unexpected parent on right");
		}

		if (binop instanceof AssignmentExpression<?>)
		{
			if ((left != null) && (right != null))
			{
				if (right.hasValue())
				{
					assertTrue(left.hasValue(),
					           testName + " unexpected left side without value "
					           	+ "in Assignment");
					assertEquals(right.value(),
					             left.value(),
					             testName + " unexpected different values "
					             	+ "in Assignment");
				}
			}
		}
	}

	/**
	 * Test method for {@link BinaryExpression}s constructors with no arguments
	 * @param type The type of expression to build
	 * @param testInfo Test info
	 */
	@Order(2)
	@ParameterizedTest(name ="[{index}] {0}()")
	@MethodSource("expressionClassesProvider")
	@DisplayName("Constructor()")
	final void testBinaryExpression(Class<? extends BinaryExpression<Double>> type,
		TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		Constructor<? extends BinaryExpression<Double>> constructor = null;
		Class<?>[] argumentTypes = new Class<?>[0];
		try
		{
			constructor = type.getConstructor(argumentTypes);
		}
		catch (NoSuchMethodException e)
		{
			fail(testName + " missing constructor " + type.getSimpleName()
			    + "(): " + e.getLocalizedMessage());
		}
		catch (SecurityException e)
		{
			fail(testName + " inaccessible default constructor "
			    + e.getLocalizedMessage());
		}

		assertNotNull(constructor,
		              testName + " unexpected null default constructor");

		Object[] arguments = new Object[0];
		Object instance = null;
		try
		{
			instance = constructor.newInstance(arguments);
		}
		catch (InstantiationException e)
		{
			fail(testName + " instantiation exception : Abstract class "
			    + e.getLocalizedMessage());
		}
		catch (IllegalAccessException e)
		{
			fail(testName + " default constructor is inaccessible "
			    + e.getLocalizedMessage());
		}
		catch (IllegalArgumentException e)
		{
			fail(testName + " default constructor illegal argument "
			    + e.getLocalizedMessage());
		}
		catch (InvocationTargetException e)
		{
			fail(testName + " invoked default constructor threw an exception "
			    + e.getLocalizedMessage());
		}

		assertNotNull(instance, testName + " unexpected null instance");
		assertEquals(type,
		             instance.getClass(),
		             testName + " unexpected instance class");
	}

	/**
	 * Test method for {@link expressions.binary.BinaryExpression#value()}.
	 * @param expression The binary expression provided by the provider
	 * @param left the expected left side of binary expression
	 * @param right the expected right side of binary expression
	 * @param testInfo Test info
	 */
	@Order(9)
	@ParameterizedTest(name ="[{index}] ({0}).value()")
	@MethodSource("expressionsAndOperandsProvider")
	@DisplayName("value()")
	final void testValue(BinaryExpression<Double> expression,
	                     Expression<Double> left,
	                     Expression<Double> right,
	                     TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		assertNotNull(expression, testName + " unexpected null expression");
		if ((left == null) || (right == null))
		{
			assertThrows(IllegalStateException.class, () -> {
				expression.value();
			}, testName + " value() didn't threw IllegalStateException with"
				+ " null operand");
		}
		else // (left != null) && (right != null)
		{
			if (left.hasValue() && right.hasValue())
			{
				Double leftValue = left.value();
				Double rightValue = right.value();

				Double providedValue = null;
				try
				{
					providedValue = expression.value();
				}
				catch (IllegalStateException e)
				{
					fail(testName + " unexpected exception: "
					    + e.getLocalizedMessage());
				}
				assertNotNull(providedValue,
				              testName + " unexpected null provided value");

				Double expectedValue = null;
				if (expression instanceof AssignmentExpression<?>)
				{
					expectedValue = right.value();
				}
				else if (expression instanceof AdditionExpression<?>)
				{
					expectedValue = Double.valueOf(leftValue + rightValue);
				}
				// TODO uncomment when SubtractionExpression is ready
//				else if (expression instanceof SubtractionExpression<?>)
//				{
//					expectedValue = Double.valueOf(leftValue - rightValue);
//				}
				// TODO uncomment when MultiplicationExpression is ready
//				else if (expression instanceof MultiplicationExpression<?>)
//				{
//					expectedValue = Double.valueOf(leftValue * rightValue);
//				}
				// TODO uncomment when DivisionExpression is ready
//				else if (expression instanceof DivisionExpression<?>)
//				{
//					expectedValue = Double.valueOf(leftValue / rightValue);
//				}
				// TODO uncomment when PowerExpression is ready
//				else if (expression instanceof PowerExpression<?>)
//				{
//					expectedValue = Double.valueOf(Math.pow(leftValue, rightValue));
//				}
				else
				{
					fail(testName + " unknown binary expression type: "
					    + expression.getClass().getSimpleName());
				}
				assertNotNull(expectedValue,
				              testName + " no expected value computed");
				assertEquals(expectedValue,
				             providedValue,
				             testName + " unexpected value");
			}
			else // !left.hasValue() || !right.hasValue()
			{
				assertThrows(IllegalStateException.class, () -> {
					expression.value();
				}, testName + " value() didn't threw IllegalStateException with"
					+ " non value operand");
			}
		}
	}

	/**
	 * Test method for {@link expressions.binary.BinaryExpression#hasValue()}.
	 * @param expression The binary expression provided by the provider
	 * @param left the expected left side of binary expression
	 * @param right the expected right side of binary expression
	 * @param testInfo Test info
	 */
	@Order(8)
	@ParameterizedTest(name = "[{index}] ({0}).hasValue()")
	@MethodSource("expressionsAndOperandsProvider")
	@DisplayName("hasValue()")
	final void testHasValue(BinaryExpression<Double> expression,
	                        Expression<Double> left,
	                        Expression<Double> right,
	                        TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		assertNotNull(expression, testName + " unexpected null expression");

		if ((left != null) && (right != null))
		{
			assertEquals(left.hasValue() && right.hasValue(),
			             expression.hasValue(),
			             testName + " unexpected hasValue status");
		}
		else
		{
			assertFalse(expression.hasValue(),
			            testName + " unexpected hasValue status");
		}
	}

	/**
	 * Test method for {@link expressions.binary.BinaryExpression#toString()}.
	 * @param expression The binary expression provided by the provider
	 * @param left the expected left side of binary expression
	 * @param right the expected right side of binary expression
	 * @param testInfo Test info
	 */
	@Order(12)
	@ParameterizedTest(name ="[{index}] ({0}).toString()")
	@MethodSource("expressionsAndOperandsProvider")
	@DisplayName("toString()")
	final void testToString(BinaryExpression<Double> expression,
	                        Expression<Double> left,
	                        Expression<Double> right,
	                        TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		assertNotNull(expression, testName + " unexpected null expression");

		StringBuilder sb = new StringBuilder();
		if (left != null)
		{
			sb.append(left.toString());
			sb.append(' ');
		}
		sb.append(expression.getRules().toString());
		if (right != null)
		{
			sb.append(' ');
			sb.append(right.toString());
		}

		assertEquals(sb.toString(),
		             expression.toString(),
		             testName + " unexpected string");
	}

	/**
	 * Test method for {@link expressions.binary.BinaryExpression#getLeft()}.
	 * @param expression The binary expression provided by the provider
	 * @param left the expected left side of binary expression
	 * @param right the expected right side of binary expression
	 * @param testInfo Test info
	 */
	@Order(3)
	@ParameterizedTest(name ="[{index}] ({0}).getLeft()")
	@MethodSource("expressionsAndOperandsProvider")
	@DisplayName("getLeft()")
	final void testGetLeft(BinaryExpression<Double> expression,
	                       Expression<Double> left,
	                       Expression<Double> right,
	                       TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		assertNotNull(expression, testName + " unexpected null expression");

		assertSame(left,
		           expression.getLeft(),
		           testName + " unexpected left side");
	}

	/**
	 * Test method for {@link expressions.binary.BinaryExpression#getRight()}.
	 * @param expression The binary expression provided by the provider
	 * @param left the expected left side of binary expression
	 * @param right the expected right side of binary expression
	 * @param testInfo Test info
	 */
	@Order(4)
	@ParameterizedTest(name ="[{index}] ({0}).getRight()")
	@MethodSource("expressionsAndOperandsProvider")
	@DisplayName("getRight()")
	final void testGetRight(BinaryExpression<Double> expression,
	                        Expression<Double> left,
	                        Expression<Double> right,
	                        TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		assertNotNull(expression, testName + " unexpected null expression");

		assertSame(right,
		           expression.getRight(),
		           testName + " unexpected right side");
	}

	/**
	 * Test method for {@link expressions.binary.BinaryExpression#setLeft(expressions.Expression)}.
	 * @param expression The binary expression provided by the provider
	 * @param left the expected left side of binary expression
	 * @param right the expected right side of binary expression
	 * @param testInfo Test info
	 */
	@Order(10)
	@ParameterizedTest(name ="[{index}] ({0}).setLeft(Expression)")
	@MethodSource("expressionsAndOperandsProvider")
	@DisplayName("setLeft(Expression)")
	final void testSetLeft(BinaryExpression<Double> expression,
	                        Expression<Double> left,
	                        Expression<Double> right,
	                        TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		assertNotNull(expression, testName + " unexpected null expression");
		assertSame(left, expression.getLeft(), testName + " unexpected left");

		/*
		 * new null left
		 */
		expression.setLeft(null);
		assertNull(expression.getLeft(), testName + " unexpected non null left side");
		if (left != null)
		{
			assertNull(left.getParent(),
			           testName + " unexpected non null parent on detached left");
		}

		/*
		 * new non null left side
		 */
		for (int i = 0; i < variables.length; i++)
		{
			VariableExpression<Double> newLeft;
			int j = 0;
			while ((newLeft = variables[((i+j)%variables.length)]) == left)
			{
				j++;
			}
			assertNotSame(left, newLeft, testName + " new left collision");
			expression.setLeft(newLeft);
			assertSame(newLeft, expression.getLeft(), testName + " unexpected right");
		}
	}

	/**
	 * Test method for {@link expressions.binary.BinaryExpression#setRight(expressions.Expression)}.
	 * @param expression The binary expression provided by the provider
	 * @param left the expected left side of binary expression
	 * @param right the expected right side of binary expression
	 * @param testInfo Test info
	 */
	@Order(11)
	@ParameterizedTest(name ="[{index}] ({0}).setRight(Expression)")
	@MethodSource("expressionsAndOperandsProvider")
	@DisplayName("setRight(Expression)")
	final void testSetRight(BinaryExpression<Double> expression,
	                        Expression<Double> left,
	                        Expression<Double> right,
	                        TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		assertNotNull(expression, testName + " unexpected null expression");
		assertSame(right, expression.getRight(), testName + " unexpected right");

		/*
		 * new null right
		 * 	- Variables
		 */
		try
		{
			expression.setRight(null);
			assertNull(expression.getRight(), testName + " unexpected non null right side");
			if (right != null)
			{
				assertNull(right.getParent(),
				           testName + " unexpected non null parent on detached right");
			}
		}
		catch (NullPointerException e)
		{
			/*
			 * Can't set null right side on AssignmentExpressions
			 * But other BinaryExpressions should allow it
			 */
			if (!(expression instanceof AssignmentExpression<?>))
			{
				fail(testName + " couldn't set null right side");
			}
		}

		/*
		 * new non null right side
		 */
		for (int i = 0; i < constants.length; i++)
		{
			ConstantExpression<Double> newRight;
			int j = 0;
			while ((newRight = constants[((i+j)%constants.length)]) == right)
			{
				j++;
			}
			assertNotSame(right, newRight, testName + " new right collision");
			expression.setRight(newRight);
			assertSame(newRight, expression.getRight(), testName + " unexpected right");
		}
	}

	/**
	 * Test method for {@link expressions.binary.BinaryExpression#getRules()}.
	 * @param expression The binary expression provided by the provider
	 * @param testInfo Test info
	 */
	@Order(5)
	@ParameterizedTest(name ="[{index}] ({0}).getRules()")
	@MethodSource("expressionsProvider")
	@DisplayName("getRules()")
	final void testGetRules(BinaryExpression<Double> expression,
	                        TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		assertNotNull(expression, testName + " unexpected null expression");

		BinaryOperatorRules expectedRules = null;
		String expectedSymbol = null;
		int expectedPriority = 0;
		if (expression instanceof AssignmentExpression<?>)
		{
			expectedRules = BinaryOperatorRules.ASSIGNMENT;
			expectedSymbol = "=";
			expectedPriority = 0;
		}
		else if (expression instanceof AdditionExpression<?>)
		{
			expectedRules = BinaryOperatorRules.ADDITION;
			expectedSymbol = "+";
			expectedPriority = 1;
		}
		// TODO uncomment when SubtractionExpression is ready
//		else if (expression instanceof SubtractionExpression<?>)
//		{
//			expectedRules = BinaryOperatorRules.SUBTRACTION;
//			expectedSymbol = "-";
//			expectedPriority = 1;
//		}
		// TODO uncomment when MultiplicationExpression is ready
//		else if (expression instanceof MultiplicationExpression<?>)
//		{
//			expectedRules = BinaryOperatorRules.MULTIPLICATION;
//			expectedSymbol = "*";
//			expectedPriority = 2;
//		}
		// TODO uncomment when DivisionExpression is ready
//		else if (expression instanceof DivisionExpression<?>)
//		{
//			expectedRules = BinaryOperatorRules.DIVISION;
//			expectedSymbol = "/";
//			expectedPriority = 2;
//		}
		// TODO uncomment when PowerExpression is ready
//		else if (expression instanceof PowerExpression<?>)
//		{
//			expectedRules = BinaryOperatorRules.POWER;
//			expectedSymbol = "^";
//			expectedPriority = 3;
//		}
		else
		{
			fail(testName + " unexpected binary expression type "
			    + expression.getClass().getSimpleName());
		}

		BinaryOperatorRules providedRules = expression.getRules();

		assertEquals(expectedRules,
		             providedRules,
		             testName + " unexpected rules");
		assertEquals(expectedSymbol,
		             providedRules.toString(),
		             testName + " unexpected symbol");
		assertEquals(expectedPriority,
		             providedRules.priority(),
		             testName + " unexpected priority");
	}

	/**
	 * Test method for {@link expressions.binary.BinaryExpression#getParent()}.
	 * @param expression The binary expression provided by the provider
	 * @param left the expected left side of binary expression
	 * @param right the expected right side of binary expression
	 * @param testInfo Test info
	 */
	@Order(6)
	@ParameterizedTest(name ="[{index}] ({0}).getParent()")
	@MethodSource("expressionsAndOperandsProvider")
	@DisplayName("getParent()")
	final void testGetParent(BinaryExpression<Double> expression,
	                         Expression<Double> left,
	                         Expression<Double> right,
	                         TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		assertNotNull(expression, testName + " unexpected null expression");
		assertNull(expression.getParent(),
		           testName + " unexpected non null parent");
		if (left != null)
		{
			assertSame(expression,
			           left.getParent(),
			           testName + " unexpected left parent");
		}
		if (right != null)
		{
			assertSame(expression,
			           right.getParent(),
			           testName + " unexpected right parent");
		}
	}

	/**
	 * Test method for {@link expressions.binary.BinaryExpression#setParent(Expression)}.
	 * @param expression The binary expression provided by the provider
	 * @param left the expected left side of binary expression
	 * @param right the expected right side of binary expression
	 * @param testInfo Test info
	 */
	@Order(7)
	@ParameterizedTest(name ="[{index}] ({0}).setParent(Expression)")
	@MethodSource("expressionsAndOperandsProvider")
	@DisplayName("setParent(Expression)")
	final void testSetParent(BinaryExpression<Double> expression,
	                         Expression<Double> left,
	                         Expression<Double> right,
	                         TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		assertNotNull(expression, testName + " unexpected null expression");
		assertNull(expression.getParent(),
		           testName + " unexpected non null parent");

		// Can't set Terminal expression as parent
		assertThrows(IllegalArgumentException.class, () -> {
			expression.setParent(new ConstantExpression<Double>(0.0));
		});

		// Parent can't be contained in this expression
		if (right != null)
		{
			assertThrows(IllegalArgumentException.class, () -> {
				expression.setParent(right);
			});
		}
		if (left != null)
		{
			assertThrows(IllegalArgumentException.class, () -> {
				expression.setParent(left);
			});

		}
		assertThrows(IllegalArgumentException.class, () -> {
			expression.setParent(expression);
		});

		// Binary Expressions are valid parents except for
		// AssignmentExpression which can't have parents at all.
		try
		{
			// Set parent is called indirectly
			BinaryExpression<Double> parent =
			    new AdditionExpression<>(expression, null);
			assertSame(expression,
			           parent.getLeft(),
			           testName + " unexpected parent's left side");
			assertSame(parent,
			           expression.getParent(),
			           testName + " unexpected expression parent");

			// Set parent is called directly (should be prohibited)
			BinaryExpression<Double> parent2 = new AdditionExpression<>();
			expression.setParent(parent2);
			assertSame(parent2,
			           expression.getParent(),
			           testName + " unexepected parent");
			// parent2's left or right hasn't been set
			assertNull(parent2.getLeft(),
			           testName + " unexpected parent's left side");
			assertNull(parent2.getRight(),
			           testName + " unexpected parent's right side");
		}
		catch (IllegalArgumentException e)
		{
			if (!(expression instanceof AssignmentExpression<?>))
			{
				fail(testName + " unexpected: " + e.getLocalizedMessage());
			}
		}
	}

	/**
	 * Test method for {@link expressions.AbstractExpression#hashCode()}.
	 * @param expression The binary expression provided by the provider
	 * @param testInfo Test info
	 */
	@Order(15)
	@ParameterizedTest(name ="[{index}] ({0}).hashCode()")
	@MethodSource("expressionsProvider")
	@DisplayName("hashCode()")
	final void testHashCode(BinaryExpression<Double> expression,
	                        TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		assertNotNull(expression, testName + " unexpected null expression");

		int expected = expression.toString().hashCode();
		assertEquals(expected,
		             expression.hashCode(),
		             testName + " unexpected hash code");
	}

	// TODO add test for contains(expr) method

	/**
	 * Test method for {@link expressions.AbstractExpression#equals(java.lang.Object)}.
	 * @param expression The binary expression provided by the provider
	 * @param left the expected left side of binary expression
	 * @param right the expected right side of binary expression
	 * @param testInfo Test info
	 */
	@Order(13)
	@ParameterizedTest(name ="[{index}] ({0}).equals(Object)")
	@MethodSource("expressionsAndOperandsProvider")
	@DisplayName("equals(Object)")
	final void testEqualsObject(BinaryExpression<Double> expression,
		                        Expression<Double> left,
		                        Expression<Double> right,
		                        TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		assertNotNull(expression, testName + " unexpected null expression");

		// Inequality to null
		assertFalse(expression.equals(null),
		            testName + " unexpected equality to null");

		// Equality to self
		assertEquals(expression,
		             expression,
		             testName + " unexpected inequality to self");

		// Inequality to other object
		assertNotEquals(new Object(),
		                expression,
		                testName + " unexpected equality to Object");

		// Equality to distinct copy
		BinaryExpression<Double> copy = copyExpression(expression);
		assertNotSame(copy,
		              expression,
		              testName + " unexpected same expression");
		assertEquals(copy, expression, testName + " unexpected inequality");

		if ((right != null) && (left != null))
		{
			copy.setRight(copy.getLeft());
			assertNotEquals(copy, expression, testName + " unexpected inequality");
		}
	}
}
