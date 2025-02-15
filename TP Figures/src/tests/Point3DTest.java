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
import points.Point3D;

/**
 * Class de test des Point3D
 *
 * @author davidroussel
 */
@DisplayName("Point3D")
public class Point3DTest
{
	/**
	 * Point 3D à tester
	 */
	private Point3D point;

	/**
	 * Liste de points 3D
	 */
	private ArrayList<Point3D> points;

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
	 * Constructeur de la classe de test.
	 */
	public Point3DTest()
	{
		point = null;
		points = new ArrayList<Point3D>();
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
		final Class<Point3D> testClass = Point3D.class;

		/*
		 * Expected constructors arguments types
		 */
		Class<?>[][] expectedConstructorsArguments = {
			{double.class, double.class, double.class},	// Point3D(double x, double y, double z)
			{},	// Point3D()
			{Point3D.class},	// Point3D(Point3D p)
			{Point2D.class},	// Point3D(Point2D p)
		};

		/*
		 * Expected methods in the test class (at least the ones under test):
		 * 	- Methods names
		 * 	- Methods arguments types
		 * 	- Methods return types
		 */
		String[] expectedMethodsNames = {
			"getZ",
			"setZ",
			"toString",
			"move",
			"distance",
			"distance",
			"equals"
		};
		Class<?>[][] expectedMethodsArguments = {
			{},								// getZ
			{double.class},					// setZ
			{},								// toString
			{double.class, double.class, double.class}, // move
			{Point3D.class, Point3D.class},	// distance
			{Point3D.class},				// distance
			{Object.class},					// equals
		};
		Class<?>[] expectedMethodsReturns = {
			double.class,		// getZ
			void.class,			// setZ
			String.class,		// toString
			Point3D.class,		// move
			double.class,		// distance
			double.class,		// distance
			boolean.class,		// equals
		};
		int[] expectedMethodsModifiers = {
			Modifier.PUBLIC,		// getZ
			Modifier.PUBLIC,		// setZ
			Modifier.PUBLIC,		// toString
			Modifier.PUBLIC,		// move
			Modifier.PUBLIC | Modifier.STATIC,		// distance
			Modifier.PUBLIC,		// distance
			Modifier.PUBLIC,		// equals
		};

		String testName = "Point3D Content Test: ";

		boolean checked =
		    ContentChecker.assertContent(testName,
		                                 testClass,
		                                 expectedConstructorsArguments,
		                                 expectedMethodsNames,
		                                 expectedMethodsArguments,
		                                 expectedMethodsReturns,
		                                 expectedMethodsModifiers);
		assertTrue(checked, testName + " content didn't check");
	}

