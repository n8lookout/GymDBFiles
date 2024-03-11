
/**
 * CSS 475 Team Project
 * @team Hex Girls
 * @authors Anna Rivas, Nasheeta Lott, Christopher Long, Nathaniel Fincham, Noa Uritsky
 * @version 3/8/2024
 */
//////////////////////////////////////////////////////////////
//                         IMPORTS                          //
//////////////////////////////////////////////////////////////
import java.sql.SQLException;
import java.util.HashMap;

public class EmergencyContacts {

    //////////////////////////////////////////////////////////////
    // GLOBAL VARIABLES //
    //////////////////////////////////////////////////////////////

    public static final String cmdupdateEmergencyContact = "updateEmergencyContact";
    public static final String cmdaddNewEmergencyContact = "addNewEmergencyContact";

    //////////////////////////////////////////////////////////////
    // METHODS //
    //////////////////////////////////////////////////////////////

    /**Insert newEmerContactInfo and associates the EmergencyContact to a student
     * @author Noa Uritsky
     * 
     * @param params
     */
    public static boolean addNewEmergencyContact(String[] params){
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("addNewEmergencyContact - Insert newEmerContactInfo and"+
                                " associates the EmergencyContact to a student");
            System.out.println("command: student_userName:xxx emergcon_userName:xxx" +
            " firstName:xxx lastName:xxx phoneNumber:xxx email:xxx");
            return false;
        } else {
            HashMap<String, String> apiParams = Util
                    .ParseInputParams(new String[] { "Student_Username", "EmergencyContact_Username",
                    "FirstName", "LastName", "PhoneNumber", "Email" });
            if (apiParams != null) {
                try {
                    return GymnasticsGymDB.addNewEmergencyContact(apiParams);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Update emergency contact information for a specific emergency contact
     * Need emergency contact userName and new emergency contact information
     * return boolean: true = successful, false = failure
     * 
     * @author @n8lookout
     * @param params Input parameter - emergency contact userName and new emergency
     *               contact information
     */
    public static boolean updateEmergencyContact(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println(
                    "updateEmergencyContact - Update emergency contact information for a specific emergency contact");
            System.out.println("command: updateEmergencyContact UserName:xxx PhoneNum:xxx Email:xxx");
            return false;
        } else {
            HashMap<String, String> apiParams = Util
                    .ParseInputParams(new String[] { "UserName", "PhoneNumber", "Email" });
            if (apiParams != null) {
                try {
                    return GymnasticsGymDB.updateEmergencyContact(apiParams);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}