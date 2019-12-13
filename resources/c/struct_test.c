#include<stdio.h>
#include<stdlib.h>

#include "struct_test.h"

static Array * array;

Array create_array(size_t size) {
    array = malloc(size * sizeof(Array));
    array->length = size;
    return *array;
}

_Bool add(DATA_TYPE data) {
    array->data = data;
    (array->length)++;
    return 1;
}

int main(void) {
    Array arr = create_array(0);
    _Bool b = add(100);
    printf("array->length = %u\n", array->length);
    printf("arr.length = %u\n", arr.length);

    printf("array->length = %d\n", array->data);
    printf("array->length = %u\n", array->length);
    return 0;
}
