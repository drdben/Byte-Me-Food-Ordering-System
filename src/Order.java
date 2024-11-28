import java.io.Serializable;
import java.util.*;
import java.time.*;

public class Order implements Serializable {

//    private static final long serialVersionUID = 1L;

//    transient Scanner sc = new Scanner(System.in);

    private int status; //0 means received, 1 preparing, 2 is outfordel, 3 complete, 4-denied
    private boolean VIP = false;
    private Customer customer;
    private LocalDate date;
    private HashMap<FoodItem, Integer> cart = new HashMap<>();
    private float total;
    private int id =0;
    //food item and specified qty
    static int count =0;
    private String specialreq = "";


    static Queue<Order> vipqueue = new LinkedList<>();
    static Queue<Order> regular = new LinkedList<>();
    // 2 prep queues 1 delivery queue and 1 history;

    static Queue<Order> preparing = new LinkedList<>();
    static Queue<Order> outfordelivery = new LinkedList<>();

    static Stack<Order> OrderHistory = new Stack<>();
    static Stack<Order> denied = new Stack<>();
    //only completed orders are added to the orderhistory stack

    public Order(HashMap<FoodItem, Integer> Cart, boolean vip, Customer c, String req){
        this.cart = Cart;
        this.VIP = vip;
        if(vip){
            vipqueue.add(this);
        }
        else{
            regular.add(this);
        }
        this.status = 0; //by default when order entered
        this.date = LocalDate.now();
        for(FoodItem i: Cart.keySet()){
            this.total += i.getPrice()*Cart.get(i);
        }
        this.customer = c;
        this.id=++count;
        this.specialreq= req;
    }

    public Order(HashMap<FoodItem, Integer> Cart, boolean vip, Customer c, OrderManager manager){
        this.cart = Cart;
        this.VIP = vip;
        if(vip){
            vipqueue.add(this);
//            manager.addToVipQueue(this);
        }
        else{
            regular.add(this);
//            manager.addToRegularQueue(this);
        }
        this.status = 0; //by default when order entered
        this.date = LocalDate.now();
        for(FoodItem i: Cart.keySet()){
            this.total += i.getPrice()*Cart.get(i);
        }
        this.customer = c;
        this.id=++count;
        this.specialreq= "";
    }

    public static void ViewOrders(){
        //pending orders
        //printed in order of priority
//        Queue<Order> vipqueue = manager.getVipQueue();
//        Queue<Order> regular = manager.getRegularQueue();

        System.out.println("-----------Orders Pending (in order of Priority)--------");
        System.out.println("Printing Next order to be handled..");
        Order next;
        if(!vipqueue.isEmpty()){
            System.out.println("VIP QUEUE: ");
            System.out.println("--------------------------------");
            int i=0;
            for(Order o: vipqueue){
                if(o.getStatus()==4){
                    System.out.println("The order here was denied. Removing it..");
                    Order p =vipqueue.poll();
                    denied.add(p);
                    System.out.println("Next order: ");
                }
                else{
                    System.out.println(++i +". "+o.customer+" \n"+"order ID: "+o.getId());
                    printCart(o.getCart());
                    System.out.println("Special requests: ");
                    System.out.println(o.getSpecialreq());
                    System.out.println("--------------------------------");
                }
            }
        }

            int i=0;
            if(regular.isEmpty()){
                System.out.println("No orders Pending!");
            }
            else {
                System.out.println("REGULAR QUEUE: ");
                System.out.println("--------------------------------");
                for(Order o: regular) {
                    if(o.getStatus()==4){
                        System.out.println("The order here was denied. Removing it..");
                        Order p = regular.poll();
                        denied.add(p);
                        System.out.println("Printing next order... ");
                    }
                    else{
                        System.out.println(++i +". "+o.customer+" \n"+"order ID: "+o.getId());
                        printCart(o.getCart());
                        System.out.println("Special requests: ");
                        System.out.println(o.getSpecialreq());
                        System.out.println("--------------------------------");
                    }
                }
            }
    }

    private static void printCart(HashMap<FoodItem, Integer> cart){
        for(FoodItem f : cart.keySet()){
            System.out.println("- "+f.getItem()+"    " +cart.get(f));
        }
    }

    public static void processOrder(){
        System.out.println("The order next in line to be completed is: ");
        if(!vipqueue.isEmpty()){
            Order o = vipqueue.peek();
            System.out.println("Order ID: "+o.getId());
            printCart(o.getCart());
            System.out.println("Moving to Preparing Queue.......(Cooking)......");
            Order p = vipqueue.poll();
            p.setStatus(1);
            preparing.add(p);
            System.out.println("Order no. "+p.getId()+"moved to Processing Queue!");
        }
        else if(!regular.isEmpty()){
                Order o = regular.peek();
                System.out.println("Order ID: "+o.getId());
                printCart(o.getCart());
                System.out.println("Moving to Preparing Queue.......(Cooking)......");
                Order p = regular.poll();
                p.setStatus(1);
                preparing.add(p);
                System.out.println("Order no. "+p.getId()+"moved to Processing Queue!");
        }
        else{
            System.out.println("No Orders Here! ><");
        }
        System.out.println("------------------------------");
    }

    public static void deliver(){
        System.out.println("The order next in line to be sent for delivery is: ");
        if(!preparing.isEmpty()){
            Order o = preparing.peek();
            System.out.println("Order ID: "+o.getId());
            printCart(o.getCart());
            System.out.println("Transferring cooked order to Delivery Personnel.......(Cooked)......");
            Order p = preparing.poll();
            p.setStatus(2);
            outfordelivery.add(p);
            System.out.println("Order no. "+p.getId()+"Out for Delivery!");
        }
        else{
            System.out.println("No Orders Here! ><");
        }
        System.out.println("------------------------------");
    }

    public static void complete(){
        System.out.println("The order next in line to be completed is: ");
        if(!outfordelivery.isEmpty()){
            Order o = outfordelivery.peek();
            System.out.println("Order ID: "+o.getId());
            printCart(o.getCart());
            System.out.println("Finishing Delivery...");
            Order p = outfordelivery.poll();
            p.setStatus(3);
            OrderHistory.add(p);
            o.getCustomer().getHistory().add(p);
            System.out.println("Order no. "+p.getId()+"Has been completed, received by customer and moved to Order History Queue!");
        }
        else{
            System.out.println("No Orders Here! ><");
        }
        System.out.println("------------------------------");
    }

    public static void generatedailyreport(){
        float total =0f;
        System.out.println("-----------------DAILY REPORT---------------");
        System.out.println("  Order ID                     Total");
        for(Order o: OrderHistory){
            LocalDate current = LocalDate.now();
            if(o.date.equals(current)){
                total+=o.getTotal();
                System.out.println("     "+o.getId()+"                "+o.getTotal());
            }
        }
        System.out.println("-----------------------------------------------");
        System.out.println("        Today's Total SALES:  "+total);
    }

    public HashMap<FoodItem, Integer> getCart() {
        return cart;
    }

    public float getTotal() {
        return total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getSpecialreq() {
        return specialreq;
    }

    public Customer getCustomer() {
        return customer;
    }
}
