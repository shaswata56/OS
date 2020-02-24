package task2;

import java.util.concurrent.locks.ReentrantLock;

class Bank {
    int balance = (int) 1e9;
    private final ReentrantLock sharedResource = new ReentrantLock(true);
    void cut(int amount) {
        sharedResource.lock();
        try {
            if (balance - amount >= 0) {
                balance -= amount;
                System.out.println(Thread.currentThread().getName() + " withdrawn " + amount);
            } else {
                System.out.println("You dont't have sufficient balance!");
            }
        } finally {
            System.out.println("Current balance: "+ balance);
            sharedResource.unlock();
        }
    }
}
