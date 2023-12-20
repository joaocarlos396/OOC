/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ooc;

/**
 *Represents login information containing user ID and account type.
 * @author joao
 */
public class LoginInfo {
    private int userId; // User ID associated with the login
    private  String accountType; // Type of the user account (e.g., ADMIN or REGULAR)

    /**
     * Constructor to initialize LoginInfo with user ID and account type.
     * userId      The unique identifier for the user.
     * accountType The type of the user account (ADMIN or REGULAR).
     */
    public LoginInfo(int userId, String accountType) {
        this.userId = userId;
        this.accountType = accountType;
    }

    // Gets the user ID associated with the login.
    //return The user ID.
    public int getUserId() {
        return userId;
    }
    //Gets the account type of the user.
    //return The account type

    public String getAccountType() {
        return accountType;
    }
}
