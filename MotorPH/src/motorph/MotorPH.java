
package motorph;

import java.io.*;
import java.util.Scanner;

public class MotorPH {
    public static void main(String[]args){
        
        String employeeDetails = "resources//EmployeeDetails.csv";
        String attendance = "resources//AttendanceRecord.csv";
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
            System.out.println("1. Enter your employee number");
            System.out.println("2. Exit the program");   
            System.out.print("Enter number: ");}
        else if(username.equals("payroll_staff")){
            System.out.println("\nChoose an option");
            System.out.println("1. Process Payroll");
            System.out.println("2. Exit the program");   
            System.out.print("Enter number: ");}
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
            e.printStackTrace();}
        if (!found){
            System.out.println("Employee Number not found.");}
            }
        }
        String option2="";
        if (username.equals("payroll_staff")&&option.equals("1")){
        while (true){
        System.out.println("\nChoose an option");
        System.out.println("1. One Employee");
        System.out.println("2. All Employees"); 
        System.out.println("3. Exit the program"); 
        System.out.print("Enter number: ");
        option2 = scanner.nextLine();
        if (option2.equals("1")){break;}
        else if (option2.equals("2")){break;}
        else if (option2.equals("3")){System.exit(0);}
        else{System.out.println("Invalid option");}
        } 
        }
        
        if (option2.equals("1")){System.out.println("1!!!");}
        else if(option2.equals("2")){System.out.println("2!!");}
        else if(option2.equals("3")){System.exit(0);}
        else {System.out.println("Invalid Input");}
        
        scanner.close();
    }
}



