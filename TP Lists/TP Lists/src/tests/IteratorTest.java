package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
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

import tests.utils.IteratorFactory;
import tests.utils.IteratorKind;
import tests.utils.NodeIteratorFactory;
import tests.utils.NodeListIteratorFactory;
import tests.utils.WordSupplier;

/**
 * Test class for Iterator
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Iterator<E>")
public class IteratorTest
{
	/**
	 * Number of elements
	 */
	private final static int chainLength = 12;

	/**
	 * Word Supplier to provide random words
	 */
	private static WordSupplier wordSupplier = null;

	/**
	 * Single word to iterator over
	 */
	private static String word = null;

	/**
	 * Multiple words to iterate over
	 */
	private static String[] words = null;

	/**
	 * Stream of {@link IteratorFactory}. Each of them will be used
	 * to build various {@link Iterator}s
	 * @return a stream of {@link IteratorFactory}
	 */
	private static Stream<IteratorFactory<String>> iteratorFactoryProvider()
	{
		return Stream.of(new NodeIteratorFactory<String>(),
		                 new NodeListIteratorFactory<String>());
	}

	/**
	 * Tests Arguments provider providing an {@link Iterator} to test and a
	 * {@link IteratorKind} indicating if the provided iterator is empty,
	 * single or multiple
	 * @return A Stream of arguments containing {@link Iterator} and
	 * {@link IteratorKind} pairs
	 */
	private static Stream<Arguments> iteratorAndKindProvider()
	{
		word = wordSupplier.get();
		words = new String[chainLength];
		for (int i = 0; i < words.length; i++)
		{
			words[i] = wordSupplier.get();
		}

		return iteratorFactoryProvider().flatMap(factory -> {
			return Stream.of(Arguments.of(factory.emptyIterator(),
			                              IteratorKind.EMPTY),
			                 Arguments.of(factory.singleIterator(word),
			                              IteratorKind.SINGLE),
			                 Arguments.of(factory.multipleIterator(Arrays.asList(words)),
			                              IteratorKind.MULTIPLE));
		});
	}

	/**
	 * Tests Arguments provider providing an {@link Iterator} to test, a
	 * {@link IteratorKind} indicating if the provided iterator is empty,
	 * single or multiple and a {@link IteratorFactory} to build iterators
	 * @return A Stream of arguments containing {@link Iterator},
	 * {@link IteratorKind}, and {@link IteratorFactory} triplets
	 */
	private static Stream<Arguments> iteratorKindAndFactoryProvider()
	{
		word = wordSupplier.get();
		words = new String[chainLength];
		for (int i = 0; i < words.length; i++)
		{
			words[i] = wordSupplier.get();
		}

		return iteratorFactoryProvider().flatMap(factory -> {
			return Stream.of(Arguments.of(factory.emptyIterator(),
			                              IteratorKind.EMPTY,
			                              factory),
			                 Arguments.of(factory.singleIterator(word),
			                              IteratorKind.SINGLE,
			                              factory),
			                 Arguments.of(factory.multipleIterator(Arrays.asList(words)),
			                              IteratorKind.MULTIPLE,
			                              factory));
		});
	}

	/**
	 * Setup before all tests
	 * @throws java.lang.Exception if setup before class fails
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
		wordSupplier = new WordSupplier("assets/prenoms.txt");
		System.out.println("-------------------------------------------------");
		System.out.println("Iterators tests");
		System.out.println("-------------------------------------------------");
	}

	/**
	 * Teardown after all tests
	 * @throws java.lang.Exception if teardown after class fails
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception
	{
		wordSupplier = null;
		System.out.println("-------------------------------------------------");
		System.out.println("Iterators tests end");
		System.out.println("-------------------------------------------------");
	}

	/**
	 * Setup before each test
	 * @throws java.lang.Exception if setup before each test fails
	 */
	@BeforeEach
	void setUp() throws Exception
	{
	}

	/**
	 * Teardown after each test
	 * @throws java.lang.Exception if tear down after each test fails
	 */
	@AfterEach
	void tearDown() throws Exception
	{
		wordSupplier.reset();
	}

	/**
	 * Test method for {@link Iterator#hasNext()}.
	 * @param iterator The iterator to test
	 * @param kind The kind of iterator to test, either empty, single or multiple
	 * @param testInfo informations about the current test
	 */
	@Order(1)
	@ParameterizedTest(name ="[{index}] {1} {0}")
	@MethodSource("iteratorAndKindProvider")
	@DisplayName("hasNext()")
	final void testHasNext(Iterator<String> iterator, IteratorKind kind,  TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		switch (kind)
		{
			case EMPTY:
			{
				/*
				 * Iterator on nothing
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on no elements");
				assertFalse(iterator.hasNext(),
				            testName
				                + " unexpected next elt to iterate on empty iterator");
				break;
			}
			case SINGLE:
			{
				/*
				 * Iterator on single element
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on single element");
				assertTrue(iterator.hasNext(),
				           testName + " unexpected no next elt to iterate");
				break;
			}
			case MULTIPLE:
			{
				/*
				 * Iterator on multiple elements
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on multiple elements");
				int count = 0;
				// Forward iteration
				while (count < chainLength)
				{
					assertTrue(iterator.hasNext(), testName + " unexpected no next");
					try
					{
						iterator.next();
					}
					catch (NoSuchElementException e)
					{
						fail(testName + " unexpected next fail: "
						    + e.getLocalizedMessage());
					}
					count++;
				}
				break;
			}
			default:
			{
				fail(testName + " unknown iterator kind: " + kind);
				break;
			}
		}
	}

	/**
	 * Test method for {@link Iterator#next()}.
	 * @param iterator The iterator to test
	 * @param kind The kind of iterator to test, either empty, single or multiple
	 * @param testInfo informations about the current test
	 */
	@Order(2)
	@ParameterizedTest(name ="[{index}] {1} {0}")
	@MethodSource("iteratorAndKindProvider")
	@DisplayName("next()")
	final void testNext(Iterator<String> iterator, IteratorKind kind, TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		switch (kind)
		{
			case EMPTY:
			{
				/*
				 * Iterator on nothing
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on no elements");
				assertThrows(NoSuchElementException.class, () -> {
					iterator.next();
				}, testName + " next() unexpectedly didn't throw on empty iterator");
				break;
			}
			case SINGLE:
			{
				/*
				 * Iterator on single element
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on single element");
				String expectedContent = null;
				try
				{
					expectedContent = iterator.next();
					assertEquals(word,
					             expectedContent,
					             testName + " unexpected next element content");
				}
				catch (NoSuchElementException e)
				{
					fail(testName + " unexpected next() fail: "
					    + e.getLocalizedMessage());
				}
				try
				{
					expectedContent = iterator.next();
					fail(testName + " unexpected next() call success");
				}
				catch (NoSuchElementException e)
				{
					// Nothing : expected
				}
				break;
			}
			case MULTIPLE:
			{
				/*
				 * Iterator on multiple elements
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on multiple elements");
				int iteration = 0;
				// Forward iteration
				while (iteration < chainLength)
				{
					assertTrue(iterator.hasNext(), testName + " unexpected no next");
					try
					{
						String iterContent = iterator.next();
						String expectedContent = words[iteration];
						assertEquals(expectedContent,
						             iterContent,
						             testName + " unexpected next content");
					}
					catch (NoSuchElementException e)
					{
						fail(testName + " unexpected next fail: "
						    + e.getLocalizedMessage());
					}
					iteration++;
				}
				break;
			}
			default:
			{
				fail(testName + " unknown iterator kind: " + kind);
				break;
			}
		}
	}

	/**
	 * Test method for {@link Iterator#remove()}.
	 * @param iterator The iterator to test
	 * @param kind The kind of iterator to test, either empty, single or multiple
	 * @param factory A factory to build/rebuild {@link Iterator}s
	 * @param testInfo informations about the current test
	 */
	@Order(3)
	@ParameterizedTest(name ="[{index}] {1} {0}")
	@MethodSource("iteratorKindAndFactoryProvider")
	@DisplayName("remove()")
	final void testRemove(Iterator<String> iterator,
	                      IteratorKind kind,
	                      IteratorFactory<String> factory,
	                      TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		switch (kind)
		{
			case EMPTY:
			{
				/*
				 * Iterator on nothing
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on no elements");
				assertThrows(IllegalStateException.class, () -> {
					factory.emptyIterator().remove();
				}, testName + " remove() didn't throw exception on empty iterator");
				break;
			}
			case SINGLE:
			{
				/*
				 * Iterator on single element
				 */
				assertThrows(IllegalStateException.class, () -> {
					factory.singleIterator(word).remove();
				}, testName + " remove() didn't throw exception");
				assertNotNull(iterator,
				              testName + " unexpected null iterator on single element");
				try
				{
					iterator.next();
					iterator.remove();
				}
				catch (NoSuchElementException e)
				{
					fail(testName + " next() failed: " + e.getLocalizedMessage());
				}
				catch (IllegalStateException e)
				{
					fail(testName + "remove() failed: " + e.getLocalizedMessage());
				}

				try
				{
					iterator.next();
					fail(testName + " unexpected next() success");
				}
				catch (NoSuchElementException e)
				{
					// Nothing : expected
				}

				try
				{
					iterator.remove();
					fail(testName + " remove() 2nd call succeeded");
				}
				catch (IllegalStateException e)
				{
					// Nothing: expected
				}
				break;
			}
			case MULTIPLE:
			{
				/*
				 * Iterator on multiple elements
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on multiple elements");
				int count = 0;
				// Forward iteration
				try
				{
					while (count < chainLength)
					{
						iterator.next();
						iterator.remove();
						count++;
					}
				}
				catch (IllegalStateException e)
				{
					fail(testName + " unexpected remove() fail + "
					    + e.getLocalizedMessage());
				}

				try
				{
					iterator.remove();
					fail(testName + " remove() call unexpectedly succeeded");
				}
				catch (IllegalStateException e)
				{
					// Nothing: expected
				}

				iterator = factory.multipleIterator(Arrays.asList(words));
				assertNotNull(iterator,
				              testName + " unexpected null iterator on multiple elements");
				// Forward iteration
				count = 0;
				while (count < chainLength)
				{
					iterator.next();
					count++;
				}
				try
				{
					iterator.remove();
				}
				catch (IllegalStateException e)
				{
					fail(testName + " unexpected remove() failure after last next(): "
					    + e.getLocalizedMessage());
				}

				/*
				 * "it" should now be an iterator on an empty chain
				 */
				assertFalse(iterator.hasNext(), testName + " unexpected next presence");
				break;
			}
			default:
			{
				fail(testName + " unknown iterator kind: " + kind);
				break;
			}
		}
	}
}
