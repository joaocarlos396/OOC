package ooc;

//import java.io.File;
//import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Stack;

public class UserManager {

    private Scanner scanner;

    public UserManager() {
        this.scanner = new Scanner(System.in);
    }

    public void registerUser() {
    System.out.println("Register New User");
    System.out.print("Enter username: ");
    String username = scanner.next();

    System.out.print("Enter password: ");
    String password = scanner.next(); 

    System.out.print("Enter first name: ");
    String firstName = scanner.next();

    System.out.print("Enter last name: ");
    String lastName = scanner.next();

    String sql = "INSERT INTO users (username, password, first_name, last_name, account_type) VALUES (?, ?, ?, ?, 'REGULAR')";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, username);
        pstmt.setString(2, password); // Remember to hash the password
        pstmt.setString(3, firstName);
        pstmt.setString(4, lastName);

        int affectedRows = pstmt.executeUpdate();

        if (affectedRows > 0) {
            System.out.println("User registered successfully!");
        } else {
            System.out.println("User registration failed.");
        }
    } catch (SQLException e) {
        System.out.println("Error registering user: " + e.getMessage());
    }
}


    public LoginInfo loginUser() {
    System.out.println("Login");
    System.out.print("Enter username: ");
    String username = scanner.next();

    System.out.print("Enter password: ");
    String password = scanner.next(); 

    String sql = "SELECT user_id, account_type FROM users WHERE username = ? AND password = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, username);
        pstmt.setString(2, password);

        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            if(rs.getString("account_type").equals("ADMIN"))
                System.out.println("Login successful as an admin!");
            else
                System.out.println("Login successful as a regular user!");
            String accountType=rs.getString("account_type");
           
            int userId=rs.getInt("user_id");
            LoginInfo loginInfo = new LoginInfo(userId, accountType);
            return loginInfo;
        } else {
            System.out.println("Login failed. Invalid username or password.");
            return null;
        }
    } catch (SQLException e) {
        System.out.println("Error logging in: " + e.getMessage());
        return null;
    }
}






   

    public void updateUserProfile(int userId) {
    System.out.println("Update User Profile");
    System.out.print("Enter new first name: ");
    String firstName = scanner.next();

    System.out.print("Enter new last name: ");
    String lastName = scanner.next();
    
    System.out.print("Enter new  Username: ");
    String username = scanner.next();

    System.out.print("Enter new password: ");
    String password = scanner.next();

    String sql = "UPDATE users SET first_name = ?, last_name = ?, username = ?, password = ? WHERE user_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, firstName);
        pstmt.setString(2, lastName);
        pstmt.setString(3, username);
        pstmt.setString(4, password);
        pstmt.setInt(5, userId);

        int affectedRows = pstmt.executeUpdate();

        if (affectedRows > 0) {
            System.out.println("User profile updated successfully!");
        } else {
            System.out.println("User profile update failed.");
        }
    } catch (SQLException e) {
        System.out.println("Error updating user profile: " + e.getMessage());
    }
}


    public void viewAllUsers() {
    System.out.println("Viewing all users");
    String sql = "SELECT user_id, username, first_name, last_name, account_type FROM users";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("user_id") + ", Username: " + rs.getString("username") +
                               ", Name: " + rs.getString("first_name") + " " + rs.getString("last_name") +
                               ", Type: " + rs.getString("account_type"));
        }
    } catch (SQLException e) {
        System.out.println("Error retrieving users: " + e.getMessage());
    }
}


    public void removeUser() {
    System.out.println("Remove a User");
    System.out.print("Enter user ID to remove: ");
    
    int userId = scanner.nextInt();  // assuming 'scanner' is already defined in the class
    if(userId==1)
    {
        System.out.println("The admin cannot be deleted.");
        return;
    }
    String deleteEquationsSql = "DELETE FROM equations WHERE user_id = ?";
    String deleteTaxRecordsSql = "DELETE FROM tax_records WHERE user_id = ?";
    String deleteUserSql = "DELETE FROM users WHERE user_id = ?";

    Connection conn = null;
    try {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false); // Start transaction

        try (PreparedStatement deleteEquationsStmt = conn.prepareStatement(deleteEquationsSql);
             PreparedStatement deleteTaxRecordsStmt = conn.prepareStatement(deleteTaxRecordsSql);
             PreparedStatement deleteUserStmt = conn.prepareStatement(deleteUserSql)) {

            // Delete from equations
            deleteEquationsStmt.setInt(1, userId);
            deleteEquationsStmt.executeUpdate();

            // Delete from tax_records
            deleteTaxRecordsStmt.setInt(1, userId);
            deleteTaxRecordsStmt.executeUpdate();

            // Delete the user
           
            deleteUserStmt.setInt(1, userId);
            int affectedRows = deleteUserStmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User and related records removed successfully.");
                conn.commit(); // Commit transaction
            } else {
                System.out.println("User removal failed. User ID may not exist.");
                conn.rollback(); // Rollback transaction in case of failure
            }
        }
    } catch (SQLException e) {
        System.out.println("Error in database operation: " + e.getMessage());
        if (conn != null) {
            try {
                conn.rollback(); // Rollback transaction in case of exception
            } catch (SQLException ex) {
                System.out.println("Error during rollback: " + ex.getMessage());
            }
        }
    } finally {
        if (conn != null) {
            try {
                conn.setAutoCommit(true); // Reset auto-commit to true
                conn.close(); // Close the connection
            } catch (SQLException e) {
                System.out.println("Error resetting auto-commit or closing connection: " + e.getMessage());
            }
        }
    }
}


   public void calculateAndStoreTax(int userId) {
    System.out.println("Calculate and Store Tax");

    System.out.print("Enter gross income: ");
    double grossIncome = scanner.nextDouble();

    System.out.print("Enter tax credits: ");
    double taxCredits = scanner.nextDouble();

    double taxOwed = calculateTaxOwed(grossIncome, taxCredits);


    String checkSql = "SELECT record_id FROM tax_records WHERE user_id = ?";
    String updateSql = "UPDATE tax_records SET gross_income = ?, tax_credits = ?, tax_owed = ? WHERE user_id = ?";
    String insertSql = "INSERT INTO tax_records (user_id, gross_income, tax_credits, tax_owed) VALUES (?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

        checkStmt.setInt(1, userId);
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next()) {
            // Record exists, so update
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setDouble(1, grossIncome);
                updateStmt.setDouble(2, taxCredits);
                updateStmt.setDouble(3, taxOwed);
                updateStmt.setInt(4, userId);
                updateStmt.executeUpdate();
                System.out.println("Tax record updated successfully!");
            }
        } else {
            // Record does not exist, so insert
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, userId);
                insertStmt.setDouble(2, grossIncome);
                insertStmt.setDouble(3, taxCredits);
                insertStmt.setDouble(4, taxOwed);
                insertStmt.executeUpdate();
                System.out.println("New tax record inserted successfully!");
            }
        }
    } catch (SQLException e) {
        System.out.println("Error storing tax record: " + e.getMessage());
    }
}



    public void solveAndStoreEquation(int userId) {
    System.out.println("Solve and Store Equation (x for gross_income and y for tax_credits) ");
    //System.out.print("Enter user ID: ");
   // int userId = scanner.nextInt();
   
   double x = 0,y = 0;
      String sql1 = "SELECT gross_income, tax_credits FROM tax_records WHERE user_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt1 = conn.prepareStatement(sql1)) {

        pstmt1.setInt(1, userId );
      

        ResultSet rs1 = pstmt1.executeQuery();

        if (rs1.next()) {
            
            x=rs1.getDouble("gross_income");
            y=rs1.getDouble("tax_credits");
            //System.out.println(y);

        } else {
            System.out.println("Failed to read.");
            //return null;
        }
    } catch (SQLException e) {
        System.out.println("Could not connect the database");
        //return null;
    }
   
   
   
   
   
    System.out.print("Enter equation: ");
    String equation = scanner.next();
    String equation1 = equation.replace("x", String.valueOf(x));
    equation1 = equation1.replace("y", String.valueOf(y));
    
    
    
    
    
    double result = evaluate(equation1);

   String checkSql = "SELECT COUNT(*) FROM equations WHERE user_id = ?";

