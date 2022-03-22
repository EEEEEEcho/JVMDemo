package chapter06;

import java.util.ArrayList;

public class GCTest {
    public static void main(String[] args) {
        int i = 0;
        try {
            ArrayList<String> list = new ArrayList<>();
            //字符串常量池是存放在堆空间的，原来是在方法区，现在放到了堆空间
            String a = "echooooooo";
            while (true){
                list.add(a);
                a = a + a;
                i ++;
            }
        }
        catch (Throwable t){
            t.printStackTrace();
            System.out.println("遍历次数为: " + i);
        }
    }
}
