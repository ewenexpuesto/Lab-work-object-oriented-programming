package figures;

import points.Point2D;

/**
 * Interface des figures
 * @author davidroussel
 */
public interface Figure
{
	/**
	 * Accesseur en lecture pour le nom de la figure
	 * @return une chaîne contenant le nom de la figure
	 */
	public abstract String getName();

	/**
	 * Méthode abstraite
	 * Déplacement de la figure
	 * @param dx déplacement selon l'axe des X
	 * @param dy déplacement selon l'axe des Y
	 * @return renvoie une référence vers la figure afin que l'on puisse déplacer une
	 * figure en cascade  : <code>f.move(dx,dy).move(dx,dy)</code>
	 * @see Point2D#move(double, double)
	 */
	public abstract Figure move(double dx, double dy);

	/**
	 * Affichage contenu
	 * @return une chaîne de caractère représentant la figure
	 */
	@Override
	public abstract String toString();

	/**
	 * Test de contenu d'un point dans la figure
	 * teste si le point passé en argument est contenu à l'intérieur de la figure
	 * @param p : point candidat à la contenance
	 * @return la contenance du point à l'intérieur de la figure
	 */
	public abstract boolean contains(Point2D p);

	/**
	 * Centre de la figure (au sens du barycentre de la figure)
	 * renvoie le centre de la figure
	 * @return renvoie le point2D central de la figure
	 */
	public abstract Point2D getCenter();

	/**
	 * Centre de la boite englobante de la figure
	 * @return le point correspondant au centre de la boite englobante de la
	 * figure
	 */
	public abstract Point2D getBoundingBoxCenter();

	/**
	 * Largeur de la figure
	 * @return la largeur de la figure
	 */
	public abstract double width();

	/**
	 * Hauteur de la figure
	 * @return la hauteur de la figure
	 */
	public abstract double height();

	/**
	 * Aire couverte par la figure
	 * @return renvoie l'aire couverte par la figure
	 */
	public abstract double area();

	/**
	 * Distance entre les centres de la figure courante et d'une figure
	 * passée en argument
	 * @param f figure avec laquelle on calcule la distance entre les centres
	 * @return la distance entre les points centraux des deux figures
	 * @see #getCenter()
	 * @see Point2D#distance(Point2D, Point2D)
	 */
	public default double distanceToCenter(Figure f)
	{
		// TODO Remplacer par l'implémentation ...
		Point2D center = f.getCenter();
		Point2D center_current = getCenter();
		return Point2D.distance(center,center_current);
	}

	/**
	 * Distance entre les centres de deux figures
	 * @param f1 première figure
	 * @param f2 seconde figure
	 * @return la distance entre les points centraux des deux figures
	 * @see #getCenter()
	 * @see Point2D#distance(Point2D, Point2D)
	 */
	public static double distanceToCenter(Figure f1, Figure f2)
	{
		// TODO Remplacer par l'implémentation ...
		Point2D center1 = f1.getCenter();
		Point2D center2 = f2.getCenter();
		return Point2D.distance(center1,center2);
	}

	/**
	 * Test d'égalité de la figure courante avec une autre figure.
	 * Cette méthode n'implémente que le test sur la nature des figures,
	 * le test sur le contenu doit être réimplémenté dans chaque sous classe,
	 * en utilisant cette méthode pour tester la nature des figures.
	 * @param o la figure dont il faut comparer le contenu.
	 * @return true si les deux figures sont de nature identique et qu'elles ont
	 * le même contenu.
	 * @apiNote A default method cannot override a method from java.lang.Object
	 */
	@Override
	public abstract boolean equals(Object o);

	/**
	 * Le hashCode d'une figure
	 * @return un entier (unique) représentant le code de hachage de la figure
	 * @apiNote A default method cannot override a method from java.lang.Object
	 */
	@Override
	public abstract int hashCode();
}
