package chapter10;

import java.util.HashSet;

/**
 * 证明字符串常量池在堆中
 * jdk1.6中设置方法区和堆空间的大小都为6M
 * -XX:PermSize=6m -XX:MaxPermSize=6m -Xms6m -Xmx6m
 *
 * jdk1.8中设置方法去和堆空间的大小都为6M
 * -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=64m -Xms6m -Xmx6m
 *
 */
public class StringTest3 {
    public static void main(String[] args) {
        HashSet<String> hashSet = new HashSet<>();
        long i = 0;
        while (true){
            //intern()方法：如果该字符串没有在字符串常量池中，将该字符串放入常量池中，然后返回其在常量池中的地址引用
            //否则，直接返回该字符串在常量池中的地址引用
            hashSet.add(String.valueOf(i ++).intern());
        }
    }
}
