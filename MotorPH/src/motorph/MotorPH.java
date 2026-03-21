package motorph;

import java.io.*;
import java.util.Scanner;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.YearMonth;
import java.util.ArrayList;

public class MotorPH {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();//username input
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();//password input

        if (!username.equals("employee") && !username.equals("payroll_staff")){
            System.out.println("Incorrect username and/or password.");
            return;
        }if (!password.equals("12345")){
            System.out.println("Incorrect username and/or password.");
            return;
        }

        ArrayList<String> empNumberList = new ArrayList<>();//Employee Number storage
        ArrayList<String[]> empDetailsTable = new ArrayList<>();// employee details storage
        loadEmpDetails(empDetailsTable,empNumberList);//read employee details and store to the ArrayLists above

        if (username.equals("employee")){
            runEmployeeMenu(scanner, empDetailsTable);// call method for employee menu 
        }

        else if (username.equals("payroll_staff")){
            ArrayList<String[]> attendanceTable = new ArrayList<>();// attendance records storage
            ArrayList<String[]> sssTable = new ArrayList<>();// sss table storage
            loadAttendance(attendanceTable);//read attendance recods and store to the ArrayLists above
            loadSSSTable(sssTable);//read SSS table and store to the ArrayLists above
            runPayrollStaffMenu(scanner, empNumberList, empDetailsTable, attendanceTable, sssTable);//call method for payroll staff menu 
        }

