#ifndef FILES_H_INCLUDED
#define FILES_H_INCLUDED

#include<stdio.h>
#include<stdlib.h>
#include<sys/types.h>
#include<sys/stat.h>
#include<fcntl.h>
#include<unistd.h>
#include<errno.h>

#endif // FILES_H_INCLUDED

int create_file(char** argv);
int create_file_with_file_name(const char * fileName);
