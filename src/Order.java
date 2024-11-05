import java.util.*;

public class Order {

    private int status; //0 means received, 1 preparing, 2 is outfordel, 3 complete, 4-denied
    private boolean VIP = false;
    private Customer customer;
    private Date date;
    private HashMap<FoodItem, Integer> cart = new HashMap<>();
    private float total;
    private int id =0;
    //food item and specified qty
    private static int count =0;
    private String specialreq = "";


    static Queue<Order> vipqueue = new LinkedList<>();
    static Queue<Order> regular = new LinkedList<>();
    // 2 prep queues 1 delivery queue and 1 history;

    static Queue<Order> preparing = new LinkedList<>();
    static Queue<Order> outfordelivery = new LinkedList<>();
    static Stack<Order> OrderHistory = new Stack<>();
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
        this.date = new Date();
        for(FoodItem i: Cart.keySet()){
            this.total += i.getPrice()*Cart.get(i);
        }
        this.customer = c;
        this.id=++count;
        this.specialreq= req;
    }

    public void ViewOrders(){
        //pending orders
        //printed in order of priority

        System.out.println("-----------Orders Pending (in order of Priority)--------");

    }

    public void generatedailyreport(){

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
}
