package points;

/**
 * Classe définissant un point du plan 2D
 * @author David Roussel
 */
public class Point2D
{
	// attributs d'instance --------------------------------------------------
	/**
	 * l’abscisse du point
	 */
	protected double x;
	/**
	 * l'ordonnée du point
	 */
	protected double y;

	// attributs de classe ---------------------------------------------------
	/**
	 * Compteur d'instances : le nombre de points actuellement instanciés
	 */
	protected static int nbPoints = 0;

	/**
	 * Constante servant à comparer deux points entre eux (à {@link #epsilon}
	 * près). On comparera alors la distance entre deux points.
	 * @see #distance(Point2D)
	 * @see #distance(Point2D, Point2D)
	 */
	protected static final double epsilon = 1e-6;

	/*
	 * Constructeurs
	 */

	/**
	 * TODO Constructeur valué
	 * @param x l’abscisse du point à créer
	 * @param y l'ordonnée du point à créer
	 */
	public Point2D (double x0, double y0)
	{
		this.x = x0;
		this.y = y0;
		Point2D.nbPoints = nbPoints + 1;
	}

	/**
	 * TODO Constructeur par défaut. Initialise un point à l'origine du repère [0.0,
	 * 0.0]
	 */
	public Point2D()
	{
		this.x = 0;
		this.y = 0;
		Point2D.nbPoints += 1;
	}

	/**
	 * TODO Constructeur de copie
	 * @param p le point dont il faut copier les coordonnées Il s'agit ici d'une
	 * copie profonde de manière à créer une nouvelle instance
	 * possédant les même caractéristiques que celle dont on copie
	 * les coordonnées.
	 */
	public Point2D(Point2D p)
	{
		this(p.x, p.y);
	}

	/**
	 * Nettoyeur avant destruction Permet de décrémenter le compteur d'instances
	 */
	@Override
	protected void finalize()
	{
		nbPoints--;
	}

	/*
	 * TODO Accesseurs : Source > Generate Getters and Setters ...
	 * 	- get[X|Y]
	 * 	- set[X|Y]
	 * 	- getEpsilon
	 * 	- getNbPoints
	 */
	// Getters
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public static double getEpsilon() {
		return epsilon;
	}
	public static int getNbPoints() {
		return nbPoints;
	}
	// Setters
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Opérations sur un point
	 * @param dx le déplacement en x
	 * @param dy le déplacement en y
	 * @return renvoie la référence vers l'instance courante (this) de manière à
	 * pouvoir enchaîner les traitements du style :
	 * unObjet.uneMéthode(monPoint.move(dx,dy))
	 */
	public Point2D move(double dx, double dy)
	{
		x += dx;
		y += dy;
		return this;
	}

	/**
	 * TODO Calcul de la distance 2D entre deux points. Cette distance ne concerne
	 * pas plus un point que l'autre c'est pourquoi on en fait une méthode de
	 * classe.
	 * @param p1 le premier point
	 * @param p2 le seconde point
	 * @return la distance entre les points p1 et p2
	 * @see #distance(Point2D)
	 */
	public static double distance(Point2D p1, Point2D p2) {
		double dx = p2.x - p1.x;
		double dy = p2.y - p1.y;
		return Math.sqrt(dx * dx + dy * dy);
	}
	/**
	 * Use case
	 * Point2D p1 = new Point2D(1, 2);
	 * Point2D p2 = new Point2D(4, 6);
	 * double d = Point2D.distance(p1, p2);
	 * System.out.println("Distance : " + d); // Résultat ≈ 5.0
	 */

	/**
	 * TODO Calcul de distance 2D par rapport au point courant
	 * @param p l'autre point dont on veut calculer la distance
	 * @return la distance entre le point courant et le point p
	 * @see #distance(Point2D, Point2D)
	 */
	public double distance(Point2D p) {
		return Point2D.distance(this, p);
	}
	/**
	 * Use case
	 * Point2D p1 = new Point2D(1, 2);
	 * Point2D p2 = new Point2D(4, 6);
	 * double d = p1.distance(p2);  // Appelle la méthode d'instance
	 * System.out.println("Distance : " + d); // Résultat ≈ 5.0
	 */

	/*
	 * TODO Affichage contenu et test d'égalité
	 * 	- toString : "x = <value> y = <value>"
	 * 	- equals avec Point2D (protected) puis avec Object (public)
	 * 	- Source > Override/Implements Methods ...
	 */
	// toString est une méthode classique en Java, elle est présente
	// dans les objets de type Object, on pourra donc ainsi l'utiliser
	// dans une éventuelle Liste de points.
	@Override
	public String toString() {
	return "x = " + x + ", y = " + y;
	}
	/**
	 * Use case
	 * Point2D p = new Point2D(3.5, 4.2);
	 * System.out.println(p); // Affiche : x = 3.5, y = 4.2
	 */
	protected boolean equals(Point2D p) {
		return Math.abs(this.x - p.x) < epsilon && Math.abs(this.y - p.y) < epsilon;
	}
	@Override
	public boolean equals(Object obj) {
	if (this == obj) return true; // Même instance
	if (obj == null || getClass() != obj.getClass()) return false; // Pas un Point2D
	return equals((Point2D) obj); // Appelle la version protégée
	}
}
