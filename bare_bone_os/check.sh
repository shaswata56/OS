#!/bin/sh

if grub-file --is-x86-multiboot microOS.bin; then
	echo Multiboot Confirmed!!!
else
	echo [ERROR] The file is not multiboot.
fi
