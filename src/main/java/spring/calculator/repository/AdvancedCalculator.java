package spring.calculator.repository;

public class AdvancedCalculator implements ICalculator {

    public int sum(int a, int b) {
        long r = (long) a + b;
        if (r < Integer.MIN_VALUE || r > Integer.MAX_VALUE) {
            throw new RuntimeException("Overflow");
        }
        return (int) r;
    }

    public int diff(int a, int b) {
        long r = (long) a - b;
        if (r < Integer.MIN_VALUE || r > Integer.MAX_VALUE) {
            throw new RuntimeException("Overflow");
        }
        return (int) r;
    }

    public int div(int a, int b) {
        return a/b;
    }

    public int mult(int a, int b) {
        long r = (long) a * b;
        if (r < Integer.MIN_VALUE || r > Integer.MAX_VALUE) {
            throw new RuntimeException("Overflow");
        }
        return (int) r;
    }

}
