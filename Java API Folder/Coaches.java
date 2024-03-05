
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
    //                    GLOBAL VARIABLES                      //
    //////////////////////////////////////////////////////////////    
    public static final String cmdlistAllCoaches = "listAllCoaches";
    public static final String cmdlistAllAvailCoachByDateRange = "listAllAvailCoachByDateRange";
    public static final String cmdgetCoachInfo = "getCoachInfo";
    public static final String cmdgetCoachAvail = "getCoachAvail";
    public static final String cmdgetCoach_userName = "getCoach_userName";
    
    //////////////////////////////////////////////////////////////
    //                       METHODS                            //
    //////////////////////////////////////////////////////////////

    /**
     * Return list of all coaches
     * @author Anna Rivas
     *
     * @params Input parameter
     */       
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
                try {
                    GymnasticsGymDB.listAllCoaches(apiParams);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }  
    
    /**
     * Return list of all coaches that are available on a specific time range
     * @author Anna Rivas
     *
     * @params Input parameter
     */     
    public static void listAllAvailCoachByDateRange(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("listAllAvailCoachByDateRange - Return list of all coaches that are available on a specific time range");
            System.out.println("command: listAllAvailCoachByDateRange StartTime:YYYY-MM-DD hh:mm EndTime:YYYY-MM-DD hh:mm");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "StartTime", "EndTime" });
            if(apiParams != null)
            {
                try {
                    GymnasticsGymDB.listAllAvailCoachByDateRange(apiParams);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }            
        }
    }   
    
    /**
     * Return Coach Contact Information Phone/Email
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @params Input parameter
     */      
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
                try {
                    GymnasticsGymDB.getCoachInfo(apiParams);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }            
        }
    } 
    
    /**
     * Returns a specific Coaches Availability times from a specific date
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @params Input parameter
     */       
    public static void getCoachAvail(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("getCoachAvail - Returns a specific Coaches Availability times from a specific date");
            System.out.println("command: getCoachAvail UserName:xxx hh:mm Date:YYYY-MM-DD");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "UserName", "Date" });
            if(apiParams != null)
            {
                try {
                    GymnasticsGymDB.getCoachAvail(apiParams);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }            
        }
    }     

    /**
     * Returns Coach’s username
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @params Input parameter
     */      
    public static void getCoach_userName(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("getCoach_userName - Coach's username");
            System.out.println("command: getCoach_userName FirstName:xxx LastName:xxx");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "FirstName", "LastName" });
            if(apiParams != null)
            {
                try {
                    GymnasticsGymDB.getCoach_userName(apiParams);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }            
        }
    } 

}
