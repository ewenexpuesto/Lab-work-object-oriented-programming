package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import points.Point2D;

/**
 * Class de test de la classe {@link Point2D}
 *
 * @author David
 */
@DisplayName("Point2D")
public class Point2DTest
{
	/**
	 * Le point2D à tester
	 */
	private Point2D point;

	/**
	 * Liste de points
	 */
	private ArrayList<Point2D> points;

	/**
	 * Étendue max pour les random
	 */
	private static final double maxRandom = 1e9;

	/**
	 * Nombre d'essais pour les tests
	 */
	private static final long nbTrials = 1000;

	/**
	 * Nombre de subdivisions pour les étendues lors des tests
	 */
	private static final int nbSteps = 100;

	/**
	 * Constructeur de la classe de test. Initialise les attributs utilisés dans
	 * les tests
	 */
	public Point2DTest()
	{
		point = null;
		points = new ArrayList<Point2D>();
	}

	/**
	 * Mise en place avant tous les tests
	 * @throws java.lang.Exception quand les poules auront des dents
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception
	{
		/*
		 * Class under test
		 */
		final Class<Point2D> testClass = Point2D.class;

		/*
		 * Expected constructors arguments types
		 */
		Class<?>[][] expectedConstructorsArguments = {
			{double.class, double.class},	// Point2D(double x, double y)
			{},								// Point2D()
			{Point2D.class},				// Point2D(Point2D p)
		};

		/*
		 * Expected methods in the test class (at least the ones under test):
		 * 	- Methods names
		 * 	- Methods arguments types
		 * 	- Methods return types
		 * 	- Modifiers of each method
		 */
		String[] expectedMethodsNames = {
			"getX",
			"getY",
			"setX",
			"setY",
			"getEpsilon",
			"getNbPoints",
			"toString",
			"move",
			"distance",
			"distance",
			"equals"
		};
		Class<?>[][] expectedMethodsArguments = {
			{},								// getX
			{},								// getY
			{double.class},					// setX
			{double.class},					// setY
			{},								// getEpsilon
			{},								// getNbPoints
			{},								// toString
			{double.class, double.class},	// move
			{Point2D.class, Point2D.class},	// distance
			{Point2D.class},				// distance
			{Object.class}					// equals
		};
		Class<?>[] expectedMethodsReturns = {
			double.class,	// getX
			double.class,	// getY
			void.class,		// setX
			void.class,		// setY
			double.class,	// getEpsilon
			int.class,		// getNbPoint
			String.class,	// toString
			Point2D.class,	// move
			double.class,	// distance
			double.class,	// distance
			boolean.class	// equals
		};
		int[] expectedMethodModifiers = {
			Modifier.PUBLIC,	// getX
			Modifier.PUBLIC,	// getY
			Modifier.PUBLIC,	// setX
			Modifier.PUBLIC,	// setY
			Modifier.PUBLIC | Modifier.STATIC,	// getEpsilon
			Modifier.PUBLIC | Modifier.STATIC,	// getNbPoints
			Modifier.PUBLIC,	// toString
			Modifier.PUBLIC,	// move
			Modifier.PUBLIC | Modifier.STATIC,	// distance
			Modifier.PUBLIC,	// distance
			Modifier.PUBLIC,	// equals
		};

		String testName = "Point2D Content Test: ";

