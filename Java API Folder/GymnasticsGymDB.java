/**
 * CSS 475 Team Project
 * @team Hex Girls
 * @authors Anna Rivas, Nasheeta Lott, Christopher Long, Nathaniel Fincham, Noa Uritsky
 * @version 3/8/2024
 */
//////////////////////////////////////////////////////////////
//                         IMPORTS                          //
//////////////////////////////////////////////////////////////
import java.sql.*;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Properties;

/***
 * Gymnastics Gym API that uses JDBC to connect to a PostgreSQL database.
 */
public class GymnasticsGymDB {


    //////////////////////////////////////////////////////////////
    //                    GLOBAL VARIABLES                      //
    //////////////////////////////////////////////////////////////

    // JDBC URL, username, and password of PostgreSQL server
    private static final String URL = "jdbc:postgresql://localhost:5432/gymnasticsgym"; 
    private static final String USER = "postgres";
    private static final String PASSWORD = "net1net2";
    private static Connection connection = null;

    //////////////////////////////////////////////////////////////
    //                       METHODS                            //
    //////////////////////////////////////////////////////////////

    /**
     * Method to connect to the PostgreSQL database with retries in case of failure
     * @author The Team
     *
     * @return Connection to the PostgreSQL database
     * @throws SQLException Connection to database times out
     */
    public static Connection getConnection() throws SQLException {
        
        if(connection != null && !connection.isValid(10000))
        {
            connection = null;
        }

        int connectionAttempts = 0;
        while(connection == null || connection.isClosed())
        {
            try {
                connectionAttempts++;
                System.out.println("Creating connection to DB...");

                Properties connectionProps = new Properties();
                connectionProps.put("user", USER);
                connectionProps.put("password", PASSWORD);
        
                connection = DriverManager.getConnection(URL, connectionProps);

                if(!connection.isValid(100))
                {
                    connection = null;
                    throw new SQLException("Connection not Valid");
                }
    
            } catch (SQLException  e) {
                System.out.println("Failed connecting to DB !");

                if(connectionAttempts >= 10)
                {
                    connection = null;
                    throw e;
                }

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }  
            System.out.println("");          
        }
        
        return connection;
    }

    /**
     * Method to disconnect from the PostgreSQL database
     * @author The Team
     *
     * @post The input variables will be closed and set to null
     * @param connection to PostgreSQL database
     * @throws SQLException Connection to database times out
     */
    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////
    //                       STUDENTS                           //
    //////////////////////////////////////////////////////////////

