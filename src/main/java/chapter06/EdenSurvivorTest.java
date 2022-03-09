package chapter06;

/**
 * -Xms600m -Xmx600m
 * -NewRatio: 设置新生代与老年代的比例，默认是2即：新生代空间 ：老年代空间 = 1 : 2
 * -NewRatio的默认值也是2
 */
public class EdenSurvivorTest {
    public static void main(String[] args) {
        System.out.println("来打个酱油~");
        try {
            Thread.sleep(1000000000);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
