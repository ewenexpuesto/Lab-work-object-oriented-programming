package figures;

import points.Point2D;

/**
 * Classe cercle héritière de la classe abstraite Figure doit donc implémenter
 * (entre-autres) les méthodes abstraites suivantes
 * @see AbstractFigure#move
 * @see AbstractFigure#contains
 * @see AbstractFigure#getCenter
 * @see AbstractFigure#area
 */
public class Circle extends AbstractFigure
{
	/**
	 * Centre
	 */
	protected Point2D center;

	/**
	 * Rayon
	 */
	protected double radius;

	// Constructeurs --------------------------------------------------------
	/**
	 * Constructeur par défaut.
	 * Crée un cercle de centre (0, 0) et de rayon 1.
	 */
	public Circle()
	{
		// super() est implicite
		// TODO Compléter ...
	}

	/**
	 * Constructeur valué : position + rayon
	 * @param x abscisse
	 * @param y ordonnée
	 * @param r rayon
	 */
	public Circle(double x, double y, double r)
	{
		// TODO Compléter ...
	}

	/**
	 * constructeur valué : position + rayon
	 * @param p Point central
	 * @param r rayon
	 */
	public Circle(Point2D p, double r)
	{
		// TODO Compléter ...
	}

	/**
	 * Constructeur de copie à partir d'un autre cercle
	 * @param c le Cercle à copier
	 */
	public Circle(Circle c)
	{
		// TODO Compléter ...
	}

	// Accesseurs -----------------------------------------------------------
	/**
	 * Accesseur en lecture pour le centre
	 * @return le point central du cercle
	 */
	@Override
	public Point2D getCenter()
	{
		// TODO Remplacer par l'implémentation ...
		return new Point2D(0.0, 0.0);
	}

	/**
	 * Accesseur en lecture du centre de la boite englobante
	 * @return le point central du cercle
	 * @see figures.Figure#getBoundingBoxCenter()
	 */
	@Override
	public Point2D getBoundingBoxCenter()
	{
		// TODO Remplacer par l'implémentation ...
		return new Point2D(0.0, 0.0);
	}

	/**
	 * Accesseur en lecture pour le rayon
	 * @return le rayon du cercle
	 */
	public double getRadius()
	{
		return radius;
	}

	/**
	 * Accesseur en écriture pour le rayon
	 * @param r rayon du cercle : si r est négatif le rayon est alors mis à 0.0
	 */
	public void setRadius(double r)
	{
		if (r < 0.0)
		{
			radius = 0.0;
		}
		else
		{
			radius = r;
		}
	}

	/**
	 * Déplacement du cercle = déplacement du centre
	 * @param dx déplacement suivant x
	 * @param dy déplacement suivant y
	 * @return une référence vers la figure déplacée
	 */
	@Override
	public Figure move(double dx, double dy)
	{
		// TODO Compléter ...
		return this;
	}

	/**
	 * Affichage contenu
	 * @return une chaîne représentant l'objet (centre + rayon)
	 * @implSpec "name : center point, r = radius"
	 */
	@Override
	public String toString()
	{
		return new String(/* TODO Compléter ... */);
	}

	/**
	 * Test de contenu : teste si le point passé en argument est contenu à
	 * l'intérieur du cercle
	 * @param p point à tester
	 * @return une valeur booléenne indiquent si le point est contenu ou pas à
	 * l'intérieur du cercle
	 */
	@Override
	public boolean contains(Point2D p)
	{
		// TODO Remplacer par l'implémentation
		return false;
	}

	/**
	 * Largeur du cercle
	 * @return 2 x le rayon du cercle
	 */
	@Override
	public double width()
	{
		// TODO Remplacer par l'implémentation
		return 0.0;
	}

	/**
	 * Hauteur du cercle
	 * @return 2 x le rayon du cercle
	 */
	@Override
	public double height()
	{
		// TODO Remplacer par l'implémentation
		return 0.0;
	}

	/**
	 * Aire
	 * @return renvoie l'aire couverte par le cercle
	 */
	@Override
	public double area()
	{
		// TODO Remplacer par l'implémentation
		return 0.0;
	}

	/**
	 * Comparaison de deux figures en termes de contenu
	 * @param figure la figure dont on veut tester l'égalité avec soi-même
	 * @return true si f est du même types que la figure courante et qu'elles
	 * ont un contenu identique
	 */
	@Override
	protected boolean equals(Figure figure)
	{
		// TODO Remplacer par l'implémentation
		return false;
	}

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = (prime * result) + ((center == null) ? 0 : center.hashCode());
        long temp;
        temp = Double.doubleToLongBits(radius);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
