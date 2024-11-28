import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Queue;
import java.util.TreeSet;

public class OrdersGUI extends JFrame {
    private JTable ordersTable;
    private DefaultTableModel ordersModel;
    private JButton backButton;
    private MenuGUI menuGUI;

    public OrdersGUI(MenuGUI menuGUI) {
        this.menuGUI = menuGUI;
        setTitle("Pending Orders");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] columns = {"Order ID", "Customer ID", "Status", "Total"};
        ordersModel = new DefaultTableModel(columns, 0);
        ordersTable = new JTable(ordersModel);
        JScrollPane scrollPane = new JScrollPane(ordersTable);

        JPanel buttonPanel = new JPanel();
        backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> switchToMenu());
        buttonPanel.add(backButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshOrders();
    }

    public void refreshOrders() {
        ordersModel.setRowCount(0);
        Queue<Order> orders1= fileHandler.readOrdersFromFile("orders.dat");
        if(orders1==null){
            System.out.println("No order history found");
            return;
        }
        for(Order o: orders1){
            Object[] row = {
                    o.getId(),
                    o.getCustomer().getID(),
                    o.getStatus(),
                    o.getTotal()
            };
            ordersModel.addRow(row);
        }
    }

    private void switchToMenu() {
        menuGUI.setVisible(true);
        this.dispose();
    }
}