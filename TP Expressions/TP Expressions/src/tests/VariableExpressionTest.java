package tests;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

import expressions.terminal.TerminalExpression;
import expressions.terminal.VariableExpression;

/**
 * Test class for {@link VariableExpression}
 * @author davidroussel
 */
@DisplayName("VariableExpression<E>")
public class VariableExpressionTest
{

	/**
	 * The filled expression to test
	 */
	private VariableExpression<? extends Number> testFilledExpression;

	/**
	 * The empty expression to test
	 */
	private VariableExpression<? extends Number> testEmptyExpression;

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
	 * List of all possible value types for expressions
	 */
	private static List<Class<? extends Number>> valueClassList = new Vector<Class<? extends Number>>();

	/**
	 * Array of all possible expressions (filled with values)
	 */
	private final static VariableExpression<? extends Number>[] filledExpressions =
		new VariableExpression<?>[valueClasses.length];
	/**
	 * Array of all possible expressions (NOT filled with values)
	 */
	private final static VariableExpression<? extends Number>[] emptyExpressions =
		new VariableExpression<?>[valueClasses.length];

	/**
	 * Map to get a filled expression from value type
	 * @see #setUpBeforeClass()
	 */
	private static Map<Class<? extends Number>, VariableExpression<? extends Number>> filledExpressionsMap =
	    new ConcurrentHashMap<Class<? extends Number>, VariableExpression<? extends Number>>();

	/**
	 * Map to get an empty expression from value type
	 * @see #setUpBeforeClass()
	 */
	private static Map<Class<? extends Number>, VariableExpression<? extends Number>> emptyExpressionsMap =
	    new ConcurrentHashMap<Class<? extends Number>, VariableExpression<? extends Number>>();

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
	 * Map get get values from type of values
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

