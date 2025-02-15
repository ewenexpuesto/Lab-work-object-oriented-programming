package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Class to test other classes content
 * @author davidroussel
 */
public class ContentChecker
{
	/**
	 * Assert a single public constructor with specified arguments
	 * @param testName the name of the test (to appear in assertion messages)
	 * @param sourceClass the class under test
	 * @param constructorArguments the classes of expected arguments of the
	 * constructor to assess
	 * @return true if the constructor have been found, false otherwise
	 */
	public static boolean assertConstructor(String testName,
	                                        Class<?> sourceClass,
	                                        Class<?>[] constructorArguments)
	{
		try
		{
			Constructor<?> constructor = sourceClass.getConstructor(constructorArguments);
			return constructor != null;
		}
		catch (NoSuchMethodException | SecurityException e)
		{
			// Constructor hasn't been found or is inaccessible
			StringBuilder signature = new StringBuilder();
			signature.append(" public ");
			signature.append(sourceClass.getSimpleName());
			signature.append('(');
			if (constructorArguments != null)
			{
				for (int i = 0; i < constructorArguments.length; i++)
				{
					signature.append(constructorArguments[i].getSimpleName());
					if (i < (constructorArguments.length - 1))
					{
						signature.append(", ");
					}
				}
			}
			signature.append(") is missing");
			fail(testName + signature.toString());
			return false;
		}
	}

	/**
	 * Assert All constructors in #sourceClass with arguments provided in
	 * #constructorsArguments
	 * @param testName the name of the test (to appear in assertion messages)
	 * @param sourceClass the class under test
	 * @param constructorsArguments The argument of the constructor to search
	 * for in the source class
	 * @return true if all searched for constructors have been found in the
	 * source class, and false otherwise
	 * @see java.lang.reflect.Modifier
	 */
	public static boolean assertConstructors(String testName,
	                                         Class<?> sourceClass,
	                                         Class<?>[][] constructorsArguments)

	{
		Constructor<?>[] constructors = sourceClass.getConstructors();
		for (int i = 0; i < constructorsArguments.length; i++)
		{
			Class<?>[] expectedConstructorArguments = constructorsArguments[i];
			boolean checked = false;
			for (Constructor<?> c : constructors)
			{
				if (Arrays.equals(c.getParameterTypes(), expectedConstructorArguments))
				{
					checked = true;
					break;
				}
			}
			if (!checked)
			{
				StringBuilder constructorSignature = new StringBuilder();
				constructorSignature.append("public ");
				constructorSignature.append(sourceClass.getSimpleName());
				constructorSignature.append('(');
				for (int j = 0; j < expectedConstructorArguments.length; j++)
				{
					constructorSignature.append(expectedConstructorArguments[j].getSimpleName());
					if (j < (expectedConstructorArguments.length - 1))
					{
						constructorSignature.append(", ");
					}
				}
				constructorSignature.append(')');
				fail(testName + " constructor " + constructorSignature.toString() + " is missing");
				return false;
			}
		}
		return true;
	}

	/**
	 * Assert a single public method
	 * @param testName The name of the test (for messages purposes)
	 * @param sourceClass the class under test
	 * @param methodName the name of the method
	 * @param methodArguments the arguments of the method
	 * @param isStatic flag indicating the method should be static
	 * @return true if such a method has been found in sourceClass, false
	 * otherwise
	 */
	public static boolean assertMethod(String testName,
	                                   Class<?> sourceClass,
	                                   String methodName,
	                                   Class<?>[] methodArguments,
	                                   boolean isStatic)
	{
		try
		{
			Method method = sourceClass.getMethod(methodName, methodArguments);
			int expectedModifiers = Modifier.PUBLIC;
			if (isStatic)
			{
				expectedModifiers |= Modifier.STATIC;
			}
			int modifiers = method.getModifiers();
			return (method != null) && (modifiers == expectedModifiers);
		}
		catch (NoSuchMethodException | SecurityException e)
		{
			// Method hasn't been found or is inaccessible
			StringBuilder signature = new StringBuilder();
			signature.append(" public ");
			if (isStatic)
			{
				signature.append("static ");
			}
			signature.append(sourceClass.getSimpleName());
			signature.append('.');
			signature.append(methodName);
			signature.append('(');
			if (methodArguments != null)
			{
				for (int i = 0; i < methodArguments.length; i++)
				{
					signature.append(methodArguments[i].getSimpleName());
					if (i < (methodArguments.length - 1))
					{
						signature.append(", ");
					}
				}
			}
			signature.append(") is missing");
			fail(testName + signature.toString());
			return false;
		}
	}

