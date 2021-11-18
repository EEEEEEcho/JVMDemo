> 笔记来源：[尚硅谷JVM全套教程，百万播放，全网巅峰（宋红康详解java虚拟机）](https://www.bilibili.com/video/BV1PJ411n7xZ "尚硅谷JVM全套教程，百万播放，全网巅峰（宋红康详解java虚拟机）")
>
> 同步更新：https://gitee.com/vectorx/NOTE_JVM
>
> https://codechina.csdn.net/qq_35925558/NOTE_JVM
>
> https://github.com/uxiahnan/NOTE_JVM

[TOC]

# 4. 虚拟机栈

## 4.1. 虚拟机栈概述

### 4.1.1. 虚拟机栈出现的背景

由于跨平台性的设计，Java的指令都是根据栈来设计的。不同平台CPU架构不同，所以不能设计为基于寄存器的。

<mark>优点是跨平台，指令集小，编译器容易实现，缺点是性能下降，实现同样的功能需要更多的指令</mark>。

### 4.1.2. 初步印象

有不少Java开发人员一提到Java内存结构，就会非常粗粒度地将JVM中的内存区理解为仅有Java堆（heap）和Java栈（stack）？为什么？

### 4.1.3. 内存中的栈与堆

<mark>栈是运行时的单位，而堆是存储的单位</mark>

- 栈解决程序的运行问题，即程序如何执行，或者说如何处理数据。
- 堆解决的是数据存储的问题，即数据怎么放，放哪里，主体数据都是放在堆上

![959680d7-1ed9-48b7-9a24-60a98084956a](../../../pic/959680d7-1ed9-48b7-9a24-60a98084956a.png)

### 4.1.4. 虚拟机栈基本内容

#### Java虚拟机栈是什么？

Java虚拟机栈（Java Virtual Machine Stack），早期也叫Java栈。每个线程在创建时都会创建一个虚拟机栈，其内部保存一个个的栈帧（Stack Frame），对应着一次次的Java方法调用，是线程私有的。

举个例子：

```java
package chapter04;

public class StackTest {
    public void methodA(){
        int i = 10;
        int j = 20;
        methodB();
    }
    public void methodB(){
        int m = 30;
        int n = 40;
    }

    public static void main(String[] args) {
        StackTest st = new StackTest();
        st.methodA();
    }
}
```

![image-20210722094307149](../../../pic/image-20210722094307149.png)

#### 生命周期

生命周期和线程一致

#### 作用

主管Java程序的运行，它保存方法的局部变量（8种数据类型和对象的引用）、部分结果，并参与方法的调用和返回。

#### 栈的特点

栈是一种快速有效的分配存储方式，访问速度仅次于程序计数器。

JVM直接对Java栈的操作只有两个：

- 每个方法执行，伴随着进栈（入栈、压栈）
- 执行结束后的出栈工作

对于栈来说不存在垃圾回收问题（栈存在溢出的情况）

![ae62419a-bf56-4e8b-8af4-216c2c6410d7](../../../pic/ae62419a-bf56-4e8b-8af4-216c2c6410d7.png)

#### 面试题：开发中遇到哪些异常？

**栈中可能出现的异常**

Java 虚拟机规范允许<mark>Java栈的大小是动态的或者是固定不变的</mark>。

- 如果采用固定大小的Java虚拟机栈，那每一个线程的Java虚拟机栈容量可以在线程创建的时候独立选定。如果线程请求分配的栈容量超过Java虚拟机栈允许的最大容量，Java虚拟机将会抛出一个<mark>StackOverflowError </mark>异常。

  ```java
  public class StackErrorTest {
      public static void main(String[] args) {
          main(args);
      }
  }
  ```

  ![2021-07-22 10-15-45屏幕截图](../../../pic/2021-07-22%2010-15-45%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE.png)

- 如果Java虚拟机栈可以动态扩展，并且在尝试扩展的时候无法申请到足够的内存，或者在创建新的线程时没有足够的内存去创建对应的虚拟机栈，那Java虚拟机将会抛出一个<mark> OutOfMemoryError </mark>异常。


```java
public static void main(String[] args) {
    test();
}
public static void test() {
    test();
}
//抛出异常：Exception in thread"main"java.lang.StackoverflowError
//程序不断的进行递归调用，而且没有退出条件，就会导致不断地进行压栈。
```

**设置栈内存大小**

我们可以使用参数 -Xss选项来设置线程的最大栈空间，栈的大小直接决定了函数调用的最大可达深度

官网中的解释：

```
-Xss size
```

Sets the thread stack size (in bytes). Append the letter `k` or `K` to indicate KB, `m` or `M` to indicate MB, and `g` or `G` to indicate GB. The default value depends on the platform:

- Linux/x64 (64-bit): 1024 KB
- macOS (64-bit): 1024 KB
- Oracle Solaris/x64 (64-bit): 1024 KB
- Windows: The default value depends on virtual memory

The following examples set the thread stack size to 1024 KB in different units:

```
Copy-Xss1m
-Xss1024k
-Xss1048576
```

This option is similar to `-XX:ThreadStackSize`.

证明栈大小

```java
public class StackDeepTest{ 
    private static int count=0; 
    public static void recursion(){
        count++; 
        recursion(); 
    }
    public static void main(String args[]){
        try{
            recursion();
        } catch (Throwable e){
            System.out.println("deep of calling="+count); 
            e.printstackTrace();
        }
    }
}
```

测试得到的默认栈大小(Linux)为![2021-07-22 10-29-25屏幕截图](../../../pic/2021-07-22%2010-29-25%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE.png)

然后设置栈的大小

![微信图片_20210722103302](../../../pic/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20210722103302.png)

再次运行之后

![2021-07-22 10-34-21屏幕截图](../../../pic/2021-07-22%2010-34-21%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE.png)

## 4.2. 栈的存储单位

### 4.2.1. 栈中存储什么？

每个线程都有自己的栈，栈中的数据都是以<mark>栈帧（Stack Frame）的格式存在</mark>。

在这个线程上正在执行的每个方法都各自对应一个栈帧（Stack Frame）。方法的执行和栈帧是一对一的关系。

栈帧是一个内存区块，是一个数据集，维系着方法执行过程中的各种数据信息。

### 4.2.2. 栈运行原理

JVM直接对Java栈的操作只有两个，就是对<mark>栈帧的压栈和出栈，遵循“先进后出”/“后进先出”原则</mark>。

在一条活动线程中，一个时间点上，只会有一个活动的栈帧。即只有当前正在执行的方法的栈帧（栈顶栈帧）是有效的，这个栈帧被称为<mark>当前栈帧（Current Frame）</mark>，与当前栈帧相对应的方法就是<mark>当前方法（Current Method）</mark>，定义这个方法的类就是<mark>当前类（Current Class）</mark>。

执行引擎运行的所有字节码指令只针对当前栈帧进行操作。

如果在该方法中调用了其他方法，对应的新的栈帧会被创建出来，放在栈的顶端，成为新的当前帧。

![345eb786-4faa-44a9-b194-a515a308bfb4](../../../pic/345eb786-4faa-44a9-b194-a515a308bfb4.png)

```java
ublic class StackFrameTest {
    public static void main(String[] args) {
        StackFrameTest s = new StackFrameTest();
        s.method1();
    }
    public void method1(){
        System.out.println("method1 开始执行");
        method2();
        System.out.println("method1 执行结束");
    }
    public int method2(){
        System.out.println("method2 开始执行");
        int i = 10;
        method3();
        System.out.println("method2 即将结束");
        return i;
    }
    public double method3(){
        System.out.println("method3 开始执行");
        double j = 10.14;
        System.out.println("method3 即将结束");
        return j;
    }
}

```

![image-20210812212606157](../../../pic/image-20210812212606157.png)

不同线程中所包含的栈帧是不允许存在相互引用的，即不可能在一个栈帧之中引用另外一个线程的栈帧,线程之间是栈隔离的。

如果当前方法调用 了其他方法，方法返回之际，当前栈帧会传回此方法的执行结果给前一个栈帧，接着，虚拟机会丢弃当前栈帧，使得前一个栈帧重新成为当前栈帧。

Java方法有两种返回函数的方式，<mark>一种是正常的函数返回，使用return指令；另外一种是抛出异常（未处理的异常）。不管使用哪种方式，都会导致栈帧被弹出</mark>。

### 4.2.3. 栈帧的内部结构

每个栈帧中存储着：

- <mark>局部变量表（Local Variables）</mark>
- <mark>操作数栈（operand Stack）（或表达式栈）</mark>
- 动态链接（DynamicLinking）（或指向运行时常量池的方法引用）
- 方法返回地址（Return Address）（或方法正常退出或者异常退出的定义）
- 一些附加信息

![f0e51fbf-3d3f-4b50-83ab-2e911bbe6fdd](../../../pic/f0e51fbf-3d3f-4b50-83ab-2e911bbe6fdd.png)

并行每个线程下的栈都是私有的，因此每个线程都有自己各自的栈，并且每个栈里面都有很多栈帧，**栈帧的大小主要由局部变量表 和 操作数栈决定的**，栈的大小（若栈大小不固定）取决于内部可以存放多少栈帧，栈帧的大小主要取决于内部的局部变量表和操作数栈。

从整体上来说

![c678d9d8-f251-495e-b681-1b5797386e4a](../../../pic/c678d9d8-f251-495e-b681-1b5797386e4a.png)

## 4.3. 局部变量表(Local Variables)

局部变量表也被称之为局部变量数组或本地变量表

- <mark>定义为一个**数字数组**，主要用于存储方法参数（从传入方法的形参开始）和定义在方法体内的局部变量</mark>，这些数据类型包括各类基本数据类型、对象引用（reference），以及returnAddress类型。
- 由于局部变量表是建立在线程的栈上，是线程的私有数据，因此<mark>不存在数据安全问题</mark>
- <mark>局部变量表所需的容量大小是在编译期确定下来的</mark>，并保存在方法的Code属性的maximum local variables数据项中。在方法运行期间是不会改变局部变量表的大小的。
- <mark>方法嵌套调用的次数由栈的大小决定</mark>。一般来说，栈越大，方法嵌套调用次数越多。对一个函数而言，它的参数和局部变量越多，使得局部变量表膨胀，它的栈帧就越大，以满足方法调用所需传递的信息增大的需求。进而函数调用就会占用更多的栈空间，导致其嵌套调用次数就会减少。
- <mark>局部变量表中的变量只在当前方法调用中有效</mark>。在方法执行时，虚拟机通过使用局部变量表完成参数值到参数变量列表的传递过程。当方法调用结束后，随着方法栈帧的销毁，局部变量表也会随之销毁。

```java
public class LocalVariablesTest {
    private int count = 0;

    public static void main(String[] args) {
        LocalVariablesTest l = new LocalVariablesTest();
        int num = 10;
        l.test1();
    }

    public void test1(){
        Date date = new Date();
        String name = "ech0";
        String info = test2(date,name);
        System.out.println(date + name);
    }

    public String test2(Date dateP,String name2){
         dateP = null;
         name2 = "jane";
         double weight = 110.0;
         char gender = '男';
         return dateP + name2;
    }

    public void test3(){
        count ++;
    }

    public void test4(){
        int a = 0;
        {
            int b = 0;
            b = a + 1;
        }
        int c = a + 1;
    }
}
```

反编译

```bash
Classfile /G:/JVMDemo/target/classes/chapter04/LocalVariablesTest.class
  Last modified 2021-8-12; size 1606 bytes
  MD5 checksum 7889a006dbd196077ceea43fc9d13ff6
  Compiled from "LocalVariablesTest.java"
public class chapter04.LocalVariablesTest
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #20.#57        // java/lang/Object."<init>":()V
   #2 = Fieldref           #3.#58         // chapter04/LocalVariablesTest.count:I
   #3 = Class              #59            // chapter04/LocalVariablesTest
   #4 = Methodref          #3.#57         // chapter04/LocalVariablesTest."<init>":()V
   #5 = Methodref          #3.#60         // chapter04/LocalVariablesTest.test1:()V
   #6 = Class              #61            // java/util/Date
   #7 = Methodref          #6.#57         // java/util/Date."<init>":()V
   #8 = String             #62            // ech0
   #9 = Methodref          #3.#63         // chapter04/LocalVariablesTest.test2:(Ljava/util/Date;Ljava/lang/String;)Ljava/lang/String;
  #10 = Fieldref           #64.#65        // java/lang/System.out:Ljava/io/PrintStream;
  #11 = Class              #66            // java/lang/StringBuilder
  #12 = Methodref          #11.#57        // java/lang/StringBuilder."<init>":()V
  #13 = Methodref          #11.#67        // java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
  #14 = Methodref          #11.#68        // java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
  #15 = Methodref          #11.#69        // java/lang/StringBuilder.toString:()Ljava/lang/String;
  #16 = Methodref          #70.#71        // java/io/PrintStream.println:(Ljava/lang/String;)V
  #17 = String             #72            // jane
  #18 = Double             110.0d
  #20 = Class              #73            // java/lang/Object
  #21 = Utf8               count
  #22 = Utf8               I
  #23 = Utf8               <init>
  #24 = Utf8               ()V
  #25 = Utf8               Code
  #26 = Utf8               LineNumberTable
  #27 = Utf8               LocalVariableTable
  #28 = Utf8               this
  #29 = Utf8               Lchapter04/LocalVariablesTest;
  #30 = Utf8               main
  #31 = Utf8               ([Ljava/lang/String;)V
  #32 = Utf8               args
  #33 = Utf8               [Ljava/lang/String;
  #34 = Utf8               l
  #35 = Utf8               num
  #36 = Utf8               test1
  #37 = Utf8               date
  #38 = Utf8               Ljava/util/Date;
  #39 = Utf8               name
  #40 = Utf8               Ljava/lang/String;
  #41 = Utf8               info
  #42 = Utf8               test2
  #43 = Utf8               (Ljava/util/Date;Ljava/lang/String;)Ljava/lang/String;
  #44 = Utf8               dateP
  #45 = Utf8               name2
  #46 = Utf8               weight
  #47 = Utf8               D
  #48 = Utf8               gender
  #49 = Utf8               C
  #50 = Utf8               test3
  #51 = Utf8               test4
  #52 = Utf8               b
  #53 = Utf8               a
  #54 = Utf8               c
  #55 = Utf8               SourceFile
  #56 = Utf8               LocalVariablesTest.java
  #57 = NameAndType        #23:#24        // "<init>":()V
  #58 = NameAndType        #21:#22        // count:I
  #59 = Utf8               chapter04/LocalVariablesTest
  #60 = NameAndType        #36:#24        // test1:()V
  #61 = Utf8               java/util/Date
  #62 = Utf8               ech0
  #63 = NameAndType        #42:#43        // test2:(Ljava/util/Date;Ljava/lang/String;)Ljava/lang/String;
  #64 = Class              #74            // java/lang/System
  #65 = NameAndType        #75:#76        // out:Ljava/io/PrintStream;
  #66 = Utf8               java/lang/StringBuilder
  #67 = NameAndType        #77:#78        // append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
  #68 = NameAndType        #77:#79        // append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
  #69 = NameAndType        #80:#81        // toString:()Ljava/lang/String;
  #70 = Class              #82            // java/io/PrintStream
  #71 = NameAndType        #83:#84        // println:(Ljava/lang/String;)V
  #72 = Utf8               jane
  #73 = Utf8               java/lang/Object
  #74 = Utf8               java/lang/System
  #75 = Utf8               out
  #76 = Utf8               Ljava/io/PrintStream;
  #77 = Utf8               append
  #78 = Utf8               (Ljava/lang/Object;)Ljava/lang/StringBuilder;
  #79 = Utf8               (Ljava/lang/String;)Ljava/lang/StringBuilder;
  #80 = Utf8               toString
  #81 = Utf8               ()Ljava/lang/String;
  #82 = Utf8               java/io/PrintStream
  #83 = Utf8               println
  #84 = Utf8               (Ljava/lang/String;)V
{
  public chapter04.LocalVariablesTest();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: aload_0
         5: iconst_0
         6: putfield      #2                  // Field count:I
         9: return
      LineNumberTable:
        line 5: 0
        line 6: 4
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      10     0  this   Lchapter04/LocalVariablesTest;

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=3, args_size=1		# 局部变量表长度，是locals的值，为3
         0: new           #3                  // class chapter04/LocalVariablesTest
         3: dup
         4: invokespecial #4                  // Method "<init>":()V
         7: astore_1
         8: bipush        10
        10: istore_2
        11: aload_1
        12: invokevirtual #5                  // Method test1:()V
        15: return
      LineNumberTable:
        line 9: 0
        line 10: 8
        line 11: 11
        line 12: 15
      LocalVariableTable:			#也可以从这里看出来，main方法局部变量表中有三个参数，第一个是String类型的引用名为args，第二个是LocalVariableTest对象的引用名为l，第三个是一个int类型的变量名为num
        Start  Length  Slot  Name   Signature
            0      16     0  args   [Ljava/lang/String;
            8       8     1     l   Lchapter04/LocalVariablesTest;
           11       5     2   num   I

  public void test1();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=3, locals=4, args_size=1
         0: new           #6                  // class java/util/Date
         3: dup
         4: invokespecial #7                  // Method java/util/Date."<init>":()V
         7: astore_1
         8: ldc           #8                  // String ech0
        10: astore_2
        11: aload_0
        12: aload_1
        13: aload_2
        14: invokevirtual #9                  // Method test2:(Ljava/util/Date;Ljava/lang/String;)Ljava/lang/String;
        17: astore_3
        18: getstatic     #10                 // Field java/lang/System.out:Ljava/io/PrintStream;
        21: new           #11                 // class java/lang/StringBuilder
        24: dup
        25: invokespecial #12                 // Method java/lang/StringBuilder."<init>":()V
        28: aload_1
        29: invokevirtual #13                 // Method java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        32: aload_2
        33: invokevirtual #14                 // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        36: invokevirtual #15                 // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
        39: invokevirtual #16                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
        42: return
      LineNumberTable:
        line 15: 0
        line 16: 8
        line 17: 11
        line 18: 18
        line 19: 42
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      43     0  this   Lchapter04/LocalVariablesTest;
            8      35     1  date   Ljava/util/Date;
           11      32     2  name   Ljava/lang/String;
           18      25     3  info   Ljava/lang/String;

  public java.lang.String test2(java.util.Date, java.lang.String);
    descriptor: (Ljava/util/Date;Ljava/lang/String;)Ljava/lang/String;
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=6, args_size=3
         0: aconst_null
         1: astore_1
         2: ldc           #17                 // String jane
         4: astore_2
         5: ldc2_w        #18                 // double 110.0d
         8: dstore_3
         9: sipush        30007
        12: istore        5
        14: new           #11                 // class java/lang/StringBuilder
        17: dup
        18: invokespecial #12                 // Method java/lang/StringBuilder."<init>":()V
        21: aload_1
        22: invokevirtual #13                 // Method java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        25: aload_2
        26: invokevirtual #14                 // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        29: invokevirtual #15                 // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
        32: areturn
      LineNumberTable:
        line 22: 0
        line 23: 2
        line 24: 5
        line 25: 9
        line 26: 14
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      33     0  this   Lchapter04/LocalVariablesTest;
            0      33     1 dateP   Ljava/util/Date;
            0      33     2 name2   Ljava/lang/String;
            9      24     3 weight   D
           14      19     5 gender   C

  public void test3();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=3, locals=1, args_size=1
         0: aload_0
         1: dup
         2: getfield      #2                  // Field count:I
         5: iconst_1
         6: iadd
         7: putfield      #2                  // Field count:I
        10: return
      LineNumberTable:
        line 30: 0
        line 31: 10
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      11     0  this   Lchapter04/LocalVariablesTest;

  public void test4();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=3, args_size=1
         0: iconst_0
         1: istore_1
         2: iconst_0
         3: istore_2
         4: iload_1
         5: iconst_1
         6: iadd
         7: istore_2
         8: iload_1
         9: iconst_1
        10: iadd
        11: istore_2
        12: return
      LineNumberTable:
        line 34: 0
        line 36: 2
        line 37: 4
        line 39: 8
        line 40: 12
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            4       4     2     b   I
            0      13     0  this   Lchapter04/LocalVariablesTest;
            2      11     1     a   I
           12       1     2     c   I
}
SourceFile: "LocalVariablesTest.java"
```

main方法的局部变量表分析

![image-20210812220053222](../../../pic/image-20210812220053222.png)

或者在jclasslib中查看

![image-20210812225559290](../../../pic/image-20210812225559290.png)

### 4.3.1. 关于Slot的理解

- 局部变量表，最基本的存储单元是Slot（变量槽）
- 参数值的存放总是在局部变量数组的index0开始，到数组长度-1的索引结束。
- 局部变量表中存放编译期可知的各种基本数据类型（8种），引用类型（reference），returnAddress类型的变量。
- 在局部变量表里，32位以内的类型只占用一个slot（包括returnAddress类型），64位的类型（long和double）占用两个slot。

- byte、short、char 在存储前被转换为int，boolean也被转换为int，0表示false，非0表示true。 
- JVM会为局部变量表中的每一个Slot都分配一个访问索引，通过这个索引即可成功访问到局部变量表中指定的局部变量值

- 当一个实例方法被调用的时候，它的方法参数和方法体内部定义的局部变量将会<mark>按照顺序被复制</mark>到局部变量表中的每一个slot上

- <mark>如果需要访问局部变量表中一个64bit的局部变量值时，只需要使用前一个索引即可</mark>。（比如：访问long或doub1e类型变量）
- 如果当前帧是由构造方法或者实例方法创建的，那么<mark>该对象引用this将会存放在index为0的slot处</mark>，其余的参数按照参数表顺序继续排列。


![image-20200705212454445](https://gitee.com/vectorx/ImageCloud/raw/master/img/20210509190245.png)

### 4.3.2. Slot的重复利用

栈帧中的局部变量表中的槽位是可以重用的，如果一个局部变量过了其作用域，那么在其作用域之后申明的新的局部变就很有可能会复用过期局部变量的槽位，从而达到节省资源的目的。

```java
public class SlotTest {
    public void localVarl() {
        int a = 0;
        System.out.println(a);
        int b = 0;
    }
    public void localVar2() {
        {
            int a = 0;
            System.out.println(a);
        }
        //此时的就会复用a的槽位
        int b = 0;
    }
}
```

### 4.3.3. 静态变量与局部变量的对比

参数表分配完毕之后，再根据方法体内定义的变量的顺序和作用域分配。

我们知道类变量表有两次初始化的机会，第一次是在“<mark>准备阶段</mark>”，执行系统初始化，对类变量设置零值，另一次则是在“<mark>初始化</mark>”阶段，赋予程序员在代码中定义的初始值。

和类变量初始化不同的是，局部变量表不存在系统初始化的过程，这意味着一旦定义了局部变量则必须人为的初始化，否则无法使用。

```java
public void test(){
    int i;
    System. out. println(i);
}
```

这样的代码是错误的，没有赋值不能够使用。

### 4.3.4. 补充说明

在栈帧中，与性能调优关系最为密切的部分就是前面提到的局部变量表。在方法执行时，虚拟机使用局部变量表完成方法的传递。

<mark>局部变量表中的变量也是重要的垃圾回收根节点，只要被局部变量表中直接或间接引用的对象都不会被回收</mark>。

## 4.4. 操作数栈（Operand Stack）

每一个独立的栈帧除了包含局部变量表以外，还包含一个后进先出（Last-In-First-Out）的 <mark>操作数栈</mark>，也可以称之为<mark>表达式栈（Expression Stack）</mark>

<mark>操作数栈，在方法执行过程中，根据字节码指令，往栈中写入数据或提取数据，即入栈（push）和 出栈（pop）</mark>

- 某些字节码指令将值压入操作数栈，其余的字节码指令将操作数取出栈。使用它们后再把结果压入栈
- 比如：执行复制、交换、求和等操作

![image-20200706090618332](https://gitee.com/moxi159753/LearningNotes/raw/master/JVM/1_内存与垃圾回收篇/5_虚拟机栈/images/image-20200706090618332.png)

代码举例

```java
public void testAddOperation(){
    byte i = 15; 
    int j = 8; 
    int k = i + j;
}
```

字节码指令信息

```shell
public void testAddOperation(); 
    Code:
    0: bipush 15
    2: istore_1 
    3: bipush 8
    5: istore_2 
    6:iload_1 
    7:iload_2 
    8:iadd
    9:istore_3 
    10:return
```

操作数栈，<mark>主要用于保存计算过程的中间结果，同时作为计算过程中变量临时的存储空间</mark>。

操作数栈就是JVM执行引擎的一个工作区，当一个方法刚开始执行的时候，一个新的栈帧也会随之被创建出来，<mark>这个方法的操作数栈是空的</mark>。

每一个操作数栈都会拥有一个明确的栈深度用于存储数值，其所需的最大深度在编译期就定义好了，保存在方法的Code属性中，为max_stack的值。

栈中的任何一个元素都是可以任意的Java数据类型

- 32bit的类型占用一个栈单位深度
- 64bit的类型占用两个栈单位深度

操作数栈<mark>并非采用访问索引的方式来进行数据访问</mark>的，而是只能通过标准的入栈和出栈操作来完成一次数据访问

<mark>如果被调用的方法带有返回值的话，其返回值将会被压入当前栈帧的操作数栈中</mark>，并更新PC寄存器中下一条需要执行的字节码指令。

操作数栈中元素的数据类型必须与字节码指令的序列严格匹配，这由编译器在编译器期间进行验证，同时在类加载过程中的类检验阶段的数据流分析阶段要再次验证。

另外，我们说Java虚拟机的<mark>解释引擎是基于栈的执行引擎</mark>，其中的栈指的就是操作数栈。

## 4.5. 代码追踪

```
public void testAddOperation() {
    byte i = 15;
    int j = 8;
    int k = i + j;
}
```

使用javap 命令反编译class文件：` javap -v 类名.class`

```java
public void testAddoperation(); 
		Code:
	0: bipush 15 
	2: istore_1 
	3: bipush 8
	5: istore_2
	6: iload_1
	7: iload_2
	8: iadd
	9: istore_3
    10: return
```

![image-20200706093131621](https://gitee.com/vectorx/ImageCloud/raw/master/img/20210509194808.png)

![image-20200706093251302](https://gitee.com/vectorx/ImageCloud/raw/master/img/20210509194813.png)

![image-20200706093646406](https://gitee.com/vectorx/ImageCloud/raw/master/img/20210509194816.png)

![image-20200706093751711](https://gitee.com/vectorx/ImageCloud/raw/master/img/20210509194819.png)

![image-20200706093859191](https://gitee.com/vectorx/ImageCloud/raw/master/img/20210509194822.png)

![image-20200706093921573](https://gitee.com/vectorx/ImageCloud/raw/master/img/20210509194824.png)

![image-20200706094046782](https://gitee.com/vectorx/ImageCloud/raw/master/img/20210509194827.png)

![image-20200706094109629](https://gitee.com/vectorx/ImageCloud/raw/master/img/20210509194829.png)

程序员面试过程中，常见的i++和++i的区别，放到字节码篇章时再介绍。

## 4.6. 栈顶缓存技术（Top Of Stack Cashing）技术

前面提过，基于栈式架构的虚拟机所使用的零地址指令更加紧凑，但完成一项操作的时候必然需要使用更多的入栈和出栈指令，这同时也就意味着将需要更多的指令分派（instruction dispatch）次数和内存读/写次数。

由于操作数是存储在内存中的，因此频繁地执行内存读/写操作必然会影响执行速度。为了解决这个问题，HotSpot JVM的设计者们提出了栈顶缓存（Tos，Top-of-Stack Cashing）技术，<mark>将栈顶元素全部缓存在物理CPU的寄存器中，以此降低对内存的读/写次数，提升执行引擎的执行效率</mark>。

## 4.7. 动态链接（Dynamic Linking）

动态链接、方法返回地址、附加信息 ： 有些地方被称为帧数据区

每一个栈帧内部都包含一个指向<mark>运行时常量池中该栈帧所属方法的引用</mark>。包含这个引用的目的就是为了支持当前方法的代码能够实现动态链接（Dynamic Linking）。比如：invokedynamic指令

在Java源文件被编译到字节码文件中时，所有的变量和方法引用都作为符号引用（Symbolic Reference）保存在class文件的常量池里。比如：描述一个方法调用了另外的其他方法时，就是通过常量池中指向方法的符号引用来表示的，那么<mark>动态链接的作用就是为了将这些符号引用转换为调用方法的直接引用</mark>。

![image-20200706101251847](https://gitee.com/moxi159753/LearningNotes/raw/master/JVM/1_内存与垃圾回收篇/5_虚拟机栈/images/image-20200706101251847.png)

为什么需要运行时常量池呢？

常量池的作用：就是为了提供一些符号和常量，便于指令的识别

## 4.8. 方法的调用：解析与分配

在JVM中，将符号引用转换为调用方法的直接引用与方法的绑定机制相关

### 4.8.1. 静态链接

当一个字节码文件被装载进JVM内部时，如果被调用的<mark>目标方法在编译期可知，且运行期保持不变时</mark>，这种情况下降调用方法的符号引用转换为直接引用的过程称之为静态链接

### 4.8.2. 动态链接

如果<mark>被调用的方法在编译期无法被确定下来，只能够在程序运行期将调用的方法的符号转换为直接引用</mark>，由于这种引用转换过程具备动态性，因此也被称之为动态链接。

<mark>静态链接和动态链接不是名词，而是动词，这是理解的关键。</mark>

---

对应的方法的绑定机制为：早期绑定（Early Binding）和晚期绑定（Late Binding）。<mark>绑定是一个字段、方法或者类在符号引用被替换为直接引用的过程，这仅仅发生一次。</mark>

### 4.8.3. 早期绑定

早期绑定就是指被调用的目标方法如果在编译期可知，且运行期保持不变时，即可将这个方法与所属的类型进行绑定，这样一来，由于明确了被调用的目标方法究竟是哪一个，因此也就可以使用静态链接的方式将符号引用转换为直接引用。

### 4.8.4. 晚期绑定

如果被调用的方法在编译期无法被确定下来，只能够在程序运行期根据实际的类型绑定相关的方法，这种绑定方式也就被称之为晚期绑定。

---

随着高级语言的横空出世，类似于Java一样的基于面向对象的编程语言如今越来越多，尽管这类编程语言在语法风格上存在一定的差别，但是它们彼此之间始终保持着一个共性，那就是都支持封装、继承和多态等面向对象特性，既然<mark>这一类的编程语言具备多态特悄，那么自然也就具备早期绑定和晚期绑定两种绑定方式。</mark>

Java中任何一个普通的方法其实都具备虚函数的特征，它们相当于C++语言中的虚函数（C++中则需要使用关键字virtual来显式定义）。如果在Java程序中不希望某个方法拥有虚函数的特征时，则可以使用关键字final来标记这个方法。

---

### 4.8.5. 虚方法和非虚方法

如果方法在编译期就确定了具体的调用版本，这个版本在运行时是不可变的。这样的方法称为非虚方法。

静态方法、私有方法、final方法、实例构造器、父类方法都是非虚方法。其他方法称为虚方法。

在类加载的解析阶段就可以进行解析，如下是非虚方法举例：

```java
class Father{
    public static void print(String str){
        System. out. println("father "+str); 
    }
    private void show(String str){
        System. out. println("father"+str);
    }
}
class Son extends Father{
    public class VirtualMethodTest{
        public static void main(String[] args){
            Son.print("coder");
            //Father fa=new Father();
            //fa.show("atguigu.com");
        }
    }
```

虚拟机中提供了以下几条方法调用指令：

#### 普通调用指令

- <mark>invokestatic：调用静态方法，解析阶段确定唯一方法版本</mark>
- <mark>invokespecial：调用方法、私有及父类方法，解析阶段确定唯一方法版本</mark>
- invokevirtual：调用所有虚方法
- invokeinterface：调用接口方法

#### 动态调用指令

- invokedynamic：动态解析出需要调用的方法，然后执行

前四条指令固化在虚拟机内部，方法的调用执行不可人为干预，而invokedynamic指令则支持由用户确定方法版本。<mark>其中invokestatic指令和invokespecial指令调用的方法称为非虚方法，其余的（fina1修饰的除外）称为虚方法。</mark>

**关于invokednamic指令**

- JVM字节码指令集一直比较稳定，一直到Java7中才增加了一个invokedynamic指令，这是<mark>Java为了实现「动态类型语言」支持而做的一种改进。</mark>

- 但是在Java7中并没有提供直接生成invokedynamic指令的方法，需要借助ASM这种底层字节码工具来产生invokedynamic指令。<mark>直到Java8的Lambda表达式的出现，invokedynamic指令的生成，在Java中才有了直接的生成方式。</mark>

- Java7中增加的动态语言类型支持的本质是对Java虚拟机规范的修改，而不是对Java语言规则的修改，这一块相对来讲比较复杂，增加了虚拟机中的方法调用，最直接的受益者就是运行在Java平台的动态语言的编译器。


#### 动态类型语言和静态类型语言

动态类型语言和静态类型语言两者的区别就在于对类型的检查是在编译期还是在运行期，满足前者就是静态类型语言，反之是动态类型语言。

说的再直白一点就是，<mark>静态类型语言是判断变量自身的类型信息；动态类型语言是判断变量值的类型信息，变量没有类型信息，变量值才有类型信息</mark>，这是动态语言的一个重要特征。

### 4.8.6. 方法重写的本质

**Java 语言中方法重写的本质：**

1. 找到操作数栈顶的第一个元素所执行的对象的实际类型，记作C。
2. 如果在类型C中找到与常量中的描述符合简单名称都相符的方法，则进行访问权限校验，如果通过则返回这个方法的直接引用，查找过程结束；如果不通过，则返回java.lang.IllegalAccessError 异常。
3. 否则，按照继承关系从下往上依次对C的各个父类进行第2步的搜索和验证过程。
4. 如果始终没有找到合适的方法，则抛出java.1ang.AbstractMethodsrror异常。

**IllegalAccessError介绍**

程序试图访问或修改一个属性或调用一个方法，这个属性或方法，你没有权限访问。一般的，这个会引起编译器异常。这个错误如果发生在运行时，就说明一个类发生了不兼容的改变。

### 4.8.7. 方法的调用：虚方法表

在面向对象的编程中，会很频繁的使用到动态分派，如果在每次动态分派的过程中都要重新在类的方法元数据中搜索合适的目标的话就可能影响到执行效率。<mark>因此，为了提高性能，JVM采用在类的方法区建立一个虚方法表 （virtual method table）（非虚方法不会出现在表中）来实现。使用索引表来代替查找。</mark>

每个类中都有一个虚方法表，表中存放着各个方法的实际入口。

虚方法表是什么时候被创建的呢？

虚方法表会在类加载的链接阶段被创建并开始初始化，类的变量初始值准备完成之后，JVM会把该类的方法表也初始化完毕。

举例1：

![image-20200706144954070](https://gitee.com/moxi159753/LearningNotes/raw/master/JVM/1_内存与垃圾回收篇/5_虚拟机栈/images/image-20200706144954070.png)

举例2：

```java
interface Friendly{
    void sayHello();
    void sayGoodbye(); 
}
class Dog{
    public void sayHello(){
    }
    public String tostring(){
        return "Dog";
    }
}
class Cat implements Friendly {
    public void eat() {
    }
    public void sayHello() { 
    } 
    public void sayGoodbye() {
    }
    protected void finalize() {
    }
}
class CockerSpaniel extends Dog implements Friendly{
    public void sayHello() { 
        super.sayHello();
    }
    public void sayGoodbye() {
    }
}
```

![image-20210509203351535](https://gitee.com/vectorx/ImageCloud/raw/master/img/20210509203352.png)

## 4.9. 方法返回地址（return address）

存放调用该方法的pc寄存器的值。一个方法的结束，有两种方式：

- 正常执行完成
- 出现未处理的异常，非正常退出

无论通过哪种方式退出，在方法退出后都返回到该方法被调用的位置。方法正常退出时，<mark>调用者的pc计数器的值作为返回地址，即调用该方法的指令的下一条指令的地址</mark>。而通过异常退出的，返回地址是要通过异常表来确定，栈帧中一般不会保存这部分信息。

当一个方法开始执行后，只有两种方式可以退出这个方法：

1. 执行引擎遇到任意一个方法返回的字节码指令（return），会有返回值传递给上层的方法调用者，简称<mark>正常完成出口</mark>；
   - 一个方法在正常调用完成之后，究竟需要使用哪一个返回指令，还需要根据方法返回值的实际数据类型而定。
   - 在字节码指令中，返回指令包含ireturn（当返回值是boolean，byte，char，short和int类型时使用），lreturn（Long类型），freturn（Float类型），dreturn（Double类型），areturn。另外还有一个return指令声明为void的方法，实例初始化方法，类和接口的初始化方法使用。
2. 在方法执行过程中遇到异常（Exception），并且这个异常没有在方法内进行处理，也就是只要在本方法的异常表中没有搜索到匹配的异常处理器，就会导致方法退出，简称<mark>异常完成出口</mark>。


方法执行过程中，抛出异常时的异常处理，存储在一个异常处理表，方便在发生异常的时候找到处理异常的代码

```shell
Exception table:
from to target type
4	 16	  19   any
19	 21	  19   any
```

本质上，方法的退出就是当前栈帧出栈的过程。此时，需要恢复上层方法的局部变量表、操作数栈、将返回值压入调用者栈帧的操作数栈、设置PC寄存器值等，让调用者方法继续执行下去。

<mark>正常完成出口和异常完成出口的区别在于：通过异常完成出口退出的不会给他的上层调用者产生任何的返回值。</mark>

## 4.10. 一些附加信息

栈帧中还允许携带与Java虚拟机实现相关的一些附加信息。例如：对程序调试提供支持的信息。

## 4.11. 栈的相关面试题

- 举例栈溢出的情况？（StackOverflowError）
  - 通过 -Xss设置栈的大小
- 调整栈大小，就能保证不出现溢出么？
  - 不能保证不溢出
- 分配的栈内存越大越好么？
  - 不是，一定时间内降低了OOM概率，但是会挤占其它的线程空间，因为整个空间是有限的。
- 垃圾回收是否涉及到虚拟机栈？
  - 不会
- 方法中定义的局部变量是否线程安全？
  - 具体问题具体分析。如果对象是在内部产生，并在内部消亡，没有返回到外部，那么它就是线程安全的，反之则是线程不安全的。

| 运行时数据区 | 是否存在Error | 是否存在GC |
| :----------- | :------------ | :--------- |
| 程序计数器   | 否            | 否         |
| 虚拟机栈     | 是（SOE）     | 否         |
| 本地方法栈   | 是            | 否         |
| 方法区       | 是（OOM）     | 是         |
| 堆           | 是            | 是         |

