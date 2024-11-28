import javax.swing.*;
import java.util.*;

public class Main{
    public static FoodItem i0;

    static {
        try {
            i0 = new FoodItem("Paneer Momos",40,3);
        } catch (WrongFoodTypeException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws WrongFoodTypeException, WrongpwException {
        InitMenu();
        Admin admin = new Admin();
        OrderManager manager = null;
        try {
            manager = fileHandler.loadOrderManager("orders.dat");
        } catch(Exception e){
            System.out.println("Error loading orders.dat or file does not exist. Initializing new data.");
        }
        if(manager!=null){
            Order.vipqueue.addAll(manager.getVipQueue());
            Order.regular.addAll(manager.getRegularQueue());
            Order.OrderHistory.addAll(manager.getOrderHistory());
            Order.preparing.addAll(manager.getPreparing());
            Order.outfordelivery.addAll(manager.getOutfordelivery());
            Order.denied.addAll(manager.getDenied());
            Customer.customers.addAll(manager.getCusts());
            Order.count = manager.getCnt();
        }
        else{
            manager = new OrderManager();
            try{
                fileHandler.saveOrderManager("orders.dat", manager);
                System.out.println("Created new orders.dat file");
            }
            catch(Exception e){
                System.out.println("couldnt create new orders.dat: "+e.getMessage());
            }
            InitCustomer(admin, manager);
        }
        LoginPage(admin, manager);
    }

    public static void LoginPage(Admin admin, OrderManager manager) {
        Scanner sc = new Scanner(System.in);
//        Customer.customers = fileHandler.readCustomersFromFile();
        boolean exit = false;
        while(!exit){
            System.out.println("1. Admin Login\n2. Customer Login\n3. Exit\n4. GUI\nEnter choice no: ");
            int ch = sc.nextInt();
            sc.nextLine();

            switch(ch){
                case 1:
                    try{
                        System.out.println("Enter Admin Password: ");
                        String pass = sc.next();
                        sc.nextLine();
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
                        sc.nextLine();
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
                    sc.nextLine();
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
                        sc.nextLine();

                        System.out.println("Would you like to be a VIP customer? (Monthly payment of $20) (1 if yes, 0 if no) ");
                        int in = sc.nextInt();
                        sc.nextLine();

                        boolean vip=false;
                        if(in==1){
                            vip = true;
                            Customer c1 = new Customer(id, add,vip, manager);
                            System.out.println("Redirecting you to Payment Page...");
                            c1.makepayment(20f, admin);
                            System.out.println("Congratulations! You are now a VIP customer and will get order priority.");
                            CustomerMain(c1, admin);
                        }
                        else{
                            Customer c1 = new Customer(id, add, vip, manager);
                            CustomerMain(c1, admin);
                        }
                    }
//                    fileHandler.writeAllCustomers(custs);
                    break;
                case 3:
                    manager.setRegularQueue(Order.regular);
                    manager.setVipQueue(Order.vipqueue);
                    manager.setCusts(Customer.customers);
                    manager.setDenied(Order.denied);
                    manager.setOutfordelivery(Order.outfordelivery);
                    manager.setOrderHistory(Order.OrderHistory);
                    manager.setPreparing(Order.preparing);
                    manager.setCnt(Order.count);

                    fileHandler.saveOrderManager("orders.dat",manager);
                    System.out.println("Sad to see you leaaveeeee :/");
                    exit = true;
                    System.exit(0);
                    break;
                case 4:
//                    fileHandler.writeAllOrders("orders.dat",Order.vipqueue,Order.regular);
                    SwingUtilities.invokeLater(() -> {
                        MenuGUI gui = new MenuGUI();

                        gui.setVisible(true);
                    });
                    break;
                default:
                    break;
            }
        }
//        sc.close();
    }

    public static void AdminMain(){
        ArrayList<FoodItem> arr = FoodItem.Menu;
        Queue<Order> vip = Order.vipqueue;
        Queue<Order> reg = Order.regular;

        System.out.println("----------------Welcome To Crazy Canteen!!----------------");
        System.out.println("Currently, the Menu is: ");
        i0.ViewMenu();
        Scanner sc = new Scanner(System.in);
        boolean logout = false;
        while(!logout) {
            System.out.println("What would you like to do today:\n1. Add Items in Menu\n2. Update Existing Items\n3. Remove Items\n4. View Pending Orders\n5. Handle Orders(Manage status etc) \n6. Generate Daily Report\n7. Log Out");
            int mc=sc.nextInt();
            sc.nextLine();
            switch(mc) {
                case 1:
                    System.out.println("enter food :");
                    additem();
                    System.out.println("New Menu is Below: ");
                    i0.ViewMenu();
                    break;
                case 2:
                    System.out.println("Enter Item serial number to update: ");
                    int sn = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter Updated price press enter(same price if unchanged) ");
                    float qt = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter Updated Availability (1 if available, 0 if not) ");
                    arr.get(sn).setPrice(qt);
                    if(qt==1){
                        arr.get(sn).setAvailable(true);
                    }
                    else if(qt==0){
                        arr.get(sn).setAvailable(false);
                    }
                    else{
                        //do nothing
                    }
                    break;
                case 3:
                    System.out.println("Enter Item serial number to remove: ");
                    int rm = sc.nextInt();
                    FoodItem f = FoodItem.removeitem(rm-1);
                    //update all *pending* orders with declined status(already processing orders will not be declined)
                    for(Order o: vip){
                        if(o.getCart().containsKey(f)){
                            o.setStatus(4);
                        }
                    }
                    for(Order o: reg){
                        if(o.getCart().containsKey(f)){
                            o.setStatus(4);
                        }
                    }
                    break;
                case 4:
                    Order.ViewOrders();
                    break;
                case 5:
                    //handling orders
                    System.out.println("Would you like to: ");
                    System.out.println("1. Begin Processing an Order\n2. Send processed Order to Delivery \n3. Complete Out For Delivery Orders\n4. Back");
                    int choice = sc.nextInt();
                    switch(choice){
                        case 1:
                            Order.processOrder();
                            break;
                        case 2:
                            Order.deliver();
                            break;
                        case 3:
                            Order.complete();
                            break;
                        default:
                            System.out.println("Invalid input");
                            break;
                    }
                    break;
                case 6:
                    Order.generatedailyreport();
                    break;
                case 7:
//                    MenuGUI.
                    logout=true;
                    break;
                default:
                    System.out.println("Invalid Input");
                    break;
            }
        }
    }

    public static void CustomerMain(Customer c, Admin admin) {
        Scanner sc = new Scanner(System.in);
        System.out.println("----------------Welcome To Crazy Canteen!!----------------");
        boolean logout = false;
        while(!logout){
            System.out.println("What would you like to do today:\n1. Browse Menu\n2. Cart & Checkout\n3. Track & Cancel Orders\n4. View Order History\n5. Leave a Review\n6. Log out");
            int mc=sc.nextInt();
            sc.nextLine();

            switch(mc){
                case 1:
                    boolean rtom = false;
                    while(!rtom){
                        System.out.println("Do you want to..\n1. View Full Menu\n2. Search by Keyword\n3. View Breakfast Options\n4. View Chinese Options\n5. View Momos\n6. Sort by Price\n7.Add to cart\n8. Return to Main menu (enter int 1-8)");
                        int mch = sc.nextInt();
                        sc.nextLine();
                        switch (mch){
                            case 1:
                                i0.ViewMenu();
                                break;
                            case 2:
                                System.out.println("-------This is the Search Bar-------");
                                System.out.println("Enter Keyword(s): ");
                                String s = sc.nextLine();
//                                sc.nextLine();
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
                                    sc.nextLine();

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
                            sc.nextLine();

                            switch (ch){
                                case 1:
                                    c.removefromcart();
                                    break;
                                case 2:
                                    c.changeqty();
                                    break;
                                case 3:
                                    System.out.println("Do you have any special requests? ");
                                    String req = sc.nextLine();
                                    System.out.println("Request recieved!");
                                    System.out.println("Are you sure? You will have to complete order once you confirm.\nTo confirm press 1.");
                                    String s = sc.next();
                                    sc.nextLine();

                                    if(s.equals("1")){
                                        System.out.println("Proceeding to Checkout...");
                                        c.checkout(admin, req);
                                    }
//                                    fileHandler.writeAllOrders("orders.dat",Order.vipqueue,Order.regular);
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
//                    fileHandler.writeAllOrders("orders.dat",Order.vipqueue,Order.regular);

                    break;
                case 4:
                    System.out.println("Loading Order History for complete orders: ");
                    c.displayComplete(admin); //can reorder in this
                    break;
                case 5:
                    //Leave Review
                    //Reviews are anonymous!
                    break;
                case 6:
//                    fileHandler.writeAllOrders("orders.dat",Order.vipqueue,Order.regular);
//                    OrdersGUI.refreshOrders();
//                    fileHandler.writeAllCustomers(Customer.customers);
                    logout=true;
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
    }

    public static void additem(){
        Scanner sc = new Scanner(System.in);
        String i = sc.nextLine();
        int p = sc.nextInt();
        sc.nextLine();
        int t = sc.nextInt();
        sc.nextLine();
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

    public static void InitCustomer(Admin admin, OrderManager manager) {
        //john is a regular customer who has placed an order and has some things in his cart
        Customer John = new Customer("john", "505 boys hostel",false, manager);
        John.AddToCartInternal(i0,5);
        float amount = i0.getPrice()*5;
        Order j1 = new Order(John.getCart(),false, John, manager);
        John.getAccount().getFrom().add(admin);
        John.getAccount().getCredit().add(-amount);
        admin.adminAcc.changebalance(amount);
        admin.adminAcc.getFrom().add(John);
        admin.adminAcc.getCredit().add(amount);

        John.getCart().clear();
        John.AddToCartInternal(i0.Menu.get(1),3);

        //emma is a vip customer who has placed an order currently empty cart
        Customer emma = new Customer("emma", "505 gh",true, manager);
        emma.AddToCartInternal(i0.Menu.get(3),2);
        amount = FoodItem.Menu.get(3).getPrice()*2;
        Order e1 = new Order(emma.getCart(),true, emma, manager);
        emma.getAccount().getFrom().add(admin);
        emma.getAccount().getCredit().add(-amount);
        admin.adminAcc.changebalance(amount);
        admin.adminAcc.getFrom().add(emma);
        admin.adminAcc.getCredit().add(amount);

        //harry regular, only items in cart no prev orders
        Customer harry = new Customer("harry", "ur place", false, manager);
        harry.AddToCartInternal(i0.Menu.get(2),1);

    }
}
