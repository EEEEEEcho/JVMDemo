package chapter10;

public class StringTest5 {
    public static void main(String[] args) {
//        test1();

        test2();
    }

    public static void test1(){
        String s1 = "a" + "b" + "c";
        String s2 = "abc";      //一定放在字符串常量池中，将此地址赋值给s2

        /**
         * 最终.java编译成.class，再执行.class
         * String s1 = "abc";
         * String s2 = "abc";
         */
        System.out.println(s1 == s2);       //true
        System.out.println(s1.equals(s2));  //true
    }

    public static void test2(){
        String s1 = "javaee";
        String s2 = "hadoop";

        String s3 = "javaeehadoop";
        String s4 = "javaee" + "hadoop";
        String s5 = s1 + "hadoop";
        String s6 = "javaee" + s2;

        String s7 = s1 + s2;

        System.out.println(s3 == s4);   //true 常量与常量的拼接结果在常量池，原理是编译期优化
        System.out.println(s3 == s5);   //false 只要其中有一个是变量，结果就在堆中（非常量池区域），相当于新new了一个字符串对象。变量拼接的原理是StringBuilder
        System.out.println(s3 == s6);   //false 只要其中有一个是变量，结果就在堆中（非常量池区域），相当于新new了一个字符串对象。变量拼接的原理是StringBuilder
        System.out.println(s3 == s7);   //false 只要其中有一个是变量，结果就在堆中（非常量池区域），相当于新new了一个字符串对象。变量拼接的原理是StringBuilder
        System.out.println(s5 == s6);   //false 只要其中有一个是变量，结果就在堆中（非常量池区域），相当于新new了一个字符串对象。变量拼接的原理是StringBuilder
        System.out.println(s5 == s7);   //false 只要其中有一个是变量，结果就在堆中（非常量池区域），相当于新new了一个字符串对象。变量拼接的原理是StringBuilder
        System.out.println(s6 == s7);   //false 只要其中有一个是变量，结果就在堆中（非常量池区域），相当于新new了一个字符串对象。变量拼接的原理是StringBuilder


        //intern():判断字符串常量池中是否存在javaeehadoop的值，如果存在，则返回常量池中javaeehadoop的地址
        //如果不存在，则在常量池中加载一份javaeehadoop,并返回此对象的地址
        String s8 = s6.intern();        //调用intern 将字符串s6 的内容放入到字符串常量池中，然后返回其在常量池中位置的引用
        System.out.println(s3 == s8);   //true
    }
}