    /**
     * Returns a list of all students with their information including their usernames. The list is ordered by their last names in aplhabetic order.
     * @author Anna Rivas
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void listAllStudents(HashMap<String, String> apiParams) throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL statement
            String sql = "SELECT Student.student_userName, Student.firstName, Student.lastName, Student.birthDate, Student.phoneNumber, Student.email, Student.isActive " +
                         "FROM Student " +
                         "ORDER BY lastName ASC"; 

            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("UserName: " + resultSet.getString("student_userName"));
                System.out.println("First Name: " + resultSet.getString("firstName"));
                System.out.println("Last Name: " + resultSet.getString("lastName"));
                System.out.println("Birthday: " + resultSet.getString("birthDate"));
                System.out.println("Phone: " + resultSet.getString("phoneNumber"));
                System.out.println("Email: " + resultSet.getString("email"));
                boolean status = resultSet.getBoolean("isActive");
                System.out.println("Student Status: " + (status ? "Active" : "Inactive"));
                System.out.println("");
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }              
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (statement != null) {
                statement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Return list of students filtered by difficulty level
     * @author Anna Rivas
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void listAllStudentsByDiffLevel(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Student.student_userName, Student.firstName, Student.lastName, Student.birthDate, Student.phoneNumber, Student.email, Student.isActive, DifficultyLevel.difficultyName " +
                         "FROM Student " +
                         "   JOIN Student_DifficultyLevel ON Student_DifficultyLevel.studentID = Student.studentID " +
                         "   JOIN DifficultyLevel ON DifficultyLevel.difficultyID = Student_DifficultyLevel.difficultyID " +
                         "WHERE Student_DifficultyLevel.difficultyID = (SELECT difficultyID " +
                         "                                              FROM DifficultyLevel " +
                         "                                              WHERE difficultyName = ?) " +
                         "ORDER BY lastName ASC";                         


            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("DifficultyLevel"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("UserName: " + resultSet.getString("student_userName"));
                System.out.println("First Name: " + resultSet.getString("firstName"));
                System.out.println("Last Name: " + resultSet.getString("lastName"));
                System.out.println("Birthday: " + resultSet.getString("birthDate"));
                System.out.println("Phone: " + resultSet.getString("phoneNumber"));
                System.out.println("Email: " + resultSet.getString("email"));
                boolean status = resultSet.getBoolean("isActive");
                System.out.println("Student Status: " + (status ? "Active" : "Inactive"));
                System.out.println("Difficulty Level: " + resultSet.getString("difficultyName"));
                System.out.println("");
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Return list of students filtered by their status
     * @author Anna Rivas
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void listAllStudentsByStatus(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Student.student_userName, Student.firstName, Student.lastName, Student.birthDate, Student.phoneNumber, Student.email, Student.isActive " +
                         "FROM Student " +
                         "WHERE isActive = ?";                         

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, apiParams.get("StudentStatus").equals("Active") ? true : false);
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("UserName: " + resultSet.getString("student_userName"));
                System.out.println("First Name: " + resultSet.getString("firstName"));
                System.out.println("Last Name: " + resultSet.getString("lastName"));
                System.out.println("Birthday: " + resultSet.getString("birthDate"));
                System.out.println("Phone: " + resultSet.getString("phoneNumber"));
                System.out.println("Email: " + resultSet.getString("email"));
                boolean status = resultSet.getBoolean("isActive");
                System.out.println("StudentStatus: " + (status ? "Active" : "Inactive"));
                System.out.println("");
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Returns a list of active classes where a specific student is listed as attendee
     * @author Anna Rivas
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void listAllClassesForAStudent(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Class.className, Class.startTime, Class.duration, Event.eventName, DifficultyLevel.difficultyName, Class_Status.statusName " +
                         "FROM Student " +
	                     "  JOIN Attendees ON Attendees.studentID = Student.studentID " +
	                     "  JOIN Class ON Class.classID = Attendees.classID " +
	                     "  JOIN Class_Status ON Class_Status.statusID = Class.statusID " +
	                     "  JOIN Event ON Event.eventID = Class.eventID " +
	                     "  JOIN DifficultyLevel ON DifficultyLevel.difficultyID = Class.difficultyID " +
                         "WHERE statusName IN ('Coming up', 'In progress', 'Postponed', 'Moved up') AND Student.student_userName = ?";                        

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("UserName"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("Class Name: " + resultSet.getString("className"));
                System.out.println("Event Name: " + resultSet.getString("eventName"));
                System.out.println("Difficulty Level: " + resultSet.getString("difficultyName"));
                System.out.println("Class Status: " + resultSet.getString("statusName"));
                System.out.println("Start Time: " + resultSet.getString("startTime"));
                System.out.println("Duration: " + resultSet.getString("duration"));
                System.out.println("");
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Return Student Name/Phone/Email/EmerContact
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void getStudentInfo(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Student.firstName, Student.lastName, Student.birthDate, Student.phoneNumber, Student.email, Student.isActive, Emergency_Contact.firstName AS EfirstName, Emergency_Contact.lastName AS ElastName " +
                         "FROM Student " +
                         "  LEFT JOIN Student_EmergContact ON Student_EmergContact.student_userName = Student.student_userName " +
                         "  LEFT JOIN Emergency_Contact ON Emergency_Contact.emergcon_userName = Student_EmergContact.emergcon_userName " +
                         "WHERE Student.student_userName = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("UserName"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("First Name: " + resultSet.getString("firstName"));
                System.out.println("Last Name: " + resultSet.getString("lastName"));
                System.out.println("Birthday: " + resultSet.getString("birthDate"));
                System.out.println("Phone: " + resultSet.getString("phoneNumber"));
                System.out.println("Email: " + resultSet.getString("email"));
                boolean status = resultSet.getBoolean("isActive");
                System.out.println("Student Status: " + (status ? "Active" : "Inactive"));
                System.out.println("Emergency Contact: " + Util.CheckStringForNull(resultSet.getString("EfirstName")) + " " + Util.CheckStringForNull(resultSet.getString("ElastName")));
                System.out.println("");
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Returns the EmerContact from a specific student if any.
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void getStudentEmerContact(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Emergency_Contact.emergcon_userName, Emergency_Contact.firstName, Emergency_Contact.lastName, Emergency_Contact.phoneNumber, Emergency_Contact.email " +
                         "FROM Emergency_Contact " +
                         "  JOIN Student_EmergContact ON Student_EmergContact.emergcon_userName = Emergency_Contact.emergcon_userName " +
                         "WHERE Student_EmergContact.student_userName = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("UserName"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("UserName: " + resultSet.getString("emergcon_userName"));
                System.out.println("First Name: " + resultSet.getString("firstName"));
                System.out.println("Last Name: " + resultSet.getString("lastName"));
                System.out.println("Phone: " + resultSet.getString("phoneNumber"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("");
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Return Difficulty Level of a specific student
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void getStudentDiffLevel(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT DifficultyLevel.difficultyName " +
                         "FROM Student " +
                         "   JOIN Student_DifficultyLevel ON Student_DifficultyLevel.studentID = Student.studentID " +
                         "   JOIN DifficultyLevel ON DifficultyLevel.difficultyID = Student_DifficultyLevel.difficultyID " +
                         "WHERE Student.student_userName = ? "; 

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("UserName"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("Difficulty Level: " + resultSet.getString("difficultyName"));
                System.out.println("");
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Return Student’s userName
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void getStudent_userName(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Student.student_userName " +
                         "FROM Student " +
                         "WHERE firstName ILIKE ? AND lastName ILIKE ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("FirstName"));
            preparedStatement.setString(2, apiParams.get("LastName"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("UserName: " + resultSet.getString("student_userName"));
                System.out.println("");
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Return Emergency Contact’s username
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void getEmerContact_userName(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Emergency_Contact.emergcon_userName " +
                         "FROM Emergency_Contact " +
                         "WHERE firstName ILIKE ? AND lastName ILIKE ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("FirstName"));
            preparedStatement.setString(2, apiParams.get("LastName"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("UserName: " + resultSet.getString("emergcon_userName"));
                System.out.println("");
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    // // Update Methods for Students
    // /**
    //  * Update Student Difficulty Level
    //  * Pass student's username and new difficulty level
    //  * Returns boolean for success or failure
    //  * @author @n8lookout
    //  * 
    //  * @param apiParams
    //  * @throws SQLException
    //  */
    // public static boolean updateStudentDiffLevel(HashMap<String, String> apiParams) throws SQLException {
    //     // TODO: Test this method
    //     PreparedStatement preparedStatement = null;
    //     try {
    //         // Get DB connection
    //         Connection connection = getConnection();

