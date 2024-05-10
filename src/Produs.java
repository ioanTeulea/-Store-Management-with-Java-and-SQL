public class Produs {
    private int produsId;
    private String brand;
    private String model;
    private double pret;
    private String descriere;
    private int stoc;
    private boolean isActive;

    public Produs(String brand, String model, double pret, String descriere, int stoc, boolean isActive) {
        this.brand = brand;
        this.model = model;
        this.pret = pret;
        this.descriere = descriere;
        this.stoc = stoc;
        this.isActive = isActive;
    }

    public Produs(int produsId, String brand, String model, double pret, String descriere, int stoc, boolean isActive) {
        this.produsId = produsId;
        this.brand = brand;
        this.model = model;
        this.pret = pret;
        this.descriere = descriere;
        this.stoc = stoc;
        this.isActive = isActive;
    }

    // Getters and setters

    public int getProdusId() {
        return produsId;
    }

    public void setProdusId(int produsId) {
        this.produsId = produsId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getPret() {
        return pret;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public int getStoc() {
        return stoc;
    }

    public void setStoc(int stoc) {
        this.stoc = stoc;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Produs{" +
                "produsId=" + produsId +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", pret=" + pret +
                ", descriere='" + descriere + '\'' +
                ", stoc=" + stoc +
                ", isActive=" + isActive +
                '}';
    }
}