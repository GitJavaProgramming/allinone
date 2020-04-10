    Docker核心概念
        镜像 Image  只读模板，容器启动时会在镜像的最上层创建一个可写层
        容器 Container  运行和隔离应用
        仓库 Repository  集中存放镜像文件的场所
    命令
        仓库：Docker Hub ...
        容器：创建|停止|运行|查看|导入导出|删除
        镜像：获取|查看|查找|删除和清理|创建|导入导出|上传
    容器中数据管理
        数据卷：容器内数据直接映射到本地主机环境，一个特殊目录，类似mount
        数据卷容器：使用特定容器维护数据卷
    端口映射和容器互联
        以端口映射方式提供外部访问
        容器互联实现容器间安全交互
    Dockerfile
        由一行行命令语句组成，支持#开头注释
        主体分为四部分：基础镜像信息、维护者信息、镜像操作指令和容器启动时执行指令
        指令：配置指令配置镜像信息、操作指令
    再看Docker
        Docker 是一个开源的应用容器引擎。
        引擎（Engine）是电子平台上开发程序或系统的核心组件。利用引擎，开发者可迅速建立、铺设程序所需的功能，或利用其辅助程序
        的运转。一般而言，引擎是一个程序或一套系统的支持部分。常见的程序引擎有游戏引擎、搜索引擎、杀毒引擎等。
        
        Docker使用Google公司推出的Go语言进行开发实现，基于Linux内核的cgroup，namespace，以及OverlayFS类的Union FS等技术，
        对进程进行封装隔离，属于操作系统层面的虚拟化技术。由于隔离的进程独立于宿主和其它的隔离的进程，因此也称其为容器。
        
        为什么要使用 Docker？
            更高效的利用系统资源
            更快速的启动时间
            一致的运行环境
            持续交付和部署
            更轻松的迁移
            更轻松的维护和扩展
        
    参考资料：
        Docker技术入门与实战 
        https://yeasy.gitbooks.io/docker_practice/content/introduction/what.html
        https://baike.baidu.com/item/%E5%BC%95%E6%93%8E/2874935 
        https://yeasy.gitbooks.io/docker_practice/content/introduction/why.html  
    
    


























