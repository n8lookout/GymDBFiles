
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
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Util {

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Check string for null
     * 
     * @author The Team
     *
     * @value String value to check
     * 
     * @return String value or empty string if null
     */
    public static String CheckStringForNull(String value) {
        if (value == null || value.equals("null")) {
            return "";
        }
        return value;
    }

    /**
     * Method to parse Input parameters
     * 
     * @author The Team
     *
     * @params String array of required input parameters
     * 
     * @return HashMap of Key/Value of required input parameters
     */
    public static HashMap<String, String> ParseInputParams(String[] params) {
        HashMap<String, String> map = new HashMap<>();

        for (int i = 0; i < params.length; i++) {
            String paramName = params[i];

            System.out.print(paramName + ": ");
            String paramValue = System.console().readLine();

            if (CheckKnownParamValues(paramName, paramValue)) {
                map.put(paramName, paramValue);
            } else {
                return null;
            }
        }

        return map;
    }

    /**
     * Method to validate known Parameters values or format
     * 
     * @author The Team
     *
     * @paramName Input parameter Name
     * @paramValue Input parameter Value
     * 
     * @return true if parameter value passed the validation
     */
    private static boolean CheckKnownParamValues(String paramName, String paramValue) {
        boolean valueOk = true;

        switch (paramName) {
            case "DifficultyLevel":
                if (!paramValue.equals("Beginner") && !paramValue.equals("Intermediate")
                        && !paramValue.equals("Advanced")) {
                    System.out.println(" - Unexpected value, expecting [Beginner|Intermediate|Advanced]");
                    valueOk = false;
                }
                break;
            case "StudentStatus":
                if (!paramValue.equals("Active") && !paramValue.equals("Inactive")) {
                    System.out.println(" - Unexpected value, expecting [Active|Inactive]");
                    valueOk = false;
                }
                break;
            case "ClassStatus":
                // Check to see if status is not = 'Coming up', 'In progess', 'Cancelled',
                // 'Postponed', 'Moved up', 'Completed', or null
                if (!paramValue.equals("Coming up") && !paramValue.equals("In progess")
                        && !paramValue.equals("Cancelled")
                        && !paramValue.equals("Postponed") && !paramValue.equals("Moved up")
                        && !paramValue.equals("Completed")) {
                    System.out.println(
                            " - Unexpected value, expecting [Coming up|In progess|Cancelled|Postponed|Moved up|Completed]");
                    valueOk = false;
                }
                break;
            case "Event":
                // Check to see if value is not 'Floor Exercise', 'Uneven Bars', 'Balance Beam',
                // 'Vault', or null
                if (!paramValue.equals("Floor Exercise") && !paramValue.equals("Uneven Bars")
                        && !paramValue.equals("Balance Beam") && !paramValue.equals("Vault")) {
                    System.out
                            .println(" - Unexpected value, expecting [Floor Exercise|Uneven Bars|Balance Beam|Vault]");
                    valueOk = false;
                }
            case "UserName":
                // Check to see if value is longer than 15 characters
                if (paramValue.length() > 15) {
                    System.out.println(" - Unexpected value, value can not be longer than 15 characters");
                    valueOk = false;
                }
                break;
            case "FirstName":
                // Check to see if value is longer than 15 characters
                if (paramValue.length() > 15) {
                    System.out.println(" - Unexpected value, value can not be longer than 15 characters");
                    valueOk = false;
                }
                break;
            case "LastName":
                // Check to see if value is longer than 15 characters
                if (paramValue.length() > 25) {
                    System.out.println(" - Unexpected value, value can not be longer than 15 characters");
                    valueOk = false;
                }
                break;
            case "ClassName":
                // Check to see if value is longer than 15 characters
                if (paramValue.length() > 15) {
                    System.out.println(" - Unexpected value, value can not be longer than 15 characters");
                    valueOk = false;
                }
                break;
            case "EventName":
                if (paramValue.length() == 0) {
                    System.out.println(" - Unexpected value, value can not be empty");
                    valueOk = false;
                }
                break;
            case "StartTime":
                try {
                    LocalDate.parse(paramValue, dtf);
                } catch (DateTimeParseException e) {
                    valueOk = false;
                    System.out.println(" - Unexpected value, expecting DateTime format yyyy-MM-dd HH:mm");
                }
                break;
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
                    System.out.println(" - Unexpected value, expecting Date format yyyy-MM-dd");
                }
                break;
            case "PhoneNumber":
                // Check to see if Phone number is not 10 characters or if characters aren't
                // numbers
                if (paramValue.length() != 10 || !paramValue.matches("[0-9]+")) {
                    System.out.println(" - Unexpected value, expecting 10 digit phone number");
                    valueOk = false;
                }
                break;
            case "Email":
                String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
                Pattern pattern = Pattern.compile(emailRegex);
                Matcher matcher = pattern.matcher(paramValue);
                if (paramValue.length() > 35 || !matcher.matches()) {
                    System.out.println(" - Unexpected value, expecting valid email address");
                    valueOk = false;
                }
                break;
            default:
                // Check for null value or empty string; else do nothing
                if (paramValue == null || paramValue.length() == 0) {
                    System.out.println(" - Unexpected value, value can not be empty");
                    valueOk = false;
                }
        }

        return valueOk;
    }

}
