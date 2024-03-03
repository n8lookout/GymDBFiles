import java.util.HashMap;

public class Classes {
    
    public static final String cmdlistAllClassesbyMissingCoach = "listAllClassesbyMissingCoach";
    public static final String cmdlistAllClassesByDate = "listAllClassesByDate";
    public static final String cmdlistAllClassAttendees = "listAllClassAttendees";
    public static final String cmdlistClassByDifficultyLevel = "listClassByDifficultyLevel";
    public static final String cmdlistClassByEvent = "listClassByEvent";
    public static final String cmdgetClassStatus = "getClassStatus";
    public static final String cmdgetClassAttendees = "getClassAttendees";
    public static final String cmdgetClassEvent = "getClassEvent";
    public static final String cmdsendStatusNotification = "sendStatusNotification";

    public static void listAllClassesbyMissingCoach(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("listAllClassesbyMissingCoach - Return list of all active classes that don’t have a coach assigned");
            System.out.println("command: listAllClassesbyMissingCoach");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { });
            if(apiParams != null)
            {
                System.out.println("Executing listAllClassesbyMissingCoach API from GymnasticsGymAPI");
            }
        }
    }  

    public static void listAllClassesByDate(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("listAllClassesByDate - Return list of all classes on given date");
            System.out.println("command: listAllClassesByDate Date:YYYY/MM/DD hh:mm");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "Date" });
            if(apiParams != null)
            {
                System.out.println("Executing listAllClassesByDate API from GymnasticsGymAPI");
            }            
        }
    }  
    
    public static void listAllClassAttendees(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("listAllClassAttendees - Return list of all classes on given date");
            System.out.println("command: listAllClassAttendees ClassName:xxx");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "ClassName" });
            if(apiParams != null)
            {
                System.out.println("Executing listAllClassAttendees API from GymnasticsGymAPI");
            }            
        }
    }     

    public static void listClassByDifficultyLevel(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("listClassByDifficultyLevel - Return list of all classes on given date");
            System.out.println("command: listClassByDifficultyLevel DifficultyLevel:[Beginner|Intermediate|Advanced]");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "DifficultyLevel" });
            if(apiParams != null)
            {
                System.out.println("Executing listClassByDifficultyLevel API from GymnasticsGymAPI");
            }            
        }
    }        

    public static void listClassByEvent(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("listClassByEvent - Return list of all active classes on a given event");
            System.out.println("command: listClassByEvent EventName:xxx");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "EventName" });
            if(apiParams != null)
            {
                System.out.println("Executing listClassByEvent API from GymnasticsGymAPI");
            }            
        }
    }     

    public static void getClassStatus(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("getClassStatus - Return the status of a specific class (Active, Canceled, Completed)");
            System.out.println("command: getClassStatus ClassName:xxx");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "ClassName" });
            if(apiParams != null)
            {
                System.out.println("Executing getClassStatus API from GymnasticsGymAPI");
            }            
        }
    } 
    
    public static void getClassAttendees(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("getClassAttendees - Returns list students attending a specific class");
            System.out.println("command: getClassAttendees ClassName:xxx");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "ClassName" });
            if(apiParams != null)
            {
                System.out.println("Executing getClassAttendees API from GymnasticsGymAPI");
            }            
        }
    }     

    public static void sendStatusNotification(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("sendStatusNotification - Retrieves attendees from a specific class  and sends a SMS notification");
            System.out.println("command: sendStatusNotification ClassName:xxx");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "ClassName" });
            if(apiParams != null)
            {
                System.out.println("Executing sendStatusNotification API from GymnasticsGymAPI");
            }            
        }
    }  
    
    public static void getClassEvent(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("getClassEvent - Returns the type of event from a specific class");
            System.out.println("command: getClassEvent ClassName:xxx");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "ClassName" });
            if(apiParams != null)
            {
                System.out.println("Executing getClassEvent API from GymnasticsGymAPI");
            }            
        }
    }     
}
