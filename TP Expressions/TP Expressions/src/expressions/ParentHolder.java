package expressions;

/**
 * Common Interface for all classes containing parent/child relationships
 * @param <T> the type of parent
 */
public interface ParentHolder<T extends ParentHolder<T>>
{
	/**
	 * Accessor to parent (if any)
	 * @return a reference to the parent or null if there is no parent
	 */
	public abstract T getParent();

	/**
	 * Set new parent.
	 * @param parent The parent to set
	 * @throws IllegalArgumentException if the provided parent is not a legal
	 * parent.
	 */
	public abstract void setParent(T parent) throws IllegalArgumentException;
}
