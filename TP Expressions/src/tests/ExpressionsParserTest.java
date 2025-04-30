package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import expressions.Expression;
import expressions.binary.AdditionExpression;
import expressions.binary.AssignmentExpression;
import expressions.terminal.ConstantExpression;
import expressions.terminal.VariableExpression;
import parser.ExpressionParser;
import parser.exceptions.IllegalAssignmentException;
import parser.exceptions.IllegalPostParsingStateException;
import parser.exceptions.MissingRightOperandException;
import parser.exceptions.MultipleAssignmentsException;
import parser.exceptions.ParserException;
import parser.exceptions.UnfinishedSubContextException;
import parser.exceptions.UnknownTokenException;
import parser.exceptions.UnsupportedUnaryOperatorException;

/**
 * Test class for {@link ExpressionParser}
 * @author davidroussel
 */
public class ExpressionsParserTest
{

	/**
	 * Value natures to test
	 */
	@SuppressWarnings("unchecked")
	private final static Class<? extends Number>[] valueClasses =
	    (Class<? extends Number>[]) new Class<?>[] {
		Integer.class,
		Float.class,
		Double.class
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
		else
		{
			return -1;
		}
	}

	/**
	 * List of all possible types of expressions
	 */
	private static List<Class<? extends Number>> valueClassList =
	    new Vector<Class<? extends Number>>();

	/**
	 * Valid Integer contexts
	 */
	private static String[] validIntegerContexts = new String[]{
		// Context 0 --> 1 expression
		"-2 + 3 * 4",
		// Context 1 --> 3 expressions
		"r = 2; pi = 3; c = 4/3; c * pi * r ^3",
		// Context 2 -->  7 expressions
		"c=1; 2 - 3 / (a * b) + 1; a = 5; a + b^3; 1 + (2 - 3 / (a * b)); (c + 2 - 3) / (a * b);b=2"
	};

	/**
	 * Valid Float contexts
	 */
	private static String[] validFloatContexts = new String[]{
		// Context 0 --> 1 expression
		"-2.35 + 3.12 * 4.23",
		// Context 1 --> 3 expressions
		"r = 2; pi = 3.14159; c = 4.0/3; c * pi * r ^3",
		// Context 2 -->  7 expressions
		"c=1.1; 2.2 - 3.3 / (a * b) + 1.1; a = 5.5; a + b^3; 1.1 + (2.2 - 3.3 / (a * b)); (c + 2.2 - 3.3) / (a * b);b=2.2"
	};

	/**
	 * Valid Double contexts
	 */
	private static String[] validDoubleContexts = validFloatContexts;

	/**
	 * Valid contexts
	 */
	private static String[][] validContextsArray = new String[][] {
		validIntegerContexts,
		validFloatContexts,
		validDoubleContexts
	};

	/**
	 * Valid contexts for a specific number type
	 */
	private String[] validContexts = null;

	/**
	 * Valid expressions expected from parsing {@link #validContexts}.
	 * 1st dimension is the number of contexts in {@link #validContexts}
	 * 2nd dimension is the number of resulting expressions
	 * @see #buildValidExpressions(Class, int)
	 */
	private List<Expression<? extends Number>>[] validExpressions = null;

	/**
	 * Invalid Integer contexts
	 */
	private static String[] invalidIntegerContexts = new String[]{
		"a + b = 2",	// Triggering IllegalAssignmentException
		"* b",			// Triggering UnsupportedUnaryOperatorException
		"a / ",			// Triggering UnsupportedUnaryOperatorException
		"--a",			// Triggering MissingRightOperandException
		"a b",			// Triggering IllegalPostParsingStateException
		"a ** b",		// Triggering UnsupportedUnaryOperatorException
		"a ! b",		// Triggering UnknownTokenException
		"a = b = c",	// Triggering MultipleAssignmentsException
		"a = a + b",	// Triggering IllegalAssignmentException
		"a+((b-c)*d",	// Triggering UnfinishedSubContextException
		"a = 2.35"		// Triggering UnknownTokenException in Integer expressions only
	};

