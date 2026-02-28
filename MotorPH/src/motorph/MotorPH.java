
package motorph;

import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

public class MotorPH {
    public static void main(String[]args){
        
        String employeeDetails = "resources//EmployeeDetails.csv";
        String username ="";
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.print("Enter Username: ");
            username = scanner.nextLine();
            if (username.equals("employee")){break;}
            else if(username.equals("payroll_staff")){break;}
            else{System.out.println("Invalid Username");}
        }
        while (true){
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();
            if (password.equals("12345")){break;}
            else{System.out.println("Incorrect Password");}  
        }  
        String option ="";
        while (true){
        if (username.equals("employee")){
            System.out.println("\nChoose an option");
            System.out.print("1. Enter your employee number \n");
            System.out.print("2. Exit the program \n");   
            System.out.print("Enter number: ");}
        else if(username.equals("payroll_staff")){System.exit(0);}
        option = scanner.nextLine();
        if (option.equals("1")){break;}
        else if (option.equals("2")){System.exit(0);}
        else{System.out.println("Invalid option");}
        }
        
        
        if (username.equals("employee")&&option.equals("1")){
        while (true){
        System.out.print("Enter Employee Number: ");
        String empNumInput = scanner.nextLine();
        boolean found = false;
        try(BufferedReader br = new BufferedReader(new FileReader(employeeDetails))){
            br.readLine();
            String line;
            while ((line=br.readLine())!=null){
                String[] data = line.split(",");
                if (data[0].equals(empNumInput)){
                    found = true;
                    System.out.println("Employee Number: "+data[0]);
                    System.out.println("Name: "+data[2] +" "+data[1]);
                    System.out.println("Birthday: "+data[3]);
                    break;
                }
            }  
        }
        catch(IOException e){
            e.printStackTrace();
        }
        if (!found){
            System.out.println("Employee Number not found.");
        }
        
        }
        }
        scanner.close();
    }
}



