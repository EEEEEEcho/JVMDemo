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
        test2(date,name);
        System.out.println(date + name);
    }

    public String test2(Date dateP,String name2){
         dateP = null;
         name2 = "jane";
         double weight = 110.0; //占据两个slot
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
        //b 出了作用域就已经挂掉了。但是局部变量表中的槽slot已经开辟了，
        //所以变量c就会重复利用该槽
        //变量c会使用之前已经销毁的变量b占据的slot的位置
        int c = a + 1;
    }
}
