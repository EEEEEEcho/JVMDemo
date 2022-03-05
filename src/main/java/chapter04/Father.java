package chapter04;

public class Father {
    public Father(){
        System.out.println("father的构造器");
    }
    public static void showStatic(String str){
        System.out.println("father的static方法:" + str);
    }
    public final void showFinal(){
        System.out.println("father中的final方法");
    }
    public void showCommon(){
        System.out.println("father中的普通成员方法");
    }
}
class Son extends Father{
    public Son(){
        //构造器：非虚方法
        // invokespecial
        super();
    }
    public Son(int age){
        //构造器，非虚方法
        //invokespecial
        this();
    }
    public static void showStatic(String str){
        System.out.println("son的static方法:" + str);
    }
    private void showPrivate(String str){
        System.out.println("son的private方法:" + str);
    }
    public void show(){
        //静态方法：非虚方法
        //invokestatic
        showStatic("son");
        //静态方法：非虚方法
        //invokestatic
        super.showStatic("father");
        //私有方法：非虚方法
        //invokespecial
        showPrivate("son");
        //显式使用super关键字调用父类方法：非虚方法
        //invokespecial
        super.showCommon();
        //虽然是invokevirtual，但是该方法在父类
        //是由final关键字修饰的，所以其是非虚方法
        //invokevirtual
        showFinal();
        //显式使用super关键字调用父类方法：非虚方法
        //invokespecial
        super.showFinal();

        //虚方法，公共的成员函数
        //invokevirtual
        showCommon();
        //虚方法：公共的成员函数
        //invokevirtual
        info();
        MethodInterface m = null;
        //虚方法：接口中定义的方法
        //invokeinterface
        m.method();
    }
    public void info(){

    }
}
interface MethodInterface{
    void method();
}
