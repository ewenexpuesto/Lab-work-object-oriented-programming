package tests.utils;

import collections.nodes.Headed;
import collections.nodes.Node;
import collections.nodes.NodeIterator;

/**
 * Head holder containing a head {@link Node} and used to build
 * {@link NodeIterator}s without
 * {@link java.util.Collection}s or {@link java.util.List}s
 * @param <E> The data type
 */
public class HeadHolder<E> implements Headed<E>
{
	/**
	 * Head Node
	 */
	Node<E> head = null;

	/**
	 * Valued constructor
	 * @param node the head node
	 */
	public HeadHolder(Node<E> node)
	{
		head = node;
	}

	@Override
	public Node<E> getHead()
	{
		return head;
	}

	@Override
	public void setHead(Node<E> head)
	{
		this.head = head;
	}
}
