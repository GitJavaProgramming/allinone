	.file	"main.c"
	.def	___main;	.scl	2;	.type	32;	.endef
	.text
	.globl	_main
	.def	_main;	.scl	2;	.type	32;	.endef
_main:
	pushl	%ebp
	movl	%esp, %ebp
	andl	$-16, %esp
	subl	$16, %esp
	call	___main
	call	_test_struct_method
	movl	12(%ebp), %eax
	movl	%eax, (%esp)
	call	_create_file
	movl	$0, %eax
	leave
	ret
	.ident	"GCC: (tdm-1) 5.1.0"
	.def	_test_struct_method;	.scl	2;	.type	32;	.endef
	.def	_create_file;	.scl	2;	.type	32;	.endef
