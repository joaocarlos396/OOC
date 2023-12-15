/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ooc;

/**
 *
 * @author joao
 */
public class LoginInfo {
    private int userId;
    private  String accountType;

    public LoginInfo(int userId, String accountType) {
        this.userId = userId;
        this.accountType = accountType;
    }

    
    public int getUserId() {
        return userId;
    }

    public String getAccountType() {
        return accountType;
    }
}
