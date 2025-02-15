package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import figures.Rectangle;
import points.Point2D;

/**
 * Classe de test pour les {@link Rectangle}
 * @implNote on ne teste que ce qui n'est pas déjà testé dans {@link FigureTest}
 * @author davidroussel
 */
@DisplayName("Rectangle")
public class RectangleTest
{
	/**
	 * Limite gauche pour le rectangle de test
	 */
	private final static double left = 5.0;
	/**
	 * Limite droite pour le rectangle de test
	 */
	private final static double right = 8.0;
	/**
	 * Limite basse pour le rectangle de test
	 */
	private final static double bottom = 1.0;
	/**
	 * Limite haute pour le rectangle de test
	 */
	private final static double  top = 4.0;
	/**
	 * Limites horizontales à utiliser lors du test de
	 * {@link Rectangle#Rectangle(double, double, double, double)}
	 */
	private final static double[] xs = new double[] {
		left, right, right, left
	};
	/**
	 * Limites verticales à utiliser lors du test de
	 * {@link Rectangle#Rectangle(double, double, double, double)}
	 */
	private final static double[] ys = new double[] {
		bottom, top, top, bottom
	};
	/**
	 * Abscisse du centre du rectangle de test
	 */
	private final static double centerX = (left + right) / 2.0;
	/**
	 * Ordonnée du centre du rectangle de test
	 */
	private final static double centerY = (bottom + top) / 2.0;
	/**
	 * Point en bas à gauche du rectangle de test
	 */
	private static Point2D bottomLeft = null;
	/**
	 * Point en haut à droite du rectangle de test
	 */
	private static Point2D topRight = null;
	/**
	 * Point en bas à droite du rectangle de test
	 */
	private static Point2D bottomRight = null;
	/**
	 * Point en haut à gauche du rectangle de test
	 */
	private static Point2D topLeft = null;
	/**
	 * Point au centre du rectangle de test
	 */
	private static Point2D center = null;
	/**
	 * Points tests à utiliser lors du test
	 * {@link Rectangle#Rectangle(Point2D, Point2D)}
	 */
	private static Point2D[] points;

	/**
	 * Rectangle de tes
	 */
	private Rectangle testRectangle = null;

	/**
	 * Setup before all tests
	 * @throws Exception never
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
		bottomLeft = new Point2D(left, bottom);
		topRight = new Point2D(right, top);
		bottomRight = new Point2D(right, bottom);
		topLeft = new Point2D(left, top);
		center = new Point2D(centerX, centerY);

		points = new Point2D[8];
		points[0] = bottomLeft;
		points[1] = topRight;
		points[2] = bottomRight;
		points[3] = topLeft;
		points[4] = topRight;
		points[5] = bottomLeft;
		points[6] = topLeft;
		points[7] = bottomRight;

		/*
		 * Class under test
		 */
		final Class<Rectangle> testClass = Rectangle.class;

		/*
		 * Expected constructors arguments types
		 */
		Class<?>[][] expectedConstructorsArguments = {
			{},	// Rectangle()
			{double.class, double.class, double.class, double.class},	// Rectangle(double x1, double y1, double x2, double y2)
			{Point2D.class, Point2D.class},	// Rectangle(Point2D p1, Point2D p2)
			{Rectangle.class}	// Rectangle(Rectangle r)
		};

		/*
		 * Expected methods in the test class (at least the ones under test):
		 * 	- Methods names
		 * 	- Methods arguments types
		 * 	- Methods return types
		 */
		String[] expectedMethodsNames = {
			"bottomLeft",
			"topRight",
			"setWidth",
			"setHeight",
		};
		Class<?>[][] expectedMethodsArguments = {
			{},	// bottomLeft
			{},	// topRight
			{double.class},	// setWidth
			{double.class},	// setHeight
		};
		Class<?>[] expectedMethodsReturns = {
			Point2D.class,	// bottomLeft
			Point2D.class,	// topRight
			void.class,	// setWidth
			void.class,	// setHeight
		};

		String testName = "Rectangle Content Test: ";

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
	 * Tear down after all tests
	 * @throws Exception never
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception
	{
	}

	/**
	 * Setup before each test.
	 * Creates the test rectangle
	 * @throws Exception never
	 */
	@BeforeEach
	void setUp() throws Exception
	{
		testRectangle = new Rectangle(bottomLeft, topRight);
	}

	/**
	 * Tear down after each test.
	 * Resets the test rectangle to null
	 * @throws Exception never
	 */
	@AfterEach
	void tearDown() throws Exception
	{
		testRectangle = null;
	}

	/**
	 * Test du constructeur par défaut {@link Rectangle#Rectangle()}
	 */
	@Test
	@DisplayName("Rectangle()")
	final void testRectangle()
	{
		String testName = new String(Rectangle.class.getSimpleName() + "()");
		System.out.println(testName);
		testRectangle = new Rectangle();

		assertNotNull(testRectangle, testName + " failed");
		assertEquals(0.0, testRectangle.width(), testName + " failed with non unit width");
		assertEquals(0.0, testRectangle.height(), testName + " failed with non unit height");
		assertEquals(new Point2D(0.0, 0.0), testRectangle.getCenter(), testName + " failed with wrong center");
	}

