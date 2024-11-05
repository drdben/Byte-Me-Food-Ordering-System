import javax.annotation.processing.SupportedSourceVersion;
import java.util.*;

public class FoodItem {
    Scanner sc = new Scanner(System.in);
    private String item;
    private Float price;
    private boolean available=true;
    private ArrayList<Review> reviews = new ArrayList<>();

    public static ArrayList<FoodItem> Menu = new ArrayList<>();
    private static ArrayList<FoodItem> Breakfast = new ArrayList<>();
    private static ArrayList<FoodItem> Chinese = new ArrayList<>();
    private static ArrayList<FoodItem> Momos= new ArrayList<>();

    public FoodItem(String item, float price, int type) throws WrongFoodTypeException {
        this.price = price;
        this.item = item;
        Menu.add(this);
        if(type<1 || type>3){
            throw new WrongFoodTypeException("Wrong type entered, try again!\n");
        }
        switch(type){
            case 1:
                Breakfast.add(this);
                break;
            case 2:
                Chinese.add(this);
                break;
            case 3:
                Momos.add(this);
                break;
            default:
                break;
        }
    }

    @Override
    public String toString() {
        return (item+" : "+price);
    }

    public void ViewMenu(){
        System.out.println("----------------Menu----------------");
        for (FoodItem food : Menu) {
            String av ="Available";
            if(!food.available) {
                av = "Not available atm.";
            }
            System.out.println("   " + food.item + " : " + food.price+" ("+av+")");
        }
        ;
        System.out.println("------------------------------------");
    }

    public void search(String s){
        String p = s;
        String[] arr = s.split(" ");
        for(FoodItem f: Menu){
            String av ="Available";
            if(!f.available) {
                av = "Not available atm.";
            }
            String it = item.toLowerCase();
            boolean yes = false;
            if(it.equals(p)){
                yes = true;
            }
            else{
                for(String q: arr){
                    if(it.contains(q)){
                        yes = true;
                        break;
                    }
                }
            }
            if(yes){
                System.out.println("   " + f.item + " : " + f.price+" ("+av+")");
            }
        }
        System.out.println("------------------------------------");
    }

    public void viewBreakfast(){
        System.out.println("----------------Breakfast Options----------------");
        for (FoodItem food : Breakfast) {
            String av ="Available";
            if(!food.available) {
                av = "Not available atm.";
            }
            System.out.println("   " + food.item + " : " + food.price+" ("+av+")");
        }
        ;
        System.out.println("------------------------------------");
    }

    public void viewChinese(){
        System.out.println("----------------Breakfast Options----------------");
        for (FoodItem food : Chinese) {
            String av ="Available";
            if(!food.available) {
                av = "Not available atm.";
            }
            System.out.println("   " + food.item + " : " + food.price+" ("+av+")");
        }
        ;
        System.out.println("------------------------------------");
    }

    public void viewMomos(){
        System.out.println("----------------Breakfast Options----------------");
        for (FoodItem food : Momos) {
            String av ="Available";
            if(!food.available) {
                av = "Not available atm.";
            }
            System.out.println("   " + food.item + " : " + food.price+" ("+av+")");
        }
        ;
        System.out.println("------------------------------------");
    }

    public void sortbyprice(boolean ascending){
        Menu.sort(Comparator.comparing(FoodItem::getPrice));
        if(ascending){
            ViewMenu();
        }
        else{
            Collections.reverse(Menu);
            ViewMenu();
        }
    }

    public void removeitem(){
        System.out.println("Enter name of item to be removed.");
        String rem = sc.nextLine();
        boolean rm =false;
            for(FoodItem e: Menu){
                if((e.item).equals(rem)) {
                    Menu.remove(e);
                    rm = true;
                    break;
                }
            };
            if(rm){
                System.out.println("Item does not exist!");
            }
    }

    public void addReview(){
        System.out.println("Please rate "+item+" out of 5: ");
        int score=0;
        try{
            score = sc.nextInt();
            if(score>5 || score<0){
                throw new InputOutofBoundsException("Please enter rating ranging from 0 to 5 only!");
            }
        }
        catch(InputOutofBoundsException e){
            System.out.println(e.getMessage());
            addReview();
        }
        System.out.println("Please leave a comment.");
        String com = sc.nextLine();
        reviews.add(new Review(score, com));
    }

    public void displayReviews(){
        if(!reviews.isEmpty()){
            System.out.println("-------------Item Reviews------------");
            for(Review r: reviews){
                System.out.println(r.getScore()+"/5");
                System.out.println(r.getComment()+"\n");
            }
        }
        else{
            System.out.println("No reviews yet for this item!");
        }
        System.out.println("-------------------------------------");
    }
    public Float getPrice() {
        return price;
    }

    public String getItem() {
        return item;
    }

    public boolean isAvailable() {
        return available;
    }
}
