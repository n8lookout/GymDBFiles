/**
 * CSS 475 Team Project
 * @team Hex Girls
 * @authors Anna Rivas, Nasheeta Lott, Christopher Long, Nathaniel Fincham, Noa Uritsky
 * @version 3/8/2024
 */
//////////////////////////////////////////////////////////////
//                         IMPORTS                          //
//////////////////////////////////////////////////////////////
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

public class Util {

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Check string for null
     * @author Anna Rivas
     *
     * @value String value to check
     * 
     * @return String value or empty string if null
     */      
    public static String CheckStringForNull(String value)
    {
        if(value == null || value.equals("null"))
        {
            return "";
        }
        return value;
    }

    /**
     * Method to parse Input parameters
     * @author Anna Rivas
     *
     * @params String array of required input parameters
     * 
     * @return HashMap of Key/Value of required input parameters
     */    
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

    /**
     * Method to validate known Parameters values or format
     * @author Anna Rivas
     *
     * @paramName Input parameter Name
     * @paramValue Input parameter Value
     * 
     * @return true if parameter value passed the validation
     */     
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
