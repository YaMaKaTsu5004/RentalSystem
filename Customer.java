import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable{
    private String name;
    private int age;
    private String gender;
    private String contrateDate;
    private ArrayList<Furniture> customerRentals;
    private ArrayList<CustomerRentalCount> customerRentalsCount;
    
    Customer(String name, int age, String gender, String contrateDate){
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contrateDate = contrateDate;
        customerRentals = new ArrayList<>();
        customerRentalsCount = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getAge(){
        return age;
    }

    public String getGender(){
        return gender;
    }

    public String getContrateDate(){
        return contrateDate;
    }

    public String getAllField(){
        return name + "," + age + "," + gender + "," + contrateDate;
    }

    public ArrayList<Furniture> getCustomerFurnitures(){
        return this.customerRentals;
    }

    public void reduceCustomerFurniture(Furniture furniture){
        for(int i = 0; i < customerRentals.size(); i++){
            if(customerRentals.get(i).getName().equals(furniture.getName()) && furniture.getCustomer() == null){
                customerRentals.remove(i);
                break;
            }
        }
        for(int i = 0; i < customerRentalsCount.size(); i++){
            if(customerRentalsCount.get(i).getName().equals(furniture.getName())){
                customerRentalsCount.get(i).reduceCount();
                break;
            }
        }
    }

    public void addCustomerFurniture(Furniture furniture){
        this.customerRentals.add(furniture);
        if(customerRentalsCount.size() == 0){
            customerRentalsCount.add(new CustomerRentalCount(furniture.getName()));
        }else{
            for(int i = 0; i < customerRentalsCount.size(); i++){
                if(customerRentalsCount.get(i).getName().equals(furniture.getName())){
                    customerRentalsCount.get(i).addCount();
                    break;
                }

                if(i + 1 == customerRentalsCount.size()){
                    customerRentalsCount.add(new CustomerRentalCount(furniture.getName()));
                    break;
                }
            }
        }
    }

    public ArrayList<CustomerRentalCount> getCustomerRentalCount(){
        return customerRentalsCount;
    }
}
