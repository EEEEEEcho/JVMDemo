package chapter07;

public class ConstPoolMeaning {
    String world = "World";
    //常量池存在的意义
    public String sayHello(){
        String hello = "Hello";
        String helloWorld = hello + world;
        System.out.println(helloWorld);
        return helloWorld;
    }

    public String sayGoodbye(){
        String goodbye = "Goodbye";
        String goodbyeWorld = goodbye + world;
        System.out.println(goodbyeWorld);
        return goodbyeWorld;
    }

    public static void main(String[] args) {

    }
}
