import java.io.*;
import java.util.ArrayList;
import java.util.Queue;

public class fileHandler {
    public static String filePath="src/customerFiles/";

//    public static void writeAllCustomers(ArrayList<Customer> customers){
//        try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(filePath + "customers.dat"))) {
//            oo.writeObject(customers);
//            System.out.println("Customer data saved successfully.");
//        } catch (IOException e) {
//            System.out.println("Error writing customer to file: " + e.getMessage());
//        }
//    }

//    public static void writeCustomer(Customer customer) {
//        try (FileOutputStream fos = new FileOutputStream(filePath+"customers.csv", true)) {// true = append mode
//            String customerData = customer.getID() + "\n";
//            fos.write(customerData.getBytes());
//        } catch (IOException e) {
//            System.out.println("Error writing customer to file: " + e.getMessage());
//        }
//    }

//    public static ArrayList<Customer> readCustomersFromFile() {
//        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(filePath+"customers.dat"))) {
//            return (ArrayList<Customer>) is.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            System.out.println("Error reading customers : " + e.getMessage());
//        }
//        return new ArrayList<>();
//    }

    public static void saveOrderManager(String file, OrderManager manager){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath+file))) {
            oos.writeObject(manager);
            System.out.println("Order history written to file successfully.");
        } catch (IOException e) {
            System.out.println("Error writing order history to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static OrderManager loadOrderManager(String file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath + file))) {
            return (OrderManager) ois.readObject();
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public static Queue<Order> readOrdersFromFile(String file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath + file))) {
            Queue<Order> vip = (Queue<Order>) ois.readObject();
            Queue<Order> regular = (Queue<Order>) ois.readObject();
            vip.addAll(regular);
            return vip;

        } catch (IOException | ClassNotFoundException e) {
//            System.out.println("Error reading order history from file: " + e.getMessage());
        }
        return null;
    }

    //for current cart
    public static void newOrder(Order order) {
        try (FileOutputStream fos = new FileOutputStream(filePath+"currentCart.csv")) {//overwrite
            String orderData = "Customer id: "+order.getCustomer() + "\n";
            fos.write(orderData.getBytes());
        } catch (IOException e) {
            System.out.println("Error writing order to file: " + e.getMessage());
        }
    }

    public static void addItem(int orderid, String name, int quantity) {
        try (FileOutputStream fos = new FileOutputStream(filePath+"currentCart.csv", true)) { // true = append mode
            String orderData = orderid + "," + name+","+quantity + "\n";
            fos.write(orderData.getBytes());
        } catch (IOException e) {
            System.out.println("Error writing order to file: " + e.getMessage());
        }
    }
}
