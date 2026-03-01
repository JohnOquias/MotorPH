
package motorph;

import java.io.*;
import java.util.Scanner;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.YearMonth;

public class MotorPH {
    public static void main(String[]args){
        
        String employeeDetails = "resources//EmployeeDetails.csv";
        String attendance = "resources//AttendanceRecord.csv";
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");
        String username ="";
        Scanner scanner = new Scanner(System.in);
        
        //Log in
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
        //end of login
       
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
        
        //Employee: Display Employee Details
        if (username.equals("employee")&&option.equals("1")){
            while (true){
        System.out.print("Enter Employee Number: ");
        String empNumInput = scanner.nextLine();
        if (empNumInput.equals("exit"))System.exit(0);
        boolean found = false;
        try(BufferedReader br = new BufferedReader(new FileReader(employeeDetails))){
            br.readLine();
            String line;
            while ((line=br.readLine())!=null){
                String[] data = line.split(",");
                if (data[0].equals(empNumInput)){
                    found = true;
                    System.out.println("Employee Number: "+data[0]);
                    System.out.println("Name: "+data[1] +", "+data[2]);
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
        
        
        //Payroll Staff: Display Process Payroll sub-options
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
        
        //Payroll Staff: Process payroll for one employee
        if (option2.equals("1")){
            String empNumber="";
            String lastName="";
            String firstName="";
            String birthday="";
            double hourlyRate=0;
            //Process payroll one employee: Display employee details
        while (true){
        System.out.print("Enter Employee Number: ");
        String empNumInput = scanner.nextLine();
        if (empNumInput.equals("exit")){System.exit(0);}
        boolean found = false;
        try(BufferedReader br = new BufferedReader(new FileReader(employeeDetails))){
            br.readLine();
            String line;
            while ((line=br.readLine())!=null){
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].replaceAll("^\"|\"$", "").trim();
                }                   
                if (data[0].equals(empNumInput)){
                    found = true;
                    empNumber = data[0];
                    lastName = data[1];
                    firstName = data[2];
                    birthday = data[3];
                    hourlyRate = Double.parseDouble(data[18]);
                    break;
                }
            } 
        }
        catch(IOException e){
            e.printStackTrace();}
        if (!found){
            System.out.println("Employee Number not found.");}
            
        System.out.println("Employee Number: "+empNumber);
        System.out.println("Name: "+lastName +", "+firstName);
        System.out.println("Birthday: "+birthday);      
        
            //Process payroll one employee: calculations
        for (int month=6;month <=12;month++){
            double hours1 =0;
            double hours2=0;
            double grossSalary1 =0;
            double grossSalary2 =0;
            double netSalary1 =0;
            double netSalary2 =0;
            
            
            int daysInMonth=YearMonth.of(2024, month).lengthOfMonth();
            try (BufferedReader br = new BufferedReader(new FileReader(attendance))){
                br.readLine();
                String line;
                while ((line=br.readLine())!=null){
                    
                    if(line.trim().isEmpty())continue;
                    String[] data = line.split(",");
                    if(!data[0].equals(empNumber))continue;
                    String[] dateParts = data[3].split("/");
                    int recordMonth = Integer.parseInt(dateParts[0]);
                    int day = Integer.parseInt(dateParts[1]);
                    int year = Integer.parseInt(dateParts[2]);
                    if(year != 2024 || recordMonth != month) continue;
                    LocalTime login = LocalTime.parse(data[4].trim(), timeFormat);
                    LocalTime logout = LocalTime.parse(data[5].trim(), timeFormat);
                    double hours = computeHours(login, logout);
                    if (day<=15) hours1 += hours;
                    else hours2 += hours;
                }
            }
            catch(Exception e){
                System.out.println("Error reading attendance file for month of "+month);
                e.printStackTrace();
                continue;
            }   
            String monthName = switch(month){
                case 6 -> "June";
                case 7 -> "July";
                case 8 -> "August";
                case 9 -> "September";
                case 10 -> "October";
                case 11 -> "November";
                case 12 -> "December";
                default -> "Month "+month;    
            };
            grossSalary1 = computeGrossSalary(hours1,hourlyRate);
            grossSalary2 = computeGrossSalary(hours2,hourlyRate);
            
            System.out.println("Hourly Rate: "+hourlyRate);
            System.out.println("\nCutoff Date: " +monthName + " 1 to 15");
            System.out.println("Total Hours Worked: "+ hours1);
            System.out.println("Gross Salary: "+ grossSalary1);
            System.out.println("Net Salary: "+ grossSalary1);
            
            System.out.println("\nCutoff Date: " +monthName + " 16 to "+daysInMonth);
            System.out.println("Total Hours Worked: "+ hours2);
            System.out.println("Gross Salary: "+ grossSalary2);
            System.out.println("Deductions: ");
            System.out.println("    SSS: ");
            System.out.println("    PhilHealth: ");
            System.out.println("    Pag-IBIG: ");
            System.out.println("    Tax: ");
            System.out.println("Net Salary: ");
            
        }
         //end of for loop
        
        }}//end of process one employee
        
        
        if(option2.equals("2")){System.out.println("2!!");}
        if(option2.equals("3")){System.exit(0);}
        
        
        scanner.close();
    }


static double computeHours(LocalTime login, LocalTime logout){
    LocalTime graceTime = LocalTime.of(8,10);
    LocalTime cutoffTime = LocalTime.of(17,0);
    
    if (logout.isAfter(cutoffTime)){
        logout=cutoffTime;
    }
    long minutesWorked = Duration.between(login, logout).toMinutes();
    if (minutesWorked > 60){
        minutesWorked -=60;
    }else{
        minutesWorked = 0;
    }
    double hours = minutesWorked / 60.0;
    if (!login.isAfter(graceTime)){
        return 8.0;
    }
    
    return Math.min(hours, 8.0);
}  
static double computeGrossSalary(double hours, double hourlyRate){
    return hours*hourlyRate;
}
}

