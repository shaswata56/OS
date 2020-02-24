package task2;

class Atm extends Thread {

    private Bank account;
    private int balance;

    Atm(Bank account, int balance) {
        this.account = account;
        this.balance = balance;
    }

    @Override
    public void run() {
        account.cut(balance);
    }
}
