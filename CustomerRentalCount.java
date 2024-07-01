import java.io.Serializable;

public class CustomerRentalCount implements Serializable{
    private String name;
    private int count;

    CustomerRentalCount(String name){
        this.name = name;
        this.count = 1;
    }

    public void reduceCount(){
        this.count--;
    }

    public void addCount(){
        this.count++;
    }

    public String getName(){
        return this.name;
    }

    public int getCount(){
        return this.count;
    }
}
