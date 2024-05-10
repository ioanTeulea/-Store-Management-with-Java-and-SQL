import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.awt.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        if (dbUtils.isWorking() != 1) {
            System.out.println("Connection FAILED");
        }

        // Frame with menu
        JFrame frame = new JFrame("Shop");
        frame.setLayout(new FlowLayout()); // Folosește un layout manager
        frame.setLayout(null);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);


        // Create a menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create a menu
        JMenu menuP = new JMenu("Products");
        menuBar.add(menuP);

        //Add items
        JMenuItem menuItemShowProducts = new JMenuItem("Show Products");
        menuP.add(menuItemShowProducts);

        menuItemShowProducts.addActionListener(e -> {
            try {
                // Deschide fereastra cu tabelul de produse
                JFrame productFrame = new JFrame("Products List");
                productFrame.add(new ShowProdus());
                menuItemShowProducts.setHorizontalAlignment(JMenuItem.CENTER);
                productFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                productFrame.setSize(600, 300);
                productFrame.setLocationRelativeTo(frame);
                productFrame.setVisible(true);
            } catch (ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });
        JMenuItem menuItemAddProducts = new JMenuItem("Edit Products");
        menuP.add(menuItemAddProducts);
        menuItemAddProducts.addActionListener(e -> {
            try {
                // Deschide fereastra cu tabelul de produse
                JFrame productFrame = new JFrame("Products List");
                productFrame.add(new AddEditDeleteProdus());
                menuItemAddProducts.setHorizontalAlignment(JMenuItem.CENTER);
                productFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Închide fereastra curentă, nu întreaga aplicație
                productFrame.setSize(600, 300);
                productFrame.setLocationRelativeTo(frame);
                productFrame.setVisible(true);

            } catch (ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });

        // Create a menu
        JMenu menuC = new JMenu("Clients");
        menuBar.add(menuC);

        //Add items
        JMenuItem menuItemShowClients = new JMenuItem("Show Clients");
        // Centrarea menuItemShowClients în frame
        menuItemShowClients.setHorizontalAlignment(JMenuItem.CENTER);
        menuC.add(menuItemShowClients);
        menuItemShowClients.addActionListener(e -> {
            try {
                // Deschide fereastra cu tabelul de clienți
                JFrame clientFrame = new JFrame("Client List");
                clientFrame.add(new ShowClient());
                menuItemShowClients.setHorizontalAlignment(JMenuItem.CENTER);
                clientFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Închide fereastra curentă, nu întreaga aplicație
                clientFrame.setSize(600, 300);
                clientFrame.setLocationRelativeTo(frame);
                clientFrame.setVisible(true);
            } catch (ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });
        JMenuItem menuItemAddClient = new JMenuItem("Edit Clients");
        menuC.add(menuItemAddClient);
        menuItemAddClient.addActionListener(e -> {
            try {
                // Deschide fereastra cu tabelul de clienți
                JFrame clientFrame = new JFrame("Client List");
                clientFrame.add(new AddEditDeleteClient());
                menuItemAddClient.setHorizontalAlignment(JMenuItem.CENTER);
                clientFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Închide fereastra curentă, nu întreaga aplicație
                clientFrame.setSize(600, 300);
                clientFrame.setLocationRelativeTo(frame);
                clientFrame.setVisible(true);

            } catch (ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });


        // Create a menu
        JMenu menuO = new JMenu("Orders");
        menuBar.add(menuO);
        frame.setJMenuBar(menuBar);

        //Add items
        JMenuItem menuItemShowOrders = new JMenuItem("Show Orders");
        menuO.add(menuItemShowOrders);
        menuItemShowOrders.addActionListener(e -> {
            try {
                // Deschide fereastra cu tabelul de comenzi
                JFrame orderFrame = new JFrame("Orders List");
                orderFrame.add(new ShowComanda());
                menuItemShowOrders.setHorizontalAlignment(JMenuItem.CENTER);
                orderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                orderFrame.setSize(600, 300);
                orderFrame.setLocationRelativeTo(frame);
                orderFrame.setVisible(true);
            } catch (ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });
        JMenuItem menuItemAddOrders = new JMenuItem("Edit Orders");
        menuO.add(menuItemAddOrders);
        menuItemAddOrders.addActionListener(e -> {
            try {
                // Deschide fereastra cu adăugarea/completarea comenzii
                JFrame orderFrame = new JFrame("Edit Orders");
                orderFrame.add(new AddEditDeleteComanda());
                menuItemAddOrders.setHorizontalAlignment(JMenuItem.CENTER);
                orderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                orderFrame.setSize(600, 300);
                orderFrame.setLocationRelativeTo(frame);
                orderFrame.setVisible(true);
            } catch (ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });

        // Set the menu bar for the frame
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}