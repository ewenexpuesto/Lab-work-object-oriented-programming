package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import collections.nodes.Headed;
import collections.nodes.Node;
import collections.nodes.NodeIterator;
import tests.utils.HeadHolder;
import tests.utils.WordSupplier;

/**
 * Test class for {@link NodeIterator}
 */
public class NodeIteratorTest
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
		System.out.println("NodeIterator tests");
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
		System.out.println("NodeIterator tests end");
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
	 * {@link collections.nodes.NodeIterator#NodeIterator(collections.nodes.Headed, int)}.
	 */
	@Test
	@DisplayName("NodeIterator(Headed<E>, int)")
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
			Iterator<String> itn = new NodeIterator<>(null, 0);
			itn.next();
		});

		/*
		 * NodeIterator on no nodes
		 */
		Iterator<String> it = new NodeIterator<>(headNothing, 0);
		assertNotNull(it, testName + " unexpected null iterator");
		assertFalse(it.hasNext(), testName + " unexpected next elt to iterate");
		assertThrows(IndexOutOfBoundsException.class, () ->{
			Iterator<String> itLocal = new NodeIterator<>(headNothing, -1);
			itLocal.next();
		}, testName + " NodeIterator with negative index didn't throw");
		assertThrows(IndexOutOfBoundsException.class, () ->{
			Iterator<String> itLocal = new NodeIterator<>(headNothing, 1);
			itLocal.next();
		}, testName + " NodeIterator with negative index didn't throw");


		/*
		 * NodeIterator on single node
		 */
		assertThrows(IndexOutOfBoundsException.class, () ->{
			Iterator<String> itLocal = new NodeIterator<>(headSingle, -1);
			itLocal.next();
		}, testName + " NodeIterator with negative index didn't throw");
		assertThrows(IndexOutOfBoundsException.class, () ->{
			Iterator<String> itLocal = new NodeIterator<>(headSingle, 2);
			itLocal.next();
		}, testName + " NodeIterator with negative index didn't throw");

		it = new NodeIterator<>(headSingle, 0);
		Node<String> singleNode = headSingle.getHead();
		assertNotNull(it, testName + " unexpected null iterator");
		assertTrue(it.hasNext(),
		           testName + " unexpected no next elt to iterate");
		String nodeContent = it.next();
		assertEquals(singleNode.getData(),
		             nodeContent,
		             testName + " unexpected next content");

		it = new NodeIterator<>(headSingle, 1);
		assertNotNull(it, testName + " unexpected null iterator");
		assertFalse(it.hasNext(), testName + " unexpected next element");
		try
		{
			it.next();
			fail(testName + " unexpected next() success");
		}
		catch (NoSuchElementException e)
		{
			// Nothing: expected
		}

		/*
		 * NodeIterator on chain node
		 */
		assertThrows(IndexOutOfBoundsException.class, () ->{
			Iterator<String> itLocal = new NodeIterator<>(headMulti, -1);
			itLocal.next();
		}, testName + " NodeIterator with negative index didn't throw");

		int nbTests = chainLength + 2;
		for (int i = 0; i < nbTests; i++)
		{
			setupHeadNode(chainLength);
			try
			{
			it = new NodeIterator<>(headMulti, i);
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
		}
		it = new NodeIterator<>(headMulti, chainLength);
		assertNotNull(it, testName + " unexpected null iterator");
		assertFalse(it.hasNext(), testName + " unexpected next presence");
	}

	/**
	 * Test method for
	 * @param testInfo informations about the current test
	 * {@link collections.nodes.NodeIterator#NodeIterator(Headed)}.
	 */
	@Test
	@DisplayName("NodeIterator(Headed<E>)")
	final void testNodeIteratorNode(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		/*
		 * NodeIterator on no node
		 */
		Iterator<String> it = new NodeIterator<>(headNothing);
		assertNotNull(it, testName + " unexpected null iterator");
		assertFalse(it.hasNext(), testName + " unexpected next elt to iterate");

		/*
		 * NodeIterator on single node
		 */
		it = new NodeIterator<>(headSingle);
		Node<String> headSingleNode = headSingle.getHead();
		assertNotNull(it, testName + " unexpected null iterator");
		assertTrue(it.hasNext(),
		           testName + " unexpected no next elt to iterate");
		String nodeContent = it.next();
		assertEquals(headSingleNode.getData(),
		             nodeContent,
		             testName + " unexpected next content");

		/*
		 * NodeIterator on chain node
		 */
		it = new NodeIterator<>(headMulti);
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
	}

	/**
	 * Test method for {@link collections.nodes.NodeIterator#hasNext()}.
	 * @param testInfo informations about the current test
	 */
	@Test
	@DisplayName("hasNext()")
	final void testHasNext(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);


		/*
		 * NodeIterator on no node
		 */
		Iterator<String> it = new NodeIterator<>(headNothing);
		assertFalse(it.hasNext(),
		            testName
		                + " unexpected next elt to iterate on empty iterator");

		/*
		 * NodeIterator on single node
		 */
		it = new NodeIterator<>(headSingle);
		assertTrue(it.hasNext(),
		           testName + " unexpected no next elt to iterate");

		/*
		 * NodeIterator on chain node
		 */
		it = new NodeIterator<>(headMulti);
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
	 * Test method for {@link collections.nodes.NodeIterator#next()}.
	 * @param testInfo informations about the current test
	 */
	@Test
	@DisplayName("next()")
	final void testNext(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		/*
		 * NodeIterator on no node
		 */
		assertThrows(NoSuchElementException.class, () -> {
			Iterator<String> it = new NodeIterator<>(headNothing);
			it.next();
		}, testName + " next() unexpectedly didn't throw on empty iterator");

		/*
		 * NodeIterator on single node
		 */
		Iterator<String> it = new NodeIterator<>(headSingle);
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
		 * NodeIterator on chain node
		 */
		it = new NodeIterator<>(headMulti);
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
	 * Test method for {@link collections.nodes.NodeIterator#remove()}.
	 * @param testInfo informations about the current test
	 */
	@Test
	@DisplayName("remove()")
	final void testRemove(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		/*
		 * NodeIterator on no node
		 */
		assertThrows(IllegalStateException.class, () -> {
			Iterator<String> it = new NodeIterator<>(headNothing);
			it.remove();
		}, testName + " remove() didn't throw exception on empty iterator");

		/*
		 * NodeIterator on single node
		 */
		assertThrows(IllegalStateException.class, () -> {
			Iterator<String> it = new NodeIterator<>(headSingle);
			it.remove();
		}, testName + " remove() didn't throw exception");
		Iterator<String> it = new NodeIterator<>(headSingle);
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
		 * NodeIterator on chain node
		 */
		it = new NodeIterator<>(headMulti);
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
		it = new NodeIterator<>(headMulti);
		// Forward iteration
		count = 0;
		while (count < chainLength)
		{
			it.next();
			count++;
		}

		/*
		 * "it" should now be an iterator on an empty chain
		 */
		assertFalse(it.hasNext(), testName + " unexpected next presence");
	}
}
