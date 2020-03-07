#include<stdio.h>
#include<stdlib.h>
#include<netinet/in.h>
#include<sys/types.h>
#include<string.h>

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
// 绑定到一个地址
if(bind(sockfd, (struct sockaddr*)server, sizeof(*server))) {
	printf("bind failed\n");
}
// 监听
listen(sockfd, SOMAXCONN);
// 等待客户端接入
int clientfd;
struct sockaddr_in* client = (struct sockaddr_in*)malloc(sizeof(struct sockaddr_in));
int client_size = sizeof(*client);
char* buf = (char*)malloc(1000);
int bytes;
printf("Wait for connection to port %u\n", echo_port);
// 接受连接请求

clientfd = accept(sockfd, (struct sockaddr*)client, & client_size);
printf("Connected to %s:%u\n\n", inet_ntoa(client->sin_addr), ntohs(client->sin_port));
printf("Numeric: %u\n", ntohl(client->sin_addr.s_addr));
while(1)
{
bytes = read(clientfd, (void*)buf, 1000);
if(bytes <= 0) 
{
  close(clientfd);
  printf("Connection closed.\n");
  exit(0);
}
printf("Bytes received:%u\n", bytes);
printf("Text: '%s'\n", buf);

write(clientfd, buf, bytes);
}
return 0;
}
