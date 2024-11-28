import java.io.Serializable;
import java.util.Scanner;

public class Admin extends Account implements Serializable {
    transient Scanner sc = new Scanner(System.in);
    private String pass = "webroke";
    public Account<Customer> adminAcc = new Account();

    public String getPass() {
        return pass;
    }

    @Override
    public String toString() {
        return "Admin Crazy Canteen";
    }

    public void addItem(FoodItem f0){
        System.out.println("Enter Item Name");
        String item = sc.nextLine();
        System.out.println();
    }


    public void processrefund(float amount, Customer c){

    }
}
