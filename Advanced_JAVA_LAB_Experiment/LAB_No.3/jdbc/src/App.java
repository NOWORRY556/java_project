import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {

    // Apne database ka name, username aur password yahan update karein
    private static final String URL = "jdbc:mysql://localhost:3306/college";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        System.out.println("Checking connection to MySQL database...\n");

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("✅ Connection Successful!\n");

            // 1. Check if 'student' table exists by selecting data from it
            String selectQuery = "SELECT * FROM student";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectQuery)) {

                System.out.println("--- Current Records in 'student' Table ---");
                boolean hasData = false;
                
                while (resultSet.next()) {
                    hasData = true;
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String branch = resultSet.getString("branch");

                    System.out.println("ID: " + id + " | Name: " + name + " | Branch: " + branch);
                }

                if (!hasData) {
                    System.out.println("Table is empty. Adding a test record...");
                    
                    // 2. Insert a test row to verify INSERT query works fine
                    String insertQuery = "INSERT INTO student (name, branch) VALUES (?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                        preparedStatement.setString(1, "Krishna Saraf");
                        preparedStatement.setString(2, "AI & Data Science");
                        
                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("✅ Test record inserted successfully!");
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Database Connection or Table Query Failed!");
            e.printStackTrace();
        }
    }
}