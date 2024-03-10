
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

    //////////////////////////////////////////////////////////////
    // METHODS //
    //////////////////////////////////////////////////////////////

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
            System.out.println("command: updateEmergencyContact UserName:xxx NewPhoneNum:xxx NewEmail:xxx");
            return false;
        } else {
            HashMap<String, String> apiParams = Util
                    .ParseInputParams(new String[] { "userName", "newPhoneNum", "newEmail" });
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