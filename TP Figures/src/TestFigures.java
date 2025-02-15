import java.util.ArrayList;
import java.util.Collection;

import figures.Circle;
import figures.Figure;
import points.Point2D;

/**
 * Class de test des Figures
 * @author davidroussel
 */
public class TestFigures
{
	/**
	 * Programme de test des figures
	 * @param args arguments (non utilisés)
	 */
	public static void main (String args[])
	{
		Circle cer, cer2;
// TODO		Rectangle rec, rec2;
// TODO		Triangle tri, tri2;

		// création d'un cercle
// TODO		Point2D centre = new Point2D(0, 0);
// TODO		cer = new Circle(centre, 2);
// TODO		cer2 = new Circle(cer);
// TODO		System.out.println(cer + " == " + cer2 + " ?  : " + cer.equals(cer2));

		// création d'un carré centré en 2.5, 1.5
// TODO		Point2D pmin = new Point2D(2,1);
// TODO		Point2D pmax = new Point2D(3,2);
// TODO		rec = new Rectangle(pmin,pmax);
// TODO		rec2 = new Rectangle(rec);
// TODO		System.out.println(rec + " == " + rec2 + " ?  : " + rec.equals(rec2));

		// création d'un triangle
// TODO		tri = new Triangle();
// TODO		tri2 = new Triangle(tri);
// TODO		System.out.println(tri + " == " + tri2 + " ?  : " + tri.equals(tri2));

		// création d'un polygone
// TODO		Point2D p0 = new Point2D(4,1);
// TODO		Point2D p1 = new Point2D(4,1);
// TODO		Point2D p2 = new Point2D(5,3);
// TODO		Point2D p3 = new Point2D(4,5);
// TODO		Point2D p4 = new Point2D(2,5);
// TODO		Polygon poly = new Polygon(p0,p1,p3,p4);
// TODO		Polygon poly2 = new Polygon(poly);
// TODO		System.out.println(poly + " == " + poly2 + " : "
//				+ (poly.equals(poly2) ? "Ok" : "Ko"));

		ArrayList<Point2D> vp = new ArrayList<Point2D>();
// TODO		vp.add(p0);
// TODO		vp.add(p1);
// TODO		vp.add(p2);
// TODO		vp.add(p3);
// TODO		vp.add(p4);
// TODO		Polygon poly3 = new Polygon(vp);
// TODO		System.out.println(poly + " == " + poly3 + " : "
//				+ (poly.equals(poly3) ? "Ok" : "Ko"));

		// création d'une ligne
// TODO		Point2D pl0 = new Point2D(0,0);
// TODO		Point2D pl1 = new Point2D(1,0);
// TODO		Polygon ligne = new Polygon(pl0,pl1);
// TODO		Polygon ligne2 = new Polygon(ligne);
// TODO		System.out.println(ligne + " == " + ligne2 + " ?  : " + ligne.equals(ligne2));


		// test des différentes méthodes communes au figures
		Collection<Figure> figures= new ArrayList<Figure>();
// TODO		figures.add(cer);
// TODO		figures.add(rec);
// TODO		figures.add(tri);
// TODO		figures.add(poly);
		System.out.println("Ma " + figures);

		// affichage
		for (Figure f : figures)
		{
			System.out.println(f);
		}

		// déplacement
		for (Figure f : figures)
		{
			f.move(1,1);
		}

		// nouvel affichage après déplacement
		for (Figure f : figures)
		{
			System.out.println(f);
		}

		// test de contenu
// TODO		Point2D pcont = new Point2D(2,2);
// TODO		pcont.move(-0.5, -0.75);
// TODO		System.out.println("Test de contenance du point " + pcont);
// TODO		System.out.println("Le point " + pcont + " est : ");
		for (Figure f : figures)
		{
// TODO			afficheContenance(f, pcont);
		}

// TODO		Point2D pcont2 = new Point2D(3,3);
// TODO		System.out.println("Test de contenance du point " + pcont2);
// TODO		System.out.println("Le point " + pcont2 + " est : ");
		for (Figure f : figures)
		{
// TODO			afficheContenance(f, pcont2);
		}

// TODO		Point2D pcont3 = new Point2D(0.5, 0);
// TODO		System.out.println("Test de contenance du point " + pcont3);
// TODO		System.out.println("Le point " + pcont3 + " est : ");
		for (Figure f : figures)
		{
// TODO			afficheContenance(f, pcont3);
		}

		// distance aux centres
		Collection<Figure> figures2 = new ArrayList<Figure>(figures);
		for (Figure f1 : figures)
		{
			for (Figure f2 : figures2)
			{
				afficheDistanceCentres(f1,f2);
			}
		}

		// areas des figures
// TODO		figures.add(ligne);
		for (Figure f : figures)
		{
			afficheAire(f);
		}

		// ajout d'une deuxième occurrence de polygone dans la collection
// TODO		figures.add(poly);
// TODO		System.out.println("Ma " + figures + " avant retrait de " + poly);

		// retrait de toutes les occurrences de poly de la collection
		int count = 0;
// TODO		while (figures.contains(poly))
//		{
//			if (figures.remove(poly))
//			{
//				count++;
//			}
//		}

		// affichage de la collection après retrait des poly
		System.out.print("Ma " + figures + "après retrait : ");
		System.out.println(" " + count + " occurrences supprimées");
	}

	/**
	 * Affichage de la contenance d'un point dans une figure
	 * @param f la figure à tester
	 * @param p le point à tester dans la figure
	 */
	public static void afficheContenance(Figure f, Point2D p)
	{
		if (f.contains(p))
		{
			System.out.println("	dans le " + f.getName());
		}
		else
		{
			System.out.println("	en dehors du " + f.getName());
		}
	}

	/**
	 * Affichage de la distance entre deux figures
	 * @param f1 la première figure
	 * @param f2 la seconde figure
	 */
	public static void afficheDistanceCentres(Figure f1, Figure f2)
	{
		System.out.println("Distance " + f1.getName() +
		                   " -> " + f2.getName() +
		                   " : " + Figure.distanceToCenter(f1,f2));
	}

	/**
	 * Affichage de l'aire d'une figure
	 * @param f la figure
	 */
	public static void afficheAire(Figure f)
	{
		System.out.println("Aire de " + f.getName() + " : " + f.area());
	}
}