
#include "files.h"
/**
[root@localhost c-test]# ./create_file haha
create file success
[root@localhost c-test]# ./create_file haha
open: File exists
[root@localhost c-test]#
*/
int create_file(char** argv) {
    int x = create_file_with_file_name(argv[1]);
    return x;
}
int create_file_with_file_name(const char * fileName) {
    int fd;
	if((fd = open(fileName, O_CREAT|O_EXCL, S_IRUSR|S_IWUSR)) == -1) {
		perror("open");
		return 1;
	} else {
		printf("create file success\n");
	}
	close(fd);
    printf("now remove file");
    remove(fileName);
	return 0;
}
