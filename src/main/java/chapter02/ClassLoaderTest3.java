package chapter02;

/**
 * 测试获取ClassLoader的各种途径
 */
public class ClassLoaderTest3 {
    public static void main(String[] args) throws ClassNotFoundException {
        //1.使用getClassLoader()方法
        ClassLoader clazzClassLoader = Class.forName("java.lang.String").getClassLoader();
        System.out.println(clazzClassLoader);
        //2.获取当前线程上下文的ClassLoader
        ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(threadClassLoader);
        //3.获取系统的ClassLoader
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader);
        //4.获取调用者的ClassLoader
    }
}
