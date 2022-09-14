package chapter10;

public class StringTest5 {
    public static void main(String[] args) {
//        test1();

//        test2();

//        test3();

        test4();
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

    public static void test3(){
        String s1 = "a";
        String s2 = "b";
        String s3 = "ab";
        /*
            s1 + s2 的执行细节
            StringBuilder sb = new StringBuilder();
            sb.append("a");
            sb.append("b");
            sb.toString();  //约等于new String("ab");
            JDK5.0 之后用的是StringBuilder 之前用的是StringBuffer
         */
        String s4 = s1 + s2;
        System.out.println(s3 == s4);   //false
    }

    public static void test4(){
        final String s1 = "a";
        final String s2 = "b";
        String s3 = "ab";
        /**
         * 1.字符串拼接操作不一定使用的是StringBuilder,
         * 如果拼接符号左右两边都是字符串常量(字面量)或者常量引用(final定义的常量的引用)
         * 则仍然使用编译器优化，即非StringBuilder的方式
         *
         * 2.针对于final修饰类、方法、基本数据类型、引用数据类型的量的结构时
         * 尽量加上final
         */
        String s4 = s1 + s2;
        System.out.println(s3 == s4);   //true
    }

    public static void test5(int highLevel){
        String src = "";
        for (int i = 0; i < highLevel; i++) {
            //每次拼接都会创建一个StringBuilder对象，并调用toString()创建一个String
            //内存中创建了许多StringBuilder和String,会占用内很多内存，而且因为创建了
            //很多对象，导致GC的压力增大
            src = src + "a";
        }

        //只有一个StringBuilder，每次添加一个字符串"a"
        //append()方式添加字符串的效率要远高于String的字符串拼接方式
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < highLevel; i++) {
            sb.append("a");
        }

        //优化
        //在实际开发中，如果基本确定一共要添加的字符串数量不高于某个限定值highlevel
        //的情况下，要考虑使用带有初始容量的构造器进行实例化，避免扩容和复制操作带来的性能损耗
        StringBuilder sbs = new StringBuilder(highLevel + 1);
        for (int i = 0; i < highLevel; i++) {
            sbs.append("a");
        }
    }
}
