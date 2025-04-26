package tests.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import collections.nodes.Node;
import collections.nodes.NodeListIterator;

/**
 * A Factory to obtain {@link NodeListIterator}s
 * @param <E> The type of elements to iterate over
 */
public class NodeListIteratorFactory<E> extends NodeIteratorFactory<E>
    implements ListIteratorFactory<E>
{
	/**
	 * Factory method to obtain an empty {@link Iterator}.
	 * @return an empty {@link Iterator}
	 */
	@Override
	public Iterator<E> emptyIterator()
	{
		return new NodeListIterator<>(new HeadHolder<>(null));
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
		return new NodeListIterator<>(new HeadHolder<>(singleNode), 0);
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
		return new NodeListIterator<>(new HeadHolder<>(headNode), 0);
	}

	/**
	 * Factory method to obtain an empty {@link ListIterator}.
	 * @return an empty {@link ListIterator}
	 */
	@Override
	public ListIterator<E> emptyListIterator()
	{
		return (ListIterator<E>)emptyIterator();
	}

	/**
	 * Factory method to obtain an {@link ListIterator} iterating over a single
	 * element.
	 * @param element the single element to iterate over
	 * @return a {@link ListIterator} on a single element
	 */
	@Override
	public ListIterator<E> singleListIterator(E element)
	{
		return (ListIterator<E>)singleIterator(element);
	}

	/**
	 * Factory method to obtain an {@link ListIterator} iterating over multiple
	 * elements.
	 * @param elements the list of elements to iterate over
	 * @return a {@link ListIterator} on multiples elements
	 */
	@Override
	public ListIterator<E> multipleListIterator(Collection<E> elements)
	{
		return (ListIterator<E>)multipleIterator(elements);
	}
}
