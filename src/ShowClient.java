import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;

public class ShowClient extends JPanel {
    private static JTable table;
    private DefaultTableModel tableModel;

    ShowClient() throws ClassNotFoundException {
        initializeUI();
    }
    private void initializeUI() throws ClassNotFoundException {
        setLayout(new BorderLayout());

        // Definiți numele coloanelor pentru tabel
        String columnNames[] = {"FirstName", "LastName", "Adresa", "Email", "Telefon"};

        // Recuperați toți clienții din baza de date
        List<Client> allClients = dbUtils.getAllActiveClients();

        // Inițializați un model de tabel cu datele clienților și coloanele corespunzătoare
        tableModel = new DefaultTableModel(getDataArray(allClients), columnNames) {
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
        titlePanel.add(new JLabel("Client Table"), BorderLayout.NORTH);

        // Combinați panoul de titlu cu panoul de derulare
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(titlePanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Combinați panoul de căutare și panoul de tabel
        add(tablePanel, BorderLayout.CENTER);

        // Stabiliți dimensiunile și comportamentul panoului FrameShowClient
        setSize(700, 400);

    }
    private String[][] getDataArray(List<Client> students) {
        return students.stream()
                .map(student -> new String[]{
                        student.getFirstName(),
                        student.getLastName(),
                        student.getAdresa(),
                        student.getEmail(),
                        student.getNrTel()
                })
                .toArray(String[][]::new);
    }
}