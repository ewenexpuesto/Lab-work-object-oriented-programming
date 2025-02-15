package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import figures.Circle;
import figures.Figure;
import figures.Group;
import figures.Polygon;
import figures.Rectangle;
import figures.Triangle;
import points.Point2D;

/**
 * Classe de test pour les {@link Group}
 * @implNote on ne teste que ce qui n'est pas déjà testé dans {@link FigureTest}
 * @author davidroussel
 */
@DisplayName("Group")
public class GroupTest
{
	/**
	 * Base name pour tous les tests
	 */
	private final static String testBasename = new String(Group.class.getSimpleName());

	/**
	 * Les points à utiliser pour construire les figures
	 */
	private static final Point2D[][] points = new Point2D[][] {
		{new Point2D(7,3)}, // 0,0 : Circle
		{new Point2D(4,1), new Point2D(8,4)}, // 1,0->1 : Rectangle
		{new Point2D(3,2), new Point2D(7,3), new Point2D(4,6)}, // 2,0->2 : Triangle
		{new Point2D(5,1), new Point2D(8,2), new Point2D(7,5), new Point2D(2,4),
			new Point2D(2,3)} // 3,0->4 : Polygon
	};

	/**
	 * L'ensemble des figures à mettre dans le groupe
	 */
	private static Figure[] figures = new Figure[4];

	/**
	 * L'ensemble des figures à mettre dans le groupe (y compris un groupe)
	 */
	private static Figure[] allFigures = new Figure[5];

	/**
	 * Centre de masse du groupe de figures {@link #figures}
	 */
	private final static Point2D figuresCenter = new Point2D(5.77714726, 2.996496);

	/**
	 * Centre de la bounding box du groupe de figures {@link #figures}
	 */
	private final static Point2D figuresBBCenter = new Point2D(5.5, 3.5);

	/**
	 * Tolérance pour les dimensions, distances, aires, etc.
	 */
	private final static double tolerance = Point2D.getEpsilon();

	/**
	 * Aire du groupe de figures {@link #figures}
	 */
	private final static double figuresArea = 47.566371;

	/**
	 * largeur du groupe de figures {@link #figures}
	 */
	private final static double figuresWidth = 7.0;

	/**
	 * hauteur du groupe de figures {@link #figures}
	 */
	private final static double figuresHeight = 5.0;

	/**
	 * Progression du centre du groupe en fonction de l'ajout de nouvelles
	 * figures dans le groupe
	 */
	private final static Point2D[] progressiveCenters = new Point2D[] {
		new Point2D(0.0, 0.0),
		new Point2D(7.0, 3.0),
		new Point2D(6.5115274, 2.755764),
		new Point2D(6.080033, 2.968815),
		figuresCenter
	};

	/**
	 * Progression du centre de la boite englobante en fonction de l'ajout
	 * de nouvelles figures dans le groupe
	 */
	private final static Point2D[] progressiveBBCenters = new Point2D[] {
		new Point2D(0.0, 0.0),
		new Point2D(7.0, 3.0),
		new Point2D(6.5, 3.0),
		new Point2D(6.0, 3.5),
		figuresBBCenter
	};

	/**
	 * Progression de l'aire en fonction de l'ajout
	 * de nouvelles figures dans le groupe
	 */
	private final static double[] progressiveAreas = new double[] {
		0.0,
		12.566371,
		24.566371,
		32.066371,
		figuresArea
	};

	/**
	 * Progression de la largeur en fonction de l'ajout
	 * de nouvelles figures dans le groupe
	 */
	private final static double[] progressiveWidths = new double[] {
		0.0,
		4.0,
		5.0,
		6.0,
		figuresWidth
	};

	/**
	 * Progression de la hauteur en fonction de l'ajout
	 * de nouvelles figures dans le groupe
	 */
	private final static double[] progressiveHeights = new double[] {
		0.0,
		4.0,
		4.0,
		5.0,
		figuresHeight
	};


	/**
	 * Un point à l'intérieur de toutes les figures de {@link #figures}
	 */
	private static final Point2D insidePoint = new Point2D(6,3);

	/**
	 * Un point à l'extérieur de toutes les figures de {@link #figures}
	 */
	private static final Point2D outsidePoint = new Point2D(6,5);