    //         // SQL PreparedStatement
    //         String sql = "UPDATE Student_DifficultyLevel " +
    //                      "SET difficultyID = (SELECT difficultyID " +
    //                      "                    FROM DifficultyLevel " +
    //                      "                    WHERE difficultyName = ?) " +
    //                      "WHERE studentID = (SELECT studentID " +
    //                      "                   FROM Student " +
    //                      "                   WHERE student_userName = ?)"; 

    //         preparedStatement = connection.prepareStatement(sql);
    //         preparedStatement.setString(1, apiParams.get("NewDifficultyLevel"));
    //         preparedStatement.setString(2, apiParams.get("UserName"));
    //         int rows = preparedStatement.executeUpdate();

    //         if(rows > 0)
    //         {
    //             System.out.println("Student's difficulty level updated successfully !");
    //             System.out.println("");
    //             return true;
    //         }
    //         else
    //         {
    //             System.out.println("Student's difficulty level update failed !");
    //             System.out.println("");
    //             return false;
    //         }             
    //     } catch (SQLException e) {
            
    //         e.printStackTrace();
    //         return false;
    //     } finally {
    //         if (preparedStatement != null) {
    //             preparedStatement.close();
    //         }   
    //     }
    // }
    // /**
    //  * Update Student Emergency Contact
    //  * Pass student's username and new emergency contact username
    //  * Returns boolean for success or failure
    //  * @author @n8lookout
    //  * 
    //  * @param apiParams
    //  * @throws SQLException
    //  */
    // public static boolean updateStudentEmerContact(HashMap<String, String> apiParams) throws SQLException {
    //     PreparedStatement preparedStatement = null;
    //     try {
    //         // Get DB connection
    //         Connection connection = getConnection();

    //         // SQL PreparedStatement
    //         String sql = "UPDATE Student_EmergContact " +
    //                      "SET emergcon_userName = ? " +
    //                      "WHERE studentID = (SELECT studentID " +
    //                      "                   FROM Student " +
    //                      "                   WHERE student_userName = ?)"; 

    //         preparedStatement = connection.prepareStatement(sql);
    //         preparedStatement.setString(1, apiParams.get("NewEmerContact"));
    //         preparedStatement.setString(2, apiParams.get("UserName"));
    //         int rows = preparedStatement.executeUpdate();

    //         if(rows > 0)
    //         {
    //             System.out.println("Student's emergency contact updated successfully !");
    //             System.out.println("");
    //             return true;
    //         }
    //         else
    //         {
    //             System.out.println("Student's emergency contact update failed !");
    //             System.out.println("");
    //             return false;
    //         }             
    //     } catch (SQLException e) {
            
    //         e.printStackTrace();
    //         return false;
    //     } finally {
    //         if (preparedStatement != null) {
    //             preparedStatement.close();
    //         }   
    //     }
    // }
    // /**
    //  * Update Student Status
    //  * Pass student's username and new status
    //  * Returns boolean for success or failure
    //  * @author @n8lookout
    //  * 
    //  * @param apiParams
    //  * @throws SQLException
    //  */
    // public static boolean updateStudentStatus(HashMap<String, String> apiParams) throws SQLException {
    //     PreparedStatement preparedStatement = null;
    //     try {
    //         // Get DB connection
    //         Connection connection = getConnection();

    //         // SQL PreparedStatement
    //         String sql = "UPDATE Student " +
    //                      "SET isActive = ? " +
    //                      "WHERE student_userName = ?"; 

    //         preparedStatement = connection.prepareStatement(sql);
    //         preparedStatement.setBoolean(1, apiParams.get("NewStatus").equals("Active") ? true : false);
    //         preparedStatement.setString(2, apiParams.get("UserName"));
    //         int rows = preparedStatement.executeUpdate();

    //         if(rows > 0)
    //         {
    //             System.out.println("Student's status updated successfully !");
    //             System.out.println("");
    //             return true;
    //         }
    //         else
    //         {
    //             System.out.println("Student's status update failed !");
    //             System.out.println("");
    //             return false;
    //         }             
    //     } catch (SQLException e) {
            
    //         e.printStackTrace();
    //         return false;
    //     } finally {
    //         if (preparedStatement != null) {
    //             preparedStatement.close();
    //         }   
    //     }
    // }
    // /**
    //  * Add Student to a Class
    //  * Use student's username and className
    //  * Check if student is already in class
    //  * Check if student has other classes at the same time
    //  * Returns boolean for success or failure
    //  * @author @n8lookout
    //  * 
    //  * @param apiParams
    //  * @throws SQLException
    //  */
    // public static boolean addStudentToClass(HashMap<String, String> apiParams) throws SQLException {
    //     PreparedStatement preparedStatement = null;
    //     ResultSet resultSet = null;
    //     try {
    //         // Get DB connection
    //         Connection connection = getConnection();

    //         // SQL PreparedStatement
    //         String sql = "SELECT * " +
    //                      "FROM Attendees " +
    //                      "  JOIN Class ON Class.classID = Attendees.classID " +
    //                      "WHERE studentID = (SELECT studentID " +
    //                      "                   FROM Student " +
    //                      "                   WHERE student_userName = ?) " +
    //                      "  AND className = ?"; 

