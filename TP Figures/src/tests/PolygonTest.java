/**
 *
 */
package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import figures.Polygon;
import points.Point2D;

/**
 * Classe de test pour les {@link Polygon}
 * @implNote on ne teste que ce qui n'est pas déjà testé dans {@link FigureTest}
 * @author davidroussel
 */
public class PolygonTest
{
	/**
	 * Base name pour tous les tests
	 */
	private final static String testBasename = new String(Polygon.class.getSimpleName());

	/**
	 * Les points à utiliser pour construire les figures
	 */
	private static final Point2D[] points = new Point2D[] {
		new Point2D(5.0, 1.0),
		new Point2D(8.0, 2.0),
		new Point2D(7.0, 5.0),
		new Point2D(2.0, 4.0),
		new Point2D(2.0, 3.0)
	};

	/**
	 * Les mêmes {@link #points} à l'envers
	 */
	private static Point2D[] reversePoints = new Point2D[points.length];

	/**
	 * Les mêmes {@link #points} dans un ordre aléatoire
	 */
	private static Point2D[] randPoints  = new Point2D[points.length];

	/**
	 * Progression du centre du polygon en fonction de l'ajout de nouveaux
	 * points dans le polygone
	 */
	private final static Point2D[] progressiveCenters = new Point2D[] {
		points[0], // 1 pts
		new Point2D(6.5, 1.5), // 2 pts
		new Point2D(6.6666667, 2.6666667), // 3 pts
		new Point2D(5.3809524, 3.0952381), // 4 pts
		new Point2D(5.1505377, 3.05376344) // 5 pts
	};

	/**
	 * Progression des aires en fonction de l'ajout de nouveau points
	 */
	private final static double[] progressiveAreas = new double[] {
		0.0, 0.0, 5.0, 14.0, 15.5
	};

	/**
	 * Progression des largeurs en fonction de l'ajout de nouveau points
	 */
	private final static double[] progressiveWidth = new double[] {
		0.0, 3.0, 3.0, 6.0, 6.0
	};

	/**
	 * Progression des hauteurs en fonction de l'ajout de nouveau points
	 */
	private final static double[] progressiveHeight = new double[] {
		0.0, 1.0, 4.0, 4.0, 4.0
	};

	/**
	 * The class under test
	 */
	private static final Class<Polygon> testClass = Polygon.class;

	/**
	 * Le Polygon à tester
	 */
	private Polygon testPolygon;

	/**
	 * Collection de points d'après {@link #points}
	 */
	private final static Collection<Point2D> pointsCollection = Arrays.asList(points);

	/**
	 * Inversion de l'ordre des éléments dans un tableau
	 * @param source le tableau à inverser
	 * @param dest le tableau recevant le tableau source inversé
	 */
	private static final <T> void reverseArray(T[] source, T[] dest)
	{
		assert(dest.length == source.length);
		int j = source.length - 1;

		for (int i = 0; i < source.length; i++,j--)
		{
			dest[j] = source[i];
		}
	}

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
	 * Mise en place avant l'ensemble des tests
	 * @throws java.lang.Exception quand les poules auront des dents
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
		reverseArray(points, reversePoints);
		for (int i = 0; i < points.length; i++)
		{
			randPoints[i] = points[i];
		}
		shuffleArray(randPoints);

		/*
		 * Expected constructors arguments types
		 */
		Class<?>[][] expectedConstructorsArguments = {
			{},					// Polygon()
			{Point2D[].class},	// Polygon(Point2D[])
			{Polygon.class},	// Polygon(Polygon)
			{Collection.class},	// Point2D(Collection<Point2D> c)
		};

		/*
		 * Expected methods in the test class (at least the ones under test):
		 * 	- Methods names
		 * 	- Methods arguments types
		 * 	- Methods return types
		 * 	- Modifiers of each method
		 */
		String[] expectedMethodsNames = {
			"add",
			"add",
			"remove",
			"remove",
			"getCenterPoint",
		};
		Class<?>[][] expectedMethodsArguments = {
			{double.class, double.class},	// add
			{Point2D.class},				// add
			{Point2D.class},				// remove
			{},								// remove
			{},								// getCenterPoint
		};
		Class<?>[] expectedMethodsReturns = {
			boolean.class,	// add
			boolean.class,	// add
			boolean.class,	// remove
			boolean.class,	// remove
			Point2D.class,	// getCenterPoint
		};

