package chapter06;

import java.util.concurrent.TimeUnit;

/**
 * 栈上分配测试
 * 先关闭逃逸分析
 * -Xmx1G -Xmx1G -XX:-DoEscapeAnalysis -XX:+PrintGCDetails
 *
 */
public class StackAllocation {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            alloc();
        }
        long end = System.currentTimeMillis();
        System.out.println("花费的时间为： " + (end - start) + " ms");


        try {
            TimeUnit.SECONDS.sleep(100000);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void alloc(){
        //这里没有发生逃逸，会采取栈上分配的策略
        User user = new User();
    }
    static class User{

    }
}
