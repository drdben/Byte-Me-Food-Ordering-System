import java.util.ArrayList;
import java.util.SequencedSet;

public class Account<T> {
    private float balance;
    private ArrayList<Float> credit = new ArrayList<>();
    private ArrayList<T> from = new ArrayList<>();

    public Account(){
        this.balance = 0.0f;
    }

    private void TransactionHistory(){
        System.out.println("-------------TRANSACTIONS-------------");
        System.out.println("     TO/FROM              AMOUNT");
        int n = credit.size();
        for(int i =0; i<n;i++){
            System.out.println("  "+from.get(i)+"  :  "+credit.get(i));
        }
    }

    public float changebalance(float amount){
        balance+=amount;
        return balance;
    }
    public float getBalance() {
        return balance;
    }

    public ArrayList<Float> getCredit() {
        return credit;
    }

    public ArrayList<T> getFrom() {
        return from;
    }

}
