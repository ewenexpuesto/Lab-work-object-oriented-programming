package collections.nodes;

import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * List iterator over {@link Node}s
 * @param <E> The type of elements to iterate over
 * @see java.util.LinkedList for inspiration
 */
public class NodeListIterator<E> extends NodeIterator<E> implements ListIterator<E>
{
	/**
	 * Ending node
	 */
	private Node<E> last;

	/**
	 * Number of nodes between {@link Headed#getHead()} and {@link #last}
	 */
	private int size;

	/**
	 * Index of element provided by {@link #next()} call, starting from
	 * {@link Headed#getHead()}
	 * @see #next()
	 */
	private int nextIndex;

	/**
	 * Flag indicating {@link #previous()} has been called
	 */
	private boolean previousCalled;

	/**
	 *  Valued constructor fromhead node holder and index
	 * @param headed the head {@link Node} holder
	 * @param index the index of the node in the list to start from with this
	 * iterator
	 * @throws NullPointerException if the provided head holder is null,
	 * however the head holder can hold a null head
	 * @throws IndexOutOfBoundsException if index is invalid
	 */
	public NodeListIterator(Headed<E> headed, int index)
	    throws NullPointerException,
	    IndexOutOfBoundsException
	{
		/*
		 * Super constructor sets
		 * - next = (index == size) ? null : node(index);
		 * 	CAUTION since there is a new implementation of #node(int) it will be
		 * 	called without proper initialization for #last and will result in
		 * 	invalid #next value.
		 * - lastReturned = null;
		 * - nextCalled = false;
		 * - and throws NullPointerException & IndexOutOfBoundsException
		 */
		super(headed, index);

		// TODO 400 Complete NodeListIterator(Headed<E> headed, int index) ...
		// Compute #size (in the same way as in super(...)) and #last
		// Compute #next node to indexth node (or null if index is already size)
		// with proper #last initialization this time
		// Initialize #nextIndex to index
		// Initialize #previousCalled

	    // Vérification de nullité
		if (headed == null) {
			throw new NullPointerException("Headed object cannot be null.");
		}
	
		// Calcul de la taille de la liste
		size = 0;
		Node<E> current = headed.getHead();
		while (current != null) {
			size++;
			current = current.getNext();
		}
	
		// Vérification de l'index valide
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException("Index out of bounds: " + index);
		}
	
		// Initialisation de #last
		last = null;
		current = headed.getHead();
		
		// Initialisation de #next jusqu'à atteindre l'index spécifié
		for (int i = 0; i < index; i++) {
			last = current;
			current = current.getNext();
		}
	
