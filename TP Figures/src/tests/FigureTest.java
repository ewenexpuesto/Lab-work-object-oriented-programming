package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import figures.Circle;
import figures.Figure;
import points.Point2D;

/**
 * Classe de test pour {@link Figure}
 * @author davidroussel
 */
@DisplayName("Figure")
public class FigureTest
{

	/**
	 * Les différentes natures de figures à tester
	 */
	@SuppressWarnings("unchecked")
	private static final Class<? extends Figure>[] figureTypes =
	(Class<? extends Figure>[]) new Class<?>[]
	{
		Circle.class,
		// TODO à décommenter quand la classe Retangle sera prête
//		Rectangle.class,
		// TODO à décommenter quand la classe Polygon sera prête
//		Polygon.class,
		// TODO à décommenter quand la classe Triangle sera prête
//		Triangle.class,
		// TODO à décommenter quand la classe Group sera prête
//		Group.class
	};

	/**
	 * Index d'un type de figure particulier dans les différents tableaux:
	 * - #points
	 * - #centers
	 * - #bbCenters
	 * - #toStrings
	 * - #areas
	 * - #widths
	 * - #heights
	 * @param type le type de figure à tester
	 * @return l'index du type de figure dans les différents tableaux
	 * sus-mentionnés ou bien -1 si ce type de figure ne fait pas partie
	 * des tableaux
	 */
	private static int indexOf(Class<? extends Figure> type)
	{
		if (type == Circle.class)
		{
			return 0;
		}
		// TODO à décommenter quand la classe Retangle sera prête
		// else if (type == Rectangle.class)
		// {
		// 	return 1;
		// }
		// TODO à décommenter quand la classe Polygon sera prête
		// else if (type == Polygon.class)
		// {
		// 	return 2;
		// }
		// TODO à décommenter quand la classe Triangle sera prête
		// else if (type == Triangle.class)
		// {
		// 	return 3;
		// }
		// TODO à décommenter quand la classe Group sera prête
		// else if (type == Group.class)
		// {
		// 	return 4;
		// }
		else
		{
			return -1;
		}
	}

	/**
	 * La figure courante à tester
	 */
	private Figure testFigure = null;

	/**
	 * La classe de la figure à tester
	 */
	private Class<? extends Figure> testFigureType = null;

	/**
	 * Le nom/type de la figure courante à tester
	 */
	private String testFigureTypeName = null;

	/**
	 * Tolérance pour les comparaisons numériques (aires, distances)
	 */
	private static final double tolerance = Point2D.getEpsilon();

	/**
	 * L'ensemble des figures à tester
	 */
	private static final Figure[] figures = new Figure[Math.max(figureTypes.length, 5)];

	/**
	 * Autre ensemble (distinct) de figures à tester pour l'égalité;
	 */
	private static final Figure[] altFigures = new Figure[Math.max(figureTypes.length, 5)];

	/**
	 * Une map permettant d'obtenir la figure en fonction de son type.
	 * Sera construite à partir de {@link #figureTypes} et de {@link #figures}
	 */
	private static Map<Class<? extends Figure>, Figure> figuresMap =
			new HashMap<Class<? extends Figure>, Figure>();

	/**
	 * Les points à utiliser pour construire les figures
	 */
	private static final Point2D[][] points = new Point2D[][] {
		{new Point2D(7,3)}, // 0,0 : Circle
		{new Point2D(4,1), new Point2D(8,4)}, // 1,0->1 : Rectangle
		{new Point2D(5,1), new Point2D(8,2), new Point2D(7,5), new Point2D(2,4),
			new Point2D(2,3)}, // 2,0->4 : Polygon
		{new Point2D(3,2), new Point2D(7,3), new Point2D(4,6)}, // 3,0->2 : Triangle
	};

	/**
	 * Les différents centre (de masse) des figures
	 */
	private static final Point2D[] centers = new Point2D[] {
		new Point2D(7,3), // Cercle
		new Point2D(6, 2.5), // Rectangle
		new Point2D(5.150537634408602, 3.053763440860215), // Polygone
		new Point2D(4.666666666666667, 3.6666666666666665), // Triangle
		new Point2D(5.77714726, 2.996496) // Groupe : centre de masse des figures
	};