        scanner.close();
    }

    static void runEmployeeMenu(Scanner scanner, ArrayList<String[]> empDetailsTable){
        System.out.println("\nChoose an option");//display employee menu 
        System.out.println("1. Enter your employee number");
        System.out.println("2. Exit the program");   
        System.out.print("Enter option: ");
        String employeeOption = scanner.nextLine();//employee: menu option input
        if (employeeOption.equals("1")){
            System.out.print("Enter Employee Number: ");
            String empNumber = scanner.nextLine();//employee: employee number input
            displayEmpDetails(empNumber, empDetailsTable);//call method for displaying employee details 
        }else if (employeeOption.equals("2"))return;
        else{System.out.println("Invalid option");
        }
    }
    
    static void runPayrollStaffMenu(Scanner scanner, ArrayList<String> empNumberList, ArrayList<String[]> empDetailsTable, ArrayList<String[]> attendanceTable, ArrayList<String[]> sssTable){
        System.out.println("\nChoose an option");//display payroll staff menu 
        System.out.println("1. Process Payroll");
        System.out.println("2. Exit the program");   
        System.out.print("Enter option: ");
        String payrollStaffOption1 = scanner.nextLine();//payroll staff: menu option input
        if (payrollStaffOption1.equals("1")){
            System.out.println("\nChoose an option");//display payroll staff sub-menu
            System.out.println("1. One Employee");
            System.out.println("2. All Employees"); 
            System.out.println("3. Exit the program"); 
            System.out.print("Enter option: ");
            String payrollStaffOption2 = scanner.nextLine();//payroll staff: sub-menu option input
            if(payrollStaffOption2.equals("1")){
                System.out.print("Enter Employee Number: ");
                String empNumber = scanner.nextLine();//payroll staff: employee number input
                processPayroll(empNumber, empDetailsTable, attendanceTable, sssTable);//call method for processing payroll
            }else if(payrollStaffOption2.equals("2")){
                System.out.println("-".repeat(100));//visual separator
                for (String empNumber:empNumberList){//looping through every employee
                    processPayroll(empNumber, empDetailsTable, attendanceTable, sssTable);
                }    
                System.out.println("-".repeat(100));
            }
            else if(payrollStaffOption2.equals("3")){return;}
            else {System.out.println("Invalid option");
            }
        }else if (payrollStaffOption1.equals("2"))return;
        else{System.out.println("Invalid option");
        }
    }
    
    //employee: display employee details
    static void displayEmpDetails(String empNumber, ArrayList<String[]> empDetailsTable ){
        boolean found = false;
        for (String[] empDetailsRow: empDetailsTable){
            if (!empNumber.equals(empDetailsRow[0]))continue;
                System.out.println("-".repeat(100));
                System.out.println("Employee Number: "+empDetailsRow[0]);
                System.out.println("Name: "+ empDetailsRow[1] + "," + empDetailsRow[2]);
                System.out.println("Birthday: "+empDetailsRow[3]);
                System.out.println("-".repeat(100));
                found = true;
                break;
        }if (!found){System.out.println("Employee Number does not exist.");
        }
    }
    
    //payroll staff: process payroll
    static void processPayroll(String empNumber, ArrayList<String[]> empDetailsTable, ArrayList<String[]> attendanceTable, ArrayList<String[]> sssTable){
        String lastName ="";
        String firstName ="";
        String birthday ="";
        double hourlyRate =0;
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");
        
        //search for employee number match
        boolean found = false;
        for (String[] empDetailsRow: empDetailsTable){
            if (!empNumber.equals(empDetailsRow[0]))continue;
            found = true;
            empNumber = empDetailsRow[0];
            lastName = empDetailsRow[1];
            firstName = empDetailsRow[2];
            birthday = empDetailsRow[3];
            try{    
                hourlyRate = Double.parseDouble(empDetailsRow[18]);
            }catch (NumberFormatException e) {//If there is a number format error for hourly rate, display error message, close method
                System.err.println("Number Format Error: Invalid hourly rate number format for Employee " + empDetailsRow[0]);
                return;
            }   
                break;
        }if (!found){System.out.println("Employee Number does not exist.");return;
        }
        
        //display employee details
        System.out.println("-".repeat(100));
        System.out.println("Employee Number: "+empNumber);
        System.out.println("Name: "+ lastName + ", " + firstName);
        System.out.println("Birthday: "+birthday);
        System.out.println("-".repeat(100));
        
        //looping through each month (June - December)
        for (int month=6;month <=12;month++){
            double[] monthlyHours = computeHoursMonthly(attendanceTable, empNumber, timeFormat, month);//A double array is returned by the method that computes for monthly hours
            double hours1 = monthlyHours[0];//number of hours worked for the first half of the month
            double hours2 = monthlyHours[1];//number of hours worked for the second half of the month
            double grossSalary1 = computeGrossSalary(hours1, hourlyRate);
            double netSalary1 = grossSalary1;//no deductions for the first half of the month
            double grossSalary2 = computeGrossSalary(hours2, hourlyRate);
            double grossSalaryTotal = grossSalary1 + grossSalary2;
            double sss = computeSSS(grossSalaryTotal, sssTable);
            double philHealth = computePhilHealth(grossSalaryTotal);
            double pagIBIG = computePagIBIG(grossSalaryTotal);
            double deductionsTotal = sss + philHealth + pagIBIG;
            double taxableIncome = grossSalaryTotal - deductionsTotal;
            double tax = computeTax(taxableIncome);
            double netSalary2 = grossSalary2 - deductionsTotal - tax;
            String monthName = getMonthName(month);//converts the number of the month to the name of the month
            int daysInMonth=YearMonth.of(2024, month).lengthOfMonth();//fetches the number of days for the given month and year
            
            //Payroll Display for first half of the month
            System.out.println("Cutoff Date: " +monthName + " 1 to 15");
            System.out.println("Total Hours Worked: " +hours1);
            System.out.println("Gross Salary: "+grossSalary1);
            System.out.println("Net Salary: "+netSalary1);

            //Payroll Display for second half of the month
            System.out.println("\nCutoff Date: " +monthName + " 16 to "+daysInMonth);
            System.out.println("Total Hours Worked: " +hours2);
            System.out.println("Gross Salary: "+grossSalary2);
            System.out.println("Deductions: ");
            System.out.println("    SSS: "+sss);
            System.out.println("    PhilHealth: "+philHealth);
            System.out.println("    Pag-IBIG: " + pagIBIG);
            System.out.println("    Tax: "+tax);
            System.out.println("Net Salary: "+netSalary2);
            System.out.println("-".repeat(100));

        }
    } 

    //read from employee details CSV file and store inside ArrayLists
    static void loadEmpDetails(ArrayList<String[]> empDetailsTable,ArrayList<String> empNumberList ){
        String empDetailsFilePath = "resources//EmployeeDetails.csv"; 
        try (BufferedReader br = new BufferedReader(new FileReader(empDetailsFilePath))){
            br.readLine();
            String line;
            while ((line=br.readLine())!=null){
                if(line.trim().isEmpty())continue;
                String[] empDetailsRow = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); //split by comma, ignore commas inside double-quoted text
                for (int i = 0; i < empDetailsRow.length; i++) {
                    empDetailsRow[i] = empDetailsRow[i].replaceAll("^\"|\"$", "").trim();//clean each column by removing surrounding quotes and extra spaces
                }    
                empDetailsTable.add(empDetailsRow);
                empNumberList.add(empDetailsRow[0]);
            }
        }catch (FileNotFoundException e) {
            System.err.println("Failed to load Employee Details. File not found at " + empDetailsFilePath);
            System.exit(1);
        }catch (IOException e) {
            System.err.println("SYSTEM ERROR: Could not access the file.");
            System.exit(1);
        }catch (Exception e) {
            System.err.println("GENERAL ERROR in loadEmpDetails " + e.getMessage());
            System.exit(1);
        }
    }

    //read attendance records CSV file and store inside an ArrayList
    static void loadAttendance(ArrayList<String[]> attendanceTable){
        String attendanceFilePath = "resources//AttendanceRecord.csv"; 
        try (BufferedReader br = new BufferedReader(new FileReader(attendanceFilePath))){
            br.readLine();
            String line;
            while ((line=br.readLine())!=null){
                if(line.trim().isEmpty())continue;
                String[] attendanceRow = line.split(",");
                attendanceTable.add(attendanceRow);
            }
        }catch (FileNotFoundException e) {
            System.err.println("Failed to load attendance records. Attendance file not found at " + attendanceFilePath);
            System.exit(1); 
        }catch (IOException e) {
            System.err.println("SYSTEM ERROR: Could not read attendance records." + e.getMessage());
            System.exit(1);
        }catch (Exception e) {
            System.err.println("GENERAL ERROR in loadAttendance: " + e.getMessage());
            System.exit(1);
        }
    }

    //read SSS table CSV file and store inside an ArrayList
    static void loadSSSTable(ArrayList<String[]> sssTable){
        String sssTableFilePath = "resources//SSSContribution.csv";
        try(BufferedReader br = new BufferedReader(new FileReader(sssTableFilePath))){
            br.readLine();
            br.readLine();
            String line;
            while ((line=br.readLine())!=null){
                String[] sssData = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");//split by comma, ignore commas inside double-quoted text
                for (int i = 0; i < sssData.length; i++) {
                    sssData[i] = sssData[i].replaceAll("^\"|\"$", "").trim().replace(",", ""); //clean each column by removing surrounding quotes and extra spaces
                }  
                sssTable.add(sssData);
            } 
        }catch (FileNotFoundException e) {
            System.err.println("Failed to load SSS Table. File not found at " + sssTableFilePath);
            System.exit(1); 
        }catch (IOException e) {
            System.err.println("SYSTEM ERROR: Could not read SSS Table." + e.getMessage());
            System.exit(1);
        }catch (Exception e) {
            System.err.println("GENERAL ERROR in loadSSSTable: " + e.getMessage());
            System.exit(1);
        }
    }

    static String getMonthName (int month){
        if (month == 6)  return "June";
        if (month == 7)  return "July";
        if (month == 8)  return "August";
        if (month == 9)  return "September";
        if (month == 10) return "October";
        if (month == 11) return "November";
        if (month == 12) return "December";
        return "Month "+month;    
    }
    
    //compute monthly hours based on the attendance records
    static double[] computeHoursMonthly(ArrayList<String[]> attendanceTable, String empNumber, DateTimeFormatter timeFormat, int month){
        double hours1=0;
        double hours2=0;
        for (String[] attendanceRow: attendanceTable){
            if(!attendanceRow[0].equals(empNumber))continue;
            String[] dateParts = attendanceRow[3].split("/");
            int recordMonth = Integer.parseInt(dateParts[0]);
            int day = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);
            if(year != 2024 || recordMonth != month) continue;
            LocalTime login = LocalTime.parse(attendanceRow[4].trim(), timeFormat);
            LocalTime logout = LocalTime.parse(attendanceRow[5].trim(), timeFormat);
            double hours = computeHours(login, logout);
            if (day<=15) {hours1 += hours;
            }else hours2 += hours;
        }double[] monthlyHours = {hours1,hours2};
        return monthlyHours;
    }

    //compute daily hours
    static double computeHours(LocalTime login, LocalTime logout){
        LocalTime graceTime = LocalTime.of(8,10);//8:10
        LocalTime startTime = LocalTime.of(8,0);//8:00
        LocalTime cutoffTime = LocalTime.of(17,0);//17:00

        if (!login.isAfter(graceTime)){
            login = startTime;
        }if (logout.isAfter(cutoffTime)){
            logout=cutoffTime;
        }if (!logout.isAfter(startTime)){
            return 0;
        }
        long minutesWorked = Duration.between(login, logout).toMinutes();
        if (minutesWorked > 60){
            minutesWorked -=60;
        }else{
            minutesWorked = 0;
        }
        double hours = minutesWorked / 60.0;

        if (hours>8.0)return 8.0;
        return hours;
    }  

    static double computeGrossSalary(double hours, double hourlyRate){
        return hours*hourlyRate;
    }

    static double computeSSS(double grossSalaryTotal, ArrayList<String[]> sssTable){
        double contribution = 0;
        if (grossSalaryTotal<3250.0)return 135.00;
        if (grossSalaryTotal>=24750.0)return 1125.00;
        for (String[] sssData: sssTable){
            double min = Double.parseDouble(sssData[0]);
            double max = Double.parseDouble(sssData[2]);
            if (grossSalaryTotal >= min && grossSalaryTotal <=max){
                contribution = Double.parseDouble(sssData[3]);
                break;
            }
        }
        return contribution;
    }

    static double computePhilHealth (double grossSalaryTotal){
        double premium = 0.03;
        return grossSalaryTotal*(premium/2);//premium shared equally by employee and employer
    }

    static double computePagIBIG (double grossSalaryTotal){
        double contribution = 0;
        if (grossSalaryTotal<1000.0)return 0;
        if (grossSalaryTotal>=1000.0&&grossSalaryTotal<=1500.0){
            contribution = grossSalaryTotal*0.01;}
        else if (grossSalaryTotal > 1500.0){
            contribution = grossSalaryTotal*0.02;}
        if (contribution > 100)return 100.0; //maximum contribution is 100
        return contribution;
    }

    static double computeTax(double taxableIncome){
        double contribution=0;
        if (taxableIncome<20833)return 0;
        else if (taxableIncome>=20833 && taxableIncome<33333){
            contribution = (taxableIncome - 20833)*0.20;
        }else if (taxableIncome>=33333 && taxableIncome<66667){
            contribution = 2500 + ((taxableIncome - 33333)*0.25);
        }else if (taxableIncome>=66667 && taxableIncome<166667){
            contribution = 10833 + ((taxableIncome - 66667)*0.30);
        }else if (taxableIncome>=166667 && taxableIncome<666667){
            contribution = 40833.33 + ((taxableIncome - 166667)*0.32);
        }else if (taxableIncome>=666667 ){
            contribution = 200833.33 + ((taxableIncome - 666667)*0.35);
        }
        return contribution;
    }
}
