package chapter04;

class Animal{
    public void eat(){
        System.out.println("动物进食");
    }
}
interface Huntable{
    void hunt();
}
class Dog extends Animal implements Huntable{
    @Override
    public void eat() {
        System.out.println("狗吃骨头");
    }

    @Override
    public void hunt() {
        System.out.println("狗拿耗子，多管闲事");
    }
}
class Cat extends Animal implements Huntable{
    public Cat(){
        super();    //方法类型已知，这就是早期绑定（静态链接）
    }
    public Cat(String name){
        this();  //方法类型已知，这就是早期绑定（静态链接）
    }

    @Override
    public void eat() {
        System.out.println("猫吃鱼");
    }

    @Override
    public void hunt() {
        System.out.println("猫吃耗子。天经地义");
    }
}
public class AnimalTestDemo {
    public void showAnimal(Animal animal){
        animal.eat();   //表现为晚期绑定,在运行时才可以确定
    }
    public void showHunt(Huntable h){
        h.hunt(); //表现为晚期绑定，在运行时才可以确定
    }
}
