#if !defined(__cplusplus)
#include <stdbool.h>
#endif

#include <stddef.h>
#include <stdint.h>

/* Safety Check for the compiler if we're targeting wrong OS. */

#if defined(__linux__)
#error "You're not using a cross-compiler, you'll most certainly run into trouble\n"
#endif

/* This kernel will only work for the 32-bit ix86 targets. */

#if !defined(__i386__)
#error "The kernel needs to be compiled with a ix86-elf compiler"
#endif

/*	Hardware text mode color constants.	*/

enum vga_color {
	COLOR_BLACK = 0,
	COLOR_BLUE  = 1,
	COLOR_GREEN = 2,
	COLOR_CYAN  = 3,
	COLOR_RED   = 4,
	COLOR_MAGENTA = 5,
	COLOR_BROWN   = 6,
	COLOR_LIGHT_GREY  = 7,
	COLOR_DARK_GREY   = 8,
	COLOR_LIGHT_BLUE  = 9,
	COLOR_LIGHT_GREEN = 10,
	COLOR_LIGHT_CYAN  = 11,
	COLOR_LIGHT_RED   = 12,
	COLOR_LIGHT_MAGENTA = 13,
	COLOR_LIGHT_BROWN   = 14,
	COLOR_WHITE	    = 15,
};

static inline uint8_t make_color(enum vga_color fg, enum vga_color bg)
{
	return fg | bg << 4;
}

static inline uint16_t make_vgaentry(char c, uint8_t color)
{
	uint16_t c16 = c;
	uint16_t color16 = color;
	return c16 | color16 << 8;
}

static inline size_t strlen(const char* str)
{
	size_t len = 0;
	
	while(str[len] != 0)
		len++;

	return len;
}

/*	For generating random numbers in kernel		*/

uint16_t lfsr = 0xACE1u;
uint16_t bit;

uint16_t s_rand()
{
	bit  = ((lfsr >> 0) ^ (lfsr >> 2) ^ (lfsr >> 3) ^ (lfsr >> 5) ) & 1;
	return lfsr =  (lfsr >> 1) | (bit << 15);
}

uint8_t rand(uint8_t n)
{
	return (uint8_t) (s_rand() % n);
}

uint8_t random_color()
{
	return make_color(rand(16), rand(16));
}

static const size_t VGA_WIDTH = 80;
static const size_t VGA_HEIGHT = 25;

size_t term_row;
size_t term_column;
uint8_t term_color;
uint16_t* term_buffer;

void init_term(void)
{
	term_row = 0;
	term_column = 0;
	term_color = make_color(COLOR_LIGHT_GREEN, COLOR_DARK_GREY);
	term_buffer = (uint16_t*) 0xB8000;

	for(size_t y = 0; y < VGA_HEIGHT; y++)
	{
		for(size_t x = 0; x < VGA_WIDTH; x++)
		{
			const size_t index = y * VGA_WIDTH + x;
			term_buffer[index] = make_vgaentry(' ', term_color);
		}
	}
}

void term_setcolor(uint8_t color)
{
	term_color = color;
}

void term_putentryat(char c, uint8_t color, size_t x, size_t y)
{
	const size_t index = y * VGA_WIDTH + x;
	term_buffer[index] = make_vgaentry(c, color);
}

void term_putchar(char c)
{
	if(c == '\n')
	{
		term_column = 0;
		term_row++;
	}
	else if(c == '\t')
	{
		uint8_t less = VGA_WIDTH - term_column;

		if(less >= 4)
		{
			term_column += 4;
		}
		else
		{
			term_column = 4 - less;
			term_row++;
		}
	}
	else
	{
		term_putentryat(c, term_color, term_column, term_row);
	}

	if(++term_column == VGA_WIDTH)
	{
		term_column = 0;
		if(++term_row == VGA_HEIGHT)
			term_row = 0;
	}
}

void term_writestring(const char* data)
{
	size_t datalen = strlen(data);

	for(size_t i = 0; i < datalen; i++)
	{
		term_putchar(data[i]);
	}
}

void write(const char* str)
{
	term_writestring(str);
}

#if defined(__cplusplus)
extern	"C"
#endif
