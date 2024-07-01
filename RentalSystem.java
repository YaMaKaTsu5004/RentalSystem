import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RentalSystem{
    public static ArrayList<Customer> customers = new ArrayList<>();
    public static ArrayList<Furniture> furnitures = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<>();
    

    private static Thread hook = new Thread(){
        @Override
        public void run(){
            try{
                if(!(new File("./data").exists())){
                    Files.createDirectory(Paths.get("./data"));
                }
                if(!(new File("./data/logs").exists())){
                    Files.createDirectory(Paths.get("./data/logs"));
                }
                ObjectOutputStream foos = new ObjectOutputStream(new FileOutputStream("./data/furnitures.ser"));
                ObjectOutputStream fcoos = new ObjectOutputStream(new FileOutputStream("./data/furnitures_count.ser"));
                ObjectOutputStream coos = new ObjectOutputStream(new FileOutputStream("./data/customers.ser"));

                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String dateStr = sdf.format(timestamp);

                File logFile = new File("./data/logs/" + dateStr + ".txt");
                BufferedWriter logbw = new BufferedWriter(new FileWriter(logFile, true));

                GUI.lastLog();
                for(int i = 0; i < GUI.log.size(); i++){
                    logbw.write(GUI.log.get(i));
                    logbw.newLine();
                }

                logbw.close();
                
                foos.writeObject(furnitures);
                fcoos.writeObject(Furniture.furnitureCounts);
                coos.writeObject(customers);
                
                
                foos.close();
                fcoos.close();
                coos.close();
                

                ObjectOutputStream woos = new ObjectOutputStream(new FileOutputStream("./data/users.ser"));
                woos.writeObject(users);
                woos.close();

            
            }catch(Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }
    };

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(hook);

        try{
            ObjectInputStream wois = new ObjectInputStream(new FileInputStream("./data/users.ser"));

            @SuppressWarnings("unchecked")
            ArrayList<User> tmpusers = (ArrayList<User>) wois.readObject();
            users = tmpusers;

            wois.close();

        } catch (IOException |ClassNotFoundException e) {
            User defaultAdmin = new User("admin", "password", true);
            users.add(defaultAdmin);
        }
        
        try{
            ObjectInputStream fois = new ObjectInputStream(new FileInputStream("./data/furnitures.ser"));
            ObjectInputStream fcois = new ObjectInputStream(new FileInputStream("./data/furnitures_count.ser"));
            ObjectInputStream cois = new ObjectInputStream(new FileInputStream("./data/customers.ser"));

            @SuppressWarnings("unchecked")
            ArrayList<Furniture> tmpFurnitures = (ArrayList<Furniture>) fois.readObject();
            furnitures = tmpFurnitures;
            @SuppressWarnings("unchecked")
            ArrayList<FurnitureCount> tmpFurnitureCounts = (ArrayList<FurnitureCount>) fcois.readObject();
            Furniture.furnitureCounts = tmpFurnitureCounts;
            @SuppressWarnings("unchecked")
            ArrayList<Customer> tmpCustomers = (ArrayList<Customer>) cois.readObject();
            customers = tmpCustomers;

            fois.close();
            fcois.close();
            cois.close();
            
        } catch (IOException | ClassNotFoundException e) {   
        }
        GUI.Gui();
    }
}
