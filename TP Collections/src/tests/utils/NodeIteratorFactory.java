package tests.utils;

import java.util.Collection;
import java.util.Iterator;

import collections.nodes.Node;
import collections.nodes.NodeIterator;

/**
 * A Factory to obtain {@link NodeIterator}s
 * @param <E> The type of elements to iterate over
 */
public class NodeIteratorFactory<E> implements IteratorFactory<E>
{
	/**
	 * Factory method to obtain an empty {@link Iterator}.
	 * @return an empty {@link Iterator}
	 */
	@Override
	public Iterator<E> emptyIterator()
	{
		return new NodeIterator<>(new HeadHolder<>(null));
	}

	/**
	 * Factory method to obtain an {@link Iterator} iterating over a single
	 * element.
	 * @param element the single element to iterate over
	 * @return a {@link Iterator} on a single element
	 */
	@Override
	public Iterator<E> singleIterator(E element)
	{
		Node<E> singleNode = new Node<>(element);
		return new NodeIterator<>(new HeadHolder<>(singleNode), 0);
	}

	/**
	 * Factory method to obtain an {@link Iterator} iterating over multiple
	 * elements.
	 * @param elements the list of elements to iterate over
	 * @return a {@link Iterator} on multiples elements
	 */
	@Override
	public Iterator<E> multipleIterator(Collection<E> elements)
	{
		Iterator<E> it = elements.iterator();
		Node<E> headNode = new Node<>(it.next());
		Node<E> current = headNode;
		while (it.hasNext())
		{
			current.setNext(new Node<>(it.next()));
			current = current.getNext();
		}
		return new NodeIterator<>(new HeadHolder<>(headNode), 0);
	}
}
