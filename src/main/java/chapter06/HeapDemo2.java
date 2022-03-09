package chapter06;

import java.util.concurrent.TimeUnit;

/**
 * -Xms20m -Xmx20m
 */
public class HeapDemo2 {
    public static void main(String[] args) {
        System.out.println("start...");
        try {
            TimeUnit.SECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end...");
    }
}
