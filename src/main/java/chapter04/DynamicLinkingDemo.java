package chapter04;

public class DynamicLinkingDemo {
    int num = 10;
    public void methodA(){
        System.out.println("MethodA(),,,,,");
    }
    public void methodB(){
        System.out.println("MethodB()......");
        methodA();
        num ++;
    }
}
