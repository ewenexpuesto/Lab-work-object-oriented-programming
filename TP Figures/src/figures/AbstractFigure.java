package figures;

import points.Point2D;

/**
 * Classe abstraite contenant juste
 * <ul>
 * 	<li>Un nom</li>
 * 	<li>une implémentation partielle de l'interface {@link Figure}.</li>
 * </ul>
 * @author David Roussel
 */
public abstract class AbstractFigure implements Figure
{
	/**
	 * Nom de la figure
	 */
	protected String name;

	/**
	 * Constructeur (protégé) par défaut.
	 * Affecte le nom de la classe comme nom de figure
	 * @see Class#getSimpleName()
	 */
	protected AbstractFigure()
	{
		name = getClass().getSimpleName();
		// this(getClass().getSimpleName()); // Impossible d'appeler une méthode d'instance sur une instance pas encore instanciée !!!
	}

	/**
	 * Constructeur (protégé) avec un nom
	 * on a fait exprès de ne pas mettre de constructeur sans arguments
	 * @param aName Chaîne de caractère pour initialiser le nom de la
	 * figure
	 */
	protected AbstractFigure(String aName)
	{
		name = aName;
	}

	/**
	 * @return le nom
	 * @see figures.Figure#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see figures.Figure#move(double, double)
	 */
	@Override
	public abstract Figure move(double dx, double dy);

	/*
	 * (non-Javadoc)
	 * @see figures.Figure#toString()
	 * @implSpec "name : "
	 */
	@Override
	public String toString()
	{
		return (name + " : ");
	}

	/*
	 * (non-Javadoc)
	 * @see figures.Figure#contient(points.Point2D)
	 */
	@Override
	public abstract boolean contains(Point2D p);

	/*
	 * (non-Javadoc)
	 * @see figures.Figure#getCentre()
	 */
	@Override
	public abstract Point2D getCenter();

	/*
	 * (non-Javadoc)
	 * @see figures.Figure#aire()
	 */
	@Override
	public abstract double area();

	/**
	 * Comparaison de deux figures en termes de contenu
	 * @param f la figure dont on veut tester l'égalité avec soi-même
	 * @return true si f est du même types que la figure courante et qu'elles
	 * ont un contenu identique
	 */
	protected abstract boolean equals(Figure f);

	/**
	 * Comparaison de deux figures, on ne peut pas vérifier grand chose pour
	 * l'instant à part la classe et le nom
	 * @implNote implémentation partielle qui ne vérifie que null/this/et l'égalité
	 * de classe
	 * @see figures.Figure#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		// TODO Remplacer par l'implémentation utilisant la méthode ci-dessus...
		return false;
	}

	/**
	 * Code de hachage d'une figure (implémentation partielle basée sur le nom d'une
	 * figure)
	 * @see java.lang.Object#hashCode()
	 * @apiNote Non utilisé
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		return result;
	}
}
