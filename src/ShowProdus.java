import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.awt.*;

public class ShowProdus extends JPanel {
    private static JTable table;
    private DefaultTableModel tableModel;

    ShowProdus() throws ClassNotFoundException {
        initializeUI();
    }

    private void initializeUI() throws ClassNotFoundException {
        setLayout(new BorderLayout());

        // Definiți numele coloanelor pentru tabel
        String columnNames[] = {"Brand", "Model", "Pret", "Descriere", "Stoc"};

        // Recuperați toate produsele din baza de date
        List<Produs> allProducts = dbUtils.getAllActiveProducts();

        // Inițializați un model de tabel cu datele produselor și coloanele corespunzătoare
        tableModel = new DefaultTableModel(getDataArray(allProducts), columnNames) {
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
        titlePanel.add(new JLabel("Produs Table"), BorderLayout.NORTH);

        // Combinați panoul de titlu cu panoul de derulare
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(titlePanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Combinați panoul de căutare și panoul de tabel
        add(tablePanel, BorderLayout.CENTER);

        // Stabiliți dimensiunile și comportamentul panoului ShowProdus
        setSize(700, 400);
    }

    private String[][] getDataArray(List<Produs> products) {
        return products.stream()
                .map(product -> new String[]{
                        product.getBrand(),
                        product.getModel(),
                        String.valueOf(product.getPret()),
                        product.getDescriere(),
                        String.valueOf(product.getStoc())
                })
                .toArray(String[][]::new);
    }
}
