package chapter06;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class HeapInstanceTest {
    byte[] buffer = new byte[new Random().nextInt(1024 * 200)];
    public static void main(String[] args) {
        ArrayList<HeapInstanceTest> list = new ArrayList<>();
        while (true){
            list.add(new HeapInstanceTest());
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