	/**
	 * la map permettant d'obtenir le centre pré-calculé d'une figure en fonction
	 * de son type.
	 * Sera construite à partir de {@link #figureTypes} et de {@link #centers}
	 */
	private static Map<Class<? extends Figure>, Point2D> centersMap =
			new HashMap<Class<? extends Figure>, Point2D>();

	/**
	 * Les différents centres (des bounding box) des figures
	 */
	private static final Point2D[] bbCenters = new Point2D[] {
		new Point2D(7, 3), // Cercle
		new Point2D(6, 2.5), // Rectangle
		new Point2D(5, 3), // Polygone
		new Point2D(5, 4), // Triangle
		new Point2D(5.5, 3.5) // Groupe : centre de la bounding box
	};

	/**
	 * la map permettant d'obtenir le centre pré-calculé de la boite englobante
	 * d'une figure en fonction de son type.
	 * Sera construite à partir de {@link #figureTypes} et de {@link #bbCenters}
	 */
	private static Map<Class<? extends Figure>, Point2D> bbCentersMap =
			new HashMap<Class<? extends Figure>, Point2D>();

	/**
	 * toString attendu des différentes figures
	 */
	private static String[] toStrings = new String[] {
		"Circle : x = 7.0 y = 3.0, r = 2.0",
		"Rectangle : x = 4.0 y = 1.0, x = 8.0 y = 4.0",
		"Polygon : x = 5.0 y = 1.0, x = 8.0 y = 2.0, x = 7.0 y = 5.0, x = 2.0 y = 4.0, x = 2.0 y = 3.0",
		"Triangle : x = 3.0 y = 2.0, x = 7.0 y = 3.0, x = 4.0 y = 6.0",
		"" // Groupe = à recalculer d'après les précédents
	};

	/**
	 * Map contenant les toString attendus pour chaque type de figure.
	 */
	static private Map<Class<? extends Figure>, String> toStringsMap =
	    new HashMap<Class<? extends Figure>, String>();

	/**
	 * aires attendues des différentes figures
	 */
	private static double[] areas = new double[] {
		12.566371, // Cercle
		12.0, // Rectangle
		15.5, // Polygone
		7.5, // Triangle
		47.566371 // Groupe
	};

	/**
	 * Map pour obtenir l'aire attendue en fonction du type de figure
	 */
	static private Map<Class<? extends Figure>, Double> areasMap =
	    new HashMap<Class<? extends Figure>, Double>();

	/**
	 * largeurs attendues des différentes figures
	 */
	private static double[] widths = new double[] {
		4.0,	// Cercle
		4.0,	// Rectangle
		6.0,	// Polygone
		4.0,	// triangle
		7.0		// Group
	};

	/**
	 * Map pour obtenir la largeur attendue en fonction du type de figure
	 */
	static private Map<Class<? extends Figure>, Double> widthMap =
	    new HashMap<Class<? extends Figure>, Double>();

	/**
	 * hauteurs attendues des différentes figures
	 */
	private static double[] heights = new double[] {
		4.0,	// Cercle
		3.0,	// Rectangle
		4.0,	// Polygone
		4.0,	// triangle
		5.0		// Group
	};

	/**
	 * Map pour obtenir la hauteur attendue en fonction du type de figure
	 */
	static private Map<Class<? extends Figure>, Double> heightMap =
	    new HashMap<Class<? extends Figure>, Double>();

	/**
	 * Distances entre les centres des figures
	 */
	private static double[][] interDistances = new double[][] {
	//   Cercle    Rect.     Poly.     Tri.      Grp.
		{0.0,      1.118034, 1.850244, 2.426703, 1.222858}, // Cercle
		{1.118034, 0.0,      1.014022, 1.771691, 0.544217}, // Rectangle
		{1.850244, 1.014022, 0.0,      0.780885, 0.629221}, // Polygone
		{2.426703, 1.771691, 0.780885,      0.0, 1.297033}, // Triangle
		{1.222858, 0.544217, 0.629221, 1.297033,      0.0}  // Groupe
	};

	/**
	 * Un point à l'intérieur de toutes les figures
	 */
	private static final Point2D insidePoint = new Point2D(6,3);

	/**
	 * Un point à l'extérieur de toutes les figures
	 */
	private static final Point2D outsidePoint = new Point2D(6,5);

