all:	boot kernel
	cross-compiler/bin/i686-elf-gcc -T linker.ld -o microOS.bin -ffreestanding -O2 -nostdlib boot.o kernel.o -lgcc
	rm kernel.o boot.o

boot:	boot.asm
	cross-compiler/bin/i686-elf-as boot.asm -o boot.o

kernel: kernel.c
	cross-compiler/bin/i686-elf-gcc -c kernel.c -o kernel.o -std=gnu99 -ffreestanding -O2 -Wall -Wextra

iso:	all
	./check_n_pack.sh
	rm microOS.bin

clean:	
	rm microOS.bin *.o
