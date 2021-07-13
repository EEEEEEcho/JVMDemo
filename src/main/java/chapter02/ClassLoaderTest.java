package chapter02;

public class ClassLoaderTest {
    public static void main(String[] args) {
        //获取系统类加载器
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader);//sun.misc.Launcher$AppClassLoader@18b4aac2
        //获取系统类加载器的上层：扩展类加载器
        ClassLoader extClassLoader = systemClassLoader.getParent();
        System.out.println(extClassLoader); //sun.misc.Launcher$ExtClassLoader@1b6d3586
        //获取扩展类加载器的上层: 引导类加载器,获取不到
        ClassLoader bootstrapClassLoader = extClassLoader.getParent();
        System.out.println(bootstrapClassLoader); //null 获取不到

        //对于用户自定义类来说：默认使用 系统类加载器加载
        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
        System.out.println(classLoader); //sun.misc.Launcher$AppClassLoader@18b4aac2

        //可以发现也获取不到，所以间接推测到是使用引导类加载器加载的
        //所以推出：Java的核心类库都是使用引导类加载器加载的
        ClassLoader strClassLoader = String.class.getClassLoader();
        System.out.println(strClassLoader); //null
    }
}
