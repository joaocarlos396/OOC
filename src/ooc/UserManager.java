package ooc;

//import java.io.File;
//import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Stack;

/*
*   This method handles the registration process for a new user. It prompts the user for
 *   necessary information such as username, password, first name, and last name. It then constructs
 *   an SQL query to insert the user data into the "users" table in the database with the account type set to 'REGULAR'.
 *   The method uses a Scanner for user input and establishes a database connection to execute the SQL statement.
 *   Password hashing is recommended but not implemented in this code.
*/
public class UserManager {

    private Scanner scanner;
    //Constructor for UserManager class.
    // Initializes the Scanner for user input.

    public UserManager() {
        this.scanner = new Scanner(System.in);
    }
    //Method for registering a new user.
    // Collects user input and inserts the data into the "users" table in the database.

    public void registerUser() {
         // Collect user input for username, password, first name, and last name.
    System.out.println("Register New User");
    System.out.print("Enter username: ");
    String username = scanner.next();

    System.out.print("Enter password: ");
    String password = scanner.next(); 

    System.out.print("Enter first name: ");
    String firstName = scanner.next();

    System.out.print("Enter last name: ");
    String lastName = scanner.next();
    
    // SQL query for user insertion.
    String sql = "INSERT INTO users (username, password, first_name, last_name, account_type) VALUES (?, ?, ?, ?, 'REGULAR')";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        // Set parameter values for the prepared statement.
        pstmt.setString(1, username);
        pstmt.setString(2, password); // Remember to hash the password
        pstmt.setString(3, firstName);
        pstmt.setString(4, lastName);
        
        // Execute the SQL statement and get the number of affected rows.
        int affectedRows = pstmt.executeUpdate();
        // Display success or failure message based on affected rows.
        if (affectedRows > 0) {
            System.out.println("User registered successfully!");
        } else {
            System.out.println("User registration failed.");
        }
        // Handle SQL exceptions and print an error message.
    } catch (SQLException e) { 
        System.out.println("Error registering user: " + e.getMessage());
    }
}

