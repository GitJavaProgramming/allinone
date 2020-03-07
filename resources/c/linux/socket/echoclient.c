#include <stdio.h>
#include <netinet/in.h>
#include <sys/types.h>
#include <string.h>
#include <stdlib.h>

int main(int argc, char** argv)
{
char* echo_host = "192.168.0.132";
int echo_port = 7777;
int sockfd;
struct sockaddr_in* server = (struct sockaddr_in*)malloc(sizeof(struct sockaddr_in));
server->sin_family = AF_INET;
server->sin_port = htons(echo_port);
server->sin_addr.s_addr = inet_addr(echo_host);
// 创建套接字
sockfd = socket(AF_INET, SOCK_STREAM, 0);
// 连接到服务器
printf("Connecting to %s \n", echo_host);
connect(sockfd, (struct sockaddr*)server, sizeof(*server));
// 发送消息
char* msg = "Hello World!";
printf("\nSend: '%s'\n", msg);
write(sockfd, msg, strlen(msg));
// 接收返回结果
char* buf = (char*)malloc(1000);
int bytes = read(sockfd, (void*)buf, 1000);
printf("\nBytes receviced: %u\n", bytes);
printf("Text: '%s'\n", buf);
// 关闭套接字
close(sockfd);
return 0;
 }
