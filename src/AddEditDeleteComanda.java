import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddEditDeleteComanda extends JPanel {
    private static JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    AddEditDeleteComanda() throws ClassNotFoundException {
        initializeUI();
    }

    private void initializeUI() throws ClassNotFoundException {
        setLayout(new BorderLayout());
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> handleAdd());
        // Definiți numele coloanelor pentru tabel
        String columnNames[] = {"Id", "Produs", "Client", "Data", "Status"};

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

        // Setează coloana "ID" să fie invizibilă
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.getColumnModel().getColumn(0).setResizable(false);

        // Adăugați tabelul la un panou de derulare
        scrollPane = new JScrollPane(table);

        // Creați un panou de titlu
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(new JLabel("Order Table"), BorderLayout.NORTH);
        titlePanel.add(addButton, BorderLayout.EAST);

        // Combinați panoul de titlu cu panoul de derulare
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(titlePanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Combinați panoul de căutare și panoul de tabel
        add(tablePanel, BorderLayout.CENTER);

        // Adăugați meniul de context pentru acțiunile cu clic dreapta
        addTableContextMenu();
        addOutsideTableContextMenu();

        // Stabiliți dimensiunile și comportamentul panoului AddEditDeleteComanda
        setSize(700, 400);

        scrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());

                    if (row == -1) {
                        // Nu s-a făcut clic pe niciun rând, deci deselectați rândul curent
                        table.clearSelection();
                    }
                }
            }
        });
    }

    private String[][] getDataArray(List<Comanda> orders) {
        return orders.stream()
                .map(order -> new String[]{
                        String.valueOf(order.getComandaId()),
                        order.getProdus().getBrand() + " - " + order.getProdus().getModel(),
                        order.getClient().getFirstName() + " " + order.getClient().getLastName(),
                        order.getData().toString(),
                        order.getStatus()
                })
                .toArray(String[][]::new);
    }

    private void addTableContextMenu() {
        // Adăugați opțiunile pentru meniul de context "Edit" și "Delete"
        JMenuItem editMenuItem = new JMenuItem("Edit");
        JMenuItem deleteMenuItem = new JMenuItem("Delete");
        editMenuItem.addActionListener(this::handleAddOrEdit);
        deleteMenuItem.addActionListener(this::handleDelete);

        // Meniu de context pentru acțiuni în tabel
        JPopupMenu tableContextMenu = new JPopupMenu();
        tableContextMenu.add(editMenuItem);
        tableContextMenu.add(deleteMenuItem);

        // Adăugați ascultător de mouse pentru meniul de context în tabel
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());

                    // Selectați rândul dacă nu este deja selectat
                    if (!table.isRowSelected(row)) {
                        table.getSelectionModel().setSelectionInterval(row, row);
                    }

                    // Afișați meniul de context în tabel
                    tableContextMenu.show(table, e.getX(), e.getY());
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    // Deselectați rândul dacă faceți clic stânga în afara tabelului
                    table.clearSelection();
                }
            }
        });
    }

    private void addOutsideTableContextMenu() {
        // Meniu de context pentru acțiuni în afara tabelului
        JPopupMenu outsideTableContextMenu = new JPopupMenu();
        JMenuItem addMenuItem = new JMenuItem("Add");
        addMenuItem.addActionListener(this::handleAddOrEdit);
        outsideTableContextMenu.add(addMenuItem);

        // Adăugați ascultător de mouse pentru panoul principal pentru a afișa meniul de context în afara tabelului
        scrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // Afișați meniul de context în afara tabelului
                    System.out.println("Mouse pressed on panel.");
                    outsideTableContextMenu.show(AddEditDeleteComanda.this, e.getX(), e.getY());
                }
            }
        });
    }

    private JPanel createDialogPanel(int selectedRow) {
        JPanel panel = new JPanel(new GridLayout(7, 2));

        JTextField idField = new JTextField();
        idField.setEditable(false);
        idField.setVisible(false);

        if (selectedRow != -1) {
            idField.setText(table.getValueAt(selectedRow, 0).toString());
        }

        panel.add(new JLabel("Selectați produs:"));
        JComboBox<String> produsComboBox = new JComboBox<>();
        List<Produs> allProducts = dbUtils.getAllActiveProducts();
        for (Produs product : allProducts) {
            produsComboBox.addItem(product.getBrand() + " - " + product.getModel());
        }
        panel.add(produsComboBox);
        if (selectedRow != -1) {
            // obține valoarea din rândul selectat
            String selectedProduct = (String) table.getValueAt(selectedRow, 1);
            produsComboBox.setSelectedItem(selectedProduct);
        }

        panel.add(new JLabel("Selectați client:"));
        JComboBox<String> clientComboBox = new JComboBox<>();
        List<Client> allClients = dbUtils.getAllActiveClients();
        for (Client client : allClients) {
            clientComboBox.addItem(client.getFirstName() + " " + client.getLastName());
        }
        panel.add(clientComboBox);
        if (selectedRow != -1) {
            // obține valoarea din rândul selectat
            String selectedClient = (String) table.getValueAt(selectedRow, 2);
            clientComboBox.setSelectedItem(selectedClient);
        }

        panel.add(new JLabel("Data:"));
        JTextField dataField = new JTextField();
        if (selectedRow != -1) {
            dataField.setText((String) table.getValueAt(selectedRow, 3));
        }
        panel.add(dataField);

        panel.add(new JLabel("Status:"));
        JTextField statusField = new JTextField();
        if (selectedRow != -1) {
            statusField.setText((String) table.getValueAt(selectedRow, 4));
        }
        panel.add(statusField);


        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                selectedRow == -1 ? "Introduceți informațiile" : "Editați informațiile",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            String selectedProduct = (String) produsComboBox.getSelectedItem();
            String selectedClient = (String) clientComboBox.getSelectedItem();

            String[] parts = selectedProduct.split(" - ");
            String selectedBrand = parts[0];
            String selectedModel = parts[1];

            // Cautați produsul în lista de produse pentru a obține obiectul Produs
            Produs selectedProductObject = null;
            for (Produs product : allProducts) {
                if (product.getBrand().equals(selectedBrand) && product.getModel().equals(selectedModel)) {
                    selectedProductObject = product;
                    break;
                }
            }

            // Cautați clientul în lista de clienți pentru a obține obiectul Client
            Client selectedClientObject = null;
            for (Client client : allClients) {
                String fullName = client.getFirstName() + " " + client.getLastName();
                if (fullName.equals(selectedClient)) {
                    selectedClientObject = client;
                    break;
                }
            }

            // Restul codului pentru a obține restul datelor (data, status, etc.)

            if (selectedRow == -1) {
                // Adăugare comandă nouă
                String data = dataField.getText();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // specificați formatul dorit
                Date dataCalen = null;
                try {
                    dataCalen = dateFormat.parse(data);
                    System.out.println(dataCalen);
                } catch (ParseException e) {
                    e.printStackTrace(); // tratați eroarea în mod corespunzător, de exemplu, afișând un mesaj utilizatorului
                }

                String status = statusField.getText();
                Comanda orderToAdd = new Comanda(selectedProductObject, selectedClientObject, dataCalen, status, true);
                Object[] rowData = orderToAdd.toTableRow();
                // Adăugare datelor în tabel
                ((DefaultTableModel) table.getModel()).addRow(rowData);

                // Adăugați în baza de date aici
                 dbUtils.addOrder(orderToAdd);

            } else {
                // Editare comandă existentă
                int idValue = Integer.parseInt(idField.getText());
                String data = dataField.getText();
                String status = statusField.getText();

                Object currentselectedProduct = produsComboBox.getSelectedItem();
                Object currentselectedClient = clientComboBox.getSelectedItem();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // specificați formatul dorit
                Date dataCalen = null;
                try {
                    dataCalen = dateFormat.parse(data);
                    System.out.println(dataCalen);
                } catch (ParseException e) {
                    e.printStackTrace(); // tratați eroarea în mod corespunzător, de exemplu, afișând un mesaj utilizatorului
                }
                Comanda updatedOrder = new Comanda(idValue, selectedProductObject, selectedClientObject, dataCalen, status, true);

                // Actualizați datele în tabel
                Object[] rowData = updatedOrder.toTableObjectsRow();
                for (int i = 0; i < rowData.length; i++) {
                    table.setValueAt(rowData[i], selectedRow, i);
                }
                produsComboBox.setSelectedItem(currentselectedProduct);
                clientComboBox.setSelectedItem(currentselectedClient);
                table.setValueAt(data, selectedRow, 3);
                table.setValueAt(status, selectedRow, 4);

                // Actualizați în baza de date aici

                 dbUtils.updateOrder(updatedOrder);
            }
        }
        return panel;
    }

    private void handleAdd() {
        handleAddOrEdit(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Add"));
    }

    private void handleAddOrEdit(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        JPanel panel = createDialogPanel(selectedRow);
    }

    private void handleDelete(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int orderId = Integer.parseInt((String) tableModel.getValueAt(selectedRow, 0));
            int dialogResult = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                ((DefaultTableModel) table.getModel()).removeRow(selectedRow);
                dbUtils.deleteOrder(orderId);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