	/**
	 * Expected exceptions for {@link #invalidIntegerContexts} or
	 * {@link #invalidFloatContexts} or {@link #invalidDoubleContexts}
	 */
	@SuppressWarnings("unchecked")
	private static Class<? extends ParserException>[] invalidContextsExceptions =
		(Class<? extends ParserException>[]) new Class<?>[] {
		IllegalAssignmentException.class,			// "a + b = 2"
		UnsupportedUnaryOperatorException.class,	// "* b"
		UnsupportedUnaryOperatorException.class,	// "a / "
		MissingRightOperandException.class,			// "--a"
		IllegalPostParsingStateException.class,		// "a b"
		UnsupportedUnaryOperatorException.class,	// "a ** b"
		UnknownTokenException.class,				// "a ! b"
		MultipleAssignmentsException.class,			// "a = b = c"
		IllegalAssignmentException.class,			// "a = a + b"
		UnfinishedSubContextException.class,		// "a+((b-c)*d"
		UnknownTokenException.class					// "a = 2.35"
	};

	/**
	 * Invalid Float contexts
	 */
	private static String[] invalidFloatContexts = invalidIntegerContexts;

	/**
	 * Invalid Double contexts
	 */
	private static String[] invalidDoubleContexts = invalidFloatContexts;

	/**
	 * Invalid contexts
	 */
	private static String[][] invalidContextsArray = new String[][] {
		invalidIntegerContexts,
		invalidFloatContexts,
		invalidDoubleContexts
	};

	/**
	 * Current Invalid contexts
	 */
	private String[] invalidContexts = null;

