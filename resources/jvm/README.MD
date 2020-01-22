# Java虚拟机
## Java虚拟机内存结构--运行时数据区
    线程共享：堆、方法区、运行时常量池
    线程私有：PC寄存器、Java虚拟机栈、本地方法栈
    
    堆：（类的实例或数组）对象分配内存的区域
    方法区：存储每一个类的结构信息，如运行时常量池、字段和方法数据、特殊方法（如<init>、<Clinit>）等。
    运行时常量池：包含字面量和符号引用（在运行期解析后才能获得方法或字段的引用）
    
    PC寄存器：类似CPU的指令指针寄存器IP，指示当前执行的字节码指令的地址
    Java虚拟机栈：与线程同时创建，用于存储栈帧
    本地方法栈：其他语言编写的支持native方法的执行，一般实现为按线程分配。当Java虚拟机使用其他语言实现指令集解释器时，也可以
    使用本地方法栈。

## javac javap javah javadoc都是用Java编写
源码包 openjdk-7u40-fcs-src-b43-26_aug_2013  
语言工具包源码位置：jdk源码目录\langtools\src\share\classes\com\sun\tools

    ├─apt
    ├─classfile 描述Java class的文件格式
    ├─doclets
    ├─javac Java虚拟机编译器 将*.java编译(Java词法、句法规则、语义与优化)成*.class
    ├─javadoc  Java文档生成器
    ├─javah  生成实现本地方法所需的C头文件和源文件 .h .c 声明与实现
    └─javap  Java虚拟机汇编（非正式的虚拟机汇编语言） -v（--verbose） -c(指反汇编 convert)等选项 查看虚拟机字节码详细指令
目录给出ClassFile类结构---ClassFile.png   
关于Java编译器：Java虚拟机规范 javase 8版 ch03 Java虚拟机编译器   
javac编译成的.class文件(字节数组，虚拟机字节码指令集)可以构造成ClassFile对象   
JIT：Just-In-Time,在class文件中代码被java虚拟机加载后，（虚拟机指令集转换）生成平台相关的特定（CPU）指令集。   

## 加载、链接与初始化
参考 Java虚拟机规范 javase 8版 ch5.3 创建和加载  
Java虚拟机启动是通过引导类加载器创建一个初始类来完成的，这个类是由虚拟机的具体实现指定的。紧接着虚拟机链接这个初始类，   
初始化它并调用它的public static void main(String[] args) {}。之后的整个执行过程都是由此方法开始。  
应用程序允许用户自定义加载器来扩展Java虚拟机功能，以支持动态创建和加载类。  


## 参考资料
* Java虚拟机规范 JavaSE 8 版  
* 深入理解Java虚拟机 JVM高级特性与最佳实践 第二版  
* HotSpot实战  


