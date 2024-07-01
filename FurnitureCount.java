import java.io.Serializable;

public class FurnitureCount implements Serializable{
    private String name;
    private int count;
    private int rendableCount;

    FurnitureCount(String name, int count){
        this.name = name;
        this.count = count;
        this.rendableCount = count;
    }

    public String getName(){
        return this.name;
    }

    public int getCount(){
        return this.count;
    }

    public void setCount(int add){
        this.count += add;
        this.rendableCount += add;
    }

    public void reduceRendableCount(){
        this.rendableCount--;
    }

    public void addRendableCount(){
        this.rendableCount++;
    }

    public int getRendableCount(){
        return rendableCount;
    }
}
