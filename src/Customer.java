import java.awt.*;
import java.util.*;

public class Customer extends Account{
    Scanner sc = new Scanner(System.in);
    private ArrayList<Order> history = new ArrayList<>();
    private ArrayList<Order> pending = new ArrayList<>();

    private String Address;
    private String ID;
    //each customer must enter unique ID on first time ordering
    //can enter name, then no two customers can enter the same name;
    private boolean VIP;
    private HashMap<FoodItem, Integer> Cart = new HashMap<>();
    private float cartTotal = 0f;
    public static ArrayList<Customer> customers = new ArrayList<>();
    private Account<Admin> account = new Account();

    @Override
    public String toString() {
        return ID;
    }

    public Customer(String id, String address, boolean vip){
        this.ID = id;
        this.Address = address;
        customers.add(this);
        this.VIP = vip;
    }

    private void viewpastorders() {
        System.out.println("----------------My Orders----------------");
        System.out.println("    Item           Qty           Total");
        int i=0;
        for(Order o : history){
            i++;
            System.out.println("Order "+i+": ");
            for(FoodItem f: o.getCart().keySet()){
                System.out.println("  "+f+" : "+o.getCart().get(f)+ "  "+f.getPrice()*o.getCart().get(f));
            }
            System.out.println("--------------Total: "+o.getTotal()+" -------------");
            System.out.println("--------------------------------------------------");
        };
    }

    public void makepayment(float amount, Admin admin){
        if(account.getBalance()>amount){
            System.out.println("Your canteen wallet has sufficient funds, deducting "+amount+" $ from account...");
        }
        else{
            System.out.println("Insufficient funds in canteen wallet, auto redirecting to Bank...\nEnter Bank authentication pin (no spaces) to complete payment of "+amount+"$");
            String pin = sc.next();
        }
        System.out.println("Congratulations! Payment Verified!"); //we accept all sort of pins :) lol
        System.out.println("Remaining Balance in Canteen account is: "+account.getBalance()+" $\nThank you for your purchase!!!");
        account.getFrom().add(admin);
        account.getCredit().add(-amount);
        admin.adminAcc.changebalance(amount);
        admin.adminAcc.getFrom().add(this);
        admin.adminAcc.getCredit().add(amount);
        System.out.println("Press any key to return to previous page");
        String s = sc.next();
    }

    public boolean isVIP() {
        return VIP;
    }

    public boolean displayCart(){
        if(Cart.size()>0){
            System.out.println("    Item           Qty           Total");
            for(FoodItem f: Cart.keySet()){
                System.out.println("  "+f+" : "+Cart.get(f)+ "  "+f.getPrice()*Cart.get(f));
                cartTotal+=f.getPrice()*Cart.get(f);
            }
            System.out.println("--------------Total: "+cartTotal+" -------------");
            return true;
        }
        else{
            System.out.println("Cart is Currently Empty!");
            return false;
        }
    }

    public void removefromcart(){
        System.out.println("Enter name of Item to completely remove from cart: ");
        String rm = sc.nextLine();
        for(FoodItem f: Cart.keySet()){
            String it = f.getItem().toLowerCase();
            if(rm.equals(it)){
                Cart.remove(f);
                break;
            }
        }
    }

    public void changeqty(){
        System.out.println("Enter name of Item to change its qty: ");
        String change = sc.nextLine();
        System.out.println("Enter New Quantity: ");
        int qty = sc.nextInt();
        for(FoodItem f: Cart.keySet()){
            String it = f.getItem().toLowerCase();
            if(change.equals(it)){
                Cart.put(f, qty);
                break;
            }
        }
        System.out.println("Congratulations! "+change+" Quantity is now "+qty);
        System.out.println("Modified Cart:");
        displayCart();
    }

    public void checkout(Admin admin){
        System.out.println("Default delivery address is "+Address+"\nTo change delivery details press 1: ");
        String s =sc.next();
        if(s.equals("1")){
            System.out.println("Enter new address: ");
            String ss = sc.nextLine();
            Address= ss;
            System.out.println("Delivery address updated to"+Address);
        }
        System.out.println("Redirecting you to Payment Page...");
        makepayment(cartTotal, admin);
        //place order:
        Order order = new Order(Cart, isVIP(), this);
        pending.add(order);
        Cart.clear();
        System.out.println("Congratulations! Order has been placed with Order ID"+order.getId());
    }

    public void displayPending(){
        System.out.println("Pending Orders:");
        for(Order o: pending){
            System.out.println("Order ID: "+o.getId());
            String status="";
            int x = o.getStatus();
            switch(x) {
                case 0:
                    status="placed";
                    break;
                case 1:
                    status="preparing";
                    break;
                case 2:
                    status="out for delivery";
                    break;
                default:
                    break;
            }
            System.out.println("Currently, your order no."+o.getId()+"is "+status);
            for(FoodItem f: o.getCart().keySet()){
                System.out.println(o.getCart().get(f)+" "+f);
            };
            System.out.println("-----------------------------");
        }
    }

    public void displayComplete(Admin admin){
        System.out.println("Previous Orders:");
        for(Order o: history){
            System.out.println("Order ID: "+o.getId());
            String status="";
            int x = o.getStatus();
            switch(x) {
                case 3:
                    status="delivered";
                    break;
                case 4:
                    status="denied";
                    break;
                default:
                    break;
            }
            System.out.println("Your order no."+o.getId()+"was "+status);
            for(FoodItem f: o.getCart().keySet()){
                System.out.println(o.getCart().get(f)+" "+f);
                System.out.println("You paid $"+o.getTotal());
            };
            System.out.println("Would you like to Re-place any of the above orders?(1=yes)");
            int yes = sc.nextInt();
            if(yes==1){
                System.out.println("Please enter order ID to replace");
                int id = sc.nextInt();
                HashMap<FoodItem,Integer> cart2 = new HashMap<>();
                for(Order order: history){
                    if(order.getId()==id){
                        //deep copy of o1
                        for(FoodItem f: order.getCart().keySet()){
                            int qt = order.getCart().get(f);
                            Integer qt2 = qt;
                            cart2.put(f ,qt2);
                        }
                    }
                }
                Order o2 = new Order(cart2, isVIP(), this);
                pending.add(o2);
                checkout(admin);
                System.out.println("New order placed successfully with order ID: "+o2.getId());
            }
            System.out.println("-----------------------------");
        }
    }

    public void AddToCart(FoodItem food){
        System.out.println("How many different items do you want to add?");
        int diff = sc.nextInt();
        System.out.println("For each item, write the item name press enter and write quantity press enter");
        for(int i=0;i<diff;i++){
            System.out.println(i+":" );
            String name = sc.nextLine();
            int qty = sc.nextInt();
            boolean found=false;
            for(FoodItem f: food.Menu){
                if(f.getItem().equals(name)){
                    Cart.put(f, qty);
                    found = true;
                    System.out.println(qty+" Item(s) "+f+"Added!");
                    break;
                }
            }
            if(!found){
                System.out.println("Sorry, couldnt add that something went wrong, non existent item maybe lol");
            }
        }
    }

    public String getID() {
        return ID;
    }

    public ArrayList<Order> getPending() {
        return pending;
    }

    public ArrayList<Order> getHistory() {
        return history;
    }

    public String getAddress() {
        return Address;
    }

}
