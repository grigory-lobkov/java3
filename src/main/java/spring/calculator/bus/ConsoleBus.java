package spring.calculator.bus;

import spring.calculator.repo.ICalculator;

import java.util.Scanner;

public class ConsoleBus implements IBus {

    public void start(ICalculator calculator) {
        System.out.println("type '/' to exit");
        Scanner in = new Scanner(System.in);
        try {
            while (true) {
                System.out.print("Input integer A: ");
                int a = in.nextInt();
                System.out.print("Input integer B: ");
                int b = in.nextInt();
                System.out.print("Choose operation (1-sum,2-diff,3-mult,4-div): ");
                int o = in.nextInt();
                int r = 0;
                try {
                    switch (o) {
                        case 1:
                            r = calculator.sum(a, b);
                            break;
                        case 2:
                            r = calculator.diff(a, b);
                            break;
                        case 3:
                            r = calculator.mult(a, b);
                            break;
                        case 4:
                            r = calculator.div(a, b);
                            break;
                        default:
                            o = 0;
                    }
                    if (o > 0) System.out.println("Calculator said: " + r);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println("Error happen: " + e.getMessage());
                }
            }
        } finally {
            in.close();
        }
    }

}