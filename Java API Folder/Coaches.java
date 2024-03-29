
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

public class Coaches {

    //////////////////////////////////////////////////////////////
    // GLOBAL VARIABLES //
    //////////////////////////////////////////////////////////////
    public static final String cmdlistAllCoaches = "listAllCoaches";
    public static final String cmdlistAllAvailCoachByDateRange = "listAllAvailCoachByDateRange";
    public static final String cmdgetCoachInfo = "getCoachInfo";
    public static final String cmdgetCoachAvail = "getCoachAvail";
    public static final String cmdgetCoach_userName = "getCoach_userName";
    public static final String cmdaddNewCoach = "addNewCoach";
    public static final String cmdaddNewCoachAvailability = "addNewCoachAvailability";
    public static final String cmdShowCoachSchedule = "showCoachSchedule";
    public static final String cmdchangeCoachSchedule = "changeCoachSchedule";

    //////////////////////////////////////////////////////////////
    // METHODS //
    //////////////////////////////////////////////////////////////

    /**Create a specific coach’s schedule based on the availStartTime and
     * availEndTime that the coach provided. Meaning, a coach schedule is
     * made up from the available times that the coach enters in the program.
     * @author Noa Uritsky
     * 
     * @param params
     */
    public static boolean addNewCoachAvailability(String[] params){
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("addNewCoachAvailability - based on the availStartTime and " +
                                "availEndTime that the coach provided. Meaning, a coach schedule is" + 
                                "made up from the available times that the coach enters in the program.");
            System.out.println("command: addNewCoachAvailability username:xxx schedulename:xxx" + 
                                "startTime:YYYY-MM-DD HH:MM endTime:YYYY-MM-DD HH:MM)");
            return false;
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] {"Username",
                                "ScheduleName", "StartTime", "EndTime"});
            if(apiParams != null)
            {
                try {
                    return GymnasticsGymDB.addNewCoachAvailability(apiParams);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }
    }

    /**Insert newCoachInfo will add a new coach to the system with their contact information
     * @author Noa Uritsky
     * 
     * @param params
     */
    public static boolean addNewCoach(String[] params){
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("addNewCoach - Insert newCoachInfo will add a new coach to the" +
                                " system with their contact information");
            System.out.println("command: addNewCoach username:xxx firstName:xxx" + 
                                "lastName:xxx phoneNumber:xxx email:xxx)");
            return false;
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] {"Username",
                                "FirstName", "LastName", "PhoneNumber", "Email"});
            if(apiParams != null)
            {
                try {
                    return GymnasticsGymDB.addNewCoach(apiParams);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } 

            return false;
        }
    }

    /**
     * Return list of all coaches
     * 
     * @author Anna Rivas
     *
     * @params Input parameter
     */
    public static void listAllCoaches(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("listAllCoaches - Return list of all coaches");
            System.out.println("command: listAllCoaches");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] {});
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.listAllCoaches(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Return list of all coaches that are available on a specific time range
     * 
     * @author Anna Rivas
     *
     * @params Input parameter
     */
    public static void listAllAvailCoachByDateRange(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println(
                    "listAllAvailCoachByDateRange - Return list of all coaches that are available on a specific time range");
            System.out.println(
                    "command: listAllAvailCoachByDateRange StartTime:YYYY-MM-DD hh:mm EndTime:YYYY-MM-DD hh:mm");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "StartTime", "EndTime" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.listAllAvailCoachByDateRange(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Return Coach Contact Information Phone/Email
     * 
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @params Input parameter
     */
    public static void getCoachInfo(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("getCoachInfo - Return Coach Contact Information Phone/Email");
            System.out.println("command: getCoachInfo UserName:xxx");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "UserName" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.getCoachInfo(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns a specific Coaches Availability times from a specific date
     * 
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @params Input parameter
     */
    public static void getCoachAvail(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("getCoachAvail - Returns a specific Coaches Availability times from a specific date");
            System.out.println("command: getCoachAvail UserName:xxx hh:mm Date:YYYY-MM-DD");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "UserName", "Date" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.getCoachAvail(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns Coach’s username
     * 
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @params Input parameter
     */
    public static void getCoach_userName(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("getCoach_userName - Coach's username");
            System.out.println("command: getCoach_userName FirstName:xxx LastName:xxx");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "FirstName", "LastName" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.getCoach_userName(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Retrieves the availability schedule for a specific coach on a specific date.
     * This schedule will only show their available times and exclude the time they
     * are in their assigned classes.
     * 
     * @author Christopher Long
     *
     * @param Input parameter
     */
    public static void showCoachSchedule(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println(
                    "showCoachSchedule - Retrieves the availability schedule for a coach on a specific date. ");
            System.out.println("command: showCoachSchedule Username:xxx Date:xxx");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "Coach UserName", "Date" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.showCoachSchedule(apiParams);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Change a coach's schedule
     * 
     * @author Nathaniel Fincham
     * @return boolean: true = successful, false = failure
     * @param params - coach's username, scheduleName, startTime, endTime
     */
    public static boolean changeCoachSchedule(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("changeCoachSchedule - Change a coach's schedule");
            System.out.println(
                    "command: changeCoachSchedule UserName:xxx ScheduleName:xxx StartTime: YYYY-MM-DD HH:MM EndTime: YYYY-MM-DD HH:MM");
            return false;
        } else {
            HashMap<String, String> apiParams = Util
                    .ParseInputParams(new String[] { "UserName", "scheduleName", "StartTime", "EndTime" });
            if (apiParams != null) {
                try {
                    return GymnasticsGymDB.changeCoachSchedule(apiParams);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}