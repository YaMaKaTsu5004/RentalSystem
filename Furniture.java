import java.io.Serializable;
import java.util.ArrayList;

public class Furniture implements Serializable{
    private String name;
    private Customer customer;
    
    public static ArrayList<FurnitureCount> furnitureCounts = new ArrayList<>();

    Furniture(String name){
        this.name = name;
    }

    Furniture(String name, int quantity, boolean add){
        this.name = name;
        if(add == false){
            furnitureCounts.add(new FurnitureCount(name, quantity));
        }else{
            for(int i = 0; i < furnitureCounts.size(); i++){
                if(name.equals(furnitureCounts.get(i).getName())){
                    furnitureCounts.get(i).setCount(quantity);
                    break;
                }
            }
        }
    }

    public String getName(){
        return this.name;
    }

    public void setCustomer(Customer customer){
        this.customer = customer;
        for(int i = 0; i < furnitureCounts.size(); i++){
            if(this.name.equals(furnitureCounts.get(i).getName())){
                furnitureCounts.get(i).reduceRendableCount();
                break;
            }
        }
    }

    public void freeCustomer(){
        this.customer = null;
        for(int i = 0; i < furnitureCounts.size(); i++){
            if(this.name.equals(furnitureCounts.get(i).getName())){
                furnitureCounts.get(i).addRendableCount();
                break;
            }
        }
    }

    public Customer getCustomer(){
        return this.customer;
    }

}