	/**
	 * Le type de la classe en cours de test
	 */
	private static final Class<Group> testClass = Group.class;

	/**
	 * Le groupe à tester
	 */
	private Group testGroup;

	/**
	 * Collection de figures simples
	 * @see #figures
	 */
	private Collection<Figure> figuresCollection;

	/**
	 * Collection de figures (contenant aussi un {@link Group})
	 * @see #allFigures
	 */
	private Collection<Figure> allFiguresCollection;

	/**
	 * Figures différentes de {@link #figures}
	 */
	static private Figure[] antiFigures = new Figure[] {
		new Rectangle(0.0, 1.0, 2.0, 3.0),
		new Circle(-1.0, 2.0, 1.0),
		new Polygon(new Point2D(0.0, 0.0),
		            new Point2D(1.0, 0.0),
		            new Point2D(1.0, 1.0),
		            new Point2D(0.0, 1.0)),
	    new Triangle(new Point2D(0.0, 0.0),
	                 new Point2D(1.0, 0.0),
	                 new Point2D(1.0, 1.0))
	};

	/**
	 * Collection de figures différentes de {@link #figuresCollection}
	 */
	static private Collection<Figure> antiFiguresCollection = Arrays.asList(antiFigures);

	/**
	 * Mélange aléatoire des éléments d'un tableau
	 * @param array le tableau à mélanger
	 * @return le tableau mélangé
	 */
	private static final <T> T[] shuffleArray(T[] array)
	{
		Random randomGenerator = new Random();

		for (int i = 0; i < array.length; i++)
		{
			int ri = randomGenerator.nextInt(array.length);
			T temp = array[i];
			array[i] = array[ri];
			array[ri] = temp;
		}

		return array;
	}

	/**
	 * Mélange la collection contenue dans input vers output
	 * @param input la collection initiale
	 * @param output la collection résultante (contient les éléments de la
	 * collection initiale dans un ordre aléatoire)
	 * @pre la collection output est considérée comme vide et les éléments
	 * aléatoirement choisis de input sont ajoutés à output
	 */
	private static final <T> void shuffleCollection(Collection<T> input,
	                                                Collection<T> output)
	{
		@SuppressWarnings("unchecked")
		T[] array = (T[])input.toArray();

		Random randomGenerator = new Random();
		for (int i = 0; i < array.length; i++)
		{
			int ri = randomGenerator.nextInt(array.length);
			if (i != ri)
			{
				T temp = array[i];
				array[i] = array[ri];
				array[ri] = temp;
			}
		}

		for (int i = 0; i < array.length; i++)
		{
			output.add(array[i]);
		}
	}

	/**
	 * Génération d'un tableau d'entiers croissants compris entre low et high
	 * @param low le premier nombre de la séquence
	 * @param high le second nombre de la séquence
	 * @return un tableau contenant des nombres variants entre low et high
	 */
	private static final Integer[] generateSequence(int low, int high)
	{
		int nbElements = Math.abs(high - low);
		int sign = (high - low) / nbElements;
		Integer[] array = new Integer[nbElements];
		for (int i = 0; i < nbElements; i++)
		{
			array[i] = low + (sign*i);	// newer Java auto-boxing
			// Or array[i] = new Integer(low + (sign*i); // with older Javas
		}
		return array;
	}

	/**
	 * Mise en place avant l'ensemble des tests
	 * @throws java.lang.Exception quand les poules auront des dents
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
		for (int i = 0; i < allFigures.length; i++)
		{
			switch (i)
			{
				case 0:
					Circle c = new Circle(points[i][0], 2.0);
					figures[i] = c;
					allFigures[i] = new Circle(c);
					break;
				case 1:
					Rectangle r = new Rectangle(points[i][0], points[i][1]);
					figures[i] = r;
					allFigures[i] = new Rectangle(r);
					break;
				case 2:
					Triangle t = new Triangle(points[i][0],
					                          points[i][1],
					                          points[i][2]);
					figures[i] = t;
					allFigures[i] = new Triangle(t);
					break;
				case 3:
					ArrayList<Point2D> polyPoints = new ArrayList<Point2D>();
					for (Point2D p : points[i])
					{
						polyPoints.add(p);
					}
					Polygon p = new Polygon(polyPoints);
					figures[i] = p;
					allFigures[i] = new Polygon(p);
					break;
				case 4:
					Collection<Figure> otherFigures = new ArrayList<Figure>();
					shuffleCollection(Arrays.asList(figures), otherFigures);
					allFigures[i] = new Group(otherFigures);
					break;
				default:
					fail("Unknown index " + i + " in figures");
					break;
			}
		}

		/*
		 * Expected constructors arguments types
		 */
		Class<?>[][] expectedConstructorsArguments = {
			{},					// Group()
			{Collection.class},	// Group(Collection<Figure>)
			{Group.class},		// Group(Group)
		};

