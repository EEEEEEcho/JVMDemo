package chapter07;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;

public class MethodAreaOOMTest extends ClassLoader{
    public static void main(String[] args) {
        //创建大量类对象，让方法区出现OOM
        int j = 0;
        try{
            MethodAreaOOMTest t = new MethodAreaOOMTest();
            for (int i = 0; i < 10000; i++) {
                //创建ClassWriter对象，用于生成类的二进制字节码
                ClassWriter classWriter = new ClassWriter(0);
                //指明：版本号、修饰符、类名、包名、父类、接口
                classWriter.visit(Opcodes.V1_8,Opcodes.ACC_PUBLIC,"Class" + i,null,"java/lang/Object",null);
                //返回byte[]
                byte[] code = classWriter.toByteArray();
                //类的加载
                t.defineClass("Class" + i,code,0,code.length);
                j ++;
            }
        }
        finally {
            System.out.println(j);
        }
    }
}
