package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import figures.Circle;
import points.Point2D;

/**
 * Classe de test pour les {@link Circle}
 * @implNote on ne teste que ce qui n'est pas déjà testé dans {@link FigureTest}
 * @author davidroussel
 */
@DisplayName("Circle")
public class CircleTest
{
	/**
	 * Base name pour tous les tests
	 */
	private final static String testBasename = new String(Circle.class.getSimpleName());

	/**
	 * Coordonnées à fournir pour un centre
	 */
	private final static Point2D testCenter = new Point2D(3, 7);

	/**
	 * Valeur à fournir pour un rayon
	 */
	private final static double testRadius = 2.0;

	/**
	 * Instance de test
	 */
	private Circle testCircle = null;

	/**
	 * Setup required before all tests.
	 * Nothing to do here
	 * @throws Exception never
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
		/*
		 * Class under test
		 */
		final Class<Circle> testClass = Circle.class;

		/*
		 * Expected constructors arguments types
		 */
		Class<?>[][] expectedConstructorsArguments = {
			{Circle.class},								// Circle(Circle c)
			{Point2D.class, double.class},				// Circle(Point2D p, double r)
			{double.class, double.class, double.class},	// Circle(double x, double y, double r)
			{}											// Circle()
		};

		/*
		 * Expected methods in the test class (at least the ones under test):
		 * 	- Methods names
		 * 	- Methods arguments types
		 * 	- Methods return types
		 */
		String[] expectedMethodsNames = {"getRadius", "setRadius"};
		Class<?>[][] expectedMethodsArguments = {
			{},
			{double.class},
		};
		Class<?>[] expectedMethodsReturns = {
			double.class,	// getRadius
			void.class,		// setRadius
		};

		boolean checked =
		    ContentChecker.assertContent(testBasename,
		                                 testClass,
		                                 expectedConstructorsArguments,
		                                 expectedMethodsNames,
		                                 expectedMethodsArguments,
		                                 expectedMethodsReturns,
		                                 null); // All methods are assumed public
		assertTrue(checked, testBasename + " content didn't check");
	}

	/**
	 * Tear down after all tests.
	 * Nothing to do here
	 * @throws Exception never
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception
	{
	}

	/**
	 * Setup required before each test.
	 * Creates a test circle.
	 * @throws Exception never
	 */
	@BeforeEach
	void setUp() throws Exception
	{
		testCircle = new Circle(testCenter, testRadius);
	}

	/**
	 * Tear down after each test.
	 * Resets the rest circle to null
	 * @throws Exception never
	 */
	@AfterEach
	void tearDown() throws Exception
	{
		testCircle = null;
	}

	/**
	 * Test du constructeur par défaut {@link Circle#Circle()}
	 */
	@Test
	@DisplayName("Circle()")
	final void testCircle()
	{
		String testName = new String(testBasename + "()");
		System.out.println(testName);
		testCircle = new Circle();

		assertNotNull(testCircle, testName + " failed");
		assertEquals(1.0, testCircle.getRadius(), testName + "failed with non null radius");
		assertEquals(new Point2D(0.0, 0.0), testCircle.getCenter(), testName + "failed with non origine center");
	}

	/**
	 * Test du constructeur valué {@link Circle#Circle(double, double, double)}
	 */
	@Test
	@DisplayName("Circle(double x, double y, double r)")
	final void testCircleDoubleDoubleDouble()
	{
		String testName = new String(testBasename + "(x, y, r)");
		System.out.println(testName);
		testCircle = new Circle(testCenter.getX(), testCenter.getY(), testRadius);

		assertNotNull(testCircle, testName + " failed");
		assertEquals(testRadius, testCircle.getRadius(), testName + " failed with wrong radius radius");
		assertEquals(testCenter, testCircle.getCenter(), testName + " failed with wrong center");
	}

	/**
	 * Test de l'autre constructeur valué {@link Circle#Circle(Point2D, double)}
	 */
	@Test
	@DisplayName("Circle(Point2D p, double r)")
	final void testCirclePoint2DDouble()
	{
		String testName = new String(testBasename + "(p, r)");
		System.out.println(testName);
		testCircle = new Circle(testCenter, testRadius);

		assertNotNull(testCircle, testName + " failed");
		assertEquals(testRadius,
		             testCircle.getRadius(),
		             testName + " failed with wrong radius radius");
		assertEquals(testCenter,
		             testCircle.getCenter(),
		             testName + " failed with wrong center");
		assertNotSame(testCenter,
		              testCircle.getCenter(),
		              testName + " failed with shallow copied center");
	}

	/**
	 * Test du constructeur de copie {@link Circle#Circle(Circle)}
	 */
	@Test
	@DisplayName("Circle(Circle c)")
	final void testCircleCircle()
	{
		String testName = new String(testBasename + "(p, r)");
		System.out.println(testName);
		Circle otherCircle = new Circle(testCenter, testRadius);
		testCircle = new Circle(otherCircle);

		assertNotNull(testCircle, testName + " failed");
		assertNotSame(otherCircle, testCircle,  testName + " failed with same Object");
		assertEquals(otherCircle.getRadius(),
		             testCircle.getRadius(),
		             testName + " failed with wrong radius radius");
		assertEquals(otherCircle.getCenter(),
		             testCircle.getCenter(),
		             testName + " failed with wrong center");
		assertNotSame(otherCircle.getCenter(),
		              testCircle.getCenter(),
		              testName + " failed with shallow copied center");
	}

	/**
	 * Test de l'accesseur au rayon {@link Circle#getRadius()}
	 */
	@Test
	@DisplayName("getRadius() : double")
	final void testGetRadius()
	{
		String testName = new String(testBasename + ".getRadius()");
		System.out.println(testName);

		assertEquals(testRadius, testCircle.getRadius() , testName + " failed with wrong radius");
	}

	/**
	 * Test de l'accesseur au rayon {@link Circle#setRadius(double)}
	 */
	@Test
	@DisplayName("setRadius(double r)")
	final void testSetRadius()
	{
		String testName = new String(testBasename + ".setRadius()");
		System.out.println(testName);

		double currentRadius = testCircle.getRadius();

		testCircle.setRadius(-1.0);

		assertNotEquals(currentRadius, testCircle.getRadius(), testName + " failed with unchanged radius");
		assertEquals(0.0, testCircle.getRadius(), testName + " failed with negative radius");

		double newRadius = 10.0;

		testCircle.setRadius(newRadius);

		assertEquals(newRadius, testCircle.getRadius(), testName + " failed with incorrect radius");
	}
}