	/**
	 * Builds valid expressions expected from parsing {@link #validContexts}
	 * @param type the type of numbers used in expressions
	 * @param index the index of the expression according to valid contexts
	 * @return a valid expression
	 */
	private static List<Expression<? extends Number>>
	    buildValidExpressions(Class<? extends Number> type, int index)
	{
		int classIndex = indexOf(type);
		List<Expression<? extends Number>> expressions = new ArrayList<>();
		switch (classIndex)
		{
			case 0: // Integer
			{
				switch (index)
				{
					case 0: // -2 + 3 * 4
					{
						AdditionExpression<Integer> root0 = new AdditionExpression<>();
						// TODO uncomment when SubtractionExpression is ready
//						SubtractionExpression<Integer> leftSub = new SubtractionExpression<>();
						// TODO uncomment when MultiplicationExpression is ready
//						MultiplicationExpression<Integer> rightMult = new MultiplicationExpression<>();
//						leftSub.setLeft(new ConstantExpression<Integer>(0));
//						leftSub.setRight(new ConstantExpression<Integer>(2));
//						rightMult.setLeft(new ConstantExpression<Integer>(3));
//						rightMult.setRight(new ConstantExpression<Integer>(4));
//						root0.setLeft(leftSub);
//						root0.setRight(rightMult);
						expressions.add(root0);
						break;
					}
					case 1: // r = 2; pi = 3; c = 4/3; c * pi * r ^3
					{
						AssignmentExpression<Integer> root0 = new AssignmentExpression<>();
						VariableExpression<Integer> r = new VariableExpression<>("r");
						ConstantExpression<Integer> two = new ConstantExpression<Integer>(2);
						root0.setLeft(r);
						root0.setRight(two);
						root0.value();
						expressions.add(root0);

						AssignmentExpression<Integer> root1 = new AssignmentExpression<>();
						VariableExpression<Integer> pi = new VariableExpression<>("pi");
						ConstantExpression<Integer> piVal = new ConstantExpression<Integer>(3);
						root1.setLeft(pi);
						root1.setRight(piVal);
						root1.value();
						expressions.add(root1);

						AssignmentExpression<Integer> root2 = new AssignmentExpression<>();
						VariableExpression<Integer> c = new VariableExpression<>("c");
						// TODO uncomment when DivisionExpression is ready
//						DivisionExpression<Integer> d43 = new DivisionExpression<>();
						ConstantExpression<Integer> c4 = new ConstantExpression<Integer>(4);
						ConstantExpression<Integer> c3 = new ConstantExpression<Integer>(3);
//						d43.setLeft(c4);
//						d43.setRight(c3);
						root2.setLeft(c);
//						root2.setRight(d43);
						root2.value();
						expressions.add(root2);

						// TODO uncomment when MultiplicationExpression is ready
//						MultiplicationExpression<Integer> root3 = new MultiplicationExpression<>();
//						MultiplicationExpression<Integer> mcpi = new MultiplicationExpression<>();
//						mcpi.setLeft(c);
//						mcpi.setRight(pi);
//						root3.setLeft(mcpi);
						// TODO uncomment when PowerExpression is ready
//						PowerExpression<Integer> r3 = new PowerExpression<>();
//						r3.setLeft(r);
//						r3.setRight(c3);
//						root3.setRight(r3);
//						expressions.add(root3);
						break;
					}
					case 2: //  "c=1; 2 - 3 / (a * b) + 1; a = 5; a + b^3; 1 + (2 - 3 / (a * b)); (c + 2 - 3) / (a * b);b=2"
					{
						// c = 1
						AssignmentExpression<Integer> root4 = new AssignmentExpression<>(
							new VariableExpression<>("c"),
							new ConstantExpression<Integer>(1));
						expressions.add(root4);
						// 2 - 3 / (a * b) + 1
						// uncomment when SubtractionExpression is ready
//						AdditionExpression<Integer> root5 = new AdditionExpression<>(
//							new SubtractionExpression<Integer>(
//								new ConstantExpression<Integer>(2),
//								new DivisionExpression<Integer>(
//									new ConstantExpression<Integer>(3),
//									new MultiplicationExpression<>(
//										new VariableExpression<Integer>("a"),
//										new VariableExpression<Integer>("b")))),
//							new ConstantExpression<Integer>(1));
//						expressions.add(root5);
						// a = 5
						AssignmentExpression<Integer> root6 = new AssignmentExpression<>(
							new VariableExpression<>("a"),
							new ConstantExpression<Integer>(5));
						expressions.add(root6);
						// a + b^3
						// TODO uncoomment when PowerExpression is ready
//						AdditionExpression<Integer> root7 = new AdditionExpression<>(
//							new VariableExpression<Integer>("a"),
//							new PowerExpression<Integer>(
//								new VariableExpression<Integer>("b"),
//								new ConstantExpression<Integer>(3)));
//						expressions.add(root7);
						// 1 + (2 - 3 / (a * b))
						// TODO uncomment when SubtractionExpression and MultiplicationExpression are ready
//						AdditionExpression<Integer> root8 = new AdditionExpression<>(
//							new ConstantExpression<Integer>(1),
//							new SubtractionExpression<Integer>(
//								new ConstantExpression<Integer>(2),
//								new DivisionExpression<Integer>(
//									new ConstantExpression<Integer>(3),
//									new MultiplicationExpression<Integer>(
//										new VariableExpression<Integer>("a"),
//										new VariableExpression<Integer>("b")))));
//						expressions.add(root8);
						// (c + 2 - 3) / (a * b)
						// TODO uncomment when DivisionExpression, SubtractionExpression & MultiplicationExpression are ready
//						DivisionExpression<Integer> root9 = new DivisionExpression<>(
//							new SubtractionExpression<Integer>(
//								new AdditionExpression<Integer>(
//									new VariableExpression<Integer>("c"),
//									new ConstantExpression<Integer>(2)),
//								new ConstantExpression<Integer>(3)),
//							new MultiplicationExpression<Integer>(
//								new VariableExpression<Integer>("a"),
//								new VariableExpression<Integer>("b")));
//						expressions.add(root9);
						// b=2
						AssignmentExpression<Integer> root10 = new AssignmentExpression<>(
							new VariableExpression<>("b"),
							new ConstantExpression<Integer>(2));
						expressions.add(root10);
						break;
					}
					default:
						break;
				}
				break;
			}
			case 1: // Float
			{
				switch (index)
				{
					case 0: // -2.35 + 3.12 * 4.23
					{
						AdditionExpression<Float> root = new AdditionExpression<>();
						// TODO uncomment when SubtractionExpression is ready
//						SubtractionExpression<Float> left = new SubtractionExpression<>();
						// TODO uncomment when MultiplicationExpression is ready
//						MultiplicationExpression<Float> right = new MultiplicationExpression<>();
//						left.setLeft(new ConstantExpression<Float>(0.0f));
//						left.setRight(new ConstantExpression<Float>(2.35f));
//						right.setLeft(new ConstantExpression<Float>(3.12f));
//						right.setRight(new ConstantExpression<Float>(4.23f));
//						root.setLeft(left);
//						root.setRight(right);
						expressions.add(root);
						break;
					}
					case 1: // r = 2; pi = 3.14159; c = 4.0/3; c * pi * r ^3
					{
						AssignmentExpression<Float> root0 = new AssignmentExpression<>();
						VariableExpression<Float> r = new VariableExpression<>("r");
						ConstantExpression<Float> two = new ConstantExpression<Float>(2.0f);
						root0.setLeft(r);
						root0.setRight(two);
						root0.value();
						expressions.add(root0);

						AssignmentExpression<Float> root1 = new AssignmentExpression<>();
						VariableExpression<Float> pi = new VariableExpression<>("pi");
						ConstantExpression<Float> piVal = new ConstantExpression<Float>(3.14159f);
						root1.setLeft(pi);
						root1.setRight(piVal);
						root1.value();
						expressions.add(root1);

						AssignmentExpression<Float> root2 = new AssignmentExpression<>();
						VariableExpression<Float> c = new VariableExpression<>("c");
						// TODO uncomment when DivisionExpression is ready
//						DivisionExpression<Float> d43 = new DivisionExpression<>();
						ConstantExpression<Float> c4 = new ConstantExpression<Float>(4.0f);
						ConstantExpression<Float> c3 = new ConstantExpression<Float>(3.0f);
//						d43.setLeft(c4);
//						d43.setRight(c3);
						root2.setLeft(c);
//						root2.setRight(d43);
						root2.value();
						expressions.add(root2);

						// TODO uncomment when MultiplicationExpression is ready
//						MultiplicationExpression<Float> root3 = new MultiplicationExpression<>();
//						MultiplicationExpression<Float> mcpi = new MultiplicationExpression<>();
//						mcpi.setLeft(c);
//						mcpi.setRight(pi);
//						root3.setLeft(mcpi);
						// TODO uncomment when PowerExpression is ready
//						PowerExpression<Float> r3 = new PowerExpression<>();
//						r3.setLeft(r);
//						r3.setRight(c3);
//						root3.setRight(r3);
//						expressions.add(root3);
						break;
					}
					case 2: //  "c=1.1; 2.2 - 3.3 / (a * b) + 1.1; a = 5.5; a + b^3; 1.1 + (2.2 - 3.3 / (a * b)); (c + 2.2 - 3.3) / (a * b);b=2.2"
					{
						// c = 1.1
						AssignmentExpression<Float> root4 = new AssignmentExpression<>(
							new VariableExpression<>("c"),
							new ConstantExpression<Float>(1.1f));
						expressions.add(root4);
						// 2.2 - 3.3 / (a * b) + 1.1
						// TODO uncomment when SubtractionExpression & MultiplicationExpression are ready
//						AdditionExpression<Float> root5 = new AdditionExpression<>(
//							new SubtractionExpression<Float>(
//								new ConstantExpression<Float>(2.2f),
//								new DivisionExpression<Float>(
//									new ConstantExpression<Float>(3.3f),
//									new MultiplicationExpression<>(
//										new VariableExpression<Float>("a"),
//										new VariableExpression<Float>("b")))),
//							new ConstantExpression<Float>(1.1f));
//						expressions.add(root5);
						// a = 5.5
						AssignmentExpression<Float> root6 = new AssignmentExpression<>(
							new VariableExpression<>("a"),
							new ConstantExpression<Float>(5.5f));
						expressions.add(root6);
						// a + b^3
						// TODO uncomment when PowerExpression is ready
//						AdditionExpression<Float> root7 = new AdditionExpression<>(
//							new VariableExpression<Float>("a"),
//							new PowerExpression<Float>(
//								new VariableExpression<Float>("b"),
//								new ConstantExpression<Float>(3.3f)));
//						expressions.add(root7);
						// 1.1 + (2.2 - 3.3 / (a * b))
						// TODO uncomment when SubtractionExpression & MultiplicationExpression are ready
//						AdditionExpression<Float> root8 = new AdditionExpression<>(
//							new ConstantExpression<Float>(1.1f),
//							new SubtractionExpression<Float>(
//								new ConstantExpression<Float>(2.2f),
//								new DivisionExpression<Float>(
//									new ConstantExpression<Float>(3.3f),
//									new MultiplicationExpression<Float>(
//										new VariableExpression<Float>("a"),
//										new VariableExpression<Float>("b")))));
//						expressions.add(root8);
						// (c + 2.2 - 3.3) / (a * b)
						// TODO uncomment when DivisionExpression, SubtractionExpression & MultiplicationExpression are ready
//						DivisionExpression<Float> root9 = new DivisionExpression<>(
//							new SubtractionExpression<Float>(
//								new AdditionExpression<Float>(
//									new VariableExpression<Float>("c"),
//									new ConstantExpression<Float>(2.2f)),
//								new ConstantExpression<Float>(3.3f)),
//							new MultiplicationExpression<Float>(
//								new VariableExpression<Float>("a"),
//								new VariableExpression<Float>("b")));
//						expressions.add(root9);
						// b=2.2
						AssignmentExpression<Float> root10 = new AssignmentExpression<>(
							new VariableExpression<>("b"),
							new ConstantExpression<Float>(2.2f));
						expressions.add(root10);
						break;
					}
					default:
						break;
				}
				break;
			}
			case 2: // Double
			{
				switch (index)
				{
					case 0: // -2.35 + 3.12 * 4.23
					{
						AdditionExpression<Double> root = new AdditionExpression<>();
						// TODO uncomment when SubtractionExpression is ready
//						SubtractionExpression<Double> left = new SubtractionExpression<>();
						// TODO uncommetn when MultiplicationExpression is ready
//						MultiplicationExpression<Double> right = new MultiplicationExpression<>();
//						left.setLeft(new ConstantExpression<Double>(0.0));
//						left.setRight(new ConstantExpression<Double>(2.35));
//						right.setLeft(new ConstantExpression<Double>(3.12));
//						right.setRight(new ConstantExpression<Double>(4.23));
//						root.setLeft(left);
//						root.setRight(right);
						expressions.add(root);
						break;
					}
					case 1: // r = 2; pi = 3.14159; c = 4.0/3; c * pi * r ^3
					{
						AssignmentExpression<Double> root0 = new AssignmentExpression<>();
						VariableExpression<Double> r = new VariableExpression<>("r");
						ConstantExpression<Double> two = new ConstantExpression<Double>(2.0);
						root0.setLeft(r);
						root0.setRight(two);
						root0.value();
						expressions.add(root0);

						AssignmentExpression<Double> root1 = new AssignmentExpression<>();
						VariableExpression<Double> pi = new VariableExpression<>("pi");
						ConstantExpression<Double> piVal = new ConstantExpression<Double>(3.14159);
						root1.setLeft(pi);
						root1.setRight(piVal);
						root1.value();
						expressions.add(root1);

						AssignmentExpression<Double> root2 = new AssignmentExpression<>();
						VariableExpression<Double> c = new VariableExpression<>("c");
						// TODO uncomment when DivisionExpression is ready
//						DivisionExpression<Double> d43 = new DivisionExpression<>();
						ConstantExpression<Double> c4 = new ConstantExpression<Double>(4.0);
						ConstantExpression<Double> c3 = new ConstantExpression<Double>(3.0);
//						d43.setLeft(c4);
//						d43.setRight(c3);
						root2.setLeft(c);
//						root2.setRight(d43);
						root2.value();
						expressions.add(root2);

						// TODO uncommetn when MultiplicationExpression is ready
//						MultiplicationExpression<Double> root3 = new MultiplicationExpression<>();
//						MultiplicationExpression<Double> mcpi = new MultiplicationExpression<>();
//						mcpi.setLeft(c);
//						mcpi.setRight(pi);
//						root3.setLeft(mcpi);
						// TODO uncomment when PowerExpression is ready
//						PowerExpression<Double> r3 = new PowerExpression<>();
//						r3.setLeft(r);
//						r3.setRight(c3);
//						root3.setRight(r3);
//						expressions.add(root3);
						break;
					}
					case 2: //  "c=1.1; 2.2 - 3.3 / (a * b) + 1.1; a = 5.5; a + b^3; 1.1 + (2.2 - 3.3 / (a * b)); (c + 2.2 - 3.3) / (a * b);b=2.2"
					{
						// c = 1.1
						AssignmentExpression<Double> root4 = new AssignmentExpression<>(
							new VariableExpression<>("c"),
							new ConstantExpression<Double>(1.1));
						expressions.add(root4);
						// 2.2 - 3.3 / (a * b) + 1.1
						// TODO uncommetn when SubtractionExpression & MultiplicationExpression are ready
//						AdditionExpression<Double> root5 = new AdditionExpression<>(
//							new SubtractionExpression<Double>(
//								new ConstantExpression<Double>(2.2),
//								new DivisionExpression<Double>(
//									new ConstantExpression<Double>(3.3),
//									new MultiplicationExpression<>(
//										new VariableExpression<Double>("a"),
//										new VariableExpression<Double>("b")))),
//							new ConstantExpression<Double>(1.1));
//						expressions.add(root5);
						// a = 5.5
						AssignmentExpression<Double> root6 = new AssignmentExpression<>(
							new VariableExpression<>("a"),
							new ConstantExpression<Double>(5.5));
						expressions.add(root6);
						// a + b^3
						// TODO uncomment when PowerExpression is ready
//						AdditionExpression<Double> root7 = new AdditionExpression<>(
//							new VariableExpression<Double>("a"),
//							new PowerExpression<Double>(
//								new VariableExpression<Double>("b"),
//								new ConstantExpression<Double>(3.3)));
//						expressions.add(root7);
						// 1.1 + (2.2 - 3.3 / (a * b))
						// TODO uncomment when SubtractionExpression, DivisionExpression & MultiplicationExpression are ready
//						AdditionExpression<Double> root8 = new AdditionExpression<>(
//							new ConstantExpression<Double>(1.1),
//							new SubtractionExpression<Double>(
//								new ConstantExpression<Double>(2.2),
//								new DivisionExpression<Double>(
//									new ConstantExpression<Double>(3.3),
//									new MultiplicationExpression<Double>(
//										new VariableExpression<Double>("a"),
//										new VariableExpression<Double>("b")))));
//						expressions.add(root8);
						// (c + 2.2 - 3.3) / (a * b)
						// TODO uncomment when DivisionExpression, SubtractionExpression & MultiplicationExpression are ready
//						DivisionExpression<Double> root9 = new DivisionExpression<>(
//							new SubtractionExpression<Double>(
//								new AdditionExpression<Double>(
//									new VariableExpression<Double>("c"),
//									new ConstantExpression<Double>(2.2)),
//								new ConstantExpression<Double>(3.3)),
//							new MultiplicationExpression<Double>(
//								new VariableExpression<Double>("a"),
//								new VariableExpression<Double>("b")));
//						expressions.add(root9);
						// b=2.2
						AssignmentExpression<Double> root10 = new AssignmentExpression<>(
							new VariableExpression<>("b"),
							new ConstantExpression<Double>(2.2));
						expressions.add(root10);
						break;
					}
					default:
						break;
				}
				break;
			}
			case -1:
			default:
				break;
		}

		return expressions;
	}