    //         preparedStatement = connection.prepareStatement(sql);
    //         preparedStatement.setString(1, apiParams.get("UserName"));
    //         preparedStatement.setString(2, apiParams.get("ClassName"));
    //         resultSet = preparedStatement.executeQuery();

    //         if(resultSet != null && resultSet.next())
    //         {
    //             System.out.println("Student is already in the class !");
    //             System.out.println("");
    //             return false;
    //         }

    //         sql = "SELECT * " +
    //               "FROM Attendees " +
    //               "  JOIN Class ON Class.classID = Attendees.classID " +
    //               "WHERE studentID = (SELECT studentID " +
    //               "                   FROM Student " +
    //               "                   WHERE student_userName = ?) " +
    //               "  AND (startTime, startTime + interval '1 hour') OVERLAPS (SELECT startTime, startTime + interval '1 hour' " +
    //               "                                                           FROM Class " +
    //               "                                                           WHERE className = ?)"; 

    //         preparedStatement = connection.prepareStatement(sql);
    //         preparedStatement.setString(1, apiParams.get("UserName"));
    //         preparedStatement.setString(2, apiParams.get("ClassName"));
    //         resultSet = preparedStatement.executeQuery();

    //         if(resultSet != null && resultSet.next())
    //         {
    //             System.out.println("Student has another class at the same time !");
    //             System.out.println("");
    //             return false;
    //         }

    //         sql = "INSERT INTO Attendees (studentID, classID) " +
    //               "VALUES ((SELECT studentID " +
    //               "         FROM Student " +
    //               "         WHERE student_userName = ?), " +
    //               "        (SELECT classID " +
    //               "         FROM Class " +
    //               "         WHERE className = ?))"; 

    //         preparedStatement = connection.prepareStatement(sql);
    //         preparedStatement.setString(1, apiParams.get("UserName"));
    //         preparedStatement.setString(2, apiParams.get("ClassName"));
    //         int rows = preparedStatement.executeUpdate();

    //         if(rows > 0)
    //         {
    //             System.out.println("Student added to the class successfully !");
    //             System.out.println("");
    //             return true;
    //         } 
    //         else
    //         {
    //             System.out.println("Student addition to the class failed !");
    //             System.out.println("");
    //             return false;
    //         }
    //     } catch (SQLException e) {                
    //             e.printStackTrace();
    //             return false;
    //         } finally {
    //             if (preparedStatement != null) {
    //                 preparedStatement.close();
    //             }   
                
    //             if (resultSet != null) {
    //                 resultSet.close();
    //             }             
    //         }
    // }

    //////////////////////////////////////////////////////////////
    //                       COACHES                            //
    //////////////////////////////////////////////////////////////