	/**
	 * Test du constructeur valué {@link Rectangle#Rectangle(double, double, double, double)}
	 */
	@Test
	@DisplayName("Rectangle(double x1, double y1, double x2, double y2)")
	final void testRectangleDoubleDoubleDoubleDouble()
	{
		String testName = new String(Rectangle.class.getSimpleName() + "(double x1, double y1, double x2, double y2)");
		System.out.println(testName);
		for (int i = 0; i < xs.length; i+=2)
		{
			testRectangle = new Rectangle(xs[i],
			                              ys[i],
			                              xs[(i+1)],
			                              ys[(i+1)]);

			assertNotNull(testRectangle, testName + " failed");
			assertEquals((right - left), testRectangle.width(), testName + " failed with wrong width");
			assertEquals((top - bottom), testRectangle.height(), testName + " failed with wrong height");
			assertEquals(center, testRectangle.getCenter(), testName + " failed with wrong center");
		}
	}

	/**
	 * Test du constructeur {@link Rectangle#Rectangle(Point2D, Point2D)}
	 */
	@Test
	@DisplayName("Rectangle(Point2D, Point2D)")
	final void testRectanglePoint2DPoint2D()
	{
		String testName = new String(Rectangle.class.getSimpleName() + "Rectangle(Point2D, Point2D)");
		System.out.println(testName);

		for (int i = 0; i < points.length; i+=2)
		{
			testRectangle = new Rectangle(points[i], points[i+1]);

			assertNotNull(testRectangle, testName + " failed");
			assertEquals((right - left), testRectangle.width(), testName + " failed with wrong width");
			assertEquals((top - bottom), testRectangle.height(), testName + " failed with wrong height");
			assertEquals(center, testRectangle.getCenter(), testName + " failed with wrong center");
		}
	}

	/**
	 * Test du constructeur de copie {@link Rectangle#Rectangle(Rectangle)}
	 */
	@Test
	@DisplayName("Rectangle(Rectangle)")
	final void testRectangleRectangle()
	{
		String testName = new String(Rectangle.class.getSimpleName() + "(Rectangle r)");
		System.out.println(testName);
		Rectangle otherRectangle = new Rectangle(bottomLeft, topRight);
		testRectangle = new Rectangle(otherRectangle);

		assertNotNull(testRectangle, testName + " failed");
		assertNotSame(otherRectangle, testRectangle,  testName + " failed with same Object");
		assertEquals(otherRectangle.width(),
		             testRectangle.width(),
		             testName + " failed with wrong width");
		assertEquals(otherRectangle.height(),
		             testRectangle.height(),
		             testName + " failed with wrong height");
		assertEquals(otherRectangle.getCenter(),
		             testRectangle.getCenter(),
		             testName + " failed with wrong center");
	}

	/**
	 * Test de la méthode {@link Rectangle#bottomLeft()}
	 */
	@Test
	@DisplayName("Rectangle.bottomLeft() : Point2D")
	final void testBottomLeft()
	{
		String testName = new String(Rectangle.class.getSimpleName() + ".bottomLeft()");
		System.out.println(testName);

		assertEquals(bottomLeft, testRectangle.bottomLeft() , testName + " failed with bottom left point");
	}

	/**
	 * Test de la méthode {@link Rectangle#topRight()}
	 */
	@Test
	@DisplayName("Rectangle.topRight() : Point2D")
	final void testTopRight()
	{
		String testName = new String(Rectangle.class.getSimpleName() + ".topRight()");
		System.out.println(testName);

		assertEquals(topRight, testRectangle.topRight() , testName + " failed with bottom left point");
	}

	/**
	 * Test de la méthode {@link Rectangle#setWidth(double width)}
	 */
	@Test
	@DisplayName("Rectangle.setWidth(double v)")
	final void testSetWidth()
	{
		String testName = new String(Rectangle.class.getSimpleName() + ".setWidth(double v)");
		System.out.println(testName);
		double currentWidth = testRectangle.width();
		Point2D currentCenter = testRectangle.getCenter();
		double invalidWidth = -1.0;

		testRectangle.setWidth(invalidWidth);
		assertEquals(currentWidth, testRectangle.width(), testName + " failed with negative width");

		double newWidth = currentWidth + 2.0;
		testRectangle.setWidth(newWidth);
		assertEquals(newWidth, testRectangle.width(), testName + " failed with new width");
		assertEquals(currentCenter, testRectangle.getCenter(), testName + " failed with center moved");
	}

	/**
	 * Test de la méthode {@link Rectangle#setHeight(double height)}
	 */
	@Test
	@DisplayName("Rectangle.setHeight(double v)")
	final void testSetHeight()
	{
		String testName = new String(Rectangle.class.getSimpleName() + ".setHeight(double v)");
		System.out.println(testName);
		double currentHeight = testRectangle.height();
		Point2D currentCenter = testRectangle.getCenter();
		double invalidHeight = -1.0;

		testRectangle.setHeight(invalidHeight);
		assertEquals(currentHeight, testRectangle.height(), testName + " failed with negative height");

		double newHeight = currentHeight + 2.0;
		testRectangle.setHeight(newHeight);
		assertEquals(newHeight, testRectangle.height(), testName + " failed with new height");
		assertEquals(currentCenter, testRectangle.getCenter(), testName + " failed with center moved");
	}

}
