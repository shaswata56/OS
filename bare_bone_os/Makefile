all:
	cross-compiler/bin/i686-elf-as boot.asm -o boot.o
	cross-compiler/bin/i686-elf-gcc -c kernel.c -o kernel.o -std=gnu99 -ffreestanding -O2 -Wall -Wextra
	cross-compiler/bin/i686-elf-gcc -T linker.ld -o microOS.bin -ffreestanding -O2 -nostdlib boot.o kernel.o -lgcc
	rm kernel.o boot.o
	sh check.sh