	/**
	 * Setup before all tests
	 * @throws java.lang.Exception if setup fails
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
		assertEquals(invalidIntegerContexts.length,
		             invalidContextsExceptions.length,
		             "failed with invalid contexts size("
		                 + invalidIntegerContexts.length
		                 + ") != expected exceptions size("
		                 + invalidContextsExceptions.length + ")");

		for (int i = 0; i < valueClasses.length; i++)
		{
			Class<? extends Number> currentValueClass = valueClasses[i];
			int index = indexOf(currentValueClass);
			if (index == -1)
			{
				fail("Unknown index for " + currentValueClass.getSimpleName());
			}
			valueClassList.add(currentValueClass);
		}
	}

	/**
	 * Tear down after all tests
	 * @throws java.lang.Exception if tear down fails
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception
	{
		valueClassList.clear();
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
	 * Setup before each test
	 * @throws java.lang.Exception if setup fails
	 */
	@BeforeEach
	void setUp() throws Exception
	{
	}

	/**
	 * Setup expressions for testing with a specific Number type
	 * @param type the number type
	 */
	@SuppressWarnings("unchecked")
	private void setupTestFor(Class<? extends Number> type)
	{
		int typeIndex = indexOf(type);
		if (typeIndex != -1)
		{

			validContexts = validContextsArray[typeIndex];
			validExpressions = (List<Expression<? extends Number>>[]) new ArrayList<?>[validContexts.length];
			for (int i = 0; i < validContexts.length; i++)
			{
				validExpressions[i] = buildValidExpressions(type, i);
			}
			invalidContexts = invalidContextsArray[typeIndex];
			// invalidContextsExceptions are always the same despite type
		}
		else
		{
			fail("Unable to setup test for type " + type.getSimpleName());
		}
	}


