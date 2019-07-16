# Declare constants for multiboot header.
.set ALIGN, 	1 << 0
.set MEMINFO, 	1 << 1
.set FLAGS,	ALIGN | MEMINFO		# Multiboot flag field
.set MAGIC,	0x1BADB002	# 'Magic Number' lets bootloader find the header
.set CHECKSUM,  - (MAGIC + FLAGS)	# checksum of above, to prove os is multiboot

# Multiboot header starts from here, it is 
# just some magic values that is documented
# in the multiboot standard.

.section .multiboot
.align 4
.long MAGIC
.long FLAGS
.long CHECKSUM

# Currently the (ESP) points at anything!!!(null pointer). Using it
# may cause fatal exception or hardware disaster. So, here we'll provide
# our own stack. We'll allocate memory for a small temporary stack by 
# creating a symbol at the bottom of the (ESP). Then allocating 
# 16384 bytes for it and finally creating a symbol at the top.

.section .bootstrap_stack, "aw", @nobits

stack_bottom:
	.skip 16384	# 16 KiB

# The linker script specifies _start as the entry point to the Kernel and
# the bootloader will jump to this position once the Kernel has been loaded.

stack_top:
	.section .text
	.global	 _start
	.type	 _start, @function

_start:

# Welcome the the KERNEL MODE!
# Now, we have sufficient code for the bootloader to lad and run our OS.
# It doesn't do anything interesting yet. Perhaps we would like to call
# printf("Hello World!\n"). We should now realize one of the profound 
# truths about kernel mode: There is noting there unless you provide it 
# ourself. And that is one of the best things about kernel development:
# We get to make the entire system ourself. We have absolute and complete
# power over the machine, there are no security restrictions, no safe guards,
# no debugging machanisms, there is nothing but what we build.

# Writing assembly is a tiresome job.  We realize some things simply 
# can not be done in C, such as making the multiboot header in the right section
# and setting up the stack. However, we would like to write the OS in a 
# higher level language, such as C or C++. To that end, the next task is 
# preparing the processor for execution of such code. C doesn't expect 
# much at this point and we only need to set up a stack. Note that the processor
# is not fully initialized yet and stuff such as floating point instructions are
# not available yet.

# To set up a stack, we simply aet the ESP register to point to the top of 
# our stack (as it grows downwards).

	movl	$stack_top, %esp

# Now we are ready to execute C code. We cannot embed that in an assembly file,
# so we'll create a kernel.c file. In that file, we'll create a C / C++ entry point
# called 'kernel_main' and call it here.

	call	kernel_main

# In case the function returns, we'll want to put the computer into an infinite loop.
# To do that, we'll use clear interrupt ('cli') instruction to diable interrupts,
# the halt instruction ('hlt') to stop the CPU untill the next interrupt arrives,
# and jumping to the halt instruction if it ever continues execution, just to be
# safe. We'll create a local label rather than real symbol and jump to there 
# endlessly.

	cli
	hlt

.Lhang:
	jmp	.Lhang

# Set the size of the _start symbol to the current location '.' minus its start.
# This is useful for debugging and implementing call tracing.

.size	_start, . - _start
