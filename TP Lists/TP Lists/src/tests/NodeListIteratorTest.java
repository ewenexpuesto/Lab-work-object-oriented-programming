package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;

import collections.nodes.Headed;
import collections.nodes.Node;
import collections.nodes.NodeListIterator;
import tests.utils.HeadHolder;
import tests.utils.WordSupplier;

/**
 * Test class for {@link NodeListIterator}
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("NodeListIterator<E>")
public class NodeListIteratorTest
{
	/**
	 * Head holder to nothing
	 */
	private Headed<String> headNothing;

	/**
	 * head holder of a single node
	 */
	private Headed<String> headSingle;

	/**
	 * Number of elements in nodes chain
	 */
	private final static int chainLength = 10;

	/**
	 * Head holder of a chain of nodes
	 */
	private Headed<String> headMulti;

	/**
	 * Word Supplier to provide random words
	 */
	private static WordSupplier wordSupplier = null;

	/**
	 * Setup before all tests
	 * @throws java.lang.Exception if setup beore class fails
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
		wordSupplier = new WordSupplier("assets/prenoms.txt");
		System.out.println("-------------------------------------------------");
		System.out.println("NodeListIterator tests");
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
		System.out.println("NodeListIterator tests end");
		System.out.println("-------------------------------------------------");
	}

	/**
	 * Setup a head node with length - 1 successors
	 * @param length the total number of nodes to setup
	 */
	private void setupHeadNode(int length)
	{
		headMulti = new HeadHolder<String>(new Node<String>(wordSupplier.get()));
		Node<String> current = headMulti.getHead();
		for (int i = 0; i < (length - 1); i++)
		{
			current.setNext(new Node<String>(wordSupplier.get()));
			current = current.getNext();
		}

		int size = 1;
		current = headMulti.getHead();
		while (current.hasNext())
		{
			size++;
			current = current.getNext();
		}
		assertEquals(chainLength, size, "unexpected nodes chain length");
	}

	/**
	 * Setup before each test
	 * @throws java.lang.Exception if setup before each test fails
	 */
	@BeforeEach
	void setUp() throws Exception
	{
		headNothing = new HeadHolder<>(null);
		headSingle = new HeadHolder<>(new Node<String>(wordSupplier.get()));
		setupHeadNode(chainLength);
	}

	/**
	 * Teardown after each test
	 * @throws java.lang.Exception if tear down after each test fails
	 */
	@AfterEach
	void tearDown() throws Exception
	{
		Node<String> singleNode = headSingle.getHead();
		if (singleNode != null)
		{
			singleNode.remove();
		}
		Node<String> current = headMulti.getHead();
		while ((current != null) && (current.getNext() != null)
		    && (current.getPrevious() != null))
		{
			Node<String> next = current.getNext();
			current.remove();
			current = next;
		}
		wordSupplier.reset();
	}

	/**
	 * Test method for
	 * @param testInfo informations about the current test
	 * {@link collections.nodes.NodeListIterator#NodeListIterator(collections.nodes.Headed, int)}.
	 */
	@Test
	@DisplayName("NodeListIterator(Headed<E>, int)")
	@Order(1)
	final void testNodeIteratorNodeInt(TestInfo testInfo)
	{
		Optional<Class<?>> testClass = testInfo.getTestClass();
		if(testClass.isPresent())
		{
			System.out.println("test class = " + testClass.get().getSimpleName());
		}
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		assertThrows(NullPointerException.class, () -> {
			ListIterator<String> itn = new NodeListIterator<>(null, 0);
			itn.next();
		});

		/*
		 * NodeListIterator on no nodes
		 */
		assertThrows(IndexOutOfBoundsException.class, () ->{
			ListIterator<String> it = new NodeListIterator<>(headNothing, -1);
			it.next();
		});
		assertThrows(IndexOutOfBoundsException.class, () ->{
			ListIterator<String> it = new NodeListIterator<>(headNothing, 1);
			it.next();
		});

		ListIterator<String> it = new NodeListIterator<>(headNothing, 0);
		assertNotNull(it, testName + " unexpected null iterator");
		assertFalse(it.hasNext(), testName + " unexpected next elt to iterate");
		assertFalse(it.hasPrevious(),
		            testName + " unexpected previous elt to iterate");

		/*
		 * NodeListIterator on single node
		 */
		assertThrows(IndexOutOfBoundsException.class, () ->{
			ListIterator<String> its = new NodeListIterator<>(headSingle, -1);
			its.next();
		});
		assertThrows(IndexOutOfBoundsException.class, () ->{
			ListIterator<String> its = new NodeListIterator<>(headSingle, 2);
			its.next();
		});

		it = new NodeListIterator<>(headSingle, 0);
		Node<String> singleNode = headSingle.getHead();
		assertNotNull(it, testName + " unexpected null iterator");
		assertTrue(it.hasNext(),
		           testName + " unexpected no next elt to iterate");
		String nodeContent = it.next();
		assertEquals(singleNode.getData(),
		             nodeContent,
		             testName + " unexpected next content");
		assertTrue(it.hasPrevious(),
		           testName + " unexpected no previous elt to iterate");
		nodeContent = it.previous();
		assertEquals(singleNode.getData(),
		             nodeContent,
		             testName + " unexpected previous content");
		assertFalse(it.hasPrevious(),
		            testName + " unexpected previous elt to iterate");

		it = new NodeListIterator<>(headSingle, 1);
		assertNotNull(it, testName + " unexpected null iterator");
		assertFalse(it.hasNext(), testName + " unexpected next element");
		assertTrue(it.hasPrevious(),
		           testName + " unexpected no previous element");
		try
		{
			it.next();
		}
		catch (NoSuchElementException e)
		{
			// Nothing: expected
		}
		assertEquals(singleNode.getData(),
		             it.previous(),
		             testName + " unexpected previous value");

		/*
		 * NodeListIterator on chain node
		 */
		assertThrows(IndexOutOfBoundsException.class, () ->{
			ListIterator<String> itm = new NodeListIterator<>(headMulti, -1);
			itm.next();
		});
		assertThrows(IndexOutOfBoundsException.class, () ->{
			ListIterator<String> itm =
			    new NodeListIterator<>(headMulti, chainLength + 1);
			itm.next();
		});

		int nbTests = chainLength + 2;
		for (int i = 0; i < nbTests; i++)
		{
			setupHeadNode(chainLength);
			try
			{
				it = new NodeListIterator<>(headMulti, i);
			}
			catch (IndexOutOfBoundsException e)
			{
				if (i > chainLength)
				{
					// Expected: skip to next i
					continue;
				}
				else
				{
					fail(testName + " unexpected: " + e.getLocalizedMessage());
				}
			}
			assertNotNull(it, testName + " unexpected null iterator");
			int count = 0;
			// Forward iteration
			while (count < (chainLength - i))
			{
				assertTrue(it.hasNext(), testName + " unexpected no next");
				try
				{
					it.next();
				}
				catch (NoSuchElementException e)
				{
					fail(testName + " unexpected next fail:"
					    + e.getLocalizedMessage());
				}
				count++;
			}
			// Backward iteration
			count = chainLength;
			while (count > 0)
			{
				assertTrue(it.hasPrevious(),
				           testName + " unexpected no previous");
				try
				{
					it.previous();
				}
				catch (NoSuchElementException e)
				{
					fail(testName + " unexpected previous fail: "
					    + e.getLocalizedMessage());
				}
				count--;
			}
		}
		it = new NodeListIterator<>(headMulti, chainLength);
		assertNotNull(it, testName + " unexpected null iterator");
		assertFalse(it.hasNext(), testName + " unexpected next presence");
		assertTrue(it.hasPrevious(),
		           testName + " unexpected no previous element");
	}

	/**
	 * Test method for
	 * @param testInfo informations about the current test
	 * {@link collections.nodes.NodeListIterator#NodeListIterator(Headed)}.
	 */
	@Test
	@DisplayName("NodeListIterator(Headed<E>)")
	@Order(2)
	final void testNodeIteratorNode(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		/*
		 * NodeListIterator on no node
		 */
		ListIterator<String> it = new NodeListIterator<>(headNothing);
		assertNotNull(it, testName + " unexpected null iterator");
		assertFalse(it.hasNext(), testName + " unexpected next elt to iterate");
		assertFalse(it.hasPrevious(),
		            testName + " unexpected previous elt to iterate");

		/*
		 * NodeListIterator on single node
		 */
		it = new NodeListIterator<>(headSingle);
		Node<String> headSingleNode = headSingle.getHead();
		assertNotNull(it, testName + " unexpected null iterator");
		assertTrue(it.hasNext(),
		           testName + " unexpected no next elt to iterate");
		String nodeContent = it.next();
		assertEquals(headSingleNode.getData(),
		             nodeContent,
		             testName + " unexpected next content");
		assertTrue(it.hasPrevious(),
		           testName + " unexpected no previous elt to iterate");
		nodeContent = it.previous();
		assertEquals(headSingleNode.getData(),
		             nodeContent,
		             testName + " unexpected previous content");
		assertFalse(it.hasPrevious(),
		            testName + " unexpected previous elt to iterate");

		/*
		 * NodeListIterator on chain node
		 */
		it = new NodeListIterator<>(headMulti);
		assertNotNull(it, testName + " unexpected null iterator");
		int count = 0;
		// Forward iteration
		while (count < chainLength)
		{
			assertTrue(it.hasNext(), testName + " unexpected no next");
			try
			{
				it.next();
			}
			catch (NoSuchElementException e)
			{
				fail(testName + " unexpected next fail:"
				    + e.getLocalizedMessage());
			}
			count++;
		}
		// Backward iteration
		while (count > 0)
		{
			assertTrue(it.hasPrevious(), testName + " unexpected no previous");
			try
			{
				it.previous();
			}
			catch (NoSuchElementException e)
			{
				fail(testName + " unexpected previous fail: "
				    + e.getLocalizedMessage());
			}
			count--;
		}
	}

	/**
	 * Test method for {@link collections.nodes.NodeListIterator#hasNext()}.
	 * @param testInfo informations about the current test
	 */
	@Test
	@DisplayName("hasNext()")
	@Order(3)
	final void testHasNext(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);


		/*
		 * NodeListIterator on no node
		 */
		ListIterator<String> it = new NodeListIterator<>(headNothing);
		assertFalse(it.hasNext(),
		            testName
		                + " unexpected next elt to iterate on empty iterator");

		/*
		 * NodeListIterator on single node
		 */
		it = new NodeListIterator<>(headSingle);
		assertTrue(it.hasNext(),
		           testName + " unexpected no next elt to iterate");

		/*
		 * NodeListIterator on chain node
		 */
		it = new NodeListIterator<>(headMulti);
		int count = 0;
		// Forward iteration
		while (count < chainLength)
		{
			assertTrue(it.hasNext(), testName + " unexpected no next");
			try
			{
				it.next();
			}
			catch (NoSuchElementException e)
			{
				fail(testName + " unexpected next fail: "
				    + e.getLocalizedMessage());
			}
			count++;
		}
	}

	/**
	 * Test method for {@link collections.nodes.NodeListIterator#next()}.
	 * @param testInfo informations about the current test
	 */
	@Test
	@DisplayName("next()")
	@Order(4)
	final void testNext(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		/*
		 * NodeListIterator on no node
		 */
		assertThrows(NoSuchElementException.class, () -> {
			ListIterator<String> it = new NodeListIterator<>(headNothing);
			it.next();
		}, testName + " next() unexpectedly didn't throw on empty iterator");

		/*
		 * NodeListIterator on single node
		 */
		ListIterator<String> it = new NodeListIterator<>(headSingle);
		Node<String> headSingleNode = headSingle.getHead();
		String nodeContent = null;
		try
		{
			nodeContent = it.next();
			assertEquals(headSingleNode.getData(),
			             nodeContent,
			             testName + " unexpected next node content");
		}
		catch (NoSuchElementException e)
		{
			fail(testName + " unexpected next() fail: "
			    + e.getLocalizedMessage());
		}
		try
		{
			nodeContent = it.next();
			fail(testName + " unexpected next() call success");
		}
		catch (NoSuchElementException e)
		{
			// Nothing : expected
		}

		/*
		 * NodeListIterator on chain node
		 */
		it = new NodeListIterator<>(headMulti);
		assertNotNull(it, testName + " unexpected null iterator");
		int count = 0;
		Node<String> current = headMulti.getHead();
		// Forward iteration
		while (count < chainLength)
		{
			assertTrue(it.hasNext(), testName + " unexpected no next");
			try
			{
				String iterContent = it.next();
				nodeContent = current.getData();
				assertEquals(nodeContent,
				             iterContent,
				             testName + " unexpected next content");
			}
			catch (NoSuchElementException e)
			{
				fail(testName + " unexpected next fail: "
				    + e.getLocalizedMessage());
			}
			count++;
			current = current.getNext();
		}
	}

	/**
	 * Test method for {@link collections.nodes.NodeListIterator#hasPrevious()}.
	 * @param testInfo informations about the current test
	 */
	@Test
	@DisplayName("hasPrevious()")
	@Order(5)
	final void testHasPrevious(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		/*
		 * NodeListIterator on no node
		 */
		ListIterator<String> it = new NodeListIterator<>(headNothing);
		assertFalse(it.hasPrevious(),
		            testName
		                + " unexpected previous elt to iterate on empty iterator");

		/*
		 * NodeListIterator on single node
		 */
		it = new NodeListIterator<>(headSingle);
		assertFalse(it.hasPrevious(),
		            testName + " unexpected previous elt to iterate");
		it.next();
		assertTrue(it.hasPrevious(),
		           testName + " unexpected no previous elt to iterate");

		/*
		 * NodeListIterator on chain node
		 */
		it = new NodeListIterator<>(headMulti);
		int count = 0;
		// Forward iteration
		while (count < chainLength)
		{
			try
			{
				it.next();
			}
			catch (NoSuchElementException e)
			{
				fail(testName + " unexpected next fail: "
				    + e.getLocalizedMessage());
			}
			if (count > 0)
			{
				assertTrue(it.hasPrevious(),
				           testName + " unexpected no previous");
			}
			count++;
		}

		// Backward iteration
		while (count > 0)
		{
			assertTrue(it.hasPrevious(), testName + " unexpected no previous");
			try
			{
				it.previous();
			}
			catch (NoSuchElementException e)
			{
				fail(testName + " unexpected previous fail: "
				    + e.getLocalizedMessage());
			}
			count--;
		}
		assertFalse(it.hasPrevious(), testName + " unexpected previous");
	}

	/**
	 * Test method for {@link collections.nodes.NodeListIterator#previous()}.
	 * @param testInfo informations about the current test
	 */
	@Test
	@DisplayName("previous()")
	@Order(6)
	final void testPrevious(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		/*
		 * NodeListIterator on no node
		 */
		assertThrows(NoSuchElementException.class, () -> {
			ListIterator<String> it = new NodeListIterator<>(headNothing);
			it.previous();
		},
		             testName
		                 + " previous() unexpectedly didn't throw on empty iterator");

		/*
		 * NodeListIterator on single node
		 */
		assertThrows(NoSuchElementException.class, () -> {
			ListIterator<String> it = new NodeListIterator<>(headSingle);
			it.previous();
		}, testName + " previous() unexpectedly didn't throw");

		ListIterator<String> it = new NodeListIterator<>(headSingle);
		Node<String> headSingleNode = headSingle.getHead();
		it.next();
		try
		{
			String itContent = it.previous();
			assertEquals(headSingleNode.getData(),
			             itContent,
			             testName + " unexpected previous content");
		}
		catch (NoSuchElementException e)
		{
			fail(testName + " previous() unexpectedly failed: "
			    + e.getLocalizedMessage());
		}

		/*
		 * NodeListIterator on chain node
		 */
		it = new NodeListIterator<>(headMulti);
		int count = 0;
		Node<String> current = headMulti.getHead();
		Node<String> previous = null;

		try
		{
			it.previous();
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
				it.next();
			}
			catch (NoSuchElementException e)
			{
				fail(testName + " unexpected next fail: "
				    + e.getLocalizedMessage());
			}
			if (count > 0)
			{
				assertTrue(it.hasPrevious(),
				           testName + " unexpected no previous");
			}
			count++;
			previous = current;
			current = current.getNext();
		}
		current = previous;

		// Backward iteration
		while (count > 0)
		{
			try
			{
				String itContent = it.previous();
				assertEquals(current.getData(),
				             itContent,
				             testName + " unexpected previous content");
			}
			catch (NoSuchElementException e)
			{
				fail(testName + " unexpected previous fail: "
				    + e.getLocalizedMessage());
			}
			count--;
			current = current.getPrevious();
		}
		try
		{
			it.previous();
			fail(testName + " previous() call unexpectedly didn't throw");
		}
		catch (NoSuchElementException e)
		{
			// Nothing: expected
		}
	}

	/**
	 * Test method for {@link collections.nodes.NodeListIterator#nextIndex()}.
	 * @param testInfo informations about the current test
	 */
	@Test
	@DisplayName("nextIndex()")
	@Order(7)
	final void testNextIndex(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		/*
		 * NodeListIterator on no node
		 */
		ListIterator<String> it = new NodeListIterator<>(headNothing);
		assertEquals(0,
		             it.nextIndex(),
		             testName
		                 + " unexpected nextIndex value on empty iterator");

		/*
		 * NodeListIterator on single node
		 */
		it = new NodeListIterator<>(headSingle);
		assertEquals(0,
		             it.nextIndex(),
		             testName + " unexpected nextIndex value");

		/*
		 * NodeListIterator on chain node
		 */
		it = new NodeListIterator<>(headMulti);
		assertNotNull(it, testName + " unexpected null iterator");
		int count = 0;
		int expectedNextIndex = 0;
		// Forward iteration
		while (count < chainLength)
		{
			assertEquals(expectedNextIndex,
			             it.nextIndex(),
			             testName + " unexpected nextIndex value");
			it.next();
			expectedNextIndex++;
			count++;
		}
		// Backward iteration
		int nextIndex;
		while (count > 0)
		{
			nextIndex = it.nextIndex();
			assertEquals(expectedNextIndex,
			             nextIndex,
			             testName + " unexpected nextIndex value");
			it.previous();
			expectedNextIndex--;
			count--;
		}
		/**
		 * next index after remove
		 */
		it = new NodeListIterator<>(headMulti);
		count = 0;
		while (count < chainLength)
		{
			it.next();
			/*
			 * removes the last element returned by next: the next index should
			 * remain 0.
			 */
			it.remove();
			assertEquals(0,
			             it.nextIndex(),
			             testName
			                 + " unexpected next index value after remove");
			count++;
		}

		setupHeadNode(chainLength);
		it = new NodeListIterator<>(headMulti);
		count = 0;
		while (count < chainLength)
		{
			it.next();
			count++;
		}
		while (count > 0)
		{
			it.previous();
			/*
			 * removes the last element returned by previous: the next index
			 * should decrease.
			 */
			it.remove();
			assertEquals(count - 1,
			             it.nextIndex(),
			             testName
			                 + " unexpected next index value after remove");
			count--;
		}

		/**
		 * next index after add
		 */
		setupHeadNode(chainLength);
		it = new NodeListIterator<>(headMulti);
		count = 0;
		expectedNextIndex = 0;
		assertEquals(0,
		             it.nextIndex(),
		             testName + " unexpected next index value");
		while (count < chainLength)
		{
			it.next(); // increments next Index
			it.add(wordSupplier.get()); // increments next Index
			expectedNextIndex += 2;
			assertEquals(expectedNextIndex,
			             it.nextIndex(),
			             testName + " unexpected next index value after add");
			count++;
		}
		expectedNextIndex = it.nextIndex();
		assertEquals(chainLength * 2,
		             expectedNextIndex,
		             testName + " unexpected next index value after add loop");
		count = 0;
		while (count < chainLength)
		{
			it.previous();
			it.add(wordSupplier.get());
			assertEquals(expectedNextIndex,
			             it.nextIndex(),
			             testName + " unexpected next index value after add");
			count++;
		}
	}

	/**
	 * Test method for {@link collections.nodes.NodeListIterator#previousIndex()}.
	 * @param testInfo informations about the current test
	 */
	@Test
	@DisplayName("previousIndex()")
	@Order(8)
	final void testPreviousIndex(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		/*
		 * NodeListIterator on no node
		 */
		ListIterator<String> it = new NodeListIterator<>(headNothing);
		assertEquals(-1,
		             it.previousIndex(),
		             testName
		                 + " unexpected previousIndex value on empty iterator");

		/*
		 * NodeListIterator on single node
		 */
		it = new NodeListIterator<>(headSingle);
		assertEquals(-1,
		             it.previousIndex(),
		             testName + " unexpected previousIndex value");

		/*
		 * NodeListIterator on chain node
		 */
		it = new NodeListIterator<>(headMulti);
		assertNotNull(it, testName + " unexpected null iterator");
		int count = 0;
		int expectedPreviousIndex = -1;
		// Forward iteration
		while (count < chainLength)
		{
			assertEquals(expectedPreviousIndex,
			             it.previousIndex(),
			             testName + " unexpected previousIndex value");
			it.next();
			expectedPreviousIndex++;
			count++;
		}
		// Backward iteration
		while (count > 0)
		{
			assertEquals(expectedPreviousIndex,
			             it.previousIndex(),
			             testName + " unexpected previousIndex value");
			it.previous();
			expectedPreviousIndex--;
			count--;
		}

		assertEquals(-1,
		             it.previousIndex(),
		             testName + " unexpected previous index value");

		/**
		 * next index after remove
		 */
		it = new NodeListIterator<>(headMulti);
		count = 0;
		while (count < chainLength)
		{
			it.next();
			/*
			 * removes the last element returned by next: the previous index
			 * should remain -1.
			 */
			it.remove();
			assertEquals(-1,
			             it.previousIndex(),
			             testName
			                 + " unexpected previous index value after remove");
			count++;
		}

		setupHeadNode(chainLength);
		it = new NodeListIterator<>(headMulti);
		count = 0;
		while (count < chainLength)
		{
			it.next();
			count++;
		}
		while (count > 0)
		{
			it.previous();
			/*
			 * removes the last element returned by previous: the previous index
			 * should decrease.
			 */
			it.remove();
			assertEquals(count - 2,
			             it.previousIndex(),
			             testName
			                 + " unexpected next index value after remove");
			count--;
		}

		/**
		 * next index after add
		 */
		setupHeadNode(chainLength);
		it = new NodeListIterator<>(headMulti);
		count = 0;
		expectedPreviousIndex = -1;
		assertEquals(expectedPreviousIndex,
		             it.previousIndex(),
		             testName + " unexpected next index value");
		while (count < chainLength)
		{
			it.next(); // increments next Index
			it.add(wordSupplier.get()); // increments next Index
			expectedPreviousIndex += 2;
			assertEquals(expectedPreviousIndex,
			             it.previousIndex(),
			             testName
			                 + " unexpected previous index value after add");
			count++;
		}
		expectedPreviousIndex = it.previousIndex();
		assertEquals((chainLength * 2) - 1,
		             expectedPreviousIndex,
		             testName
		                 + " unexpected previous index value after add loop");
		count = 0;
		while (count < chainLength)
		{
			it.previous();
			it.add(wordSupplier.get());
			assertEquals(expectedPreviousIndex,
			             it.previousIndex(),
			             testName
			                 + " unexpected previous index value after add");
			count++;
		}
	}

	/**
	 * Test method for {@link collections.nodes.NodeListIterator#remove()}.
	 * @param testInfo informations about the current test
	 */
	@Test
	@DisplayName("remove()")
	@Order(9)
	final void testRemove(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		/*
		 * NodeListIterator on no node
		 */
		assertThrows(IllegalStateException.class, () -> {
			ListIterator<String> it = new NodeListIterator<>(headNothing);
			it.remove();
		}, testName + " remove() didn't throw exception on empty iterator");

		/*
		 * NodeListIterator on single node
		 */
		assertThrows(IllegalStateException.class, () -> {
			ListIterator<String> it = new NodeListIterator<>(headSingle);
			it.remove();
		}, testName + " remove() didn't throw exception");
		ListIterator<String> it = new NodeListIterator<>(headSingle);
		try
		{
			it.next();
			it.remove();
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
			it.next();
			fail(testName + " unexpected next() success");
		}
		catch (NoSuchElementException e)
		{
			// Nothing : expected
		}
		assertEquals(-1,
		             it.previousIndex(),
		             testName + " unexpected previous index value");
		assertEquals(0,
		             it.nextIndex(),
		             testName + " unexpected next index value");
		try
		{
			it.remove();
			fail(testName + " remove() 2nd call succeeded");
		}
		catch (IllegalStateException e)
		{
			// Nothing: expected
		}

		/*
		 * NodeListIterator on chain node
		 */
		it = new NodeListIterator<>(headMulti);
		int count = 0;
		// Forward iteration
		try
		{
			while (count < chainLength)
			{
				it.next();
				it.remove();
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
			it.remove();
			fail(testName + " remove() call unexpectedly succeeded");
		}
		catch (IllegalStateException e)
		{
			// Nothing: expected
		}

		setupHeadNode(chainLength);
		it = new NodeListIterator<>(headMulti);
		// Forward iteration
		count = 0;
		while (count < chainLength)
		{
			it.next();
			count++;
		}
		int size = count;
		// Backward iteration
		while (count >= 0)
		{
			try
			{
				it.previous();
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
				 * Remove can only be performed when there is a last element to
				 * remove
				 */
				it.remove();
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

		/*
		 * "it" should now be an iterator on an empty chain
		 */
		assertFalse(it.hasNext(), testName + " unexpected next presence");
		assertFalse(it.hasPrevious(),
		            testName + " unexpected previous presence");
	}

	/**
	 * Test method for
	 * {@link collections.nodes.NodeListIterator#set(java.lang.Object)}.
	 * @param testInfo informations about the current test
	 */
	@Test
	@DisplayName("set(E)")
	@Order(10)
	final void testSet(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		/*
		 * NodeListIterator on no node
		 */
		assertThrows(IllegalStateException.class, () -> {
			ListIterator<String> it = new NodeListIterator<>(headNothing);
			it.set("impossible");
		}, testName + " set(E) didn't throw exception on empty iterator");

		/*
		 * NodeListIterator on single node
		 */
		assertThrows(IllegalStateException.class, () -> {
			ListIterator<String> it = new NodeListIterator<>(headSingle);
			// set without next() or previous() call should fail
			it.set("impossible");
		}, testName + " set(E) didn't throw exception");

		ListIterator<String> it = new NodeListIterator<>(headSingle);
		String expected = wordSupplier.get();
		try
		{
			it.next();
			// set on next
			it.set(expected);
			// assert on previous
			assertEquals(expected,
			             it.previous(),
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
			it.set(expected);
			// assert on next
			assertEquals(expected,
			             it.next(),
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

		/*
		 * NodeListIterator on chain node
		 */
		it = new NodeListIterator<>(headMulti);
		try
		{
			it.set(wordSupplier.get());
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
				it.next();
			}
			catch (NoSuchElementException e)
			{
				fail(testName + " unexpected next fail:"
				    + e.getLocalizedMessage());
			}

			expected = wordSupplier.get();
			try
			{
				it.set(expected);
			}
			catch (IllegalStateException e)
			{
				fail(testName + " unexpected set(E) failed: "
				    + e.getLocalizedMessage());
			}

			try
			{
				provided = it.previous();
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
				it.next();
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
		 * As long as there is a last node, set is possible
		 */
		try
		{
			it.set(expected);
		}
		catch (IllegalStateException e)
		{
			fail(testName + " unexpected set(E) failed: "
			    + e.getLocalizedMessage());
		}

		try
		{
			provided = it.previous();
			assertEquals(expected,
			             provided,
			             testName + " unexpected previous() value");
			it.next();
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
				it.previous();
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
				it.set(expected);
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
					provided = it.next();
					assertEquals(expected,
					             provided,
					             testName + " unexpected next() value");
					it.previous();
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
		 * Setting null always fail
		 */
		try
		{
			it.set(null);
			fail(testName + " unexpected set(null) success");
		}
		catch (IllegalArgumentException e)
		{
			// Nothing: expected
		}
	}

	/**
	 * Test method for
	 * {@link collections.nodes.NodeListIterator#add(java.lang.Object)}.
	 * @param testInfo informations about the current test
	 */
	@Test
	@DisplayName("add(E)")
	@Order(11)
	final void testAdd(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		/*
		 * NodeListIterator on no node
		 */
		ListIterator<String> it = new NodeListIterator<>(headNothing);
		try
		{
			it.add(null);
			fail(testName + " unexpected add(null) success");
		}
		catch (IllegalArgumentException e)
		{
			// Nothing: expected
		}

		String expected = wordSupplier.get();
		int expectedNextIndex = it.nextIndex() + 1;
		try
		{
			it.add(expected);
			assertEquals(expectedNextIndex,
			             it.nextIndex(),
			             testName + " unexpected nextIndex value");
		}
		catch (IllegalStateException e)
		{
			fail(testName + " unexpected add(E) fail: "
			    + e.getLocalizedMessage());
		}
		try
		{
			// further modification should not fail
			it.add(expected);
		}
		catch (IllegalStateException e)
		{
			fail(testName + " unexpected second add(E) failure: "
			    + e.getLocalizedMessage());
		}
		try
		{
			// further modification should fail
			it.remove();
			fail(testName + " unexpected remove() success");
		}
		catch (IllegalStateException e)
		{
			// Nothing: expected
		}

		assertFalse(it.hasNext(), testName + " unexpected next presence");
		assertTrue(it.hasPrevious(), testName + " unexpected no previous");
		try
		{
			it.next();
			fail(testName + " unexpected next() success");
		}
		catch (NoSuchElementException e)
		{
			// Nothing: expected
		}

		try
		{
			assertEquals(expected,
			             it.previous(),
			             testName + " unexpected previous() value");
		}
		catch (NoSuchElementException e)
		{
			fail(testName + " unexpected previous() fail: "
			    + e.getLocalizedMessage());
		}

		/*
		 * NodeListIterator on single node
		 */
		it = new NodeListIterator<>(headSingle);
		Node<String> headSingleNode = headSingle.getHead();
		expected = wordSupplier.get();
		expectedNextIndex = it.nextIndex() + 1;
		try
		{
			it.add(expected);
			assertEquals(expectedNextIndex,
			             it.nextIndex(),
			             testName + " unexpected nextIndex value");
		}
		catch (IllegalStateException e)
		{
			fail(testName + " unexpected add(E) fail: "
			    + e.getLocalizedMessage());
		}
		assertTrue(it.hasNext(), testName + " unexpected no next");
		assertTrue(it.hasPrevious(), testName + " unexpected no previous");
		try
		{
			assertEquals(expected,
			             it.previous(),
			             testName + " unexpected previous() value");
			assertEquals(expected,
			             it.next(),
			             testName + " unexpected next() value");
		}
		catch (NoSuchElementException e)
		{
			fail(testName + " unexpected next() or previous() fail: "
			    + e.getLocalizedMessage());
		}
		try
		{
			assertEquals(headSingleNode.getData(),
			             it.next(),
			             testName + " unexpected next() value");
		}
		catch (NoSuchElementException e)
		{
			fail(testName + " unexpected next() fail: "
			    + e.getLocalizedMessage());
		}

		try
		{
			it.add(null);
			fail(testName + " unexpected add(null) success");
		}
		catch (IllegalArgumentException e)
		{
			// Nothing: expected
		}

		/*
		 * NodeListIterator on chain node
		 */
		it = new NodeListIterator<>(headMulti);

		/*
		 * If iterator is already modified (either by remove or add) then add
		 * should fail with IllegalStateException
		 */
	}
}
