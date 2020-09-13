package spring.jsonfiledb.bus;

import spring.jsonfiledb.pojo.IPojo;
import spring.jsonfiledb.repo.IRepo;

import java.util.Scanner;

public class ConsoleBus implements IBus {

    public void start(IPojo pojo, IRepo repo) {
        System.out.println("type '/' to exit");
        Scanner in = new Scanner(System.in);
        try {
            while (true) {
                /*System.out.print("Input integer A: ");
                int a = in.nextInt();
                System.out.print("Input integer B: ");
                int b = in.nextInt();
                System.out.print("Choose operation (1-sum,2-diff,3-mult,4-div): ");
                int o = in.nextInt();
                int r = 0;
                try {
                    switch (o) {
                        case 1:
                            r = pojo.sum(a, b);
                            break;
                        case 2:
                            r = pojo.diff(a, b);
                            break;
                        case 3:
                            r = pojo.mult(a, b);
                            break;
                        case 4:
                            r = pojo.div(a, b);
                            break;
                        default:
                            o = 0;
                    }
                    if (o > 0) System.out.println("Calculator said: " + r);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println("Error happen: " + e.getMessage());
                }*/
            }
        } finally {
            in.close();
        }
    }

}