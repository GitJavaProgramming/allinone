使用CodeBlocks自带MinGW
C库头文件在%MinGW_home%include
gcc as gdb等在bin目录下

PS D:\CProjects\cproject> .\app.exe nihao
array->length = 1
arr.length = 0
array->length = 100
array->length = 1
create file success
now remove file
PS D:\CProjects\cproject> history

  Id CommandLine
  -- -----------
   1 Set-Location -literalPath 'D:\CProjects\cproject'
   2 gcc main.c // 编译失败
   3 ls
   4 cat main.c
   5 cat .\files.h
   6 gcc -c struct_test.c create_file.c main.c // 编译 .c 到 .o  源文件 到 目标文件 包含二进制代码和数据 不可直接执行
   7 ls
   8 gcc -S main.c  // 汇编
   9 ls
  10 cat main.s
  11 ls
  12 gcc main.o .\struct_test.o .\create_file.o -o app // 将所有需要重定位的目标文件链接--可执行文件
  13 ls
  14 app
  15 .\app.exe  // 执行exe main方法中create_file()函数需要传入文件名
  16 history
  17 .\app.exe nihao

最后生成可执行文件exe 因为没有实现图形界面所以命令行模式下运行 如./app hello  如果把app加入path就可以直接运行app hello