    /**
     * Return list of all coaches
     * @author Anna Rivas
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void listAllCoaches(HashMap<String, String> apiParams) throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL Statement
            String sql = "SELECT Coach.coach_userName, Coach.firstName, Coach.lastName, Coach.phoneNumber, Coach.email " +
                         "FROM Coach " +
                         "ORDER BY Coach.lastName ASC"; 

            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;

                System.out.println("UserName: " + resultSet.getString("coach_userName"));
                System.out.println("First Name: " + resultSet.getString("firstName"));
                System.out.println("Last Name: " + resultSet.getString("lastName"));
                System.out.println("Phone: " + resultSet.getString("phoneNumber"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("");
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }              
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (statement != null) {
                statement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Return list of all coaches that are available on a specific time range
     * @author Anna Rivas
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void listAllAvailCoachByDateRange(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Coach.coach_userName, Coach.firstName, Coach.lastName " +
                         "FROM Coach_Availability " +
                         "  JOIN Coach ON Coach.coachID = Coach_Availability.coachID " +
                         "WHERE Coach_Availability.availStartTime <= TO_TIMESTAMP(?,'YYYY-MM-DD HH24:MI') AND Coach_Availability.availEndTime >= TO_TIMESTAMP(?,'YYYY-MM-DD HH24:MI')";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("StartTime"));
            preparedStatement.setString(2, apiParams.get("EndTime"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("UserName: " + resultSet.getString("coach_userName"));
                System.out.println("First Name: " + resultSet.getString("firstName"));
                System.out.println("Last Name: " + resultSet.getString("lastName"));
                System.out.println("");
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Return Coach Contact Information Phone/Email
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void getCoachInfo(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Coach.firstName, Coach.lastName, Coach.phoneNumber, Coach.email " +
                         "FROM Coach " +
                         "WHERE Coach.coach_userName = ?";                          

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("UserName"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("First Name: " + resultSet.getString("firstName"));
                System.out.println("Last Name: " + resultSet.getString("lastName"));
                System.out.println("Phone: " + resultSet.getString("phoneNumber"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("");
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Returns a specific Coaches Availability times from a specific date
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void getCoachAvail(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Coach_Availability.scheduleName ,Coach_Availability.availStartTime, Coach_Availability.availEndTime " +
                         "FROM Coach_Availability " +
                         "  JOIN Coach ON Coach.coachID = Coach_Availability.coachID " +
                         "WHERE Coach.coach_userName = ? " +
                         " AND (Coach_Availability.availStartTime > TO_DATE(?,'YYYY-MM-DD') AND Coach_Availability.availStartTime < DATE_ADD(TO_DATE(?,'YYYY-MM-DD'), INTERVAL '1 DAY')) " +
                         " AND (Coach_Availability.availEndTime   > TO_DATE(?,'YYYY-MM-DD') AND Coach_Availability.availEndTime   < DATE_ADD(TO_DATE(?,'YYYY-MM-DD'), INTERVAL '1 DAY'))";                              

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("UserName"));
            preparedStatement.setString(2, apiParams.get("Date"));
            preparedStatement.setString(3, apiParams.get("Date"));
            preparedStatement.setString(4, apiParams.get("Date"));
            preparedStatement.setString(5, apiParams.get("Date"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("Availability "+ resultSet.getString("scheduleName") + " from " + resultSet.getString("availStartTime") + " to " + resultSet.getString("availEndTime"));
                System.out.println("");
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Return Coach’s username
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void getCoach_userName(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Coach.coach_userName " +
                         "FROM Coach " +
                         "WHERE firstName ILIKE ? AND lastName ILIKE ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("FirstName"));
            preparedStatement.setString(2, apiParams.get("LastName"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("UserName: " + resultSet.getString("coach_userName"));
                System.out.println("");
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Retrieves the availability schedule for a specific coach on a specific date. 
     * This schedule will only show their available times and exclude the time they are in their assigned classes.
     * 
     * @author Christopher Long
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void showCoachSchedule(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet coachesAvilability = null;
        ResultSet coachesClassesSchedule = null;
        
        try {
            // Get DB connection
            connection = getConnection();

            // Extract parameters
            String coach_userName = apiParams.get("Coach UserName");
            String date = apiParams.get("Date");

            // Prepare SQL statement
            String sql =    "SELECT " +
                                "Coach_Availability.availStartTime::time AS starttime, " +
                                "Coach_Availability.availEndTime::time AS endtime " +
                            "FROM " +
                                "Coach_Availability " +
                                "JOIN Coach ON ( Coach_Availability.coachID = Coach.CoachID ) " +
                            "WHERE " +
                                "Coach.Coach_userName = ? AND " +
                                "Coach_Availability.availStartTime::date = TO_DATE(?,'YYYY-MM-DD') " +
                            "ORDER BY " +
                                "Coach_Availability.availStartTime";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, coach_userName);
            preparedStatement.setString(2, date);

            // Execute query
            coachesAvilability = preparedStatement.executeQuery();

            // Process results
            boolean gotRecords = false;
            String availStartTime = "";
            String availEndTime = "";
            if (coachesAvilability.next()) {
                gotRecords = true;
                availStartTime = coachesAvilability.getString("starttime");
                availEndTime =  coachesAvilability.getString("endtime");
            }

            sql =   "SELECT " +
                        "Class.startTime::time AS starttime, " +
                        "( Class.startTime + Class.duration )::time AS endtime, " +
                        "Class.className " +
                    "FROM " +
                        "Class " +
                        "JOIN Class_Coach ON ( Class.classID = Class_Coach.classID ) " +
                        "JOIN Coach ON ( Class_Coach.coachID = Coach.coachID ) " +
                    "WHERE " +
                        "Coach.Coach_userName = ? AND " +
                        "Class.startTime::date = TO_DATE(?,'YYYY-MM-DD') AND " +
                        "Class.startTime::time > ?::time AND " +
                        "( Class.startTime + Class.duration )::time < ?::time " +
                    "ORDER BY " +
                        "Class.startTime";


            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, coach_userName);
            preparedStatement.setString(2, date);
            preparedStatement.setString(3, availStartTime);
            preparedStatement.setString(4, availEndTime);

            // Execute query
            coachesClassesSchedule = preparedStatement.executeQuery();

            if(gotRecords) {
                System.out.println("Start of Availability: " + availStartTime);


                while(coachesClassesSchedule != null && coachesClassesSchedule.next()) {
                    if(coachesClassesSchedule.getString("starttime").equals(availEndTime)) {
                        System.out.println("UNAVAILABLE - Teaching " + coachesClassesSchedule.getString("className"));
                    } else {
                        System.out.println("===========");
                        System.out.println(coachesClassesSchedule.getString("starttime"));
                        System.out.println("UNAVAILABLE - Teaching " + coachesClassesSchedule.getString("className"));
                    }
                    
                    if(!coachesClassesSchedule.getString("endtime").equals(availEndTime)) {
                        System.out.println(coachesClassesSchedule.getString("endtime"));
                        System.out.println("===========");
                    }
                }

                System.out.println("End of Availability: " + availEndTime);
            }

            if (!gotRecords) {
                System.out.println("No schedule found for the specified coach and date!");
                System.out.println("");
            }
    
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (coachesAvilability != null) {
                coachesAvilability.close();
            }
            if (coachesClassesSchedule != null) {
                coachesClassesSchedule.close();
            }
        }
    }


    //////////////////////////////////////////////////////////////
    //                       CLASSES                           //
    //////////////////////////////////////////////////////////////

    /**
     * Return list of all active classes that don’t have a coach assigned
     * @author Anna Rivas
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void listAllClassesbyMissingCoach(HashMap<String, String> apiParams) throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Class.className, Event.eventName, DifficultyLevel.difficultyName, Class_Status.statusName " +
                         "FROM Class " +
                         "JOIN Event ON Event.eventID = Class.eventID " +
                         "JOIN DifficultyLevel ON DifficultyLevel.difficultyID = Class.difficultyID " +
                         "JOIN Class_Status ON Class_Status.statusID = Class.statusID " +
                         "WHERE classID NOT IN (SELECT classID " +
				                               "FROM  Class_Coach) AND Class.statusID = '1' " +
                                               "OR classID NOT IN (SELECT classID " +
				                               "FROM  Class_Coach) AND Class.statusID = '2' " +
                                               "OR classID NOT IN (SELECT classID " +
				                               "FROM  Class_Coach) AND Class.statusID = '4' " +
                                               "OR classID NOT IN (SELECT classID " +
				                               "FROM  Class_Coach) AND Class.statusID = '5'";


            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;

                System.out.println("Class Name: " + resultSet.getString("className"));
                System.out.println("Event Name: " + resultSet.getString("eventName"));
                System.out.println("Difficulty Level: " + resultSet.getString("difficultyName"));
                System.out.println("Class Status: " + resultSet.getString("statusName"));
                System.out.println("");
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }              
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (statement != null) {
                statement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Return list of all classes on given date
     * @author Anna Rivas
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void listAllClassesByDate(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Class.className, Class.duration, Event.eventName, DifficultyLevel.difficultyName, Class_Status.statusName, Coach.firstName AS CfirstName, Coach.lastName AS ClastName " +
                         "FROM Class " +
                            "JOIN Event ON Event.eventID = Class.eventID " +
                            "JOIN DifficultyLevel ON DifficultyLevel.difficultyID = Class.difficultyID " +
                            "JOIN Class_Status ON Class_Status.statusID = Class.statusID " +
                            "LEFT JOIN Class_Coach ON Class_Coach.classID = Class.classID " +
                            "LEFT JOIN Coach ON Coach.coachID = Class_Coach.coachID " +
                         "WHERE Class.startTime >= TO_DATE(?,'YYYY-MM-DD') AND Class.startTime < DATE_ADD(TO_DATE(?,'YYYY-MM-DD'), INTERVAL '1 DAY'); ";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("Date"));
            preparedStatement.setString(2, apiParams.get("Date"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("Class Name: " + resultSet.getString("className"));
                System.out.println("Event Name: " + resultSet.getString("eventName"));
                System.out.println("Difficulty Level: " + resultSet.getString("difficultyName"));
                System.out.println("Class Status: " + resultSet.getString("statusName"));
                System.out.println("Duration: " + resultSet.getString("duration"));
                System.out.println("Coach: " + resultSet.getString("CfirstName") + " " + resultSet.getString("ClastName"));
                System.out.println("");            
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Return list of active students that are attendees of a specific class
     * @author Anna Rivas
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void listAllClassAttendees(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Student.student_userName, Student.firstName, Student.lastName " +
                         "FROM Student " +
                         "    JOIN Attendees ON Attendees.studentID = Student.studentID " +
                         "    JOIN Class ON Class.classID = Attendees.classID " +
                         "WHERE Class.classID = (SELECT classID " +
                         "                       FROM Class " +
                         "                       WHERE className = ?) AND Student.isActive = TRUE ";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("ClassName"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("UserName: " + resultSet.getString("student_userName"));
                System.out.println("First Name: " + resultSet.getString("firstName"));
                System.out.println("Last Name: " + resultSet.getString("lastName"));
                System.out.println("");            
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Return list of all active classes of a difficulty level
     * @author Anna Rivas
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void listClassByDifficultyLevel(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Class.className, Class.duration, Event.eventName, DifficultyLevel.difficultyName, Class_Status.statusName, Coach.firstName AS CfirstName, Coach.lastName AS ClastName " +
                         "FROM Class " +
                            "JOIN Event ON Event.eventID = Class.eventID " +
                            "JOIN DifficultyLevel ON DifficultyLevel.difficultyID = Class.difficultyID " +
                            "JOIN Class_Status ON Class_Status.statusID = Class.statusID " +
                            "LEFT JOIN Class_Coach ON Class_Coach.classID = Class.classID " +
                            "LEFT JOIN Coach ON Coach.coachID = Class_Coach.coachID " +
                         "WHERE DifficultyLevel.difficultyName = ? AND Class.statusID IN (1, 2, 4, 5) ";
                 
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("DifficultyLevel"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("Class Name: " + resultSet.getString("className"));
                System.out.println("Event Name: " + resultSet.getString("eventName"));
                System.out.println("Difficulty Level: " + resultSet.getString("difficultyName"));
                System.out.println("Class Status: " + resultSet.getString("statusName"));
                System.out.println("Duration: " + resultSet.getString("duration"));
                System.out.println("Coach: " + resultSet.getString("CfirstName") + " " + resultSet.getString("ClastName"));
                System.out.println("");            
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Return list of all active classes on a given event
     * @author Anna Rivas
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void listClassByEvent(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Class.className, Class.startTime, Class.duration, Event.eventName, DifficultyLevel.difficultyName, Class_Status.statusName, Coach.firstName AS CfirstName, Coach.lastName AS ClastName " +
                         "FROM Class " +
                            "JOIN Event ON Event.eventID = Class.eventID " +
                            "JOIN DifficultyLevel ON DifficultyLevel.difficultyID = Class.difficultyID " +
                            "JOIN Class_Status ON Class_Status.statusID = Class.statusID " +
                            "LEFT JOIN Class_Coach ON Class_Coach.classID = Class.classID " +
                            "LEFT JOIN Coach ON Coach.coachID = Class_Coach.coachID " +
                         "WHERE Event.eventName = ? AND Class.statusID IN (1, 2, 4, 5) ";
     
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("EventName"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("Class Name: " + resultSet.getString("className"));
                System.out.println("Event Name: " + resultSet.getString("eventName"));
                System.out.println("Difficulty Level: " + resultSet.getString("difficultyName"));
                System.out.println("Class Status: " + resultSet.getString("statusName"));
                System.out.println("Start Time: " + resultSet.getString("startTime"));
                System.out.println("Duration: " + resultSet.getString("duration"));
                System.out.println("Coach: " + resultSet.getString("CfirstName") + " " + resultSet.getString("ClastName"));
                System.out.println("");             
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Return list of all active classes on a given event and level of difficulty
     * @author Anna Rivas
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void listClassByEventandDiffLevel(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Class.className, Class.startTime, Class.duration, Event.eventName, DifficultyLevel.difficultyName, Class_Status.statusName, Coach.firstName AS CfirstName, Coach.lastName AS ClastName " +
                         "FROM Class " +
                            "JOIN Event ON Event.eventID = Class.eventID " +
                            "JOIN DifficultyLevel ON DifficultyLevel.difficultyID = Class.difficultyID " +
                            "JOIN Class_Status ON Class_Status.statusID = Class.statusID " +
                            "LEFT JOIN Class_Coach ON Class_Coach.classID = Class.classID " +
                            "LEFT JOIN Coach ON Coach.coachID = Class_Coach.coachID " +
                         "WHERE Event.eventName = ? AND DifficultyLevel.difficultyName = ? AND Class.statusID IN (1, 2, 4, 5) ";
     
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("EventName"));
            preparedStatement.setString(2, apiParams.get("DifficultyLevel"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("Class Name: " + resultSet.getString("className"));
                System.out.println("Event Name: " + resultSet.getString("eventName"));
                System.out.println("Difficulty Level: " + resultSet.getString("difficultyName"));
                System.out.println("Class Status: " + resultSet.getString("statusName"));
                System.out.println("Start Time: " + resultSet.getString("startTime"));
                System.out.println("Duration: " + resultSet.getString("duration"));
                System.out.println("Coach: " + resultSet.getString("CfirstName") + " " + resultSet.getString("ClastName"));
                System.out.println("");             
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Retrieves active attendees from a specific class and sends a SMS notification specifying the class status
     * @author Anna Rivas
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void sendStatusNotification(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Class.className, Class_Status.statusName, Student.firstName, Student.lastName " +
                         "FROM Student " +
                         "    JOIN Attendees ON Attendees.studentID = Student.studentID " +
                         "    JOIN Class ON Class.classID = Attendees.classID " +
                         "    JOIN Class_Status ON Class_Status.statusID = Class.statusID " +
                         "WHERE Class.classID = (SELECT classID " +
                         "                       FROM Class " +
                         "                       WHERE className = ?) AND Student.isActive = TRUE ";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("ClassName"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                if(!gotRecords)
                {
                    System.out.println("Sending Notification that class: [" + resultSet.getString("className") + "] has the status of [" + resultSet.getString("statusName") + "] to:");
                    System.out.println("");
                }
                gotRecords = true;
                System.out.println(resultSet.getString("firstName") + " "+ resultSet.getString("lastName"));
                System.out.println("");            
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Return the status of a specific class (Active, Canceled, Completed, etc)
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void getClassStatus(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Class_Status.statusName " +
                         "FROM Class " +
                            "JOIN Class_Status ON Class_Status.statusID = Class.statusID " +
                         "WHERE Class.classID = (SELECT Class.classID " +
                                                "FROM Class " +
                                                "WHERE Class.className = ?) ";
                         
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("ClassName"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("Class Status: " + resultSet.getString("statusName"));
                System.out.println("");            
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Returns the type of event from a specific class
     * @author Nasheeta Lott (with assitance of Anna Rivas)
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void getClassEvent(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // We add a blank line to separate from input parameters
        System.out.println("");

        try {
            // Get DB connection
            Connection connection = getConnection();

            // SQL PreparedStatement
            String sql = "SELECT Event.eventName " +
                         "FROM Class " +
                            "JOIN Event ON Event.eventID = Class.eventID " +
                         "WHERE Class.classID = (SELECT Class.classID " +
                                                "FROM Class " +
                                                "WHERE Class.className = ?) ";
                         
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("ClassName"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("Event Name: " + resultSet.getString("eventName"));
                System.out.println("");            
            }

            if(!gotRecords)
            {
                System.out.println("No results found !");
                System.out.println("");
            }             
        } catch (SQLException e) {
            
            e.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }   
            
            if (resultSet != null) {
                resultSet.close();
            }             
        }
    }

    /**
     * Assigns a coach to a class based on their schedule and class requirements
     * 
     * @author Christopher Long
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static void assignClassCoach(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet coachesAvilability = null;
        ResultSet classTimes = null;
        ResultSet coachesClassesSchedule = null;
        
        try {
            // Get DB connection
            connection = getConnection();

            // Extract parameters
            String coach_userName = apiParams.get("Coach UserName");
            String class_name = apiParams.get("Class Name");

            // Prepare SQL statement
            String sql = "SELECT " +
                            "Coach_Availability.availStartTime AS starttime, " +
                            "Coach_Availability.availEndTime AS endtime " +
                        "FROM " +
                            "Coach_Availability " +
                            "JOIN Coach ON ( Coach_Availability.coachID = Coach.CoachID ) " +
                        "WHERE " +
                            "Coach.Coach_userName = ? AND " +
                            "Coach_Availability.availStartTime::date = ( " +
                                "SELECT " +
                                    "Class.startTime AS starttime " +
                                "FROM " +
                                    "Class " +
                                    "JOIN Class_Coach ON ( Class.classID = Class_Coach.classID ) " +
                                "WHERE " +
                                    "Class.ClassName = ? " +
                                ")::date AND " +
                            "Coach_Availability.availStartTime::time < ( " +
                                "SELECT " +
                                    "Class.startTime AS starttime " +
                                "FROM " +
                                    "Class " +
                                    "JOIN Class_Coach ON ( Class.classID = Class_Coach.classID ) " +
                                "WHERE " +
                                    "Class.ClassName = ? " +
                                ")::time AND " +
                            "Coach_Availability.availEndTime::time > ( " +
                                "SELECT " +
                                    "( Class.startTime + Class.duration )::time AS endtime " +
                                "FROM " +
                                    "Class " +
                                    "JOIN Class_Coach ON ( Class.classID = Class_Coach.classID ) " +
                                "WHERE " +
                                    "Class.ClassName = ? " +
                                ")::time " +
                        "ORDER BY " +
                            "Coach_Availability.availStartTime";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, coach_userName);
            preparedStatement.setString(2, class_name);
            preparedStatement.setString(3, class_name);
            preparedStatement.setString(4, class_name);

            // Execute query
            coachesAvilability = preparedStatement.executeQuery();

            // Process results
            boolean gotRecords = false;
            String availStartTime = "";
            String availEndTime = "";
            if (coachesAvilability.next()) {
                gotRecords = true;
                availStartTime = coachesAvilability.getString("starttime");
                availEndTime =  coachesAvilability.getString("endtime");
            }

            sql =   "SELECT " +
                        "Class.startTime::time AS starttime, " +
                        "( Class.startTime + Class.duration )::time AS endtime, " +
                        "Class.className " +
                    "FROM " +
                        "Class " +
                        "JOIN Class_Coach ON ( Class.classID = Class_Coach.classID ) " +
                        "JOIN Coach ON ( Class_Coach.coachID = Coach.coachID ) " +
                    "WHERE " +
                        "Coach.Coach_userName = ? AND " +
                        "Class.startTime::date = TO_DATE(?,'YYYY-MM-DD') AND " +
                        "Class.startTime::time > ?::time AND " +
                        "( Class.startTime + Class.duration )::time < ?::time " +
                    "ORDER BY " +
                        "Class.startTime";


            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, coach_userName);
            preparedStatement.setString(2, availStartTime);
            preparedStatement.setString(3, availStartTime);
            preparedStatement.setString(4, availEndTime);

            // Execute query
            coachesClassesSchedule = preparedStatement.executeQuery();


            sql =   "SELECT " +
                        "Class.startTime::time AS starttime, " +
                        "( Class.startTime + Class.duration )::time AS endtime " +
                    "FROM " +
                        "Class " +
                    "WHERE " +
                        "Class.ClassName = ? " +
                    "ORDER BY " +
                        "Class.startTime";


            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, class_name);

            // Execute query
            classTimes = preparedStatement.executeQuery();
            String classStartTime = "";
            String classEndTime = "";
            if (classTimes.next()) {
                classStartTime = classTimes.getString("starttime");
                classEndTime =  classTimes.getString("endtime");
            }

            boolean coarseOverlap = true;
            if(gotRecords) {

                while(coachesClassesSchedule != null && coachesClassesSchedule.next()) {

                    LocalTime currentCoarseStarTime = LocalTime.parse(coachesClassesSchedule.getString("starttime"));
                    LocalTime currentCoarseEndTime =  LocalTime.parse(coachesClassesSchedule.getString("endtime"));
                    LocalTime classStartTimeLocal = LocalTime.parse(classStartTime);
                    LocalTime classEndTimeLocal = LocalTime.parse(classEndTime);

                    if (classStartTimeLocal.compareTo(currentCoarseEndTime) < 0 && classStartTimeLocal.compareTo(currentCoarseStarTime) > 0) {
                        coarseOverlap = true;
                        break;
                    } else if (classEndTimeLocal.compareTo(currentCoarseStarTime) > 0 && classEndTimeLocal.compareTo(currentCoarseEndTime) < 0) {
                        coarseOverlap = true;
                        break;
                    } else {
                        coarseOverlap = false;
                    }
                }

            }

            if (!gotRecords) {
                System.out.println("Course Outside of Coaches Availability!");
                System.out.println("");
            } else {

                if(!coarseOverlap) {

                    sql =   "UPDATE Class " +
                            "SET coachID = (" +
                            "SELECT Coach.coachID " +
                            "FROM Coach " +
                            "WHERE Coach.Coach_userName = ?" +
                            ") " +
                            "WHERE Class.ClassName = ?";
                    
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, coach_userName);
                    preparedStatement.setString(2, class_name);

                    connection.setAutoCommit(false);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        connection.commit();
                        System.out.println("Coach Added to Coarse");
                    } else {
                        System.out.println("Failed to Add Coach to Coarse");
                    }

                } else {
                    System.out.println("Overlap with a currently taught course!");
                    System.out.println("");
                }

            }


    
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (coachesAvilability != null) {
                coachesAvilability.close();
            }
            if (coachesClassesSchedule != null) {
                coachesClassesSchedule.close();
            }
        }
    }

}
