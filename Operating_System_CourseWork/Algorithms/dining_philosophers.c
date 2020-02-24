#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>

typedef struct {
	int pos;
	int cnt;
	sem_t *forks;
	sem_t *lock;
} params_t;

void init_semaphores(sem_t *lock, sem_t *forks, int num_forks);
void run_all(pthread_t *threads, sem_t *forks, sem_t *lock, int num_phils);

void *philosopher(void *params);
void think(int pos);
void eat(int position);

int main(int argc, char *args[])
{
	int num_phils = 5;
	sem_t lock;
	sem_t forks[num_phils];
	pthread_t phils[num_phils];
	
	init_semaphores(&lock, forks, num_phils);
	run_all(phils, forks, &lock, num_phils);
	pthread_exit(NULL);
	return 0;
}

void init_semaphores(sem_t *lock, sem_t *forks, int num_forks)
{
	int i;
	for(i = 0; i < num_forks; i++)
		sem_init(&forks[i], 0, 1);
	sem_init(lock, 0, num_forks - 1);
}

void run_all(pthread_t *threads, sem_t *forks, sem_t *lock, int num_phils)
{
	int i;
	for(i = 0; i < num_phils; i++)
	{
		params_t *arg = malloc(sizeof(params_t));
		arg->pos = i;
		arg->cnt = num_phils;
		arg->lock = lock;
		arg->forks = forks;
		pthread_create(&threads[i], NULL, philosopher, (void *)arg);
	}
}

void *philosopher(void *params)
{
	int i;
	params_t self = *(params_t *)params;

	for(i = 0; i < 3; i++)
	{
		think(self.pos);

		sem_wait(self.lock);
		sem_wait(&self.forks[self.pos]);
		sem_wait(&self.forks[(self.pos + 1) % self.cnt]);
		eat(self.pos);
		sem_post(&self.forks[self.pos]);
		sem_post(&self.forks[(self.pos + 1) % self.cnt]);
		sem_post(self.lock);
	}
	think(self.pos);
	pthread_exit(NULL);
}

void think(int pos)
{
	printf("Philosopher %d thinking...\n", pos);
}

void eat(int pos)
{
	printf("Philosopher %d eating...\n", pos);
}
