package chapter02;

public class HelloApp {
    /**
     * 这个值在准备阶段 prepare阶段 : a = 0
     * 此值在初始化阶段 initial阶段才会被赋值为1: a = 1
     */
    private static int a = 1;

    public static void main(String[] args) {
        System.out.println(a);
    }
}
