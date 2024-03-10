
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

public class Classes {

    //////////////////////////////////////////////////////////////
    // GLOBAL VARIABLES //
    //////////////////////////////////////////////////////////////
    public static final String cmdlistAllClassesbyMissingCoach = "listAllClassesbyMissingCoach";
    public static final String cmdlistAllClassesByDate = "listAllClassesByDate";
    public static final String cmdlistAllClassAttendees = "listAllClassAttendees";
    public static final String cmdlistClassByDifficultyLevel = "listClassByDifficultyLevel";
    public static final String cmdlistClassByEvent = "listClassByEvent";
    public static final String cmdlistClassByEventandDiffLevel = "listClassByEventandDiffLevel";
    public static final String cmdsendStatusNotification = "sendStatusNotification";
    public static final String cmdgetClassStatus = "getClassStatus";
    public static final String cmdgetClassEvent = "getClassEvent";
    public static final String cmdupdateClassStatus = "updateClassStatus";
    public static final String cmdupdateClassEvent = "updateClassEvent";
    public static final String cmdupdateClassStartTime = "updateClassStartTime";
    public static final String cmdassignClassCoach = "assignClassCoach";
    public static final String cmdaddNewClass = "addNewClass";


    //////////////////////////////////////////////////////////////
    // METHODS //
    //////////////////////////////////////////////////////////////


