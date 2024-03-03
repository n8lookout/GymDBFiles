
import java.util.HashMap;

public class Students {

    public static final String cmdlistAllStudents = "listAllStudents";
    public static final String cmdlistAllStudentsByDiffLevel = "listAllStudentsByDiffLevel";
    public static final String cmdlistAllStudentsByStatus = "listAllStudentsByStatus";
    public static final String cmdlistAllClassesForAStudent = "listAllClassesForAStudent";
    public static final String cmdgetStudentInfo = "getStudentInfo";
    public static final String cmdgetStudentEmerContact = "getStudentEmerContact";
    public static final String cmdgetStudentDiffLevel = "getStudentDiffLevel";

    public static void listAllStudents(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("listAllStudents - Return students names ordered by last name and their unique user name");
            System.out.println("command: listAllStudents");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] {});
            if(apiParams != null)
            {
                System.out.println("Executing listAllStudents API from GymnasticsGymAPI");
            }
        }
    }

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
                System.out.println("Executing listAllStudentsByDiffLevel API from GymnasticsGymAPI");
            }
        }
    } 
    
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
                System.out.println("Executing listAllStudentsByStatus API from GymnasticsGymAPI");
            }
        }
    }  

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
                System.out.println("Executing listAllClassesForAStudent API from GymnasticsGymAPI");
            }            
        }
    }   

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
                System.out.println("Executing getStudentInfo API from GymnasticsGymAPI");
            }            
        }
    } 
    
    public static void getStudentEmerContact(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("getStudentEmerContact - Returns the EmerContact from a specific student if any. If not it returns null");
            System.out.println("command: getStudentEmerContact UserName:xxx");
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "UserName" });
            if(apiParams != null)
            {
                System.out.println("Executing getStudentEmerContact API from GymnasticsGymAPI");
            }            
        }
    }   
    
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
                System.out.println("Executing getStudentDiffLevel API from GymnasticsGymAPI");
            }            
        }
    }      
      
}
