package org.example;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ExpenseTracker {
    private Connection conn;
    public ExpenseTracker(String url, String userName,String passWord){
        try{
            conn = DriverManager.getConnection(url, userName, passWord);
            System.out.println("Connected to the Database.");
        }
        catch (SQLException ex){
            System.out.println("Connection failed");
            ex.printStackTrace();
        }
    }
    public void addExpense(double amount, String description){
        String sql = "INSERT INTO expenses (amount,description,date) VALUES (?,?,?)";
        try{
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setDouble(1,amount);
            prst.setString(2,description);
            prst.setString(3,getCurrentDate());
            prst.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("Error adding expense.");
            e.printStackTrace();
        }
    }
    public void viewExpenses() {
        String sql = "SELECT * FROM expenses";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Amount: " + rs.getDouble("amount") +
                        ", Description: " + rs.getString("description") +
                        ", Date: " + rs.getString("date"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing expenses.");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/expense_tracker";
        String userName = "root";
        String passWord = "";
        ExpenseTracker et = new ExpenseTracker(url,userName,passWord);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter expense amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter expense description: ");
                    String description = scanner.nextLine();
                    et.addExpense(amount, description);
                    break;
                case 2:
                    et.viewExpenses();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    private String getCurrentDate(){
        LocalDate currDate = LocalDate.now();
        return  currDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
