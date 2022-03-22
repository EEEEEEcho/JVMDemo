package chapter07;

import java.util.concurrent.TimeUnit;

/**
 * 测试设置方法区大小参数的默认值
 * JDK8及以后：
 * -XX:MetaspaceSize=100m -XX:MaxMetaspaceSize=100m
 */
public class MetaAreaInfo {
    public static void main(String[] args) throws InterruptedException {
        TimeUnit.SECONDS.sleep(10000000);
    }
}