	/**
	 * Figures type provider utilisé pour chaque Parameterized test
	 * (Appelé par son nom (string), d'où la nécessité du suppress warnings)
	 * @return un flux de types de Figures à utiliser dans chaque @ParameterizedTest
	 */
	private static Stream<Class<? extends Figure>> figureClassesProvider()
	{
		return Stream.of(figureTypes);
	}

	/**
	 * Figures provider utilisé pour chaque Parameterized test
	 * (Appelé par son nom (string), d'où la nécessité du suppress warnings)
	 * @return un flux de Figures à utiliser dans chaque @ParameterizedTest
	 */
	private static Stream<Figure> figuresProvider()
	{
		/*
		 * figuresMap is built in the setupBeforeClass so it should
		 * be already built when this stream is provided to every
		 * ParameterizedTest
		 */
	    return figuresMap.values().stream();
	}

	/**
	 * Mise en place avant l'ensemble des tests
	 * @throws java.lang.Exception quand les poules auront des dents
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
		// remplissage des xxxMap à partir des tableaux
		for (int i = 0; i < figureTypes.length; i++)
		{
			Class<? extends Figure> currentFigureType = figureTypes[i];
			int index = indexOf(currentFigureType);
			if (index == -1)
			{
				fail("Unknown index for " + currentFigureType.getSimpleName());
			}
			if (currentFigureType == Circle.class)
			{
				figures[index] = new Circle(points[index][0], 2.0);
				altFigures[index] = new Circle(points[index][0], 2.0);
			}
			// TODO à décommenter quand la classe Rectangle sera prête
//			else if (currentFigureType == Rectangle.class)
//			{
//				figures[index] = new Rectangle(points[index][0],
//				                               points[index][1]);
//				altFigures[index] = new Rectangle(points[index][1],
//				                                  points[index][0]);
//			}
			// TODO à décommenter quand la classe Polygon sera prête
//			else if (currentFigureType == Polygon.class)
//			{
//				ArrayList<Point2D> polyPoints = new ArrayList<Point2D>();
//				for (Point2D p : points[index])
//				{
//					polyPoints.add(p);
//				}
//				figures[index] = new Polygon(polyPoints);
//				polyPoints.clear();
//				for (int j = 1; j <= points[index].length; j++)
//				{
//					polyPoints.add(points[index][j%points[index].length]);
//				}
//				altFigures[index] = new Polygon(polyPoints);
//			}
			// TODO à décommenter quand la classe Triangle sera prête
//			else if (currentFigureType == Triangle.class)
//			{
//				figures[index] = new Triangle(points[index][0],
//				                              points[index][1],
//				                              points[index][2]);
//				altFigures[index] = new Triangle(points[index][1],
//				                                 points[index][0],
//				                                 points[index][2]);
//			}
			// TODO à décommenter quand la classe Group sera prête
//			else if (currentFigureType == Group.class)
//			{
//				ArrayList<Figure> allNGFigures = new ArrayList<Figure>();
//				for (int j = 0; j < figureTypes.length; j++)
//				{
//					if (figureTypes[j] != Group.class)
//					{
//						allNGFigures.add(figures[j]);
//					}
//				}
//				figures[index] = new Group(allNGFigures);
//				// calcul du toString des Groupes
//				StringBuilder sb = new StringBuilder("Group : ");
//				for (int j = 0; j < toStrings.length; j++)
//				{
//					if (figureTypes[j] != Group.class)
//					{
//						sb.append("\n" + toStrings[j]);
//					}
//				}
//				toStrings[index] = sb.toString();
//
//				allNGFigures.clear();
//				for (int j = figureTypes.length - 1; j >= 0; j--)
//				{
//					if (figureTypes[j] != Group.class)
//					{
//						allNGFigures.add(altFigures[j]);
//					}
//				}
//				altFigures[index] = new Group(allNGFigures);
//			}
			else
			{
				index = 0; // safe but useless
				fail("Unknown figure type : " + currentFigureType.getSimpleName());
			}

			figuresMap.put(currentFigureType, figures[index]);
			centersMap.put(currentFigureType, centers[index]);
			bbCentersMap.put(currentFigureType, bbCenters[index]);
			toStringsMap.put(currentFigureType, toStrings[index]);
			areasMap.put(currentFigureType, Double.valueOf(areas[index]));
			widthMap.put(currentFigureType, Double.valueOf(widths[index]));
			heightMap.put(currentFigureType, Double.valueOf(heights[index]));


			/*
			 * Expected constructors arguments types
			 */
			Class<?>[][] expectedConstructorsArguments = {
				{},						// Default constructor
				{currentFigureType},	// Copy constructor
			};

