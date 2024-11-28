import java.util.*;
import java.io.Serializable;

public class OrderManager implements Serializable {
    private Queue<Order> vipQueue = new LinkedList<>();
    private Queue<Order> regularQueue = new LinkedList<>();
    private ArrayList<Customer> custs = new ArrayList<>();
    private int cnt=0;
    private Queue<Order> preparing = new LinkedList<>();
    private Queue<Order> outfordelivery = new LinkedList<>();

    private Stack<Order> OrderHistory = new Stack<>();
    private Stack<Order> denied = new Stack<>();

    public Stack<Order> getOrderHistory() {
        return OrderHistory;
    }

    public Queue<Order> getPreparing() {
        return preparing;
    }

    public Stack<Order> getDenied() {
        return denied;
    }

    public Queue<Order> getOutfordelivery() {
        return outfordelivery;
    }

    public void setOutfordelivery(Queue<Order> outfordelivery) {
        this.outfordelivery = outfordelivery;
    }

    public void setOrderHistory(Stack<Order> orderHistory) {
        OrderHistory = orderHistory;
    }

    public void setPreparing(Queue<Order> preparing) {
        this.preparing = preparing;
    }

    public void setDenied(Stack<Order> denied) {
        this.denied = denied;
    }

    public void  addtocusts(Customer c){
        custs.add(c);
    }

    public ArrayList<Customer> getCusts() {
        return custs;
    }

    public Queue<Order> getVipQueue() {
        return vipQueue;
    }

    public Queue<Order> getRegularQueue() {
        return regularQueue;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCusts(ArrayList<Customer> custs) {
        this.custs = custs;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public void setRegularQueue(Queue<Order> regularQueue) {
        this.regularQueue = regularQueue;
    }

    public void setVipQueue(Queue<Order> vipQueue) {
        this.vipQueue = vipQueue;
    }
}
