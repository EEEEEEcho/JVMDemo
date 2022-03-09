package chapter06;

public class HeapSpaceInitial {
    public static void main(String[] args) {
        //Java虚拟机中的堆内存总量,初始内存
        long initialMemory = Runtime.getRuntime().totalMemory() / 1024 /1024;
        //虚拟机试图使用的最大内存量
        long maxMemory = Runtime.getRuntime().maxMemory() / 1024 /1024;

        System.out.println("-Xms : " + initialMemory + "M");
        System.out.println("-Xmx : " + maxMemory + "M");

        System.out.println("系统内存大小 : " + initialMemory * 64.0 / 1024 + "G");
        System.out.println("系统内存大小 : " + maxMemory * 4.0 / 1024 + "G");
    }
}
