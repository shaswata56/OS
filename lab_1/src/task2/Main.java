package task2;

public class Main {
    public static void main(String[] args) {
        Bank account = new Bank();
        Atm atm1 = new Atm(account, 5000);
        Atm atm2 = new Atm(account, 100000);
        Atm atm3 = new Atm(account, 1500);
        Atm atm4 = new Atm(account, 2000000);

        atm1.start();
        atm2.start();
        atm3.start();
        atm4.start();

        try {
            atm1.join();
            atm2.join();
            atm3.join();
            atm4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Net Balance: "+ account.balance);
        }
    }
}