	/**
	 * Nettoyage après tous les tests
	 * @throws java.lang.Exception quand les poules auront des dents
	 */
	@AfterAll
	public static void tearDownAfterClass() throws Exception
	{
		// rien
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
		points.clear();
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
	 * Assertion de la valeur de z du point "point"
	 * @param message le message associé à l'assertion
	 * @param value la valeur attendue
	 * @param tolerance la tolérance de la valeur
	 */
	private void assertZ(String message, double value, double tolerance)
	{
		if (tolerance == 0.0)
		{
			assertEquals(value, point.getZ(), message);
		}
		else
		{
			assertEquals(value, point.getZ(), tolerance, message);
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
	 * Test method for {@link points.Point3D#Point3D()}.
	 */
	@Test
	@DisplayName("Point3D()")
	public final void testPoint3D()
	{
		String testName = new String("Point3D()");
		System.out.println(testName);

		point = new Point3D();
		assertNotNull(point, testName + " instance");
		assertX(testName + ".getX() == 0.0", 0.0, 0.0);
		assertY(testName + ".getY() == 0.0", 0.0, 0.0);
		assertZ(testName + ".getZ() == 0.0", 0.0, 0.0);
	}

	/**
	 * Test method for {@link points.Point3D#Point3D(double, double, double)}.
	 */
	@Test
	@DisplayName("Point3D(double, double, double)")
	public final void testPoint3DDoubleDoubleDouble()
	{
		String testName = new String("Point3D(double, double, double)");
		System.out.println(testName);

		point = new Point3D(1.0, 2.0, 3.0);
		assertNotNull(point, testName + " instance");
		assertX(testName + ".getX() == 1.0", 1.0, 0.0);
		assertY(testName + ".getY() == 2.0", 2.0, 0.0);
		assertZ(testName + ".getZ() == 3.0", 3.0, 0.0);
	}

	/**
	 * Test method for {@link points.Point3D#Point3D(points.Point2D)}.
	 */
	@Test
	@DisplayName("Point3D(Point2D)")
	public final void testPoint3DPoint2D()
	{
		String testName = new String("Point3D(Point2D)");
		System.out.println(testName);

		Point2D p2 = new Point2D(1.0, 2.0);
		point = new Point3D(p2);
		assertNotNull(point, testName + " instance");
		assertX(testName + ".getX() == 1.0", 1.0, 0.0);
		assertY(testName + ".getY() == 2.0", 2.0, 0.0);
		assertZ(testName + ".getZ() == 0.0", 0.0, 0.0);
	}

	/**
	 * Test method for {@link points.Point3D#Point3D(points.Point3D)}.
	 */
	@Test
	@DisplayName("Point3D(Point3D)")
	public final void testPoint3DPoint3D()
	{
		String testName = new String("Point3D(Point3D)");
		System.out.println(testName);

		Point3D p = new Point3D(1.0, 2.0, 3.0);
		point = new Point3D(p);
		assertNotNull(point, testName + " instance");
		assertX(testName + ".getX() == 1.0", 1.0, 0.0);
		assertY(testName + ".getY() == 2.0", 2.0, 0.0);
		assertZ(testName + ".getZ() == 3.0", 3.0, 0.0);
	}

	/**
	 * Test method for {@link points.Point3D#getZ()}.
	 */
	@Test
	@DisplayName("getZ() : double")
	public final void testGetZ()
	{
		String testName = new String("Point3D.getZ()");
		System.out.println(testName);

		point = new Point3D(0.0, 0.0, 1.0);
		assertNotNull(point, testName + " instance");
		assertEquals(1.0, point.getZ(), testName + " == 1.0");
	}

	/**
	 * Test method for {@link points.Point3D#setZ(double)}.
	 */
	@Test
	@DisplayName("setZ(double)")
	public final void testSetZ()
	{
		String testName = new String("Point3D.setZ(double)");
		System.out.println(testName);

		point = new Point3D();
		assertNotNull(point, testName + " instance");
		assertEquals(0.0, point.getZ(), testName + ".getZ() == 0.0");
		point.setZ(2.0);
		assertEquals(2.0, point.getZ(), testName + ".getZ() == 2.0");
	}

	/**
	 * Test method for {@link points.Point3D#toString()}.
	 */
	@Test
	@DisplayName("toString() : String")
	public final void testToString()
	{
		String testName = new String("Point3D.toString()");
		System.out.println(testName);

		point = new Point3D(Math.PI, Math.E, Math.PI);

		String expected = new String("x = 3.141592653589793 " +
				"y = 2.718281828459045 z = 3.141592653589793");
		String result = point.toString();

		assertEquals(expected, result, testName);
	}

	/**
	 * Test method for {@link points.Point3D#move(double, double, double)}.
	 */
	@Test
	@DisplayName("move(double, double, double) : Point3D")
	public final void testMoveDoubleDoubleDouble()
	{
		String testName = new String("Point3D.move(double, double, double)");
		System.out.println(testName);

		point = new Point3D();
		double origineX = point.getX();
		double origineY = point.getY();
		double origineZ = point.getZ();
		double deltaX = 5.0;
		double deltaY = 3.0;
		double deltaZ = -2.0;

		point.move(deltaX, deltaY, deltaZ);
		assertEquals(point.getX(),
		             0.0,
		             origineX + deltaX,
		             testName + ".getX() == " + origineX + deltaX);
		assertEquals(origineY + deltaY,
		             point.getY(),
		             testName + ".getY() == " + origineY + deltaY);
		assertEquals(origineZ + deltaZ,
		             point.getZ(),
		             testName + ".getZ() == " + origineZ + deltaZ);

		Point3D retour = point.move(-deltaX, -deltaY, -deltaZ);
		assertSame(point, retour, testName + " retour déplacé = point déplacé");
		assertEquals(origineX,
		             point.getX(),
		             Point2D.getEpsilon(),
		             testName + ".getX() == " + origineX);
		assertEquals(origineY,
		             point.getY(),
		             Point2D.getEpsilon(),
		             testName + ".getY() == " + origineY);
		assertEquals(origineZ,
		             point.getZ(),
		             Point2D.getEpsilon(),
		             testName + ".getZ() == " + origineZ);
	}

	/**
	 * Test method for
	 * {@link points.Point3D#distance(points.Point3D, points.Point3D)}.
	 */
	@Test
	@DisplayName("Point3D.distance(Point3D, Point3D) : double")
	public final void testDistancePoint3DPoint3D()
	{
		String testName = new String("Point3D.distance(Point3D, Point3D)");
		System.out.println(testName);

		double radius = randomNumber(maxRandom);
		double angleStep = Math.PI / nbSteps;

		// Distance entre deux points opposés le long d'une sphere
		for (double longitude = 0.0; longitude < (Math.PI * 2.0);
				longitude += angleStep)
		{
			for (double latitude = -Math.PI; latitude < Math.PI;
					latitude += angleStep)
			{
				points.clear();
				double cosLatitude = Math.cos(latitude);
				double x = radius * Math.cos(longitude) * cosLatitude;
				double y = radius * Math.sin(longitude) * cosLatitude;
				double z = radius * Math.sin(latitude);
				points.add(new Point3D(x, y, z));
				points.add(new Point3D(-x, -y, -z));

				assertEquals(radius * 2.0,
				             Point3D.distance(points.get(0), points.get(1)),
				             Point2D.getEpsilon(),
				             testName + "(P3D1, P3D2)[" + longitude + ", "
				                 + latitude + "]");
				assertEquals(radius * 2.0,
				             Point3D.distance(points.get(1), points.get(0)),
				             Point2D.getEpsilon(),
				             testName + "(P3D2, P3D1)[" + longitude + ", "
				                 + latitude + "]");
			}
		}
	}

//	/**
//	 * Test method for
//	 * {@link points.Point3D#distance(points.Point3D, points.Point3D)}.
//	 */
//	@Test
//	@DisplayName("Point3D.distance2(Point3D, Point3D) : double")
//	public final void testDistance2()
//	{
//		String testName = new String("Point3D.distance2(Point3D, Point3D)");
//		System.out.println(testName);
//
//		double radius = randomNumber(maxRandom);
//		double angleStep = Math.PI / nbSteps;
//
//		// Distance entre deux points opposés le long d'une sphere
//		for (double longitude = 0.0; longitude < (Math.PI * 2.0);
//				longitude += angleStep)
//		{
//			for (double latitude = -Math.PI; latitude < Math.PI;
//					latitude += angleStep)
//			{
//				points.clear();
//				double cosLatitude = Math.cos(latitude);
//				double x = radius * Math.cos(longitude) * cosLatitude;
//				double y = radius * Math.sin(longitude) * cosLatitude;
//				double z = radius * Math.sin(latitude);
//				points.add(new Point3D(x, y, z));
//				points.add(new Point3D(-x, -y, -z));
//
//				assertEquals(radius * 2.0,
//				             Point3D.distance2(points.get(0), points.get(1)),
//				             Point2D.getEpsilon(),
//				             testName + "(P3D1, P3D2)[" + longitude + ", "
//				                 + latitude + "]");
//				assertEquals(radius * 2.0,
//				             Point3D.distance2(points.get(1), points.get(0)),
//				             Point2D.getEpsilon(),
//				             testName + "(P3D1, P3D2)[" + longitude + ", "
//				                 + latitude + "]");
//			}
//		}
//	}

	/**
	 * Test method for {@link points.Point3D#distance(points.Point3D)}.
	 */
	@Test
	@DisplayName("distance(Point3D) : double")
	public final void testDistancePoint3D()
	{
		String testName = new String("Point3D.distance(Point3D)");
		System.out.println(testName);

		double origineX = randomRange(maxRandom);
		double origineY = randomRange(maxRandom);
		double origineZ = randomRange(maxRandom);

		point = new Point3D(origineX, origineY, origineZ);

		double radius = randomNumber(maxRandom);
		double angleStep = Math.PI / nbSteps;
		double tolerance = Point2D.getEpsilon();
		Point3D p2 = new Point3D();

		// Distances entre un point fixe (point) et des points le long d'une
		// sphere (p)
		for (double longitude = 0.0; longitude < (Math.PI * 2.0);
				longitude += angleStep)
		{
			for (double latitude = -Math.PI; latitude < Math.PI;
					latitude += angleStep)
			{
				double cosLatitude = Math.cos(latitude);
				p2.setX(origineX + (radius * Math.cos(longitude) * cosLatitude));
				p2.setY(origineY + (radius * Math.sin(longitude) * cosLatitude));
				p2.setZ(origineZ + (radius * Math.sin(latitude)));

				assertEquals(radius,
				             point.distance(p2),
				             tolerance,
				             testName + "P1P2[" + longitude + ", " + latitude
				                 + "]");
				assertEquals(radius,
				             p2.distance(point),
				             tolerance,
				             testName + "P2P1[" + longitude + ", " + latitude
				                 + "]");
			}
		}
	}

	/**
	 * Test method for {@link points.Point3D#equals(java.lang.Object)}.
	 */
	@Test
	@DisplayName("equals(Object) : boolean")
	public final void testEqualsObject()
	{
		String testName = new String("Point3D.equals(Object)");
		System.out.println(testName);

		point = new Point3D(randomRange(maxRandom), randomRange(maxRandom),
				randomRange(maxRandom));
		Object o = new Object();

		// Inégalité avec un objet null
		assertFalse(point.equals(null), testName + " sur null");

		// Inégalité avec un objet de nature différente
		assertFalse(point.equals(o), testName + " sur object");

		// Égalité avec soi même
		Object objectPoint = point;
		assertTrue(point.equals(objectPoint), testName + " sur this");

		// Égalité avec une copie de soi même
		Point3D otherPoint = new Point3D(point);
		Object objectOtherPoint = otherPoint;
		assertTrue(point.equals(objectOtherPoint), testName + " sur copie");

		// Égalité avec un point déplacé de epsilon au plus
		double epsilon = Point2D.getEpsilon();
		for (long i = 0; i < nbTrials; i++)
		{
			otherPoint.setX(point.getX());
			otherPoint.setY(point.getY());
			otherPoint.setZ(point.getZ());
			double radius = randomNumber(epsilon);
			double longitude = randomNumber(Math.PI * 2.0);
			double latitude = randomNumber(Math.PI) - (Math.PI/2.0);
			otherPoint.move(
					radius * Math.cos(longitude) * Math.cos(latitude),
					radius * Math.sin(longitude) * Math.cos(latitude),
					radius * Math.sin(latitude));
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
				assertFalse(point.equals(objectOtherPoint),
				            testName + " point déplacé >= epsilon [" + distance
				                + "]");
			}
		}

		// Inégalité avec un autre point
		for (long i = 0; i < nbTrials; i++)
		{
			otherPoint.setX(point.getX());
			otherPoint.setY(point.getY());
			otherPoint.setZ(point.getZ());
			double radius = randomNumber(maxRandom);
			double longitude = randomNumber(Math.PI * 2.0);
			double latitude = randomNumber(Math.PI) - (Math.PI/2.0);
			otherPoint.move(
					radius * Math.cos(longitude) * Math.cos(latitude),
					radius * Math.sin(longitude) * Math.cos(latitude),
					radius * Math.sin(latitude));
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
				assertFalse(point.equals(objectOtherPoint),
				            testName + " point déplacé loin [" + distance
				                + "]");
			}
		}

		// Création d'un point 2D à partir d'un point 3D
		Point2D otherPoint2D = new Point2D(point);

		// Inégalité du point2D avec le point 3D (z != 0.0)
		assertFalse(point.equals(otherPoint2D), testName + " inégalité 3D/2D");

		// Inégalité du point 3D avec le point 2D (getClass ds Point2D.equals)
		assertFalse(otherPoint2D.equals(point), testName + " inégalité 2D/3D");

		// Projection du point 3D sur le plan 2D
		point.setZ(0.0);

		// égalité avec un point2D copié à partir du point 3D lorsque z == 0.0
		assertEquals(point, otherPoint2D, testName + " égalité 3D/2D z == 0.0");

		// Égalité avec un point 2D déplacé de epsilon au plus
		for (long i = 0; i < nbTrials; i++)
		{
			otherPoint2D.setX(point.getX());
			otherPoint2D.setY(point.getY());
			double radius = randomNumber(epsilon);
			double angle = randomNumber(Math.PI * 2.0);
			otherPoint2D.move(radius * Math.cos(angle),
					radius * Math.sin(angle));
			/*
			 * Attention, ci-dessous utilisation de distance(Point2D p)
			 */
			double distance = point.distance(otherPoint2D);

			if (distance < epsilon)
			{
				assertEquals(point,
				             otherPoint2D,
				             testName + " point 2D déplacé < epsilon ["
				                 + distance + "]");
			}
			else
			{
				assertFalse(point.equals(otherPoint2D),
				            testName + " point 2D déplacé >= epsilon ["
				                + distance + "]");
			}
		}

		// Inégalité avec un point 2D déplacé
		for (long i = 0; i < nbTrials; i++)
		{
			otherPoint2D.setX(point.getX());
			otherPoint2D.setY(point.getY());
			otherPoint2D.move(randomRange(maxRandom), randomRange(maxRandom));
			/*
			 * Attention, ci-dessous utilisation de distance(Point2D p)
			 */
			double distance = point.distance(otherPoint2D);

			if (distance < epsilon)
			{
				assertEquals(point,
				             otherPoint2D,
				             testName + " point 2D déplacé proche [" + distance
				                 + "]");
			}
			else
			{
				assertFalse(point.equals(otherPoint2D),
				            testName + " point 2D déplacé loin [" + distance
				                + "]");
			}
		}
	}
}
