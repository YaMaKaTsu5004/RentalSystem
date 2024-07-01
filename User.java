import java.io.Serializable;

public class User implements Serializable{
    private String name;
    private String password;
    private boolean adminstar;

    User(String name, String password){
        this.name = name;
        this.password = password;
    }

    User(String name, String password, boolean adminstar){
        this(name, password);
        this.adminstar = adminstar;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public boolean getAdminster() {
        return adminstar;
    }
}
