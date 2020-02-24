package task1;

class Bank {
    int balance = (int) 1e9;
    synchronized void cut(int amount) {
        if(balance - amount >= 0) {
            balance -= amount;
            System.out.println(Thread.currentThread().getName()+ " withdrawn "+ amount);
        } else {
            System.out.println("You dont't have sufficient balance!");
        }
        System.out.println("Current balance: "+ balance);
    }
}
