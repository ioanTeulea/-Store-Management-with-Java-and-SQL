import java.util.Date;

public class Client {
    private int clientId;
    private String firstName;
    private String lastName;
    private String adresa;
    private String email;
    private String nrTel;
    private boolean isActive;

    // Constructori
    public Client(String firstName, String lastName, String adresa, String email, String nrTel, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.adresa = adresa;
        this.email = email;
        this.nrTel = nrTel;
        this.isActive = isActive;
    }
    public Client(int clientId, String firstName, String lastName, String adresa, String email, String nrTel, boolean isActive) {
        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.adresa = adresa;
        this.email = email;
        this.nrTel = nrTel;
        this.isActive = isActive;
    }

    // Getteri și Setteri

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNrTel() {
        return nrTel;
    }

    public void setNrTel(String nrTel) {
        this.nrTel = nrTel;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // Alte metode
    public String getWelcomeMessage() {
        return "Hello, " + firstName + " " + lastName + "!";
    }

    // Poți adăuga și altele în funcție de nevoi
}