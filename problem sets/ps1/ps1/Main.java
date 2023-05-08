public class Main {
    public static void main(String args[]) {
        ILFShiftRegister r = new ShiftRegister(15, 2);
        int[] seed = { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 };
        r.setSeed(seed);
        for (int i = 0; i < 10; i++) {
            System.out.println(r.generate(6));
        }
    }
}