String insertSql = "INSERT INTO equations (user_id, equation, solution) VALUES (?, ?, ?)";

String updateSql = "UPDATE equations SET equation = ?, solution = ? WHERE user_id = ?";

try (Connection conn = DatabaseConnection.getConnection();
     PreparedStatement checkStmt = conn.prepareStatement(checkSql);
     PreparedStatement insertStmt = conn.prepareStatement(insertSql);
     PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

    // Check if the user_id exists
    checkStmt.setInt(1, userId);
    ResultSet rs = checkStmt.executeQuery();
    rs.next();
    int count = rs.getInt(1);

    if (count > 0) {
        // If user_id exists, update the record
        updateStmt.setString(1, equation);
        updateStmt.setDouble(2, result);
        updateStmt.setInt(3, userId);
        updateStmt.executeUpdate();
        System.out.println("Equation updated successfully!");
    } else {
        // If user_id does not exist, insert a new record
        insertStmt.setInt(1, userId);
        insertStmt.setString(2, equation);
        insertStmt.setDouble(3, result);
        insertStmt.executeUpdate();
        System.out.println("Equation solved and stored successfully!");
    }
} catch (SQLException e) {
    System.out.println("Error in SQL operation: " + e.getMessage());
}


 String checkSqlA = "SELECT COUNT(*) FROM tax_records WHERE user_id = ?";



