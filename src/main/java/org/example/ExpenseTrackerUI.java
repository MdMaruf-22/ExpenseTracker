package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpenseTrackerUI extends JFrame {
    private ExpenseTracker expenseTracker;

    public ExpenseTrackerUI(String url, String userName, String passWord) {
        expenseTracker = new ExpenseTracker(url, userName, passWord);
        createUI();
    }

    private void createUI() {
        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 1));

        JLabel titleLabel = new JLabel("Expense Tracker");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel);

        JButton addExpenseButton = new JButton("Add Expense");
        addExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String amountStr = JOptionPane.showInputDialog("Enter expense amount:");
                double amount = Double.parseDouble(amountStr);
                String description = JOptionPane.showInputDialog("Enter expense description:");
                expenseTracker.addExpense(amount, description);
                JOptionPane.showMessageDialog(null, "Expense added successfully!");
            }
        });
        mainPanel.add(addExpenseButton);

        JButton viewExpensesButton = new JButton("View Expenses");
        viewExpensesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder expenses = new StringBuilder();
                for (String expense : expenseTracker.viewExpenses()) {
                    expenses.append(expense).append("\n");
                }
                JOptionPane.showMessageDialog(null, expenses.toString(), "Expenses", JOptionPane.PLAIN_MESSAGE);
            }
        });
        mainPanel.add(viewExpensesButton);

        JButton viewTotalExpensesButton = new JButton("View Total Expenses for Today");
        viewTotalExpensesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String today = dateFormat.format(new Date());
                double totalExpenses = expenseTracker.getTotalExpensesForDate(today);
                JOptionPane.showMessageDialog(null, "Total expenses for today: $" + totalExpenses, "Total Expenses", JOptionPane.PLAIN_MESSAGE);
            }
        });
        mainPanel.add(viewTotalExpensesButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        mainPanel.add(exitButton);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/expense_tracker";
        String userName = "root";
        String passWord = "";

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ExpenseTrackerUI(url, userName, passWord);
            }
        });
    }
}


