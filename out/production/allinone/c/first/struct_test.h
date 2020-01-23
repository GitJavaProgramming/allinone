#ifndef STRUCT_TEST_H_INCLUDED
#define STRUCT_TEST_H_INCLUDED

#include<stdio.h>
#include<stdlib.h>


#endif // STRUCT_TEST_H_INCLUDED

typedef int DATA_TYPE;
typedef struct array {
    DATA_TYPE * data;
    size_t length;
}Array;

Array create_array(size_t size);

_Bool add(DATA_TYPE data);

void test_struct_method();