String updateSqlA = "UPDATE tax_records SET tax_owed = ? WHERE user_id = ?";

try (Connection conn = DatabaseConnection.getConnection();
     PreparedStatement checkStmtA = conn.prepareStatement(checkSqlA);
    
     PreparedStatement updateStmtA= conn.prepareStatement(updateSqlA)) {

    // Check if the user_id exists
    checkStmtA.setInt(1, userId);
    ResultSet rsA = checkStmtA.executeQuery();
    rsA.next();
    

    
        // If user_id exists, update the record
        updateStmtA.setDouble(1, result);
        updateStmtA.setInt(2, userId); 
        updateStmtA.executeUpdate();
       
    
} catch (SQLException e) {
    System.out.println("Error in SQL operation: " + e.getMessage());
}





}


    private double calculateTaxOwed(double grossIncome, double taxCredits) {
    double taxRate = 0.2; // Example tax rate of 20%
    double taxOwed = grossIncome * taxRate - taxCredits;
    return Math.max(taxOwed, 0); // Ensure tax owed is not negative
}


    
    private String solveEquation(String equation) {
    // This will only work for simple linear equations of the form "ax + b = 0"
    // Extract 'a' and 'b' from the equation
    try {
        String[] parts = equation.split("x");
        double a = Double.parseDouble(parts[0].trim());
        double b = Double.parseDouble(parts[1].replace("=", "").trim());

        if (a == 0) {
            return "No solution (a cannot be 0)";
        }

        double solution = -b / a;
        return "x = " + solution;
    } catch (NumberFormatException e) {
        return "Error: Unable to solve equation. " + e.getMessage();
    }
}


 

