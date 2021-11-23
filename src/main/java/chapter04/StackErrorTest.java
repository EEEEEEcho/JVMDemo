package chapter04;

/**
 * 演示栈中的异常
 */
public class StackErrorTest {
    private static int count = 0;
    public static void main(String[] args) {
        count ++;
        System.out.println(count);
        main(args);
    }
}