		/*
		 * Expected methods in the test class (at least the ones under test):
		 * 	- Methods names
		 * 	- Methods arguments types
		 * 	- Methods return types
		 * 	- Modifiers of each method
		 */
		String[] expectedMethodsNames = {
			"add",			// boolean add(Figure f)
			"addAll",		// boolean addAll(Collection<? extends Figure> c)
			"clear",		// void clear()
			"contains",		// boolean contains(Point2D p)
			"contains",		// boolean contains(Figure f)
			"contains",		// boolean contains(Object o)
			"containsAll",	// boolean containsAll(Collection<?> c)
			"isEmpty",		// boolean isEmpty()
			"iterator",		// Iterator<Figure> iterator()
			"remove",		// boolean remove(Object o)
			"removeAll",	// boolean removeAll(Collection<?> c)
			"retainAll",	// boolean retainAll(Collection<?> c)
			"size",			// int size()
			"toArray",		// Object[] toArray()
			"toArray",		// T[] toArray(T[] t)
		};
		Class<?>[][] expectedMethodsArguments = {
			{Figure.class},		// boolean add(Figure f)
			{Collection.class},	// boolean addAll(Collection<? extends Figure> c)
			{},					// void clear()
			{Point2D.class},	// boolean contains(Point2D p)
			{Figure.class},		// boolean contains(Figure f)
			{Object.class},		// boolean contains(Object o)
			{Collection.class},	// boolean containsAll(Collection<?> c)
			{},					// boolean isEmpty()
			{},					// Iterator<Figure> iterator()
			{Object.class},		// boolean remove(Object o)
			{Collection.class},	// boolean removeAll(Collection<?> c)
			{Collection.class},	// boolean retainAll(Collection<?> c)
			{},					// int size()
			{},					// Object[] toArray()
			{Object[].class},	// T[] toArray(T[] t)
		};
		Class<?>[] expectedMethodsReturns = {
			boolean.class,	// boolean add(Figure f)
			boolean.class,	// boolean addAll(Collection<? extends Figure> c)
			void.class,		// void clear()
			boolean.class,	// boolean contains(Point2D p)
			boolean.class,	// boolean contains(Figure f)
			boolean.class,	// boolean contains(Object o)
			boolean.class,	// boolean containsAll(Collection<?> c)
			boolean.class,	// boolean isEmpty()
			Iterator.class,	// Iterator<Figure> iterator()
			boolean.class,	// boolean remove(Object o)
			boolean.class,	// boolean removeAll(Collection<?> c)
			boolean.class,	// boolean retainAll(Collection<?> c)
			int.class,		// int size()
			Object[].class,	// Object[] toArray()
			Object[].class,	// T[] toArray(T[] t)
		};

		String testName = "Group Content Test: ";

