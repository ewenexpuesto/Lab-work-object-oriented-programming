package tests;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * Factory to build a new instance of a {@link Collection} that can be used in tests
 * @param <E> The type of elements contained in the collection to create.
 * @author davidroussel
 */
public class CollectionFactory<E>
{
	/**
	 * Factory to create a new collection based on required type and an optional
	 * content
	 * @param <E> The type of content for the created collection
	 * @param collectionType type of collection to create: either
	 * {@link collections.NodeCollection} or ...
	 * @param content the optional content to add to the created collection.
	 * @return a new collection of the required type and content
	 * @throws SecurityException if the security manager doesn't allow access to
	 * the required constructor
	 * @throws NoSuchMethodException if the required constructor doesn't exist
	 * @throws IllegalArgumentException if the number of arguments provided to
	 * the constructor is wrong
	 * @throws InstantiationException if the required class is abstract
	 * @throws IllegalAccessException if the required constructor is
	 * inaccessible
	 * @throws InvocationTargetException if the invoked constructor raises an
	 * exception
	 */
	@SuppressWarnings("unchecked")
	public static <E> Collection<E>
	    getCollection(Class<? extends Collection<E>> collectionType,
	                  Collection<? extends E> content) throws
	    	SecurityException,
		    NoSuchMethodException,
		    IllegalArgumentException,
		    InstantiationException,
		    IllegalAccessException,
		    InvocationTargetException
	{
		Constructor<? extends Collection<E>> constructor = null;
		Class<?>[] argumentsTypes = null;
		Object[] arguments = null;
		Object instance = null;

		if (content == null)
		{
			argumentsTypes = new Class<?>[0];
			arguments = new Object[0];
		}
		else
		{
			argumentsTypes = new Class<?>[1];
			argumentsTypes[0] = Collection.class;
			arguments = new Object[1];
			arguments[0] = content;
		}

		constructor = collectionType.getConstructor(argumentsTypes);

		if (constructor != null)
		{
			instance = constructor.newInstance(arguments);
		}

		return (Collection<E>) instance;
	}
}
