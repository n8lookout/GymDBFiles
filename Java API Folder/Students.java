
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

public class Students {

    //////////////////////////////////////////////////////////////
    //                    GLOBAL VARIABLES                      //
    //////////////////////////////////////////////////////////////    
    public static final String cmdlistAllStudents = "listAllStudents";
    public static final String cmdlistAllStudentsByDiffLevel = "listAllStudentsByDiffLevel";
    public static final String cmdlistAllStudentsByStatus = "listAllStudentsByStatus";
    public static final String cmdlistAllClassesForAStudent = "listAllClassesForAStudent";
    public static final String cmdgetStudentInfo = "getStudentInfo";
    public static final String cmdgetStudentEmerContact = "getStudentEmerContact";
    public static final String cmdgetStudentDiffLevel = "getStudentDiffLevel";
    public static final String cmdgetStudent_userName = "getStudent_userName";
    public static final String cmdgetEmerContact_userName = "getEmerContact_userName";

    //////////////////////////////////////////////////////////////
    //                       METHODS                            //
    //////////////////////////////////////////////////////////////

    /**
     * Returns a list of all students with their information including their usernames. The list is ordered by their last names in aplhabetic order.
     * @author Anna Rivas
     *
     * @params Input parameter
     */       
    public static void listAllStudents(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("listAllStudents - Returns a list of all students with their information including their usernames. The list is ordered by their last names in aplhabetic order.");
            System.out.println("command: listAllStudents");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] {});
            if(apiParams != null)
            {
                try {
                    GymnasticsGymDB.listAllStudents(apiParams);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Return list of students filtered by difficulty level
     * @author Anna Rivas
     *
     * @params Input parameter
     */       
    public static void listAllStudentsByDiffLevel(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("listAllStudentsByDiffLevel - Return list of students filtered by difficulty level");
            System.out.println("command: listAllStudentsByDiffLevel DifficultyLevel:[Beginner|Intermediate|Advanced]");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "DifficultyLevel" });
            if(apiParams != null)
            {
                try {
                    GymnasticsGymDB.listAllStudentsByDiffLevel(apiParams);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    } 
    
    /**
     * Return list of students filtered by their status
     * @author Anna Rivas
     *
     * @params Input parameter
     */       
    public static void listAllStudentsByStatus(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("listAllStudentsByStatus - Return list of students filtered by their status");
            System.out.println("command: listAllStudentsByStatus StudentStatus:[Active|Inactive]");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "StudentStatus" });
            if(apiParams != null)
            {
                try {
                    GymnasticsGymDB.listAllStudentsByStatus(apiParams);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }  

    /**
     * Returns a list of active classes where a specific student is listed as attendee
     * @author Anna Rivas
     *
     * @params Input parameter
     */       
    public static void listAllClassesForAStudent(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("listAllClassesForAStudent - Returns a list of active classes where a specific student is listed as attendee");
            System.out.println("command: listAllClassesForAStudent UserName:xxx");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "UserName" });
            if(apiParams != null)
            {
                try {
                    GymnasticsGymDB.listAllClassesForAStudent(apiParams);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }            
        }
    }   

    /**
     * Return Student Name/Phone/Email/EmerContact
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @params Input parameter
     */      
    public static void getStudentInfo(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("getStudentInfo - Return Student Name/Phone/Email/EmerContact");
            System.out.println("command: getStudentInfo UserName:xxx");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "UserName" });
            if(apiParams != null)
            {
                try {
                    GymnasticsGymDB.getStudentInfo(apiParams);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }            
        }
    } 
    
    /**
     * Returns the EmerContact from a specific student if any.
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @params Input parameter
     */     
    public static void getStudentEmerContact(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("getStudentEmerContact - Returns the EmerContact from a specific student if any.");
            System.out.println("command: getStudentEmerContact UserName:xxx");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "UserName" });
            if(apiParams != null)
            {
                try {
                    GymnasticsGymDB.getStudentEmerContact(apiParams);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }            
        }
    }   
    
    /**
     * Return Difficulty Level of a specific student
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @params Input parameter
     */    
    public static void getStudentDiffLevel(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("getStudentDiffLevel - Return Difficulty Level of a specific student");
            System.out.println("command: getStudentDiffLevel UserName:xxx");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "UserName" });
            if(apiParams != null)
            {
                try {
                    GymnasticsGymDB.getStudentDiffLevel(apiParams);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }            
        }
    }      

    /**
     * Return Student’s userName
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @params Input parameter
     */      
    public static void getStudent_userName(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("getStudent_userName - Student's userName");
            System.out.println("command: getStudent_userName FirstName:xxx LastName:xxx");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "FirstName", "LastName" });
            if(apiParams != null)
            {
                try {
                    GymnasticsGymDB.getStudent_userName(apiParams);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }            
        }
    } 

    /**
     * Returns Emergency Contact’s username
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @params Input parameter
     */      
    public static void getEmerContact_userName(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("getEmerContact_userName - Emergency Contact's username");
            System.out.println("command: getEmerContact_userName FirstName:xxx LastName:xxx");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "FirstName", "LastName" });
            if(apiParams != null)
            {
                try {
                    GymnasticsGymDB.getEmerContact_userName(apiParams);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }            
        }
    } 
    
}