		String testName = "Polygon Content Test: ";

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
		testPolygon = null;
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
	 * Test method for {@link figures.Polygon#Polygon()}.
	 * |
	 * |			i	x	y
	 * | .--.		0 	2	1
	 * | |   \		1	4	1
	 * | |    .		2	5	3
	 * | |   /		3	4	5
	 * | .--.		4	2	5
	 * .-------
	 */
	@Test
	@DisplayName("Polygon()")
	final void testPolygon()
	{
		String testName = new String(testBasename + "()");
		System.out.println(testName);

		ContentChecker.assertConstructor(testName, testClass, null);

		testPolygon = new Polygon();

		assertNotNull(testPolygon, testName);
		assertEquals(new Point2D(3.2666667, 3.0),
		             testPolygon.getCenter(),
		             testName + " failed with wrong center");
		assertEquals(10.0, testPolygon.area(), testName + " failed with wrong area");
		assertEquals(3.0, testPolygon.width(), testName + " failed with wrong width");
		assertEquals(4.0, testPolygon.height(), testName + " failed with wrong height");
	}

	/**
	 * Test method for {@link figures.Polygon#Polygon(points.Point2D[])}.
	 */
	@Test
	@DisplayName("Polygon(Point2D ... pts)")
	final void testPolygonPoint2DArray()
	{
		String testName = new String(testBasename + "(Point2D ... pts)");
		System.out.println(testName);

		for (int i = 0; i < points.length; i++)
		{
			switch (i)
			{
				case 0:
					testPolygon = new Polygon(points[0]);
					break;
				case 1:
					testPolygon = new Polygon(points[0],
					                          points[1]);
					break;
				case 2:
					testPolygon = new Polygon(points[0],
					                          points[1],
					                          points[2]);
					break;
				case 3:
					testPolygon = new Polygon(points[0],
					                          points[1],
					                          points[2],
					                          points[3]);
					break;
				case 4:
					testPolygon = new Polygon(points[0],
					                          points[1],
					                          points[2],
					                          points[3],
					                          points[4]);
					break;
				default:
					break;
			}
			if (testPolygon != null)
			{
				assertNotNull(testPolygon, testName);
				assertEquals(progressiveCenters[i],
				             testPolygon.getCenter(),
				             testName + "[" + i + "] failed with wrong center");
				assertEquals(progressiveAreas[i], testPolygon.area(), testName + "[" + i + "] failed with wrong area");
				assertEquals(progressiveWidth[i], testPolygon.width(), testName + "[" + i + "] failed with wrong width");
				assertEquals(progressiveHeight[i], testPolygon.height(), testName + "[" + i + "] failed with wrong height");
			}
			else
			{
				fail(testName + " null instance");
			}
		}
	}

	/**
	 * Test method for {@link figures.Polygon#Polygon(figures.Polygon)}.
	 */
	@Test
	@DisplayName("Polygon(Polygon)")
	final void testPolygonPolygon()
	{
		String testName = new String(testBasename + "(Polygon)");
		System.out.println(testName);

		Polygon otherPolygon = new Polygon();
		testPolygon = new Polygon(otherPolygon);

		assertNotNull(testPolygon, testName + " is null");
		assertNotSame(otherPolygon, testPolygon, testName + " is same");
		assertEquals(otherPolygon, testPolygon, testName + " are not equal");

		otherPolygon = new Polygon(points[0],
		                           points[1],
		                           points[2],
		                           points[3],
		                           points[4]);
		testPolygon = new Polygon(otherPolygon);

		assertNotNull(testPolygon, testName + " is null");
		assertNotSame(otherPolygon, testPolygon, testName + " is same");
		assertEquals(otherPolygon, testPolygon, testName + " are not equal");
	}

	/**
	 * Test method for {@link figures.Polygon#Polygon(java.util.Collection)}.
	 */
	@Test
	@DisplayName("Polygon(Collection<Point2D>)")
	final void testPolygonCollectionOfPoint2D()
	{
		String testName = new String(testBasename + "(Collection<Point2D>)");
		System.out.println(testName);
		testPolygon = new Polygon(pointsCollection);
		assertNotNull(testPolygon, testName);
		assertEquals(progressiveCenters[4],
		             testPolygon.getCenter(),
		             testName + " failed with wrong center");
		assertEquals(progressiveAreas[4], testPolygon.area(), testName + " failed with wrong area");
		assertEquals(progressiveWidth[4], testPolygon.width(), testName + " failed with wrong width");
		assertEquals(progressiveHeight[4], testPolygon.height(), testName + " failed with wrong height");

		Polygon otherPolygon = new Polygon(Arrays.asList(reversePoints));

		assertNotNull(otherPolygon, testName);
		assertEquals(testPolygon, otherPolygon, testName + " similar instances are not");
	}

	/**
	 * Test method for {@link figures.Polygon#add(double, double)}.
	 */
	@Test
	@DisplayName("add(double, double)")
	final void testAddDoubleDouble()
	{
		String testName = new String(testBasename + ".add(double, double)");
		System.out.println(testName);
		testPolygon = new Polygon(points[0]);
		assertNotNull(testPolygon, testName);

		for (int i = 1; i < points.length; i++)
		{
			testPolygon.add(points[i].getX(), points[i].getY());
			assertEquals(progressiveCenters[i],
			             testPolygon.getCenter(),
			             testName + " failed with wrong center");
			assertEquals(progressiveAreas[i], testPolygon.area(), testName + " failed with wrong area");
			assertEquals(progressiveWidth[i], testPolygon.width(), testName + " failed with wrong width");
			assertEquals(progressiveHeight[i], testPolygon.height(), testName + " failed with wrong height");
		}

		// Ajout de points déjà présents dans le polygone
		for (int i = 0; i < reversePoints.length; i++)
		{
			assertFalse(testPolygon.add(reversePoints[i].getX(),
			                            reversePoints[i].getY()),
			            testName + " added double point " + reversePoints[i]);
		}
	}

	/**
	 * Test method for {@link figures.Polygon#add(points.Point2D)}.
	 */
	@Test
	@DisplayName("add(Point2D)")
	final void testAddPoint2D()
	{
		String testName = new String(testBasename + ".add(Point2D)");
		System.out.println(testName);
		testPolygon = new Polygon(points[0]);
		assertNotNull(testPolygon, testName);

		assertThrows(NullPointerException.class,
		             () -> testPolygon.add(null),
		             testName + " added null point");

		for (int i = 1; i < points.length; i++)
		{
			assertTrue(testPolygon.add(points[i]),
			           testName + " failed to add" +  points[i]);
			assertFalse(testPolygon.add(points[i]),
			           testName + " added" +  points[i] + " twice");

			assertEquals(progressiveCenters[i],
			             testPolygon.getCenter(),
			             testName + " failed with wrong center");
			assertEquals(progressiveAreas[i], testPolygon.area(), testName + " failed with wrong area");
			assertEquals(progressiveWidth[i], testPolygon.width(), testName + " failed with wrong width");
			assertEquals(progressiveHeight[i], testPolygon.height(), testName + " failed with wrong height");
		}

		// Ajout de points déjà présents dans le polygone
		for (int i = 0; i < reversePoints.length; i++)
		{
			assertFalse(testPolygon.add(reversePoints[i]),
			            testName + " added double point " + reversePoints[i]);
		}
	}

	/**
	 * Test method for {@link figures.Polygon#remove(points.Point2D)}.
	 */
	@Test
	@DisplayName("remove(Point2D)")
	final void testRemovePoint2D()
	{
		String testName = new String(testBasename + ".remove(Point2D)");
		System.out.println(testName);
		testPolygon = new Polygon(pointsCollection);
		assertNotNull(testPolygon, testName);

		for (int i = 0; i < points.length; i++)
		{
			Point2D center = testPolygon.getCenter();
			if (center != null)
			{
				assertEquals(progressiveCenters[points.length - i - 1],
				             center,
				             testName + "[" + i + "] failed with wrong center");
				assertEquals(progressiveAreas[points.length - i - 1], testPolygon.area(), testName + "[" + i + "] failed with wrong area");
				assertEquals(progressiveWidth[points.length - i - 1], testPolygon.width(), testName + "[" + i + "] failed with wrong width");
				assertEquals(progressiveHeight[points.length - i - 1], testPolygon.height(), testName + "[" + i + "] failed with wrong height");

				assertTrue(testPolygon.remove(reversePoints[i]),
				           testName + " "+ reversePoints[i] + " not removed");
			}
		}

		testPolygon = new Polygon(pointsCollection);
		assertNotNull(testPolygon, testName);
		for (int i = 0; i < points.length; i++)
		{
			assertTrue(testPolygon.contains(reversePoints[i]),
			           testName + " did not contained " + reversePoints[i]);
			testPolygon.remove(reversePoints[i]);
			assertFalse(testPolygon.contains(reversePoints[i]),
			           testName + " contained " + reversePoints[i]);
		}

		// Retrait de points déjà enlevés du le polygone
		for (int i = 0; i < reversePoints.length; i++)
		{
			assertFalse(testPolygon.remove(reversePoints[i]),
			            testName + " removed inexistant point " + reversePoints[i]);
		}

	}

	/**
	 * Test method for {@link figures.Polygon#remove()}.
	 */
	@Test
	@DisplayName("remove()")
	final void testRemove()
	{
		String testName = new String(testBasename + ".remove(Point2D)");
		System.out.println(testName);
		testPolygon = new Polygon(pointsCollection);
		assertNotNull(testPolygon, testName);

		for (int i = 0; i < points.length; i++)
		{
			Point2D center = testPolygon.getCenter();
			if (center != null)
			{
				assertEquals(progressiveCenters[points.length - i - 1],
				             center,
				             testName + "[" + i + "] failed with wrong center");
				assertEquals(progressiveAreas[points.length - i - 1], testPolygon.area(), testName + "[" + i + "] failed with wrong area");
				assertEquals(progressiveWidth[points.length - i - 1], testPolygon.width(), testName + "[" + i + "] failed with wrong width");
				assertEquals(progressiveHeight[points.length - i - 1], testPolygon.height(), testName + "[" + i + "] failed with wrong height");

				assertTrue(testPolygon.remove(),
				           testName + " "+ reversePoints[i] + " not removed");
			}
		}

		testPolygon = new Polygon(pointsCollection);
		assertNotNull(testPolygon, testName);
		for (int i = 0; i < points.length; i++)
		{
			assertTrue(testPolygon.contains(reversePoints[i]),
			           testName + " did not contained " + reversePoints[i]);
			testPolygon.remove();
			assertFalse(testPolygon.contains(reversePoints[i]),
			           testName + " contained " + reversePoints[i]);
		}

		assertFalse(testPolygon.remove(), testName + " remove inexistant point");
	}

	/**
	 * Test method for {@link figures.Polygon#getCenterPoint()}
	 */
	@Test
	final void testGetCenterPoint()
	{
		String testName = new String(testBasename + ".getCenterPoint()");
		System.out.println(testName);
		testPolygon = new Polygon(pointsCollection);
		assertNotNull(testPolygon, testName);

		double sumX = 0.0;
		double sumY = 0.0;
		for (Point2D p : pointsCollection)
		{
			sumX += p.getX();
			sumY += p.getY();
		}
		double nbPoints = pointsCollection.size();
		Point2D expectedCenter = new Point2D(sumX / nbPoints, sumY / nbPoints);
		Point2D providedCenter = testPolygon.getCenterPoint();

		assertTrue(expectedCenter.distance(providedCenter) < Point2D.getEpsilon(),
		           testName + " unexpected distance between expected and provided center point");
		assertEquals(expectedCenter.getX(),
		             providedCenter.getX(),
		             Point2D.getEpsilon(),
		             testName + " unexpected center X");
		assertEquals(expectedCenter.getY(),
		             providedCenter.getY(),
		             Point2D.getEpsilon(),
		             testName + " unexpected center Y");

	}
}