			/*
			 * Expected methods in the test class (at least the ones under test):
			 * 	- Methods names
			 * 	- Methods arguments types
			 * 	- Methods return types
			 * 	- Modifiers of each method
			 */
			String[] expectedMethodsNames = {
				"getName",				// String getName();
				"move",					// Figure move(double dx, double dy);
				"toString",				// String toString();
				"contains",				// boolean contains(Point2D p);
				"getCenter",			// Point2D getCenter();
				"getBoundingBoxCenter",	// Point2D getBoundingBoxCenter();
				"width",				// double width();
				"height",				// double height();
				"area",					// double area();
				"distanceToCenter",		// double distanceToCenter(Figure f)
				"equals",				// boolean equals(Object o);
				"hashCode"				// int hashCode();
			};
			Class<?>[][] expectedMethodsArguments = {
				{},								// String getName();
				{double.class, double.class},	// Figure move(double dx, double dy);
				{},								// String toString();
				{Point2D.class},				// boolean contains(Point2D p);
				{},								// Point2D getCenter();
				{},								// Point2D getBoundingBoxCenter();
				{},								// double width();
				{},								// double height();
				{},								// double area();
				{Figure.class},					// double distanceToCenter(Figure f)
				{Object.class},					// boolean equals(Object o);
				{}								// int hashCode();
			};
			Class<?>[] expectedMethodsReturns = {
				String.class,	// String getName();
				Figure.class,	// Figure move(double dx, double dy);
				String.class,	// String toString();
				boolean.class,	// boolean contains(Point2D p);
				Point2D.class,	// Point2D getCenter();
				Point2D.class,	// Point2D getBoundingBoxCenter();
				double.class,	// double width();
				double.class,	// double height();
				double.class,	// double area();
				double.class,	// double distanceToCenter(Figure f)
				boolean.class,	// boolean equals(Object o);
				int.class		// int hashCode();
			};
			String testName = currentFigureType + " Content Test: ";

			boolean checked =
			    ContentChecker.assertContent(testName,
			                                 currentFigureType,
			                                 expectedConstructorsArguments,
			                                 expectedMethodsNames,
			                                 expectedMethodsArguments,
			                                 expectedMethodsReturns,
			                                 null); // All methods are assumed public
			assertTrue(checked, testName + " content didn't check");

		}
	}

	/**
	 * Nettoyage après l'ensemble des tests
	 * @throws java.lang.Exception quand les poules auront des dents
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception
	{
		figuresMap.clear();
		centersMap.clear();
		bbCentersMap.clear();
		toStringsMap.clear();
		areasMap.clear();
		widthMap.clear();
		heightMap.clear();
	}

	/**
	 * Setup d'un test en fonction d'une figure donnée.
	 * Met en place:
	 * 	- La figure à tester
	 * 	- Son type
	 * 	- Le nom de son type
	 * @param figure la figure à tester
	 */
	void setupTest(Figure figure)
	{
		testFigure = figure;
		testFigureType = figure.getClass();
		testFigureTypeName = testFigureType.getSimpleName();
	}

//	class FigureParameterResolver implements ParameterResolver
//	{
//		@Override
//		public boolean supportsParameter(ParameterContext parameterContext,
//		                                 ExtensionContext extensionContext)
//		    throws ParameterResolutionException
//		{
//			return Figure.class.isInstance(parameterContext.getParameter().getType());
//		}
//
//		@Override
//		public Object resolveParameter(ParameterContext parameterContext,
//		                               ExtensionContext extensionContext)
//		    throws ParameterResolutionException
//		{
//			return figuresMap.get(parameterContext.getParameter().getType().getSimpleName());
//		}
//	}

	/**
	 * Mise en place avant chaque test
	 * @throws java.lang.Exception quand les poules auront des dents
	 */
	@BeforeEach