			valueClassList.add(currentValueClass);
			valuesMap.put(currentValueClass, value);
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
		filledExpressionsMap.clear();
		emptyExpressionsMap.clear();
	}

	/**
	 * Check constructor and build instance
	 * @param type The type of Number contained in variable
	 * @param name the name of the variable (or null if no name is to be set)
	 * @param value the value of the variable (or null if no value is to be set)
	 * @param testName Name of the test where this method is used
	 * @return a {@link VariableExpression} instance of null if such instance
	 * could not be build.
	 */
	@SuppressWarnings("unchecked")
	private static VariableExpression<? extends Number>
	    buildVariable(Class<? extends Number> type,
	                  String name,
	                  Number value,
	                  String testName)
	{
		@SuppressWarnings("rawtypes")
		Constructor<VariableExpression> constructor = null;
		final int nbArgs = (name == null ? 0 : 1) + (value == null ? 0 : 1);
		Class<?>[] argumentTypes = new Class<?>[nbArgs];
		if (nbArgs > 0)
		{
			argumentTypes[0] = String.class;
		}
		if (nbArgs > 1)
		{
			argumentTypes[1] = Number.class;
		}

		try
		{
			constructor = VariableExpression.class.getConstructor(argumentTypes);
		}
		catch (NoSuchMethodException e)
		{
			fail(testName + " missing constructor " + type.getSimpleName() +
			    "(" + argumentTypes + ")" + e.getLocalizedMessage());
		}
		catch (SecurityException e)
		{
			fail(testName + " inaccessible constructor "
			    + e.getLocalizedMessage());
		}

		assertNotNull(constructor,
		              testName + " unexpected null valued constructor");

		Object[] arguments = new Object[nbArgs];
		if (nbArgs > 0)
		{
			arguments[0] = name;
		}
		if (nbArgs > 1)
		{
			arguments[1] = value;
		}
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

		return (VariableExpression<? extends Number>) instance;
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

		// Refil expressions cause they might have been modified
		for (int i = 0; i < valueClasses.length; i++)
		{
			Class<? extends Number> currentValueClass = valueClasses[i];
			int index = indexOf(currentValueClass);
			if (index == -1)
			{
				fail("Unknown index for " + currentValueClass.getSimpleName());
			}

			Number value = values[indexOf(currentValueClass)];
			String varName = names[indexOf(currentValueClass)];

			if (currentValueClass == Integer.class)
			{
				filledExpressions[index] = new VariableExpression<Integer>(varName, (Integer) value);
				emptyExpressions[index] = new VariableExpression<Integer>(varName);
			}
			else if (currentValueClass == Float.class)
			{
				filledExpressions[index] = new VariableExpression<Float>(varName, (Float) value);
				emptyExpressions[index] = new VariableExpression<Float>(varName);
			}
			else if (currentValueClass == Double.class)
			{
				filledExpressions[index] = new VariableExpression<Double>(varName, (Double) value);
				emptyExpressions[index] = new VariableExpression<Double>(varName);
			}
			else if (currentValueClass == BigDecimal.class)
			{
				filledExpressions[index] = new VariableExpression<BigDecimal>(varName, (BigDecimal) value);
				emptyExpressions[index] = new VariableExpression<BigDecimal>(varName);
			}
			else
			{
				index = 0; // safe but useless
				fail("Unknown expression type : " + currentValueClass.getSimpleName());
			}

			filledExpressionsMap.put(currentValueClass, filledExpressions[index]);
			emptyExpressionsMap.put(currentValueClass, emptyExpressions[index]);
		}

	}

	/**
	 * Tear down after each test
	 * @throws java.lang.Exception if tear down fails
	 */
	@AfterEach
	void tearDown() throws Exception
	{
		testFilledExpression = null;
//		testValue = null;
		testValueName = null;
	}
	/**
	 * Expression factory based on the value class
	 * @param valueClass the value class to use
	 * @param filled indeicates if the Varriable should be filled with value or not
	 * @return a new {@link VariableExpression} instance with the specified
	 * value class or null if the valueClass is not Integer, Float or Double
	 */
	private VariableExpression<? extends Number>
	    expressionFactory(Class<? extends Number> valueClass, boolean filled)
	{
		String varName = names[indexOf(valueClass)];
		if (valueClass == Integer.class)
		{
			if (filled)
			{
				return new VariableExpression<Integer>(varName, (Integer) valuesMap.get(valueClass));
			}
			else
			{
				return new VariableExpression<Integer>(varName);
			}
		}
		else if (valueClass == Float.class)
		{
			if (filled)
			{
				return new VariableExpression<Float>(varName, (Float) valuesMap.get(valueClass));
			}
			else
			{
				return new VariableExpression<Float>(varName);
			}
		}
		else if (valueClass == Double.class)
		{
			if (filled)
			{
				return new VariableExpression<Double>(varName, (Double) valuesMap.get(valueClass));
			}
			else
			{
				return new VariableExpression<Double>(varName);
			}
		}
		else if (valueClass == BigDecimal.class)
		{
			if (filled)
			{
				return new VariableExpression<BigDecimal>(varName, (BigDecimal) valuesMap.get(valueClass));
			}
			else
			{
				return new VariableExpression<BigDecimal>(varName);
			}
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
		testFilledExpression = filledExpressionsMap.get(type);
		testEmptyExpression = emptyExpressionsMap.get(type);
//		testValue = valuesMap.get(type);
		testValueName = type.getSimpleName();
	}

	/**
	 * Test method for {@link VariableExpression#VariableExpression(String)}.
	 * @param type number type for setting up the test
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("VariableExpression<Number>(String)")
	final void testVariableExpressionOfString(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("VariableExpression<" + testValueName
	    + ">(String)");
		System.out.println(testName);

		// Build variable with no name and no value : constructor inaccessible
		String name = "a";
		VariableExpression<? extends Number> variable = buildVariable(type, name, null, testName);
		assertNotNull(variable, testName + " unexpected null instance");
		assertEquals(name,
		             variable.getName(),
		             testName + " unexpected name");
		assertFalse(variable.hasValue(), testName + " unexpected value status");
		assertThrows(IllegalStateException.class, () -> {
			variable.value();
		});
	}

	/**
	 * Test method for {@link VariableExpression#VariableExpression(String, Number)}.
	 * @param type number type for setting up the test
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("VariableExpression<Number>(String, Number)")
	final void testVariableExpressionOfStringOfNumber(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("VariableExpression<" + testValueName
	    + ">(String, " + testValueName + ")");
		System.out.println(testName);

		// Build variable with no name and no value : constructor inaccessible
		Number value = null;
		if (type == Integer.class)
		{
			value = 2;
		}
		else if (type == Float.class)
		{
			value = 2.0f;
		}
		else if (type == Double.class)
		{
			value = 2.0;
		}
		else if (type == BigDecimal.class)
		{
			value = new BigDecimal(2);
		}
		else
		{
			fail(testName + " unknown type: " + type.getSimpleName());
		}
		String name = "a";
		VariableExpression<? extends Number> variable = buildVariable(type, name, value, testName);
		assertNotNull(variable, testName + " unexpected null instance");
		assertEquals(name,
		             variable.getName(),
		             testName + " unexpected name");
		assertTrue(variable.hasValue(), testName + " unexpected value status");
		assertEquals(value, variable.value(), testName + " unexpected value");


		assertNotNull(testFilledExpression, testName + " failed with null instance");

		testFilledExpression = expressionFactory(type, true);
		assertNotNull(testFilledExpression, testName + " failed with null instance");
	}

	/**
	 * Test method for {@link VariableExpression#VariableExpression(String)}
	 * @param type number type for setting up the test
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("VariableExpression<Number>(String)")
	final void testVariableExpressionOfStringOfValue(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("VariableExpression<" + testValueName
	    + ">(String)");
		System.out.println(testName);

		assertNotNull(testEmptyExpression, testName + " failed with null instance");

		testEmptyExpression = expressionFactory(type, false);
		assertNotNull(testEmptyExpression, testName + " failed with null instance");
	}

	/**
	 * Test method for {@link VariableExpression#getName()}.
	 * @param type number type for setting up the test
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("String getName()")
	final void testGetName(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("String getName() [" + type.getSimpleName() + "]");
		System.out.println(testName);

		assertNotNull(testFilledExpression.getName(),
		              testName + " failed with null name");
		assertNotNull(testEmptyExpression.getName(),
		              testName + " failed with null name");

		assertEquals(names[indexOf(type)],
		             testFilledExpression.getName(),
		             testName + " failed with wrong name");
		assertEquals(names[indexOf(type)],
		             testEmptyExpression.getName(),
		             testName + " failed with wrong name");

		testFilledExpression = expressionFactory(type, true);
		assertEquals(names[indexOf(type)],
		             testFilledExpression.getName(),
		             testName + " failed with wrong name");

		testEmptyExpression = expressionFactory(type, false);
		assertEquals(names[indexOf(type)],
		             testEmptyExpression.getName(),
		             testName + " failed with wrong name");
	}

	/**
	 * Test method for {@link VariableExpression#hasValue()}.
	 * @param type number type for setting up the test
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("boolean hasValue()")
	final void testHasValue(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("boolean canEvaluate() [" + type.getSimpleName() + "]");
		System.out.println(testName);

		assertTrue(testFilledExpression.hasValue(),
		           testName + " failed with non evaluable expression");
		assertFalse(testEmptyExpression.hasValue(),
		            testName + " failed with evaluable expression");

		testFilledExpression = expressionFactory(type, true);
		assertTrue(testFilledExpression.hasValue(),
		           testName + " failed with non evaluable expression");
		testEmptyExpression = expressionFactory(type, false);
		assertFalse(testEmptyExpression.hasValue(),
		            testName + " failed with evaluable expression");
	}

	/**
	 * Test method for {@link VariableExpression#value()}.
	 * @param type number type for setting up the test
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("Number value()")
	final void testValue(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("Number evaluate() [" + type.getSimpleName() + "]");
		System.out.println(testName);

		assertEquals(valuesMap.get(type),
		             testFilledExpression.value(),
		             testName + " failed with wrong evaluation");

		testFilledExpression = expressionFactory(type, true);

		assertEquals(valuesMap.get(type),
		             testFilledExpression.value(),
		             testName + " failed with wrong evaluation");

		assertThrows(IllegalStateException.class,
		             () -> {testEmptyExpression.value();},
		             testName + " failed with no exception thrown");

		testEmptyExpression = expressionFactory(type, false);

		assertThrows(IllegalStateException.class,
		             () -> {testEmptyExpression.value();},
		             testName + " failed with no exception thrown");
	}

	/**
	 * Test method for {@link VariableExpression#setValue(Number)}.
	 * @param type number type for setting up the test
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("void setValue(Number)")
	final void testSetValueOfNumber(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("void setValue("+type.getSimpleName()+")");
		System.out.println(testName);

		if (type == Integer.class)
		{
			Integer value = (Integer) valuesMap.get(type);
			@SuppressWarnings("unchecked")
			VariableExpression<Integer> castedExpression =
			    (VariableExpression<Integer>) testEmptyExpression;
			castedExpression.setValue(value);
			assertEquals(value,
			             castedExpression.value(),
			             testName + "[" + type.getSimpleName()
			                 + "] failed with wrong evaluation");
			final Integer nullValue = null;
			assertThrows(NullPointerException.class,
			             () -> {castedExpression.setValue(nullValue);},
			             testName + " failed with no exception thrown");

		}
		if (type == Float.class)
		{
			Float value = (Float) valuesMap.get(type);
			@SuppressWarnings("unchecked")
			VariableExpression<Float> castedExpression =
			    (VariableExpression<Float>) testEmptyExpression;
			castedExpression.setValue(value);
			assertEquals(value,
			             castedExpression.value(),
			             testName + "[" + type.getSimpleName()
			                 + "] failed with wrong evaluation");
			final Float nullValue = null;
			assertThrows(NullPointerException.class,
			             () -> {castedExpression.setValue(nullValue);},
			             testName + " failed with no exception thrown");
		}
		if (type == Double.class)
		{
			Double value = (Double) valuesMap.get(type);
			@SuppressWarnings("unchecked")
			VariableExpression<Double> castedExpression =
			    (VariableExpression<Double>) testEmptyExpression;
			castedExpression.setValue(value);
			assertEquals(value,
			             castedExpression.value(),
			             testName + "[" + type.getSimpleName()
			                 + "] failed with wrong evaluation");
			final Double nullValue = null;
			assertThrows(NullPointerException.class,
			             () -> {castedExpression.setValue(nullValue);},
			             testName + " failed with no exception thrown");

		}
		if (type == BigDecimal.class)
		{
			BigDecimal value = (BigDecimal) valuesMap.get(type);
			@SuppressWarnings("unchecked")
			VariableExpression<BigDecimal> castedExpression =
			    (VariableExpression<BigDecimal>) testEmptyExpression;
			castedExpression.setValue(value);
			assertEquals(value,
			             castedExpression.value(),
			             testName + "[" + type.getSimpleName()
			                 + "] failed with wrong evaluation");
			final BigDecimal nullValue = null;
			assertThrows(NullPointerException.class,
			             () -> {castedExpression.setValue(nullValue);},
			             testName + " failed with no exception thrown");

		}
	}

	/**
	 * Test method for {@link VariableExpression#setValue(TerminalExpression)}.
	 * @param type number type for setting up the test
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("void setValue(TerminalExpression<Number>)")
	final void testSetValueOfTerminalExpression(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("void setValue(TerminalExpression<" + type.getSimpleName() + ">)");
		System.out.println(testName);


		if (type == Integer.class)
		{
			Integer value = (Integer) valuesMap.get(type);
			@SuppressWarnings("unchecked")
			VariableExpression<Integer> castedEmptyExpression =
			    (VariableExpression<Integer>) testEmptyExpression;

			@SuppressWarnings("unchecked")
			VariableExpression<Integer> castedFilleExpression =
				(VariableExpression<Integer>) testFilledExpression;

			assertFalse(castedFilleExpression.setValue(castedEmptyExpression),
			            testName + "[" + type.getSimpleName()
		                 + "] failed with filled set to empty");

			assertTrue(castedEmptyExpression.setValue(castedFilleExpression),
			           testName + "[" + type.getSimpleName()
		                 + "] failed with empty not set to filled");
			assertEquals(value,
			             castedEmptyExpression.value(),
			             testName + "[" + type.getSimpleName()
			                 + "] failed with wrong evaluation");
			final TerminalExpression<Integer> nullExpr = null;
			assertFalse(castedEmptyExpression.setValue(nullExpr),
			            testName + "[" + type.getSimpleName()
			                + "] failed with true");

		}
		if (type == Float.class)
		{
			Float value = (Float) valuesMap.get(type);
			@SuppressWarnings("unchecked")
			VariableExpression<Float> castedEmptyExpression =
			    (VariableExpression<Float>) testEmptyExpression;

			@SuppressWarnings("unchecked")
			VariableExpression<Float> castedFilleExpression =
				(VariableExpression<Float>) testFilledExpression;

			assertFalse(castedFilleExpression.setValue(castedEmptyExpression),
			            testName + "[" + type.getSimpleName()
		                 + "] failed with filled set to empty");

			assertTrue(castedEmptyExpression.setValue(castedFilleExpression),
			           testName + "[" + type.getSimpleName()
		                 + "] failed with empty not set to filled");
			assertEquals(value,
			             castedEmptyExpression.value(),
			             testName + "[" + type.getSimpleName()
			                 + "] failed with wrong evaluation");
			final TerminalExpression<Float> nullExpr = null;
			assertFalse(castedEmptyExpression.setValue(nullExpr),
			            testName + "[" + type.getSimpleName()
			                + "] failed with true");
		}
		if (type == Double.class)
		{
			Double value = (Double) valuesMap.get(type);
			@SuppressWarnings("unchecked")
			VariableExpression<Double> castedEmptyExpression =
			    (VariableExpression<Double>) testEmptyExpression;

			@SuppressWarnings("unchecked")
			VariableExpression<Double> castedFilleExpression =
				(VariableExpression<Double>) testFilledExpression;

			assertFalse(castedFilleExpression.setValue(castedEmptyExpression),
			            testName + "[" + type.getSimpleName()
		                 + "] failed with filled set to empty");

			assertTrue(castedEmptyExpression.setValue(castedFilleExpression),
			           testName + "[" + type.getSimpleName()
		                 + "] failed with empty not set to filled");
			assertEquals(value,
			             castedEmptyExpression.value(),
			             testName + "[" + type.getSimpleName()
			                 + "] failed with wrong evaluation");
			final TerminalExpression<Double> nullExpr = null;
			assertFalse(castedEmptyExpression.setValue(nullExpr),
			            testName + "[" + type.getSimpleName()
			                + "] failed with true");
		}
		if (type == BigDecimal.class)
		{
			BigDecimal value = (BigDecimal) valuesMap.get(type);
			@SuppressWarnings("unchecked")
			VariableExpression<BigDecimal> castedEmptyExpression =
			    (VariableExpression<BigDecimal>) testEmptyExpression;

			@SuppressWarnings("unchecked")
			VariableExpression<BigDecimal> castedFilleExpression =
				(VariableExpression<BigDecimal>) testFilledExpression;

			assertFalse(castedFilleExpression.setValue(castedEmptyExpression),
			            testName + "[" + type.getSimpleName()
		                 + "] failed with filled set to empty");

			assertTrue(castedEmptyExpression.setValue(castedFilleExpression),
			           testName + "[" + type.getSimpleName()
		                 + "] failed with empty not set to filled");
			assertEquals(value,
			             castedEmptyExpression.value(),
			             testName + "[" + type.getSimpleName()
			                 + "] failed with wrong evaluation");
			final TerminalExpression<BigDecimal> nullExpr = null;
			assertFalse(castedEmptyExpression.setValue(nullExpr),
			            testName + "[" + type.getSimpleName()
			                + "] failed with true");
		}
	}

	// TODO add test for contains(expr) method

	/**
	 * Test method for {@link VariableExpression#equals(java.lang.Object)}.
	 * @param type number type for setting up the test
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("boolean equals(Object)")
	final void testEqualsObject(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("boolean equals(Object) [" + type.getSimpleName() + "]");
		System.out.println(testName);

		// Non equality to null
		assertNotEquals(null,
		                testFilledExpression,
		                testName + " failed with equality to null");
		assertNotEquals(null,
		                testEmptyExpression,
		                testName + " failed with equality to null");

		// Equality to self
		assertEquals(testFilledExpression,
		             testFilledExpression,
		             testName + " failed with non equality to self");
		assertEquals(testEmptyExpression,
		             testEmptyExpression,
		             testName + " failed with non equality to self");

		// Equality between filled and unfilled since only name matters
		assertEquals(testFilledExpression,
		             testEmptyExpression,
		             testName + " failed with inequality between filled and "
		             	+ "unfilled");

		// Equality to other with same content
		VariableExpression<? extends Number> other = expressionFactory(type, true);
		assertEquals(other,
		             testFilledExpression,
		             testName + " failed with non equality to other with same "
		             	+ "content");

		// Equality with other with no content
		other = expressionFactory(type, false);
		assertEquals(other,
		             testEmptyExpression,
		             testName + " failed with inequality to other with same "
		                 + "content but unevaluable");

		// Equality to other with different content (since only name matters)
		other = new VariableExpression<Number>(names[indexOf(type)],
		                                       altValues[indexOf(type)]);
		assertEquals(other,
		             testFilledExpression,
		             testName + " failed with inequality to other with "
		             	+ "different content");

		// Inequality to other with different type
		other = new VariableExpression<Number>(names[indexOf(type)],
		                                       altValues[(indexOf(type) + 1)
		                                           % altValues.length]);
		assertNotEquals(other,
		                testFilledExpression,
		                testName + " failed with equality to other with different "
		                    + "type");
	}

	/**
	 * Test method for {@link VariableExpression#toString()}.
	 * @param type number type for setting up the test
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("String toString()")
	final void testToString(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("String toString() [" + type.getSimpleName() + "]");
		System.out.println(testName);

		assertEquals(testEmptyExpression.getName(),
		             testEmptyExpression.toString(),
		             testName + " failed with wrong toString");

//		String expected = new String(testFilledExpression.getName() + "("
//		    + valuesMap.get(type).toString() + ")");
		String expected = new String(testFilledExpression.getName());
		assertEquals(expected,
		             testFilledExpression.toString(),
		             testName + " failed with different strings");
	}

	/**
	 * Test method for {@link VariableExpression#hashCode()}.
	 * @param type number type for setting up the test
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("int hashCode()")
	final void testHashCode(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("int hashCode() [" + type.getSimpleName() + "]");
		System.out.println(testName);

		assertEquals(testFilledExpression.toString().hashCode(),
		             testFilledExpression.hashCode(),
		             testName + " failed with different hashcode");
	}

}