/*
 * Class: UserManager
 * 
 * Method: loginUser()
 * Description:
 *   This method handles the user login process. It prompts the user for their username and password,
 *   constructs an SQL query to check if the credentials are valid in the "users" table, and performs
 *   appropriate actions based on the login result. The method uses a Scanner for user input and establishes
 *   a database connection to execute the SQL statement.
 */
    
    public LoginInfo loginUser() {
    System.out.println("Login");
    
    // Collect user input for username and password.
    System.out.print("Enter username: ");
    String username = scanner.next();

    System.out.print("Enter password: ");
    String password = scanner.next(); 
    // SQL query to check if the username and password are valid.
    String sql = "SELECT user_id, account_type FROM users WHERE username = ? AND password = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        // Set parameter values for the prepared statement.
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        // Execute the SQL statement and get the result set.
        ResultSet rs = pstmt.executeQuery();
        // Check if the login is successful.
        if (rs.next()) {
            // Print success message based on account type (admin or regular user).
            if(rs.getString("account_type").equals("ADMIN"))
                System.out.println("Login successful as an admin!");
            else
                System.out.println("Login successful as a regular user!");
            // Retrieve user ID and account type from the result set.
            String accountType=rs.getString("account_type");
           
            int userId=rs.getInt("user_id");
            // Create a LoginInfo object with user details and return it.
            LoginInfo loginInfo = new LoginInfo(userId, accountType);
            return loginInfo;
        } else {
            // Print error message if login fails and return null.
            System.out.println("Login failed. Invalid username or password.");
            return null;
        }
    } catch (SQLException e) {
        // Handle SQL exceptions and print an error message.
        System.out.println("Error logging in: " + e.getMessage());
        return null;
    }
}

   /*
    * This method handles the update of a user's profile. It prompts the user for new profile information,
    * constructs an SQL query to update the corresponding record in the "users" table, and executes the update
    * operation. The method uses a Scanner for user input and establishes a database connection to perform the update.
    */

    public void updateUserProfile(int userId) {
    System.out.println("Update User Profile");
    // Collect user input for new first name, last name, username, and password.
    System.out.print("Enter new first name: ");
    String firstName = scanner.next();

    System.out.print("Enter new last name: ");
    String lastName = scanner.next();
    
    System.out.print("Enter new  Username: ");
    String username = scanner.next();

    System.out.print("Enter new password: ");
    String password = scanner.next();
    // SQL query to update the user's profile in the "users" table.
    String sql = "UPDATE users SET first_name = ?, last_name = ?, username = ?, password = ? WHERE user_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        // Set parameter values for the prepared statement.
        pstmt.setString(1, firstName);
        pstmt.setString(2, lastName);
        pstmt.setString(3, username);
        pstmt.setString(4, password);
        pstmt.setInt(5, userId);
        
        // Execute the SQL statement and get the number of affected rows.
        int affectedRows = pstmt.executeUpdate();
        // Check if the update is successful and print the corresponding message.
        if (affectedRows > 0) {
            System.out.println("User profile updated successfully!");
        } else {
            System.out.println("User profile update failed.");
        }
    } catch (SQLException e) {
         // Handle SQL exceptions and print an error message.
        System.out.println("Error updating user profile: " + e.getMessage());
    }
}
   /*
    *   This method retrieves and displays information about all users from the "users" table in the database.
    *   It constructs an SQL query to select user details, establishes a database connection, executes the query,
    *   and prints the retrieved user information. The method uses a PreparedStatement for the SQL query and handles
    *   SQLExceptions to display an error message in case of an issue during the retrieval process.
    */

    public void viewAllUsers() {
    System.out.println("Viewing all users");
    // SQL query to select user details from the "users" table.
    String sql = "SELECT user_id, username, first_name, last_name, account_type FROM users";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
       // Execute the SQL statement and get the ResultSet containing user information.
        ResultSet rs = pstmt.executeQuery();
        
         // Iterate through the ResultSet to print user details.
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("user_id") + ", Username: " + rs.getString("username") +
                               ", Name: " + rs.getString("first_name") + " " + rs.getString("last_name") +
                               ", Type: " + rs.getString("account_type"));
        }
    } catch (SQLException e) {
        // Handle SQL exceptions and print an error message.
        System.out.println("Error retrieving users: " + e.getMessage());
    }
}

   /*
    *   This method allows the removal of a user from the system. It prompts the user to enter the ID of the user to be removed,
    *   performs several database operations in a transaction, including deleting related records from the "equations" and "tax_records"
    *   tables, and finally removes the user from the "users" table. The method uses PreparedStatement for each SQL operation,
    *   handles SQLExceptions, and ensures transactional integrity by using commit and rollback.
    */
    
    public void removeUser() {
    System.out.println("Remove a User");
    System.out.print("Enter user ID to remove: ");
    
    int userId = scanner.nextInt();  // assuming 'scanner' is already defined in the class
    // Check if the specified user ID is the admin's ID; if true, display a message that the admin cannot be deleted.
    if(userId==1)
    {
        System.out.println("The admin cannot be deleted.");
        return;
    }
    // SQL statements for deleting records related to the user from the "equations" and "tax_records" tables.
    String deleteEquationsSql = "DELETE FROM equations WHERE user_id = ?";
    String deleteTaxRecordsSql = "DELETE FROM tax_records WHERE user_id = ?";
     // SQL statement for removing the user from the "users" table.
    String deleteUserSql = "DELETE FROM users WHERE user_id = ?";

    Connection conn = null;
    // Establish a database connection and start a transaction.
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

       /*
        *   This method allows the calculation and storage of tax information for a user. It prompts the user to enter gross income
        *   and tax credits, calculates the tax owed using a helper method, and then checks if a tax record already exists for the user.
        *   If a record exists, it updates the existing record; otherwise, it inserts a new record into the "tax_records" table.
        *   The method utilizes PreparedStatements for SQL operations, handles SQLExceptions, and provides appropriate feedback messages. 
        */ 
    
   public void calculateAndStoreTax(int userId) {
    System.out.println("Calculate and Store Tax");
     // Prompt the user to enter gross income and tax credits.
    System.out.print("Enter gross income: ");
    double grossIncome = scanner.nextDouble();

    System.out.print("Enter tax credits: ");
    double taxCredits = scanner.nextDouble();
     // Calculate the tax owed using a helper method.
    double taxOwed = calculateTaxOwed(grossIncome, taxCredits);

     // SQL statements for checking, updating, and inserting tax records in the "tax_records" table.
    String checkSql = "SELECT record_id FROM tax_records WHERE user_id = ?";
    String updateSql = "UPDATE tax_records SET gross_income = ?, tax_credits = ?, tax_owed = ? WHERE user_id = ?";
    String insertSql = "INSERT INTO tax_records (user_id, gross_income, tax_credits, tax_owed) VALUES (?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection(); // Establish a database connection.
         PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

        checkStmt.setInt(1, userId);
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next()) {
            // Record exists, so update
            // Execute the check SQL statement to see if a tax record already exists for the user.
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

  /*
   *   This method allows the solving and storage of an equation involving gross income (x) and tax credits (y).
   *   It retrieves the user's gross income and tax credits from the "tax_records" table, uses the obtained values in the equation,
   *   and then stores the result in the "equations" table. The method utilizes a SQL SELECT statement to retrieve data,
   *   PreparedStatements for SQL operations, handles SQLExceptions, and provides appropriate feedback messages. 
   */

    public void solveAndStoreEquation(int userId) {
    System.out.println("Solve and Store Equation (x for gross_income and y for tax_credits) ");
    //System.out.print("Enter user ID: ");
   // int userId = scanner.nextInt();
   
   double x = 0,y = 0; // Variables to store the user's gross income (x) and tax credits (y).
   // SQL SELECT statement to retrieve gross income and tax credits for the given user.
      String sql1 = "SELECT gross_income, tax_credits FROM tax_records WHERE user_id = ?";
      

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt1 = conn.prepareStatement(sql1)) {

        pstmt1.setInt(1, userId );
      
        // Execute the SQL SELECT statement.
        ResultSet rs1 = pstmt1.executeQuery();

        if (rs1.next()) {
             // Retrieve gross income and tax credits values from the ResultSet.
            x=rs1.getDouble("gross_income");
            y=rs1.getDouble("tax_credits");
            // Use the obtained values in the equation.
            //System.out.println(y);

        } else {
            System.out.println("Failed to read."); // Display an error message if the retrieval fails
            //return null;
        }
    } catch (SQLException e) {
        // Handle SQLExceptions and display appropriate error messages.
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