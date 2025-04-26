package collections.lists;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

/**
 * Factory to build a new instance of a {@link List} that can be used in tests
 * @param <E> The type of elements contained in the list to create.
 * @author davidroussel
 */
public class ListFactory<E>
{
	/**
	 * Factory to create a new list based on required type and an optional
	 * content
	 * @param <E> The type of content for the created list
	 * @param listType the kind of list to create
	 * @param content the optional content to add to the created list.
	 * @return a new list of the required type and content
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
	@SuppressWarnings("unchecked")	// because of (List<E>) instance cast
	public static <E> List<E> getList(Class<? extends List<E>> listType,
	                                  Collection<? extends E> content)
	    throws SecurityException,
	    NoSuchMethodException,
	    IllegalArgumentException,
	    InstantiationException,
	    IllegalAccessException,
	    InvocationTargetException
	{
		Constructor<? extends List<E>> constructor = null;
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

		constructor = listType.getConstructor(argumentsTypes);

		if (constructor != null)
		{
			instance = constructor.newInstance(arguments);
		}

		return (List<E>) instance;
	}
}
