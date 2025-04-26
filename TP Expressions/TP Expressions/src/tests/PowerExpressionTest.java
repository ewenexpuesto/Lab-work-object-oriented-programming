package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Vector;
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
import expressions.binary.BinaryOperatorRules;
import expressions.binary.PowerExpression;
import expressions.terminal.ConstantExpression;
import expressions.terminal.VariableExpression;

/**
 * Test class for {@link PowerExpression}
 * @author davidroussel
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("PowerExpression<E>")
public class PowerExpressionTest
{
	/**
	 * The filled expression to test
	 */
	private PowerExpression<? extends Number> testFilledExpression;

	/**
	 * The empty expression to test
	 */
	private PowerExpression<? extends Number> testEmptyExpression;

	/**
	 * The null expression to test
	 */
	private PowerExpression<? extends Number> testNullExpression;

	/**
	 * Value natures to test
	 */
	@SuppressWarnings("unchecked")
	private final static Class<? extends Number>[] valueClasses =
	    (Class<? extends Number>[]) new Class<?>[] {
		Integer.class,
		Float.class,
		Double.class,
		BigDecimal.class	// Should trigger exceptions
	};

	/**
	 * Names for variables
	 */
	private final static String[] names = new String[] {
		"a",
		"b",
		"c",
		"d"
	};

	/**
	 * Index a  particular type in {@link #valueClasses}
	 * @param type the type to search
	 * @return the index of this type in various arrays
	 */
	private static int indexOf(Class<? extends Number> type)
	{
		if (type == Integer.class)
		{
			return 0;
		}
		else if (type == Float.class)
		{
			return 1;
		}
		else if (type == Double.class)
		{
			return 2;
		}
		else if (type == BigDecimal.class)
		{
			return 3;
		}
		else
		{
			return -1;
		}
	}

	/**
	 * List of all possible types of expressions
	 */
	private static List<Class<? extends Number>> valueClassList = new Vector<Class<? extends Number>>();

	/**
	 * Array of all possible expressions (filled with values).
	 * {@link #filledExpressions} are evaluable since they contains
	 * {@link ConstantExpression} on each side
	 */
	private final static PowerExpression<? extends Number>[] filledExpressions =
		new PowerExpression<?>[valueClasses.length];
	/**
	 * Array of all possible expressions (NOT filled with values)
	 * {@link #emptyExpressions} are NOT evaluable since they contains
	 * a {@link VariableExpression} on left side and a
	 * {@link ConstantExpression} on right side
	 */
	private final static PowerExpression<? extends Number>[] emptyExpressions =
		new PowerExpression<?>[valueClasses.length];
	/**
	 * Array of all possible expressions (with null childs)
	 * {@link #emptyExpressions} are not evaluable since they do not
	 * contain any expression on either side
	 */
	private final static PowerExpression<? extends Number>[] nullExpressions =
		new PowerExpression<?>[valueClasses.length];

	/**
	 * Map to get a filled expression from value type
	 * @see #setUpBeforeClass()
	 */
	private static Map<Class<? extends Number>, PowerExpression<? extends Number>> filledExpressionsMap =
	    new ConcurrentHashMap<Class<? extends Number>, PowerExpression<? extends Number>>();

	/**
	 * Map to get an empty expression from value type
	 * @see #setUpBeforeClass()
	 */
	private static Map<Class<? extends Number>, PowerExpression<? extends Number>> emptyExpressionsMap =
	    new ConcurrentHashMap<Class<? extends Number>, PowerExpression<? extends Number>>();

	/**
	 * Map to get an null expression from value type
	 * @see #setUpBeforeClass()
	 */
	private static Map<Class<? extends Number>, PowerExpression<? extends Number>> nullExpressionsMap =
	    new ConcurrentHashMap<Class<? extends Number>, PowerExpression<? extends Number>>();

	/**
	 * Values to set
	 */
	private final static Number[] values = new Number[] {
		Integer.valueOf(5),
		Float.valueOf(5.0f),
		Double.valueOf(5.0),
		BigDecimal.valueOf(5)
	};

	/**
	 * Other Values to set
	 */
	private final static Number[] altValues = new Number[] {
		Integer.valueOf(3),
		Float.valueOf(3.0f),
		Double.valueOf(3.0),
		BigDecimal.valueOf(3)
	};

	/**
	 * Expected evaluation values
	 */
	private final static Number[] expectedEvaluations = new Number[] {
		Integer.valueOf(5*5*5),
		Float.valueOf(5.0f*5.0f*5.0f),
		Double.valueOf(5.0*5.0*5.0),
		BigDecimal.valueOf(5*5*5)
	};

	/**
	 * Map get get values from type of values
	 */
	private static Map<Class<? extends Number>, Number> valuesMap =
		new ConcurrentHashMap<Class<? extends Number>, Number>();

	/**
	 * Map to get alt values from type of values
	 */
	private static Map<Class<? extends Number>, Number> altValuesMap =
		new ConcurrentHashMap<Class<? extends Number>, Number>();

	/**
	 * Map to get results from type of values
	 */
	private static Map<Class<? extends Number>, Number> evaluationsMap =
		new ConcurrentHashMap<Class<? extends Number>, Number>();

	/**
	 * Setup before all tests
	 * @throws java.lang.Exception if setup fails
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
		for (int i = 0; i < valueClasses.length; i++)
		{
			Class<? extends Number> currentValueClass = valueClasses[i];
			int index = indexOf(currentValueClass);
			if (index == -1)
			{
				fail("Unknown index for " + currentValueClass.getSimpleName());
			}

			Number value = values[indexOf(currentValueClass)];
			Number altValue = altValues[indexOf(currentValueClass)];
			Number evaluation = expectedEvaluations[indexOf(currentValueClass)];

			valueClassList.add(currentValueClass);
			valuesMap.put(currentValueClass, value);
			altValuesMap.put(currentValueClass, altValue);
			evaluationsMap.put(currentValueClass, evaluation);
		}
	}

	/**
	 * Value classes provider used in each Parameterized test
	 * @return a stream of value classes to use in each @ParameterizedTest
	 */
	private static Stream<Class<? extends Number>> valueClassesProvider()
	{
		return valueClassList.stream();
	}

	/**
	 * Tear down after all tests
	 * @throws java.lang.Exception if tear down fails
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception
	{
		valueClassList.clear();
		valuesMap.clear();
		altValuesMap.clear();
		evaluationsMap.clear();
		filledExpressionsMap.clear();
		emptyExpressionsMap.clear();
	}

	/**
	 * Setup before each test
	 * @throws java.lang.Exception if setup fails
	 */
	@BeforeEach
	void setUp() throws Exception
	{
		filledExpressionsMap.clear();
		emptyExpressionsMap.clear();
		nullExpressionsMap.clear();

		// Refill expressions cause they might have been modified
		for (int i = 0; i < valueClasses.length; i++)
		{
			Class<? extends Number> currentValueClass = valueClasses[i];
			int index = indexOf(currentValueClass);
			if (index == -1)
			{
				fail("Unknown index for " + currentValueClass.getSimpleName());
			}

			Number value = values[index];
			Number altValue = altValues[index];
			String varName = names[index];

			if (currentValueClass == Integer.class)
			{
				ConstantExpression<Integer> c1 = new ConstantExpression<>((Integer)value);
				ConstantExpression<Integer> c2 = new ConstantExpression<>((Integer)altValue);
				VariableExpression<Integer> v1 = new VariableExpression<>(varName);
				filledExpressions[index] = new PowerExpression<Integer>(c1, c2);
				emptyExpressions[index] = new PowerExpression<Integer>(v1, c2);
				nullExpressions[index] = new PowerExpression<Integer>();
			}
			else if (currentValueClass == Float.class)
			{
				ConstantExpression<Float> c1 = new ConstantExpression<>((Float)value);
				ConstantExpression<Float> c2 = new ConstantExpression<>((Float)altValue);
				VariableExpression<Float> v1 = new VariableExpression<>(varName);
				filledExpressions[index] = new PowerExpression<Float>(c1, c2);
				emptyExpressions[index] = new PowerExpression<Float>(v1, c2);
				nullExpressions[index] = new PowerExpression<Float>();
			}
			else if (currentValueClass == Double.class)
			{
				ConstantExpression<Double> c1 = new ConstantExpression<>((Double)value);
				ConstantExpression<Double> c2 = new ConstantExpression<>((Double)altValue);
				VariableExpression<Double> v1 = new VariableExpression<>(varName);
				filledExpressions[index] = new PowerExpression<Double>(c1, c2);
				emptyExpressions[index] = new PowerExpression<Double>(v1, c2);
				nullExpressions[index] = new PowerExpression<Double>();
			}
			else if (currentValueClass == BigDecimal.class)
			{
				ConstantExpression<BigDecimal> c1 = new ConstantExpression<>((BigDecimal)value);
				ConstantExpression<BigDecimal> c2 = new ConstantExpression<>((BigDecimal)altValue);
				VariableExpression<BigDecimal> v1 = new VariableExpression<>(varName);
				filledExpressions[index] = new PowerExpression<BigDecimal>(c1, c2);
				emptyExpressions[index] = new PowerExpression<BigDecimal>(v1, c2);
				nullExpressions[index] = new PowerExpression<BigDecimal>();
			}
			else
			{
				index = 0; // safe but useless
				fail("Unknown expression type : " + currentValueClass.getSimpleName());
			}

			filledExpressionsMap.put(currentValueClass, filledExpressions[index]);
			emptyExpressionsMap.put(currentValueClass, emptyExpressions[index]);
			nullExpressionsMap.put(currentValueClass, nullExpressions[index]);
		}

	}

	/**
	 * Teardown after each test
	 * @throws java.lang.Exception if tear down fails
	 */
	@AfterEach
	void tearDown() throws Exception
	{
		testFilledExpression = null;
		filledExpressionsMap.clear();
		emptyExpressionsMap.clear();
		nullExpressionsMap.clear();
	}

	/**
	 * Setup expressions for testing with a specific Number type
	 * @param type the number type
	 */
	private void setupTestFor(Class<? extends Number> type)
	{
		testFilledExpression = filledExpressionsMap.get(type);
		testEmptyExpression = emptyExpressionsMap.get(type);
		testNullExpression = nullExpressionsMap.get(type);
	}

	/**
	 * Test method for {@link PowerExpression#PowerExpression()}.
	 * @param type number type for setting up the test
	 * @param info Test infos
	 */
	@ParameterizedTest(name="PowerExpression<{0}>()")
	@MethodSource("valueClassesProvider")
	@DisplayName("PowerExpression<Number>()")
	@Order(1)
	final void testPowerExpression(Class<? extends Number> type, TestInfo info)
	{
		setupTestFor(type);
		String testName = info.getDisplayName();
		System.out.println(testName);

		assertNotNull(testNullExpression, testName + " failed with null instance");
	}

	/**
	 * Test method for {@link PowerExpression#PowerExpression(expressions.Expression, expressions.Expression)}
	 * @param type number type for setting up the test
	 * @param info Test infos
	 */
	@ParameterizedTest(name="PowerExpression<{0}>(Expression, Expression)")
	@MethodSource("valueClassesProvider")
	@DisplayName("PowerExpression<Number>(Expression<Number> left, Expression<Number> right)")
	@Order(2)
	final void testPowerExpressionExpressionExpression(Class<? extends Number> type, TestInfo info)
	{
		setupTestFor(type);
		String testName = info.getDisplayName();
		System.out.println(testName);

		assertNotNull(testEmptyExpression, testName + " failed with null instance");
		assertNotNull(testFilledExpression, testName + " failed with null instance");
	}

	/**
	 * Test method for {@link PowerExpression#getRules()}.
	 * @param type number type for setting up the test
	 * @param info Test infos
	 */
	@ParameterizedTest(name="PowerExpression<{0}>.getRules()")
	@MethodSource("valueClassesProvider")
	@DisplayName("getRules()")
	@Order(3)
	final void testGetRules(Class<? extends Number> type, TestInfo info)
	{
		setupTestFor(type);
		String testName = info.getDisplayName();
		System.out.println(testName);

		int expectedPriority = BinaryOperatorRules.POWER.priority();
		String expectedToString = BinaryOperatorRules.POWER.toString();

		BinaryOperatorRules rules = testFilledExpression.getRules();
		assertEquals(expectedPriority,
		             rules.priority(),
		             testName + " failed with wrong priority");
		assertEquals(expectedToString,
		             rules.toString(),
		             testName + " failed with wrong toString");

		rules = testEmptyExpression.getRules();
		assertEquals(expectedPriority,
		             rules.priority(),
		             testName + " failed with wrong priority");
		assertEquals(expectedToString,
		             rules.toString(),
		             testName + " failed with wrong toString");

		rules = testNullExpression.getRules();
		assertEquals(expectedPriority,
		             rules.priority(),
		             testName + " failed with wrong priority");
		assertEquals(expectedToString,
		             rules.toString(),
		             testName + " failed with wrong toString");
	}

	/**
	 * Test method for {@link PowerExpression#getLeft}.
	 * @param type number type for setting up the test
	 * @param info Test infos
	 */
	@ParameterizedTest(name="PowerExpression<{0}>.getLeft()")
	@MethodSource("valueClassesProvider")
	@DisplayName("getLeft()")
	@Order(4)
	final void testGetLeft(Class<? extends Number> type, TestInfo info)
	{
		setupTestFor(type);
		String testName = info.getDisplayName();
		System.out.println(testName);

		assertNull(testNullExpression.getLeft(),
		           testName + " unexpected non null left side on null expression");
		assertNotNull(testEmptyExpression.getLeft(),
		              testName + " unexpected null left side on empty expression");
		assertNotNull(testFilledExpression.getLeft(),
		              testName + " unexpected null left side on filled expression");

		int index = indexOf(type);
		Number value = values[index];
//		Number altValue = altValues[index];
		String varName = names[index];
		Expression<?> emptyLeft = testEmptyExpression.getLeft();
		assertTrue(emptyLeft instanceof VariableExpression<?>,
		           testName + " unexpected left from empty expression");
		@SuppressWarnings("unchecked")
		VariableExpression<Number> emptyLeftVariable = (VariableExpression<Number>) emptyLeft;
		assertEquals(varName,
		             emptyLeftVariable.getName(),
		             testName + " unexpected variable name fom left side");
		Expression<?> filledLeft = testFilledExpression.getLeft();
		assertTrue(filledLeft instanceof ConstantExpression<?>,
		           testName + " unexpected left from filled expression");
		@SuppressWarnings("unchecked")
		ConstantExpression<Number> filledLeftConstant = (ConstantExpression<Number>) filledLeft;
		try
		{
			assertEquals(value,
			             filledLeftConstant.value(),
			             testName + " unexpected value from left side");
		}
		catch (IllegalStateException e)
		{
			fail(testName + " failed value() computing "
			    + e.getLocalizedMessage());
		}
	}


	/**
	 * Test method for {@link PowerExpression#getRight}.
	 * @param type number type for setting up the test
	 * @param info Test infos
	 */
	@ParameterizedTest(name="PowerExpression<{0}>.getRight()")
	@MethodSource("valueClassesProvider")
	@DisplayName("getRight()")
	@Order(5)
	final void testGetRight(Class<? extends Number> type, TestInfo info)
	{
		setupTestFor(type);
		String testName = info.getDisplayName();
		System.out.println(testName);

		assertNull(testNullExpression.getRight(),
		           testName + " unexpected non null right side on null expression");
		assertNotNull(testEmptyExpression.getRight(),
		              testName + " unexpected null right side on empty expression");
		assertNotNull(testFilledExpression.getRight(),
		              testName + " unexpected null right side on filled expression");

		int index = indexOf(type);
//		Number value = values[index];
		Number altValue = altValues[index];
//		String varName = names[index];

		Expression<?> emptyRight = testEmptyExpression.getRight();
		assertTrue(emptyRight instanceof ConstantExpression<?>,
		           testName + " unexpected right from empty expression");
		@SuppressWarnings("unchecked")
		ConstantExpression<Number> emptyRightConstant = (ConstantExpression<Number>) emptyRight;
		assertEquals(altValue,
		             emptyRightConstant.value(),
		             testName + " unexpected value from right empty side");

		Expression<?> filledRight = testFilledExpression.getRight();
		assertTrue(filledRight instanceof ConstantExpression<?>,
		           testName + " unexpected right from filled expression");
		@SuppressWarnings("unchecked")
		ConstantExpression<Number> filledRightConstant = (ConstantExpression<Number>) filledRight;
		try
		{
			assertEquals(altValue,
			             filledRightConstant.value(),
			             testName + " unexpected value from right filled side");
		}
		catch (IllegalStateException e)
		{
			fail(testName + " failed value() computing "
			    + e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for {@link PowerExpression#hasValue()}.
	 * @param type number type for setting up the test
	 * @param info Test infos
	 */
	@ParameterizedTest(name="PowerExpression<{0}>.hasValue()")
	@MethodSource("valueClassesProvider")
	@DisplayName("hasValue()")
	final void testHasValue(Class<? extends Number> type, TestInfo info)
	{
		setupTestFor(type);
		String testName = info.getDisplayName();
		System.out.println(testName);

		assertTrue(testFilledExpression.hasValue(),
		           testName + " failed with non evaluable expression");
		assertFalse(testEmptyExpression.hasValue(),
		            testName + " failed with evaluable expression");
		assertFalse(testNullExpression.hasValue(),
		            testName + " failed with evaluable expression");
	}

	/**
	 * Test method for {@link PowerExpression#value()}.
	 * @param type number type for setting up the test
	 * @param info Test infos
	 */
	@ParameterizedTest(name="PowerExpression<{0}>.value()")
	@MethodSource("valueClassesProvider")
	@DisplayName("value()")
	final void testValue(Class<? extends Number> type, TestInfo info)
	{
		setupTestFor(type);
		String testName = info.getDisplayName();
		System.out.println(testName);

		assertThrows(IllegalStateException.class,
		             () -> {testEmptyExpression.value();},
		             testName + " failed with no exception thrown");

		assertThrows(IllegalStateException.class,
		             () -> {testNullExpression.value();},
		             testName + " failed with no exception thrown");

		if (type != BigDecimal.class)
		{
			assertEquals(evaluationsMap.get(type),
			             testFilledExpression.value(),
			             testName + " failed with wrong evaluation");
		}
		else // We don't expect binary eval to work on BigDecimal
		{
			assertThrows(UnsupportedOperationException.class,
			             () -> {testFilledExpression.value();},
			             testName + " failed with no exception thrown");
		}
	}

	/**
	 * Test method for {@link PowerExpression#setLeft(expressions.Expression)}.
	 * @param type number type for setting up the test
	 * @param info Test infos
	 */
	@ParameterizedTest(name="PowerExpression<{0}>.setLeft(Expression)")
	@MethodSource("valueClassesProvider")
	@DisplayName("setLeft(Expression)")
	final void testSetLeft(Class<? extends Number> type, TestInfo info)
	{
		setupTestFor(type);
		String testName = info.getDisplayName();
		System.out.println(testName);

		if (type == Integer.class)
		{
			Integer value = (Integer) valuesMap.get(type);
			ConstantExpression<Integer> newLeft = new ConstantExpression<Integer>(value);
			@SuppressWarnings("unchecked")
			PowerExpression<Integer> castedEmptyExpression =
			    (PowerExpression<Integer>) testEmptyExpression;
			castedEmptyExpression.setLeft(newLeft);
			Integer result = (Integer) evaluationsMap.get(type);
			assertEquals(result,
			             castedEmptyExpression.value(),
			             testName + "[" + type.getSimpleName()
			                 + "] failed with wrong evaluation");

			@SuppressWarnings("unchecked")
			PowerExpression<Integer> castedNullExpression =
				(PowerExpression<Integer>) testNullExpression;
			castedNullExpression.setLeft(newLeft);
			assertThrows(IllegalStateException.class,
			             () -> {castedNullExpression.value();},
			             testName + " failed with no exception thrown");

			ConstantExpression<Integer> newNull = null;
			@SuppressWarnings("unchecked")
			PowerExpression<Integer> castedFilledExpression =
			    (PowerExpression<Integer>) testFilledExpression;
			castedFilledExpression.setLeft(newNull);
			assertThrows(IllegalStateException.class,
			             () -> {castedFilledExpression.value();},
			             testName + " failed with no exception thrown");

		}
		if (type == Float.class)
		{
			Float value = (Float) valuesMap.get(type);
			ConstantExpression<Float> newLeft = new ConstantExpression<Float>(value);
			@SuppressWarnings("unchecked")
			PowerExpression<Float> castedEmptyExpression =
			    (PowerExpression<Float>) testEmptyExpression;
			castedEmptyExpression.setLeft(newLeft);
			Float result = (Float) evaluationsMap.get(type);
			assertEquals(result,
			             castedEmptyExpression.value(),
			             testName + "[" + type.getSimpleName()
			                 + "] failed with wrong evaluation");

			@SuppressWarnings("unchecked")
			PowerExpression<Float> castedNullExpression =
				(PowerExpression<Float>) testNullExpression;
			castedNullExpression.setLeft(newLeft);
			assertThrows(IllegalStateException.class,
			             () -> {castedNullExpression.value();},
			             testName + " failed with no exception thrown");

			ConstantExpression<Float> newNull = null;
			@SuppressWarnings("unchecked")
			PowerExpression<Float> castedFilledExpression =
			    (PowerExpression<Float>) testFilledExpression;
			castedFilledExpression.setLeft(newNull);
			assertThrows(IllegalStateException.class,
			             () -> {castedFilledExpression.value();},
			             testName + " failed with no exception thrown");

		}
		if (type == Double.class)
		{
			Double value = (Double) valuesMap.get(type);
			ConstantExpression<Double> newLeft = new ConstantExpression<Double>(value);
			@SuppressWarnings("unchecked")
			PowerExpression<Double> castedEmptyExpression =
			    (PowerExpression<Double>) testEmptyExpression;
			castedEmptyExpression.setLeft(newLeft);
			Double result = (Double) evaluationsMap.get(type);
			assertEquals(result,
			             castedEmptyExpression.value(),
			             testName + "[" + type.getSimpleName()
			                 + "] failed with wrong evaluation");

			@SuppressWarnings("unchecked")
			PowerExpression<Double> castedNullExpression =
				(PowerExpression<Double>) testNullExpression;
			castedNullExpression.setLeft(newLeft);
			assertThrows(IllegalStateException.class,
			             () -> {castedNullExpression.value();},
			             testName + " failed with no exception thrown");

			ConstantExpression<Double> newNull = null;
			@SuppressWarnings("unchecked")
			PowerExpression<Double> castedFilledExpression =
			    (PowerExpression<Double>) testFilledExpression;
			castedFilledExpression.setLeft(newNull);
			assertThrows(IllegalStateException.class,
			             () -> {castedFilledExpression.value();},
			             testName + " failed with no exception thrown");

		}
		if (type == BigDecimal.class)
		{
			BigDecimal value = (BigDecimal) valuesMap.get(type);
			ConstantExpression<BigDecimal> newLeft = new ConstantExpression<BigDecimal>(value);

			@SuppressWarnings("unchecked")
			PowerExpression<BigDecimal> castedEmptyExpression =
			    (PowerExpression<BigDecimal>) testEmptyExpression;
			castedEmptyExpression.setLeft(newLeft);
			// We don't expect binary eval to work on BigDecimal
			assertThrows(UnsupportedOperationException.class,
			             () -> {castedEmptyExpression.value();},
			             testName + " failed with no exception thrown");

			@SuppressWarnings("unchecked")
			PowerExpression<BigDecimal> castedNullExpression =
				(PowerExpression<BigDecimal>) testNullExpression;
			castedNullExpression.setLeft(newLeft);
			assertThrows(IllegalStateException.class,
			             () -> {castedNullExpression.value();},
			             testName + " failed with no exception thrown");

			ConstantExpression<BigDecimal> newNull = null;
			@SuppressWarnings("unchecked")
			PowerExpression<BigDecimal> castedFilledExpression =
			    (PowerExpression<BigDecimal>) testFilledExpression;
			castedFilledExpression.setLeft(newNull);
			assertThrows(IllegalStateException.class,
			             () -> {castedFilledExpression.value();},
			             testName + " failed with no exception thrown");
		}
	}

	/**
	 * Test method for {@link PowerExpression#setRight(expressions.Expression)}.
	 * @param type number type for setting up the test
	 * @param info Test infos
	 */
	@ParameterizedTest(name="PowerExpression<{0}>.setRight(Expression)")
	@MethodSource("valueClassesProvider")
	@DisplayName("void setRight(Expression)")
	final void testSetRight(Class<? extends Number> type, TestInfo info)
	{
		setupTestFor(type);
		String testName = info.getDisplayName();
		System.out.println(testName);

		if (type == Integer.class)
		{
			ConstantExpression<Integer> newNull = null;
			@SuppressWarnings("unchecked")
			PowerExpression<Integer> castedFilledExpression =
				(PowerExpression<Integer>) testFilledExpression;
			castedFilledExpression.setRight(newNull);
			assertThrows(IllegalStateException.class,
			             () -> {castedFilledExpression.value();},
			             testName + " failed with no exception thrown");

			Integer value = (Integer) altValuesMap.get(type);
			ConstantExpression<Integer> newRight = new ConstantExpression<Integer>(value);
			castedFilledExpression.setRight(newRight);
			Integer result = (Integer) evaluationsMap.get(type);
			assertEquals(result,
			             castedFilledExpression.value(),
			             testName + "[" + type.getSimpleName()
			                 + "] failed with wrong evaluation");

			@SuppressWarnings("unchecked")
			PowerExpression<Integer> castedEmptyExpression =
			    (PowerExpression<Integer>) testEmptyExpression;
			castedEmptyExpression.setRight(newRight);
			assertThrows(IllegalStateException.class,
			             () -> {castedEmptyExpression.value();},
			             testName + " failed with no exception thrown");

			@SuppressWarnings("unchecked")
			PowerExpression<Integer> castedNullExpression =
				(PowerExpression<Integer>) testNullExpression;
			castedNullExpression.setRight(newRight);
			assertThrows(IllegalStateException.class,
			             () -> {castedNullExpression.value();},
			             testName + " failed with no exception thrown");
		}
		if (type == Float.class)
		{
			ConstantExpression<Float> newNull = null;
			@SuppressWarnings("unchecked")
			PowerExpression<Float> castedFilledExpression =
				(PowerExpression<Float>) testFilledExpression;
			castedFilledExpression.setRight(newNull);
			assertThrows(IllegalStateException.class,
			             () -> {castedFilledExpression.value();},
			             testName + " failed with no exception thrown");

			Float value = (Float) altValuesMap.get(type);
			ConstantExpression<Float> newRight = new ConstantExpression<Float>(value);
			castedFilledExpression.setRight(newRight);
			Float result = (Float) evaluationsMap.get(type);
			assertEquals(result,
			             castedFilledExpression.value(),
			             testName + "[" + type.getSimpleName()
			                 + "] failed with wrong evaluation");

			@SuppressWarnings("unchecked")
			PowerExpression<Float> castedEmptyExpression =
			    (PowerExpression<Float>) testEmptyExpression;
			castedEmptyExpression.setRight(newRight);
			assertThrows(IllegalStateException.class,
			             () -> {castedEmptyExpression.value();},
			             testName + " failed with no exception thrown");

			@SuppressWarnings("unchecked")
			PowerExpression<Float> castedNullExpression =
				(PowerExpression<Float>) testNullExpression;
			castedNullExpression.setRight(newRight);
			assertThrows(IllegalStateException.class,
			             () -> {castedNullExpression.value();},
			             testName + " failed with no exception thrown");
		}
		if (type == Double.class)
		{
			ConstantExpression<Double> newNull = null;
			@SuppressWarnings("unchecked")
			PowerExpression<Double> castedFilledExpression =
				(PowerExpression<Double>) testFilledExpression;
			castedFilledExpression.setRight(newNull);
			assertThrows(IllegalStateException.class,
			             () -> {castedFilledExpression.value();},
			             testName + " failed with no exception thrown");

			Double value = (Double) altValuesMap.get(type);
			ConstantExpression<Double> newRight = new ConstantExpression<Double>(value);
			castedFilledExpression.setRight(newRight);
			Double result = (Double) evaluationsMap.get(type);
			assertEquals(result,
			             castedFilledExpression.value(),
			             testName + "[" + type.getSimpleName()
			                 + "] failed with wrong evaluation");

			@SuppressWarnings("unchecked")
			PowerExpression<Double> castedEmptyExpression =
			    (PowerExpression<Double>) testEmptyExpression;
			castedEmptyExpression.setRight(newRight);
			assertThrows(IllegalStateException.class,
			             () -> {castedEmptyExpression.value();},
			             testName + " failed with no exception thrown");

			@SuppressWarnings("unchecked")
			PowerExpression<Double> castedNullExpression =
				(PowerExpression<Double>) testNullExpression;
			castedNullExpression.setRight(newRight);
			assertThrows(IllegalStateException.class,
			             () -> {castedNullExpression.value();},
			             testName + " failed with no exception thrown");
		}
		if (type == BigDecimal.class)
		{
			ConstantExpression<BigDecimal> newNull = null;
			@SuppressWarnings("unchecked")
			PowerExpression<BigDecimal> castedFilledExpression =
				(PowerExpression<BigDecimal>) testFilledExpression;
			castedFilledExpression.setRight(newNull);
			assertThrows(IllegalStateException.class,
			             () -> {castedFilledExpression.value();},
			             testName + " failed with no exception thrown");

			BigDecimal value = (BigDecimal) altValuesMap.get(type);
			ConstantExpression<BigDecimal> newRight = new ConstantExpression<BigDecimal>(value);
			castedFilledExpression.setRight(newRight);
			assertThrows(UnsupportedOperationException.class,
			             () -> {castedFilledExpression.value();},
			             testName + " failed with no exception thrown");

			@SuppressWarnings("unchecked")
			PowerExpression<BigDecimal> castedEmptyExpression =
			    (PowerExpression<BigDecimal>) testEmptyExpression;
			castedEmptyExpression.setRight(newRight);
			assertThrows(IllegalStateException.class,
			             () -> {castedEmptyExpression.value();},
			             testName + " failed with no exception thrown");

			@SuppressWarnings("unchecked")
			PowerExpression<BigDecimal> castedNullExpression =
				(PowerExpression<BigDecimal>) testNullExpression;
			castedNullExpression.setRight(newRight);
			assertThrows(IllegalStateException.class,
			             () -> {castedNullExpression.value();},
			             testName + " failed with no exception thrown");
		}
	}

	/**
	 * Copy of an {@link PowerExpression}
	 * @param <E> The type of numbes in expressions
	 * @param expression The expression to copy
	 * @return a copy of the provide expression
	 */
	private static <E extends Number> PowerExpression<E> copyExpression(PowerExpression<E> expression)
	{
		return new PowerExpression<>(expression.getLeft(),
		                                expression.getRight());
	}

	/**
	 * Test method for {@link PowerExpression#equals(Object)}.
	 * @param type number type for setting up the test
	 * @param info Test infos
	 */
	@ParameterizedTest(name="PowerExpression<{0}>.equals(Object)")
	@MethodSource("valueClassesProvider")
	@DisplayName("equals(Object)")
	final void testEqualsObject(Class<? extends Number> type, TestInfo info)
	{
		setupTestFor(type);
		String testName = info.getDisplayName();
		System.out.println(testName);

		// Non equality to null
		assertNotEquals(null,
		                testFilledExpression,
		                testName + " failed with equality to null");
		assertNotEquals(null,
		                testEmptyExpression,
		                testName + " failed with equality to null");
		assertNotEquals(null,
		                testNullExpression,
		                testName + " failed with equality to null");

		// Equality to self
		assertEquals(testFilledExpression,
		             testFilledExpression,
		             testName + " failed with non equality to self");
		assertEquals(testEmptyExpression,
		             testEmptyExpression,
		             testName + " failed with non equality to self");
		assertEquals(testNullExpression,
		             testNullExpression,
		             testName + " failed with non equality to self");

		// Inequality between filled and unfilled
		assertNotEquals(testFilledExpression,
		                testEmptyExpression,
		                testName
		                    + " failed with equality between filled and unfilled");

		@SuppressWarnings("unchecked")
		PowerExpression<Number> copyNullExpression =
		    (PowerExpression<Number>) copyExpression(testNullExpression);
		assertEquals(testNullExpression,
		             copyNullExpression,
		             testName + " unexpected inequality with copy of null expression");

		@SuppressWarnings("unchecked")
		PowerExpression<Number> copyEmptyExpression =
		    (PowerExpression<Number>) copyExpression(testEmptyExpression);
		assertEquals(testEmptyExpression,
		             copyEmptyExpression,
		             testName + " unexpected inequality with copy of empty expression");

		@SuppressWarnings("unchecked")
		PowerExpression<Number> copyFilledExpression =
		    (PowerExpression<Number>) copyExpression(testFilledExpression);
		/*
		 * Caution : if type == BigDecimal comparing expressions number type
		 * will trigger a value() call which will fail with BigDecimal
		 */
		try
		{
			assertEquals(testFilledExpression,
			             copyFilledExpression,
			             testName + " unexpected inequality with copy of filled expression");
		}
		catch (UnsupportedOperationException e)
		{
			if (type != BigDecimal.class)
			{
				fail(testName + " unexpected comparison failure");
			}
		}

		// Equality to other with same content
		if (type == Integer.class)
		{

			// Equality with other with same content
			@SuppressWarnings("unchecked")
			PowerExpression<Integer> castedFilledExpression = (PowerExpression<Integer>) testFilledExpression;
			@SuppressWarnings("unchecked")
			PowerExpression<Integer> castedEmptyExpression = (PowerExpression<Integer>) testEmptyExpression;
			Integer value = (Integer)valuesMap.get(type);
			ConstantExpression<Integer> newLeft = new ConstantExpression<Integer>(value);
			castedEmptyExpression.setLeft(newLeft);
			assertEquals(castedFilledExpression,
			             castedEmptyExpression,
			             testName + "[" + type.getSimpleName()
		                 + "] failed with non equality to other with same "
			             	+ "content");

			// Inequality to other with different content
			value = (Integer)altValuesMap.get(type);
			newLeft = new ConstantExpression<Integer>(value);
			castedEmptyExpression.setLeft(newLeft);
			assertNotEquals(castedFilledExpression,
			                castedEmptyExpression,
			                testName + "[" + type.getSimpleName()
			                    + "] failed with equality to other with different "
			                    + "content");
			// Inequality to other with different type
			// ...
		}
		if (type == Float.class)
		{
			// Equality with other with same content
			@SuppressWarnings("unchecked")
			PowerExpression<Float> castedFilledExpression = (PowerExpression<Float>) testFilledExpression;
			@SuppressWarnings("unchecked")
			PowerExpression<Float> castedEmptyExpression = (PowerExpression<Float>) testEmptyExpression;
			Float value = (Float)valuesMap.get(type);
			ConstantExpression<Float> newLeft = new ConstantExpression<Float>(value);
			castedEmptyExpression.setLeft(newLeft);
			assertEquals(castedFilledExpression,
			             castedEmptyExpression,
			             testName + "[" + type.getSimpleName()
		                 + "] failed with non equality to other with same "
			             	+ "content");

			// Inequality to other with different content
			value = (Float)altValuesMap.get(type);
			newLeft = new ConstantExpression<Float>(value);
			castedEmptyExpression.setLeft(newLeft);
			assertNotEquals(castedFilledExpression,
			                castedEmptyExpression,
			                testName + "[" + type.getSimpleName()
			                    + "] failed with equality to other with different "
			                    + "content");
			// Inequality to other with different type
			// ...
		}
		if (type == Double.class)
		{
			// Equality with other with same content
			@SuppressWarnings("unchecked")
			PowerExpression<Double> castedFilledExpression = (PowerExpression<Double>) testFilledExpression;
			@SuppressWarnings("unchecked")
			PowerExpression<Double> castedEmptyExpression = (PowerExpression<Double>) testEmptyExpression;
			Double value = (Double)valuesMap.get(type);
			ConstantExpression<Double> newLeft = new ConstantExpression<Double>(value);
			castedEmptyExpression.setLeft(newLeft);
			assertEquals(castedFilledExpression,
			             castedEmptyExpression,
			             testName + "[" + type.getSimpleName()
		                 + "] failed with non equality to other with same "
			             	+ "content");

			// Inequality to other with different content
			value = (Double)altValuesMap.get(type);
			newLeft = new ConstantExpression<Double>(value);
			castedEmptyExpression.setLeft(newLeft);
			assertNotEquals(castedFilledExpression,
			                castedEmptyExpression,
			                testName + "[" + type.getSimpleName()
			                    + "] failed with equality to other with different "
			                    + "content");
			// Inequality to other with different type
			// ...
		}
		if (type == BigDecimal.class)
		{
			/*
			 * Equals will NEVER work on BigDecimal since it can't evaluate values
			 */
			// Equality with other with same content
			@SuppressWarnings("unchecked")
			PowerExpression<BigDecimal> castedFilledExpression = (PowerExpression<BigDecimal>) testFilledExpression;
			@SuppressWarnings("unchecked")
			PowerExpression<BigDecimal> castedEmptyExpression = (PowerExpression<BigDecimal>) testEmptyExpression;
			BigDecimal value = (BigDecimal)valuesMap.get(type);
			ConstantExpression<BigDecimal> newLeft = new ConstantExpression<BigDecimal>(value);
			castedEmptyExpression.setLeft(newLeft);
			try
			{
				assertEquals(castedFilledExpression,
				             castedEmptyExpression,
				             testName + "[" + type.getSimpleName()
			                 + "] failed with non equality to other with same "
				             	+ "content");
			}
			catch (UnsupportedOperationException e)
			{
				// Nothing: expected
			}

			// Inequality to other with different content
			value = (BigDecimal)altValuesMap.get(type);
			newLeft = new ConstantExpression<BigDecimal>(value);
			castedEmptyExpression.setLeft(newLeft);
			try
			{
				assertNotEquals(castedFilledExpression,
				                castedEmptyExpression,
				                testName + "[" + type.getSimpleName()
				                    + "] failed with equality to other with different "
				                    + "content");
			}
			catch (UnsupportedOperationException e)
			{
				// Nothing: expected
			}

			// Inequality to other with different type
			// ...
		}


	}

	/**
	 * Test method for {@link PowerExpression#toString()}.
	 * @param type number type for setting up the test
	 * @param info Test infos
	 */
	@ParameterizedTest(name="PowerExpression<{0}>.toString()")
	@MethodSource("valueClassesProvider")
	@DisplayName("toString()")
	final void testToString(Class<? extends Number> type, TestInfo info)
	{
		setupTestFor(type);
		String testName = info.getDisplayName();
		System.out.println(testName);

		String opString = BinaryOperatorRules.POWER.toString();
		String leftString = valuesMap.get(type).toString();
		String rightString = altValuesMap.get(type).toString();
		String space = new String(" ");

		StringBuilder sb = new StringBuilder();
		// Old version sb.append("? ");
		// New version
		sb.append(((VariableExpression<?>)testEmptyExpression.getLeft()).getName());
		sb.append(space);
		sb.append(opString);
		sb.append(space);
		sb.append(rightString);
		assertEquals(sb.toString(),
		             testEmptyExpression.toString(),
		             testName + " failed with wrong toString");

		sb = new StringBuilder();
		sb.append(leftString);
		sb.append(space);
		sb.append(opString);
		sb.append(space);
		sb.append(rightString);
		assertEquals(sb.toString(),
		             testFilledExpression.toString(),
		             testName + " failed with different strings");

		assertEquals(opString,
		             testNullExpression.toString(),
		             testName + " failed with different strings");
	}

	/**
	 * Test method for {@link PowerExpression#hashCode()}.
	 * @param type number type for setting up the test
	 * @param info Test infos
	 */
	@ParameterizedTest(name="PowerExpression<{0}>.hashCode()")
	@MethodSource("valueClassesProvider")
	@DisplayName("hashCode()")
	final void testHashCode(Class<? extends Number> type, TestInfo info)
	{
		setupTestFor(type);
		String testName = info.getDisplayName();
		System.out.println(testName);

		int expectedHash = testFilledExpression.toString().hashCode();

		assertEquals(expectedHash,
		             testFilledExpression.hashCode(),
		             testName + " failed with different hashcode");

		expectedHash = testEmptyExpression.toString().hashCode();
		assertEquals(expectedHash,
		             testEmptyExpression.hashCode(),
		             testName + " failed with different hashcode");

		expectedHash = testNullExpression.toString().hashCode();
		assertEquals(expectedHash,
		             testNullExpression.hashCode(),
		             testName + " failed with different hashcode");
	}
}