	/**
	 * Assert Methods in #sourceClass with names provided by #methodsNames
	 * featuring
	 * #methodsArguments and #methodsReturns
	 * @param testName The name of the test (for messages purposes)
	 * @param sourceClass the class under test
	 * @param methodsNames the expected methods names to assess
	 * @param methodsArguments the expected methods arguments
	 * @param methodsReturns the expected methods return types
	 * @param methodsModifiers (iff non null) the expected modifiers of the
	 * methods as determined with {@link java.lang.reflect.Modifier}
	 * @return true if all methods described in arguments have been found in
	 * #sourceClass, and false otherwise
	 * @see java.lang.reflect.Modifier
	 */
	public static boolean assertMethods(String testName,
	                                    Class<?> sourceClass,
	                                    String[] methodsNames,
	                                    Class<?>[][] methodsArguments,
	                                    Class<?>[] methodsReturns,
	                                    int[] methodsModifiers)
	{
		/*
		 * Methods assessments
		 */
		boolean[] checked = new boolean[methodsNames.length];
		for (int i = 0; i < checked.length; i++)
		{
			checked[i] = false;
		}

		assertEquals(methodsNames.length,
		             methodsArguments.length,
		             testName + " unexpected methods arguments length");
		assertEquals(methodsNames.length,
		             methodsArguments.length,
		             testName + " unexpected methods returns length");

		/*
		 * The testClass might provide a lot of methods (including inherited ones)
		 */
		Method[] providedMethods = sourceClass.getMethods();

		/*
		 * Check only expected methods
		 */
		for (int i = 0; i < methodsNames.length; i++)
		{
			String expectedMethodName = methodsNames[i];
			Class<?>[] expectedMethodArguments = methodsArguments[i];
			Class<?>expectedMethodReturn = methodsReturns[i];
			for (Method method : providedMethods)
			{
				if (!method.getName().equals(expectedMethodName))
				{
					continue;
				}
				if (method.getReturnType() != expectedMethodReturn)
				{
					continue;
				}
				if (methodsModifiers != null)
				{
					if (method.getModifiers() != methodsModifiers[i])
					{
						continue;
					}
				}
				if (method.getParameterCount() != expectedMethodArguments.length)
				{
					continue;
				}
				if (Arrays.equals(method.getParameterTypes(), expectedMethodArguments))
				{
					checked[i] = true;
					break;
				}
			}
		}

		for (int i = 0; i < checked.length; i++)
		{
			if (checked[i])
			{
				continue;
			}

			StringBuilder methodSignature = new StringBuilder();
			if (methodsModifiers != null)
			{
				if (Modifier.isPublic(methodsModifiers[i]))
				{
					methodSignature.append("public ");
				}
				if (Modifier.isProtected(methodsModifiers[i]))
				{
					methodSignature.append("protected ");
				}
				if (Modifier.isPrivate(methodsModifiers[i]))
				{
					methodSignature.append("private ");
				}
				if (Modifier.isStatic(methodsModifiers[i]))
				{
					methodSignature.append("static ");
				}
			}
			else
			{
				methodSignature.append("public ");
			}
			methodSignature.append(methodsReturns[i].getSimpleName());
			methodSignature.append(" ");
			methodSignature.append(methodsNames[i]);
			methodSignature.append("(");
			for (int j = 0; j < methodsArguments[i].length; j++)
			{
				methodSignature.append(methodsArguments[i][j].getSimpleName());
				if (j < (methodsArguments[i].length - 1))
				{
					methodSignature.append(", ");
				}
			}
			methodSignature.append(")");
			fail(testName + " method " + methodSignature.toString() + " is missing");
			return false;
		}
		return true;
	}

	/**
	 * Assert Tested class contents (constructors and methods)
	 * @param testName The name of the test (for messages purposes)
	 * @param sourceClass the class under test
	 * @param constructorsArguments the expected constructors arguments
	 * @param methodsNames the expected methods names to assess
	 * @param methodsArguments the expected methods arguments
	 * @param methodsReturns the expected methods return types
	 * @param methodsModifiers (iff non null) the expected modifiers of the
	 * methods as determined with {@link java.lang.reflect.Modifier}
	 * @return true if all expected elements have been assessed, false otherwise
	 */
	public static boolean assertContent(String testName,
	                                    Class<?> sourceClass,
	                                    Class<?>[][] constructorsArguments,
	                                    String[] methodsNames,
	                                    Class<?>[][] methodsArguments,
	                                    Class<?>[] methodsReturns,
	                                    int[] methodsModifiers)
	{
		boolean result = assertConstructors(testName,
		                                    sourceClass,
		                                    constructorsArguments);

		if (result)
		{
			result = assertMethods(testName,
			                       sourceClass,
			                       methodsNames,
			                       methodsArguments,
			                       methodsReturns,
			                       methodsModifiers);
		}
		return result;
	}
}
