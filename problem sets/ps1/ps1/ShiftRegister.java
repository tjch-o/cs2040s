///////////////////////////////////
// This is the main shift register class.
// Notice that it implements the ILFShiftRegister interface.
// You will need to fill in the functionality.
///////////////////////////////////

/**
 * class ShiftRegister
 * 
 * @author
 *         Description: implements the ILFShiftRegister interface.
 */
public class ShiftRegister implements ILFShiftRegister {
    ///////////////////////////////////
    // Create your class variables here
    ///////////////////////////////////
    // size is the instance variable representing the number of bits in the shift register
    private int size;
    // tap is the instance variable representing the tap which is one of the bits in the array
    private int tap;
    // seed is the instance variable representing our shift register which is an array of bits
    private int[] seed;

    ///////////////////////////////////
    // Create your constructor here:
    ///////////////////////////////////
    ShiftRegister(int size, int tap) {
        this.size = size;
        this.tap = tap;
        this.seed = new int[size];
        // tap refers to one of the bits and must be between 0 and size - 1 inclusive
        if (this.tap >= this.size) {
            throw new IllegalArgumentException("tap must be between 0 and size - 1");
        }
    }

    ///////////////////////////////////
    // Create your class methods here:
    ///////////////////////////////////
    /**
     * setSeed
     * 
     * @param seed
     *             Description:
     */
    @Override
    public void setSeed(int[] seed) {
        // we first check if seed length matches size
        if (this.size != seed.length) {
            throw new RuntimeException("Seed length does not match size");
        } else {
            for (int i = 0; i < this.size; i++) {
                // checking if there are values that are not 0 or 1 in the array
                // if there is indeed such a value we straight away throw an exception
                if (seed[i] != 0 && seed[i] != 1) {
                    throw new IllegalArgumentException("Seed needs to be an array of 0s and 1s "
                            + "only; no other numbers allowed");
                } else {
                    // we reverse the array to make it look like the seed
                    this.seed[i] = seed[this.size - i - 1];
                }
            }
        }
    }

    /**
     * shift
     * 
     * @return
     *         Description:
     */
    @Override
    public int shift() {
        /* first step we calculate xor using the formula (a^b) and that will be our feedback bit */
        // FYI the diagram in the lab is actually the seed and NOT the array
        int mostSignificantBit = this.seed[0];
        int tapBit = this.seed[this.size - this.tap - 1];
        int feedbackBit = (mostSignificantBit ^ tapBit);
        // shift everything to the left in the array
        for (int i = 1; i < this.size; i += 1) {
            this.seed[i - 1] = this.seed[i];
        }
        this.seed[this.size - 1] = feedbackBit;
        // last step is to return the least signficant bit of the resulting register
        return feedbackBit;
    }

    /**
     * generate
     * 
     * @param k
     * @return
     *         Description:
     */
    @Override
    public int generate(int k) {
        // handling the case of negative k value
        if (k < 0) {
            throw new IllegalArgumentException("cannot extract negative k bits; k cannot be less" +
                    " than 0");
        }

        // generate the binary
        String binaryResult = "";
        // temp variable used to hold the feedback bit from each round of calling
        // shift()
        int temp = 0;
        for (int i = 0; i < k; i += 1) {
            temp = this.shift();
            // we use concatenation: String + int = String
            binaryResult += temp;
        }

        // // we will convert binaryResult to int
        int result = 0;
        for (int j = 0; j < binaryResult.length(); j += 1) {
            if (binaryResult.charAt(j) == '1') {
                result += Math.pow(2, binaryResult.length() - 1 - j);
            }
        }
        return result;
    }

    /**
     * Returns the integer representation for a binary int array.
     * 
     * @param array
     * @return
     */
    private int toDecimal(int[] array) {
        // TODO:
        return 0;
    }
}
