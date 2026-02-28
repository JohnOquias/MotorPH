
package motorph;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class CutOffDate {
    public static void main(String[] args){
        String empNumInput = "10001";
        String attendance = "resources//AttendanceRecord.csv";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        Set<String> displayedCutOffs = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(attendance))){
            br.readLine();
            String line;
            
            while((line = br.readLine())!=null){
                String[] data = line.split(",");
                String employeeNumber = data[0];
                if (!employeeNumber.equals(empNumInput))continue;
                LocalDate date = LocalDate.parse(data[3], formatter);
                LocalDate start1 = date.withDayOfMonth(1);
                LocalDate end1 = date.withDayOfMonth(15);
                LocalDate start2 = date.withDayOfMonth(16);
                LocalDate end2 = date.withDayOfMonth(date.lengthOfMonth());
                String cutOff;
                if (date.getDayOfMonth()<=15){
                   cutOff = "Cuttoff Date: " + start1 + " - "+end1;
                }else{
                   cutOff = "Cuttoff Date: " + start2 + " - "+end2;
                }
                
                if(!displayedCutOffs.contains(cutOff)){
                    System.out.println(cutOff);
                    displayedCutOffs.add(cutOff);
                }
                
                
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        
    }
}
