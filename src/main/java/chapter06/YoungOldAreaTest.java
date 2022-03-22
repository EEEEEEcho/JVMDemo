package chapter06;

/**
 * 测试:大对象直接进入老年代
 * -Xms60m -Xmx60m -XX:NewRatio=2 -XX:SurvivorRatio=8 -XX:+PrintGCDetails
 * 设置新生代和老年代的比例为1:2,设置Eden区和Survivor区的比例为8:1:1
 * [16m(eden),2m(survivor0),2m(survivor1)][40m (old)]
 */
public class YoungOldAreaTest {
    public static void main(String[] args) {
        byte[] bytes = new byte[1024 * 1024 * 20];  //20M
    }
}
