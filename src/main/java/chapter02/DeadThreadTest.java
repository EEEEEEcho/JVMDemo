package chapter02;

public class DeadThreadTest {
    public static void main(String[] args) {
        Runnable r = () -> {
            System.out.println(Thread.currentThread().getName() + "开始");
            DeadThread deadThread = new DeadThread();
            System.out.println(Thread.currentThread().getName() + "结束");
        };

        Thread t1 = new Thread(r,"线程1");
        Thread t2 = new Thread(r,"线程2");

        t1.start();
        t2.start();
    }
}


class DeadThread{
    static {
        if (true){
            System.out.println(Thread.currentThread().getName() + "初始化当前类");
            //由于设定了死循环，所以该类的加载过程是无法结束的，是无法加载完的
            while (true){

            }
        }
    }
}