//	@MethodSource("figuresProvider")
//	@ExtendWith(FigureParameterResolver.class)
//	void setUp(Figure figure) throws Exception
	void setUp() throws Exception
	{
//		if (figure != null)
//		{
//			testFigure = figure;
//			figureType = figure.getClass();
//			typeName = figureType.getSimpleName();
//		}
//		else
//		{
//			throw new Exception("Null figure");
//		}
	}

	/**
	 * Nettoyage après chaque test
	 * @throws java.lang.Exception quand les poules auront des dents
	 */
	@AfterEach
	void tearDown() throws Exception
	{
		testFigure = null;
		testFigureType = null;
		testFigureTypeName = null;
	}

	/**
	 * Test du constructeur par défaut de chaque figure
	 * @param type le type de la figure à tester
	 */
	@ParameterizedTest
	@MethodSource("figureClassesProvider")
	@DisplayName("Default constructors")
	public final void testFigureConstructor(Class<? extends Figure> type)
	{
		String testName = new String(type.getSimpleName() + "()");
		System.out.println(testName);
		Constructor<? extends Figure> defaultConstructor = null;
		Class<?>[] constructorsArgs = new Class<?>[0];

		try
		{
			defaultConstructor = type.getConstructor(constructorsArgs);
		}
		catch (SecurityException e)
		{
			fail(testName +  " constructor security exception");
		}
		catch (NoSuchMethodException e)
		{
			fail(testName +  " constructor not found");
		}

		if (defaultConstructor != null)
		{
			Object instance = null;
			try
			{
				instance = defaultConstructor.newInstance(new Object[0]);
			}
			catch (IllegalArgumentException e)
			{
				fail(testName + " wrong constructor arguments");
			}
			catch (InstantiationException e)
			{
				fail(testName + " instanciation exception");
			}
			catch (IllegalAccessException e)
			{
				fail(testName + " illegal access");
			}
			catch (InvocationTargetException e)
			{
				fail(testName + " invocation target exception");
			}
			assertNotNull(instance, testName);
			assertEquals(instance, instance, testName + " self equality");
		}
	}

	/**
	 * Test du constructeur de copie de chaque figure
	 * @param type le type de la figure à tester
	 */
	@ParameterizedTest
	@MethodSource("figureClassesProvider")
	@DisplayName("Copy constructors")
	public final void testFigureCopyConstructor(Class<? extends Figure> type)
	{
		String typeName = type.getSimpleName();
		String testName = new String(typeName + "(" + typeName + ")");
		System.out.println(testName);
		Constructor<? extends Figure> copyConstructor = null;
		Class<?>[] constructorsArgs = new Class<?>[] {type};

		try
		{
			copyConstructor = type.getConstructor(constructorsArgs);
		}
		catch (SecurityException e)
		{
			fail(testName +  " constructor security exception");
		}
		catch (NoSuchMethodException e)
		{
			fail(testName +  " constructor not found");
		}

		if (copyConstructor != null)
		{
			Object instance = null;
			try
			{
				instance = copyConstructor.newInstance(figuresMap.get(type));
			}
			catch (IllegalArgumentException e)
			{
				fail(testName + " wrong constructor arguments");
			}
			catch (InstantiationException e)
			{
				fail(testName + " instanciation exception");
			}
			catch (IllegalAccessException e)
			{
				fail(testName + " illegal access");
			}
			catch (InvocationTargetException e)
			{
				fail(testName + " invocation target exception");
			}
			assertNotNull(instance, testName);
			assertEquals(instance, instance, testName + " self equality");
			assertEquals(figuresMap.get(type), instance, testName + " equality with similar");
		}
	}

	/**
	 * Test de la méthode {@link Figure#getName()}
	 * @param figure la figure à tester
	 */
	@ParameterizedTest
	@MethodSource("figuresProvider")
	@DisplayName("getName() : String")
	final void testGetName(Figure figure)
	{
		setupTest(figure);
		String testName = new String(testFigureTypeName + ".getName()");
		System.out.println(testName);
		assertNotNull(figure, testName);

		assertEquals(figuresMap.get(testFigureType).getName(),
		             testFigure.getName(),
		             testName);
	}

	/**
	 * Test de la méthode {@link Figure#move(double, double)}
	 * @param figure la figure à tester
	 */
	@ParameterizedTest
	@MethodSource("figuresProvider")
	@DisplayName("move(double dx, double dy) : Figure")
	final void testMove(Figure figure)
	{
		setupTest(figure);
		String testName = new String(testFigureTypeName + ".move(double, double)");
		System.out.println(testName);

		Point2D centreBefore = new Point2D(testFigure.getCenter());

		double dx = 1.0;
		double dy = 1.0;

		Figure result = testFigure.move(dx, dy);

		assertSame(figure, result, testName + " Not same instance");

		Point2D centreAfter = testFigure.getCenter();
		Point2D centreBeforeMoved = centreBefore.move(dx, dy);
		assertNotSame(centreAfter, centreBeforeMoved, testName + " same center moved");
		assertEquals(centreBeforeMoved,
		             centreAfter,
		             testName + " unexpected center");

		testFigure.move(-dx, -dy); // inutile
	}

	/**
	 * Test de la méthode {@link Figure#toString()}
	 * @param figure la figure à tester
	 */
	@ParameterizedTest
	@MethodSource("figuresProvider")
	@DisplayName("toString() : String")
	final void testToString(Figure figure)
	{
		setupTest(figure);
		String testName = new String(testFigureTypeName + ".toString()");
		System.out.println(testName);

		assertEquals(toStringsMap.get(testFigureType),
		             testFigure.toString(),
		             testName);
	}

	/**
	 * Test method for {@link figures.Figure#contains(points.Point2D)}.
	 * @param figure la figure à tester
	 */
	@ParameterizedTest
	@MethodSource("figuresProvider")
	@DisplayName("contains(Point2D p) : boolean")
	final void testContains(Figure figure)
	{
		setupTest(figure);
		String testName = new String(testFigureTypeName + ".contains(Point2D)");
		System.out.println(testName);

		assertTrue(testFigure.contains(insidePoint), testName + " inner point");
		assertFalse(testFigure.contains(outsidePoint), testName + " outer point");
	}

	/**
	 * Test de la méthode {@link figures.Figure#getCenter()}
	 * @param figure la figure à tester
	 */
	@ParameterizedTest
	@MethodSource("figuresProvider")
	@DisplayName("getCenter() : Point2D")
	final void testGetCenter(Figure figure)
	{
		setupTest(figure);
		String testName = new String(testFigureTypeName + ".getCenter()");
		System.out.println(testName);

		assertEquals(centersMap.get(testFigureType),
		             testFigure.getCenter(),
		             testName + " failed with center not equal to expected");
	}

	/**
	 * Test de la méthode {@link Figure#getBoundingBoxCenter()}
	 * @param figure la figure à tester
	 */
	@ParameterizedTest
	@MethodSource("figuresProvider")
	@DisplayName("getBoundingBoxCenter() : Point2D")
	final void testGetBoundingBoxCenter(Figure figure)
	{
		setupTest(figure);
		String testName = new String(testFigureTypeName + ".getBoundingBoxCenter()");
		System.out.println(testName);

		assertEquals(bbCentersMap.get(testFigureType),
		             testFigure.getBoundingBoxCenter(),
		             testName + " failed with bounding box center not equal to expected");
	}

	/**
	 * Test de la méthode {@link Figure#width()}
	 * @param figure la figure à tester
	 */
	@ParameterizedTest
	@MethodSource("figuresProvider")
	@DisplayName("width() : double")
	final void testWidth(Figure figure)
	{
		setupTest(figure);
		String testName = new String(testFigureTypeName + ".width()");
		System.out.println(testName);

		assertEquals(widthMap.get(testFigureType).doubleValue(),
		             testFigure.width(),
		             testName + " failed with width not equal to expected");
	}

	/**
	 * Test de la méthode {@link Figure#height()}
	 * @param figure la figure à tester
	 */
	@ParameterizedTest
	@MethodSource("figuresProvider")
	@DisplayName("height() : double")
	final void testHeight(Figure figure)
	{
		setupTest(figure);
		String testName = new String(testFigureTypeName + ".height()");
		System.out.println(testName);

		assertEquals(heightMap.get(testFigureType).doubleValue(),
		             testFigure.height(),
		             testName + " failed with height not equal to expected");
	}

	/**
	 * Test de la méthode {@link Figure#area()}
	 * @param figure la figure à tester
	 */
	@ParameterizedTest
	@MethodSource("figuresProvider")
	@DisplayName("area() : double")
	final void testArea(Figure figure)
	{
		setupTest(figure);
		String testName = new String(testFigureTypeName + ".area()");
		System.out.println(testName);

		assertEquals(areasMap.get(testFigureType).doubleValue(),
		             testFigure.area(),
		             tolerance,
		             testName + " failed with area not equal to expected ± tolerance");
	}

	/**
	 * Test de la méthode {@link Figure#distanceToCenter(Figure)}
	 * @param figure la figure à tester
	 */
	@ParameterizedTest
	@MethodSource("figuresProvider")
	@DisplayName("distanceToCenter(Figure) : double")
	final void testDistanceToCenterFigure(Figure figure)
	{
		setupTest(figure);
		int testFigureIndex = indexOf(testFigureType);

		Set<Class<? extends Figure>> keySet = figuresMap.keySet();
		for (Iterator<Class<? extends Figure>> figIt = keySet.iterator();
			 figIt.hasNext();)
		{
			Figure currentFigure = figuresMap.get(figIt.next());
			String currentFigureName = currentFigure.getName();
			String testName = new String(testFigureTypeName + ".distanceToCenter("
			    + currentFigureName + ")");
			int currentFigureIndex = indexOf(currentFigure.getClass());
			System.out.println(testName);

			assertEquals(interDistances[testFigureIndex][currentFigureIndex],
			             testFigure.distanceToCenter(currentFigure),
			             tolerance,
			             testName + " failed with distance to center not equal to expected ± tolerance");
		}
	}

	/**
	 * Test de la méthode {@link Figure#distanceToCenter(Figure, Figure)}
	 * @param figure la figure à tester
	 */
	@ParameterizedTest
	@MethodSource("figuresProvider")
	@DisplayName("distanceToCenter(Figure, Figure) : double")
	final void testDistanceToCenterFigureFigure(Figure figure)
	{
		setupTest(figure);
		int testFigureIndex = indexOf(testFigureType);

		Set<Class<? extends Figure>> keySet = figuresMap.keySet();
		for (Iterator<Class<? extends Figure>> figIt = keySet.iterator();
			 figIt.hasNext();)
		{
			Figure currentFigure = figuresMap.get(figIt.next());
			String currentFigureName = currentFigure.getName();
			String testName = new String("Figure.distanceToCenter("
			    + testFigureTypeName + ", " + currentFigureName + ")");
			int currentFigureIndex = indexOf(currentFigure.getClass());
			System.out.println(testName);

			assertEquals(interDistances[testFigureIndex][currentFigureIndex],
			             Figure.distanceToCenter(testFigure, currentFigure),
			             tolerance,
			             testName + " failed with distance to center not equal to expected ± tolerance");
		}
	}

	/**
	 * Test de la méthode {@link Figure#equals(Object)}
	 * @param figure la figure à tester
	 */
	@ParameterizedTest
	@MethodSource("figuresProvider")
	@DisplayName("equals(Object) : boolean")
	final void testEquals(Figure figure)
	{
		setupTest(figure);
		String testName = new String(testFigureTypeName + ".equals(Object)");
		System.out.println(testName);

		// Inégalité avec null
		assertFalse(testFigure.equals(null), testName + " != null");

		// Égalité avec soi même
		assertEquals(testFigure, testFigure, testName + " == this");

		/*
		 * Égalité / Inégalité avec le même ensemble de figures :
		 * 	Références identiques == shallow compare
		 */
		Set<Class<? extends Figure>> keySet = figuresMap.keySet();
		for (Iterator<Class<? extends Figure>> figIt = keySet.iterator();
			 figIt.hasNext();)
		{
			Figure currentFigure = figuresMap.get(figIt.next());
			String currentFigureName = currentFigure.getName();
			testName = new String(testFigureTypeName + "equals("
			    + currentFigureName + ") : self equality");
			if (testFigure.getClass() == currentFigure.getClass())
			{
				assertEquals(testFigure, currentFigure, testName + " failed with not equal figures");
			}
			else
			{
				assertNotEquals(testFigure, currentFigure, testName + "failed");
			}
		}

		/*
		 * Égalité / Inégalité avec l'autre ensemble de figures
		 * 	Références différentes mais contenus identiques == deep compare
		 */
		for (int i = 0; i < altFigures.length; i++)
		{
			Figure currentFigure = altFigures[i];
			if (currentFigure != null)
			{
				String currentFigureName = currentFigure.getName();
				testName = new String(testFigureTypeName + "equals("
				    + currentFigureName + ") : equality with similar");
				if (testFigure.getClass() == currentFigure.getClass())
				{
					assertEquals(testFigure, currentFigure, testName);
				}
				else
				{
					assertNotEquals(testFigure, currentFigure, testName);
				}
			}
		}
	}
}
