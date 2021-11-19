package chapter02;

public class HelloApp {
    /**
     * 这个值在类加载过程的链接阶段中的准备阶段初始化为0
     * 此值在初始化阶段 initial阶段才会被赋值为1: a = 1
     */
    private static int a = 1;

    public static void main(String[] args) {
        System.out.println(a);
    }
}
