package chapter04;
@FunctionalInterface
interface Func{
    public boolean func(String str);
}
public class LambdaDemo {
    public void lambda(Func func){
        return;
    }

    public static void main(String[] args) {
        LambdaDemo lambdaDemo = new LambdaDemo();
        //invokedynamic #4 <func, BootstrapMethods #0>
        Func func = s -> {
            return true;
        };
        lambdaDemo.lambda(func);
        lambdaDemo.lambda(s -> {
            return true;
        });
    }
}
