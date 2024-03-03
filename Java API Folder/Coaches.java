
import java.util.HashMap;

public class Coaches {
    
    public static final String cmdlistAllCoaches = "listAllCoaches";
    public static final String cmdlistAllAvailCoachByDateRange = "listAllAvailCoachByDateRange";
    public static final String cmdgetCoachInfo = "getCoachInfo";
    public static final String cmdgetCoachAvail = "getCoachAvail";

    public static void listAllCoaches(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("listAllCoaches - Return list of all coaches");
            System.out.println("command: listAllCoaches");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { });
            if(apiParams != null)
            {
                System.out.println("Executing listAllCoaches API from GymnasticsGymAPI");
            }
        }
    }  
    
    public static void listAllAvailCoachByDateRange(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("listAllAvailCoachByDateRange - Return list of all coaches that are available on a specific time range");
            System.out.println("command: listAllAvailCoachByDateRange StartTime:YYYY/MM/DD hh:mm EndTime:YYYY/MM/DD hh:mm");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "StartTime", "EndTime" });
            if(apiParams != null)
            {
                System.out.println("Executing listAllAvailCoachByDateRange API from GymnasticsGymAPI");
            }            
        }
    }   
    
    public static void getCoachInfo(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("getCoachInfo - Return Coach Contact Information Phone/Email");
            System.out.println("command: getCoachInfo UserName:xxx");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "UserName" });
            if(apiParams != null)
            {
                System.out.println("Executing getCoachInfo API from GymnasticsGymAPI");
            }            
        }
    } 
    
    public static void getCoachAvail(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("getCoachAvail - Returns a specific Coaches’ Availability times from a specific date");
            System.out.println("command: getCoachAvail UserName:xxx hh:mm Date:YYYY/MM/DD hh:mm");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "UserName", "Date" });
            if(apiParams != null)
            {
                System.out.println("Executing getCoachAvail API from GymnasticsGymAPI");
            }            
        }
    }     

}
