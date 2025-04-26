package tests;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import collections.lists.ListFactory;
import collections.lists.NodeList;
import collections.lists.NodeSequentialList;
import collections.nodes.Headed;
import collections.nodes.Node;

/**
 * A Class to test all kinds of {@link List}s
 * @author davidroussel
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("List<E>")
public class ListTest
{
	/**
	 * Different natures of lists to test
	 */
	@SuppressWarnings("unchecked")
	private static final Class<? extends List<?>>[] listTypes =
		(Class<? extends List<?>>[]) new Class<?>[] {
//			ArrayList.class,
//			LinkedList.class,
			NodeList.class,
			NodeSequentialList.class
		};

	/**
	 * The list under test
	 */
	private List<String> testList;

	/**
	 * The type of list to test
	 */
	private Class<? extends List<String>> testListType;

	/**
	 * The name of the type of list to test
	 */
	private String testListTypeName;

	/**
	 * The name of the current test in all testXXX methods
	 */
	private String testName;

	/**
	 * Elements to fill lists :
	 * "Lorem ipsum dolor sit amet"
	 */
	private static final String[] elements1 = new String[] {
		"Lorem",
		"ipsum",
		"sit",
		"dolor",
		"amet"
	};

	/**
	 * Other elements to fill lists :
	 * "dolor amet consectetur adipisicing elit"
	 */
	private static final String[] elements2 = new String[] {
    	"consectetur",
    	"adipisicing",
    	"elit",
    	"Donec",
    	"ut",
    	"urna",
    	"nisl"
    };

	/**
	 * Complementary elements not present in either {@link #elements1}
	 * nor {@link #elements2}
	 */
	private static final String[] elements3 = new String[] {
		"Curabitur",
		"ut",
		"libero",
		"vestibulum",
		"elit",
		"sagittis",
		"mattis",
		"Mauris",
		"lacinia",
		"elit",
		"eget",
		"eros"
	};

	/**
	 * Elements to fill a list with duplicates.
	 */
	private static final String[] elements = new String[elements1.length + elements2.length];

	/**
	 * Collection to hold elements to fill lists.
	 */
	private List<String> listElements;

	/**
	 * List class provider used for parameterized tests requiring the type of
	 * list
	 * @return a stream of List Classes to use in each ParameterizedTest
	 */
	private static Stream<Class<? extends List<?>>> listClassesProvider()
	{
		return Stream.of(listTypes);
	}

	/**
	 * List class provider used for parameterized tests requiring the type of
	 * list (restricted to classes implementing the {@link Headed} interface)
	 * @return a stream of List Classes to use in each ParameterizedTest
	 */
	private static Stream<Class<? extends List<?>>> headedClassesProvider()
	{
		return listClassesProvider().filter((Class<? extends List<?>> c) -> {
			/*
			 * Keep only classes that uses internal arrays and feature
			 * a constructor with a int argument for initial capacity
			 */
			Class<?>[] interfaces = c.getInterfaces();
			for (Class<?> i : interfaces)
			{
				if (i == Headed.class)
				{
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Utility method indicating if the the provided class has been implemented
	 * in the "collections.lists" package
	 * @param <E> the type of collection content
	 * @param type the type of collection we're investigating
	 * @return true if the provided collection class is located in the
	 * "collections" package
	 */
	private static <E> boolean isCustom(Class<? extends List<E>> type)
	{
		return type.getPackage().getName().equals("collections.lists");
	}

	/**
	 * Creates an instance of a {@code List<String>} according to the type of
	 * list to create and eventually a content to set.
	 * @param testName the message to repeat in each assertion based on the test
	 * this method is used in
	 * @param type the type of list to create
	 * @param content the content to setup in the created list
	 * @return a new list with the required type filled with the required
	 * content
	 * (if provided)
	 */
	private static List<String>
	    constructList(String testName,
	                  Class<? extends List<String>> type,
	                  Collection<String> content)
	{
		List<String> list = null;

		try
		{
			list = ListFactory.<String> getList(type, content);
		}
		catch (SecurityException e)
		{
			fail(testName + " constructor security exception");
		}
		catch (NoSuchMethodException e)
		{
			fail(testName + " constructor not found");
		}
		catch (IllegalArgumentException e)
		{
			fail(testName + " wrong constructor arguments");
		}
		catch (InstantiationException e)
		{
			fail(testName + " instantiation exception");
		}
		catch (IllegalAccessException e)
		{
			fail(testName + " illegal access");
		}
		catch (InvocationTargetException e)
		{
			fail(testName + " invocation exception: " + e.getCause());
		}

		return list;
	}

	/**
	 * Shuffle elements from the provided elements array
	 * @param elements the array containing elements to shuffle
	 * @return an new array containing the same elements as the provided array
	 * with a different order
	 */
	private static String[] shuffleArray(String[] elements)
	{
		/*
		 * CAUTION elements needs to be shuffled in a new list in order to
		 * preserve "elements" order
		 */
		List<String> listElements = new ArrayList<>(Arrays.asList(elements));

		Collections.shuffle(listElements);

		String[] result = new String[elements.length];
		int i = 0;
		for (String elt : listElements)
		{
			result[i++] = elt;
		}

		return result;
	}

	/**
	 * Setup before all tests
	 * @throws java.lang.Exception if setup fails
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
		int j = 0;
		for (int i = 0; i < elements1.length; i++)
		{
			elements[j++] = elements1[i];
		}
		for (int i = 0; i < elements2.length; i++)
		{
			elements[j++] = elements2[i];
		}

		System.out.println("-------------------------------------------------");
		System.out.println("Lists tests");
		System.out.println("-------------------------------------------------");
	}

	/**
	 * Tear down after al tests
	 * @throws java.lang.Exception if teardown fails
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception
	{
		System.out.println("-------------------------------------------------");
		System.out.println("Lists test end");
		System.out.println("-------------------------------------------------");
	}

	/**
	 * Setup variables for a specific test.
	 * To be used in every testXXX(...) methods requiring {@code Lists<String>}
	 * instances.
	 * @param list the list to test
	 * @param testName the name of the current test
	 */
	@SuppressWarnings("unchecked")
	void setUpTest(List<String> list, String testName)
	{
		testList = list;
		testListType = (Class<? extends List<String>>) testList.getClass();
		testListTypeName = testListType.getSimpleName();
		this.testName = testListTypeName + "." + testName;
		System.out.println(this.testName);
	}

	/**
	 * Setup before each test
	 * @throws java.lang.Exception if setup fails
	 */
	@BeforeEach
	void setUp() throws Exception
	{
		listElements = new ArrayList<String>();
		for (String elt : elements)
		{
			listElements.add(elt);
		}
	}

	/**
	 * Teardown after each test
	 * @throws java.lang.Exception if teardown fails
	 */
	@AfterEach
	void tearDown() throws Exception
	{
		testList = null;
		listElements.clear();
		listElements = null;
	}

	/**
	 * Test method for all lists constructors such as
	 * {@link collections.lists.NodeList#NodeList()}
	 * @param type the type of list provided by {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("...List()")
	@Order(1)
	final void testDefaultConstructor(Class<? extends List<String>> type)
	{
		String testName = new String(type.getSimpleName() + "()");
		System.out.println(testName);

		Constructor<? extends List<String>> defaultConstructor = null;
		Class<?>[] constructorsArgs = new Class<?>[0];

		try
		{
			defaultConstructor = type.getConstructor(constructorsArgs);
		}
		catch (NoSuchMethodException e)
		{
			fail(testName + " constructor not found");
		}
		catch (SecurityException e)
		{
			fail(testName + " constructor security exception");
		}

		if (defaultConstructor != null)
		{
			Object instance = null;
			Object[] args = new Object[0];
			try
			{
				instance = defaultConstructor.newInstance(args);
			}
			catch (InstantiationException e)
			{
				fail(testName + " instantiation exception : Abstract class");
			}
			catch (IllegalAccessException e)
			{
				fail(testName + " constructor is inaccessible");
			}
			catch (IllegalArgumentException e)
			{
				fail(testName + " illegal argument");
			}
			catch (InvocationTargetException e)
			{
				fail(testName + " invoked constructor throwed an exception"
				    + e.getCause());
			}

			assertNotNull(instance, testName + " null instance");
			assertEquals(type,
			             instance.getClass(),
			             testName + " unexpected instance class");
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) instance;
			assertTrue(list.isEmpty(),
			           testName + " unexpected non empty on default instance");
			assertEquals(0,
			             list.size(),
			             testName
			                 + " unexpected non 0 size on default instance");
		}
		else
		{
			fail(testName + " null constructor");
		}
	}

	/**
	 * Test method for all lists constructors such as
	 * {@link collections.lists.NodeList#NodeList(Collection)}
	 * @param type the type of list provided by {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("...List(Collection)")
	@Order(2)
	final void testCopyConstructor(Class<? extends List<String>> type)
	{
		String testName = new String(type.getSimpleName() + "(Collection)");
		System.out.println(testName);

		Constructor<? extends List<String>> copyConstructor = null;
		Class<?>[] constructorsArgs = new Class<?>[1];
		constructorsArgs[0] = Collection.class;

		try
		{
			copyConstructor = type.getConstructor(constructorsArgs);
		}
		catch (NoSuchMethodException e)
		{
			fail(testName + " copy constructor not found");
		}
		catch (SecurityException e)
		{
			fail(testName + " copy constructor security exception");
		}

		if (copyConstructor != null)
		{
			Object instance = null;
			Object[] args = new Object[1];
			args[0] = listElements;
			try
			{
				instance = copyConstructor.newInstance(args);
			}
			catch (InstantiationException e)
			{
				fail(testName
				    + " copy instantiation exception : Abstract class");
			}
			catch (IllegalAccessException e)
			{
				fail(testName + " copy constructor is inaccessible");
			}
			catch (IllegalArgumentException e)
			{
				fail(testName + " copy constructor illegal argument");
			}
			catch (InvocationTargetException e)
			{
				fail(testName
				    + " invoked copy constructor throwed an exception: "
					+ e.getCause());
			}
			catch (Exception e)
			{
				fail(testName + " copy constructor exception");
			}

			assertNotNull(instance, testName + " null instance");
			assertEquals(type,
			             instance.getClass(),
			             testName + " unexpected instance class");
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) instance;
			assertFalse(list.isEmpty(),
			            testName + " unexpected empty on copy instance");
			// assert expected elements
			for (int i = 0; i < elements.length; i++)
			{
				assertEquals(elements[i],
				             list.get(i),
				             testName + " unexpected list value at index " + i);
			}
			instance = null;
			args[0] = null;
			try
			{
				instance = copyConstructor.newInstance(args);
			}
			catch (InstantiationException e)
			{
				fail(testName
				    + " copy instantiation exception : Abstract class");
			}
			catch (IllegalAccessException e)
			{
				fail(testName + " copy constructor is inaccessible");
			}
			catch (IllegalArgumentException e)
			{
				fail(testName + " copy constructor illegal argument");
			}
			catch (InvocationTargetException e)
			{
				Throwable thrown = e.getTargetException();
				assertEquals(NullPointerException.class,
				             thrown.getClass(),
				             testName + " unexpected exception thrown");
			}
			catch (Exception e)
			{
				fail(testName + " copy constructor exception");
			}
			assertNull(instance, testName + " non null instance");
		}
		else
		{
			fail(testName + " null constructor");
		}
	}

	/**
	 * Test method for {@link List#size()}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("size()")
	@Order(7)
	final void testSize(Class<? extends List<String>> type)
	{
		String baseTestName = "size()";
		setUpTest(constructList(baseTestName, type, null), baseTestName);

		assertEquals(0,
		             testList.size(),
		             testName + " size 0 on empty list failed");

		testList = constructList(testName, testListType, listElements);
		assertNotNull(testList, testName + " unexpected null list instance");

		assertEquals(elements.length,
		             testList.size(),
		             testName + " size " + elements.length
		                 + " on filled list failed");
	}

	/**
	 * Test method for {@link List#get(int)}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("get(int)")
	@Order(8)
	final void testGetInt(Class<? extends List<String>> type)
	{
		String baseTestName = "get(int)";
		setUpTest(constructList(baseTestName, type, null), baseTestName);

		// Invalid index should trigger an IndexOutOfBoundsException
		assertThrows(IndexOutOfBoundsException.class,
		             () -> {
		            	 testList.get(0);
		             },
		             testName + "didn't throw IndexOutOfBoundsException with 0 "
		             	+ "index on empty list");

		testList = constructList(testName, type, listElements);
		for (int i = 0; i < testList.size(); i++)
		{
			assertEquals(elements[i],
			             testList.get(i),
			             testName + " unexpected element");
		}

		assertThrows(IndexOutOfBoundsException.class,
		             () -> {
		            	 testList.get(-1);
		             },
		             testName + "didn't throw IndexOutOfBoundsException with -1 "
		             	+ "index on empty list");

		assertThrows(IndexOutOfBoundsException.class,
		             () -> {
		            	 testList.get(testList.size());
		             },
		             testName + "didn't throw IndexOutOfBoundsException with "
		             	+ "\"size\" index on empty list");
	}

	/**
	 * Test method for {@link List#set(int, Object)}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("set(int, E)")
	@Order(9)
	final void testSetIntE(Class<? extends List<String>> type)
	{
		String baseTestName = "set(int, E)";
		setUpTest(constructList(baseTestName, type, listElements), baseTestName);
		assertNotNull(testList, testName + " unexpected null filled instance");
		assertEquals(listElements.size(),
		             testList.size(),
		             testName + " unexpected list size");

		// MyArrayList shall not allow setting null elements
		if (isCustom(type))
		{
			// Setting null elt should trigger a NullPointerException
			try
			{
				testList.set(0, null);
				fail(testName + " unexpected successful set of null element");
			}
			catch (NullPointerException e)
			{
				// Nothing: expected
			}
			catch (UnsupportedOperationException e)
			{
				fail(testName + " unexpected UnsupportedOperationException: "
					+ "set(int index, E element) should be overridden");
			}
		}

		// Setting at invalid index should trigger an IndexOutOfBoundsException
		assertThrows(IndexOutOfBoundsException.class,
		             () -> {
		            	 testList.set(-1, elements3[0]);
		             },
		             testName + " didn't throw IndexOutOfBoundsException on "
		             	+ "-1 index");

		assertThrows(IndexOutOfBoundsException.class,
		             () -> {
		            	 testList.set(testList.size(), elements3[0]);
		             },
		             testName + " didn't throw IndexOutOfBoundsException on "
		             	+ "\"size\" index");

		for (int i = 0; i < testList.size(); i++)
		{
			String oldElement;
			String indexString = String.valueOf(i);
			try
			{
				oldElement = testList.set(i, elements3[i]);
				assertEquals(elements[i],
				             oldElement,
				             testName + " unexpected previous element at index "
				                 + indexString);
				assertEquals(elements3[i],
				             testList.get(i),
				             testName + " unexpected new element at index "
				                 + indexString);
			}
			catch (IndexOutOfBoundsException e)
			{
				fail(testName
				    + " unexpected IndexOutOfBoundsException at index "
				    + indexString);
			}
		}
	}

	/**
	 * Test method for {@link List#add(int, Object)}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("add(int, E)")
	@Order(10)
	final void testAddIntE(Class<? extends List<String>> type)
	{
		String baseTestName = "add(int, E)";
		setUpTest(constructList(baseTestName, type, listElements), baseTestName);
		assertNotNull(testList, testName + " unexpected null filled instance");
		assertEquals(listElements.size(),
		             testList.size(),
		             testName + " unexpected list size");

		// Our classes shall not allow setting null elements
		if (isCustom(type))
		{
			assertThrows(NullPointerException.class,
			             () -> {
			            	 testList.add(0, null);
			             },
			             testName + " didn't throw IllegalArgumentException on adding null element");
		}

		testList = constructList(testName, type, listElements);

		assertThrows(IndexOutOfBoundsException.class,
		             () -> {
		            	 testList.add(-1, elements3[0]);
		             },
		             testName + " didn't throw IndexOutOfBoundsException on -1 index");

		assertThrows(IndexOutOfBoundsException.class,
		             () -> {
		            	 testList.add(testList.size() + 1, elements3[0]);
		             },
		             testName + " didn't throw IndexOutOfBoundsException on \"size\" index");

		for (int i = 0; (i < testList.size()) && (i < elements3.length); i++)
		{
			String indexString = String.valueOf(i);
			try
			{
				int expectedSize = testList.size() + 1;
				testList.add(i, elements3[i]);
				assertEquals(expectedSize,
				             testList.size(),
				             testName + " unexpected size after add(int, E)");
			}
			catch (IndexOutOfBoundsException e)
			{
				fail(testName
				    + " unexpected IndexOutOfBoundsException at index "
				    + indexString + " on list of size " + testList.size());
			}
			assertEquals(elements3[i],
			             testList.get(i),
			             testName + " unexpected element at index "
			                 + indexString + " on list of size "
			                 + testList.size());
		}

		int j = 0;
		for (int i = testList.size(); (i >= 0) && (j < elements3.length); i--)
		{
			String indexString = String.valueOf(i);
			try
			{
				int expectedSize = testList.size() + 1;
				testList.add(i, elements3[j]);
				assertEquals(expectedSize,
				             testList.size(),
				             testName + " unexpected size after add(int, E)");
			}
			catch (IndexOutOfBoundsException e)
			{
				fail(testName
				    + " unexpected IndexOutOfBoundsException at index "
				    + indexString + " on list of size " + testList.size());
			}
			assertEquals(elements3[j],
			             testList.get(i),
			             testName + " unexpected element at index "
			                 + indexString + " on list of size "
			                 + testList.size());
			j++;
		}
	}

	/**
	 * Test method for {@link List#remove(int)}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("remove(int)")
	@Order(11)
	final void testRemoveInt(Class<? extends List<String>> type)
	{
		String baseTestName = "remove(int)";
		setUpTest(constructList(baseTestName, type, listElements), baseTestName);
		assertNotNull(testList, testName + " unexpected null filled instance");
		assertEquals(listElements.size(),
		             testList.size(),
		             testName + " unexpected list size");

		/*
		 * Remove at invalid indices
		 */
		try
		{
			testList.remove(testList.size());
			fail(testName + " remove(int) didn't throw exception on invalid index");
		}
		catch (IndexOutOfBoundsException e)
		{
			// Nothing: expected
		}
		catch(UnsupportedOperationException e)
		{
			fail(testName + " unexpected UnsupportedOperationException: "
				+ "remove(int) should be overridden");
		}

		assertThrows(IndexOutOfBoundsException.class,
		             () -> {
		            	 testList.remove(-1);
		             },
		             testName + " remove(int) didn't throw exception on invalid index");

		/*
		 * Remove at start index
		 */
		while (testList.size() > 0)
		{
			String expectedElement = testList.get(0);
			try
			{
				String providedElement = testList.remove(0);
				assertEquals(expectedElement,
				             providedElement,
				             testName + " unexpected removed element after remove(0)");
			}
			catch (Exception e)
			{
				fail(testName + " unexpected exception during remove(0) on "
				    + testList.size() + " sized list");
			}
		}

		/*
		 * Remove at end index
		 */
		testList = constructList(testName, type, listElements);
		while (testList.size() > 0)
		{
			int endIndex = testList.size() - 1;
			String expectedElement = testList.get(endIndex);
			try
			{
				String providedElement = testList.remove(endIndex);
				assertEquals(expectedElement,
				             providedElement,
				             testName + " unexpected removed element after remove(size - 1)");
			}
			catch (Exception e)
			{
				fail(testName
				    + " unexpected exception during remove(size - 1) on "
				    + (endIndex + 1) + " sized list");
			}
		}

		/*
		 * Remove at random valid indices
		 */
		testList = constructList(testName, type, listElements);
		Random gen = new Random();
		while(testList.size() > 0)
		{
			int size = testList.size();
			int index = gen.nextInt(size);
			String expectedElement = testList.get(index);
			try
			{
				String providedElement = testList.remove(index);
				assertEquals(expectedElement,
				             providedElement,
				             testName + " unexpected removed element");
			}
			catch (Exception e)
			{
				fail(testName + " unexpected exception during remove(" + index
				    + ") on " + size + " sized list");
			}
		}
	}

	/**
	 * Test method for {@link List#add(Object)}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("add(E)")
	@Order(12)
	final void testAddE(Class<? extends List<String>> type)
	{
		String baseTestName = "add(E)";
		setUpTest(constructList(baseTestName, type, null), baseTestName);

		assertNotNull(testList, testName + " unexpected null list instance");
		assertEquals(0,
		             testList.size(),
		             testName + " initial empty list doesn't have 0 size");

		int expectedSize = 1;
		if (isCustom(type))
		{
			expectedSize = 0;
		}
		try
		{
			testList.add(null);
		}
		catch (NullPointerException e)
		{
			if (!isCustom(type))
			{
				fail(testName + " unexpected NullPointerException when adding "
					+ "null element in standard List");
			}
		}
		catch (UnsupportedOperationException e)
		{
			fail(testName + " unexpected UnsupportedOperationException: "
				+ "add(int index, E element) should be overridden");
		}

		assertEquals(expectedSize, testList.size(), testName + " unexpected size");

		for (int i = 0; i < elements3.length; i++)
		{
			expectedSize = testList.size() + 1;
			boolean result = testList.add(elements3[i]);
			assertTrue(result, testName + " unexpected add(E) result");
			assertEquals(expectedSize,
			             testList.size(),
			             testName + " unexpected size after add(E)");
			assertEquals(elements3[i],
			             testList.get(testList.size() - 1),
			             testName + " unexpected element value after add(E)");
		}
	}

	/**
	 * Test method for {@link List#hashCode()}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("hashCode()")
	@Order(13)
	final void testHashCode(Class<? extends List<String>> type)
	{
		String baseTestName = "hashCode()";
		setUpTest(constructList(baseTestName, type, null), baseTestName);

		assertNotNull(testList, testName + " unexpected null list instance");
		assertEquals(0,
		             testList.size(),
		             testName + " initial empty list doesn't have 0 size");

	    int expectedHash = 1;
		assertEquals(expectedHash,
		             testList.hashCode(),
		             testName + " unexpected hash code on empty list");

		testList = constructList(testName, type, listElements);
	    final int prime = 31;
	    for (String elt : testList)
	    {
	    	expectedHash = (prime * expectedHash) + (elt == null ? 0 : elt.hashCode());
	    }
		assertEquals(expectedHash,
		             testList.hashCode(),
		             testName + " unexpected hash code on full list");
	}

	/**
	 * Test method for {@link List#equals(Object)}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@SuppressWarnings("unlikely-arg-type")
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("equals(Object)")
	@Order(14)
	final void testEquals(Class<? extends List<String>> type)
	{
		String baseTestName = "equals(Object)";
		setUpTest(constructList(baseTestName, type, null), baseTestName);

		assertNotNull(testList, testName + " unexpected null list instance");
		assertEquals(0,
		             testList.size(),
		             testName + " initial empty list doesn't have 0 size");

		/*
		 * Inequality with null (always)
		 */
		assertFalse(testList.equals(null),
		            testName + "unexpected equality with null");
		/*
		 * Equality with self (always)
		 */
		assertTrue(testList.equals(testList),
		            testName + "unexpected inequality with self");

		/*
		 * Equality with another list
		 */
		testList = constructList(testName, type, listElements);
		assertIterableEquals(testList,
		                     listElements,
		                     testName + " unexpected not same content with listElements");
		assertTrue(testList.equals(listElements),
		           testName + " unexpected inequality with listElements");

		/*
		 * Inequality with another collection which is not a list
		 * (even with same content)
		 */
		LinkedBlockingQueue<String> lbq = new LinkedBlockingQueue<>(listElements);
		assertIterableEquals(testList,
		                     lbq,
		                     testName + " unexpected not same content with Queue");
		assertFalse(testList.equals(lbq),
		            testName + " unexpected equality with a Queue");

		/*
		 * Inequality with another list featuring same content + 1 elt
		 */
		listElements.add(elements[0]);
		assertFalse(testList.equals(listElements),
		            testName +  " unexpected equality with different content (+1 elt)");

		/*
		 * Inequality with another list featuring same content - 1 elt
		 */
		listElements.remove(listElements.size() - 1);
		assertTrue(testList.equals(listElements),
		            testName +  " unexpected inequality with same content list");

		Collection<String> colElements = listElements;
		assertTrue(testList.equals(colElements),
		            testName +  " unexpected inequality with same content collection");

		Iterable<String> itElements = listElements;
		assertTrue(testList.equals(itElements),
		            testName +  " unexpected inequality with same content iterable");

		listElements.remove(listElements.size() - 1);
		assertFalse(testList.equals(listElements),
		            testName +  " unexpected equality with different content (-1 elt)");
	}

	/**
	 * Test method for {@link List#iterator()}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("iterator()")
	@Order(15)
	final void testIterator(Class<? extends List<String>> type)
	{
		String baseTestName = "iterator()";
		setUpTest(constructList(baseTestName, type, null), baseTestName);

		assertNotNull(testList, testName + " unexpected null list instance");
		assertEquals(0,
		             testList.size(),
		             testName + " initial empty list doesn't have 0 size");

		Iterator<String> it = testList.iterator();
		assertNotNull(it, testName + " unexpected null iterator");
		assertFalse(it.hasNext(), testName + " unexpected iterator state");

		testList = constructList(testName, type, listElements);
		it = testList.iterator();
		assertNotNull(it, testName + " unexpected null iterator");
		assertTrue(it.hasNext(), testName + " unexpected iterator state");

		int i = 0;
		while (i < elements.length)
		{
			try
			{
				assertEquals(elements[i],
				             it.next(),
				             testName + " unexpected value from iterator at index "
				                 + i);
			}
			catch (NoSuchElementException e)
			{
				fail(testName + " unexpected NoSuchElementException at index " + i);
			}
			i++;
			assertEquals(i < elements.length,
			             it.hasNext(),
			             testName + " unexpected hasNext state at index " + i);
		}

		assertThrows(NoSuchElementException.class,
		             () -> {
		            	 Iterator<String> ite = testList.iterator();
		            	 while (ite.hasNext())
		            	 {
		            		 ite.next();
		            	 }
		            	 // last call to next should trigger exception
		            	 ite.next();
		             },
		             testName + " unexpected next result on terminated iterator");
		it = testList.iterator();
		i = 0;
		int expectedSize = testList.size();
		while (it.hasNext())
		{
			assertEquals(elements[i],
			             it.next(),
			             testName + " unexpected iterator value at index " + i);
			try
			{
				it.remove();
				assertEquals(--expectedSize,
				             testList.size(),
				             testName + " unexpected list size after remove at step " + i);
			}
			catch (IllegalStateException e)
			{
				fail(testName + " unexpected IllegalStatException during remove of index " + i);
			}

			try
			{
				it.remove();
				fail(testName + " unexpected second remove success");
			}
			catch (IllegalStateException e)
			{
				// Nothing : Expected
			}
			i++;
		}

		for (i = 0; i < elements.length; i++)
		{
			testList = constructList(testName, type, listElements);
			expectedSize = testList.size() - 1;
			it = testList.iterator();
			String element = null;
			int j = 0;
			for (; j <= i; j++)
			{
				element = it.next();
			}
			try
			{
				it.remove();
				assertEquals(expectedSize,
				             testList.size(),
				             testName
				                 + " unexpected collection size after remove of "
				                 + element + " at step " + j);
			}
			catch (IllegalStateException e)
			{
				fail(testName + " unexpected IllegalStatException during remove of index " + j);
			}
		}
	}

	/**
	 * Test method for {@link List#clear()}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("clear()")
	@Order(16)
	final void testClear(Class<? extends List<String>> type)
	{
		String baseTestName = "clear()";
		setUpTest(constructList(baseTestName, type, listElements), baseTestName);
		assertNotNull(testList, testName + " unexpected null filled instance");
		assertEquals(listElements.size(),
		             testList.size(),
		             testName + " unexpected list size");

		testList.clear();

		assertEquals(0,
		             testList.size(),
		             testName + " unexpected list size after clear");
	}

	/**
	 * Test method for {@link List#indexOf(Object)}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("indexOf(E)")
	@Order(17)
	final void testIndexOf(Class<? extends List<String>> type)
	{
		String baseTestName = "indexOf(Object)";
		setUpTest(constructList(baseTestName, type, listElements), baseTestName);

		assertNotNull(testList, testName + " unexpected null filled instance");
		assertEquals(listElements.size(),
		             testList.size(),
		             testName + " unexpected filled instance size");

		for (int i = 0; i < elements.length; i++)
		{
			testList.add(elements[i]);
		}

		for (int i = 0; i < (elements.length * 2); i++)
		{
			int sourceIndex = i%elements.length;
			assertEquals(sourceIndex,
			             testList.indexOf(elements[sourceIndex]),
			             testName + " unexpected indexOf(" + elements[sourceIndex] +")");
		}

		assertEquals(-1,
		             testList.indexOf(elements3[0]),
		             testName + " unexpected valid index on foreign element");
	}

	/**
	 * Test method for
	 * {@link List#lastIndexOf(Object)}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("lastIndexOf(Object)")
	@Order(18)
	final void testLastIndexOf(Class<? extends List<String>> type)
	{
		String baseTestName = "lastIndexOf(Object)";
		setUpTest(constructList(baseTestName, type, listElements), baseTestName);

		assertNotNull(testList, testName + " unexpected null filled instance");
		assertEquals(listElements.size(),
		             testList.size(),
		             testName + " unexpected filled instance size");

		for (int i = 0; i < elements.length; i++)
		{
			testList.add(elements[i]);
		}

		for (int i = (elements.length * 2) - 1; i >= 0 ; i--)
		{
			int sourceIndex = i%elements.length;
			assertEquals(sourceIndex + elements.length,
			             testList.lastIndexOf(elements[sourceIndex]),
			             testName + " unexpected indexOf(" + elements[sourceIndex] +")");
		}

		assertEquals(-1,
		             testList.lastIndexOf(elements3[0]),
		             testName + " unexpected valid index on foreign element");
	}

	/**
	 * Test method for {@link List#addAll(int, Collection)}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("addAll(int, Collection)")
	@Order(19)
	final void
	    testAddAllIntCollectionOfQextendsE(Class<? extends List<String>> type)
	{
		String baseTestName = "addAll(int, Collection)";
		setUpTest(constructList(baseTestName, type, null), baseTestName);

		assertNotNull(testList, testName + " unexpected null list instance");
		assertEquals(0,
		             testList.size(),
		             testName + " initial empty list doesn't have 0 size");

		assertThrows(NullPointerException.class,
		             () -> {
		            	 testList.addAll(0, null);
		             },
		             testName + " unexpected addAll(0, null) didn't throw "
		             	+ "NullPointerException");

		testList = constructList(testName, type, listElements);
		assertThrows(IndexOutOfBoundsException.class,
		             () -> {
		            	 testList.addAll(-1, listElements);
		             },
		             testName + " unexpected addAll(invalid index, ...) didn't "
		             	+ "throw IndexOutOfBoundsException");
		assertThrows(IndexOutOfBoundsException.class,
		             () -> {
		            	 testList.addAll(listElements.size() + 1, listElements);
		             },
		             testName + " unexpected addAll(invalid index, ...) didn't "
		             	+ "throw IndexOutOfBoundsException");

		try
		{
			testList = constructList(testName, type, null);
			testList.addAll(0, listElements);
			assertEquals(listElements.size(),
			             testList.size(),
			             testName + " unexpected size after addAll(0, listElements) on empty list");
			for (int i = 0; i < listElements.size(); i++)
			{
				assertEquals(listElements.get(i),
				             testList.get(i),
				             testName + " unexpected element value");
			}
		}
		catch (Exception e)
		{
			fail(testName + " unexpected exception during addAll(0, listElements)");
		}

		List<String> addedList = Arrays.asList(elements3);
		for (int i = 0; i <= elements.length; i++)
		{
			testList = constructList(testName, type, listElements);
			int expectedSize = testList.size() + addedList.size();
			testList.addAll(i, addedList);
			assertEquals(expectedSize,
			             testList.size(),
			             testName + " unexpected size after addAll(" + i + ",...)");
			for (int j = 0; j < i; j++) // elements of initial listElements
			{
				assertEquals(elements[j],
				             testList.get(j),
				             testName + " unexpected value at index " + j);
			}
			int k = 0;
			for (int j = i; j < (i + addedList.size()); j++, k++)
			{
				assertEquals(elements3[k],
				             testList.get(j),
				             testName + " unexpected value at index " + j);

			}
			k = i;
			for (int j = i + addedList.size(); j < testList.size(); j++, k++)
			{
				assertEquals(elements[k],
				             testList.get(j),
				             testName + " unexpected value at index " + j);
			}
		}
	}

	/**
	 * Test method for {@link List#listIterator()}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("listIterator()")
	@Order(20)
	final void testListIterator(Class<? extends List<String>> type)
	{
		String baseTestName = "listIterator()";
		setUpTest(constructList(baseTestName, type, null), baseTestName);

		assertNotNull(testList, testName + " unexpected null list instance");
		assertEquals(0,
		             testList.size(),
		             testName + " initial empty list doesn't have 0 size");

		ListIterator<String> it = testList.listIterator();
		assertNotNull(it, testName + " unexpected null listIterator");
		assertFalse(it.hasNext(), testName + " unexpected iterator state");
		assertFalse(it.hasPrevious(), testName + " unexpected iterator state");

		testList = constructList(testName, type, listElements);
		it = testList.listIterator();
		assertNotNull(it, testName + " unexpected null iterator");
		assertTrue(it.hasNext(), testName + " unexpected iterator state");
		assertFalse(it.hasPrevious(), testName + " unexpected iterator state");

		int i = 0;
		while (i < elements.length)
		{
			try
			{
				assertEquals(elements[i],
				             it.next(),
				             testName + " unexpected value from iterator at index "
				                 + i);
			}
			catch (NoSuchElementException e)
			{
				fail(testName + " unexpected NoSuchElementException at index " + i);
			}
			i++;
			assertEquals(i, it.nextIndex(), testName + " unexpected nextIndex");
			assertEquals(i < elements.length,
			             it.hasNext(),
			             testName + " unexpected hasNext state at index " + i);
			assertEquals(i > 0,
			             it.hasPrevious(),
			             testName + " unexpected hasPrevious state at index " + i);
		}
		assertFalse(it.hasNext(), testName + " unexpected hasNext");
		assertEquals(testList.size(),
		             it.nextIndex(),
		             testName + " unexpected nextIndex");
		assertThrows(NoSuchElementException.class,
		             () -> {
		            	 ListIterator<String> ite = testList.listIterator();
		            	 while (ite.hasNext())
		            	 {
		            		 ite.next();
		            	 }
		            	 // last call to next should trigger exception
		            	 ite.next();
		             },
		             testName + " unexpected next result on terminated iterator");
		i--;
		while (i >= 0)
		{
			try
			{
				assertEquals(elements[i],
				             it.previous(),
				             testName + " unexpected value from iterator at index "
				                 + i);
			}
			catch (NoSuchElementException e)
			{
				fail(testName + " unexpected NoSuchElementException at index " + i);
			}
			assertEquals(i < elements.length,
			             it.hasNext(),
			             testName + " unexpected hasNext state at index " + i);
			assertEquals(i > 0,
			             it.hasPrevious(),
			             testName + " unexpected hasPrevious state at index " + i);
			i--;
			assertEquals(i,
			             it.previousIndex(),
			             testName + " unexpected previous index");
		}
		assertFalse(it.hasPrevious(), testName + " unexpected hasPrevious");
		assertEquals(-1,
		             it.previousIndex(),
		             testName + " unexpected previous index");

		assertThrows(NoSuchElementException.class,
		             () -> {
		            	 ListIterator<String> ite = testList.listIterator();
		            	 // last call to previous should trigger exception
		            	 ite.previous();
		             },
		             testName + " unexpected next result on terminated iterator");

		/*
		 * forward remove
		 */
		it = testList.listIterator();
		i = 0;
		int expectedSize = testList.size();
		while (it.hasNext())
		{
			try
			{
				assertEquals(elements[i],
				             it.next(),
				             testName + " unexpected iterator value at index " + i);
			}
			catch (NoSuchElementException e1)
			{
				fail(testName + " unexpected NoSuchElementException after next() call");
			}
			try
			{
				it.remove();
				assertEquals(--expectedSize,
				             testList.size(),
				             testName + " unexpected list size after remove at step " + i);
			}
			catch (IllegalStateException e)
			{
				fail(testName + " unexpected IllegalStatException during remove of index " + i);
			}

			try
			{
				it.remove();
				fail(testName + " unexpected second remove success");
			}
			catch (IllegalStateException e)
			{
				// Nothing : Expected
			}
			i++;
		}

		/*
		 * backward remove
		 */
		testList = constructList(testName, type, listElements);
		it = testList.listIterator();
		while (it.hasNext())
		{
			it.next();
		}
		i = listElements.size() - 1;
		expectedSize = testList.size();
		while (it.hasPrevious())
		{
			try
			{
				assertEquals(elements[i],
				             it.previous(),
				             testName + " unexpected iterator value at index " + i);
			}
			catch (NoSuchElementException e1)
			{
				fail(testName + " unexpected NoSuchElementException after previous() call at index " + i);
			}
			try
			{
				it.remove();
				assertEquals(--expectedSize,
				             testList.size(),
				             testName + " unexpected list size after remove at step " + i);
			}
			catch (IllegalStateException e)
			{
				fail(testName + " unexpected IllegalStatException during remove of index " + i);
			}

			try
			{
				it.remove();
				fail(testName + " unexpected second remove success");
			}
			catch (IllegalStateException e)
			{
				// Nothing : Expected
			}
			i--;
		}
	}

	/**
	 * Test method for {@link List#listIterator(int)}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("listIterator(int)")
	@Order(21)
	final void testListIteratorInt(Class<? extends List<String>> type)
	{
		String baseTestName = "listIterator(int)";
		setUpTest(constructList(baseTestName, type, null), baseTestName);
		int nbElements = listElements.size();

		assertNotNull(testList, testName + " unexpected null list instance");
		assertEquals(0,
		             testList.size(),
		             testName + " initial empty list doesn't have 0 size");

		ListIterator<String> it = null;
		try
		{
			it = testList.listIterator(0);
		}
		catch (IndexOutOfBoundsException e2)
		{
			fail(testName + " unexpected IndexOutOfBoundsException on listIterator(0) on empty list");
		}
		assertNotNull(it, testName + " unexpected null ListIterator");
		assertFalse(it.hasNext(),
		            testName + " unexpected hasNext() on empty iterator");
		assertFalse(it.hasPrevious(),
		            testName + " unexpected hasPrevious() on empty iterator");

		for (int o = 0; o < nbElements; o++)
		{
			testList = constructList(testName, type, listElements);
			assertThrows(IndexOutOfBoundsException.class,
			             () -> {
			            	 testList.listIterator(-1);
			             },
			             testName + " creation at invalid index (-1) didn't "
			             	+ "throw IndexOutOfBoundsException");
			assertThrows(IndexOutOfBoundsException.class,
			             () -> {
			            	 testList.listIterator(nbElements + 1);
			             },
			             testName + " creation at invalid index didn't "
				             	+ "throw IndexOutOfBoundsException");

			it = null;
			try
			{
				it = testList.listIterator(o);
			}
			catch (IndexOutOfBoundsException e)
			{
				fail(testName
					    + " unexpected IndexOutOfBounds exception on ListIterator("
					    + o + ") over [0" + "..." + nbElements + ")");
			}
			assertNotNull(it, testName + " unexpected null listIterator");
			assertEquals((o <= nbElements),
			             it.hasNext(),
			             testName + " unexpected invalid hashNext");
			assertEquals((o > 0),
			             it.hasPrevious(),
			             " unexpected invalid hasPrevious");

			int i = o;
			while (i < nbElements)
			{
				try
				{
					assertEquals(elements[i],
					             it.next(),
					             testName + " unexpected value from iterator at index "
					                 + i + " over [" + o + "..." + nbElements + ")");
				}
				catch (NoSuchElementException e)
				{
					fail(testName
					    + " unexpected NoSuchElementException at index " + i
					    + " over [" + o + "..." + nbElements + ")");
				}
				i++;
				assertEquals(i < nbElements,
				             it.hasNext(),
				             testName + " unexpected hasNext state at index "
				                 + i + " over [" + o + "..." + nbElements
				                 + ")");
				assertEquals(i > 0,
				             it.hasPrevious(),
				             testName
				                 + " unexpected hasPrevious state at index " + i
				                 + " over [" + o + "..." + nbElements + ")");
			}
			assertFalse(it.hasNext(),
			            testName + " unexpected hasNext value at index " + i
			                + " over [" + o + "..." + nbElements + ")");
			try
			{
				it.next();
				fail(testName
				    + " unexpected last next() didn't throw NoSuchElementException");
			}
			catch (NoSuchElementException e)
			{
				// Nothing : expected
			}

			i--;
			while (i >= 0)
			{
				try
				{
					assertEquals(elements[i],
					             it.previous(),
					             testName + " unexpected value from iterator at index "
					                 + i + " over [" + o + "..." + nbElements + ")");
				}
				catch (NoSuchElementException e)
				{
					fail(testName
					    + " unexpected NoSuchElementException at index " + i
					    + " over [" + o + "..." + nbElements + ")");
				}
				assertEquals(i < elements.length,
				             it.hasNext(),
				             testName + " unexpected hasNext state at index "
				                 + i + " over [" + o + "..." + nbElements
				                 + ")");
				assertEquals(i > 0,
				             it.hasPrevious(),
				             testName
				                 + " unexpected hasPrevious state at index " + i
				                 + " over [" + o + "..." + nbElements + ")");
				i--;
			}
			assertEquals(i > 0,
			             it.hasPrevious(),
			             testName + " unexpected hasPrevious value at index " + i
			                + " over [" + o + "..." + nbElements + ")");
			try
			{
				it.previous();
				fail(testName + " unexpected last previous() didn't throw "
					+ "NoSuchElementException");
			}
			catch (Exception e)
			{
				// Nothing: expected
			}

			/*
			 * forward remove
			 */
			it = testList.listIterator(o);
			i = o;
			int expectedSize = testList.size();
			while (it.hasNext())
			{
				try
				{
					assertEquals(elements[i],
					             it.next(),
					             testName + " unexpected iterator value at index "
					            	 + i);
				}
				catch (NoSuchElementException e1)
				{
					fail(testName + " unexpected NoSuchElementException after "
						+ "next() call");
				}
				try
				{
					it.remove();
					assertEquals(--expectedSize,
					             testList.size(),
					             testName + " unexpected list size after remove "
					             	+ "at step " + i);
				}
				catch (IllegalStateException e)
				{
					fail(testName + " unexpected IllegalStatException during "
						+ "remove of index " + i);
				}

				try
				{
					it.remove();
					fail(testName + " unexpected second remove success");
				}
				catch (IllegalStateException e)
				{
					// Nothing : Expected
				}
				i++;
			}

			/*
			 * backward remove
			 */
			testList = constructList(testName, type, listElements);
			it = testList.listIterator(o);
			while (it.hasNext())
			{
				it.next();
			}
			i = listElements.size() - 1;
			expectedSize = testList.size();
			while (it.hasPrevious())
			{
				try
				{
					assertEquals(elements[i],
					             it.previous(),
					             testName + " unexpected iterator value at index "
					            	 + i);
				}
				catch (NoSuchElementException e1)
				{
					fail(testName + " unexpected NoSuchElementException after "
						+ "previous() call at index " + i);
				}
				try
				{
					it.remove();
					assertEquals(--expectedSize,
					             testList.size(),
					             testName + " unexpected list size after remove "
					             	+ "at step " + i);
				}
				catch (IllegalStateException e)
				{
					fail(testName + " unexpected IllegalStatException during "
						+ "remove of index " + i);
				}

				try
				{
					it.remove();
					fail(testName + " unexpected second remove success");
				}
				catch (IllegalStateException e)
				{
					// Nothing : Expected
				}
				i--;
			}
		}
	}

	/**
	 * Test method for {@link List#subList(int, int)}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("subList(int, int)")
	@Order(22)
	final void testSubList(Class<? extends List<String>> type)
	{
		String baseTestName = "subList(int, int)";
		setUpTest(constructList(baseTestName, type, listElements), baseTestName);

		int nbElements = listElements.size();
		assertNotNull(testList, testName + " unexpected null list instance");
		assertEquals(nbElements,
		             testList.size(),
		             testName + " initial empty list doesn't have 0 size");

		for (int i = -1; i < nbElements; i++)
		{
			for (int j = nbElements + 1; j > 0; j--)
			{
				List<String> subList = null;
				try
				{
					subList = testList.subList(i, j);
				}
				catch (IndexOutOfBoundsException ioobe)
				{
					if ((i > 0) && (j < nbElements))
					{
						fail(testName
						    + " unexpected IndexOutOfBoundsException with indices ["
						    + i + "," + j + "]");
					}
					continue;
				}
				catch(IllegalArgumentException iae)
				{
					if (i <= j)
					{
						fail(testName
							    + " unexpected IllegalArgumentException with indices ["
							    + i + "," + j + "]");
					}
					continue;
				}

				assertNotNull(subList,
				              testName + " unexpected null sub list[" + i + ","
				                  + j + "]");
				assertEquals(j - i,
				             subList.size(),
				             testName + " unexpected sub list size");
				for (int k = i; k < j; k++)
				{
					assertEquals(elements[k],
					             subList.get(k - i),
					             testName + " unexpected subList value at index "
					            	 + (k - i));
				}
			}
		}
	}

	/**
	 * Test method for {@link List#toString()}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("toString()")
	@Order(23)
	final void testToString(Class<? extends List<String>> type)
	{
		String baseTestName = "toString()";
		setUpTest(constructList(baseTestName, type, null), baseTestName);

		assertNotNull(testList, testName + " unexpected null list instance");
		assertEquals(0,
		             testList.size(),
		             testName + " initial empty list doesn't have 0 size");
		assertEquals("[]",
		             testList.toString(),
		             testName + " unexpected toString value");

		testList = constructList(testName, type, listElements);
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (int i = 0; i < elements.length; i++)
		{
			sb.append(elements[i]);
			if (i < (elements.length - 1))
			{
				sb.append(", ");
			}
		}
		sb.append(']');
		String expected = sb.toString();
		assertEquals(expected,
		             testList.toString(),
		             testName + " unexpected toString value");
	}

	/**
	 * Test method for {@link List#isEmpty()}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("isEmpty()")
	@Order(24)
	final void testIsEmpty(Class<? extends List<String>> type)
	{
		String baseTestName = "isEmpty()";
		setUpTest(constructList(baseTestName, type, null), baseTestName);

		assertNotNull(testList, testName + " unexpected null list instance");
		assertEquals(0,
		             testList.size(),
		             testName + " initial empty list doesn't have 0 size");

		assertTrue(testList.isEmpty(), testName + " unexpected non empty list");

		testList = constructList(testName, type, listElements);
		assertFalse(testList.isEmpty(), testName + " unexpected empty list");
	}

	/**
	 * Test method for {@link List#contains(Object)}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("contains(Object)")
	@Order(25)
	final void testContains(Class<? extends List<String>> type)
	{
		String baseTestName = "contains(Object)";
		setUpTest(constructList(baseTestName, type, null), baseTestName);

		assertNotNull(testList, testName + " unexpected null list instance");
		assertEquals(0,
		             testList.size(),
		             testName + " initial empty list doesn't have 0 size");

		assertFalse(testList.contains(null),
		            testName + " unexpected null contained in empty list");

		for (int i = 0; i < elements.length; i++)
		{
			assertFalse(testList.contains(elements[i]),
			            testName + " unexpected contained object: "
			                + elements[i]);
		}

		testList = constructList(testName, type, listElements);
		assertFalse(testList.contains(null),
		            testName + " unexpected null contained in non empty list");
		String[] containedElements = shuffleArray(elements);
		for (int i = 0; i < containedElements.length; i++)
		{
			assertTrue(testList.contains(containedElements[i]),
			           testName + " unexpected not contained object: "
			               + containedElements[i]);
		}
		List<String> uncontainedElements = Arrays.asList(elements3);
		for (Iterator<String> it = uncontainedElements.iterator(); it.hasNext();)
		{
			final String elt = it.next();
			if (listElements.contains(elt))
			{
				uncontainedElements.replaceAll((String s) -> {
					if (s.equals(elt))
					{
						s = new String("replaced");
					}
					return s;
				});
			}
		}

		for (int i = 0; i < uncontainedElements.size(); i++)
		{
			String searched = uncontainedElements.get(i);
			assertFalse(testList.contains(searched),
			            testName + " unexpected contained object: "
			                + searched);
		}
	}

	/**
	 * Test method for {@link List#toArray()}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("toArray()")
	@Order(26)
	final void testToArray(Class<? extends List<String>> type)
	{
		String baseTestName = "toArray()";
		setUpTest(constructList(baseTestName, type, null), baseTestName);

		assertNotNull(testList, testName + " unexpected null list instance");
		assertEquals(0,
		             testList.size(),
		             testName + " initial empty list doesn't have 0 size");

		Object[] providedArray = testList.toArray();
		assertNotNull(providedArray, testName + " unexpected null array");
		assertEquals(0,
		             providedArray.length,
		             testName + " unexpected array length");

		testList = constructList(testName, type, listElements);
		providedArray = testList.toArray();
		assertNotNull(providedArray, testName + " unexpected null array");
		assertEquals(testList.size(),
		             providedArray.length,
		             testName + " unexpected array length");
		assertArrayEquals(elements,
		                  providedArray,
		                  testName + " unexpected array inequality");
	}

	/**
	 * Test method for {@link List#toArray(Object[])}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("toArray(T[])")
	@Order(27)
	final void testToArrayTArray(Class<? extends List<String>> type)
	{
		String baseTestName = "toArray(T[])";
		setUpTest(constructList(baseTestName, type, null), baseTestName);

		assertNotNull(testList, testName + " unexpected null list instance");
		assertEquals(0,
		             testList.size(),
		             testName + " initial empty list doesn't have 0 size");

		String[] providedArray = testList.toArray(new String[0]);
		assertNotNull(providedArray, testName + " unexpected null array");
		assertEquals(0,
		             providedArray.length,
		             testName + " unexpected array length");

		testList = constructList(testName, type, listElements);

		providedArray = testList.toArray(new String[0]);
		assertNotNull(providedArray, testName + " unexpected null array");
		assertEquals(testList.size(),
		             providedArray.length,
		             testName + " unexpected array length");
		assertArrayEquals(elements,
		                  providedArray,
		                  testName + " unexpected array inequality");

		providedArray = testList.toArray(new String[elements.length]);
		assertNotNull(providedArray, testName + " unexpected null array");
		assertEquals(testList.size(),
		             providedArray.length,
		             testName + " unexpected array length");
		assertArrayEquals(elements,
		                  providedArray,
		                  testName + " unexpected array inequality");
	}

	/**
	 * Test method for {@link List#remove(Object)}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("remove(Object)")
	@Order(28)
	final void testRemoveObject(Class<? extends List<String>> type)
	{
		String baseTestName = "remove(Object)";
		setUpTest(constructList(baseTestName, type, null), baseTestName);

		assertNotNull(testList, testName + " unexpected null list instance");
		assertEquals(0,
		             testList.size(),
		             testName + " initial empty list doesn't have 0 size");
		assertFalse(testList.remove(null),
		            testName + " unexpected null removed in empty list");

		for (int i = 0; i < elements.length; i++)
		{
			assertFalse(testList.remove(elements[i]),
			            testName + " unexpected removing of object "
			                + elements[i] + " on empty list");
		}

		testList = constructList(testName, type, listElements);
		assertFalse(testList.remove(null),
		            testName + " unexpected null removed in non empty list");
		String[] containedElements = shuffleArray(elements);
		int expectedSize = testList.size();
		for (int i = 0; i < containedElements.length; i++)
		{
			assertTrue(testList.remove(containedElements[i]),
			           testName + " unexpected not removed object: "
			               + containedElements[i]);
			assertEquals(--expectedSize,
			             testList.size(),
			             testName + " unexpected list size after remove");
		}
		testList = constructList(testName, type, listElements);
		List<String> uncontainedElements = Arrays.asList(elements3);
		for (Iterator<String> it = uncontainedElements.iterator(); it.hasNext(); )
		{
			final String elt = it.next();
			if (listElements.contains(elt))
			{
				uncontainedElements.replaceAll((String s) -> {
					if (s.equals(elt))
					{
						s = new String("replaced");
					}
					return s;
				});
			}
		}

		for (int i = 0; i < uncontainedElements.size(); i++)
		{
			String searched = uncontainedElements.get(i);
			assertFalse(testList.remove(searched),
			            testName + " unexpected removed object: "
			                + searched);
		}
	}

	/**
	 * Test method for {@link List#containsAll(Collection)}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("containsAll(Collection)")
	@Order(29)
	final void testContainsAll(Class<? extends List<String>> type)
	{
		String baseTestName = "containsAll(Collection)";
		setUpTest(constructList(baseTestName, type, null), baseTestName);

		assertNotNull(testList, testName + " unexpected null list instance");
		assertEquals(0,
		             testList.size(),
		             testName + " initial empty list doesn't have 0 size");
		assertThrows(NullPointerException.class,
		             () -> {
		            	 testList.containsAll(null);
		             },
		             testName +  " missing NullPointerException");
		assertFalse(testList.containsAll(listElements),
		            testName + " unexpected contained collection in empty list");
		testList = constructList(testName, type, listElements);
		assertThrows(NullPointerException.class,
		             () -> {
		            	 testList.containsAll(null);
		             },
		             testName +  " missing NullPointerException");
		String[] shuffledElements = shuffleArray(elements);
		assertTrue(testList.containsAll(Arrays.asList(shuffledElements)),
		           testName + " unexpected not contained collection");
		listElements.add("lacinia");
		assertFalse(testList.containsAll(listElements),
		            testName + " unexpected contained collection");
		listElements.remove("lacinia");
		while (listElements.size() > 0)
		{
			assertTrue(testList.containsAll(listElements),
			           testName + " unexpected not contained collection");
			listElements.remove(0);
		}
		assertFalse(testList.containsAll(Arrays.asList(elements3)),
		            testName + " unexpected contained collection");
	}

	/**
	 * Test method for {@link List#addAll(Collection)}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("addAll(Collection)")
	@Order(30)
	final void
	    testAddAllCollectionOfQextendsE(Class<? extends List<String>> type)
	{
		String baseTestName = "addAll(Collection)";
		setUpTest(constructList(baseTestName, type, null), baseTestName);

		assertNotNull(testList, testName + " unexpected null list instance");
		assertEquals(0,
		             testList.size(),
		             testName + " initial empty list doesn't have 0 size");
		assertThrows(NullPointerException.class,
		             () -> {
		            	 testList.addAll(null);
		             },
		             testName + " unexpected added null collection without throwing NullPointerException");
		int initialSize = testList.size();
		assertTrue(testList.addAll(listElements),
		           testName + " unexpected addAll failure");
		assertNotEquals(initialSize,
		                testList.size(),
		                testName + " unexpected resulting list unchanged size");
		assertTrue(testList.containsAll(listElements),
		           testName + " unexpected collection not contained");

		initialSize = testList.size();
		assertTrue(testList.addAll(listElements),
		           testName + " unexpected addAll failure");
		assertNotEquals(initialSize,
		                testList.size(),
		                testName + " unexpected resulting list unchanged size");

		Map<String, Integer> wordCounts = new HashMap<String, Integer>();
		for (Iterator<String> it = testList.iterator(); it.hasNext();)
		{
			String word = it.next();
			Integer value = wordCounts.get(word);
			wordCounts.put(word, (value == null ? 1 : value + 1));
		}
		for (Iterator<String> it = wordCounts.keySet().iterator(); it.hasNext(); )
		{
			String word = it.next();
			assertEquals(2,
			             wordCounts.get(word),
			             testName + " unexpected count for word \"" + word + "\"");
		}
	}

	/**
	 * Test method for {@link List#removeAll(Collection)}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("removeAll(Collection)")
	@Order(31)
	final void testRemoveAll(Class<? extends List<String>> type)
	{
		String baseTestName = "removeAll(Collection)";
		setUpTest(constructList(baseTestName, type, null), baseTestName);

		assertNotNull(testList, testName + " unexpected null list instance");
		assertEquals(0,
		             testList.size(),
		             testName + " initial empty list doesn't have 0 size");
		assertThrows(NullPointerException.class,
		             () -> {
		            	 testList.removeAll(null);
		             },
		             testName + " unexpected removeAll of null collection");

		testList = constructList(testName, type, listElements);
		assertThrows(NullPointerException.class,
		             () -> {
		            	 testList.removeAll(null);
		             },
		             testName + " unexpected removeAll of null collection");
		int expectedSize = testList.size() - elements2.length;
		assertTrue(testList.removeAll(Arrays.asList(elements2)),
		           testName + " unexpected removeAll failure of elements2");
		assertEquals(expectedSize,
		             testList.size(),
		             testName + " unexpected size after removeAll");
		for (int i = 0; i < elements2.length; i++)
		{
			assertFalse(testList.contains(elements2[i]),
			            testName + " unexpected contained element "
			                + elements2[i]);
		}
		for (int i = 0; i < elements1.length; i++)
		{
			assertTrue(testList.contains(elements1[i]),
			            testName + " unexpected not contained element "
			                + elements1[i]);
		}
		assertFalse(testList.removeAll(Arrays.asList(elements3)),
		            testName
		                + " unexpected removeAll result of uncontained collection");

		listElements.add("lacinia");
		assertTrue(testList.removeAll(listElements),
		            testName + " unexpected non removal of collection");
		for (Iterator<String> it = listElements.iterator(); it.hasNext();)
		{
			String word = it.next();
			assertFalse(testList.contains(word),
			            testName + " unexpected contained element " + word);
		}
		assertEquals(0,
		             testList.size(),
		             testName + " unexpected resulting list size");
	}

	/**
	 * Test method for {@link List#retainAll(Collection)}.
	 * @param type the type of list to test provided by
	 * {@link #listClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("listClassesProvider")
	@DisplayName("retainAll(Collection)")
	@Order(32)
	final void testRetainAll(Class<? extends List<String>> type)
	{
		String baseTestName = "retainAll(Collection)";
		setUpTest(constructList(baseTestName, type, null), baseTestName);

		assertNotNull(testList, testName + " unexpected null list instance");
		assertEquals(0,
		             testList.size(),
		             testName + " initial empty list doesn't have 0 size");

		assertThrows(NullPointerException.class,
		             () -> {
		            	 testList.retainAll(null);
		             },
		             testName + " didn't throw NullPointerException on "
		             	+ "null collection");

		testList = constructList(testName, type, listElements);
		assertTrue(testList.retainAll(Arrays.asList(elements1)),
		           testName + " unexpected retain result");
		for (int i = 0; i < elements1.length; i++)
		{
			assertTrue(testList.contains(elements1[i]),
			            testName + " unexpected not contained object "
			                + elements1[i]);
		}
		for (int i = 0; i < elements2.length; i++)
		{
			assertFalse(testList.contains(elements2[i]),
			            testName + " unexpected contained object "
			                + elements2[i]);
		}

		int expectedSize = testList.size();
		assertFalse(testList.retainAll(Arrays.asList(elements1)),
		            testName + " unexpected retain result");
		assertEquals(expectedSize,
		             testList.size(),
		             testName
		                 + " unexpected list size after retain of already contained list");

		assertTrue(testList.retainAll(Arrays.asList(elements3)),
		           testName + " unexpected retain result");
		assertEquals(0,
		             testList.size(),
		             testName
		                 + " unexpected list size after retain of uncontained list");
	}

	/**
	 * Test method for {@link Headed#getHead()}.
	 * @param type the type of list to test provided by
	 * {@link #headedClassesProvider()}
	 */
	@SuppressWarnings("unchecked") // because of (Headed<String>) testCollection
	@ParameterizedTest
	@MethodSource("headedClassesProvider")
	@DisplayName("getHead()")
	@Order(33)
	final void testGetHead(Class<? extends List<String>> type)
	{
		String baseTestName = "getHead()";
		setUpTest(constructList(baseTestName, type, null), baseTestName);
		if (Headed.class.isInstance(testList))
		{
			Headed<String> headed = (Headed<String>) testList;

			// empty list has null head
			assertNull(headed.getHead(),
			           testName + " unexpected non null head");

			// filled list has non null head
			testList = constructList(baseTestName, type, listElements);
			headed = (Headed<String>) testList;
			assertNotNull(headed.getHead(), testName + " unexpected null head");

			// head contains the same data as iterator's first value
			Iterator<String> it = testList.iterator();
			assertTrue(it.hasNext(), testName + " unexpected finished iterator on filled list");
			assertEquals(it.next(),
			             headed.getHead().getData(),
			             testName + " unexpected data from head");
		}
		else
		{
			fail(testName + " unexpected type: " + type.getSimpleName());
		}
	}

	/**
	 * Test method for {@link Headed#setHead(collections.nodes.Node)}.
	 * @param type the type of list to test provided by
	 * {@link #headedClassesProvider()}
	 */
	@SuppressWarnings("unchecked") // because of (Headed<String>) testCollection
	@ParameterizedTest
	@MethodSource("headedClassesProvider")
	@DisplayName("setHead(Node)")
	@Order(34)
	final void testSetHead(Class<? extends List<String>> type)
	{
		String baseTestName = "setHead(Node)";
		setUpTest(constructList(baseTestName, type, null), baseTestName);
		if (Headed.class.isInstance(testList))
		{
			Headed<String> headed = (Headed<String>) testList;

			Collection<String> other = constructList(baseTestName, type, listElements);
			Headed<String> otherHeaded = (Headed<String>) other;
			Node<String> otherHead = otherHeaded.getHead();
			assertNotNull(otherHead,
			              testName + " unexpected null head on filled collection");

			headed.setHead(otherHead);
			assertEquals(otherHead,
			             headed.getHead(),
			             testName + " unexpected head after setHead");
		}
		else
		{
			fail(testName + " unexpected type: " + type.getSimpleName());
		}
	}
}
