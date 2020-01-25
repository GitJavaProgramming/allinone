/******************************************************************************
linux
编译、链接、运行
gcc createThread.c -o createThread -lpthread
./createThread
结果
this is a new thread, thread ID= 3074053888
main exit
说明
-lpthread 参数指示在链接时装在函数库libpthread
命令ldd xx查看程序依赖的共享库
root@localhost c-test]# ldd createThread
    linux-vdso.so.1 =>  (0x00007fff0c761000)
    libpthread.so.0 => /lib64/libpthread.so.0 (0x00007f9f21d93000)
    libc.so.6 => /lib64/libc.so.6 (0x00007f9f219c5000)
    /lib64/ld-linux-x86-64.so.2 (0x00007f9f21faf000)
******************************************************************************/
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

int* thread_func(void*arg)
{
    pthread_t newThreadId;
    newThreadId = pthread_self(); // self thread id
    printf("this is a new thread, thread ID= %u\n", newThreadId);
    return NULL;
}

int main(int argc, char** argv)
{
    pthread_t thid;
    if(pthread_create(&thid, NULL, (void *)thread_func, NULL) != 0)
    {
        printf("thread create failed\n");
        exit(1);
    }
    sleep(1);
    printf("main exit");
    exit(0);
    printf("main exit2");
}
