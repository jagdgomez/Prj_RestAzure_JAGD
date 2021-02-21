package pojo;

public class User {

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private String email;
    private String password;
    private String name;

    public String getName() {
        return name;
    }

        public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }

}
