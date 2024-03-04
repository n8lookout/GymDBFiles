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
    private static final String URL = "jdbc:postgresql://10.0.0.9:5432/gymnasticsgym"; //"jdbc:postgresql://localhost:5432/GymnasticsGym";
    private static final String USER = "postgres";
    private static final String PASSWORD = "2606";  //"net1net2"

    private static Connection connection = null;


    //////////////////////////////////////////////////////////////
    //                       METHODS                            //
    //////////////////////////////////////////////////////////////
    /**
     * Method to connect to the PostgreSQL database
     * @author The Team
     *
     * @return Connection to the PostgreSQL database
     * @throws SQLException Connection to database times out
     */
    public static Connection getConnection() throws SQLException {
        
        if(connection == null || connection.isClosed())
        {
            Properties connectionProps = new Properties();
            connectionProps.put("user", USER);
            connectionProps.put("password", PASSWORD);
    
            connection = DriverManager.getConnection(URL, connectionProps);
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

    // Students

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

            // SQL query
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

            // SQL query
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

            // SQL query
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

            // SQL query
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

            // SQL query
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

            // SQL query
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

            // SQL query
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

    // Coaches

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

            // SQL query
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

            // SQL query
            String sql = "SELECT Coach.coach_userName, Coach.firstName, Coach.lastName " +
                         "FROM Coach_Availability " +
                         "  JOIN Coach ON Coach.coachID = Coach_Availability.coachID " +
                         "WHERE Coach_Availability.availStartTime <= '" + apiParams.get("StartTime") + "' AND Coach_Availability.availEndTime >= '" + apiParams.get("EndTime") + "'";

            preparedStatement = connection.prepareStatement(sql);
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

            // SQL query
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

            // SQL query
            String sql = "SELECT Coach_Availability.availStartTime, Coach_Availability.availEndTime " +
                         "FROM Coach_Availability " +
                         "  JOIN Coach ON Coach.coachID = Coach_Availability.coachID " +
                         "WHERE Coach.coach_userName = ? " +
                         " AND (Coach_Availability.availStartTime > '" + apiParams.get("Date") + "' AND Coach_Availability.availStartTime < DATE_ADD('" + apiParams.get("Date") + "', INTERVAL '1 DAY')) " +
                         " AND (Coach_Availability.availEndTime   > '" + apiParams.get("Date") + "' AND Coach_Availability.availEndTime   < DATE_ADD('" + apiParams.get("Date") + "', INTERVAL '1 DAY'))";                              

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("UserName"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while(resultSet != null && resultSet.next())
            {
                gotRecords = true;
                System.out.println("Availability from " + resultSet.getString("availStartTime") + " to " + resultSet.getString("availEndTime"));
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

}
