package chapter02;

import java.io.FileNotFoundException;

/**
 * 自定义类加载器
 */
public class CustomClassLoader extends ClassLoader{

    /**
     * 开发人员可以通过继承抽象类ava.lang.ClassLoader类的方式，实现自己的类加载器，以满足一些特殊的需求
     * 在JDK1.2之前，在自定义类加载器时，总会去继承ClassLoader类并重写loadClass() 方法，从而实现自定义的
     * 类加载器，但是在JDK1.2之后已不再建议用户去覆盖loadclass() 方法，而是建议把自定义的类加载逻辑写
     * 在findClass()方法中
     * 在编写自定义类加载器时，如果没有太过于复杂的需求，可以直接继承URLClassLoader类，这样就可以避免自己
     * 去编写findClass() 方法及其获取字节码流的方式，使自定义类加载器编写更加简洁。
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try{
            //从自定义的路径中加载字节流
            byte[] result = getClassFromCustomPath(name);
            if (result == null){
                throw new FileNotFoundException();
            }
            else{
                return defineClass(name,result,0, result.length);
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        throw new ClassNotFoundException(name);
    }

    private byte[] getClassFromCustomPath(String name){
        //从自定义路径中加载指定类：没有细节
        //如果指定路径的字节码文件进行了加密，则需要在此方法中进行解密操作
        return null;
    }
}
