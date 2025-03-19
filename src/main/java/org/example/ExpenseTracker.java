package org.example;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    public List<String> viewExpenses() {
        List<String> expensesList = new ArrayList<>();
        String sql = "SELECT * FROM expenses";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String expense = "ID: " + rs.getInt("id") +
                        ", Amount: " + rs.getDouble("amount") +
                        ", Description: " + rs.getString("description") +
                        ", Date: " + rs.getString("date");
                expensesList.add(expense);
            }
        } catch (SQLException e) {
            System.out.println("Error viewing expenses.");
            e.printStackTrace();
        }
        return expensesList;
    }
    public Map<String, Double> getTotalExepnseOfToday(String date) {
        Map<String, Double> expensesAndDescriptions = new HashMap<>();
        String sql = "SELECT description, amount FROM expenses WHERE date = ?";
        double totalExpenses = 0.0;
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1, date);
            ResultSet rs = prst.executeQuery();
            while (rs.next()) {
                String description = rs.getString("description");
                double amount = rs.getDouble("amount");
                totalExpenses += amount;
                expensesAndDescriptions.put(description, amount);
            }
            expensesAndDescriptions.put("Total Expenses", totalExpenses);
        } catch (SQLException e) {
            System.out.println("Error retrieving expenses for date.");
            e.printStackTrace();
        }
        return expensesAndDescriptions;
    }

    private String getCurrentDate(){
        LocalDate currDate = LocalDate.now();
        return  currDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public void addMoney(double amount) {
        String sql = "INSERT INTO credit (amount,date) VALUES (?,?)";
        try{
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setDouble(1,amount);
            prst.setString(2,getCurrentDate());
            prst.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("Error in crediting.");
            e.printStackTrace();
        }
    }
}
