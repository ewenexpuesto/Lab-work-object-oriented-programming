package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import expressions.terminal.ConstantExpression;

/**
 * Test class for {@link ConstantExpression}
 * @author davidroussel
 */
public class ConstantExpressionTest
{

	/**
	 * The expression to test
	 */
	private ConstantExpression<? extends Number> testExpression;

//	/**
//	 * The value to set
//	 */
//	private Number testValue;

	/**
	 * Name of the value
	 */
	private String testValueName = null;

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
	 * Array of all possible expressions
	 */
	private final static ConstantExpression<? extends Number>[] expressions =
		new ConstantExpression<?>[valueClasses.length];

	/**
	 * Mapt to get an expression from the value type
	 * @see #setUpBeforeClass()
	 */
	private static Map<Class<? extends Number>, ConstantExpression<? extends Number>> expressionsMap =
	    new ConcurrentHashMap<Class<? extends Number>, ConstantExpression<? extends Number>>();

	/**
	 * Values to set
	 */
	private final static Number[] values = new Number[] {
		Integer.valueOf(2),
		Float.valueOf(2.0f),
		Double.valueOf(2.0),
		BigDecimal.valueOf(2)
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
	 * Map to get values from type of values
	 */
	private static Map<Class<? extends Number>, Number> valuesMap =
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

			if (currentValueClass == Integer.class)
			{
				expressions[index] = new ConstantExpression<Integer>((Integer) value);
			}
			else if (currentValueClass == Float.class)
			{
				expressions[index] = new ConstantExpression<Float>((Float) value);
			}
			else if (currentValueClass == Double.class)
			{
				expressions[index] = new ConstantExpression<Double>((Double) value);
			}
			else if (currentValueClass == BigDecimal.class)
			{
				expressions[index] = new ConstantExpression<BigDecimal>((BigDecimal) value);
			}
			else
			{
				index = 0; // safe but useless
				fail("Unknown expression type : " + currentValueClass.getSimpleName());
			}

			valueClassList.add(currentValueClass);
			valuesMap.put(currentValueClass, value);
			expressionsMap.put(currentValueClass, expressions[index]);
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
		expressionsMap.clear();
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
	 * Tear down after each test
	 * @throws java.lang.Exception if tear down fails
	 */
	@AfterEach
	void tearDown() throws Exception
	{
		testExpression = null;
//		testValue = null;
		testValueName = null;
	}
	/**
	 * Expression factory based on the value class
	 * @param valueClass the value class to use
	 * @return a new {@link ConstantExpression} instance with the specified
	 * value class or null if the valueClass is not Integer, Float or Double
	 */
	private ConstantExpression<? extends Number>
	    expressionFactory(Class<? extends Number> valueClass)
	{
		if (valueClass == Integer.class)
		{
			return new ConstantExpression<Integer>((Integer) valuesMap.get(valueClass));
		}
		else if (valueClass == Float.class)
		{
			return new ConstantExpression<Float>((Float) valuesMap.get(valueClass));
		}
		else if (valueClass == Double.class)
		{
			return new ConstantExpression<Double>((Double) valuesMap.get(valueClass));
		}
		else if (valueClass == BigDecimal.class)
		{
			return new ConstantExpression<BigDecimal>((BigDecimal) valuesMap.get(valueClass));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Setup expressions for testing with a specific Number type
	 * @param type the number type
	 */
	private void setupTestFor(Class<? extends Number> type)
	{
		testExpression = expressionsMap.get(type);
//		testValue = valuesMap.get(type);
		testValueName = type.getSimpleName();
	}

	/**
	 * Test method for {@link ConstantExpression#ConstantExpression(Number)}.
	 * @param type number type for setting up the test
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("ConstantExpression<>(Number)")
	final void testConstantExpression(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("ConstantExpression<" + testValueName
	    + ">(" + testValueName + ")");
		System.out.println(testName);

		assertNotNull(testExpression, testName + " failed with null instance");

		testExpression = expressionFactory(type);
		assertNotNull(testExpression, testName + " failed with null instance");
	}

	/**
	 * Test method for {@link ConstantExpression#ConstantExpression(Number)}.
	 * @param type number type for setting up the test
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("getConstant<Number>(Number)")
	final void testGetConstant(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("ConstantExpression<" + testValueName
	    + ">(" + testValueName + ")");
		System.out.println(testName);

		ConstantExpression<? extends Number> constant = null;
		Number expectedValue = null;
		if (type == Integer.class)
		{
			expectedValue = 2;
			constant = ConstantExpression.<Integer>getConstant(2);
		}
		else if (type == Float.class)
		{
			expectedValue = 2.0f;
			constant = ConstantExpression.<Float>getConstant(2.0f);
		}
		else if (type == Double.class)
		{
			expectedValue = 2.0;
			constant = ConstantExpression.<Double>getConstant(2.0);
		}
		else if (type == BigDecimal.class)
		{
			expectedValue = new BigDecimal(2);
			constant = ConstantExpression.<BigDecimal>getConstant(new BigDecimal(2));
		}
		else
		{
			fail(testName + " unexpected number type " + type.getSimpleName());
		}

		assertNotNull(constant, testName + " failed with null instance");
		assertTrue(constant.hasValue(), testName + " unexpected value status");
		try
		{
			assertEquals(expectedValue,
			             constant.value(),
			             testName + " unexpected value");
		}
		catch (IllegalStateException e)
		{
			fail(testName + " unexpected exception " + e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for {@link ConstantExpression#hasValue()}.
	 * @param type number type for setting up the test
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("hasValue()")
	final void testHasValue(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("hasValue() [" + type.getSimpleName() + "]");
		System.out.println(testName);

		assertTrue(testExpression.hasValue(),
		           testName + " failed with non evaluable expression");

		testExpression = expressionFactory(type);
		assertTrue(testExpression.hasValue(),
		           testName + " failed with non evaluable expression");
	}

	/**
	 * Test method for {@link ConstantExpression#value()}.
	 * @param type number type for setting up the test
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("value()")
	final void testEvaluate(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("value() [" + type.getSimpleName() + "]");
		System.out.println(testName);

		assertEquals(valuesMap.get(type),
		             testExpression.value(),
		             testName + " failed with wrong evaluation");

		testExpression = expressionFactory(type);

		assertEquals(valuesMap.get(type),
		             testExpression.value(),
		             testName + " failed with wrong evaluation");
	}

	// TODO add test for contains(expr) method

	/**
	 * Test method for {@link ConstantExpression#equals(java.lang.Object)}.
	 * @param type number type for setting up the test
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("equals(Object)")
	final void testEqualsObject(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("boolean equals(Object) [" + type.getSimpleName() + "]");
		System.out.println(testName);

		// Non equality to null
		assertNotEquals(null,
		                testExpression,
		                testName + " failed with equality to null");

		// Equality to self
		assertEquals(testExpression,
		             testExpression,
		             testName + " failed with non equality to self");

		// Equality to other with same content
		ConstantExpression<? extends Number> other = expressionFactory(type);
		assertEquals(other,
		             testExpression,
		             testName + " failed with non equality to other with same "
		             	+ "content");

		// Inequality to other with different content
		other = new ConstantExpression<Number>(altValues[indexOf(type)]);
		assertNotEquals(other,
		                testExpression,
		                testName + " failed with equality to other with different "
		                    + "content");

		// Inequality to other with different type
		other = new ConstantExpression<Number>(altValues[(indexOf(type) + 1)%altValues.length]);
		assertNotEquals(other,
		                testExpression,
		                testName + " failed with equality to other with different "
		                    + "type");
	}

	/**
	 * Test method for {@link ConstantExpression#toString()}.
	 * @param type number type for setting up the test
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("toString()")
	final void testToString(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("String toString() [" + type.getSimpleName() + "]");
		System.out.println(testName);

		assertEquals(valuesMap.get(type).toString(),
		             testExpression.toString(),
		             testName + " failed with different strings");
	}

	/**
	 * Test method for {@link ConstantExpression#hashCode()}.
	 * @param type number type for setting up the test
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("hashCode()")
	final void testHashCode(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("int hashCode() [" + type.getSimpleName() + "]");
		System.out.println(testName);

		int expectedHash = testExpression.toString().hashCode();

		assertEquals(expectedHash,
		             testExpression.hashCode(),
		             testName + " failed with different hashcode");
	}

}
