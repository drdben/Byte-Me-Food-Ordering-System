public class Admin extends Account {
    private String pass = "webroke";
    public Account<Customer> adminAcc = new Account();

    public String getPass() {
        return pass;
    }

    @Override
    public String toString() {
        return "Admin Crazy Canteen";
    }

    public void processrefund(float amount, Customer c){

    }
}
