

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

public class Util {

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static HashMap<String, String> ParseInputParams(String[] params)
    {
        HashMap<String, String> map = new HashMap<>();

        for(int i = 0; i < params.length; i++)
        {
            String paramName = params[i];

            System.out.print(paramName + ": ");
            String paramValue = System.console().readLine();

            if(CheckKnownParamValues(paramName, paramValue))
            {
                map.put(paramName, paramValue);
            }
            else
            {
                return null;
            }
        }

        return map;
    }

    private static boolean CheckKnownParamValues(String paramName, String paramValue)
    {
        boolean valueOk = true;

        switch(paramName) {
            case "DifficultyLevel":
                if(!paramValue.equals("Beginner") && !paramValue.equals("Intermediate") && !paramValue.equals("Advanced"))
                {
                    System.out.println(" - Unexpected value, expecting [Beginner|Intermediate|Advanced]");
                    valueOk = false;
                }
              break;
            case "StudentStatus":
                if(!paramValue.equals("Active") && !paramValue.equals("Inactive"))
                {
                    System.out.println(" - Unexpected value, expecting [Active|Inactive]");
                    valueOk = false;
                }     
                break;
            case "StartTime":
            case "EndTime":
                try {
                    LocalDate.parse(paramValue, dtf);
                } catch (DateTimeParseException e) {
                    valueOk = false;
                    System.out.println(" - Unexpected value, expecting DateTime format yyyy-MM-dd HH:mm");
                }  
                break;                   
            case "Date":
                try {
                    LocalDate.parse(paramValue, df);
                } catch (DateTimeParseException e) {
                    valueOk = false;
                    System.out.println(" - Unexpected value, expecting DateTime format yyyy-MM-dd");
                }  
                break;                   
            default:
          }        

        return valueOk;
    }
    
}