		next = current;
		nextIndex = index;
		previousCalled = false;
	}

	/**
	 * Valued constructor from head node holder and index
	 * @param headed the head {@link Node} holder
	 */
	public NodeListIterator(Headed<E> headed)
	{
		this(headed, 0);
	}

	/**
	 * Access to node of index index
	 * @param index the index of the desired node
	 * @return the desired node
	 * @implSpec {@link NodeIterator#node(int)} provide a basic implementation
	 * starting from {@link NodeIterator#headed}'s head. Depending on index's
	 * value vs size, search for the desired node might start from either
	 * {@link Headed#getHead()} or {@link #last} node.
	 */
	@Override
	protected Node<E> node(int index)
	{
		// TODO 401 Replace node(int index) with faster implementation
		// If index < size /2 then search forward starting from head of headed
		// Else search backward starting from #last
	
		if (index < size / 2) { // Parcours depuis la tête
			Node<E> current = headed.getHead();
			for (int i = 0; i < index; i++) {
				current = current.getNext();
			}
			return current;
		} else { // Parcours depuis la fin
			Node<E> current = last;
			for (int i = size - 1; i > index; i--) {
				current = current.getPrevious();
			}
			return current;
		}
		//return super.node(index);
	}

	/**
	 * Collapses non-null node x by relating its previous and next nodes.
	 * @param x the node to unlink
	 * @return the value of the collepased node
	 * @implSpec {@link NodeIterator#collapse(Node)} is not enough since
	 * {@link #last} and {@link #size} might be updated if required
	 * @implSpec CAUTION {@link Node#remove()} usage is not appropriate here
	 * since we need to consider cases where head or last nodes should be updated
	 */
	@Override
	protected E collapse(Node<E> x)
	{
		// TODO 402 Complete collapse(Node<E> x)
		// Stores x's data, previous and next nodes

		// If previous is null then we're removing head and
		// new head needs to be set to next
		// Otherwise just relate previous to next

		// If next is null then we're removing the last node and
		// new #last needs to be set to previous
		// Otherwise just relate next to previous (might already be done)

		// Unlink x's previous and next to help Garbage Collector
		// Decrease #size
		// Return removed node data
		if (x == null) {
			throw new IllegalStateException("Cannot remove a null node.");
		}
	
		E element = x.getData();
		Node<E> prev = x.getPrevious();
		Node<E> next = x.getNext();
	
		if (prev == null) { // Suppression de la tête
			headed.setHead(next);
		} else {
			prev.setNext(next);
		}
	
		if (next == null) { // Suppression du dernier élément
			last = prev;
		} else {
			next.setPrevious(prev);
		}
	
		size--;
		return element;
	}

	/**
	 * Creates a new Node with provided value and set it as #last's next node
	 * @param value the element's value to set
	 */
	private void linkLast(E value)
	{
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(value, l, null);
        last = newNode;
        if (l == null)
		{
        	headed.setHead(newNode);
		}
        else	// l != null
		{
			l.setNext(newNode);
		}
        size++;
	}

	/**
	 * Inserts new element node before non-null Node successor.
	 * @param element the element to insert in new node
	 * @param successor the (non null) successor node to insert before
	 * @throws IllegalArgumentException if successor is null
	 */
	private void linkBefore(E element, Node<E> successor)
	    throws IllegalArgumentException
	{
		if (successor == null)
		{
			throw new IllegalArgumentException("null successor");
		}
		final Node<E> pred = successor.getPrevious();
		final Node<E> newNode = new Node<>(element, pred, successor);
		if (pred == null)
		{
			headed.setHead(newNode);
		}
		else
		{
			pred.setNext(newNode);
		}
		size++;
	}

	/**
	 * Returns {@code true} if this list iterator has more elements when
	 * traversing the list in the forward direction. (In other words,
	 * returns {@code true} if {@link #next} would return an element rather
	 * than throwing an exception.)
	 * @return {@code true} if the list iterator has more elements when
	 * traversing the list in the forward direction
	 */
	@Override
	public boolean hasNext()
	{
		return nextIndex < size;
	}

	/**
	 * Returns the next element in the list and advances the cursor position.
	 * This method may be called repeatedly to iterate through the list,
	 * or intermixed with calls to {@link #previous} to go back and forth.
	 * (Note that alternating calls to {@code next} and {@code previous}
	 * will return the same element repeatedly.)
	 * @return the next element in the list
	 * @throws NoSuchElementException if the iteration has no next element
	 * @implSpec Use {@link NodeIterator#next()} first then
	 * update {@link #nextIndex} and {@link #previousCalled}.
	 * @see NodeIterator#next
	 */
	@Override
	public E next() throws NoSuchElementException
	{
		// TODO 403 Complete next()...
		// super.next() is fine but #nextIndex & #previousCalled need to be
		// updated as well
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
	
		lastReturned = next;
		next = next.getNext();
		nextIndex++;
		previousCalled = false;
		return lastReturned.getData();
	}

	/**
	 * Returns {@code true} if this list iterator has more elements when
	 * traversing the list in the reverse direction. (In other words,
	 * returns {@code true} if {@link #previous} would return an element
	 * rather than throwing an exception.)
	 * @return {@code true} if the list iterator has more elements when
	 * traversing the list in the reverse direction
	 */
	@Override
	public boolean hasPrevious()
	{
		return nextIndex > 0;
	}

	/**
	 * Returns the previous element in the list and moves the cursor
	 * position backwards. This method may be called repeatedly to
	 * iterate through the list backwards, or intermixed with calls to
	 * {@link #next} to go back and forth. (Note that alternating calls
	 * to {@code next} and {@code previous} will return the same
	 * element repeatedly.)
	 * @return the value of the previous element in the list
	 * @throws NoSuchElementException if the iteration has no previous
	 * element
	 */
	@Override
	public E previous() throws NoSuchElementException
	{
		// TODO 404 Complete previous()...
		// If there is no previous then throw NoSuchElementException
		// Set #next to its previous or to #last if #next was already null
		// Set #lastReturned to #next
		// Update #nextIndex, #nextCalled and #previousCalled
		// returns #lastReturned's data
		if (!hasPrevious()) {
			throw new NoSuchElementException();
		}
	
		if (next == null) { // Fin de liste, on retourne le dernier élément
			lastReturned = last;
		} else {
			lastReturned = next.getPrevious();
		}
	
		next = lastReturned;
		nextIndex--;
		previousCalled = true;
		return lastReturned.getData();
	}

	/**
	 * Returns the index of the element that would be returned by a
	 * subsequent call to {@link #next}. (Returns list size if the list
	 * iterator is at the end of the list.)
	 * @return the index of the element that would be returned by a
	 * subsequent call to {@code next}, or list size if the list
	 * iterator is at the end of the list
	 */
	@Override
	public int nextIndex()
	{
		return nextIndex;
	}

	/**
	 * Returns the index of the element that would be returned by a
	 * subsequent call to {@link #previous}. (Returns -1 if the list
	 * iterator is at the beginning of the list.)
	 * @return the index of the element that would be returned by a
	 * subsequent call to {@code previous}, or -1 if the list
	 * iterator is at the beginning of the list
	 */
	@Override
	public int previousIndex()
	{
		return nextIndex - 1;
	}

	/**
	 * Removes from the list the last element that was returned by {@link
	 * #next} or {@link #previous} (optional operation). This call can
	 * only be made once per call to {@code next} or {@code previous}.
	 * It can be made only if {@link #add} has not been
	 * called after the last call to {@code next} or {@code previous}.
	 * @throws IllegalStateException if neither {@code next} nor
	 * {@code previous} have been called, or {@code remove} or
	 * {@code add} have been called after the last call to
	 * {@code next} or {@code previous}
	 * @see NodeIterator#remove()
	 */
	@Override
	public void remove() throws IllegalStateException
	{
		// TODO 405 Complete remove()...
		// CAUTION super.remove(); // Impossible since super.remove() modifies lastReturned

		// If neither nextCalled nor previousCalled: throw IllegalStateException
		// If there is no lastReturned to remove: throw IllegalStateException

		// Stores lastReturned's Next before collapsing lastReturned
		// collapse lastReturned node
		// If next was lastReturned then set next to lastReturned's Next
		// Otherwise just decrease nextIndex
		// Update #lastReturned, #nextCalled & #previousCalled
		if (lastReturned == null) {
			throw new IllegalStateException("Cannot remove before calling next() or previous().");
		}
	
		collapse(lastReturned);
	
		if (previousCalled) { // Si `previous()` a été appelé en dernier
			nextIndex--;
			next = lastReturned.getNext();
		} else { // Si `next()` a été appelé en dernier
			next = lastReturned.getNext();
		}
	
		lastReturned = null;
	}

	/**
	 * Replaces the last element returned by {@link #next} or
	 * {@link #previous} with the specified element (optional operation).
	 * This call can be made only if neither {@link #remove} nor {@link
	 * #add} have been called after the last call to {@code next} or
	 * {@code previous}.
	 * @param e the element with which to replace the last element returned by
	 * {@code next} or {@code previous}
	 * @throws ClassCastException if the class of the specified element
	 * prevents it from being added to this list
	 * @throws IllegalArgumentException if some aspect of the specified
	 * element prevents it from being added to this list
	 * @throws IllegalStateException if neither {@code next} nor
	 * {@code previous} have been called, or {@code remove} or
	 * {@code add} have been called after the last call to
	 * {@code next} or {@code previous}
	 */
	@Override
	public void set(E e) throws IllegalStateException, IllegalArgumentException
	{
		// TODO 406 Complete set(E e)...
		// If e is null throw IllegalArgumentException
		// If #lastReturned is null throw IllegalStateException

		// Store #lastReturned's previous and next nodes
		// Create an new "inserted" node with "e" data and #lastReturned previous and next nodes
		// Cleanup #lastReturned to help GC
		// Replace #lastReturned with newly created node
		// If #lastReturned was on head node, new head node needs to be set to "inserted" node
		// If #lastReturned was on #last node, #last needs to be set to "inserted" node
		// If #lastReturned was on #next node, #next needs to be set to "inserted" node
	
		if (e == null) { throw new IllegalArgumentException("Cannot set a null element."); }

			// Si lastReturned est null, lever une IllegalStateException
		if (lastReturned == null) {
			throw new IllegalStateException("Cannot set before calling next() or previous().");
		}

		// Stocker les références aux nœuds précédent et suivant de lastReturned
		Node<E> prev = lastReturned.getPrevious();
		Node<E> next = lastReturned.getNext();

		// Créer un nouveau nœud avec la valeur e et les références aux nœuds précédent et suivant
		Node<E> inserted = new Node<>(e, prev, next);

		// Mettre à jour les références des nœuds adjacents
		if (prev != null) {
			prev.setNext(inserted);
		} else {
			// Si lastReturned était la tête, mettre à jour la tête
			headed.setHead(inserted);
		}

		if (next != null) {
			next.setPrevious(inserted);
		} else {
			// Si lastReturned était le dernier élément, mettre à jour last
			last = inserted;
		}

		// Mettre à jour lastReturned avec le nouveau nœud
		lastReturned = inserted;
	}

	/**
	 * Inserts the specified element into the list (optional operation).
	 * The element is inserted immediately before the element that
	 * would be returned by {@link #next}, if any, and after the element
	 * that would be returned by {@link #previous}, if any. (If the
	 * list contains no elements, the new element becomes the sole element
	 * on the list.) The new element is inserted before the implicit
	 * cursor: a subsequent call to {@code next} would be unaffected, and a
	 * subsequent call to {@code previous} would return the new element.
	 * (This call increases by one the value that would be returned by a
	 * call to {@code nextIndex} or {@code previousIndex}.)
	 * @param e the element to insert
	 * @throws ClassCastException if the class of the specified element
	 * prevents it from being added to this list
	 * @throws IllegalArgumentException if some aspect of this element
	 * prevents it from being added to this list
	 * @see #linkLast(Object)
	 * @see #linkBefore(Object, Node)
	 */
	@Override
	public void add(E e) throws IllegalArgumentException
	{
		// TODO 407 Complete add(E e)...
		// If e is null then throw IllegalArgumentException

		// If #next is already null then add new node with "e" value after
		// #last node with #linkLast utility method
		// Otherwise insert new node with "e" value before #next node
		// with #linkBefore utility method
		// Update #nextIndex
		if (e == null) {
			throw new NullPointerException("Element cannot be null.");
		}
		Node<E> newNode = new Node<>(e);

		if (next == null) { // Ajout à la fin de la liste
			if (last == null) { // Liste vide
				headed.setHead(newNode);
			} else {
				linkLast(e);
			}
			last = newNode;
		} else { // Ajout avant `next`
			linkBefore(e, newNode);
		}
	
		size++;
		nextIndex++;
		lastReturned = null; // Annule toute suppression possible
	}

	/**
	 * Performs the given action for each remaining element until all elements
	 * have been processed or the action throws an exception. Actions are
	 * performed in the order of iteration, if that order is specified.
	 * Exceptions thrown by the action are relayed to the caller.
	 * @implSpec
	 * <p>
	 * The default implementation behaves as if:
	 *
	 * <pre>{@code
	 *     while (hasNext())
	 *         action.accept(next());
	 * }</pre>
	 *
	 * @param action The action to be performed for each element
	 * @throws NullPointerException if the provided action is null
	 * @since 1.8
	 */
	@Override
	public void forEachRemaining(Consumer<? super E> action)
	{
		Objects.requireNonNull(action);
		while (nextIndex < size)
		{
			action.accept(next.getData());
			lastReturned = next;
			next = next.getNext();
			nextIndex++;
		}
	}

	// TODO 408 Run tests/NodeListIteratorTest
	// TODO 409 Run tests/ListIteratorTest
}
