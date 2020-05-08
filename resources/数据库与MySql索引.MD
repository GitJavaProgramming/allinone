# mysql在没有认真研究源码之前都是浮云
    MySql是一个关系数据库系统,用C++语言写成,本质是一个命令行终端(分析字符串并管理数据)程序
    
    数据模型：关系模型
        数据模型用于抽象,表示和处理现实世界中的数据和信息.
        在用户看来,关系模型中数据是一张二维表,由行和列组成.关系必须是规范化的,满足一定规范条件：如不允许表中还有表
        FAQ. 概念模型,逻辑模型和物理模型 ？？
    数据库：长期储存在计算机内,有组织的,可共享的大量数据的集合
    数据库管理系统(由某种编程语言实现的软件)功能：
        提供数据定义语言(DDL 如create drop) 定义数据库中的数据对象
        数据组织,存储和管理
        数据操纵(DML 如select update)
        数据库的事务管理和运行管理
        数据库的建立和维护功能
    数据库系统：引入数据库的软件(软件对于计算机系统来说是进程).主要包括数据库,数据库管理系统,应用系统,数据库管理员.
    数据库系统对用户屏蔽复杂性,以简化用户和系统的交互,抽象出不同层次：物理层 逻辑层 视图层 根据抽象层次数据库系统可以分为几种
    不同的模式：
        物理模式在物理层描述数据库的设计（物理存储 数据在数据库中的存储形式）
        逻辑模式在逻辑层描述数据库的设计（总体数据的逻辑特征抽象）
        视图层可以有几种模式称为子模式,它描述数据的不同视图
## mysql InnoDB引擎的存储设计
    数据库系统并不直接使用操作系统提供的文件系统作为直接的存储,它在文件系统之上封装了一层自己对于存储设备的管理,以保证数据库中
    数据的完整性
    
    1. 表空间
    对于InnoDB储存引擎 数据文件最小的存储单位是页(page),默认大小为16KB.在页的基础上又逻辑地分为区(extent) 段(segment)和
    表空间(tablespace)
    page: 文件系统的块构成 MySQL v3.23中 InnoDB定义了2种页类型 索引页(index page)和undo日志页(undo log page)
        物理磁盘存在一个个扇区(sector) 大小为512字节 扇区以0和1二进制形式存储数据 为方便管理数据文件系统将扇区映射为块(block)
    extent: 区是空间申请的最小单位 一个区由64个连续的页组成,大小为1MB
    segment: InnoDB存储引擎表中的数据都是根据主键值以B+树索引的方式组织 也称索引组织表 (索引结构后面再讲)因此在InnoDB存储设计
        中 每张用户表至少有两个段 聚集索引的叶子节点段(leaf segment)和非叶子节点段(non leaf segment).段以区为单位组织存储空间
        [2]【备注】 数据库段是占物理空间的数据库对象例如表和索引 不知是不是和这里的段对应？
    tablespace:表空间是一个逻辑概念,由页 区 段组成 一个表空间可以由多个文件组成 
    
    2. 索引
    InnoDB中表由索引组织故索引即数据 索引页就是记录时如何在索引中存储和组织的
    索引页：具体结构查阅其他资料
    page header保存了页的存储信息如相对空闲的偏移量 若要进行记录的查询操作需要通过page directory.
    
    B+树查询只能查询到记录所在的页位置 精确定位到记录在页内的位置则需要通过page directory
    InnoDB将B+树索引分为聚集索引(clustered)和辅助索引(secondary index)
        聚集索引通过表的主键作为键值来构造B+树 聚集索引的非叶子节点保存<键值, 地址>对 地址为指向下一层的指针 InnoDB存储引擎
        通过页在表空间中的偏移量来表示
        辅助索引又称二级索引 它的叶子结点保存的是<键值, 主键键值>
        
    3. 事务隔离
        读未提交--没提交也可以读 read uncommitted
        不可重复读（读提交）--有提交的就可以读  read committed
        可重复读--单个事务内读一致  repeatable read
        序列化--串行化 锁表  serializable
    
        隔离级别：   
            select @@global.tx_isolation; select @@tx_isolation;
            set [global|session] transaction isolation level Read uncommitted|Read committed|Repeatable|Serializable;
        自动提交：
            设置会话：select @@autocommit; set [global|session] autocommit=0;
        显示事务：start transaction
        
        本机测试 Server version: 5.7.17 MySQL Community Server (GPL)
        不论是否设置自动提交都是自动提交，在配置文件my. 
            [mysqld]
            autocommit=0
         
         mysql> desc account;
         +---------+---------+------+-----+---------+-------+
         | Field   | Type    | Null | Key | Default | Extra |
         +---------+---------+------+-----+---------+-------+
         | account | float   | YES  |     | NULL    |       |
         | id      | int(11) | NO   | PRI | NULL    |       |
         +---------+---------+------+-----+---------+-------+
            
         开启两个会话
         select @@global.tx_isolation;
         select @@tx_isolation;
         set [global|session] transaction isolation level Read uncommitted|Read committed|Repeatable|Serializable;
         -----------------------------------------------
         start transaction;
         select * from account;
         update account set account = account + 1;
         select * from account;
         rollback;
         commit;
         
         本机测试并没有得到想要的结果！！！
         例如Read uncommitted级别，事务1中更新数据，不提交，在事务2中不能读到更新后的数据。
         
        参考 
        https://zhuanlan.zhihu.com/p/57302592
        https://blog.csdn.net/rwangnqian/article/details/80087381
## 源码分析
    源码下载 https://github.com/mysql/mysql-server
    InnoDB存储引擎目录： ~/storage/innobase
        page: 页的实现,研究InnoDB文件结构
        trx: 事务的实现
    sql解析

# 参考资料
* 数据库系统概念 第6版 
* mysql内核:INNODB存储引擎 卷1 
* [2] https://zh.wikipedia.org/wiki/表空间
* http://mysql.taobao.org/monthly/2019/10/01/
* https://zhuanlan.zhihu.com/p/35811482
* https://segmentfault.com/a/1190000008545713
* http://www.searchdoc.cn/rdbms/mysql/dev.mysql.com/doc/refman/5.7/en/innodb-storage-engine.com.coder114.cn.html
* https://blog.csdn.net/ajian005/article/details/8482649
