/**
 *
 */
package tests.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

/**
 * A random word supplier based on a list of possible words
 */
public class WordSupplier implements Supplier<String>
{
	/**
	 * Words provided by this supplier
	 */
	private String[] words;

	/**
	 * Index of the current word to provide
	 * @implNote Whenever index >= words.length elements are considered exhausted
	 */
	private int index;

	/**
	 * Flag indicating index has to be reset to 0 automatically when
	 * elements are exhausted.
	 */
	private boolean autoReset;

	/**
	 * Default constructor
	 */
	public WordSupplier()
	{
		this(new String[] { "Lorem", "ipsum", "odor", "amet", "consectetuer",
	        "adipiscing", "elit", "Taciti", "placerat", "consequat", "mi",
	        "tellus", "vitae", "scelerisque", "diam", "tincidunt", "Varius",
	        "fames", "tempus", "luctus", "nec", "litora", "mauris" });
	}

	/**
	 * Constructor from a list of possible words
	 * @param words the words to choose from
	 */
	public WordSupplier(String[] words)
	{
		index = 0;
		List<String> wordList = Arrays.asList(words);
		Collections.shuffle(wordList);
		this.words = wordList.toArray(new String[words.length]);
		autoReset = false;
	}

	/**
	 * Reads a word list from file and returns a words array
	 * @param path the path to the file containing one word per line
	 * @return an array containing the words read from file
	 * @throws FileNotFoundException if file can't be found
	 */
	private static String[] readWordList(String path) throws FileNotFoundException
	{
		File file = new File(path);
		Scanner scanner = new Scanner(file);
		Set<String> wordSet = new TreeSet<>();
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			if (!line.isEmpty())
			{
				wordSet.add(line);
			}
		}
		scanner.close();
		return wordSet.toArray(new String[wordSet.size()]);
	}

	/**
	 * Constructor from file path
	 * @param path the path to the file containing one word per line
	 * @throws FileNotFoundException if file can't be found
	 */
	public WordSupplier(String path) throws FileNotFoundException
	{
		this(readWordList(path));
	}

	/**
	 * Obtain next word;
	 * @return the next random word
	 * @throws NoSuchElementException whenever elements are exhausted which evt requires a reset
	 */
	@Override
	public String get() throws NoSuchElementException
	{
		if ((index >= words.length) && !autoReset)
		{
			throw new NoSuchElementException("get(): elements exhausted");
		}
		index++;
		if (autoReset && (index == words.length))
		{
			index = 0;
		}
		return words[index];
	}

	/**
	 * Reset elements exhausted status
	 */
	public void reset()
	{
		index = 0;
	}

	/**
	 * Get {@link #autoReset} status
	 * @return the auto reset status
	 */
	public boolean isAutoReset()
	{
		return autoReset;
	}

	/**
	 * Set {@link #autoReset} status
	 * @param status the new auto reset status
	 */
	public void setAutoReset(boolean status)
	{
		autoReset = status;
	}
}
