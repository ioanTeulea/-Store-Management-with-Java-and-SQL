import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AddEditDeleteProdus extends JPanel {
    private static JTable table;
    private DefaultTableModel tableModel;

    private  JScrollPane scrollPane;
    AddEditDeleteProdus() throws ClassNotFoundException {
        initializeUI();
    }

    private void initializeUI() throws ClassNotFoundException {
        setLayout(new BorderLayout());
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> handleAdd());
        // Definiți numele coloanelor pentru tabel
        String columnNames[] = {"Id", "Brand", "Model", "Pret", "Descriere", "Stoc"};

        // Recuperați toți produsele din baza de date
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
        titlePanel.add(new JLabel("Product Table"), BorderLayout.NORTH);
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

        // Stabiliți dimensiunile și comportamentul panoului FrameShowProduct
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

    private String[][] getDataArray(List<Produs> products) {
        return products.stream()
                .map(product -> new String[]{
                        String.valueOf(product.getProdusId()),
                        product.getBrand(),
                        product.getModel(),
                        String.valueOf(product.getPret()),
                        product.getDescriere(),
                        String.valueOf(product.getStoc())
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
        // Adăugați ascultător de mouse pentru panoul principal pentru a afișa meniul de context în afara tabelului
        scrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // Afișați meniul de context în afara tabelului
                    System.out.println("Mouse pressed on panel.");
                    outsideTableContextMenu.show(AddEditDeleteProdus.this, e.getX(), e.getY());
                }
            }
        });
    }

    private JPanel createDialogPanel(int selectedRow) {
        JPanel panel = new JPanel(new GridLayout(6, 2));

        JTextField idField = new JTextField();
        idField.setEditable(false);
        idField.setVisible(false);

        if (selectedRow != -1) {
            idField.setText((String) table.getValueAt(selectedRow, 0));
        }

        panel.add(new JLabel("Brand:"));
        JTextField brandField = new JTextField();
        if (selectedRow != -1) {
            brandField.setText((String) table.getValueAt(selectedRow, 1));
        }
        panel.add(brandField);

        panel.add(new JLabel("Model:"));
        JTextField modelField = new JTextField();
        if (selectedRow != -1) {
            modelField.setText((String) table.getValueAt(selectedRow, 2));
        }
        panel.add(modelField);

        panel.add(new JLabel("Pret:"));
        JTextField pretField = new JTextField();
        if (selectedRow != -1) {
            pretField.setText((String) table.getValueAt(selectedRow, 3));
        }
        panel.add(pretField);

        panel.add(new JLabel("Descriere:"));
        JTextField descriereField = new JTextField();
        if (selectedRow != -1) {
            descriereField.setText((String) table.getValueAt(selectedRow, 4));
        }
        panel.add(descriereField);

        panel.add(new JLabel("Stoc:"));
        JTextField stocField = new JTextField();
        if (selectedRow != -1) {
            stocField.setText((String) table.getValueAt(selectedRow, 5));
        }
        panel.add(stocField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                selectedRow == -1 ? "Introduceți informațiile" : "Editați informațiile",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            if (selectedRow == -1) {
                String brand = brandField.getText();
                String model = modelField.getText();
                double pret = Double.parseDouble(pretField.getText());
                String descriere = descriereField.getText();
                int stoc = Integer.parseInt(stocField.getText());

                // Adăugarea datelor în tabel
                ((DefaultTableModel) table.getModel()).addRow(new Object[]{brand, model, pret, descriere, stoc});
                // Adăugați în baza de date aici
                Produs productToAdd = new Produs(brand, model, pret, descriere, stoc, true);
                dbUtils.addProduct(productToAdd);
            } else {
                int idValue = Integer.parseInt(idField.getText());
                String brand = brandField.getText();
                String model = modelField.getText();
                double pret = Double.parseDouble(pretField.getText());
                String descriere = descriereField.getText();
                int stoc = Integer.parseInt(stocField.getText());

                // Actualizați datele în tabel
                table.setValueAt(brand, selectedRow, 1);
                table.setValueAt(model, selectedRow, 2);
                table.setValueAt(pret, selectedRow, 3);
                table.setValueAt(descriere, selectedRow, 4);
                table.setValueAt(stoc, selectedRow, 5);
                // Actualizați în baza de date aici
                Produs updatedProduct = new Produs(idValue, brand, model, pret, descriere, stoc, true);
                dbUtils.updateProduct(updatedProduct);
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
            int productId = Integer.parseInt((String) tableModel.getValueAt(selectedRow, 0));
            int dialogResult = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                ((DefaultTableModel) table.getModel()).removeRow(selectedRow);
                dbUtils.deleteProduct(productId);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
