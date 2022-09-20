package chapter11;

public class RefCountGc {
    private byte[] bigSize = new byte[5 * 1024 * 1024];

    Object reference = null;

    public static void main(String[] args) {
        RefCountGc r1 = new RefCountGc();
        RefCountGc r2 = new RefCountGc();

        r1.reference = r2;
        r2.reference = r1;

        r1 = null;
        r2 = null;

        //显式执行垃圾收集行为
        System.gc();
    }
}
