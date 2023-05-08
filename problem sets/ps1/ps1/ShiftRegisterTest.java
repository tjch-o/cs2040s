import static org.junit.Assert.*;

import org.junit.Test;

/**
 * ShiftRegisterTest
 * 
 * @author dcsslg
 *         Description: set of tests for a shift register implementation
 */
public class ShiftRegisterTest {
    /**
     * Returns a shift register to test.
     * 
     * @param size
     * @param tap
     * @return a new shift register
     */
    ILFShiftRegister getRegister(int size, int tap) {
        return new ShiftRegister(size, tap);
    }

    /**
     * Tests shift with simple example.
     */
    @Test
    public void testShift1() {
        ILFShiftRegister r = getRegister(9, 7);
        int[] seed = { 0, 1, 0, 1, 1, 1, 1, 0, 1 };
        r.setSeed(seed);
        int[] expected = { 1, 1, 0, 0, 0, 1, 1, 1, 1, 0 };
        for (int i = 0; i < 10; i++) {
            assertEquals(expected[i], r.shift());
        }
    }

    /**
     * Tests shift with simple example.
     */
    @Test
    public void testShift2() {
        ILFShiftRegister r = getRegister(5, 3);
        int[] seed = { 1, 0, 1, 0, 1 };
        r.setSeed(seed);
        int[] expected = { 1, 1, 1, 1, 0, 0 };
        for (int i = 0; i < 6; i += 1) {
            assertEquals(expected[i], r.shift());
        }
    }

    /**
     * Tests generate with simple example.
     */
    @Test
    public void testGenerate1() {
        ILFShiftRegister r = getRegister(9, 7);
        int[] seed = { 0, 1, 0, 1, 1, 1, 1, 0, 1 };
        r.setSeed(seed);
        int[] expected = { 6, 1, 7, 2, 2, 1, 6, 6, 2, 3 };
        for (int i = 0; i < 10; i++) {
            assertEquals("GenerateTest", expected[i], r.generate(3));
        }
    }

    /**
     * Tests generate with simple example.
     */
    @Test
    public void testGenerate2() {
        ILFShiftRegister r = getRegister(15, 2);
        int[] seed = { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 };
        r.setSeed(seed);
        int[] expected = { 2, 61, 0, 21, 0, 2, 63, 63, 42, 42 };
        for (int i = 0; i < 10; i += 1) {
            assertEquals("GenerateTest", expected[i], r.generate(6));
        }
    }

    /**
     * Tests register of length 1.
     */
    @Test
    public void testOneLength() {
        ILFShiftRegister r = getRegister(1, 0);
        int[] seed = { 1 };
        r.setSeed(seed);
        int[] expected = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
        for (int i = 0; i < 10; i++) {
            assertEquals(expected[i], r.generate(3));
        }
    }

    /**
     * Tests with erroneous seed.
     */
    @Test
    public void testError() {
        ILFShiftRegister r = getRegister(4, 1);
        int[] seed = { 1, 0, 0, 0, 1, 1, 0 };
        r.setSeed(seed);
        r.shift();
        r.generate(4);
    }

    /**
     * Tests if anything other than 0 or 1 is in the seed.
     */
    @Test
    public void testInvalidElement() {
        ILFShiftRegister r = getRegister(4, 0);
        int[] seed = { 1, 0, 0, 2 };
        r.setSeed(seed);
    }

    /**
     * Tests tap = 0 which is comparing least significant bit.
     */
    @Test
    public void testTap0() {
        ILFShiftRegister r = getRegister(4, 0);
        int[] seed = { 1, 1, 0, 1 };
        r.setSeed(seed);
        int[] expected = { 0, 0, 1, 0 };
        for (int j = 0; j < 4; j += 1) {
            assertEquals(expected[j], r.shift());
        }
    }

    /**
     * Tests if tap is valid: within 0 to size - 1 inclusive.
     */
    @Test
    public void testInvalidTap() {
        ILFShiftRegister r = getRegister(5, 6);
        int[] seed = { 1, 0, 0, 1, 0 };
        r.setSeed(seed);
    }

    /**
     * Tests with negative integer k value passed into generate.
     */
    @Test
    public void testNegativeK() {
        ILFShiftRegister r = getRegister(4, 2);
        int[] seed = { 1, 1, 0, 1 };
        r.setSeed(seed);
        r.shift();
        r.generate(-2);
    }
}
