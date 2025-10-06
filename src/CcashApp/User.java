package src.CcashApp;

public class User {
    private int id;
    private String name;
    private String email;
    private String number;
    private String pin;

    public User(int id, String name, String email, String number, String pin) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.number = number;
        this.pin = pin;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getNumber() { return number; }
    public String getPin() { return pin; }

    public void setPin(String newPin) { this.pin = newPin; }
}
