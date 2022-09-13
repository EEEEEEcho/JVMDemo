package chapter10;

public class StringTest4 {
    public static void main(String[] args) {
        System.out.println();       //字符串数量2139
        System.out.println("1");    //字符串数量2140
        System.out.println("2");
        System.out.println("3");
        System.out.println("4");
        System.out.println("5");
        System.out.println("6");
        System.out.println("7");
        System.out.println("8");
        System.out.println("9");
        System.out.println("10");   //2150


        System.out.println("1");    //2150
        System.out.println("2");    //2150
        System.out.println("3");    //2150
        System.out.println("4");    //后续字符串的取出都是从字符串常量池中取出来的，不会再次加载
        System.out.println("5");
        System.out.println("6");
        System.out.println("7");
        System.out.println("8");
        System.out.println("9");
        System.out.println("10");
    }
}
