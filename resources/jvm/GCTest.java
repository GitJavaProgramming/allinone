package jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.List;

/**
 * Oracle doc  Java平台,标准版工具参考
 * https://docs.oracle.com/javase/8/docs/technotes/tools/windows/toc.html
 * 参考其中《java》章节 查看提供的选项（java启动应用程序）
 *
 * C语言实现GC--标记-清除实现
 * 译文 http://ifeve.com/babys-first-garbage-collector/
 * 原文 http://journal.stuffwithstuff.com/2013/12/08/babys-first-garbage-collector/
 * 例如 VM栈定义如下
 * VM* newVM() {
 * 	  VM* vm = malloc(sizeof(VM));
 * 	  vm->stackSize = 0;
 * 	  return vm;
 * }
 * 回收对象：作为"GCRoots"的起始点，如果对象到GCRoots不可达，则说明对象不可用，可GC
 * 堆内存结构：
 * heap
 * -- PSYoungGen
 * ---- Eden
 * ---- Survivor
 * ------ S0 to
 * ------ S1 from
 * -- ParOldGen(object space)
 * -- Metaspace(class space)
 * 对象在Eden区分配，当Eden区没有足够的空间进行分配时，会触发一次Minor GC;老年代存储长期存活的对象和大对象。在发生Minor GC时，虚拟机会检测之前每次晋升到老年代的平均
 * 大小是否大于老年代的剩余空间大小，如果大于，则改为进行一次Full GC
 *
 * 启动参数建议参考oracle文档 Oracle doc  Java平台,标准版工具参考 --> java
 *
 * VM args: -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintCommandLineFlags
 *
 * -XX:SurvivorRatio=8 设置伊甸园空间大小与幸存者空间大小之间的比率。默认情况下，此选项设置为8。
 * 以下公式可用于根据年轻代（Y）的大小和初始幸存者空间比率（R）计算幸存者空间的初始大小（S）：
 * S = Y /（R + 2）
 * 等式中的2表示两个幸存空间。指定为初始生存空间比例的值越大，初始生存空间尺寸就越小。
 * 默认情况下，初始生存者空间比率设置为8。如果使用了年轻代空间大小的默认值（2 MB），则生存者空间的初始大小将为0.2 MB。
 *
 * 分代收集 收集器 收集算法 VM参数
 * client、server模式
 * -- Server模式启用重型虚拟机，所以相对Client模式下启动稍慢，但长期运行的稳定性要优于Client模式。
 * -- 64位是无法切换到Client模式的，默认只能工作在Server模式下  java -version可以看到模式信息
 * client模式下默认GC UseSerialGC
 * server模式下默认GC UseParallelGC 使用Parallel Scavenge + Serial Old的收集器组合进行内存回收
 * Parallel Scavenge 复制算法 并行
 * Serial Old 标记整理
 */
public class GCTest {

    private static final int _1MB = 1024 * 1024; // 1Mb

