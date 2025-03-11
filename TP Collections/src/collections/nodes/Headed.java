package collections.nodes;

/**
 * An interface used by all classes using a head {@link Node}
 * @param <E> the type of content of a head node.
 */
public interface Headed<E>
{
	/**
	 * Accessor to the head node
	 * @return the head node
	 */
	public abstract Node<E> getHead();

	/**
	 * Mutator of the head node
	 * @param head the new head to set
	 */
	public abstract void setHead(Node<E> head);
}
