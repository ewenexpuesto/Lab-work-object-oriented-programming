package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.ListIterator;
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

import tests.utils.ArrayList_ListIteratorFactory;
import tests.utils.IteratorKind;
import tests.utils.LinkedList_ListIteratorFactory;
import tests.utils.ListIteratorFactory;
import tests.utils.NodeListIteratorFactory;
import tests.utils.WordSupplier;

/**
 * Test class for ListIterator
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ListIterator<E>")
public class ListIteratorTest
{
	/**
	 * Number of elements
	 */
	private final static int chainLength = 15;

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
	 * Stream of {@link ListIteratorFactory}. Each of them will be used
	 * to build various {@link ListIterator}s
	 * @return a stream of {@link ListIteratorFactory}
	 */
	private static Stream<ListIteratorFactory<String>> listIteratorFactoryProvider()
	{
		return Stream.of(new ArrayList_ListIteratorFactory<String>(),
						 new LinkedList_ListIteratorFactory<String>(),
						 new NodeListIteratorFactory<String>());
	}

//	/**
//	 * Stream of {@link ListIterator} to test
//	 * @return a stream of ListIterator to test
//	 */
//	private static Stream<ListIterator<String>> listIteratorProvider()
//	{
//		word = wordSupplier.get();
//		words = new String[chainLength];
//		for (int i = 0; i < words.length; i++)
//		{
//			words[i] = wordSupplier.get();
//		}
//		return listIteratorFactoryProvider().flatMap(factory->{
//			return Stream.of(factory.emptyListIterator(),
//							 factory.singleListIterator(word),
//							 factory.multipleListIterator(Arrays.asList(words)));
//		});
//	}

	/**
	 * Stream of {@link ListIterator} and {@link IteratorKind} to test
	 * @return a stream of ListIterator to test
	 */
	private static Stream<Arguments> listIteratorAndKindProvider()
	{
		word = wordSupplier.get();
		words = new String[chainLength];
		for (int i = 0; i < words.length; i++)
		{
			words[i] = wordSupplier.get();
		}
		return listIteratorFactoryProvider().flatMap(factory->{
			return Stream.of(Arguments.of(factory.emptyListIterator(),
			                              IteratorKind.EMPTY),
							 Arguments.of(factory.singleListIterator(word),
							              IteratorKind.SINGLE),
							 Arguments.of(factory.multipleListIterator(Arrays.asList(words)),
							              IteratorKind.MULTIPLE));
		});
	}

	/**
	 * Stream of {@link ListIterator} and {@link IteratorKind} and
	 * {@link ListIteratorFactory} to test
	 * @return a stream of ListIterator to test
	 */
	private static Stream<Arguments> listIteratorKindAndFactoryProvider()
	{
		word = wordSupplier.get();
		words = new String[chainLength];
		for (int i = 0; i < words.length; i++)
		{
			words[i] = wordSupplier.get();
		}
		return listIteratorFactoryProvider().flatMap(factory->{
			return Stream.of(Arguments.of(factory.emptyListIterator(),
			                              IteratorKind.EMPTY, factory),
							 Arguments.of(factory.singleListIterator(word),
							              IteratorKind.SINGLE, factory),
							 Arguments.of(factory.multipleListIterator(Arrays.asList(words)),
							              IteratorKind.MULTIPLE, factory));
		});
	}

	/**
	 * Utility method indicating if the the provided {@link ListIterator} has
	 * been implemented in the project "collections" package
	 * @param literator a {@link ListIterator} we're investigating
	 * @return true if the provided iterator class is located in the
	 * "collections" package
	 */
	private static boolean isCustom(ListIterator<?> literator)
	{
		 return literator.getClass().getPackage().getName().contains("collections");
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
		System.out.println("ListIterators tests");
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
		System.out.println("ListIterators tests end");
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
	 * Test method for {@link ListIterator#hasNext()}.
	 * @param iterator The iterator to test
	 * @param kind the type of provided iterator: Either empty, single or multiple
	 * @param testInfo informations about the current test
	 */
	@ParameterizedTest(name ="[{index}] {1} {0}")
	@MethodSource("listIteratorAndKindProvider")
	@DisplayName("hasNext()")
	@Order(1)
	final void testHasNext(ListIterator<String> iterator, IteratorKind kind,  TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);
		switch (kind)
		{
			case EMPTY:
			{
				assertNotNull(iterator,
				              testName + " unexpected null iterator on no elements");
				assertFalse(iterator.hasNext(),
				            testName
				                + " unexpected next elt to iterate on empty iterator");
				break;
			}
			case SINGLE:
			{
				assertNotNull(iterator,
				              testName + " unexpected null iterator on single element");
				assertTrue(iterator.hasNext(),
				           testName + " unexpected no next elt to iterate");
				iterator.next();
				assertFalse(iterator.hasNext(),
							testName + " unexpected next elt to iterate");
				break;
			}
			case MULTIPLE:
			{
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
				fail(testName + " unknown iterator kind:" + kind);
				break;
		}
	}

	/**
	 * Test method for {@link ListIterator#next()}.
	 * @param iterator The iterator to test
	 * @param kind the type of provided iterator: Either empty, single or multiple
	 * @param testInfo informations about the current test
	 */
	@ParameterizedTest(name ="[{index}] {1} {0}")
	@MethodSource("listIteratorAndKindProvider")
	@DisplayName("next()")
	@Order(2)
	final void testNext(ListIterator<String> iterator, IteratorKind kind,  TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		switch (kind)
		{
			case EMPTY:
			{
				/*
				 * ListIterator on nothing
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
				 * ListIterator on single element
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
					iterator.next();
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
				 * ListIterator on multiple elements
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on multiple elements");
				int iteration = 0;
				String expectedContent = null;
				// Forward iteration
				while (iteration < chainLength)
				{
					assertTrue(iterator.hasNext(), testName + " unexpected no next");
					try
					{
						String iterContent = iterator.next();
						expectedContent = words[iteration];
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
	 * Test method for {@link ListIterator#hasPrevious()}.
	 * @param iterator The iterator to test
	 * @param kind the type of provided iterator: Either empty, single or multiple
	 * @param testInfo informations about the current test
	 */
	@ParameterizedTest(name ="[{index}] {1} {0}")
	@MethodSource("listIteratorAndKindProvider")
	@DisplayName("hasPrevious()")
	@Order(3)
	final void testHasPrevious(ListIterator<String> iterator, IteratorKind kind,  TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		switch (kind)
		{
			case EMPTY:
			{
				/*
				 * ListIterator on nothing
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on no elements");
				assertFalse(iterator.hasPrevious(),
				            testName
				                + " unexpected previous elt to iterate on empty iterator");
				break;
			}
			case SINGLE:
			{
				/*
				 * ListIterator on single element
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on single element");
				assertFalse(iterator.hasPrevious(),
				            testName + " unexpected previous elt to iterate");
				iterator.next();
				assertTrue(iterator.hasPrevious(),
				           testName + " unexpected no previous elt to iterate");
				break;
			}
			case MULTIPLE:
			{
				/*
				 * ListIterator on multiple elements
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on multiple elements");
				int count = 0;
				// Forward iteration
				while (count < chainLength)
				{
					try
					{
						iterator.next();
					}
					catch (NoSuchElementException e)
					{
						fail(testName + " unexpected next fail: "
						    + e.getLocalizedMessage());
					}
					if (count > 0)
					{
						assertTrue(iterator.hasPrevious(),
						           testName + " unexpected no previous");
					}
					count++;
				}

				// Backward iteration
				while (count > 0)
				{
					assertTrue(iterator.hasPrevious(), testName + " unexpected no previous");
					try
					{
						iterator.previous();
					}
					catch (NoSuchElementException e)
					{
						fail(testName + " unexpected previous fail: "
						    + e.getLocalizedMessage());
					}
					count--;
				}
				assertFalse(iterator.hasPrevious(), testName + " unexpected previous");
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
	 * Test method for {@link ListIterator#previous()}.
	 * @param iterator The iterator to test
	 * @param kind the type of provided iterator: Either empty, single or multiple
	 * @param testInfo informations about the current test
	 */
	@ParameterizedTest(name ="[{index}] {1} {0}")
	@MethodSource("listIteratorAndKindProvider")
	@DisplayName("previous()")
	@Order(4)
	final void testPrevious(ListIterator<String> iterator, IteratorKind kind,  TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		switch (kind)
		{
			case EMPTY:
			{
				/*
				 * ListIterator on nothing
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on no elements");
				assertThrows(NoSuchElementException.class, () -> {
					iterator.previous();
				}, testName + " previous() unexpectedly didn't throw on empty iterator");
				break;
			}
			case SINGLE:
			{
				/*
				 * ListIterator on single element
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on single element");
				assertThrows(NoSuchElementException.class, () -> {
					iterator.previous();
				}, testName + " previous() unexpectedly didn't throw");

				iterator.next();
				try
				{
					String itContent = iterator.previous();
					assertEquals(word,
					             itContent,
					             testName + " unexpected previous content");
				}
				catch (NoSuchElementException e)
				{
					fail(testName + " previous() unexpectedly failed: "
					    + e.getLocalizedMessage());
				}
				break;
			}
			case MULTIPLE:
			{
				/*
				 * ListIterator on multiple elements
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on multiple elements");

				int count = 0;
				String currentWord = words[0];
				try
				{
					iterator.previous();
					fail(testName
					    + " previous() unexpectedly didn't fail on first element");
				}
				catch (NoSuchElementException e)
				{
					// Nothing: expected
				}
				catch (Exception e)
				{
					fail(testName + " unexpected exception: "
					    + e.getLocalizedMessage());
				}

				// Forward iteration
				while (count < chainLength)
				{
					try
					{
						iterator.next();
					}
					catch (NoSuchElementException e)
					{
						fail(testName + " unexpected next fail: "
						    + e.getLocalizedMessage());
					}
					assertTrue(iterator.hasPrevious(),
					           testName + " unexpected no previous");
					count++;
				}

				// Backward iteration
				while (count > 0)
				{
					currentWord = words[count - 1];
					try
					{
						String itContent = iterator.previous();
						assertEquals(currentWord,
						             itContent,
						             testName + " unexpected previous content");
					}
					catch (NoSuchElementException e)
					{
						fail(testName + " unexpected previous fail: "
						    + e.getLocalizedMessage());
					}
					count--;
				}
				try
				{
					iterator.previous();
					fail(testName + " previous() call unexpectedly didn't throw");
				}
				catch (NoSuchElementException e)
				{
					// Nothing: expected
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
	 * Test method for {@link ListIterator#nextIndex()}.
	 * @param iterator The iterator to test
	 * @param kind the type of provided iterator: Either empty, single or multiple
	 * @param factory The factory to build {@link ListIterator}s
	 * @param testInfo informations about the current test
	 */
	@ParameterizedTest(name ="[{index}] {1} {0}")
	@MethodSource("listIteratorKindAndFactoryProvider")
	@DisplayName("nextIndex()")
	@Order(5)
	final void testNextIndex(ListIterator<String> iterator, IteratorKind kind,  ListIteratorFactory<String> factory, TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		switch (kind)
		{
			case EMPTY:
			{
				/*
				 * ListIterator on nothing
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on no elements");
				assertEquals(0,
				             iterator.nextIndex(),
				             testName
				                 + " unexpected nextIndex value on empty iterator");
				break;
			}
			case SINGLE:
			{
				/*
				 * ListIterator on single element
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on single element");
				assertEquals(0,
				             iterator.nextIndex(),
				             testName + " unexpected nextIndex value");
				break;
			}
			case MULTIPLE:
			{
				/*
				 * ListIterator on multiple elements
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on multiple elements");
				int count = 0;
				int expectedNextIndex = 0;
				// Forward iteration
				while (count < chainLength)
				{
					assertEquals(expectedNextIndex,
					             iterator.nextIndex(),
					             testName + " unexpected nextIndex value");
					iterator.next();
					expectedNextIndex++;
					count++;
				}
				// Backward iteration
				int nextIndex;
				while (count > 0)
				{
					nextIndex = iterator.nextIndex();
					assertEquals(expectedNextIndex,
					             nextIndex,
					             testName + " unexpected nextIndex value");
					iterator.previous();
					expectedNextIndex--;
					count--;
				}
				/**
				 * next index after remove
				 */
				iterator = factory.multipleListIterator(Arrays.asList(words));
				assertNotNull(iterator,
				              testName + " unexpected null iterator on multiple elements");
				count = 0;
				while (count < chainLength)
				{
					iterator.next();
					/*
					 * removes the last element returned by next: the next index should
					 * remain 0.
					 */
					iterator.remove();
					assertEquals(0,
					             iterator.nextIndex(),
					             testName
					                 + " unexpected next index value after remove");
					count++;
				}

				iterator = factory.multipleListIterator(Arrays.asList(words));
				assertNotNull(iterator,
				              testName + " unexpected null iterator on multiple elements");

				count = 0;
				while (count < chainLength)
				{
					iterator.next();
					count++;
				}
				while (count > 0)
				{
					iterator.previous();
					/*
					 * removes the last element returned by previous: the next index
					 * should decrease.
					 */
					iterator.remove();
					assertEquals(count - 1,
					             iterator.nextIndex(),
					             testName
					                 + " unexpected next index value after remove");
					count--;
				}

				/**
				 * next index after add
				 */
				iterator = factory.multipleListIterator(Arrays.asList(words));
				assertNotNull(iterator,
				              testName + " unexpected null iterator on multiple elements");
				count = 0;
				expectedNextIndex = 0;
				assertEquals(0,
				             iterator.nextIndex(),
				             testName + " unexpected next index value");
				while (count < chainLength)
				{
					iterator.next(); // increments next Index
					try
					{
						iterator.add(wordSupplier.get()); // increments next Index
					}
					catch (UnsupportedOperationException e)
					{
						fail(testName + " unexpected unsupported operation: "
						    + e.getLocalizedMessage());
					}
					expectedNextIndex += 2;
					assertEquals(expectedNextIndex,
					             iterator.nextIndex(),
					             testName + " unexpected next index value after add");
					count++;
				}
				int providedNextIndex =  iterator.nextIndex();
				expectedNextIndex = chainLength * 2;
				assertEquals(expectedNextIndex,
				             providedNextIndex,
				             testName + " unexpected next index value after add loop");
				count = 0;
				while (count < chainLength)
				{
					iterator.previous();
					try
					{
						iterator.add(wordSupplier.get());
					}
					catch (UnsupportedOperationException e)
					{
						fail(testName + " unexpected unsupported operation: "
						    + e.getLocalizedMessage());
					}
					providedNextIndex = iterator.nextIndex();
					assertEquals(expectedNextIndex,
					             providedNextIndex,
					             testName + " unexpected next index value after add");
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
	 * Test method for {@link ListIterator#previousIndex()}.
	 * @param iterator The iterator to test
	 * @param kind the type of provided iterator: Either empty, single or
	 * multiple
	 * @param factory The factory to build {@link ListIterator}s
	 * @param testInfo informations about the current test
	 */
	@ParameterizedTest(name = "[{index}] {1} {0}")
	@MethodSource("listIteratorKindAndFactoryProvider")
	@DisplayName("previousIndex()")
	@Order(6)
	final void testPreviousIndex(ListIterator<String> iterator,
	                             IteratorKind kind,
	                             ListIteratorFactory<String> factory,
	                             TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);
		switch (kind)
		{
			case EMPTY:
			{
				/*
				 * ListIterator on nothing
				 */
				assertNotNull(iterator,
				              testName
				                  + " unexpected null iterator on no elements");
				assertNotNull(iterator,
				              testName
				                  + " unexpected null iterator on no elements");
				assertEquals(-1,
				             iterator.previousIndex(),
				             testName
				                 + " unexpected previousIndex value on empty iterator");
				break;
			}
			case SINGLE:
			{
				/*
				 * ListIterator on single element
				 */
				assertNotNull(iterator,
				              testName
				                  + " unexpected null iterator on single element");
				assertEquals(-1,
				             iterator.previousIndex(),
				             testName + " unexpected previousIndex value");
				break;
			}
			case MULTIPLE:
			{
				/*
				 * ListIterator on multiple elements
				 */
				assertNotNull(iterator,
				              testName
				                  + " unexpected null iterator on multiple elements");
				int count = 0;
				int expectedPreviousIndex = -1;
				// Forward iteration
				while (count < chainLength)
				{
					assertEquals(expectedPreviousIndex,
					             iterator.previousIndex(),
					             testName + " unexpected previousIndex value");
					iterator.next();
					expectedPreviousIndex++;
					count++;
				}
				// Backward iteration
				while (count > 0)
				{
					assertEquals(expectedPreviousIndex,
					             iterator.previousIndex(),
					             testName + " unexpected previousIndex value");
					iterator.previous();
					expectedPreviousIndex--;
					count--;
				}

				assertEquals(-1,
				             iterator.previousIndex(),
				             testName + " unexpected previous index value");

				/**
				 * next index after remove
				 */
				iterator = factory.multipleListIterator(Arrays.asList(words));
				assertNotNull(iterator,
				              testName
				                  + " unexpected null iterator on multiple elements");
				count = 0;
				while (count < chainLength)
				{
					iterator.next();
					/*
					 * removes the last element returned by next: the previous
					 * index should remain -1.
					 */
					iterator.remove();
					assertEquals(-1,
					             iterator.previousIndex(),
					             testName
					                 + " unexpected previous index value after remove");
					count++;
				}

				iterator = factory.multipleListIterator(Arrays.asList(words));
				assertNotNull(iterator,
				              testName
				                  + " unexpected null iterator on multiple elements");
				count = 0;
				while (count < chainLength)
				{
					iterator.next();
					count++;
				}
				while (count > 0)
				{
					iterator.previous();
					/*
					 * removes the last element returned by previous: the
					 * previous index should decrease.
					 */
					iterator.remove();
					assertEquals(count - 2,
					             iterator.previousIndex(),
					             testName
					                 + " unexpected next index value after remove");
					count--;
				}

				/**
				 * next index after add
				 */
				iterator = factory.multipleListIterator(Arrays.asList(words));
				assertNotNull(iterator,
				              testName
				                  + " unexpected null iterator on multiple elements");
				count = 0;
				expectedPreviousIndex = -1;
				assertEquals(expectedPreviousIndex,
				             iterator.previousIndex(),
				             testName + " unexpected next index value");
				while (count < chainLength)
				{
					iterator.next(); // increments next Index
					iterator.add(wordSupplier.get()); // increments next Index
					expectedPreviousIndex += 2;
					assertEquals(expectedPreviousIndex,
					             iterator.previousIndex(),
					             testName
					                 + " unexpected previous index value after add");
					count++;
				}
				expectedPreviousIndex = (chainLength * 2) - 1;
				int providedPreviousIndex = iterator.previousIndex();
				assertEquals(expectedPreviousIndex,
				             providedPreviousIndex,
				             testName
				                 + " unexpected previous index value after add loop");
				count = 0;
				final int available = iterator.nextIndex();
				while (count < available)
				{
					iterator.previous();
					try
					{
						// successful add operation will maintain the same
						// previous
						// Index
						iterator.add(wordSupplier.get());
					}
					catch (UnsupportedOperationException e)
					{
						fail(testName + " unexpected unsupported operation: "
						    + e.getLocalizedMessage());
					}
					assertEquals(expectedPreviousIndex,
					             iterator.previousIndex(),
					             testName
					                 + " unexpected previous index value after add");
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
	 * Test method for {@link ListIterator#remove()}.
	 * @param iterator The iterator to test
	 * @param kind the type of provided iterator: Either empty, single or
	 * multiple
	 * @param factory The factory to build {@link ListIterator}s
	 * @param testInfo informations about the current test
	 */
	@ParameterizedTest(name = "[{index}] {1} {0}")
	@MethodSource("listIteratorKindAndFactoryProvider")
	@DisplayName("remove()")
	@Order(7)
	final void testRemove(ListIterator<String> iterator,
	                      IteratorKind kind,
	                      ListIteratorFactory<String> factory,
	                      TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);
		switch (kind)
		{
			case EMPTY:
			{

				/*
				 * ListIterator on nothing
				 */
				assertNotNull(iterator,
				              testName
				                  + " unexpected null iterator on no elements");
				assertThrows(IllegalStateException.class, () -> {
					factory.emptyListIterator().remove();
				}, testName + " remove() didn't throw exception on empty iterator");
				break;
			}
			case SINGLE:
			{
				/*
				 * ListIterator on single element
				 */
				assertThrows(IllegalStateException.class, () -> {
					factory.singleListIterator(word).remove();
				}, testName + " remove() didn't throw exception");
				assertNotNull(iterator,
				              testName
				                  + " unexpected null iterator on single element");
				try
				{
					iterator.next();
					iterator.remove();
				}
				catch (NoSuchElementException e)
				{
					fail(testName + " next() failed: "
					    + e.getLocalizedMessage());
				}
				catch (IllegalStateException e)
				{
					fail(testName + "remove() failed: "
					    + e.getLocalizedMessage());
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
				assertEquals(-1,
				             iterator.previousIndex(),
				             testName + " unexpected previous index value");
				assertEquals(0,
				             iterator.nextIndex(),
				             testName + " unexpected next index value");
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
				 * ListIterator on multiple elements
				 */
				assertNotNull(iterator,
				              testName
				                  + " unexpected null iterator on multiple elements");
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

				iterator =
				    factory.multipleListIterator(Arrays.asList(words));
				assertNotNull(iterator,
				              testName
				                  + " unexpected null iterator on multiple elements");
				// Forward iteration
				count = 0;
				while (count < chainLength)
				{
					iterator.next();
					count++;
				}
				int size = count;
				// Backward iteration
				while (count >= 0)
				{
					try
					{
						iterator.previous();
					}
					catch (NoSuchElementException e)
					{
						if (count != 0)
						{
							fail(testName + " unexpected previous() fail: "
							    + e.getLocalizedMessage());
						}
						else
						{
							// Nothing: expected
						}
					}
					try
					{
						/*
						 * Remove can only be performed when there is a last
						 * element to
						 * remove
						 */
						iterator.remove();
					}
					catch (IllegalStateException e)
					{
						if ((count != size) && (count != 0))
						{
							fail(testName + " unexpected remove() fail: "
							    + e.getLocalizedMessage());
						}
						else
						{
							// Nothing: expected
						}
					}
					count--;
				}
				// Adding after remove should not fail
				String lastWord = null;
				try
				{
					lastWord = wordSupplier.get();
					iterator.add(lastWord);
				}
				catch (IllegalStateException e)
				{
					fail(testName
					    + " unexpected add(E) failure after remove(): "
					    + e.getLocalizedMessage());
				}
				try
				{
					String removed = iterator.previous();
					assertEquals(lastWord,
					             removed,
					             testName
					                 + " unexpected previous() value after add()");
				}
				catch (NoSuchElementException e)
				{
					fail(testName + " unexpected previous() fail: "
					    + e.getLocalizedMessage());
				}
				try
				{
					iterator.remove();
				}
				catch (IllegalStateException e)
				{
					fail(testName
					    + " unexpected remove() failure after add() and previous(): "
					    + e.getLocalizedMessage());
				}

				/*
				 * "it" should now be an iterator on an empty chain
				 */
				assertFalse(iterator.hasNext(),
				            testName + " unexpected next presence");
				assertFalse(iterator.hasPrevious(),
				            testName + " unexpected previous presence");
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
	 * Test method for
	 * {@link ListIterator#set(java.lang.Object)}.
	 * @param iterator The iterator to test
	 * @param kind the type of provided iterator: Either empty, single or
	 * multiple
	 * @param testInfo informations about the current test
	 */
	@ParameterizedTest(name = "[{index}] {1} {0}")
	@MethodSource("listIteratorAndKindProvider")
	@DisplayName("set(E)")
	@Order(8)
	final void testSet(ListIterator<String> iterator,
	                   IteratorKind kind,
	                   TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		switch (kind)
		{
			case EMPTY:
			{
				/*
				 * ListIterator on nothing
				 */
				assertNotNull(iterator,
				              testName
				                  + " unexpected null iterator on no elements");
				assertThrows(IllegalStateException.class, () -> {
					iterator.set("impossible");
				}, testName + " set(E) didn't throw exception on empty iterator");
				break;
			}
			case SINGLE:
			{
				/*
				 * ListIterator on single element
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on single element");
				assertThrows(IllegalStateException.class, () -> {
					// set without next() or previous() call should fail
					iterator.set("impossible");
				}, testName + " set(E) didn't throw exception");
				String expected = wordSupplier.get();
				try
				{
					iterator.next();
					// set on next
					iterator.set(expected);
					// assert on previous
					assertEquals(expected,
					             iterator.previous(),
					             testName
					                 + " unexpected previous() value after set(E)");
				}
				catch (NoSuchElementException e)
				{
					fail(testName + " next() failed: " + e.getLocalizedMessage());
				}
				catch (IllegalStateException e)
				{
					fail(testName + "set(E) failed: " + e.getLocalizedMessage());
				}

				expected = wordSupplier.get();
				try
				{
					// set on previous
					iterator.set(expected);
					// assert on next
					assertEquals(expected,
					             iterator.next(),
					             testName + " unexpected next() value after set(E)");
				}
				catch (NoSuchElementException e)
				{
					fail(testName + " next() failed: " + e.getLocalizedMessage());
				}
				catch (IllegalStateException e)
				{
					fail(testName + "set(E) failed: " + e.getLocalizedMessage());
				}
				break;
			}
			case MULTIPLE:
			{
				/*
				 * ListIterator on multiple elements
				 */
				assertNotNull(iterator,
				              testName
				                  + " unexpected null iterator on multiple elements");
				String expected = null;
				try
				{
					iterator.set(wordSupplier.get());
					fail(testName + " unexpected set(E) success");
				}
				catch (IllegalStateException e)
				{
					// Nothing: expected
				}
				int count = 0;
				String provided = null;
				// Forward iteration
				while (count < chainLength)
				{
					try
					{
						iterator.next();
					}
					catch (NoSuchElementException e)
					{
						fail(testName + " unexpected next fail:"
						    + e.getLocalizedMessage());
					}

					expected = wordSupplier.get();
					try
					{
						iterator.set(expected);
					}
					catch (IllegalStateException e)
					{
						fail(testName + " unexpected set(E) failed: "
						    + e.getLocalizedMessage());
					}

					try
					{
						provided = iterator.previous();
						assertEquals(expected,
						             provided,
						             testName + " unexpected previous value");
					}
					catch (NoSuchElementException e)
					{
						fail(testName + " unexpected previous fail:"
						    + e.getLocalizedMessage());
					}

					try
					{
						iterator.next();
					}
					catch (NoSuchElementException e)
					{
						fail(testName + " unexpected next fail:"
						    + e.getLocalizedMessage());
					}
					count++;
				}
				expected = wordSupplier.get();
				/**
				 * As long as there is a last element, set is possible
				 */
				try
				{
					iterator.set(expected);
				}
				catch (IllegalStateException e)
				{
					fail(testName + " unexpected set(E) failed: "
					    + e.getLocalizedMessage());
				}

				try
				{
					provided = iterator.previous();
					assertEquals(expected,
					             provided,
					             testName + " unexpected previous() value");
					iterator.next();
				}
				catch (NoSuchElementException e)
				{
					fail(testName + " unexpected previous() or next() fail: "
					    + e.getLocalizedMessage());
				}

				// Backward iteration
				while (count > 0)
				{
					try
					{
						iterator.previous();
					}
					catch (NoSuchElementException e)
					{
						fail(testName + " unexpected previous fail: "
						    + e.getLocalizedMessage());
					}

					expected = wordSupplier.get();
					boolean setDone = false;
					try
					{
						iterator.set(expected);
						setDone = true;
					}
					catch (IllegalStateException e)
					{
						fail(testName + " unexpected set(E) failed: "
						    + e.getLocalizedMessage());
					}
					if (setDone)
					{
						try
						{
							provided = iterator.next();
							assertEquals(expected,
							             provided,
							             testName + " unexpected next() value");
							iterator.previous();
						}
						catch (NoSuchElementException e)
						{
							fail(testName + " unexpected previous() or next() fail: "
							    + e.getLocalizedMessage());
						}
					}
					count--;
				}

				/*
				 * Setting null always fail (in our implementation only)
				 */
				boolean customIterator = isCustom(iterator);
				try
				{
					iterator.set(null);
					if (customIterator)
					{
						fail(testName + " unexpected set(null) success");
					}
				}
				catch (IllegalArgumentException e)
				{
					// Nothing: expected
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
	 * Test method for
	 * {@link ListIterator#add(java.lang.Object)}.
	 * @param iterator The iterator to test
	 * @param kind the type of provided iterator: Either empty, single or
	 * multiple
	 * @param testInfo informations about the current test
	 */
	@ParameterizedTest(name = "[{index}] {1} {0}")
	@MethodSource("listIteratorAndKindProvider")
	@DisplayName("add(E)")
	@Order(9)
	final void testAdd(ListIterator<String> iterator,
	                   IteratorKind kind,
	                   TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);
		switch (kind)
		{
			case EMPTY:
			{
				/*
				 * ListIterator on nothing
				 */
				boolean customIterator = isCustom(iterator);
				try
				{
					iterator.add(null);
					if (customIterator)
					{
						fail(testName + " unexpected add(null) success");
					}
				}
				catch (IllegalArgumentException e)
				{
					// Nothing: expected
				}

				String expected = wordSupplier.get();
				int expectedNextIndex = iterator.nextIndex() + 1;
				try
				{
					iterator.add(expected);
					assertEquals(expectedNextIndex,
					             iterator.nextIndex(),
					             testName + " unexpected nextIndex value");
				}
				catch (IllegalStateException e)
				{
					fail(testName + " unexpected add(E) fail: "
					    + e.getLocalizedMessage());
				}
				catch (UnsupportedOperationException e)
				{
					fail(testName + " unexpected unsupported operation: "
					    + e.getLocalizedMessage());
				}

				try
				{
					/*
					 * I thought further call to add should fail,
					 * but nothing prevents us from adding multiple elements, so do it
					 */
					iterator.add(expected);
				}
				catch (IllegalStateException e)
				{
					fail(testName + " unexpected second add(E) failure: "
					    + e.getLocalizedMessage());
				}
				catch (UnsupportedOperationException e)
				{
					fail(testName + " unexpected unsupported operation: "
					    + e.getLocalizedMessage());
				}

				try
				{
					// further modification should fail
					iterator.remove();
					fail(testName + " unexpected remove() success");
				}
				catch (IllegalStateException e)
				{
					// Nothing: expected
				}

				assertFalse(iterator.hasNext(), testName + " unexpected next presence");
				assertTrue(iterator.hasPrevious(), testName + " unexpected no previous");

				try
				{
					iterator.next();
					fail(testName + " unexpected next() success");
				}
				catch (NoSuchElementException e)
				{
					// Nothing: expected
				}

				try
				{
					assertEquals(expected,
					             iterator.previous(),
					             testName + " unexpected previous() value");
				}
				catch (NoSuchElementException e)
				{
					fail(testName + " unexpected previous() fail: "
					    + e.getLocalizedMessage());
				}

				break;
			}
			case SINGLE:
			{
				/*
				 * ListIterator on single element
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on single element");
				String expected = wordSupplier.get();
				int expectedNextIndex = iterator.nextIndex() + 1;
				try
				{
					iterator.add(expected);
					assertEquals(expectedNextIndex,
					             iterator.nextIndex(),
					             testName + " unexpected nextIndex value");
				}
				catch (IllegalStateException e)
				{
					fail(testName + " unexpected add(E) fail: "
					    + e.getLocalizedMessage());
				}
				assertTrue(iterator.hasNext(), testName + " unexpected no next");
				assertTrue(iterator.hasPrevious(), testName + " unexpected no previous");
				try
				{
					assertEquals(expected,
					             iterator.previous(),
					             testName + " unexpected previous() value");
					assertEquals(expected,
					             iterator.next(),
					             testName + " unexpected next() value");
				}
				catch (NoSuchElementException e)
				{
					fail(testName + " unexpected next() or previous() fail: "
					    + e.getLocalizedMessage());
				}
				try
				{
					assertEquals(word,
					             iterator.next(),
					             testName + " unexpected next() value");
				}
				catch (NoSuchElementException e)
				{
					fail(testName + " unexpected next() fail: "
					    + e.getLocalizedMessage());
				}

				boolean customIterator = isCustom(iterator);
				try
				{
					iterator.add(null);
					if (customIterator)
					{
						fail(testName + " unexpected add(null) success");
					}
				}
				catch (IllegalArgumentException e)
				{
					// Nothing: expected
				}
				break;
			}
			case MULTIPLE:
			{
				/*
				 * ListIterator on multiple elements
				 */
				assertNotNull(iterator,
				              testName + " unexpected null iterator on multiple elements");
				String expected = wordSupplier.get();
				int expectedNextIndex = iterator.nextIndex() + 1;
				try
				{
					iterator.add(expected);
					assertEquals(expectedNextIndex,
					             iterator.nextIndex(),
					             testName + " unexpected nextIndex value");
				}
				catch (IllegalStateException e)
				{
					fail(testName + " unexpected add(E) fail: "
					    + e.getLocalizedMessage());
				}
				assertTrue(iterator.hasNext(), testName + " unexpected no next");
				assertTrue(iterator.hasPrevious(), testName + " unexpected no previous");
				try
				{
					assertEquals(expected,
					             iterator.previous(),
					             testName + " unexpected previous() value");
					assertEquals(expected,
					             iterator.next(),
					             testName + " unexpected next() value");
				}
				catch (NoSuchElementException e)
				{
					fail(testName + " unexpected next() or previous() fail: "
					    + e.getLocalizedMessage());
				}

				while (iterator.hasNext())
				{
					iterator.next();
				}

				expected = wordSupplier.get();
				try
				{
					iterator.add(expected);
				}
				catch (IllegalArgumentException e)
				{
					fail(testName + " unexpected add fail: "
					    + e.getLocalizedMessage());
				}
				assertThrows(NoSuchElementException.class, () -> {
					iterator.next();
				}, testName + " unexpected next success after add at the end");
				assertEquals(expected,
				             iterator.previous(),
				             testName + " unexpected previous value after add at the end");

				while (iterator.hasPrevious())
				{
					iterator.previous();
				}

				expected = wordSupplier.get();
				try
				{
					iterator.add(expected);
				}
				catch (IllegalStateException e)
				{
					fail(testName + " unexpected add fail: "
					    + e.getLocalizedMessage());
				}
				assertEquals(expected,
				             iterator.previous(),
				             testName + " unexpected previous value after add at the beginning");

				boolean customIterator = isCustom(iterator);
				try
				{
					iterator.add(null);
					if (customIterator)
					{
						fail(testName + " unexpected add(null) success");
					}
				}
				catch (IllegalArgumentException e)
				{
					// Nothing: expected
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
}
