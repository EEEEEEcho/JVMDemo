package chapter10;

public class StringTest1 {
    public static void test1(){
        String s1 = "abc"; //字面量定义的方式,"abc"存储在字符串常量池里
        String s2 = "abc";
        //字符串常量池里面不会存储相同的字符串
        System.out.println(s1 == s2);   //判断地址是否相等
        s1 = "hello";
        System.out.println(s1);
        System.out.println(s2);
    }
    public static void test2(){
        String s1 = "abc";
        String s2 = "abc";
        //这个时候s1和s2是共用的字符串常量池中同一个字符串"abc"的引用

        s2 += "def";
        //s2做拼接操作时，并没有改变字符串常量池中的"abc"，也不是在abc后面直接拼接，
        //而是新建了一个字符串"abcdef"对象注意，这里不是新建到字符串常量池中,
        //这就体现了字符串的不变性,即abc没有改变，每次都是新创建一个

        String s3 = "abcdef";

        System.out.println(s2 == s3);   //false 说明对s2的拼接操作没有将结果放到字符串常量池中
    }

    public static void test3(){
        String s1 = "abc";

        //这里的s2又新建了一个字符数组，然后将a替换成了m
        String s2 = s1.replace("a","m");

        System.out.println(s1);
        System.out.println(s2);
    }
    public static void main(String[] args) {
//        test1();
//        test2();
        test3();
    }
}
