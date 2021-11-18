package chapter04;

import java.util.Date;

public class LocalVariablesTest {
    private int count = 0;

    public static void main(String[] args) {
        LocalVariablesTest l = new LocalVariablesTest();
        int num = 10;
        l.test1();
    }

    public void test1(){
        Date date = new Date();
        String name = "ech0";
        String info = test2(date,name);
        System.out.println(date + name);
    }

    public String test2(Date dateP,String name2){
         dateP = null;
         name2 = "jane";
         double weight = 110.0;
         char gender = '男';
         return dateP + name2;
    }

    public void test3(){
        count ++;
    }

    public void test4(){
        int a = 0;
        {
            int b = 0;
            b = a + 1;
        }
        int c = a + 1;
    }
}
