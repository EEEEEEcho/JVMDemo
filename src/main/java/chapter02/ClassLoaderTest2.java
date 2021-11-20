package chapter02;

//import sun.misc.Launcher;
//import sun.security.util.CurveDB;
//import sun.util.resources.cldr.TimeZoneNames;

import java.net.URL;
//import java.security.Provider;

/**
 * 测试引导类加载器
 */
public class ClassLoaderTest2 {
    public static void main(String[] args) {
        System.out.println("*************引导类加载器*************");
        //获取BootstrapClassLoader能够加载的API路径
//        URL[] bootStrapUrls = Launcher.getBootstrapClassPath().getURLs();
//        for (URL url : bootStrapUrls){
//            System.out.println(url.toExternalForm());
//        }
        /**
         * file:/D:/Java/jdk1.8.0_271/jre/lib/resources.jar
         * file:/D:/Java/jdk1.8.0_271/jre/lib/rt.jar
         * file:/D:/Java/jdk1.8.0_271/jre/lib/sunrsasign.jar
         * file:/D:/Java/jdk1.8.0_271/jre/lib/jsse.jar
         * file:/D:/Java/jdk1.8.0_271/jre/lib/jce.jar
         * file:/D:/Java/jdk1.8.0_271/jre/lib/charsets.jar
         * file:/D:/Java/jdk1.8.0_271/jre/lib/jfr.jar
         * file:/D:/Java/jdk1.8.0_271/jre/classes
         */
        /**从上述路径中随意选择一个类，看看其类加载器是什么 **/
//        ClassLoader classLoader = Provider.class.getClassLoader();
//        System.out.println(classLoader);    //null 引导类加载器
//
//
//        System.out.println("*************扩展类加载器*************");
//        String extDirs = System.getProperty("java.ext.dirs");
//        for (String path: extDirs.split(";")){
//            System.out.println(path);
//        }
//        /**
//         * D:\Java\jdk1.8.0_271\jre\lib\ext
//         * C:\WINDOWS\Sun\Java\lib\ext
//         */
//        /**从上述路径中随意选择一个类，看看其类加载器是什么 **/
//        ClassLoader extClassLoader = TimeZoneNames.class.getClassLoader();
//        System.out.println(extClassLoader); //sun.misc.Launcher$ExtClassLoader@677327b6 扩展类加载器

    }
}