	/**
	 * Tear down after each test
	 * @throws java.lang.Exception if tear down fails
	 */
	@AfterEach
	void tearDown() throws Exception
	{
		validContexts = null;
		validExpressions = null;
		invalidContexts = null;
	}

	/**
	 * Test method for {@link parser.ExpressionParser#ExpressionParser(java.lang.Number)}.
	 * @param type The type of numbers in expressions
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("ExpressionParser(Number specimen)")
	final void testExpressionParser(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("ExpressionParser<" + type.getSimpleName()
		    + ">.ExpressionParser(" + type.getSimpleName() + ")");
		System.out.println(testName);

		ExpressionParser<?> parser = null;
		if (type == Integer.class)
		{
			parser = new ExpressionParser<Integer>(Integer.valueOf(0));
		}
		else if (type == Float.class)
		{
			parser = new ExpressionParser<Float>(Float.valueOf(0.0f));
		}
		else if (type == Double.class)
		{
			parser = new ExpressionParser<Double>(Double.valueOf(0.0));
		}
		else
		{
			fail("Unknown Number type + " + type.getSimpleName());
		}

		assertNotNull(parser, testName + " failed with null parser");
	}

	/**
	 * Test method for {@link parser.ExpressionParser#parse(java.lang.String)}.
	 * @param type the type of numbers in expressions
	 */
	@ParameterizedTest
	@MethodSource("valueClassesProvider")
	@DisplayName("parse(String)")
	final void testParse(Class<? extends Number> type)
	{
		setupTestFor(type);
		String testName = new String("ExpressionParser<" + type.getSimpleName()
		    + ">.parse(String context)");
		System.out.println(testName);

		ExpressionParser<?> parser = null;
		if (type == Integer.class)
		{
			parser = new ExpressionParser<Integer>(Integer.valueOf(0));
		}
		else if (type == Float.class)
		{
			parser = new ExpressionParser<Float>(Float.valueOf(0.0f));
		}
		else if (type == Double.class)
		{
			parser = new ExpressionParser<Double>(Double.valueOf(0.0));
		}
		else
		{
			fail("Unknown Number type + " + type.getSimpleName());
		}

		assertNotNull(parser, testName + " failed with null parser");

		// ---------------------------------------------------------------------
		// Test valid contexts by validating resulting expressions
		// ---------------------------------------------------------------------
		for (int i = 0; i < validContexts.length; i++)
		{
			try
			{
				if (type == Integer.class)
				{
					@SuppressWarnings("unchecked")
					ExpressionParser<Integer> iParser = (ExpressionParser<Integer>) parser;
					List<Expression<Integer>> parsedExpressions = iParser.parse(validContexts[i]);
					List<Expression<? extends Number>> expectedExpressions = validExpressions[i];
					assertEquals(parsedExpressions.size(),
					             expectedExpressions.size(),
					             testName
					                 + " failed with wrong number of parsed expressions");
					Iterator<Expression<Integer>> parseIt = parsedExpressions.iterator();
					Iterator<Expression<? extends Number>> expIt = expectedExpressions.iterator();
					while (parseIt.hasNext() && expIt.hasNext())
					{
						Expression<Integer> parsedExpression = parseIt.next();
						@SuppressWarnings("unchecked")
						Expression<Integer> expectedExpression = (Expression<Integer>) expIt.next();
						assertTrue(ExpressionsComparator.compare(parsedExpression, expectedExpression),
						           testName
						               + " failed with unexpected expressions after parsing "
						               + validExpressions[i]);
					}
				}
				else if (type == Float.class)
				{
					@SuppressWarnings("unchecked")
					ExpressionParser<Float> fParser = (ExpressionParser<Float>) parser;
					List<Expression<Float>> parsedExpressions = fParser.parse(validContexts[i]);
					List<Expression<? extends Number>> expectedExpressions = validExpressions[i];
					assertEquals(parsedExpressions.size(),
					             expectedExpressions.size(),
					             testName
					                 + " failed with wrong number of parsed expressions");
					Iterator<Expression<Float>> parseIt = parsedExpressions.iterator();
					Iterator<Expression<? extends Number>> expIt = expectedExpressions.iterator();
					while (parseIt.hasNext() && expIt.hasNext())
					{
						Expression<Float> parsedExpression = parseIt.next();
						@SuppressWarnings("unchecked")
						Expression<Float> expectedExpression = (Expression<Float>) expIt.next();
						assertTrue(ExpressionsComparator.compare(parsedExpression, expectedExpression),
						           testName
						               + " failed with unexpected expressions after parsing "
						               + validExpressions[i]);
					}
				}
				else if (type == Double.class)
				{
					@SuppressWarnings("unchecked")
					ExpressionParser<Double> dParser = (ExpressionParser<Double>) parser;
					List<Expression<Double>> parsedExpressions = dParser.parse(validContexts[i]);
					List<Expression<? extends Number>> expectedExpressions = validExpressions[i];
					assertEquals(parsedExpressions.size(),
					             expectedExpressions.size(),
					             testName
					                 + " failed with wrong number of parsed expressions");
					Iterator<Expression<Double>> parseIt = parsedExpressions.iterator();
					Iterator<Expression<? extends Number>> expIt = expectedExpressions.iterator();
					while (parseIt.hasNext() && expIt.hasNext())
					{
						Expression<Double> parsedExpression = parseIt.next();
						@SuppressWarnings("unchecked")
						Expression<Double> expectedExpression = (Expression<Double>) expIt.next();
						assertTrue(ExpressionsComparator.compare(parsedExpression, expectedExpression),
						           testName
						               + " failed with unexpected expressions after parsing "
						               + validExpressions[i]);
					}
				}
				else
				{
					fail(testName
					    + " failed while parsing valid expressions Unknown Number type + "
					    + type.getSimpleName());
				}
			}
			catch (ParserException e)
			{
				fail(testName + " Parsing " + validContexts[i] + " failed with "
				    + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}

		// ---------------------------------------------------------------------
		// Test invalid contexts by checking expected exceptions
		// ---------------------------------------------------------------------
		for (int i = 0; i < invalidContexts.length; i++)
		{
			final int index = i;
			Class<? extends ParserException> expectedExceptionClass =
			    invalidContextsExceptions[i];
			if (type == Integer.class)
			{
				@SuppressWarnings("unchecked")
				ExpressionParser<Integer> iParser = (ExpressionParser<Integer>) parser;
				assertThrows(expectedExceptionClass, () -> {
					iParser.parse(invalidContexts[index]);
				},testName + " failed with no exception thrown while parsing \""
					+ invalidContexts[index] + "\"");
			}
			else if (type == Float.class)
			{
				@SuppressWarnings("unchecked")
				ExpressionParser<Float> fParser = (ExpressionParser<Float>) parser;
				if (index < (invalidContexts.length -1))
				{
					assertThrows(expectedExceptionClass, () -> {
						fParser.parse(invalidContexts[index]);
					},testName+ " failed with no exception thrown while parsing \""
							+ invalidContexts[index] + "\"");
				}
				else // index == invalidContexts.length - 1
				{
					try
					{
						List<Expression<Float>> expressions = fParser.parse(invalidContexts[index]);
						assertEquals(1,
						             expressions.size(),
						             testName
						                 + " failed with more than 1 expression while parsing "
						                 + invalidContexts[index]);
						VariableExpression<Float> a = new VariableExpression<Float>("a");
						ConstantExpression<Float> two = new ConstantExpression<Float>(2.35f);
						AssignmentExpression<Float> assign = new AssignmentExpression<Float>(a, two);
						assertTrue(ExpressionsComparator
						    .compare(assign, expressions.get(0)),
						           testName
						               + " failed with unexpected expressions while parsing "
						               + invalidContexts[index]);
					}
					catch (ParserException e)
					{
						fail(testName + " failed while parsing " + invalidContexts[index]);
						e.printStackTrace();
					}
				}
			}
			else if (type == Double.class)
			{
				@SuppressWarnings("unchecked")
				ExpressionParser<Double> dParser = (ExpressionParser<Double>) parser;
				if (index < (invalidContexts.length -1))
				{
					assertThrows(expectedExceptionClass, () -> {
						dParser.parse(invalidContexts[index]);
					},
					             testName
					                 + " failed with no exception thrown while parsing \""
					                 + invalidContexts[index] + "\"");
				}
				else
				{
					try
					{
						List<Expression<Double>> expressions = dParser.parse(invalidContexts[index]);
						assertEquals(1,
						             expressions.size(),
						             testName
						                 + " failed with more than 1 expression while parsing "
						                 + invalidContexts[index]);
						VariableExpression<Double> a = new VariableExpression<Double>("a");
						ConstantExpression<Double> two = new ConstantExpression<Double>(2.35);
						AssignmentExpression<Double> assign = new AssignmentExpression<Double>(a, two);
						assertTrue(ExpressionsComparator
						    .compare(assign, expressions.get(0)),
						           testName
						               + " failed with unexpected expressions while parsing "
						               + invalidContexts[index]);
					}
					catch (ParserException e)
					{
						fail(testName + " failed while parsing " + invalidContexts[index]);
						e.printStackTrace();
					}
				}
			}
			else
			{
				fail(testName
				    + " failed while parsing invalid expressions Unknown Number type "
				    + type.getSimpleName());
			}
		}
	}
}
