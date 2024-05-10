import java.sql.*;
import java.util.List;
import java.util.ArrayList;
public class dbUtils {

    public static int isWorking()
    {
        if(getConnection()==null)
        {
            return 0;
        }

        return 1;
    }

    public static Connection getConnection()
    {
        Connection conn;

        String connectionUrl;

        //connectionUrl = ApplicationProperties.getProperties().getProperty(Constants.CONNECTION_STRING);
        connectionUrl = "jdbc:sqlserver://localhost:1433;database=dbShop;user=sa1;password=Admin123!;encrypt=true;trustServerCertificate=true";

        try
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(connectionUrl);

            if(conn==null)
            {
                return null;
            }

            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery("dbo.spComandaSelectAllActive");
            while (resultSet.next())
            {
                System.out.println(resultSet.getString(2) + " " + resultSet.getString(3));
            }

        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            return null;
        }
        return conn;
    }
    public static List<Client> getAllActiveClients() {
        List<Client> clients = new ArrayList<>();

        // Ia toți clienții din baza de date unde active = true
        try (Connection con = getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("spClientSelectAllActive");
            while (rs.next()) {
                int id = rs.getInt(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String cnp = rs.getString(4);
                String email = rs.getString(5);
                String phoneNumber = rs.getString(6);
                Client client = new Client( firstName, lastName, cnp, email, phoneNumber, true);
                client.setClientId(id);
                clients.add(client);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return clients;
    }
    public static void addClient(Client client) {
        String spCall = "{call spClientInsert(?, ?, ?, ?, ?, ?)}";

        try (Connection conn = getConnection();
             CallableStatement callableStatement = conn.prepareCall(spCall)) {

            callableStatement.setString(1, client.getFirstName());
            callableStatement.setString(2, client.getLastName());
            callableStatement.setString(3, client.getAdresa());
            callableStatement.setString(4, client.getEmail());
            callableStatement.setString(5, client.getNrTel());
            callableStatement.setBoolean(6, client.isActive());

            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();  // sau altă logică de gestionare a erorilor
        }
    }
    public static void updateClient(Client client) {
        String spCall = "{call spClientUpdate(?, ?, ?, ?, ?, ?)}";

        try (Connection conn = getConnection();
             CallableStatement callableStatement = conn.prepareCall(spCall)) {

            callableStatement.setInt(1, client.getClientId());  // presupunând că getClientId returnează ID-ul clientului
            callableStatement.setString(2, client.getFirstName());
            callableStatement.setString(3, client.getLastName());
            callableStatement.setString(4, client.getAdresa());
            callableStatement.setString(5, client.getEmail());
            callableStatement.setString(6, client.getNrTel());

            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();  // sau altă logică de gestionare a erorilor
        }
    }
    public static void deleteClient(int clientId) {
        String spCall = "{call spClientDelete(?)}";

        try (Connection conn = getConnection();
             CallableStatement callableStatement = conn.prepareCall(spCall)) {

            callableStatement.setInt(1, clientId);

            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();  // sau altă logică de gestionare a erorilor
        }
    }
    public static List<Produs> getAllActiveProducts() {
        List<Produs> products = new ArrayList<>();

        String spCall = "{call spProductSelectAllActive}";

        try (Connection conn = getConnection();
             CallableStatement callableStatement = conn.prepareCall(spCall);
             ResultSet resultSet = callableStatement.executeQuery()) {

            while (resultSet.next()) {
                int produsId = resultSet.getInt("ProdusID");
                String brand = resultSet.getString("Brand");
                String model = resultSet.getString("Model");
                double pret = resultSet.getDouble("Pret");
                String descriere = resultSet.getString("Descriere");
                int stoc = resultSet.getInt("Stoc");
                boolean active = resultSet.getBoolean("Active");

                Produs product = new Produs(brand, model, pret, descriere, stoc, active);
                product.setProdusId(produsId);
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();  // sau altă logică de gestionare a erorilor
        }

        return products;
    }
    public static void addProduct(Produs product) {
        String spCall = "{call spProductInsert(?, ?, ?, ?, ?, ?)}";

        try (Connection conn = getConnection();
             CallableStatement callableStatement = conn.prepareCall(spCall)) {

            callableStatement.setString(1, product.getBrand());
            callableStatement.setString(2, product.getModel());
            callableStatement.setDouble(3, product.getPret());
            callableStatement.setString(4, product.getDescriere());
            callableStatement.setInt(5, product.getStoc());
            callableStatement.setBoolean(6, product.isActive());

            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();  // sau altă logică de gestionare a erorilor
        }
    }
    public static void updateProduct(Produs product) {
        String spCall = "{call spProductUpdate(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = getConnection();
             CallableStatement callableStatement = conn.prepareCall(spCall)) {

            callableStatement.setInt(1, product.getProdusId());
            callableStatement.setString(2, product.getBrand());
            callableStatement.setString(3, product.getModel());
            callableStatement.setDouble(4, product.getPret());
            callableStatement.setString(5, product.getDescriere());
            callableStatement.setInt(6, product.getStoc());
            callableStatement.setBoolean(7, product.isActive());

            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();  // sau altă logică de gestionare a erorilor
        }
    }
    public static void deleteProduct(int productId) {
        String spCall = "{call spProductDelete(?)}";

        try (Connection conn = getConnection();
             CallableStatement callableStatement = conn.prepareCall(spCall)) {

            callableStatement.setInt(1, productId);
            System.out.println(productId);

            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();  // sau altă logică de gestionare a erorilor
        }
    }
    public static List<Comanda> getAllActiveComenzi() {
        List<Comanda> comenzi = new ArrayList<>();

        String spCall = "{call spComandaSelectAllActive}";

        try (Connection conn = getConnection();
             CallableStatement callableStatement = conn.prepareCall(spCall);
             ResultSet resultSet = callableStatement.executeQuery()) {

            while (resultSet.next()) {
                int comandaId = resultSet.getInt("ComandaID");
                int produsId = resultSet.getInt("ProdusID");
                int clientId = resultSet.getInt("ClientID");
                Date data = resultSet.getDate("Data");
                String status = resultSet.getString("Status");
                boolean active = resultSet.getBoolean("Active");

                // Obțineți Produsul și Clientul din baza de date pe baza ID-urilor
                Produs produs = getProdusById(produsId);
                Client client = getClientById(clientId);

                // Schimbați acest constructor cu cel corespunzător clasei Comanda
                Comanda comanda = new Comanda(produs, client, data, status, active);

                comanda.setComandaId(comandaId);
                comenzi.add(comanda);
            }

        } catch (SQLException e) {
            e.printStackTrace();  // sau altă logică de gestionare a erorilor
        }

        return comenzi;
    }

    // Implementați metoda pentru a obține un Produs după ID
    private static Produs getProdusById(int produsId) {
        String spCall = "{call spProductSelectById(?)}";

        try (Connection conn = getConnection();
             CallableStatement callableStatement = conn.prepareCall(spCall)) {

            callableStatement.setInt(1, produsId);
            try (ResultSet resultSet = callableStatement.executeQuery()) {
                if (resultSet.next()) {
                    String brand = resultSet.getString("Brand");
                    String model = resultSet.getString("Model");
                    double pret = resultSet.getDouble("Pret");
                    String descriere = resultSet.getString("Descriere");
                    int stoc = resultSet.getInt("Stoc");
                    boolean active = resultSet.getBoolean("Active");

                    return new Produs(brand, model, pret, descriere, stoc, active);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();  // sau altă logică de gestionare a erorilor
        }

        return null;
    }

    // Implementați metoda pentru a obține un Client după ID
    private static Client getClientById(int clientId) {
        String spCall = "{call spClientSelectById(?)}";

        try (Connection conn = getConnection();
             CallableStatement callableStatement = conn.prepareCall(spCall)) {

            callableStatement.setInt(1, clientId);
            try (ResultSet resultSet = callableStatement.executeQuery()) {
                if (resultSet.next()) {
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                    String cnp = resultSet.getString("Adresa");
                    String email = resultSet.getString("Email");
                    String phoneNumber = resultSet.getString("NrTel");
                    boolean active = resultSet.getBoolean("Active");

                    return new Client(firstName, lastName, cnp, email, phoneNumber, active);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();  // sau altă logică de gestionare a erorilor
        }

        return null;
    }
    public static void addOrder(Comanda comanda) {
        String addOrderProcedure = "{call AddOrder(?, ?, ?, ?, ?)}";
        try (Connection conn = getConnection();
        CallableStatement callableStatement = conn.prepareCall(addOrderProcedure)) {
            callableStatement.setInt(1, comanda.getProdus().getProdusId());  // sau orice alt mod de a obține ID-ul produsului
            callableStatement.setInt(2, comanda.getClient().getClientId());  // sau orice alt mod de a obține ID-ul clientului
            callableStatement.setDate(3, new java.sql.Date(comanda.getData().getTime()));  // convertește Date la java.sql.Date
            callableStatement.setString(4, comanda.getStatus());
            callableStatement.setBoolean(5, comanda.isActive());

            // Execută procedura stocată
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace(); // sau tratează eroarea în mod corespunzător
        }
    }
    public static  void updateOrder(Comanda comanda) {
        String updateOrderProcedure = "{call UpdateOrder(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = getConnection();
        CallableStatement callableStatement = conn.prepareCall(updateOrderProcedure)) {
            callableStatement.setInt(1, comanda.getComandaId());
            callableStatement.setInt(2, comanda.getProdus().getProdusId());  // sau orice alt mod de a obține ID-ul produsului
            callableStatement.setInt(3, comanda.getClient().getClientId());  // sau orice alt mod de a obține ID-ul clientului
            callableStatement.setDate(4, new java.sql.Date(comanda.getData().getTime()));  // convertește Date la java.sql.Date
            callableStatement.setString(5, comanda.getStatus());
            callableStatement.setBoolean(6, comanda.isActive());

            // Execută procedura stocată
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace(); // sau tratează eroarea în mod corespunzător
        }
    }
    public static void deleteOrder(int orderId) {
        String deleteOrderProcedure = "{call DeleteOrder(?)}";
        try (Connection conn = getConnection();
             CallableStatement callableStatement = conn.prepareCall(deleteOrderProcedure)) {
            callableStatement.setInt(1, orderId);

            // Execută procedura stocată
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace(); // sau tratează eroarea în mod corespunzător
        }
    }




}