    /**Insert newClassInfointo system which allows for scheduling and categorizing 
     * based on type and difficulty
     * @author Noa Uritsky
     * 
     * @param params
     */
    public static void addNewClass(String[] params){
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("addNewClass - insert new class info into system");
            System.out.println("command: addNewClass className:xxx startTime:YYYY-MM-DD HH:MM " + 
                                "duration:XXM eventName:xxx difficultyName:xxx statusName:xxx)");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] {"className", 
            "startTime", "duration", "event", "difficulty", "status"});
            if(apiParams != null)
            {
                try {
                    GymnasticsGymDB.addNewClass(apiParams);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } 
        }

    }

    /**
     * Return list of all active classes that do not have a coach assigned
     * 
     * @author Anna Rivas
     *
     * @params Input parameter
     */
    public static void listAllClassesbyMissingCoach(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println(
                    "listAllClassesbyMissingCoach - Return list of all active classes that do not have a coach assigned");
            System.out.println("command: listAllClassesbyMissingCoach");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] {});
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.addNewClass(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            } 
        }

    }

    /**
     * Return list of all active classes that do not have a coach assigned
     * 
     * @author Anna Rivas
     *
     * @params Input parameter
     */
    public static void listAllClassesbyMissingCoach(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println(
                    "listAllClassesbyMissingCoach - Return list of all active classes that do not have a coach assigned");
            System.out.println("command: listAllClassesbyMissingCoach");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] {});
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.listAllClassesbyMissingCoach(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Return list of all classes on given date
     * 
     * @author Anna Rivas
     *
     * @params Input parameter
     */
    public static void listAllClassesByDate(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("listAllClassesByDate - Return list of all classes on given date");
            System.out.println("command: listAllClassesByDate Date:YYYY-MM-DD");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "Date" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.listAllClassesByDate(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Return list of all classes on given date
     * 
     * @author Anna Rivas
     *
     * @params Input parameter
     */
    public static void listAllClassAttendees(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("listAllClassAttendees - Return list of all classes on given date");
            System.out.println("command: listAllClassAttendees ClassName:xxx");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "ClassName" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.listAllClassAttendees(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Return list of all classes on given date
     * 
     * @author Anna Rivas
     *
     * @params Input parameter
     */
    public static void listClassByDifficultyLevel(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("listClassByDifficultyLevel - Return list of all classes on given date");
            System.out.println("command: listClassByDifficultyLevel DifficultyLevel:[Beginner|Intermediate|Advanced]");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "DifficultyLevel" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.listClassByDifficultyLevel(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Return list of all active classes on a given event
     * 
     * @author Anna Rivas
     *
     * @params Input parameter
     */
    public static void listClassByEvent(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("listClassByEvent - Return list of all active classes on a given event");
            System.out.println("command: listClassByEvent EventName:xxx");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "EventName" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.listClassByEvent(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Retrieves attendees from a specific class and sends a SMS notification
     * 
     * @author Anna Rivas
     *
     * @params Input parameter
     */
    public static void listClassByEventandDiffLevel(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println(
                    "listClassByEventandDiffLevel - Return list of all active classes on a given event and level of difficulty");
            System.out.println("command: listClassByEventandDiffLevel EventName:xxx ClassName:xxx");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "EventName", "DifficultyLevel" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.listClassByEventandDiffLevel(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Retrieves active attendees from a specific class and sends a SMS notification
     * specifying the class status
     * 
     * @author Anna Rivas
     *
     * @params Input parameter
     */
    public static void sendStatusNotification(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println(
                    "sendStatusNotification - Retrieves active attendees from a specific class and sends a SMS notification specifying the class status");
            System.out.println("command: sendStatusNotification ClassName:xxx");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "ClassName" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.sendStatusNotification(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Return the status of a specific class (Active, Canceled, Completed, etc.)
     * 
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @params Input parameter
     */
    public static void getClassStatus(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println(
                    "getClassStatus - Return the status of a specific class (Active, Canceled, Completed, etc.)");
            System.out.println("command: getClassStatus ClassName:xxx");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "ClassName" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.getClassStatus(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns the type of event from a specific class
     * 
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @params Input parameter
     */
    public static void getClassEvent(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("getClassEvent - Returns the type of event from a specific class");
            System.out.println("command: getClassEvent ClassName:xxx");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "ClassName" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.getClassEvent(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Updates the status of a specific class (Active, Canceled, Completed, etc.)
     * to a new status (Active, Canceled, Completed, etc.).
     * returns true if successful and false if operation failed.
     * 
     * @author Nathaniel Fincham
     *
     * @params Input parameter - ClassName, NewStatus
     */
    public static boolean updateClassStatus(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println(
                    "updateClassStatus - Updates the status of a specific class (In progress, Canceled, Completed, etc.) to a new status (In progress, Canceled, Completed, etc.)");
            System.out.println(
                    "command: updateClassStatus ClassName:xxx NewStatus:[Coming Up|In progress|Cancelled|Postponed|Moved up|Completed]");
            return false;
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "ClassName", "NewStatus" });
            if (apiParams != null) {
                try {
                    return GymnasticsGymDB.updateClassStatus(apiParams);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Updates the event of a specific class to a new event.
     * returns true if successful and false if operation failed.
     * 
     * @author Nathaniel Fincham
     * 
     * @param Input parameter - ClassName, NewEvent
     * @return boolean
     */
    public static boolean updateClassEvent(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("updateClassEvent - Updates the event of a specific class to a new event");
            System.out.println("command: updateClassEvent ClassName:xxx NewEvent:xxx");
            return false;
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "ClassName", "NewEvent" });
            if (apiParams != null) {
                try {
                    return GymnasticsGymDB.updateClassEvent(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Update the start time of a specific class to a new start time.
     * returns true if successful and false if operation failed.
     * 
     * @author Nathaniel Fincham
     * 
     * @param Input parameter - ClassName, NewStartTime
     * @return boolean
     */
    public static boolean updateClassStartTime(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("updateClassStartTime - Update the start time of a specific class to a new start time");
            System.out.println("command: updateClassStartTime ClassName:xxx NewStartTime:YYYY-MM-DD HH:MM");
            return false;
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "ClassName", "NewStartTime" });
            if (apiParams != null) {
                try {
                    return GymnasticsGymDB.updateClassStartTime(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Assigns a coach to a class based on their schedule and class requirements
     * 
     * @author Christopher Long
     *
     * @params Input parameter
     */
    public static void assignClassCoach(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println(
                    "assignClassCoach - Assigns a coach to a class based on their schedule and class requirements");
            System.out.println("command: assignClassCoach Coach UserName:xxx Class Name:xxx");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "Coach UserName", "Class Name" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.assignClassCoach(apiParams);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
