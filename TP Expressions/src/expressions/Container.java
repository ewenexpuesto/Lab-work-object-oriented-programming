package expressions;

/**
 * Common Interface for all {@link Expression}s allowing to search for a
 * provided sub {@link Expression}
 * @param <T> the type of {@link Expression} to search for
 */
public interface Container<T extends Expression<?>>
{
	/**
	 * Check for containment of expression "elt"
	 * @param elt the expression to search
	 * @return true if elt can be found in this Container, false otherwise
	 */
	public abstract boolean contains(T elt);
}
