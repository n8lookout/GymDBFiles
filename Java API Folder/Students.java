
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
    // GLOBAL VARIABLES //
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
    public static final String cmdupdateStudentDiffLevel = "updateStudentDiffLevel";
    public static final String cmdupdateStudentStatus = "updateStudentStatus";
    public static final String cmdupdateStudentEmerContact = "updateStudentEmerContact";
    public static final String cmdaddStudentToClass = "addStudentToClass";
    public static final String cmdupdateStudentSchedule = "updateStudentSchedule";
    public static final String cmdAddNewStudent = "addNewStudent";

    //////////////////////////////////////////////////////////////
    // METHODS //
    //////////////////////////////////////////////////////////////

    /**Insert newStudentInfo will register new students into the system with their
     * personal and contact information and will automatically set the student’s
     * status to Active
     * @author Noa Uritsky
     * 
     * @param params
     */
    public static boolean addNewStudent(String[] params)
    {
        System.out.println("");
        if(params == null || params.length == 0)
        {
            System.out.println("addNewStudent - Insert newStudentInfo will register new  " +
                                "students into the systemwith their personal and contact information " +
                                "and will automatically set the student’s status to Active");
            System.out.println("command: addNewStudent username:xxx firstName:xxx" + 
                                "lastName:xxx birthDate:YYYY-MM-DD phoneNumber:xxx email:xxx)");
            return false;
        }
        else
        {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] {"Username",
                                "FirstName", "LastName", "BirthDate", "PhoneNumber", "Email"});
            if(apiParams != null)
            {
                try {
                    return GymnasticsGymDB.addNewStudent(apiParams);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } 
            return false;
        }
    }


    /**
     * Returns a list of all students with their information including their
     * usernames. The list is ordered by their last names in aplhabetic order.
     * 
     * @author Anna Rivas
     *
     * @params Input parameter
     */
    public static void listAllStudents(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println(
                    "listAllStudents - Returns a list of all students with their information including their usernames. The list is ordered by their last names in aplhabetic order.");
            System.out.println("command: listAllStudents");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] {});
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.listAllStudents(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Return list of students filtered by difficulty level
     * 
     * @author Anna Rivas
     *
     * @params Input parameter
     */
    public static void listAllStudentsByDiffLevel(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("listAllStudentsByDiffLevel - Return list of students filtered by difficulty level");
            System.out.println("command: listAllStudentsByDiffLevel DifficultyLevel:[Beginner|Intermediate|Advanced]");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "DifficultyLevel" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.listAllStudentsByDiffLevel(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Return list of students filtered by their status
     * 
     * @author Anna Rivas
     *
     * @params Input parameter
     */
    public static void listAllStudentsByStatus(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("listAllStudentsByStatus - Return list of students filtered by their status");
            System.out.println("command: listAllStudentsByStatus StudentStatus:[Active|Inactive]");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "StudentStatus" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.listAllStudentsByStatus(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns a list of active classes where a specific student is listed as
     * attendee
     * 
     * @author Anna Rivas
     *
     * @params Input parameter
     */
    public static void listAllClassesForAStudent(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println(
                    "listAllClassesForAStudent - Returns a list of active classes where a specific student is listed as attendee");
            System.out.println("command: listAllClassesForAStudent UserName:xxx");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "UserName" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.listAllClassesForAStudent(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Return Student Name/Phone/Email/EmerContact
     * 
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @params Input parameter
     */
    public static void getStudentInfo(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("getStudentInfo - Return Student Name/Phone/Email/EmerContact");
            System.out.println("command: getStudentInfo UserName:xxx");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "UserName" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.getStudentInfo(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns the EmerContact from a specific student if any.
     * 
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @params Input parameter
     */
    public static void getStudentEmerContact(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("getStudentEmerContact - Returns the EmerContact from a specific student if any.");
            System.out.println("command: getStudentEmerContact UserName:xxx");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "UserName" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.getStudentEmerContact(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Return Difficulty Level of a specific student
     * 
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @params Input parameter
     */
    public static void getStudentDiffLevel(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("getStudentDiffLevel - Return Difficulty Level of a specific student");
            System.out.println("command: getStudentDiffLevel UserName:xxx");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "UserName" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.getStudentDiffLevel(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Return Student’s userName
     * 
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @params Input parameter
     */
    public static void getStudent_userName(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("getStudent_userName - Student's userName");
            System.out.println("command: getStudent_userName FirstName:xxx LastName:xxx");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "FirstName", "LastName" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.getStudent_userName(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns Emergency Contact’s username
     * 
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @params Input parameter
     */
    public static void getEmerContact_userName(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("getEmerContact_userName - Emergency Contact's username");
            System.out.println("command: getEmerContact_userName FirstName:xxx LastName:xxx");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "FirstName", "LastName" });
            if (apiParams != null) {
                try {
                    GymnasticsGymDB.getEmerContact_userName(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Update Student Difficulty Level
     * Gather student's username and new difficulty level
     * Returns boolean for success or failure
     * 
     * @author @n8lookout
     * 
     * @params Input parameter
     */
    public static boolean updateStudentDiffLevel(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("updateStudentDiffLevel - Update Student Difficulty Level");
            System.out.println(
                    "command: updateStudentDiffLevel UserName:xxx DifficultyLevel:[Beginner|Intermediate|Advanced]");
        } else {
            HashMap<String, String> apiParams = Util
                    .ParseInputParams(new String[] { "UserName", "DifficultyLevel" });
            if (apiParams != null) {
                try {
                    return GymnasticsGymDB.updateStudentDiffLevel(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Update Student Status
     * Gather student's username and new status
     * Returns boolean for success or failure
     * 
     * @author @n8lookout
     * @param Input parameter
     */
    public static boolean updateStudentStatus(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("updateStudentStatus - Update Student Status");
            System.out.println("command: updateStudentStatus UserName:xxx StudentStatus:[Active|Inactive]");
        } else {
            HashMap<String, String> apiParams = Util
                    .ParseInputParams(new String[] { "UserName", "StudentStatus" });
            if (apiParams != null) {
                try {
                    return GymnasticsGymDB.updateStudentStatus(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Update Student Emergency Contact
     * Gather student's username and new emergency contact
     * Returns boolean for success or failure
     * 
     * @author @n8lookout
     * @param Input parameters
     */
    public static boolean updateStudentEmerContact(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("updateStudentEmerContact - Update Student Emergency Contact");
            System.out.println("command: updateStudentEmerContact UserName:xxx NewEmerContactUserName:xxx");
        } else {
            HashMap<String, String> apiParams = Util
                    .ParseInputParams(new String[] { "UserName", "NewEmerContactUserName" });
            if (apiParams != null) {
                try {
                    return GymnasticsGymDB.updateStudentEmerContact(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Add Student to Class
     * Gather student's username and className
     * Check if student is already in class
     * Check if student has other classes at the same time
     * Returns boolean for success or failure
     * 
     * @author @n8lookout
     * 
     * @param input parameters
     */
    public static boolean addStudentToClass(String[] params) {
        System.out.println("");
        if (params == null || params.length == 0) {
            System.out.println("addStudentToClass - Add Student to Class");
            System.out.println("command: addStudentToClass UserName:xxx ClassName:xxx");
        } else {
            HashMap<String, String> apiParams = Util.ParseInputParams(new String[] { "UserName", "ClassName" });
            if (apiParams != null) {
                try {
                    return GymnasticsGymDB.addStudentToClass(apiParams);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
