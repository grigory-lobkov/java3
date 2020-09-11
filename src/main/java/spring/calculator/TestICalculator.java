package spring.calculator;


import org.junit.Assert;
import org.junit.Test;

public class TestICalculator {

    @Test
    public void ICalculator_sum1() {
        int expected = 5 + 7;
        int got = App.calculator.sum(5, 7);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void ICalculator_sum2() {
        int expected = -5 + Integer.MAX_VALUE;
        int got = App.calculator.sum(-5, Integer.MAX_VALUE);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void ICalculator_sum3() {
        int expected = -5 - 7;
        int got = App.calculator.sum(-5, -7);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void ICalculator_diff() {
        int expected = 5 - 7;
        int got = App.calculator.diff(5, 7);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void ICalculator_div() {
        int expected = 8 / 2;
        int got = App.calculator.div(8, 2);
        Assert.assertEquals(expected, got);
    }

    @Test(expected = ArithmeticException.class)
    public void ICalculator_divErr() {
        App.calculator.div(8, 0);
    }

    @Test
    public void ICalculator_mult() {
        int expected = 5 * (-7);
        int got = App.calculator.mult(5, -7);
        Assert.assertEquals(expected, got);
    }

    @Test(expected = RuntimeException.class)
    public void ICalculator_sumOver() {
        App.calculator.sum(Integer.MAX_VALUE - 10, 20);
    }

    @Test(expected = RuntimeException.class)
    public void ICalculator_sumOver2() {
        App.calculator.sum(-Integer.MAX_VALUE + 10, -20);
    }

    @Test(expected = RuntimeException.class)
    public void ICalculator_diffOver() {
        App.calculator.diff(Integer.MAX_VALUE - 10, -20);
    }

    @Test(expected = RuntimeException.class)
    public void ICalculator_diffOver2() {
        App.calculator.diff(-Integer.MAX_VALUE + 10, 20);
    }

    @Test(expected = RuntimeException.class)
    public void ICalculator_multOver() {
        App.calculator.mult(Integer.MAX_VALUE / 2, 4);
    }

}