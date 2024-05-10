import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AddEditDeleteClient extends JPanel {
    private static JTable table;
    private DefaultTableModel tableModel;

    private  JScrollPane scrollPane;
    AddEditDeleteClient() throws ClassNotFoundException {
        initializeUI();
    }

    private void initializeUI() throws ClassNotFoundException {
        setLayout(new BorderLayout());
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> handleAdd());
        // Definiți numele coloanelor pentru tabel
        String columnNames[] = {"Id","FirstName", "LastName", "Adresa", "Email", "Telefon"};

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
        titlePanel.add(new JLabel("Client Table"), BorderLayout.NORTH);
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

        // Stabiliți dimensiunile și comportamentul panoului FrameShowClient
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


    private String[][] getDataArray(List<Client> clients) {
        return clients.stream()
                .map(client -> new String[]{
                        String.valueOf(client.getClientId()), // Adăugați ID-ul înaintea celorlalte câmpuri
                        client.getFirstName(),
                        client.getLastName(),
                        client.getAdresa(),
                        client.getEmail(),
                        client.getNrTel()
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
                    outsideTableContextMenu.show(AddEditDeleteClient.this, e.getX(), e.getY());
                }
            }
        });
    }



    private JPanel createDialogPanel(int selectedRow) {
        JPanel panel = new JPanel(new GridLayout(6, 2));


        //panel.add(new JLabel("ID:"));
        JTextField idField = new JTextField();
        idField.setEditable(false);
        idField.setVisible(false);

        if (selectedRow != -1) {
            idField.setText((String) table.getValueAt(selectedRow, 0));
        }

        //panel.add(idField);

        panel.add(new JLabel("First Name:"));
        JTextField firstNameField = new JTextField();
        if (selectedRow!=-1) {
            firstNameField.setText((String) table.getValueAt(selectedRow, 1));
        }
        panel.add(firstNameField);

        panel.add(new JLabel("Last Name:"));
        JTextField lastNameField = new JTextField();
        if (selectedRow!=-1) {
            lastNameField.setText((String) table.getValueAt(selectedRow, 2));
        }
        panel.add(lastNameField);

        panel.add(new JLabel("Adresa:"));
        JTextField adresaField = new JTextField();
        if (selectedRow!=-1) {
            adresaField.setText((String) table.getValueAt(selectedRow, 3));
        }
        panel.add(adresaField);

        panel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        if (selectedRow!=-1) {
            emailField.setText((String) table.getValueAt(selectedRow, 4));
        }
        panel.add(emailField);

        panel.add(new JLabel("Telefon:"));
        JTextField nrTelField = new JTextField();
        if (selectedRow!=-1) {
            nrTelField.setText((String) table.getValueAt(selectedRow, 5));
        }
        panel.add(nrTelField);
        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                selectedRow == -1 ? "Introduceți informațiile" : "Editați informațiile",
                JOptionPane.OK_CANCEL_OPTION
        );
        // Verificarea apăsării butonului "OK"
        if (result == JOptionPane.OK_OPTION) {
            if(selectedRow==-1)
            {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String adresa = adresaField.getText();
                String email = emailField.getText();
                String nrTel = nrTelField.getText();

                // Adăugarea datelor în tabel
                ((DefaultTableModel) table.getModel()).addRow(new Object[]{firstName, lastName, adresa, email, nrTel});
                // Adăugați în baza de date aici
                Client clientToAdd = new Client(firstName,lastName,adresa,email,nrTel,true);
                dbUtils.addClient(clientToAdd);
            }
            else
            {
                int idValue = Integer.parseInt(idField.getText());
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String adresa = adresaField.getText();
                String email = emailField.getText();
                String nrTel = nrTelField.getText();

                // Actualizați datele în tabel
                table.setValueAt(firstName, selectedRow, 1);
                table.setValueAt(lastName, selectedRow, 2);
                table.setValueAt(adresa, selectedRow, 3);
                table.setValueAt(email, selectedRow, 4);
                table.setValueAt(nrTel, selectedRow, 5);
                // Actualizați în baza de date aici
                Client updatedClient = new Client(idValue, firstName, lastName, adresa, email, nrTel, true);
                dbUtils.updateClient(updatedClient);

            }
        }
        return panel;
    }

    private void handleAdd() {
        // Apelați metoda handleAddOrEdit și furnizați un argument care indică că este o acțiune de adăugare
        handleAddOrEdit(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Add"));
    }

    private void handleAddOrEdit(ActionEvent e) {
        // Obțineți rândul selectat
        int selectedRow = table.getSelectedRow();

        JPanel panel = createDialogPanel(selectedRow);
    }

    private void handleDelete(ActionEvent e) {
        // Obțineți rândul selectat
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int clientId = Integer.parseInt((String) tableModel.getValueAt(selectedRow, 0));
            // Afișați un dialog de confirmare
            int dialogResult = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                // Ștergeți clientul din tabel și din baza de date
                ((DefaultTableModel) table.getModel()).removeRow(selectedRow);
                // Implementați ștergerea din baza de date aici
                dbUtils.deleteClient(clientId);

            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}