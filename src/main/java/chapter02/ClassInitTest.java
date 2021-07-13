package chapter02;

public class ClassInitTest {
    private static int num = 1;

    static {
        num = 2;
        number = 20;
        System.out.println(num);
        //System.out.println(number); //非法前向引用
    }

    /**
     * 该变量在prepare阶段会分配内存，并且初始化值为0，
     * 然后在initial阶段，这个number变量已经有了存储空间，然后
     * <clinit>()方法将所有静态变量的操作收集起来进行执行，
     * 执行时按顺序执行，最后这个number的值会被赋值为10
     * prepare:number = 0
     * initial:number:20 -> 10
     */
    private static int number =10;

    public static void main(String[] args) {
        System.out.println(ClassInitTest.num);
        System.out.println(ClassInitTest.number);
    }
}
