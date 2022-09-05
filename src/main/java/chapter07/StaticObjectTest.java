package chapter07;

public class StaticObjectTest {
    static class Test{
        //静态变量的引用
        static ObjectHolder staticObj = new ObjectHolder();
        //实例变量的引用
        ObjectHolder instanceObj = new ObjectHolder();

        void foo(){
            //局部变量的引用
            ObjectHolder localObj = new ObjectHolder();
            System.out.println("Done");
        }
    }
    private static class ObjectHolder{
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.foo();
    }
}
