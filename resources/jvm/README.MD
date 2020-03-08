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
    
## 编译与解释 javac javap javah javadoc都是用Java编写
源码包 openjdk-7u40-fcs-src-b43-26_aug_2013  
语言工具包源码位置：jdk源码目录\langtools\src\share\classes\com\sun\tools

    ├─apt
    ├─classfile 描述Java class的文件格式
    ├─doclets
    ├─javac Java虚拟机编译器 将*.java编译(Java词法、句法规则、语义与优化)成*.class
    ├─javadoc  Java文档生成器
    ├─javah  生成实现本地方法所需的C头文件和源文件 .h .c 声明与实现
    └─javap  Java虚拟机汇编（非正式的虚拟机汇编语言） -v（--verbose） -c(指反汇编 convert)等选项 查看虚拟机字节码详细指令
目录给出ClassFile类结构---ClassFile.png  DataInputStream中相关读方法如readUnsignedByte readShort等  
class DataInputStream  
```
public final int readUnsignedShort() throws IOException {
    int ch1 = in.read();
    int ch2 = in.read();
    if ((ch1 | ch2) < 0)
        throw new EOFException();
    return (ch1 << 8) + (ch2 << 0); // short占两个字节 ch1左移8位成高8位 ch2成为低8位 返回int为4字节
}
```
class BufferedInputStream  
```
public synchronized int read() throws IOException {
    if (pos >= count) {
        fill();
        if (pos >= count)
            return -1;
    }
    return getBufIfOpen()[pos++] & 0xff; // 位与操作 1&1=1 其他为0
}
private byte[] getBufIfOpen() throws IOException {
    byte[] buffer = buf;
    if (buffer == null)
        throw new IOException("Stream closed");
    return buffer;
}
// 构造buf
protected volatile byte buf[];
public BufferedInputStream(InputStream in, int size) {
    super(in);
    if (size <= 0) {
        throw new IllegalArgumentException("Buffer size <= 0");
    }
    buf = new byte[size];
}
```
class ByteArrayInputStream  
```
public synchronized int read() {
    return (pos < count) ? (buf[pos++] & 0xff) : -1;
}
```
关于Java编译器：Java虚拟机规范 javase 8版 ch03 Java虚拟机编译器   
javac编译成的.class文件(字节数组)可以构造成ClassFile对象   
JIT：Just-In-Time,在class文件中代码被java虚拟机加载后，（虚拟机指令集转换）生成平台相关的特定（CPU）指令集。   

## 加载、链接与初始化
参考 Java虚拟机规范 javase 8版 ch5.3 创建和加载  
编译语言 预处理-编译-汇编-链接 最后形成可执行文件    
Java虚拟机启动是通过引导类加载器创建一个初始类来完成的，这个类是由虚拟机的具体实现指定的。紧接着虚拟机链接这个初始类，   
初始化它并调用它的public static void main(String[] args) {}。之后的整个执行过程都是由此方法开始。  
应用程序允许用户自定义加载器来扩展Java虚拟机功能，以支持动态创建和加载类。  

链接类或接口包括验证和准备类或接口、它的直接父类、直接父接口、元素类型。解析类和接口中的符号引用是链接过程中可选的部分。  
验证用于确保class文件的二进制表示在结构上是正确的   
准备阶段的任务是创建类和接口的静态字段，并用默认值初始化这些字段  
解析根据运行时常量池里的符号引用来决定具体值（直接引用）--静态解析
    动态连接：每个栈帧都包含一个指向运行时常量池中该帧所属方法的符号引用，在解释执行期间一部分符号引用转化为直接引用  
类或接口的初始化<Clinit> 参考 Java虚拟机规范 javase 8版 ch5.5 初始化 ch2.9 特殊方法  
本地方法的绑定---库/文件的链接  

虚拟机退出
class System
```
public static void exit(int status) {
    Runtime.getRuntime().exit(status);
}
```

## 虚拟机指令集
指令格式 操作码 操作数1 操作数2  
例：aaload=50(0x32)  助记符=操作码     
执行操作：从数组中加载一个reference类型数据到操作数栈   
操作数栈：arrayref,index -> value  

## 基于（操作数）栈的解释执行
静态分派与重载  
虚拟机编译器在重载时通过参数的静态类型而不是实际类型作为判断依据---编译期确定的  
动态分派与重写  
运行期根据实际类型确定方法执行版本--虚拟机解释执行的动态连接  

不会直接用到寄存器，虚拟机实现自行决定把一些访问最频繁的数据（指令指针计数器、栈顶缓存等）放到寄存器中以获取最好的性能。  

## 类加载系统
Java虚拟机提供两种类加载器：Java虚拟机提供的引导类加载器和用户自定义的类加载器。每个用户自定义的类加载器都应该是抽象类  
ClassLoader的某个子类的实例  

网络资料参考
http://blog.itpub.net/374079/viewspace-129971/ 
### 支持的四个功能
#### 延迟加载
    类按需加载。类加载被尽可能的延迟，从而减少内存使用并提高系统响应时间。
#### 类型安全的链接
    动态类加载不能违反JVM的类型安全机制，类加载不需要额外的运行时检查以保证类型安全。在链接时检查却被允许，因为这些检查
    只执行一次。
#### 自定义类加载策略
    程序员可以完全控制动态类的加载过程。例如自定义加载器可以指定加载时类的远程位置或者为某个特定资源分配适当的安全属性。
#### 多名称空间
    类加载器为不同的软件组件分离名称空间。例如Hotjava浏览器从不同的类加载器加载不同的applet资源，这些applet可能包含同名的类，
    但是JVM会将其视为不同类型。


## hotspot
### GC
### 编译期与运行时优化

## 参考资料
* [OpenJDK7 hotspot源码](https://github.com/GitJavaProgramming/openjdk7-src)
* [OpenJDK7 javac源码](https://github.com/GitJavaProgramming/javac)
* Java虚拟机规范 JavaSE 8 版  
* 深入理解Java虚拟机 JVM高级特性与最佳实践 第二版  
* HotSpot实战  
* https://blog.csdn.net/qq_31865983/category_9216603_2.html
* https://blog.csdn.net/renfufei/article/details/54885190

