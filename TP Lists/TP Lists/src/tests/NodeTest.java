package tests;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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

import collections.nodes.Node;
import tests.utils.WordSupplier;

/**
 * Test class for {@link Node}s
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Node<E>")
public class NodeTest
{
	/**
	 * The node under test
	 */
	private Node<String> testNode = null;

	/**
	 * Random word supplier
	 */
	private static WordSupplier wordSupplier = null;

	/**
	 * Setup before all tests
	 * @throws java.lang.Exception if setup before all tests fails
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
		wordSupplier = new WordSupplier();
		wordSupplier.setAutoReset(true);
		System.out.println("-------------------------------------------------");
		System.out.println("Node tests");
		System.out.println("-------------------------------------------------");
	}

	/**
	 * Tear down after all tests
	 * @throws java.lang.Exception if tear down after all tests fails
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception
	{
		System.out.println("-------------------------------------------------");
		System.out.println("Node tests end");
		System.out.println("-------------------------------------------------");
	}

	/**
	 * Setup before each test
	 * @throws java.lang.Exception if setup before each test fails
	 */
	@BeforeEach
	void setUp() throws Exception
	{
		testNode = null;
	}

	/**
	 * Setup new node with provided values
	 * @param word the data to set in the node
	 * @param previous the previous node to set
	 * @param next the next node to set
	 */
	private void setupNode(String word,
						   Node<String> previous,
						   Node<String> next)
	{
		testNode = new Node<String>(word, previous, next);
	}

	/**
	 * Tear down after each test
	 * @throws java.lang.Exception if tear down after each test fails
	 */
	@AfterEach
	void tearDown() throws Exception
	{
		testNode = null;
	}

	/**
	 * Test method for {@link collections.nodes.Node#Node(java.lang.Object)}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("Node(E)")
	@Order(1)
	final void testNodeE(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		String word = wordSupplier.get();
		setupNode(word, null, null);
		assertNotNull(testNode, testName + " unexpected null test node");
		assertEquals(word,
		             testNode.getData(),
		             testName + " unexpected node data");
		assertFalse(testNode.hasPrevious(),
		            testName + " unexpected previous node presence");
		assertNull(testNode.getPrevious(),
		           testName + " unexpected non null previous node");
		assertFalse(testNode.hasNext(),
		            testName + " unexpected next node presence");
		assertNull(testNode.getNext(),
		           testName + " unexpected non null next node");
	}

	/**
	 * Test method for
	 * {@link collections.nodes.Node#Node(java.lang.Object, collections.nodes.Node, collections.nodes.Node)}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("Node(E, Node<E>, Node<E>")
	@Order(2)
	final void testNodeENodeOfENodeOfE(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		Node<String> previous = new Node<>(wordSupplier.get());
		Node<String> next = new Node<>(wordSupplier.get());
		String word = wordSupplier.get();

		try
		{
			setupNode(word, null, next);
		}
		catch (NullPointerException npe)
		{
			fail(testName + " unexpected exception with null previous: "
			    + npe.getLocalizedMessage());
		}

		try
		{
			setupNode(word, previous, null);
		}
		catch (NullPointerException npe)
		{
			fail(testName + " unexpected exception with null next: "
			    + npe.getLocalizedMessage());
		}

		setupNode(word, previous, next);
		assertNotNull(testNode, testName + " unexpected null test node");
		assertEquals(word,
		             testNode.getData(),
		             testName + " unexpected node data");

		Node<String> obtainedPrevious = testNode.getPrevious();
		assertNotNull(obtainedPrevious, testName + " unexpected null previous");
		assertSame(previous,
		           obtainedPrevious,
		           testName + " unexpected previous");
		assertSame(testNode,
		           obtainedPrevious.getNext(),
		           testName + " unexpected next of previous node");

		Node<String> obtainedNext = testNode.getNext();
		assertNotNull(obtainedNext, testName + " unexpected null next");
		assertSame(next, obtainedNext, testName + " unexpected next");
		assertSame(testNode,
		           obtainedNext.getPrevious(),
		           testName + " unexpected previous of next node");
	}

	/**
	 * Test method for {@link collections.nodes.Node#getData()}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("getData()")
	@Order(3)
	final void testGetData(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		String word = wordSupplier.get();
		setupNode(word, null, null);
		assertNotNull(testNode, testName + " unexpected null test node");
		assertEquals(word, testNode.getData(), testName + " unexpected data");
	}

	/**
	 * Test method for {@link collections.nodes.Node#getPrevious()}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("getPrevious()")
	@Order(4)
	final void testGetPrevious(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		Node<String> aNode = new Node<>(wordSupplier.get());
		String word = wordSupplier.get();

		setupNode(word, null, null);
		assertNotNull(testNode, testName + " unexpected null test node");
		assertNull(testNode.getPrevious(),
		           testName + " unexpected not null previous");

		setupNode(word, aNode, null);
		assertNotNull(testNode, testName + " unexpected null test node");
		Node<String> nodePrevious = testNode.getPrevious();
		assertNotNull(nodePrevious, testName + " unexpected null previous");
		assertSame(aNode, nodePrevious, testName + " unexpected previous");
	}

	/**
	 * Test method for {@link collections.nodes.Node#getNext()}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("getNext()")
	@Order(5)
	final void testGetNext(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		Node<String> aNode = new Node<>(wordSupplier.get());
		String word = wordSupplier.get();

		setupNode(word, null, null);
		assertNotNull(testNode, testName + " unexpected null test node");
		assertNull(testNode.getNext(), testName + " unexpected not null next");

		setupNode(word, null, aNode);
		assertNotNull(testNode, testName + " unexpected null test node");
		Node<String> nodeNext = testNode.getNext();
		assertNotNull(nodeNext, testName + " unexpected null next");
		assertSame(aNode, nodeNext, testName + " unexpected next");
	}

	/**
	 * Test method for {@link collections.nodes.Node#hasNext()}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("hasNext()")
	@Order(6)
	final void testHasNext(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		Node<String> aNode = new Node<>(wordSupplier.get());
		String word = wordSupplier.get();

		setupNode(word, null, null);
		assertNotNull(testNode, testName + " unexpected null test node");
		assertFalse(testNode.hasNext(),
		            testName + " unexpected hasNext() result");

		setupNode(word, null, aNode);
		assertNotNull(testNode, testName + " unexpected null test node");
		assertTrue(testNode.hasNext(),
		           testName + " unexpected hasNext() result");
	}

	/**
	 * Test method for {@link collections.nodes.Node#hasPrevious()}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("hasPrevious()")
	@Order(7)
	final void testHasPrevious(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		Node<String> aNode = new Node<>(wordSupplier.get());
		String word = wordSupplier.get();

		setupNode(word, null, null);
		assertNotNull(testNode, testName + " unexpected null test node");
		assertFalse(testNode.hasPrevious(),
		            testName + " unexpected hasPrevious() result");

		setupNode(word, aNode, null);
		assertNotNull(testNode, testName + " unexpected null test node");
		assertTrue(testNode.hasPrevious(),
		           testName + " unexpected hasPrevious() result");
	}

	/**
	 * Test method for
	 * {@link collections.nodes.Node#setPrevious(collections.nodes.Node)}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("setPrevious(Node<E>)")
	@Order(8)
	final void testSetPrevious(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		Node<String> aNode = new Node<>(wordSupplier.get());
		String word = wordSupplier.get();

		setupNode(word, null, null);
		assertNotNull(testNode, testName + " unexpected null test node");
		Node<String> currentPrevious = testNode.getPrevious();
		Node<String> previousNext = aNode.getNext();
		assertNull(currentPrevious, testName + " unexpected non null previous");
		assertNull(previousNext,
		           testName + " unexpected non null next for future previous");

		testNode.setPrevious(null);
		assertNull(testNode.getPrevious(),
		           testName
		               + " unexpected previous value after setPrevious(null)");

		testNode.setPrevious(aNode);
		assertSame(aNode,
		           testNode.getPrevious(),
		           testName + " unexpected previous after setPrevious");
		assertSame(testNode,
		           aNode.getNext(),
		           testName + " unexpected previous's next after setPrevious");
	}

	/**
	 * Test method for
	 * {@link collections.nodes.Node#setNext(collections.nodes.Node)}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("setNext(Node<E>)")
	@Order(9)
	final void testSetNext(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		Node<String> aNode = new Node<>(wordSupplier.get());
		String word = wordSupplier.get();

		setupNode(word, null, null);
		assertNotNull(testNode, testName + " unexpected null test node");
		Node<String> currentNext = testNode.getNext();
		Node<String> nextPrevious = aNode.getPrevious();
		assertNull(currentNext, testName + " unexpected non null next");
		assertNull(nextPrevious,
		           testName + " unexpected non null previous for future next");

		testNode.setNext(null);
		assertNull(testNode.getNext(),
		           testName + " unexpected next value after setNext(null)");

		testNode.setNext(aNode);
		assertSame(aNode,
		           testNode.getNext(),
		           testName + " unexpected next after setNext");
		assertSame(testNode,
		           aNode.getPrevious(),
		           testName + " unexpected next's previous after setNext");
	}

	/**
	 * Test method for
	 * {@link collections.nodes.Node#insertNext(collections.nodes.Node)}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("insertNext(Node<E>)")
	@Order(10)
	final void testInsertNext(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		Node<String> aNode = new Node<>(wordSupplier.get());
		Node<String> anotherNode = new Node<>(wordSupplier.get());
		String word = wordSupplier.get();

		setupNode(word, null, null);
		assertNotNull(testNode, testName + " unexpected null test node");
		Node<String> currentNext = testNode.getNext();
		Node<String> nextPrevious = aNode.getPrevious();
		assertNull(currentNext, testName + " unexpected non null next");
		assertNull(nextPrevious,
		           testName + " unexpected non null previous for future next");

		assertThrows(NullPointerException.class, () -> {
			testNode.insertNext(null);
		});

		Node<String> oldNext = testNode.getNext();
		Node<String> oldPrevious = testNode.getPrevious();
		testNode.insertNext(aNode);
		assertSame(oldPrevious,
		           testNode.getPrevious(),
		           testName + " previous node unexpectedly changed");
		assertSame(aNode,
		           testNode.getNext(),
		           testName + " unexpected next after setNext");
		assertSame(testNode,
		           aNode.getPrevious(),
		           testName + " unexpected next's previous after setNext");
		assertSame(oldNext,
		           aNode.getNext(),
		           testName + " unexpected inserted node's next");

		oldNext = testNode.getNext();
		testNode.insertNext(anotherNode);
		assertSame(oldPrevious,
		           testNode.getPrevious(),
		           testName + " previous node unexpectedly changed");
		assertSame(anotherNode,
		           testNode.getNext(),
		           testName + " unexpected next after setNext");
		assertSame(testNode,
		           anotherNode.getPrevious(),
		           testName + " unexpected next's previous after setNext");
		assertSame(oldNext,
		           anotherNode.getNext(),
		           testName + " unexpected inserted node's next");
		assertSame(anotherNode,
		           oldNext.getPrevious(),
		           testName + " unexpected previous next's previous");
	}

	/**
	 * Test method for
	 * {@link collections.nodes.Node#insertPrevious(collections.nodes.Node)}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("insertPrevious(Node<E>)")
	@Order(11)
	final void testInsertPrevious(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		Node<String> aNode = new Node<>(wordSupplier.get());
		Node<String> anotherNode = new Node<>(wordSupplier.get());
		String word = wordSupplier.get();

		setupNode(word, null, null);
		assertNotNull(testNode, testName + " unexpected null test node");
		Node<String> currentPrevious = testNode.getPrevious();
		Node<String> previousNext = aNode.getNext();
		assertNull(currentPrevious, testName + " unexpected non null previous");
		assertNull(previousNext,
		           testName + " unexpected non null next for future previous");

		assertThrows(NullPointerException.class, () -> {
			testNode.insertPrevious(null);
		});

		Node<String> oldPrevious = testNode.getPrevious();
		Node<String> oldNext = testNode.getNext();
		testNode.insertPrevious(aNode);
		assertSame(oldNext,
		           testNode.getNext(),
		           testName + " next node unexpectedly changed");
		assertSame(aNode,
		           testNode.getPrevious(),
		           testName + " unexpected previous after setPrevious");
		assertSame(testNode,
		           aNode.getNext(),
		           testName + " unexpected previous's next after setPrevious");
		assertSame(oldPrevious,
		           aNode.getPrevious(),
		           testName + " unexpected inserted node's previous");
		oldPrevious = testNode.getPrevious();
		testNode.insertPrevious(anotherNode);
		assertSame(oldNext,
		           testNode.getNext(),
		           testName + " next node unexpectedly changed");
		assertSame(anotherNode,
		           testNode.getPrevious(),
		           testName + " unexpected previous after setPrevious");
		assertSame(testNode,
		           anotherNode.getNext(),
		           testName + " unexpected previous's next after setPrevious");
		assertSame(oldPrevious,
		           anotherNode.getPrevious(),
		           testName + " unexpected inserted node's previous");
		assertSame(anotherNode,
		           oldPrevious.getNext(),
		           testName + " unexpected previous previous's next");
	}

	/**
	 * Test method for {@link collections.nodes.Node#removeNext()}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("removeNext()")
	@Order(12)
	final void testRemoveNext(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		Node<String> aNode = new Node<>(wordSupplier.get());
		String word = wordSupplier.get();
		setupNode(word, null, null);
		assertNotNull(testNode, testName + " unexpected null test node");

		Node<String> oldNext = testNode.getNext();
		Node<String> oldPrevious = testNode.getPrevious();
		testNode.removeNext();
		assertSame(oldNext,
		           testNode.getNext(),
		           testName
		               + " next changed on empty test node after removeNext");
		assertSame(oldPrevious,
		           testNode.getPrevious(),
		           testName
		               + " previous changed on empty test node after removeNext");

		testNode.setNext(aNode);
		assertNotNull(testNode.getNext(),
		              testName + " unexpected null next node");

		testNode.removeNext();
		assertSame(oldPrevious,
		           testNode.getPrevious(),
		           testName
		               + " previous node unexpectedly changed after removeNext");
		assertNull(testNode.getNext(),
		           testName + " unexpected non null next after removeNext");

		Node<String> anotherNode = new Node<>(wordSupplier.get(), aNode, null);
		assertSame(anotherNode,
		           aNode.getNext(),
		           testName + " unexpected next's next node");
		testNode.setNext(aNode);

		testNode.removeNext();
		assertSame(oldPrevious,
		           testNode.getPrevious(),
		           testName
		               + " previous node unexpectedly changed after removeNext");
		assertSame(anotherNode,
		           testNode.getNext(),
		           testName + " unexpected next node after removeNext");
		assertSame(testNode,
		           anotherNode.getPrevious(),
		           testName
		               + " unexpected next's previous node after removeNext");
		assertNull(aNode.getPrevious(),
		           testName + " unexpected removed node's previous");
		assertNull(aNode.getNext(),
		           testName + " unexpected removed node's next");
	}

	/**
	 * Test method for {@link collections.nodes.Node#removePrevious()}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("removePrevious()")
	@Order(13)
	final void testRemovePrevious(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		Node<String> aNode = new Node<>(wordSupplier.get());
		String word = wordSupplier.get();
		setupNode(word, null, null);
		assertNotNull(testNode, testName + " unexpected null test node");

		Node<String> oldNext = testNode.getNext();
		Node<String> oldPrevious = testNode.getPrevious();
		testNode.removePrevious();
		assertSame(oldNext,
		           testNode.getNext(),
		           testName
		               + " next changed on empty test node after removePrevious");
		assertSame(oldPrevious,
		           testNode.getPrevious(),
		           testName
		               + " previous changed on empty test node after removePrevious");

		testNode.setPrevious(aNode);
		assertNotNull(testNode.getPrevious(),
		              testName + " unexpected null previous node");
		oldPrevious = testNode.getPrevious();

		testNode.removePrevious();
		assertSame(oldNext,
		           testNode.getNext(),
		           testName
		               + " next node unexpectedly changed after removePrevious");
		assertNull(testNode.getPrevious(),
		           testName
		               + " unexpected non null previous after removePrevious");
		assertNull(aNode.getNext(),
		           testName
		               + " unexpected non null next on removed node after removePrevious");

		Node<String> anotherNode = new Node<>(wordSupplier.get(), null, aNode);
		assertSame(anotherNode,
		           aNode.getPrevious(),
		           testName + " unexpected previous's previous node");
		testNode.setPrevious(aNode);

		testNode.removePrevious();
		assertSame(oldNext,
		           testNode.getNext(),
		           testName
		               + " next node unexpectedly changed after removePrevious");
		assertSame(anotherNode,
		           testNode.getPrevious(),
		           testName + " unexpected previous node after removePrevious");
		assertSame(testNode,
		           anotherNode.getNext(),
		           testName
		               + " unexpected previous's next node after removePrevious");
		assertNull(aNode.getPrevious(),
		           testName + " unexpected removed node's previous");
		assertNull(aNode.getNext(),
		           testName + " unexpected removed node's next");
	}

	/**
	 * Test method for {@link collections.nodes.Node#remove()}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("remove()")
	@Order(14)
	final void testRemove(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		Node<String> previous = new Node<>(wordSupplier.get());
		Node<String> next = new Node<>(wordSupplier.get());
		String word = wordSupplier.get();
		setupNode(word, previous, next);
		assertSame(previous,
		           testNode.getPrevious(),
		           testName + " unexpected previous");
		assertSame(next, testNode.getNext(), testName + " unexpected next");
		assertSame(testNode,
		           previous.getNext(),
		           testName + " unexpected previous's next");
		assertSame(testNode,
		           next.getPrevious(),
		           testName + " unexpected next's previous");
		assertNotSame(previous.getNext(),
		              next,
		              testName + " unexpected next's previous");
		assertNotSame(next.getPrevious(),
		              previous,
		              testName + " unexpected previous's next");

		testNode.remove();
		assertNull(testNode.getPrevious(),
		           testName + " unexpected next after remove()");
		assertNull(testNode.getNext(),
		           testName + " unexpected previous after remove()");
		assertSame(next,
		           previous.getNext(),
		           testName + " unexpected previous's next");
		assertSame(previous,
		           next.getPrevious(),
		           testName + " unexpected next's previous");
	}

	/**
	 * Test method for {@link collections.nodes.Node#nextLength()}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("nextLength()")
	@Order(15)
	final void testNextLength(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		Node<String> previous = new Node<>(wordSupplier.get());
		Node<String> next = new Node<>(wordSupplier.get());
		String word = wordSupplier.get();
		setupNode(word, previous, next);
		assertEquals(2,
		             testNode.nextLength(),
		             testName + " unexpected next length value");
		Node<String> nextnext = new Node<>(wordSupplier.get(), null, null);
		next.setNext(nextnext);
		assertEquals(3,
		             testNode.nextLength(),
		             testName + " unexpected next length value");

		testNode.remove();
		assertEquals(1,
		             testNode.nextLength(),
		             testName + " unexpected next length value");
	}

	/**
	 * Test method for {@link collections.nodes.Node#previousLength()}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("previousLength()")
	@Order(16)
	final void testPreviousLength(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		Node<String> previous = new Node<>(wordSupplier.get());
		Node<String> next = new Node<>(wordSupplier.get());
		String word = wordSupplier.get();
		setupNode(word, previous, next);
		assertEquals(2,
		             testNode.previousLength(),
		             testName + " unexpected previous length value");
		Node<String> previousprevious = new Node<>(wordSupplier.get(), null, null);
		previous.setPrevious(previousprevious);
		assertEquals(3,
		             testNode.previousLength(),
		             testName + " unexpected previous length value");

		testNode.remove();
		assertEquals(1,
		             testNode.previousLength(),
		             testName + " unexpected previous length value");
	}

	/**
	 * Test method for {@link collections.nodes.Node#equals(java.lang.Object)}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("equals(Object)")
	@Order(18)
	final void testEqualsObject(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		String word = wordSupplier.get();
		setupNode(word, null, null);

		// compare with null
		assertFalse(testNode.equals(null),
		            testName + " unexpected result with equals(null)");

		// compare with this
		assertTrue(testNode.equals(testNode),
		           testName + " unexpected result with equals(this)");

		// compare with similar
		Node<String> aNode = new Node<>(word, null, null);
		assertTrue(testNode.equals(aNode),
		           testName + " unexpected result with similar node");
		String previousWord = wordSupplier.get();
		String nextWord = wordSupplier.get();

		testNode.setNext(new Node<>(nextWord));
		testNode.setPrevious(new Node<>(previousWord));
		aNode.setNext(new Node<>(nextWord));
		aNode.setPrevious(new Node<>(previousWord));
		assertFalse(testNode.equals(aNode),
		            testName + " unexpected equality between nodes");

		Node<String> previousNode = new Node<>(previousWord);
		Node<String> nextNode = new Node<>(nextWord);
		testNode.setPrevious(previousNode);
		testNode.setNext(nextNode);
		aNode.setPrevious(previousNode);
		aNode.setNext(nextNode);
		assertTrue(testNode.equals(aNode),
		           testName + " unexpected inequality between nodes");
	}

	/**
	 * Test method for {@link collections.nodes.Node#isDangling()}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("isDangling()")
	@Order(17)
	final void testIsDangling(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		String word = wordSupplier.get();
		setupNode(word, null, null);

		// single node with no previous nor next can't be dangling
		assertFalse(testNode.isDangling(),
		            testName + " unexpected dangling test node");

		// compare with similar
		Node<String> aNode = new Node<>(word, null, null);
		assertTrue(testNode.equals(aNode),
		           testName + " unexpected result with similar node");
		String previousWord = wordSupplier.get();
		String nextWord = wordSupplier.get();

		Node<String> nextNode = new Node<>(nextWord);
		testNode.setNext(nextNode);
		assertFalse(testNode.isDangling(),
		            testName + " unexpected dangling test node after setNext");
		assertFalse(nextNode.isDangling(),
		            testName + " unexpected dangling next node after setNext");

		Node<String> previousNode = new Node<>(previousWord);
		testNode.setPrevious(previousNode);
		assertFalse(testNode.isDangling(),
		            testName
		                + " unexpected dangling test node after setPrevious");
		assertFalse(previousNode.isDangling(),
		            testName
		                + " unexpected dangling previous node after setPrevious");

		aNode.setNext(nextNode);
		aNode.setPrevious(previousNode);
		assertFalse(aNode.isDangling(), testName + " unexpected dangling node");
		assertFalse(nextNode.isDangling(),
		            testName + " unexpected dangling next node");
		assertFalse(previousNode.isDangling(),
		            testName + " unexpected dangling previous node");
		// testNode still points to nextNode and previousNode and therefor is
		// dangling
		assertTrue(testNode.isDangling(),
		           testName + " unexpected non dangling test node");
	}

	/**
	 * Test method for {@link collections.nodes.Node#hashCode()}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("hashCode()")
	@Order(19)
	final void testHashCode(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		String word = wordSupplier.get();
		int wordHash = word.hashCode();
		setupNode(word, null, null);
		final int prime = 31;
		int hasHash = 1231;
		int hasNotHash = 1237;
		int expectedHash = 1;
		expectedHash = (prime * expectedHash) + wordHash;
		expectedHash = (prime * expectedHash) + hasNotHash;
		expectedHash = (prime * expectedHash) + hasNotHash;
		assertEquals(expectedHash,
		             testNode.hashCode(),
		             testName + " unexpected hash code on single node");

		String previousWord = wordSupplier.get();
		String nextWord = wordSupplier.get();
		Node<String> nextNode = new Node<>(nextWord);
		testNode.setNext(nextNode);
		expectedHash = 1;
		expectedHash = (prime * expectedHash) + wordHash;
		expectedHash = (prime * expectedHash) + hasNotHash;
		expectedHash = (prime * expectedHash) + hasHash;

		assertEquals(expectedHash,
		             testNode.hashCode(),
		             testName + " unexpected hash code on node with next");

		Node<String> previousNode = new Node<>(previousWord);
		testNode.setPrevious(previousNode);
		expectedHash = 1;
		expectedHash = (prime * expectedHash) + wordHash;
		expectedHash = (prime * expectedHash) + hasHash;
		expectedHash = (prime * expectedHash) + hasHash;
		assertEquals(expectedHash,
		             testNode.hashCode(),
		             testName + " unexpected hash code on node with next and previous");

	}

	/**
	 * Test method for {@link collections.nodes.Node#toString()}.
	 * @param testInfo Test information to extract Display Name
	 */
	@Test
	@DisplayName("toString()")
	@Order(20)
	final void testToString(TestInfo testInfo)
	{
		String testName = testInfo.getDisplayName();
		System.out.println(testName);

		String word = wordSupplier.get();
		setupNode(word, null, null);
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		builder.append(word);
		builder.append(']');
		String expectedString = builder.toString();
		assertEquals(expectedString,
		             testNode.toString(),
		             testName + " unexpected string on single node");

		String previousWord = wordSupplier.get();
		String nextWord = wordSupplier.get();
		Node<String> nextNode = new Node<>(nextWord);
		testNode.setNext(nextNode);
		builder.append("->");
		expectedString = builder.toString();

		assertEquals(expectedString,
		             testNode.toString(),
		             testName + " unexpected string on node with next");

		Node<String> previousNode = new Node<>(previousWord);
		testNode.setPrevious(previousNode);
		builder = new StringBuilder();
		builder.append("<-");
		builder.append(expectedString);
		expectedString = builder.toString();
		assertEquals(expectedString,
		             testNode.toString(),
		             testName
		                 + " unexpected string on node with next and previous");
	}
}
