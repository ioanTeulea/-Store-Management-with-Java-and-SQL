import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ShowComanda extends JPanel {
    private static JTable table;
    private DefaultTableModel tableModel;

    ShowComanda()throws ClassNotFoundException {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Definiți numele coloanelor pentru tabel
        String columnNames[] = {"Produs", "Client", "Data", "Status"};

        // Recuperați toate comenzile din baza de date
        List<Comanda> allOrders = dbUtils.getAllActiveComenzi();

        // Inițializați un model de tabel cu datele comenzilor și coloanele corespunzătoare
        tableModel = new DefaultTableModel(getDataArray(allOrders), columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Faceți toate coloanele necaptabile
                return false;
            }
        };

        // Inițializați un tabel cu modelul creat
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);

        // Adăugați tabelul la un panou de derulare
        JScrollPane scrollPane = new JScrollPane(table);

        // Creați un panou de titlu
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(new JLabel("Comanda Table"), BorderLayout.NORTH);

        // Combinați panoul de titlu cu panoul de derulare
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(titlePanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Combinați panoul de căutare și panoul de tabel
        add(tablePanel, BorderLayout.CENTER);

        // Stabiliți dimensiunile și comportamentul panoului ShowComanda
        setSize(700, 400);
    }

    private String[][] getDataArray(List<Comanda> orders) {
        return orders.stream()
                .map(order -> new String[]{
                        order.getProdus() != null ? order.getProdus().getBrand() + " " + order.getProdus().getModel() : "",
                        order.getClient() != null ? order.getClient().getFirstName() + " " + order.getClient().getLastName() : "",
                        order.getData().toString(),
                        order.getStatus()
                })
                .toArray(String[][]::new);
    }
}
