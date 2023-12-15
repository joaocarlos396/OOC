/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ooc;

import java.util.Scanner;

/**
 *
 * @author joao
 */
public class OOC {

    /**
     * @param args the command line arguments
     */
private static String accountType = null;
    private static int userId = -1; // Assuming user ID is managed

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserManager userManager = new UserManager();

        while (true) {
            System.out.println("1. Register\n2. Login\n3. Admin Actions\n4. User Actions\n5. Exit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> userManager.registerUser();
                case 2 -> {
                    LoginInfo loginInfo = userManager.loginUser();
                    if (loginInfo != null) {
                        accountType = loginInfo.getAccountType();
                        userId = loginInfo.getUserId(); 
                    }
                }
                case 3 -> {
                    if ("ADMIN".equals(accountType)) {
                        handleAdminActions(userManager, scanner);
                    } else {
                        System.out.println("Access denied. Admin privileges are required.");
                    }
                }
                case 4 -> {
                if ("REGULAR".equals(accountType)) {
                        handleUserActions(userManager, scanner);
                    } else {
                        System.out.println("Login as a user.");
                    }
                }
                case 5 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleAdminActions(UserManager userManager, Scanner scanner) {
        System.out.println("Admin Actions:\n1. Update Profile\n2. View All Users\n3. Remove User\n4. Back");
        int adminChoice = scanner.nextInt();
        switch (adminChoice) {
            case 1 ->  userManager.updateUserProfile(userId);
            case 2 -> userManager.viewAllUserRecords();
            case 3 -> userManager.removeUser();
            case 4 -> {
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void handleUserActions(UserManager userManager, Scanner scanner) {
        System.out.println("User Actions:\n1. Update Profile\n2. Insert Gross Income & Tax Credit\n3. Solve Equation\n4. View Your details\n5. Back");
        int userChoice = scanner.nextInt();
        switch (userChoice) {
            case 1 -> userManager.updateUserProfile(userId);
            case 2 -> userManager.calculateAndStoreTax(userId);
            case 3 -> userManager.solveAndStoreEquation(userId);
            case 4 -> userManager.viewUserRecords(userId) ;
            case 5 -> {
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }
}