		boolean checked =
		    ContentChecker.assertContent(testName,
		                                 testClass,
		                                 expectedConstructorsArguments,
		                                 expectedMethodsNames,
		                                 expectedMethodsArguments,
		                                 expectedMethodsReturns,
		                                 null); // All methods are assumed public
		assertTrue(checked, testName + " content didn't check");

	}

	/**
	 * Nettoyage après l'ensemble des tests
	 * @throws java.lang.Exception quand les poules auront des dents
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception
	{
	}

	/**
	 * Mise en place avant chaque test
	 * @throws java.lang.Exception quand les poules auront des dents
	 */
	@BeforeEach
	void setUp() throws Exception
	{
		testGroup = null;
		figuresCollection = Arrays.asList(figures);
		allFiguresCollection = Arrays.asList(allFigures);
	}

	/**
	 * Nettoyage après chaque test
	 * @throws java.lang.Exception quand les poules auront des dents
	 */
	@AfterEach
	void tearDown() throws Exception
	{
	}

	/**
	 * Test method for {@link figures.Group#Group()}.
	 */
	@Test
	@DisplayName("Group()")
	final void testGroup()
	{
		String testName = new String(testBasename + "()");
		System.out.println(testName);

		testGroup = new Group();

		assertNotNull(testGroup, testName);

		assertEquals(new Point2D(0.0, 0.0),
		             testGroup.getCenter(),
		             testName + " failed with wrong center");
		assertEquals(0, testGroup.size(), testName + " failed with wrong size");
		assertEquals(0.0, testGroup.area(), testName + " failed with wrong area");
		assertEquals(0.0, testGroup.width(), testName + " failed with wrong width");
		assertEquals(0.0, testGroup.height(), testName + " failed with wrong height");
	}

	/**
	 * Test method for {@link figures.Group#Group(java.util.Collection)}.
	 */
	@Test
	@DisplayName("Group(Collection<Figure>)")
	final void testGroupCollectionOfFigure()
	{
		String testName = new String(testBasename + "(Collection<Figure>)");
		System.out.println(testName);

		testGroup = new Group(figuresCollection);

		assertNotNull(testGroup, testName + " is null");
		assertEquals(figuresCenter,
		             testGroup.getCenter(),
		             testName + " failed with wrong center");
		assertEquals(figuresBBCenter,
		             testGroup.getBoundingBoxCenter(),
		             testName + " failed with wrong bounding box center");
		assertEquals(figures.length, testGroup.size(), testName + " failed with wrong size");
		assertEquals(figuresArea, testGroup.area(), tolerance, testName + " failed with wrong area");
		assertEquals(figuresWidth, testGroup.width(), tolerance, testName + " failed with wrong width");
		assertEquals(figuresHeight, testGroup.height(), tolerance, testName + " failed with wrong height");
	}

	/**
	 * Test method for {@link figures.Group#Group(figures.Group)}.
	 */
	@Test
	@DisplayName("Group(Group)")
	final void testGroupGroup()
	{
		String testName = new String(testBasename + "(Group)");
		System.out.println(testName);
		Group otherGroup = new Group();
		testGroup = new Group(otherGroup);

		assertNotNull(testGroup, testName + " is null");
		assertNotSame(otherGroup, testGroup, testName + " is same");
		assertEquals(otherGroup, testGroup, testName + " are not equal");

		otherGroup = new Group(allFiguresCollection);
		testGroup = new Group(otherGroup);

		assertNotNull(testGroup, testName + " is null");
		assertNotSame(otherGroup, testGroup, testName + " is same");
		assertEquals(otherGroup, testGroup, testName + " are not equal");
	}

	/**
	 * Test method for {@link figures.Group#add(figures.Figure)}.
	 */
	@Test
	@DisplayName("add(Figure) : boolean")
	final void testAdd()
	{
		String testName = new String(testBasename + ".add(Figure)");
		System.out.println(testName);
		testGroup = new Group();

		assertNotNull(testGroup, testName + " is null");

		assertThrows(NullPointerException.class,
		             () -> testGroup.add(null),
		             testName + " added null figure");

		for (int i = -1; i < figures.length; i++)
		{
			if (i >= 0)
			{
				testGroup.add(figures[i]);
			}
			assertEquals(progressiveCenters[i+1],
			             testGroup.getCenter(),
			             testName + " wrong center");
			assertEquals(progressiveBBCenters[i+1],
			             testGroup.getBoundingBoxCenter(),
			             testName + " wrong Bounding Box center");
			assertEquals(progressiveAreas[i+1],
			             testGroup.area(),
			             tolerance,
			             testName + " wrong area");
			assertEquals(progressiveWidths[i+1],
			             testGroup.width(),
			             tolerance,
			             testName + " wrong width");
			assertEquals(progressiveHeights[i+1],
			             testGroup.height(),
			             tolerance,
			             testName + " wrong height");
		}
	}

	/**
	 * Test method for {@link figures.Group#addAll(java.util.Collection)}.
	 */
	@Test
	@DisplayName("addAll(Collection<? extends Figure>) : boolean")
	final void testAddAll()
	{
		String testName = new String(testBasename + ".addAll(Collection<? extends Figure>)");
		System.out.println(testName);
		testGroup = new Group();

		assertNotNull(testGroup, testName + " is null");

		assertThrows(NullPointerException.class,
		             () -> testGroup.addAll(null),
		             testName + " added null collection");

		Collection<Figure> collectionWithNull = new ArrayList<Figure>();
		collectionWithNull.add(figures[0]);
		collectionWithNull.add(null);
		assertThrows(NullPointerException.class,
		             () -> testGroup.addAll(collectionWithNull),
		             testName + " added collection with null element");

		testGroup = new Group();
		int[] indexes = new int[] {0, 4};
		for (int i = 0; i < indexes.length; i++)
		{
			assertEquals(progressiveCenters[indexes[i]],
			             testGroup.getCenter(),
			             testName + " wrong center");
			assertEquals(progressiveBBCenters[indexes[i]],
			             testGroup.getBoundingBoxCenter(),
			             testName + " wrong Bounding Box center");
			assertEquals(progressiveAreas[indexes[i]],
			             testGroup.area(),
			             tolerance,
			             testName + " wrong area");
			assertEquals(progressiveWidths[indexes[i]],
			             testGroup.width(),
			             tolerance,
			             testName + " wrong width");
			assertEquals(progressiveHeights[indexes[i]],
			             testGroup.height(),
			             tolerance,
			             testName + " wrong height");
			if (i == 0)
			{
				testGroup.addAll(figuresCollection);
			}
		}

		assertFalse(testGroup.addAll(figuresCollection),
		            testName + " added same collection twice");
	}

	/**
	 * Test method for {@link figures.Group#clear()}.
	 */
	@Test
	@DisplayName("clear()")
	final void testClear()
	{
		String testName = new String(testBasename + ".clear()");
		System.out.println(testName);
		testGroup = new Group(figuresCollection);

		assertNotNull(testGroup, testName + " is null");
		int[] indexes = new int[] {4, 0};
		for (int i = 0; i < indexes.length; i++)
		{
			assertEquals(progressiveCenters[indexes[i]],
			             testGroup.getCenter(),
			             testName + " wrong center");
			assertEquals(progressiveBBCenters[indexes[i]],
			             testGroup.getBoundingBoxCenter(),
			             testName + " wrong Bounding Box center");
			assertEquals(progressiveAreas[indexes[i]],
			             testGroup.area(),
			             tolerance,
			             testName + " wrong area");
			assertEquals(progressiveWidths[indexes[i]],
			             testGroup.width(),
			             tolerance,
			             testName + " wrong width");
			assertEquals(progressiveHeights[indexes[i]],
			             testGroup.height(),
			             tolerance,
			             testName + " wrong height");
			if (i == 0)
			{
				testGroup.clear();
			}
		}
	}

	/**
	 * Test method for {@link figures.Group#contains(figures.Figure)}.
	 */
	@Test
	@DisplayName("contains(Figure) : boolean")
	final void testContainsFigure()
	{
		String testName = new String(testBasename + ".contains(Figure)");
		System.out.println(testName);

		testGroup = new Group();
		// Contains itself
		assertTrue(testGroup.contains(testGroup),
		           testName + " does not contain itself");
		// Empty Group Contains no figures
		for (int i = 0; i < figures.length; i++)
		{
			assertFalse(testGroup.contains(figures[i]),
			            testName + " contained " + figures[i]);
		}

		testGroup = new Group(figuresCollection);
		// Contains itself
		assertTrue(testGroup.contains(testGroup),
		           testName + " does not contain itself");
		// Full Group Contains all figures
		for (int i = 0; i < figures.length; i++)
		{
			assertTrue(testGroup.contains(figures[i]),
			           testName + " did not contain " + figures[i]);
		}

		// Full Group does not contains any of antiFigures
		for (int i = 0; i < antiFigures.length; i++)
		{
			assertFalse(testGroup.contains(antiFigures[i]),
			            testName + " contained " + antiFigures[i]);
		}
	}

	/**
	 * Test method for {@link figures.Group#contains(java.lang.Object)}.
	 */
	@SuppressWarnings("unlikely-arg-type") // on purpose
	@Test
	@DisplayName("contains(Object o) : boolean")
	final void testContainsObject()
	{
		String testName = new String(testBasename + ".contains(Object)");
		System.out.println(testName);

		testGroup = new Group(figuresCollection);

		assertThrows(NullPointerException.class,
		             () -> testGroup.contains((Object)null),
		             testName + " NullPointerException did not occur");

		// Contains itself
		Object o = testGroup;
		assertTrue(testGroup.contains(o),
		           testName + " does not contain itself");

		// Does not contains foreign objects
		assertFalse(testGroup.contains(new String("group")),
		            testName + " contains String");

		// Contains all figures
		for (Figure f : figuresCollection)
		{
			Object of = f;
			assertTrue(testGroup.contains(of),
			           testName + " does not contains figure " + f);
		}

		// Does not contain any of antiFigures
		for (Figure f : antiFiguresCollection)
		{
			Object figureObject = f;
			assertFalse(testGroup.contains(figureObject),
			           testName + " contains figure " + f);
		}

		// Contains a point inside all figures
		assertTrue(testGroup.contains(insidePoint),
		           testName + " does not contain inside point");

		// Does not contain a point outside all figures
		assertFalse(testGroup.contains(outsidePoint),
		           testName + " contains outside point");

		// Contains points inside a single figure in the Group
		for (Figure f : figuresCollection)
		{
			assertTrue(testGroup.contains(f.getCenter()),
			           testName + " does not contain a figure center");
		}
	}

	/**
	 * Test method for {@link figures.Group#containsAll(java.util.Collection)}.
	 */
	@Test
	@DisplayName("containsAll(Collection<?>) : boolean")
	final void testContainsAll()
	{
		String testName = new String(testBasename + ".containsAll(Collection<?>)");
		System.out.println(testName);

		Collection<Figure> otherFiguresCollection = new ArrayList<Figure>();
		shuffleCollection(figuresCollection, otherFiguresCollection);

		testGroup = new Group(otherFiguresCollection);

		assertThrows(NullPointerException.class,
		             () -> testGroup.containsAll(null),
		             testName + " NullPointerException did not occur with null collection");

		assertTrue(testGroup.containsAll(figuresCollection),
		           testName + " does not contain all figures");
		assertFalse(testGroup.containsAll(allFiguresCollection),
		            testName + " contains all figures + a Group");

//		otherFiguresCollection.add(null);
//		assertTrue(otherFiguresCollection.contains(null));
//		assertThrows(NullPointerException.class,
//		             () -> testGroup.containsAll(otherFiguresCollection),
//		             testName + " NullPointerException did not occur with collection with null");

		testGroup = new Group(allFiguresCollection);

		assertTrue(testGroup.containsAll(figuresCollection),
		           testName + " does not contain all figures");
		assertTrue(testGroup.containsAll(allFiguresCollection),
		           testName + " does not contain all figures + a Group");
		assertFalse(testGroup.containsAll(antiFiguresCollection),
		            testName + " contained all anti figures");

		Collection<Figure> combinedCollection =
		    Stream.of(figuresCollection, antiFiguresCollection)
		          .flatMap(Collection::stream)
		          .collect(Collectors.toList());

		assertFalse(testGroup.containsAll(combinedCollection),
		            testName + " contained all combined figures");
	}

	/**
	 * Test method for {@link figures.Group#isEmpty()}.
	 */
	@Test
	@DisplayName("isEmpty() : boolean")
	final void testIsEmpty()
	{
		String testName = new String(testBasename + ".isEmpty()");
		System.out.println(testName);

		testGroup = new Group();
		assertTrue(testGroup.isEmpty(), testName + " empty Group is not empty");

		testGroup = new Group(figuresCollection);
		assertFalse(testGroup.isEmpty(), testName + " full Group is empty");
	}

	/**
	 * Test method for {@link figures.Group#iterator()}.
	 */
	@Test
	@DisplayName("iterator() : Iterator<Figure>")
	final void testIterator()
	{
		String testName = new String(testBasename + ".iterator()");
		System.out.println(testName);

		testGroup = new Group();
		Iterator<Figure> it = testGroup.iterator();

		assertNotNull(it, testName + " iterator is null");
		assertFalse(it.hasNext(), testName + " iterator on empty group has next");
		try
		{
			/*
			 * Note : we can't use assertThrows with a lambda here since
			 * it.next() changes "it" which is not allowed within lambda expressions
			 */
			it.next();
			fail(testName + " NoSuchElementException wasn't thrown");
		}
		catch (NoSuchElementException e)
		{
			// Nothing, it was expected;
		}

		testGroup = new Group(allFiguresCollection);
		it = testGroup.iterator();

		assertNotNull(it, testName + " iterator is null");
		assertTrue(it.hasNext(), testName + " iterator has no next");
		for (int i = 0; i < allFigures.length; i++)
		{
			Figure f = it.next();
			assertSame(allFigures[i], f, testName + " are not the same");
			assertEquals(allFigures[i], f, testName + " are not equals");
		}
		assertFalse(it.hasNext(), testName + " has more next");
	}

	/**
	 * Test method for {@link figures.Group#remove(java.lang.Object)}.
	 */
	@Test
	@DisplayName("remove(Object o) : boolean")
	final void testRemove()
	{
		String testName = new String(testBasename + ".remove(Object o)");
		System.out.println(testName);

		testGroup = new Group();

		// Retrait d'un élément null sur Group vide
		assertFalse(testGroup.remove(null),
		            testName + " removed null elt on empty Group");

		// Retrait d'un élément non null sur Group vide
		assertFalse(testGroup.remove(figures[0]),
		            testName + " removed elt on empty Group");

		testGroup = new Group(figuresCollection);

		// Retrait d'un élément null sur Group rempli
		assertFalse(testGroup.remove(null),
		            testName + " removed null elt on full Group");

		// Retrait d'un élément non null non présent sur Group rempli
		assertFalse(testGroup.remove(allFigures[allFigures.length - 1]),
		            testName + " removed non present elt on full Group");

		testGroup = new Group(allFiguresCollection);

		// Retrait d'un élément non null présent sur Group rempli
		Integer[] randomIndexes = shuffleArray(generateSequence(0, allFigures.length));
		for (int i = 0; i < randomIndexes.length; i++)
		{
			int ri = randomIndexes[i].intValue();
//			System.out.println("randomIndex[" + i + "] = " + ri);
			assertTrue(testGroup.remove(allFigures[ri]),
			           testName + " failed to remove " + allFigures[ri]
			               + " from Group");
		}

		testGroup = new Group(allFiguresCollection);
		// Retrait d'éléments non présents
		for (int i = 0; i < antiFigures.length; i++)
		{
			assertFalse(testGroup.remove(antiFigures[i]),
			            testName + " removed figure " + antiFigures[i]
			                + " from Group");
		}
	}

	/**
	 * Test method for {@link figures.Group#removeAll(java.util.Collection)}.
	 */
	@Test
	@DisplayName("removeAll(Collection<?> c) : boolean")
	final void testRemoveAll()
	{
		String testName = new String(testBasename + ".removeAll(Collection<?> c)");
		System.out.println(testName);

		testGroup = new Group();

		// retrait collection nulle sur groupe vide (--> NullPointerException)
		assertThrows(NullPointerException.class,
		             () -> testGroup.removeAll(null),
		             testName + " didn't throw NullPointerException");

		// retrait collection sur groupe vide
		assertFalse(testGroup.removeAll(figuresCollection),
		            testName +  " removed collection from empty Group");

		Collection<Figure> otherFiguresCollection = new ArrayList<Figure>();
		shuffleCollection(figuresCollection, otherFiguresCollection);
		testGroup = new Group(otherFiguresCollection);

		// retrait collection nulle sur groupe rempli
		assertThrows(NullPointerException.class,
		             () -> testGroup.removeAll(null),
		             testName + " didn't throw NullPointerException");

		// retrait collection identique sur groupe rempli
		assertTrue(testGroup.removeAll(figuresCollection),
		           testName + " didn't remove same collection from full Group");

		testGroup = new Group(otherFiguresCollection);
		// retrait collection identique mais + grande sur groupe rempli
		assertTrue(testGroup.removeAll(allFiguresCollection),
		            testName +  " didn't remove greater collection from full Group");

		testGroup = new Group(figuresCollection);
		// retrait collection identique mais + petite sur groupe rempli
		Collection<Figure> smallerFigures = new ArrayList<Figure>(figuresCollection);
		smallerFigures.remove(figures[figures.length - 1]);
		assertTrue(testGroup.removeAll(smallerFigures),
		           testName + "didn't remove smaller collection from full Group");

		testGroup = new Group(figuresCollection);
		// retrait collection différente
		assertFalse(testGroup.removeAll(antiFiguresCollection),
		            testName + " remove antiFigures from full Group");
	}

	/**
	 * Test method for {@link figures.Group#retainAll(java.util.Collection)}.
	 */
	@Test
	@DisplayName("retainAll(Collection<?> c) : boolean")
	final void testRetainAll()
	{
		String testName = new String(testBasename + ".retainAll(Collection<?> c)");
		System.out.println(testName);

		testGroup = new Group();

		// retain collection nulle sur Group Vide (--> NullPointerException)
		assertThrows(NullPointerException.class, () -> testGroup.retainAll(null),
		             testName + " NullPointerException wasn't thrown");

		// retain other collection sur Group vide
		assertFalse(testGroup.retainAll(figuresCollection),
		            testName + " retained other collection on empty Group");

		testGroup = new Group(figuresCollection);

		// retain null collection sur Group rempli (--> NullPointerException)
		assertThrows(NullPointerException.class, () -> testGroup.retainAll(null),
		             testName + " NullPointerException wasn't thrown");

		// retain same collection sur Group rempli
		assertFalse(testGroup.retainAll(figuresCollection),
		            testName + " retained same collection modified Group");

		// retain bigger collection
		assertFalse(testGroup.retainAll(allFiguresCollection),
		            testName + " retained bigger collection modified Group");

		testGroup = new Group(allFiguresCollection);
		// retain smaller collection
		assertTrue(testGroup.retainAll(figuresCollection),
		           testName + " retained smaller collection didn't modify Group");

		// retain other collection
		assertTrue(testGroup.retainAll(antiFiguresCollection),
		           testName + " retained other collection didn't modify Group");
	}

	/**
	 * Test method for {@link figures.Group#size()}.
	 */
	@Test
	@DisplayName("size() : int")
	final void testSize()
	{
		String testName = new String(testBasename + ".size()");
		System.out.println(testName);

		testGroup = new Group();
		assertEquals(0, testGroup.size(), testName +  " size is not 0");

		testGroup = new Group(figuresCollection);
		assertEquals(figuresCollection.size(),
		             testGroup.size(),
		             testName + " size is not " + figuresCollection.size());

		Collection<Figure> otherCollection = new ArrayList<Figure>();
		shuffleCollection(figuresCollection, otherCollection);

		int remainingElements = figuresCollection.size();
		for (Figure f : otherCollection)
		{
			testGroup.remove(f);
			assertEquals(--remainingElements,
			             testGroup.size(),
			             testName + " size is not " + remainingElements);
		}
	}

	/**
	 * Test method for {@link figures.Group#toArray()}.
	 */
	@Test
	@DisplayName("toArray() : Object[]")
	final void testToArray()
	{
		String testName = new String(testBasename + ".toArray()");
		System.out.println(testName);
		Object[] result;

		testGroup = new Group();
		result = testGroup.toArray();
		assertEquals(0,
		             result.length,
		             testName + " resulting array is not empty");

		testGroup = new Group(figuresCollection);
		result = testGroup.toArray();
		assertEquals(figures.length,
		             result.length,
		             testName + " wrong result array size " + result.length);
		for (int i = 0; i < figures.length; i++)
		{
			assertEquals(figures[i], result[i], testName + " result[" + i + "]");
		}
	}

	/**
	 * Test method for {@link figures.Group#toArray(Object[])}.
	 */
	@Test
	@DisplayName("<T> toArray(T[] a) : T[]")
	final void testToArrayTArray()
	{
		String testName = new String(testBasename + ".toArray(T[] a)");
		System.out.println(testName);
		Figure[] result;

		testGroup = new Group();
		result = testGroup.toArray(new Figure[0]);
		assertEquals(0,
		             result.length,
		             testName + " resulting array is not empty");

		testGroup = new Group(figuresCollection);
		result = testGroup.toArray(new Figure[0]);
		assertEquals(figures.length,
		             result.length,
		             testName + " wrong result array size " + result.length);
		for (int i = 0; i < figures.length; i++)
		{
			assertEquals(figures[i], result[i], testName + " result[" + i + "]");
		}
	}
}
