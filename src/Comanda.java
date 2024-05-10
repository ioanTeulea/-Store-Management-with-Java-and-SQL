import java.text.SimpleDateFormat;
import java.util.Date;

public class Comanda {
    private int comandaId;
    private Produs produs;
    private Client client;
    private int produsId;
    private int clientId;
    private String dataString;
    private Date data;
    private String status;
    private boolean active;

    // Constructori, getteri, setteri

    public Comanda(int comandaId, Produs produs, Client client, Date data, String status, boolean active) {
        this.comandaId = comandaId;
        this.produs = produs;
        this.client = client;
        this.data = data;
        this.status = status;
        this.active = active;
    }

    public Comanda(Produs produs, Client client, Date data, String status, boolean active) {
        this.produs = produs;
        this.client = client;
        this.data = data;
        this.status = status;
        this.active = active;
    }
    public Comanda(Produs produs, Client client, String data, String status, boolean active) {
        this.produs = produs;
        this.client = client;
        this.dataString = data;
        this.status = status;
        this.active = active;
    }
    public Comanda(int produs, int client, Date data, String status, boolean active) {
        this.produsId = produs;
        this.clientId = client;
        this.data = data;
        this.status = status;
        this.active = active;
    }


    // Getteri și setteri

    public int getComandaId() {
        return comandaId;
    }

    public void setComandaId(int comandaId) {
        this.comandaId = comandaId;
    }

    public Produs getProdus() {
        return produs;
    }

    public void setProdus(Produs produs) {
        this.produs = produs;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Metoda pentru a transforma obiectul într-un rând pentru tabel
    public Object[] toTableRow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return new Object[]{
                comandaId,
                produs != null ? produs.getBrand() + " " + produs.getModel() : "",
                client != null ? client.getFirstName() + " " + client.getLastName() : "",
                data != null ? dateFormat.format(data) : "N/A",
                status,
                active
        };
    }
    public Object[] toTableObjectsRow() {
        return new Object[]{
                comandaId,
                produs.getBrand() + " - " + produs.getModel(),
                client.getFirstName() + " " + client.getLastName(),
                data,
                status
        };
    }
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = data != null ? dateFormat.format(data) : "N/A";

        return "Comanda{" +
                "comandaId=" + comandaId +
                ", produs=" + (produs != null ? produs.getBrand() + " " + produs.getModel() : "N/A") +
                ", client=" + (client != null ? client.getFirstName() + " " + client.getLastName() : "N/A") +
                ", data=" + formattedDate +
                ", status='" + status + '\'' +
                ", active=" + active +
                '}';
    }
}