		boolean checked =
		    ContentChecker.assertContent(testName,
		                                 testClass,
		                                 expectedConstructorsArguments,
		                                 expectedMethodsNames,
		                                 expectedMethodsArguments,
		                                 expectedMethodsReturns,
		                                 expectedMethodModifiers);
		assertTrue(checked, testName + " content didn't check");
	}

	/**
	 * Nettoyage après tous les tests
	 * @throws java.lang.Exception quand les poules auront des dents
	 */
	@AfterAll
	public static void tearDownAfterClass() throws Exception
	{
		// Rien
	}

	/**
	 * Mise en place avant chaque test
	 * @throws java.lang.Exception quand les poules auront des dents
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		// rien
	}

	/**
	 * Nettoyage après chaque test
	 * @throws java.lang.Exception quand les poules auront des dents
	 */
	@AfterEach
	public void tearDown() throws Exception
	{
		point = null;
		System.gc();
	}

	/**
	 * Assertion de la valeur de x du point "point"
	 * @param message le message associé à l'assertion
	 * @param value la valeur attendue
	 * @param tolerance la tolérance de la valeur
	 */
	private void assertX(String message, double value, double tolerance)
	{
		if (tolerance == 0.0)
		{
			assertEquals(value, point.getX(), message);
		}
		else
		{
			assertEquals(value, point.getX(), tolerance, message);
		}
	}

	/**
	 * Assertion de la valeur de y du point "point"
	 * @param message le message associé à l'assertion
	 * @param value la valeur attendue
	 * @param tolerance la tolérance de la valeur
	 */
	private void assertY(String message, double value, double tolerance)
	{
		if (tolerance == 0.0)
		{
			assertEquals(value, point.getY(), message);
		}
		else
		{
			assertEquals(value, point.getY(), tolerance, message);
		}
	}

	/**
	 * Génère un nombre aléatoire compris entre [0...maxValue[
	 * @param maxValue la valeur max du nombre aléatoire
	 * @return un nombre aléatoire compris entre [0...maxValue[
	 */
	private double randomNumber(double maxValue)
	{
		return Math.random() * maxValue;
	}

	/**
	 * Génère un nombre aléatoire compris entre [-range...range[
	 * @param range l'étendue du nombre aléatoire généré
	 * @return un nombre aléatoire compris entre [-range...range[
	 */
	private double randomRange(double range)
	{
		return (Math.random() - 0.5) * 2.0 * range;
	}

	/**
	 * Test method for {@link points.Point2D#Point2D()}.
	 */
	@Test
	@DisplayName("Point2D()")
	public void testPoint2D()
	{
		String testName = new String("Point2D()");
		System.out.println(testName);

		point = new Point2D();

		assertNotNull(point, testName + " instance");
		assertX(testName + ".getX() == 0.0", 0.0, 0.0);
		assertY(testName + ".getY() == 0.0", 0.0, 0.0);
		assertTrue(Point2D.getNbPoints() > 0, testName + ".getNbPoints()");
	}

	/**
	 * Test method for {@link points.Point2D#Point2D(double, double)}.
	 */
	@Test
	@DisplayName("Point2D(double, double)")
	public void testPoint2DDoubleDouble()
	{
		String testName = new String("Point2D(double, double)");
		System.out.println(testName);

		double valueX = 1.0;
		double valueY = Double.NaN;
		point = new Point2D(valueX, valueY);
		assertNotNull(point, testName + " instance");
		assertX(testName + ".getX() == 1.0", valueX, 0.0);
		assertY(testName + ".getY() == NaN", valueY, 0.0);
	}

	/**
	 * Test method for {@link points.Point2D#Point2D(points.Point2D)}.
	 */
	@Test
	@DisplayName("Point2D(Point2D)")
	public void testPoint2DPoint2D()
	{
		String testName = new String("Point2D(Point2D)");
		System.out.println(testName);

		Point2D specimen = new Point2D(randomNumber(maxRandom),
		                               randomNumber(maxRandom));
		assertNotNull(specimen, testName + " instance specimen");

		point = new Point2D(specimen);
		assertNotNull( point, testName + " instance copie");
		assertX(testName + ".getX() == " + specimen.getX(), specimen.getX(),
				0.0);
		assertY(testName + ".getY() == " + specimen.getY(), specimen.getY(),
				0.0);
	}

	/**
	 * Test method for {@link points.Point2D#getX()}.
	 */
	@Test
	@DisplayName("getX() : double")
	public void testGetX()
	{
		String testName = new String("Point2D.getX()");
		System.out.println(testName);

		point = new Point2D(1.0, 0.0);
		assertNotNull( point, testName + "instance");
		assertEquals(1.0, point.getX(), testName + ".getX() == 1.0");
	}

	/**
	 * Test method for {@link points.Point2D#getY()}.
	 */
	@Test
	@DisplayName("getY() : double")
	public void testGetY()
	{
		String testName = new String("Point2D.getY()");
		System.out.println(testName);

		point = new Point2D(0.0, 1.0);
		assertNotNull( point, testName + "instance");
		assertEquals(1.0, point.getY(), testName + ".getY() == 1.0");
	}

	/**
	 * Test method for {@link points.Point2D#setX(double)}.
	 */
	@Test
	@DisplayName("setX(double)")
	public void testSetX()
	{
		String testName = new String("Point2D.setX(double)");
		System.out.println(testName);

		point = new Point2D();
		assertNotNull( point, testName + " instance");
		assertEquals(0.0, point.getX(), testName + ".getX() == 0.0");
		point.setX(2.0);
		assertEquals(2.0, point.getX(), testName + ".getX() == 2.0");
	}

	/**
	 * Test method for {@link points.Point2D#setY(double)}.
	 */
	@Test
	@DisplayName("setY(double)")
	public void testSetY()
	{
		String testName = new String("Point2D.setY(double)");
		System.out.println(testName);

		point = new Point2D();
		assertNotNull( point, testName + " instance");
		assertEquals(0.0, point.getY(), testName + ".getY() == 0.0");
		point.setY(2.0);
		assertEquals(2.0, point.getY(), testName + ".getY() == 2.0");
	}

	/**
	 * Test method for {@link points.Point2D#getEpsilon()}.
	 */
	@Test
	@DisplayName("Point2D.getEpsilon() : double")
	public void testGetEpsilon()
	{
		String testName = new String("Point2D.getEpsilon()");
		System.out.println(testName);

		double result = Point2D.getEpsilon();
		assertEquals(1e-6, result, testName);
	}

	/**
	 * Test method for {@link points.Point2D#getNbPoints()}.
	 */
	@Test
	@DisplayName("Point2D.getNbPoints() : int")
	public void testGetNbPoints()
	{
		String testName = new String("Point2D.getNbPoints()");
		System.out.println(testName);

		point = new Point2D();
		/*
		 * On ne sait pas combien de points sont encore en mémoire : cela dépend
		 * du Garbage Collector. On peut donc juste vérifier qu'il y en a au
		 * moins un
		 */
		assertTrue(Point2D.getNbPoints() >= 1, testName);
	}

	/**
	 * Test method for {@link points.Point2D#toString()}.
	 */
	@Test
	@DisplayName("toString() : String")
	public void testToString()
	{
		String testName = new String("Point2D.toString()");
		System.out.println(testName);

		point = new Point2D(Math.PI, Math.E);
		String expectedString = new String(
				"x = 3.141592653589793 y = 2.718281828459045");
		String result = point.toString();
		assertEquals(expectedString, result, testName);
	}

	/**
	 * Test method for {@link points.Point2D#move(double, double)}.
	 */
	@Test
	@DisplayName("move(double, double) : Point2D")
	public void testMove()
	{
		String testName = new String("Point2D.move(double, double)");
		System.out.println(testName);

		point = new Point2D();
		double origineX = point.getX();
		double origineY = point.getY();
		double deltaX = 5.0;
		double deltaY = 3.0;

		Point2D result = point.move(deltaX, deltaY);
		assertEquals(origineX + deltaX,
		             point.getX(),
		             testName + ".getX() après +delta");
		assertEquals(origineY + deltaY,
		             point.getY(),
		             testName + ".getY() après +delta");
		assertSame(point, result, testName + " not same point");

		result = point.move(-deltaX, -deltaY);
		double tolerance = Point2D.getEpsilon();
		assertSame(point, result, testName + " return == point déplacé");
		assertEquals(origineX,
		             point.getX(),
		             tolerance,
		             testName + ".getX() après -delta");
		assertEquals(origineY,
		             point.getY(),
		             tolerance,
		             testName + ".getY() après -delta");
	}

	/**
	 * Test method for
	 * {@link points.Point2D#distance(points.Point2D, points.Point2D)}.
	 */
	@Test
	@DisplayName("Point2D.distance(Point2D, Point2D)")
	public void testDistancePoint2DPoint2D()
	{
		String testName = new String("Point2D.distance(Point2D, Point2D)");
		System.out.println(testName);

		double radius = randomNumber(maxRandom);
		double angleStep = Math.PI / nbSteps;

		// Distances entre deux points diamétralement opposés le long d'un
		// cercle
		for (double angle = 0.0; angle < (Math.PI * 2.0); angle += angleStep)
		{
			points.clear();
			double x = radius * Math.cos(angle);
			double y = radius * Math.sin(angle);
			points.add(new Point2D(x, y));
			points.add(new Point2D(-x, -y));

			assertEquals(radius * 2.0,
			             Point2D.distance(points.get(0), points.get(1)),
			             Point2D.getEpsilon(),
			             testName + "p0p1[" + String.valueOf(angle) + "]");
			assertEquals(radius * 2.0,
			             Point2D.distance(points.get(1), points.get(0)),
			             Point2D.getEpsilon(),
			             testName + "p1p0[" + String.valueOf(angle) + "]");
		}
	}

	/**
	 * Test method for {@link points.Point2D#distance(points.Point2D)}.
	 */
	@Test
	@DisplayName("distance(Point2D)")
	public void testDistancePoint2D()
	{
		String testName = new String("Point2D.distance(Point2D)");
		System.out.println(testName);

		double origineX = randomRange(maxRandom);
		double origineY = randomRange(maxRandom);
		point = new Point2D(origineX, origineY);
		double radius = randomNumber(maxRandom);
		double angleStep = Math.PI / nbSteps;

		// Distance entre un point fixe (point) et des points le long
		// d'un cercle l'entourant (p)
		for (double angle = 0.0; angle < (Math.PI * 2.0); angle += angleStep)
		{
			Point2D p = new Point2D(origineX + (radius * Math.cos(angle)),
					origineY + (radius * Math.sin(angle)));

			assertEquals(radius,
			             point.distance(p),
			             Point2D.getEpsilon(),
			             testName + "this,p[" + String.valueOf(angle) + "]");
			assertEquals(radius,
			             p.distance(point),
			             Point2D.getEpsilon(),
			             testName + "p,this[" + String.valueOf(angle) + "]");
		}
	}

	/**
	 * Test method for {@link points.Point2D#equals(java.lang.Object)}.
	 */
	@Test
	@DisplayName("equals(Object)")
	public void testEqualsObject()
	{
		String testName = new String("Point2D.equals(Object)");
		System.out.println(testName);

		point = new Point2D(randomRange(maxRandom), randomRange(maxRandom));
		Object o = new Object();

		// Inégalité avec un objet null
		assertFalse(point.equals(null), testName + " sur null");

		// Inégalité avec un objet de nature différente
		assertFalse(point.equals(o), testName + " sur Object");

		// Égalité avec soi même
		Object oPoint = point;
		assertEquals(point, oPoint, testName + " sur this");

		// Égalité avec une copie de soi même
		Point2D otherPoint = new Point2D(point);
		Object op = otherPoint;
		assertEquals(point, op, testName + " sur copie");
		double epsilon = Point2D.getEpsilon();

		// Égalité avec un point déplacé de epsilon au plus
		for (long i = 0; i < nbTrials; i++)
		{
			otherPoint.setX(point.getX());
			otherPoint.setY(point.getY());
			double radius = randomNumber(epsilon);
			double angle = randomNumber(Math.PI * 2.0);
			otherPoint.move(radius * Math.cos(angle),
			                radius * Math.sin(angle));
			double distance = point.distance(otherPoint);

			/*
			 * Attention, à cause des approximations dues aux cos et sin
			 * le déplacement peut être légèrement supérieure à epsilon
			 */
			if (distance < epsilon)
			{
				assertEquals(point,
				             otherPoint,
				             testName + " point déplacé < epsilon [" + distance
				                 + "]");
			}
			else
			{
				assertFalse(point.equals(op),
				            testName + " point déplacé >= epsilon [" + distance
				                + "]");
			}
		}

		// Inégalité avec un point déplacé
		for (long i = 0; i < nbTrials; i++)
		{
			otherPoint.setX(point.getX());
			otherPoint.setY(point.getY());
			otherPoint.move(randomRange(maxRandom), randomRange(maxRandom));
			double distance = point.distance(otherPoint);

			if (distance < epsilon)
			{
				assertEquals(point,
				             otherPoint,
				             testName + " point déplacé proche [" + distance
				                 + "]");
			}
			else
			{
				assertFalse(point.equals(otherPoint),
				            testName + " point déplacé loin [" + distance
				                + "]");
			}
		}
	}
}
