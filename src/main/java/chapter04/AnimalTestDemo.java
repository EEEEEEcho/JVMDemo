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

    public static void main(String[] args) {


//        AnimalTestDemo a = new AnimalTestDemo();
//        int divide = a.divide(-2147483648, -1);
//        System.out.println(divide);
        int[] arr = new int[]{1,4,7,11,15};
        int left = 0;
        int right = arr.length - 1;
        int target = 11;
        int res = 0;
        int mid = -1;
        while (left <= right){
            mid = left + (right - left) / 2;
            res = arr[mid];
            if (arr[mid] == target){
                break;
            }
            else if (arr[mid] > target){
                right = mid - 1;
            }
            else{
                left = mid + 1;
            }
        }
        System.out.println(mid);
        System.out.println(res);
        System.out.println(left);
        System.out.println(right);
    }
    public int divide(int a, int b) {
        if(a == 0) return 0;
        long m = Math.abs(a);
        long n = Math.abs(b);
        long res = 0;
        while(m >= n){
            m -= n;
            res ++;
        }
        if(res >= Integer.MAX_VALUE){
            res = Integer.MAX_VALUE;
        }
        if((a > 0 && b > 0) || (a < 0 && b < 0)){
            return (int)res;
        }
        else{
            return -(int)res;
        }
    }
}
