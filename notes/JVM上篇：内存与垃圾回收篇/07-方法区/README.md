笔记来源：[尚硅谷JVM全套教程，百万播放，全网巅峰（宋红康详解java虚拟机）](https://www.bilibili.com/video/BV1PJ411n7xZ "尚硅谷JVM全套教程，百万播放，全网巅峰（宋红康详解java虚拟机）")

> 同步更新：https://gitee.com/vectorx/NOTE_JVM
>
> https://codechina.csdn.net/qq_35925558/NOTE_JVM
>
> https://github.com/uxiahnan/NOTE_JVM

[TOC]

# 7. 方法区

运行时数据区结构图

![772b8c1a-207d-424e-a140-3a75c2252bef](README.assets/772b8c1a-207d-424e-a140-3a75c2252bef-16479533096261.png)

从线程共享与否的角度来看（方法区具体的实现是元空间）

![356e9e64-a703-4338-bfad-ef5e9361422e](README.assets/356e9e64-a703-4338-bfad-ef5e9361422e.png)

## 7.1. 栈、堆、方法区的交互关系

![637e9597-7159-4443-8e77-1dbb5f588cb5](README.assets/637e9597-7159-4443-8e77-1dbb5f588cb5.png)

## 7.2. 方法区的理解

官方文档：[Chapter 2. The Structure of the Java Virtual Machine (oracle.com)](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.5.4)

![0f073338-0a62-4887-a92f-13463b5124fa](README.assets/0f073338-0a62-4887-a92f-13463b5124fa.png)

### 7.2.1. 方法区在哪里？

《Java虚拟机规范》中明确说明：“尽管所有的方法区**在逻辑上是属于堆的一部分**，但一些简单的实现可能不会选择去进行垃圾收集或者进行压缩。”但对于HotSpotJVM而言，方法区还有一个别名叫做Non-Heap（非堆），目的就是要和堆分开。

所以，<mark>方法区看作是一块独立于Java堆的内存空间</mark>。

![dec8bf65-bd7f-4072-9e38-8ace3e2e77e9](README.assets/dec8bf65-bd7f-4072-9e38-8ace3e2e77e9.png)



### 7.2.2. 方法区的基本理解

- 方法区（Method Area）与Java堆一样，是各个线程共享的内存区域。
- 方法区在JVM启动的时候被创建，并且**它的实际的物理内存空间中和Java堆区一样都可以是不连续的。**
- 方法区的大小，跟堆空间一样，可以选择固定大小或者可扩展。
- 方法区的大小决定了系统可以保存多少个类，如果系统定义了太多的类，导致方法区溢出，虚拟机同样会抛出内存溢出错误：`java.lang.OutOfMemoryError: PermGen space` 或者`java.lang.OutOfMemoryError: Metaspace`  
  - <mark>加载大量的第三方的jar包；Tomcat部署的工程过多（30~50个）；大量动态的生成反射类都会产生方法区的溢出</mark>
- 关闭JVM就会释放这个区域的内存。

### 7.2.3. HotSpot中方法区的演进

在jdk7及以前，习惯上把方法区，称为永久代。jdk8开始，使用元空间取代了永久代。

![image-20220322211143778](README.assets/image-20220322211143778.png)

本质上，方法区和永久代并不等价。仅是对hotspot而言的。《Java虚拟机规范》对如何实现方法区，不做统一要求。例如：BEA JRockit / IBM J9 中不存在永久代的概念。

现在来看，当年使用永久代，不是好的idea。导致Java程序更容易OOM（超过`-XX:MaxPermsize`上限）

![image-20220322212132220](README.assets/image-20220322212132220.png)

而到了**JDK8**，终于完全废弃了永久代的概念，改用与JRockit、J9一样在**本地内存中实现的元空间**（Metaspace）来代替

![image-20220322212153745](README.assets/image-20220322212153745.png)

元空间的本质和永久代类似，都是对JVM规范中方法区的实现。不过元空间与永久代最大的区别在于：<mark>元空间不在虚拟机设置的内存中，而是使用本地内存</mark>

永久代、元空间二者并不只是名字变了，内部结构也调整了。

方法区是《Java虚拟机规范》中规定要实现的一个数据存储的地方，可以理解为是一个“接口”，在JDK7之前使用的是永久代来实现的这个接口，但是这个永久代使用的是JVM的内存，实现并不好。在JDK8之后采用的是元空间来实现的这个接口，使用的是机器的直接内存。

根据《Java虚拟机规范》的规定，如果方法区无法满足新的内存分配需求时，将抛出OOM异常

## 7.3. 设置方法区大小与OOM

### 7.3.1. 设置方法区内存的大小

方法区的大小不必是固定的，JVM可以根据应用的需要动态调整。

**jdk7及以前**

- <mark>通过`-XX:Permsize`来设置永久代初始分配空间。默认值是20.75M</mark>
- <mark>通过`-XX:MaxPermsize`来设定永久代最大可分配空间。32位机器默认是64M，64位机器模式是82M</mark>
- 当JVM加载的类信息容量超过了这个值，会报异常`OutOfMemoryError:PermGen space`。

![image-20220322212643090](README.assets/image-20220322212643090.png)

**JDK8以后**

- 元数据区大小可以使用参数 `-XX:MetaspaceSize` 和 `-XX:MaxMetaspaceSize`指定

- 默认值依赖于平台。windows下，`-XX:MetaspaceSize=21M -XX:MaxMetaspaceSize=-1//即没有限制`。

  ```java
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
  ```

  ![image-20220322213540174](README.assets/image-20220322213540174.png)

- 与永久代不同，如果不指定大小，默认情况下，虚拟机会耗尽所有的可用系统内存。如果元数据区发生溢出，虚拟机一样会抛出异常`OutOfMemoryError:Metaspace`

- `-XX:MetaspaceSize`：设置初始的元空间大小。对于一个64位的服务器端JVM来说，其默认的`-XX:MetaspaceSize`值为21MB。这就是初始的高水位线，一旦触及这个水位线，超出21M进行FullGC，Full GC将会被触发并卸载没用的类（即这些类对应的类加载器不再存活），然后这个高水位线将会重置。新的高水位线的值取决于GC后释放了多少元空间。如果释放的空间不足，那么在不超过`MaxMetaspaceSize`时，适当提高该值。如果释放空间过多，则适当降低该值。

- 如果初始化的高水位线设置过低，上述高水位线调整情况会发生很多次。通过垃圾回收器的日志可以观察到Full GC多次调用。为了避免频繁地GC，建议将`-XX:MetaspaceSize`设置为一个相对较高的值。

**举例1：《深入理解Java虚拟机》的例子**

![image-20220322213934089](README.assets/image-20220322213934089.png)

**举例2**

```java
/**
 * jdk8中：
 * -XX:MetaspaceSize=10m -XX:MaxMetaspaceSize=10m
 * jdk6中：
 * -XX:PermSize=10m-XX:MaxPermSize=10m
 */
public class OOMTest extends ClassLoader{
    public static void main(String[] args){
        int j = 0;
        try{
            OOMTest test = new OOMTest();
            for (int i=0;i<10000;i++){
                //创建Classwriter对象，用于生成类的二进制字节码
                ClassWriter classWriter = new ClassWriter(0);
                //指明版本号，public，类名，包名，父类，接口
                classWriter.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC, "Class" + i, nu1l, "java/lang/Object", null);
                //返回byte[]
                byte[] code = classWriter.toByteArray();
                //类的加载
                test.defineClass("Class" + i, code, 0, code.length); //CLass对象
                j++;
            }
        } finally{
            System.out.println(j);
        }
    }
}
```

正常执行

```bash
10000
```

使用参数限制元空间的大小为10M之后

```bash
3331
Exception in thread "main" java.lang.OutOfMemoryError: Compressed class space
	at java.lang.ClassLoader.defineClass1(Native Method)
	at java.lang.ClassLoader.defineClass(ClassLoader.java:756)
	at java.lang.ClassLoader.defineClass(ClassLoader.java:635)
	at chapter07.MethodAreaOOMTest.main(MethodAreaOOMTest.java:20)
```

### 7.3.2. 如何解决这些OOM

1. 要解决OOM异常或heap space的异常，一般的手段是首先通过内存映像分析工具（如Eclipse Memory Analyzer）对dump出来的堆转储快照进行分析，重点是确认内存中的对象是否是必要的，也就是要先分清楚到底是出现了内存泄漏（Memory Leak,类似于僵尸进程，就是该对象不会使用了，但是一直有引用指向它，没办法回收）还是内存溢出（Memory Overflow）

2. 如果是内存泄漏，可进一步通过工具查看泄漏对象到GC Roots的引用链。于是就能找到泄漏对象是通过怎样的路径与GCRoots相关联并导致垃圾收集器无法自动回收它们的。掌握了泄漏对象的类型信息，以及GCRoots引用链的信息，就可以比较准确地定位出泄漏代码的位置。

2. 如果不存在内存泄漏，换句话说就是内存中的对象确实都还必须存活着，那就应当检查虚拟机的堆参数（`-Xmx`与`-Xms`），与机器物理内存对比看是否还可以调大，从代码上检查是否存在某些对象生命周期过长、持有状态时间过长的情况，尝试减少程序运行期的内存消耗。

## 7.4. 方法区的内部结构

![117de0e1-bd70-4935-9241-724e7a9a8320](README.assets/117de0e1-bd70-4935-9241-724e7a9a8320.png)



### 7.4.1. 方法区（Method Area）存储什么？

《深入理解Java虚拟机》书中对方法区（Method Area）存储内容描述如下：

> 它用于存储已被虚拟机加载的类型信息、常量、静态变量、即时编译器编译后的代码缓存等。

![0afcbe80-2364-4dd7-afdd-bbf3262728fc](README.assets/0afcbe80-2364-4dd7-afdd-bbf3262728fc.png)

### 7.4.2. 方法区的内部结构

#### 类型信息

对每个加载的类型（类class、接口interface、枚举enum、注解annotation），JVM必须在方法区中存储以下类型信息：

1. 这个类型的完整有效名称（全名=包名.类名）
2. 这个类型直接父类的完整有效名（对于interface或是java.lang.object，都没有父类）
3. 这个类型的修饰符（public，abstract，final的某个子集）
4. 这个类型直接接口的一个有序列表

#### 域（Field）信息（成员变量/属性)

JVM必须在方法区中保存类型的所有域的相关信息以及域的声明顺序。

域的相关信息包括：域名称、域类型、域修饰符（public，private，protected，static，final，volatile，transient的某个子集）

#### 方法（Method）信息

JVM必须保存所有方法的以下信息，同域信息一样包括声明顺序：

1. 方法名称
2. 方法的返回类型（或void）
3. 方法参数的数量和类型（按顺序）
4. 方法的修饰符（public，private，protected，static，final，synchronized，native，abstract的一个子集）
5. 方法的字节码（bytecodes）、操作数栈、局部变量表及大小（abstract和native方法除外）
6. 异常表（abstract和native方法除外）
   - 每个异常处理的开始位置、结束位置、代码处理在程序计数器中的偏移地址、被捕获的异常类的常量池索引

#### 查看方法区的内部结构

```java
/**
 * 查看方法区的内部结构
 */
public class MethodInnerStructTest extends Object implements Comparable<String>, Serializable {
    //属性
    public int num = 10;
    private static String str = "测试方法的内部结构";
    //构造器
    public MethodInnerStructTest(){
        
    }
    //方法
    public void test1(){
        int count = 20;
        System.out.println("count = " + count);
    }

    public static int test2(int cal){
        int result = 0;
        try {
            int value = 30;
            result = value / cal;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int compareTo(String o) {
        return 0;
    }
}
```

使用javap -v -p 反编译字节码

```bash
Classfile /F:/JVMDemo/target/classes/chapter07/MethodInnerStructTest.class
  Last modified 2022-3-28; size 1619 bytes
  MD5 checksum 1f0d55ebca2c522a19fa712bf37366e2
  Compiled from "MethodInnerStructTest.java"
-------------类型信息--------------  
public class chapter07.MethodInnerStructTest extends java.lang.Object implements java.lang.Comparable<java.lang.String>, java.io.Serializable
-------------类型信息--------------  
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #18.#52        // java/lang/Object."<init>":()V
   #2 = Fieldref           #17.#53        // chapter07/MethodInnerStructTest.num:I
   #3 = Fieldref           #54.#55        // java/lang/System.out:Ljava/io/PrintStream;
   #4 = Class              #56            // java/lang/StringBuilder
   #5 = Methodref          #4.#52         // java/lang/StringBuilder."<init>":()V
   #6 = String             #57            // count =
   #7 = Methodref          #4.#58         // java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
   #8 = Methodref          #4.#59         // java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
   #9 = Methodref          #4.#60         // java/lang/StringBuilder.toString:()Ljava/lang/String;
  #10 = Methodref          #61.#62        // java/io/PrintStream.println:(Ljava/lang/String;)V
  #11 = Class              #63            // java/lang/Exception
  #12 = Methodref          #11.#64        // java/lang/Exception.printStackTrace:()V
  #13 = Class              #65            // java/lang/String
  #14 = Methodref          #17.#66        // chapter07/MethodInnerStructTest.compareTo:(Ljava/lang/String;)I
  #15 = String             #67            // 测试方法的内部结构
  #16 = Fieldref           #17.#68        // chapter07/MethodInnerStructTest.str:Ljava/lang/String;
  #17 = Class              #69            // chapter07/MethodInnerStructTest
  #18 = Class              #70            // java/lang/Object
  #19 = Class              #71            // java/lang/Comparable
  #20 = Class              #72            // java/io/Serializable
  #21 = Utf8               num
  #22 = Utf8               I
  #23 = Utf8               str
  #24 = Utf8               Ljava/lang/String;
  #25 = Utf8               <init>
  #26 = Utf8               ()V
  #27 = Utf8               Code
  #28 = Utf8               LineNumberTable
  #29 = Utf8               LocalVariableTable
  #30 = Utf8               this
  #31 = Utf8               Lchapter07/MethodInnerStructTest;
  #32 = Utf8               test1
  #33 = Utf8               count
  #34 = Utf8               test2
  #35 = Utf8               (I)I
  #36 = Utf8               value
  #37 = Utf8               e
  #38 = Utf8               Ljava/lang/Exception;
  #39 = Utf8               cal
  #40 = Utf8               result
  #41 = Utf8               StackMapTable
  #42 = Class              #63            // java/lang/Exception
  #43 = Utf8               compareTo
  #44 = Utf8               (Ljava/lang/String;)I
  #45 = Utf8               o
  #46 = Utf8               (Ljava/lang/Object;)I
  #47 = Utf8               <clinit>
  #48 = Utf8               Signature
  #49 = Utf8               Ljava/lang/Object;Ljava/lang/Comparable<Ljava/lang/String;>;Ljava/io/Serializable;
  #50 = Utf8               SourceFile
  #51 = Utf8               MethodInnerStructTest.java
  #52 = NameAndType        #25:#26        // "<init>":()V
  #53 = NameAndType        #21:#22        // num:I
  #54 = Class              #73            // java/lang/System
  #55 = NameAndType        #74:#75        // out:Ljava/io/PrintStream;
  #56 = Utf8               java/lang/StringBuilder
  #57 = Utf8               count =
  #58 = NameAndType        #76:#77        // append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
  #59 = NameAndType        #76:#78        // append:(I)Ljava/lang/StringBuilder;
  #60 = NameAndType        #79:#80        // toString:()Ljava/lang/String;
  #61 = Class              #81            // java/io/PrintStream
  #62 = NameAndType        #82:#83        // println:(Ljava/lang/String;)V
  #63 = Utf8               java/lang/Exception
  #64 = NameAndType        #84:#26        // printStackTrace:()V
  #65 = Utf8               java/lang/String
  #66 = NameAndType        #43:#44        // compareTo:(Ljava/lang/String;)I
  #67 = Utf8               测试方法的内部结构
  #68 = NameAndType        #23:#24        // str:Ljava/lang/String;
  #69 = Utf8               chapter07/MethodInnerStructTest
  #70 = Utf8               java/lang/Object
  #71 = Utf8               java/lang/Comparable
  #72 = Utf8               java/io/Serializable
  #73 = Utf8               java/lang/System
  #74 = Utf8               out
  #75 = Utf8               Ljava/io/PrintStream;
  #76 = Utf8               append
  #77 = Utf8               (Ljava/lang/String;)Ljava/lang/StringBuilder;
  #78 = Utf8               (I)Ljava/lang/StringBuilder;
  #79 = Utf8               toString
  #80 = Utf8               ()Ljava/lang/String;
  #81 = Utf8               java/io/PrintStream
  #82 = Utf8               println
  #83 = Utf8               (Ljava/lang/String;)V
  #84 = Utf8               printStackTrace
{
-------------域信息 -------------  
  public int num;
    descriptor: I
    flags: ACC_PUBLIC

  private static java.lang.String str;
    descriptor: Ljava/lang/String;
    flags: ACC_PRIVATE, ACC_STATIC
-------------域信息 -------------  

-------------构造器 --------------  
  public chapter07.MethodInnerStructTest();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: aload_0
         5: bipush        10
         7: putfield      #2                  // Field num:I
        10: return
      LineNumberTable:
        line 13: 0
        line 10: 4
        line 15: 10
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      11     0  this   Lchapter07/MethodInnerStructTest;
-------------构造器 --------------  

-------------方法信息 --------------  
  public void test1();
    descriptor: ()V
    flags: ACC_PUBLIC
    ----- 方法中的字节码 -----
    Code:
      stack=3, locals=2, args_size=1
         0: bipush        20
         2: istore_1
         3: getstatic     #3                  // Field java/lang/System.out:Ljava/io/PrintStream;
         6: new           #4                  // class java/lang/StringBuilder
         9: dup
        10: invokespecial #5                  // Method java/lang/StringBuilder."<init>":()V
        13: ldc           #6                  // String count =
        15: invokevirtual #7                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        18: iload_1
        19: invokevirtual #8                  // Method java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        22: invokevirtual #9                  // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
        25: invokevirtual #10                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
        28: return
      ----- 方法中的字节码 -----
      LineNumberTable:
        line 18: 0
        line 19: 3
        line 20: 28
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      29     0  this   Lchapter07/MethodInnerStructTest;
            3      26     1 count   I
-------------方法信息 -------------- 

-------------方法信息 -------------- 
  public static int test2(int);
    descriptor: (I)I
    flags: ACC_PUBLIC, ACC_STATIC
    ----- 方法中的字节码 -----
    Code:
      stack=2, locals=3, args_size=1
         0: iconst_0
         1: istore_1
         2: bipush        30
         4: istore_2
         5: iload_2
         6: iload_0
         7: idiv
         8: istore_1
         9: goto          17
        12: astore_2
        13: aload_2
        14: invokevirtual #12                 // Method java/lang/Exception.printStackTrace:()V
        17: iload_1
        18: ireturn
      --- 异常表 ---
      Exception table:
         from    to  target type
             2     9    12   Class java/lang/Exception
      --- 异常表 ---
      ----- 方法中的字节码 -----       
      LineNumberTable:
        line 23: 0
        line 25: 2
        line 26: 5
        line 30: 9
        line 28: 12
        line 29: 13
        line 31: 17
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            5       4     2 value   I
           13       4     2     e   Ljava/lang/Exception;
            0      19     0   cal   I
            2      17     1 result   I
      StackMapTable: number_of_entries = 2
        frame_type = 255 /* full_frame */
          offset_delta = 12
          locals = [ int, int ]
          stack = [ class java/lang/Exception ]
        frame_type = 4 /* same */
-------------方法信息 -------------- 

-------------方法信息 -------------- 
  public int compareTo(java.lang.String);
    descriptor: (Ljava/lang/String;)I
    flags: ACC_PUBLIC
    ----- 方法中的字节码 -----
    Code:
      stack=1, locals=2, args_size=2
         0: iconst_0
         1: ireturn
      LineNumberTable:
        line 36: 0
     ----- 方法中的字节码 -----   
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       2     0  this   Lchapter07/MethodInnerStructTest;
            0       2     1     o   Ljava/lang/String;
-------------方法信息 -------------- 

-------------方法信息 -------------- 
  public int compareTo(java.lang.Object);
    descriptor: (Ljava/lang/Object;)I
    flags: ACC_PUBLIC, ACC_BRIDGE, ACC_SYNTHETIC
    Code:
      stack=2, locals=2, args_size=2
         0: aload_0
         1: aload_1
         2: checkcast     #13                 // class java/lang/String
         5: invokevirtual #14                 // Method compareTo:(Ljava/lang/String;)I
         8: ireturn
      LineNumberTable:
        line 8: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       9     0  this   Lchapter07/MethodInnerStructTest;
-------------方法信息 -------------- 
  static {};
    descriptor: ()V
    flags: ACC_STATIC
    Code:
      stack=1, locals=0, args_size=0
         0: ldc           #15                 // String 测试方法的内部结构
         2: putstatic     #16                 // Field str:Ljava/lang/String;
         5: return
      LineNumberTable:
        line 11: 0
}
Signature: #49                          // Ljava/lang/Object;Ljava/lang/Comparable<Ljava/lang/String;>;Ljava/io/Serializable;
SourceFile: "MethodInnerStructTest.java"
```

#### non-final的类变量

- 静态变量和类关联在一起，随着类的加载而加载，他们成为**类数据在逻辑上的一部分**
- 类变量被类的所有实例共享，即使没有类实例时，你也可以访问它

```java
public class MethodAreaTest {
    public static void main(String[] args) {
        Order order = null;
        order.hello();
        System.out.println(order.count);
    }
}
class Order{
    public static int count = 1;
    public static final int number = 2;
    public static void hello(){
        System.out.println("Hello World");
    }
}
```

```bash
Hello World
1
```

#### 补充说明：全局常量（static final）

被声明为final的static变量的处理方法则不同，每个全局常量在编译的时候就会被分配了。

```java
class Order{
    public static int count = 1;
    //常量
    public static final int number = 2;
    public static void hello(){
        System.out.println("Hello World");
    }
}
```

```bash
Classfile /F:/JVMDemo/target/classes/chapter07/Order.class
  Last modified 2022-3-28; size 606 bytes
  MD5 checksum e0cc6256d49c628f9345901c09f4e6fd
  Compiled from "MethodAreaTest.java"
class chapter07.Order
  minor version: 0
  major version: 52
  flags: ACC_SUPER
Constant pool:
   #1 = Methodref          #7.#24         // java/lang/Object."<init>":()V
   #2 = Fieldref           #25.#26        // java/lang/System.out:Ljava/io/PrintStream;
   #3 = String             #27            // Hello World
   #4 = Methodref          #28.#29        // java/io/PrintStream.println:(Ljava/lang/String;)V
   #5 = Fieldref           #6.#30         // chapter07/Order.count:I
   #6 = Class              #31            // chapter07/Order
   #7 = Class              #32            // java/lang/Object
   #8 = Utf8               count
   #9 = Utf8               I
  #10 = Utf8               number
  #11 = Utf8               ConstantValue
  #12 = Integer            2
  #13 = Utf8               <init>
  #14 = Utf8               ()V
  #15 = Utf8               Code
  #16 = Utf8               LineNumberTable
  #17 = Utf8               LocalVariableTable
  #18 = Utf8               this
  #19 = Utf8               Lchapter07/Order;
  #20 = Utf8               hello
  #21 = Utf8               <clinit>
  #22 = Utf8               SourceFile
  #23 = Utf8               MethodAreaTest.java
  #24 = NameAndType        #13:#14        // "<init>":()V
  #25 = Class              #33            // java/lang/System
  #26 = NameAndType        #34:#35        // out:Ljava/io/PrintStream;
  #27 = Utf8               Hello World
  #28 = Class              #36            // java/io/PrintStream
  #29 = NameAndType        #37:#38        // println:(Ljava/lang/String;)V
  #30 = NameAndType        #8:#9          // count:I
  #31 = Utf8               chapter07/Order
  #32 = Utf8               java/lang/Object
  #33 = Utf8               java/lang/System
  #34 = Utf8               out
  #35 = Utf8               Ljava/io/PrintStream;
  #36 = Utf8               java/io/PrintStream
  #37 = Utf8               println
  #38 = Utf8               (Ljava/lang/String;)V
{
  # 这里可以看到public static int count = 1;	没有加final关键字，这里只是对count进行了描述
  public static int count;
    descriptor: I
    flags: ACC_PUBLIC, ACC_STATIC
  # public static final int number = 2;这里有了final关键字，则即包含了描述，也对它进行了初始化
  public static final int number;
    descriptor: I
    flags: ACC_PUBLIC, ACC_STATIC, ACC_FINAL
    ConstantValue: int 2		# 初始化的值为2
  
  chapter07.Order();
    descriptor: ()V
    flags:
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 10: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Lchapter07/Order;

  public static void hello();
    descriptor: ()V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=0, args_size=0
         0: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
         3: ldc           #3                  // String Hello World
         5: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
         8: return
      LineNumberTable:
        line 14: 0
        line 15: 8
  # 这个其实就是<clinit>方法，对静态变量进行复制	
  static {};
    descriptor: ()V
    flags: ACC_STATIC
    Code:
      stack=1, locals=0, args_size=0
         0: iconst_1
         1: putstatic     #5                  // Field count:I
         4: return
      LineNumberTable:
        line 11: 0
}
SourceFile: "MethodAreaTest.java"
```



### 7.4.3. 运行时常量池 VS 常量池

![c46442c3-a805-4224-a521-3bef46055bb6](README.assets/c46442c3-a805-4224-a521-3bef46055bb6.png)

- 方法区，内部包含了运行时常量池
- 字节码文件，内部包含了常量池
- 要弄清楚方法区，需要理解清楚ClassFile，因为加载类的信息都在方法区。
- 要弄清楚方法区的运行时常量池，需要理解清楚ClassFile中的常量池。

官方文档：[https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html)

![15958167-cbfb-4375-826b-525b9721413c](README.assets/15958167-cbfb-4375-826b-525b9721413c.png)

一个有效的字节码文件中除了包含类的版本信息、字段、方法以及接口等描述符信息外，还包含一项信息就是常量池表（Constant Pool Table），包括各种字面量和对类型、域和方法的符号引用

![image-20220328210124747](README.assets/image-20220328210124747.png)

**什么是字面量？**

![image-20220328210011594](README.assets/image-20220328210011594.png)

#### 为什么需要常量池？

一个java源文件中的类、接口，编译后产生一个字节码文件。而Java中的字节码需要数据支持，通常这种数据会很大以至于不能直接存到字节码里，换另一种方式，可以存到常量池，这个字节码包含了指向常量池的引用。在动态链接的时候会用到运行时常量池，之前有介绍。

比如：如下的代码：

```java
public class SimpleClass {
    public void sayHello() {
        System.out.println("hello");
    }
}
```

虽然只有194字节，但是里面却使用了String、System、PrintStream及Object等结构。这里的代码量其实很少了，如果代码多的话，引用的结构将会更多，这里就需要用到常量池了。

![image-20220328210158454](README.assets/image-20220328210158454.png)

以test1()方法中的字节码指令为例

![image-20220328210700785](README.assets/image-20220328210700785.png)

![image-20220328210754657](README.assets/image-20220328210754657.png)

其实可以类比生活中，每一个方法都是一道菜，常量池中的各个值就是各种原料。使用常量池中的各种原料来完成各种菜，即各种方法。

#### 常量池中有什么?

其中常量池内存储的数据类型包括：

- 数量值
- 字符串值
- 类引用
- 字段引用
- 方法引用

例如下面这段代码：

```java
public class MethodAreaTest2 {
    public static void main(String args[]) {
        Object obj = new Object();
    }
}
```

`Object obj = new Object();`将会被翻译成如下字节码：

```java
0: new #2  // Class java/lang/Object
1: dup
2: invokespecial // Method java/lang/Object "<init>"() V
```

#### 小结

常量池、可以看做是一张表，虚拟机指令根据这张常量表找到要执行的类名、方法名、参数类型、字面量等类型

### 7.4.4. 运行时常量池

- 运行时常量池（Runtime Constant Pool）是方法区的一部分。
- <mark>常量池表（Constant Pool Table）是Class文件的一部分，用于存放编译期生成的各种字面量与符号引用，这部分内容将在类加载后存放到方法区的运行时常量池中。</mark>
- 运行时常量池，在加载类和接口到虚拟机后，就会创建对应的运行时常量池。
- JVM为每个已加载的类型（类或接口）都维护一个常量池。池中的数据项像数组项一样，是通过<mark>索引访问</mark>的。
  ![image-20220328211540046](README.assets/image-20220328211540046.png)
- **运行时常量池中包含多种不同的常量，包括编译期就已经明确的数值字面量，也包括到运行期解析后才能够获得的方法或者字段引用。此时不再是常量池中的符号地址了，这里换为<mark>真实地址</mark>**。
- 运行时常量池，相对于Class文件常量池的另一重要特征是：具备<mark>动态性</mark>。
  - String.intern()方法：如果当前字符串没有在常量池中，则把该字符串放入常量池

- 运行时常量池类似于传统编程语言中的符号表（symboltable），但是它所包含的数据却比符号表要更加丰富一些。
- 当创建类或接口的运行时常量池时，如果构造运行时常量池所需的内存空间超过了方法区所能提供的最大值，则JVM会抛OutOfMemoryError异常。

## 7.5. 方法区使用举例

```java
public class MethodAreaDemo {
    public static void main(String args[]) {
        int x = 500;
        int y = 100;
        int a = x / y;
        int b = 50;
        System.out.println(a+b);
    }
}
```

```java
Classfile /F:/JVMDemo/target/classes/chapter07/MethodAreaDemo.class
  Last modified 2022-3-28; size 624 bytes
  MD5 checksum fea937f80b9f5ff58b8456e9bdce6b98
  Compiled from "MethodAreaDemo.java"
public class chapter07.MethodAreaDemo
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #5.#24         // java/lang/Object."<init>":()V
   #2 = Fieldref           #25.#26        // java/lang/System.out:Ljava/io/PrintStream;
   #3 = Methodref          #27.#28        // java/io/PrintStream.println:(I)V
   #4 = Class              #29            // chapter07/MethodAreaDemo
   #5 = Class              #30            // java/lang/Object
   #6 = Utf8               <init>
   #7 = Utf8               ()V
   #8 = Utf8               Code
   #9 = Utf8               LineNumberTable
  #10 = Utf8               LocalVariableTable
  #11 = Utf8               this
  #12 = Utf8               Lchapter07/MethodAreaDemo;
  #13 = Utf8               main
  #14 = Utf8               ([Ljava/lang/String;)V
  #15 = Utf8               args
  #16 = Utf8               [Ljava/lang/String;
  #17 = Utf8               x
  #18 = Utf8               I
  #19 = Utf8               y
  #20 = Utf8               a
  #21 = Utf8               b
  #22 = Utf8               SourceFile
  #23 = Utf8               MethodAreaDemo.java
  #24 = NameAndType        #6:#7          // "<init>":()V
  #25 = Class              #31            // java/lang/System
  #26 = NameAndType        #32:#33        // out:Ljava/io/PrintStream;
  #27 = Class              #34            // java/io/PrintStream
  #28 = NameAndType        #35:#36        // println:(I)V
  #29 = Utf8               chapter07/MethodAreaDemo
  #30 = Utf8               java/lang/Object
  #31 = Utf8               java/lang/System
  #32 = Utf8               out
  #33 = Utf8               Ljava/io/PrintStream;
  #34 = Utf8               java/io/PrintStream
  #35 = Utf8               println
  #36 = Utf8               (I)V
{
  public chapter07.MethodAreaDemo();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 3: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Lchapter07/MethodAreaDemo;

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=3, locals=5, args_size=1
         0: sipush        500
         3: istore_1
         4: bipush        100
         6: istore_2
         7: iload_1
         8: iload_2
         9: idiv
        10: istore_3
        11: bipush        50
        13: istore        4
        15: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
        18: iload_3
        19: iload         4
        21: iadd
        22: invokevirtual #3                  // Method java/io/PrintStream.println:(I)V
        25: return
      LineNumberTable:
        line 5: 0
        line 6: 4
        line 7: 7
        line 8: 11
        line 9: 15
        line 10: 25
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      26     0  args   [Ljava/lang/String;
            4      22     1     x   I
            7      19     2     y   I
           11      15     3     a   I
           15      11     4     b   I
}
SourceFile: "MethodAreaDemo.java"
```

![1](README.assets/1.png)

![2](README.assets/2.png)

![3](README.assets/3.png)

![4](README.assets/4.png)

![5](README.assets/5.png)

![6](README.assets/6.png)

![7](README.assets/7.png)

![8](README.assets/8.png)

![9](README.assets/9.png)

![10](README.assets/10.png)

![11](README.assets/11.png)

![12](README.assets/12.png)

![13](README.assets/13.png)

![14](README.assets/14.png)

![15](README.assets/15.png)

![16](README.assets/16.png)

## 7.6. 方法区的演进细节

1. 首先明确：只有Hotspot才有永久代。BEA JRockit、IBMJ9等来说，是不存在永久代的概念的。原则上如何实现方法区属于虚拟机实现细节，不受《Java虚拟机规范》管束，并不要求统一
2. Hotspot中方法区的变化：

| JDK1.6及之前 | 有永久代（permanet），静态变量存储在永久代上                 |
| :----------- | :----------------------------------------------------------- |
| **JDK1.7**   | **有永久代，但已经逐步 “去永久代”，字符串常量池，静态变量移除，保存在堆中** |
| **JDK1.8**   | **无永久代，类型信息，字段，方法，常量保存在本地内存的元空间，但字符串常量池、静态变量仍然在堆中。** |

![image-20220414155351233](README.assets/image-20220414155351233.png)

![image-20220414155749746](README.assets/image-20220414155749746.png)

![image-20220414155853760](README.assets/image-20220414155853760.png)

![image-20220414155841809](README.assets/image-20220414155841809.png)

### 7.6.1. 为什么永久代要被元空间替代？

官网地址：[JEP 122: Remove the Permanent Generation (java.net)](http://openjdk.java.net/jeps/122)

![image-20220414160136913](README.assets/image-20220414160136913.png)

JRockit是和HotSpot融合后的结果，因为JRockit没有永久代，所以他们不需要配置永久代

随着Java8的到来，HotSpot VM中再也见不到永久代了。但是这并不意味着类的元数据信息也消失了。这些数据被移到了一个<mark>与堆不相连的本地内存区域，这个区域叫做元空间（Metaspace）</mark>。

由于类的元数据分配在本地内存中，元空间的最大可分配空间就是系统可用内存空间。

这项改动是很有必要的，原因有：

- 为永久代设置空间大小是很难确定的。在某些场景下，如果动态加载类过多，容易产生Perm区的oom。比如某个实际Web工 程中，因为功能点比较多，在运行过程中，要不断动态加载很多类，经常出现致命错误。

  ```java
  "Exception in thread 'dubbo client x.x connector' java.lang.OutOfMemoryError:PermGen space"
  ```

  而元空间和永久代之间最大的区别在于：元空间并不在虚拟机中，而是使用本地内存。 因此，默认情况下，元空间的大小仅受本地内存限制。

- 对永久代进行调优是很困难的。

有些人认为方法区（如HotSpot虚拟机中的元空间或者永久代）是没有垃圾收集行为的，其实不然。《Java虚拟机规范》对方法区的约束是非常宽松的，提到过可以不要求虚拟机在方法区中实现垃圾收集。事实上也确实有未实现或未能完整实现方法区类型卸载的收集器存在（如JDK 11时期的ZGC收集器就不支持类卸载）。 一般来说这个区域的回收效果比较难令人满意，尤其是类型的卸载，条件相当苛刻。但是这部分区域的回收有时又确实是必要的。以前Sun公司的Bug列表中，曾出现过的若干个严重的Bug就是由于低版本的HotSpot虚拟机对此区域未完全回收而导致内存泄漏

方法区的垃圾收集主要回收两部分内容：常量池中废弃的常量和不再使用的类型

### 7.6.2. StringTable为什么要调整位置？

jdk7中将StringTable放到了堆空间中。因为永久代的回收效率很低，在full gc的时候才会触发。而full gc是老年代的空间不足、永久代不足时才会触发。

这就导致StringTable回收效率不高。而我们开发中会有大量的字符串被创建，回收效率低，导致永久代内存不足。放到堆里，能及时回收内存。

### 7.6.3. 静态变量存放在那里？

```java
/**
 * 静态引用对应的对象实体始终都存在堆空间
 * jdk7:
 * -Xms200m -Xmx200m -XX:PermSize=300m -XX:MaxPermSize=300m -XX:+PrintGCDetails
 * jdk8:
 * -Xms200m -Xmx200m-XX:MetaspaceSize=300m -XX:MaxMetaspaceSize=300m -XX:+PrintGCDetails
 */
public class StaticFieldTest {
    private static byte[] arr = new byte[1024 * 1024 * 100];
    public static void main(String[] args) {
        System.out.println(StaticFieldTest.arr);
        
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
```

```java
/**
 * staticobj、instanceobj、Localobj存放在哪里？
 */
public class StaticobjTest {
    static class Test {
        static ObjectHolder staticobj = new ObjectHolder();
        ObjectHolder instanceobj = new ObjectHolder();
        void foo(){
            ObjectHolder localobj = new ObjectHolder();
            System.out.println("done");
        }    
    }
    private static class ObjectHolder{
        public static void main(String[] args) {
            Test test = new StaticobjTest.Test();
            test.foo();
        }
    }
}
```

使用JHSDB工具进行分析，这里细节略掉

![image-20200708215218078](https://gitee.com/vectorx/ImageCloud/raw/master/img/20210510164814.png)

staticobj随着Test的类型信息存放在方法区，instanceobj随着Test的对象实例存放在Java堆，localobject则是存放在foo()方法栈帧的局部变量表中。

![image-20200708215025527](https://gitee.com/vectorx/ImageCloud/raw/master/img/20210510165837.png)

测试发现：三个对象的数据在内存中的地址都落在Eden区范围内，所以结论：只要是对象实例必然会在Java堆中分配。

接着，找到了一个引用该staticobj对象的地方，是在一个java.lang.Class的实例里，并且给出了这个实例的地址，通过Inspector查看该对象实例，可以清楚看到这确实是一个java.lang.Class类型的对象实例，里面有一个名为staticobj的实例字段：

从《Java虚拟机规范》所定义的概念模型来看，所有Class相关的信息都应该存放在方法区之中，但方法区该如何实现，《Java虚拟机规范》并未做出规定，这就成了一件允许不同虚拟机自己灵活把握的事情。JDK7及其以后版本的HotSpot虚拟机选择把静态变量与类型在Java语言一端的映射class对象存放在一起，存储于Java堆之中，从我们的实验中也明确验证了这一点

## 7.7. 方法区的垃圾回收

有些人认为方法区（如Hotspot虚拟机中的元空间或者永久代）是没有垃圾收集行为的，其实不然。《Java虚拟机规范》对方法区的约束是非常宽松的，提到过可以不要求虚拟机在方法区中实现垃圾收集。事实上也确实有未实现或未能完整实现方法区类型卸载的收集器存在（如JDK11时期的zGC收集器就不支持类卸载）。

一般来说<mark>这个区域的回收效果比较难令人满意，尤其是类型的卸载，条件相当苛刻</mark>。但是这部分区域的回收<mark>有时又确实是必要的</mark>。以前sun公司的Bug列表中，曾出现过的若干个严重的Bug就是由于低版本的HotSpot虚拟机对此区域未完全回收而导致内存泄漏。

<mark>方法区的垃圾收集主要回收两部分内容：常量池中废弃的常量和不再使用的类型。</mark>

先来说说方法区内常量池之中主要存放的两大类常量：字面量和符号引用。字面量比较接近Java语言层次的常量概念，如文本字符串、被声明为final的常量值等。而符号引用则属于编译原理方面的概念，包括下面三类常量：

- 类和接口的全限定名
- 字段的名称和描述符
- 方法的名称和描述符

HotSpot虚拟机对常量池的回收策略是很明确的，<mark>只要常量池中的常量没有被任何地方引用，就可以被回收</mark>。

回收废弃常量与回收Java堆中的对象非常类似。

判定一个常量是否“废弃”还是相对简单，而要判定一个类型是否属于“不再被使用的类”的条件就比较苛刻了。需要同时满足下面三个条件：

- <mark>该类所有的实例都已经被回收</mark>，也就是Java堆中不存在该类及其任何派生子类的实例。 

- <mark>加载该类的类加载器已经被回收</mark>，这个条件除非是经过精心设计的可替换类加载器的场景，如OSGi、JSP的重加载等，否则通常是很难达成的。

- <mark>该类对应的java.lang.Class对象没有在任何地方被引用</mark>，无法在任何地方通过反射访问该类的方法。

Java虚拟机被允许对满足上述三个条件的无用类进行回收，这里说的仅仅是“被允许”，而并不是和对象一样，没有引用了就必然会回收。关于是否要对类型进行回收，HotSpot虚拟机提供了`-Xnoclassgc`参数进行控制，还可以使用`-verbose:class` 以及 `-XX:+TraceClassLoading`、`-XX:+TraceClassUnLoading`查看类加载和卸载信息

在大量使用反射、动态代理、CGLib等字节码框架，动态生成JSP以及OSGi这类频繁自定义类加载器的场景中，<u>通常都需要Java虚拟机具备类型卸载的能力，以保证不会对方法区造成过大的内存压力</u>。

## 总结

![image-20200708220303243](https://gitee.com/vectorx/ImageCloud/raw/master/img/20210510165830.png)

## 常见面试题

> <mark>百度</mark>：
>
> 说一下JVM内存模型吧，有哪些区？分别干什么的？
>
> 
>
> <mark>蚂蚁金服</mark>： 
>
> Java8的内存分代改进 JVM内存分哪几个区，每个区的作用是什么？ 
>
> 一面：JVM内存分布/内存结构？栈和堆的区别？堆的结构？为什么两个survivor区？ 
>
> 二面：Eden和survior的比例分配
>
> 
>
> <mark>小米</mark>：
>
> jvm内存分区，为什么要有新生代和老年代
>
> 
>
> <mark>字节跳动</mark>： 
>
> 二面：Java的内存分区 
>
> 二面：讲讲vm运行时数据库区 什么时候对象会进入老年代？
>
> 
>
> <mark>京东</mark>： 
>
> JVM的内存结构，Eden和Survivor比例。
>
> JVM内存为什么要分成新生代，老年代，持久代。
>
> 新生代中为什么要分为Eden和survivor。
>
> 
>
> <mark>天猫</mark>： 
>
> 一面：Jvm内存模型以及分区，需要详细到每个区放什么。
>
>  一面：JVM的内存模型，Java8做了什么改
>
> 
>
> <mark>拼多多</mark>： 
>
> JVM内存分哪几个区，每个区的作用是什么？
>
> 
>
> <mark>美团</mark>：
>
>  java内存分配 jvm的永久代中会发生垃圾回收吗？
>
>  一面：jvm内存分区，为什么要有新生代和老年代？