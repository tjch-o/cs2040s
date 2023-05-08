import java.util.Random;
import java.util.HashMap;

/**
 * This is the main class for your Markov Model.
 *
 * Assume that the text will contain ASCII characters in the range [1,255].
 * ASCII character 0 (the NULL character) will be treated as a non-character.
 *
 * Any such NULL characters in the original text should be ignored.
 */
public class MarkovModel {
	// Use this to generate random numbers as needed
	private Random generator = new Random();

	// This is a special symbol to indicate no character
	public static final char NOCHARACTER = (char) 0;

	private int order;
	private int totalAsciiCharacters = 256;
	private HashMap<String, int[]> hashmap;


	/**
	 * Constructor for MarkovModel class.
	 *
	 * @param order the number of characters to identify for the Markov Model sequence
	 * @param seed the seed used by the random number generator
	 */
	public MarkovModel(int order, long seed) {
		// Initialize your class here
		this.order = order;
		this.hashmap = new HashMap<>();

		// Initialize the random number generator
		generator.setSeed(seed);
	}

	/**
	 * Builds the Markov Model based on the specified text string.
	 */
	public void initializeText(String text) {
		// Build the Markov model here
		String currentKgram;
		char currentChar;
		int len = text.length();
		for (int i = 0; i < len - this.order; i += 1) {
			currentKgram = text.substring(i, i + this.order);
			currentChar = text.charAt(i + this.order);
			if (!this.hashmap.containsKey(currentKgram)) {
				int[] newArr = new int[totalAsciiCharacters];
				// if current kgram does not exist in hashmap we just create one new arr of ascii characters
				hashmap.put(currentKgram, newArr);
			} 
			int currentCharAscii = (int) currentChar;
			int[] currentArr = this.hashmap.get(currentKgram);
			currentArr[currentCharAscii] += 1;
		}
	}

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
		int[] currentArr = this.hashmap.get(kgram);
		int sum = 0;
		// we find the sum of all the elements since each element is the frequency of one character
		for (int i = 0; i < totalAsciiCharacters; i += 1) {
			sum += currentArr[i];
		}
		return sum;
	}

	/**
	 * Returns the number of times the character c appears immediately after the
	 * specified kgram.
	 */
	public int getFrequency(String kgram, char c) {
		int ascii = (int) c;
		return this.hashmap.get(kgram)[ascii];
	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NOCHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public char nextCharacter(String kgram) {
		// See the problem set description for details
		// on how to make the random selection.
		if (this.hashmap.containsKey(kgram)) {
			int frequencyKgram = this.getFrequency(kgram);
			// we want a range [0, frequencyKgram - 1]
			int randomNumber = generator.nextInt(frequencyKgram);
			char currentChar;
			int currentFreq;

			// we loop through possible characters following kgram
			for (int i = 0; i < totalAsciiCharacters; i += 1) {
				currentChar = (char) i;
				currentFreq = this.getFrequency(kgram, currentChar);
				if (currentFreq > 0) {
					// we are partitioning the range of numbers 
					if (randomNumber > currentFreq - 1) {
						randomNumber -= currentFreq;
					} else {
						return currentChar;
					}
				}
			}
		}
		return NOCHARACTER;
	}
}
