import java.util.*;

public class Main {
    public static FoodItem i0;

    static {
        try {
            i0 = new FoodItem("Paneer Momos",40,3);
        } catch (WrongFoodTypeException e) {
            throw new RuntimeException(e);
        }
    }

    public Main() {
    }

    public static void main(String[] args) throws WrongFoodTypeException, WrongpwException {
        InitMenu();
        InitCustomer();
        Admin admin = new Admin();
        LoginPage(admin);
    }

    public static void LoginPage(Admin admin) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        while(!exit){
            System.out.println("1. Admin Login\n2. Customer Login\n3. Exit\nEnter choice no: ");
            int ch = sc.nextInt();
            switch(ch){
                case 1:
                    try{
                        System.out.println("Enter Admin Password: ");
                        String pass = sc.next();
                        if(pass.equals(admin.getPass())) {
                            System.out.println("--------Login successful!----");
                            AdminMain();
                        }
                        else{
                            throw new WrongpwException("Wrong Password entered! This is your last try!");
                        }
                    }
                    catch(WrongpwException w){
                        System.out.println(w.getMessage());
                        System.out.println("Enter Admin Password: ");
                        String pass = sc.next();
                        if(pass.equals(admin.getPass())) {
                            System.out.println("--------Login successful!----");
                            AdminMain();
                        }
                        else{break;}
                    }
                    break;
                case 2:
                    ArrayList<Customer> custs = Customer.customers;
                    System.out.println("Enter Customer ID (your name if youre new, no spaces): ");
                    String id = sc.next();
                    boolean found =false;
                    for(Customer c: custs){
                        if((c.getID()).equals(id)){
                            CustomerMain(c, admin);
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        System.out.println("Enter Address/Room no (no spaces): ");
                        String add = sc.next();
                        System.out.println("Would you like to be a VIP customer? (Monthly payment of $20) (1 if yes, 0 if no) ");
                        int in = sc.nextInt();
                        boolean vip=false;
                        if(in==1){
                            vip = true;
                            Customer c1 = new Customer(id, add,vip);
                            System.out.println("Redirecting you to Payment Page...");
                            c1.makepayment(20f, admin);
                            System.out.println("Congratulations! You are now a VIP customer and will get order priority.");
                            CustomerMain(c1, admin);
                        }
                        else{
                            Customer c1 = new Customer(id, add, vip);
                            CustomerMain(c1, admin);
                        }
                    }
                    break;
                case 3:
                    System.out.println("Sad to see you leaaveeeee :/");
                    exit = true;
                    break;
                default:
                    break;
            }
        }
//        sc.close();
    }

    public static void AdminMain(){
        System.out.println("----------------Welcome To Crazy Canteen!!----------------");

    }

    public static void CustomerMain(Customer c, Admin admin) {
        Scanner sc = new Scanner(System.in);
        System.out.println("----------------Welcome To Crazy Canteen!!----------------");
        boolean logout = false;
        while(!logout){
            System.out.println("What would you like to do today:\n1. Browse Menu\n2. Cart & Checkout\n3. Track & Cancel Orders\n4. View Order History\n5. Leave a Review\n6. Log out");
            int mc=sc.nextInt();
            switch(mc){
                case 1:
                    boolean rtom = false;
                    while(!rtom){
                        System.out.println("Do you want to..\n1. View Full Menu\n2. Search by Keyword\n3. View Breakfast Options\n4. View Chinese Options\n5. View Momos\n6. Sort by Price\n7.Add to cart\n8.Return to Main menu (enter int 1-8)");
                        int mch = sc.nextInt();
                        switch (mch){
                            case 1:
                                i0.ViewMenu();
                                break;
                            case 2:
                                System.out.println("-------This is the Search Bar-------");
                                System.out.println("Enter Keyword(s)");
                                String s = sc.nextLine();
                                String ss = s.toLowerCase();
                                i0.search(ss);
                                break;
                            case 3:
                                i0.viewBreakfast();
                                break;
                            case 4:
                                i0.viewChinese();
                                break;
                            case 5:
                                i0.viewMomos();
                                break;
                            case 6:
                                boolean back = false;
                                while(!back){
                                    System.out.println("Would you like to:\n1. Sort in Ascending order\n2. Sort in Descending order\n3. Go Back");
                                    int ord = sc.nextInt();
                                    switch (ord){
                                        case 1:
                                            i0.sortbyprice(true);
                                            break;
                                        case 2:
                                            i0.sortbyprice(false);
                                            break;
                                        case 3:
                                            back = true;
                                            break;
                                    }
                                }
                                break;
                            case 7:
                                c.AddToCart(i0);
                            case 8:
                                rtom = true;
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                case 2:
                    System.out.println("Fetching Cart...");
                    boolean b = c.displayCart();
                    if(b){
                        boolean exitcart= false;
                        while(!exitcart){
                            System.out.println("1. Remove Item\n2. Change Quantity of an item\n3. Proceed to Checkout\n4. Exit Cart");
                            int ch = sc.nextInt();
                            switch (ch){
                                case 1:
                                    c.removefromcart();
                                    break;
                                case 2:
                                    c.changeqty();
                                    break;
                                case 3:
                                    System.out.println("Are you sure? You will have to complete order once you confirm.\nTo confirm press 1.");
                                    String s = sc.next();
                                    if(s.equals("1")){
                                        System.out.println("Proceeding to Checkout...");
                                        c.checkout(admin);
                                    }
                                    break;
                                case 4:
                                    exitcart = true;
                                    break;
                            }
                        }
                    }
                    break;
                case 3:
                    System.out.println("Enabling Order Tracking...");
                    c.displayPending();
                    break;
                case 4:
                    System.out.println("Loading Order History for complete orders: ");
                    c.displayComplete(admin); //can reorder in this
                    break;
                case 5:
                    //Leave Review
                    //Reviews are anonymous!
                    break;
                default:
                    break;
            }
        }
//        sc.close();
    }

    public static void InitMenu() throws WrongFoodTypeException {
        FoodItem i1 = new FoodItem("Aloo Paratha", 35, 1);
        FoodItem i2 = new FoodItem("Paneer Paratha", 40, 1);
        FoodItem i3 = new FoodItem("Noodles", 25, 2);
        FoodItem i4 = new FoodItem("Omlette", 35, 1);
        FoodItem i5 = new FoodItem("Chicken Steamed Momos", 40, 3);

        i1.ViewMenu();
        System.out.println("enter food :");
        additem();
        i1.ViewMenu();
    }

    public static void additem(){
        Scanner sc = new Scanner(System.in);
        String i = sc.nextLine();
        int p = sc.nextInt();
        int t = sc.nextInt();
        try{
            FoodItem f4 = new FoodItem(i,p,t);
            System.out.println(i+" Added!\n");
        }
        catch(WrongFoodTypeException e){
            System.out.println(e.getMessage());
            additem();
        }
//        sc.close();
    }

    public static void InitCustomer() {

    }
}