public void viewAllUserRecords() {
    System.out.println("Viewing Records for All Users");

    // SQL queries
    String allUsersSql = "SELECT user_id, username, first_name, last_name, account_type FROM users";
    String taxRecordsSql = "SELECT * FROM tax_records WHERE user_id = ?";
    String equationsSql = "SELECT * FROM equations WHERE user_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement allUsersStmt = conn.prepareStatement(allUsersSql);
         PreparedStatement taxStmt = conn.prepareStatement(taxRecordsSql);
         PreparedStatement eqStmt = conn.prepareStatement(equationsSql)) {

        ResultSet usersRs = allUsersStmt.executeQuery();
        while (usersRs.next()) {
            int userId = usersRs.getInt("user_id");
            System.out.println("\nUser ID: " + userId + ", Username: " + usersRs.getString("username") +
                               ", Name: " + usersRs.getString("first_name") + " " + usersRs.getString("last_name") +
                               ", Account Type: " + usersRs.getString("account_type"));

            // Fetch and display tax records for each user
            taxStmt.setInt(1, userId);
            ResultSet taxRs = taxStmt.executeQuery();
            System.out.println("Tax Records:");
            while (taxRs.next()) {
                System.out.println("Gross Income: " + taxRs.getDouble("gross_income") +
                                   ", Tax Credits: " + taxRs.getDouble("tax_credits") +
                                   ", Tax Owed: " + taxRs.getDouble("tax_owed"));
            }

            // Fetch and display equations for each user
            eqStmt.setInt(1, userId);
            ResultSet eqRs = eqStmt.executeQuery();
            System.out.println("Equations:");
            while (eqRs.next()) {
                System.out.println("Equation: " + eqRs.getString("equation") +
                                   ", Solution: " + eqRs.getDouble("solution"));
            }
        }

    } catch (SQLException e) {
        System.out.println("Error retrieving records: " + e.getMessage());
    }
}
    
    
    
    
    
 public void viewUserRecords(int userId) {
    System.out.println("Viewing Records for User ID: " + userId);

    // SQL queries
    String userDetailsSql = "SELECT * FROM users WHERE user_id = ?";
    String taxRecordsSql = "SELECT * FROM tax_records WHERE user_id = ?";
    String equationsSql = "SELECT * FROM equations WHERE user_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement userStmt = conn.prepareStatement(userDetailsSql);
         PreparedStatement taxStmt = conn.prepareStatement(taxRecordsSql);
         PreparedStatement eqStmt = conn.prepareStatement(equationsSql)) {

        // Fetch and display user details
        userStmt.setInt(1, userId);
        ResultSet userRs = userStmt.executeQuery();
        if (userRs.next()) {
            System.out.println("User Details:");
            System.out.println("Username: " + userRs.getString("username") +
                               ", Name: " + userRs.getString("first_name") + " " + userRs.getString("last_name") +
                               ", Account Type: " + userRs.getString("account_type"));
        }

        // Fetch and display tax records
        taxStmt.setInt(1, userId);
        ResultSet taxRs = taxStmt.executeQuery();
        System.out.println("Tax Records:");
        while (taxRs.next()) {
            System.out.println("Gross Income: " + taxRs.getDouble("gross_income") +
                               ", Tax Credits: " + taxRs.getDouble("tax_credits") +
                               ", Tax Owed: " + taxRs.getDouble("tax_owed"));
        }

        // Fetch and display equations
        eqStmt.setInt(1, userId);
        ResultSet eqRs = eqStmt.executeQuery();
        System.out.println("Equations:");
        while (eqRs.next()) {
            System.out.println("Equation: " + eqRs.getString("equation") +
                               ", Solution: " + eqRs.getDouble("solution"));
        }

    } catch (SQLException e) {
        System.out.println("Error retrieving records: " + e.getMessage());
    }
}
   
    
 

 public static double evaluate(String expression) {
        char[] tokens = expression.toCharArray();
        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ') {
                continue;
            }

            if ((tokens[i] >= '0' && tokens[i] <= '9') || tokens[i] == '.') {
                StringBuilder sbuf = new StringBuilder();
                // Check for digits and decimal points
                while (i < tokens.length && ((tokens[i] >= '0' && tokens[i] <= '9') || tokens[i] == '.')) {
                    sbuf.append(tokens[i++]);
                }
                values.push(Double.parseDouble(sbuf.toString()));
                i--;
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/' || tokens[i] == '^') {
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(tokens[i]);
            }
        }

        while (!ops.empty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    public static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        return !(op1 == '^' && (op2 == '+' || op2 == '-' || op2 == '*' || op2 == '/'));
    }

    public static double applyOp(char op, double b, double a) {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) throw new UnsupportedOperationException("Cannot divide by zero");
                yield a / b;
            }
            case '^' -> Math.pow(a, b);
            default -> 0;
        };
    }
}