    /**
     * VM args: -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintCommandLineFlags
     * heap=20M Eden 8M from=1M to=1M oldGen=10M
     * JDK 1.8.0_221
     * 本机测试加上PrintCommandLineFlags参数 可以看到server模式默认GC UseParallelGC
     */
    public static void main(String[] args) {
        byte[] allocation1, allocation2, allocation3, allocation4;
        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        allocation4 = new byte[4 * _1MB]; // 对象优先在Eden分配 出现一次Minor GC
//        byte[] allocation5 = new byte[4 * _1MB]; // 内存分配失败
        /**
         * 一次调试执行结果 PSYoungGen total=9216K ??
         * -XX:InitialHeapSize=20971520 -XX:MaxHeapSize=20971520 -XX:MaxNewSize=10485760 -XX:NewSize=10485760
         * -XX:+PrintCommandLineFlags -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:SurvivorRatio=8
         * -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseParallelGC
         * 0.446: [GC (Allocation Failure) [PSYoungGen: 6309K->767K(9216K)] 6309K->4871K(19456K), 0.0171284 secs] [Times: user=0.00 sys=0.00, real=0.02 secs]
         * Heap
         *  PSYoungGen      total 9216K, used 7314K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
         *   eden space 8192K, 79% used [0x00000000ff600000,0x00000000ffc64e08,0x00000000ffe00000)
         *   from space 1024K, 74% used [0x00000000ffe00000,0x00000000ffebfcb0,0x00000000fff00000)
         *   to   space 1024K, 0% used [0x00000000fff00000,0x00000000fff00000,0x0000000100000000)
         *  ParOldGen       total 10240K, used 4104K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
         *   object space 10240K, 40% used [0x00000000fec00000,0x00000000ff002020,0x00000000ff600000)
         *  Metaspace       used 3070K, capacity 4556K, committed 4864K, reserved 1056768K
         *   class space    used 324K, capacity 392K, committed 512K, reserved 1048576K
         */

//        Runtime.getRuntime().addShutdownHook(new Thread(()-> getJvmInfo()));
    }

    private static void getJvmInfo() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage mu = memoryMXBean.getHeapMemoryUsage(); // 内存使用快照
        /**
         * MXBean规范 MXBean 概念提供了一种对 MBean 进行编码的简单方式，这种方式只引用一组预定义的类型，这些类型由javax.management.openmbean定义。
         * init - Java 虚拟机分配的初始内存量（以字节为单位）；或者，如果未定义，则为 -1。
         * used - 已经使用的内存量（以字节为单位）。
         * committed - 已经提交的内存量（以字节为单位）。
         * max - 可以使用的最大内存量（以字节为单位）；或者，如果未定义，则为 -1。
         */
        System.out.println("堆快照:" + mu/*.toString()*/);
        System.out.println("初始内存量:" + mu.getInit() / _1MB + "Mb");
        System.out.println("可以使用最大内存:" + mu.getMax() / _1MB + "Mb");
        System.out.println("已用内存:" + mu.getUsed() / _1MB + "Mb");

        /**
         * Java 虚拟机管理堆之外的内存（称为非堆内存）。
         * Java 虚拟机具有一个由所有线程共享的方法区。方法区属于非堆内存。它存储每个类结构，如运行时常量池、字段和方法数据，以及方法和构造方法的代码。它是在 Java 虚拟机启动时创建的。
         * 方法区在逻辑上是堆的一部分，但 Java 虚拟机实现可以选择不对其进行回收或压缩。与堆类似，方法区的大小可以固定，也可以扩大和缩小。方法区的内存不需要是连续空间。
         * 除了方法区外，Java 虚拟机实现可能需要用于内部处理或优化的内存，这种内存也是非堆内存。例如，JIT 编译器需要内存来存储从 Java 虚拟机代码转换而来的本机代码，从而获得高性能。
         */
        MemoryUsage none = memoryMXBean.getNonHeapMemoryUsage();
        System.out.println("非堆内存:" + none);

        List<String> args = ManagementFactory.getRuntimeMXBean().getInputArguments();
        System.out.println("运行时VM参数:" + args);

        System.out.println("虚拟机中的内存总量" + Runtime.getRuntime().totalMemory() / _1MB + "Mb");
        /**
         * Java 虚拟机中的空闲内存量。调用 gc 方法可能导致 freeMemory 返回值的增加。
         * 供将来分配对象使用的当前可用内存的近似总量，以字节为单位。
         */
        System.out.println("虚拟机中的空闲内存" + Runtime.getRuntime().freeMemory() / _1MB + "Mb");
        /**
         * Java虚拟机试图使用的最大内存量。如果内存本身没有限制，则返回值 Long.MAX_VALUE。
         */
        System.out.println("试图使用的最大内存量" + Runtime.getRuntime().maxMemory() / _1MB + "Mb");
    }
}
