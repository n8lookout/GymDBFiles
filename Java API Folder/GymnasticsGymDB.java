
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/***
 * Gymnastics Gym API that uses JDBC to connect to a PostgreSQL database.
 */
public class GymnasticsGymDB {

    //////////////////////////////////////////////////////////////
    // GLOBAL VARIABLES //
    //////////////////////////////////////////////////////////////

    // JDBC URL, username, and password of PostgreSQL server
    private static final String URL = "jdbc:postgresql://localhost:5432/gymnasticsgym";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Phushkola2$";
    private static Connection connection = null;

    //////////////////////////////////////////////////////////////
    // METHODS //
    //////////////////////////////////////////////////////////////

    /**
     * Method to connect to the PostgreSQL database with retries in case of failure
     * 
     * @author The Team
     *
     * @return Connection to the PostgreSQL database
     * @throws SQLException Connection to database times out
     */
    public static Connection getConnection() throws SQLException {

        if (connection != null && !connection.isValid(10000)) {
            connection = null;
        }

        int connectionAttempts = 0;
        while (connection == null || connection.isClosed()) {
            try {
                connectionAttempts++;
                System.out.println("Creating connection to DB...");

                Properties connectionProps = new Properties();
                connectionProps.put("user", USER);
                connectionProps.put("password", PASSWORD);

                connection = DriverManager.getConnection(URL, connectionProps);

                if (!connection.isValid(100)) {
                    connection = null;
                    throw new SQLException("Connection not Valid");
                }

            } catch (SQLException e) {
                System.out.println("Failed connecting to DB !");

                if (connectionAttempts >= 10) {
                    connection = null;
                    throw e;
                }

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {

                    e1.printStackTrace();
                }
            }
            System.out.println("");
        }

        return connection;
    }

    /**
     * Method to disconnect from the PostgreSQL database
     * 
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

            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////
    // STUDENTS //
    //////////////////////////////////////////////////////////////

    /**Insert newStudentInfo will register new students into the system with their
     * personal and contact information and will automatically set the student’s
     * status to Active
     * @author Noa Uritsky
     * 
     * @param apiParams
     * @return
     * @throws SQLException
     */
    public static boolean addNewStudent(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        Statement lockStatement = null;

        try {
            // Get DB connection
            Connection connection = getConnection();

            // Start a transaction
            connection.setAutoCommit(false);

            // Lock the Student_EmergContact and Student tables
            lockStatement = connection.createStatement();
            lockStatement.execute("LOCK TABLE Student IN EXCLUSIVE MODE");

            // SQL PreparedStatement
            String sql = "INSERT INTO Student(student_Username, firstName, lastName, " +
            "birthDate, phoneNumber, email, isActive) " +
            "Values( ?, ?, ?, ?::date, ?, ?, TRUE)";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("Username"));
            preparedStatement.setString(2, apiParams.get("FirstName"));
            preparedStatement.setString(3, apiParams.get("LastName"));
            preparedStatement.setString(4, apiParams.get("BirthDate"));
            preparedStatement.setString(5, apiParams.get("PhoneNumber"));
            preparedStatement.setString(6, apiParams.get("Email"));
            int rows = preparedStatement.executeUpdate();

            // Commit the transaction
            connection.commit();

            if (rows > 0) {
                System.out.println("New student added successfully !");
                System.out.println("");
                return true;
            } else {
                System.out.println("Adding new student failed !");
                System.out.println("");
                return false;
            }
    } catch (SQLException e) {
        // Rollback the transaction in case of an error
        if (connection != null) {
            connection.rollback();
        }

        e.printStackTrace();
        return false;
    } finally {
        if (preparedStatement != null) {
            preparedStatement.close();
        }
        if (lockStatement != null) {
            lockStatement.close();
        }
        // Reset auto-commit mode
        if (connection != null) {
            connection.setAutoCommit(true);
        }
    }
    }

    /**
     * Returns a list of all students with their information including their
     * usernames. The list is ordered by their last names in aplhabetic order.
     * 
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
            String sql = "SELECT Student.student_userName, Student.firstName, Student.lastName, Student.birthDate, Student.phoneNumber, Student.email, Student.isActive "
                    +
                    "FROM Student " +
                    "ORDER BY lastName ASC";

            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            boolean gotRecords = false;

            while (resultSet != null && resultSet.next()) {
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

            if (!gotRecords) {
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
     * 
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
            String sql = "SELECT Student.student_userName, Student.firstName, Student.lastName, Student.birthDate, Student.phoneNumber, Student.email, Student.isActive, DifficultyLevel.difficultyName "
                    +
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

            while (resultSet != null && resultSet.next()) {
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

            if (!gotRecords) {
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
     * 
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
            String sql = "SELECT Student.student_userName, Student.firstName, Student.lastName, Student.birthDate, Student.phoneNumber, Student.email, Student.isActive "
                    +
                    "FROM Student " +
                    "WHERE isActive = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, apiParams.get("StudentStatus").equals("Active") ? true : false);
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while (resultSet != null && resultSet.next()) {
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

            if (!gotRecords) {
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
     * Returns a list of active classes where a specific student is listed as
     * attendee
     * 
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
            String sql = "SELECT Class.className, Class.startTime, Class.duration, Event.eventName, DifficultyLevel.difficultyName, Class_Status.statusName "
                    +
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

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;
                System.out.println("Class Name: " + resultSet.getString("className"));
                System.out.println("Event Name: " + resultSet.getString("eventName"));
                System.out.println("Difficulty Level: " + resultSet.getString("difficultyName"));
                System.out.println("Class Status: " + resultSet.getString("statusName"));
                System.out.println("Start Time: " + resultSet.getString("startTime"));
                System.out.println("Duration: " + resultSet.getString("duration"));
                System.out.println("");
            }

            if (!gotRecords) {
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
     * 
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
            String sql = "SELECT Student.firstName, Student.lastName, Student.birthDate, Student.phoneNumber, Student.email, Student.isActive, Emergency_Contact.firstName AS EfirstName, Emergency_Contact.lastName AS ElastName "
                    +
                    "FROM Student " +
                    "  LEFT JOIN Student_EmergContact ON Student_EmergContact.student_userName = Student.student_userName "
                    +
                    "  LEFT JOIN Emergency_Contact ON Emergency_Contact.emergcon_userName = Student_EmergContact.emergcon_userName "
                    +
                    "WHERE Student.student_userName = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("UserName"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;
                System.out.println("First Name: " + resultSet.getString("firstName"));
                System.out.println("Last Name: " + resultSet.getString("lastName"));
                System.out.println("Birthday: " + resultSet.getString("birthDate"));
                System.out.println("Phone: " + resultSet.getString("phoneNumber"));
                System.out.println("Email: " + resultSet.getString("email"));
                boolean status = resultSet.getBoolean("isActive");
                System.out.println("Student Status: " + (status ? "Active" : "Inactive"));
                System.out.println("Emergency Contact: " + Util.CheckStringForNull(resultSet.getString("EfirstName"))
                        + " " + Util.CheckStringForNull(resultSet.getString("ElastName")));
                System.out.println("");
            }

            if (!gotRecords) {
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
     * 
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
            String sql = "SELECT Emergency_Contact.emergcon_userName, Emergency_Contact.firstName, Emergency_Contact.lastName, Emergency_Contact.phoneNumber, Emergency_Contact.email "
                    +
                    "FROM Emergency_Contact " +
                    "  JOIN Student_EmergContact ON Student_EmergContact.emergcon_userName = Emergency_Contact.emergcon_userName "
                    +
                    "WHERE Student_EmergContact.student_userName = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("UserName"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;
                System.out.println("UserName: " + resultSet.getString("emergcon_userName"));
                System.out.println("First Name: " + resultSet.getString("firstName"));
                System.out.println("Last Name: " + resultSet.getString("lastName"));
                System.out.println("Phone: " + resultSet.getString("phoneNumber"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("");
            }

            if (!gotRecords) {
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
     * 
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

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;
                System.out.println("Difficulty Level: " + resultSet.getString("difficultyName"));
                System.out.println("");
            }

            if (!gotRecords) {
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
     * 
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

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;
                System.out.println("UserName: " + resultSet.getString("student_userName"));
                System.out.println("");
            }

            if (!gotRecords) {
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
     * 
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

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;
                System.out.println("UserName: " + resultSet.getString("emergcon_userName"));
                System.out.println("");
            }

            if (!gotRecords) {
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

    // Update Methods for Students
    /**
     * Update Student Difficulty Level
     * Pass student's username and new difficulty level
     * Returns boolean for success or failure
     * 
     * @author @n8lookout
     * 
     * @param apiParams
     * @throws SQLException
     */
    public static boolean updateStudentDiffLevel(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        Statement lockStatement = null;
        try {
            // Get DB connection
            Connection connection = getConnection();

            // Start a transaction
            connection.setAutoCommit(false);

            // Lock the Student_DifficultyLevel and DifficultyLevel tables
            lockStatement = connection.createStatement();
            lockStatement.execute("LOCK TABLE Student_DifficultyLevel, DifficultyLevel IN EXCLUSIVE MODE");

            // SQL PreparedStatement
            String sql = "UPDATE Student_DifficultyLevel " +
                    "SET difficultyID = (SELECT difficultyID " +
                    "                    FROM DifficultyLevel " +
                    "                    WHERE difficultyName = ?) " +
                    "WHERE studentID = (SELECT studentID " +
                    "                   FROM Student " +
                    "                   WHERE student_userName = ?)";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("DifficultyLevel"));
            preparedStatement.setString(2, apiParams.get("UserName"));
            int rows = preparedStatement.executeUpdate();

            // Commit the transaction
            connection.commit();

            if (rows > 0) {
                System.out.println("Student's difficulty level updated successfully !");
                System.out.println("");
                return true;
            } else {
                System.out.println("Student's difficulty level update failed !");
                System.out.println("");
                return false;
            }
        } catch (SQLException e) {
            // Rollback the transaction in case of an error
            if (connection != null) {
                connection.rollback();
            }

            e.printStackTrace();
            return false;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (lockStatement != null) {
                lockStatement.close();
            }
            // Reset auto-commit mode
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    /**
     * Update Student Emergency Contact
     * Pass student's username and new emergency contact username
     * Returns boolean for success or failure
     * 
     * @author @n8lookout
     * 
     * @param apiParams
     * @throws SQLException
     */
    public static boolean updateStudentEmerContact(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        Statement lockStatement = null;
        try {
            // Get DB connection
            Connection connection = getConnection();

            // Start a transaction
            connection.setAutoCommit(false);

            // Lock the Student_EmergContact and Student tables
            lockStatement = connection.createStatement();
            lockStatement.execute("LOCK TABLE Student_EmergContact, Student IN EXCLUSIVE MODE");

            // SQL PreparedStatement
            String sql = "UPDATE Student_EmergContact " +
                    "SET emergcon_userName = ? " +
                    "WHERE student_userName = ? ";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("NewEmerContactUserName"));
            preparedStatement.setString(2, apiParams.get("UserName"));
            int rows = preparedStatement.executeUpdate();

            // Commit the transaction
            connection.commit();

            if (rows > 0) {
                System.out.println("Student's emergency contact updated successfully !");
                System.out.println("");
                return true;
            } else {
                System.out.println("Student's emergency contact update failed !");
                System.out.println("");
                return false;
            }
        } catch (SQLException e) {
            // Rollback the transaction in case of an error
            if (connection != null) {
                connection.rollback();
            }

            e.printStackTrace();
            return false;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (lockStatement != null) {
                lockStatement.close();
            }
            // Reset auto-commit mode
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    /**
     * Update Student Status
     * Pass student's username and new status
     * Returns boolean for success or failure
     * 
     * @author @n8lookout
     * 
     * @param apiParams
     * @throws SQLException
     */
    public static boolean updateStudentStatus(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        Statement lockStatement = null;
        try {
            // Get DB connection
            Connection connection = getConnection();

            // Start a transaction
            connection.setAutoCommit(false);

            // Lock the Student table
            lockStatement = connection.createStatement();
            lockStatement.execute("LOCK TABLE Student IN EXCLUSIVE MODE");

            // SQL PreparedStatement
            String sql = "UPDATE Student " +
                    "SET isActive = ? " +
                    "WHERE student_userName = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, apiParams.get("StudentStatus").equals("Active") ? true : false);
            preparedStatement.setString(2, apiParams.get("UserName"));
            int rows = preparedStatement.executeUpdate();

            // Commit the transaction
            connection.commit();

            if (rows > 0) {
                System.out.println("Student's status updated successfully !");
                System.out.println("");
                return true;
            } else {
                System.out.println("Student's status update failed !");
                System.out.println("");
                return false;
            }
        } catch (SQLException e) {
            // Rollback the transaction in case of an error
            if (connection != null) {
                connection.rollback();
            }

            e.printStackTrace();
            return false;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (lockStatement != null) {
                lockStatement.close();
            }
            // Reset auto-commit mode
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    /**
     * Add Student to a Class
     * Use student's username and className
     * Check if student is already in class
     * Check if student has other classes at the same time
     * Returns boolean for success or failure
     * 
     * @author @n8lookout
     * 
     * @param apiParams
     * @throws SQLException
     */
    public static boolean addStudentToClass(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Statement lockStatement = null;
        try {
            // Get DB connection
            Connection connection = getConnection();

            // Start a transaction
            connection.setAutoCommit(false);

            // Lock the Attendees, Class, and Student tables
            lockStatement = connection.createStatement();
            lockStatement.execute("LOCK TABLE Attendees, Class, Student IN ACCESS EXCLUSIVE MODE");

            // SQL PreparedStatement
            String sql = "SELECT * " +
                    "FROM Attendees " +
                    "  JOIN Class ON Class.classID = Attendees.classID " +
                    "WHERE studentID = (SELECT studentID " +
                    "                   FROM Student " +
                    "                   WHERE student_userName = ?) " +
                    "  AND className = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("UserName"));
            preparedStatement.setString(2, apiParams.get("ClassName"));
            // System.out.println(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet != null && resultSet.next()) {
                System.out.println("Student is already in the class !");
                System.out.println("");
                return false;
            }

            sql = "SELECT * " +
                    "FROM Attendees " +
                    "JOIN Class ON Class.classID = Attendees.classID " +
                    "WHERE studentID = (SELECT studentID " +
                    "                   FROM Student " +
                    "                   WHERE student_userName = ?) " +
                    "AND EXISTS (SELECT 1 " +
                    "            FROM Class AS Class2 " +
                    "            WHERE Class2.className = ? " +
                    "            AND (Class.startTime, Class.startTime + interval '1 hour') OVERLAPS " +
                    "(Class2.startTime, Class2.startTime + interval '1 hour'))";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("UserName"));
            preparedStatement.setString(2, apiParams.get("ClassName"));
            resultSet = preparedStatement.executeQuery();

            if (resultSet != null && resultSet.next()) {
                System.out.println("Student has another class at the same time !");
                System.out.println("");
                return false;
            }

            sql = "INSERT INTO Attendees (studentID, classID) " +
                    "VALUES ((SELECT studentID " +
                    "         FROM Student " +
                    "         WHERE student_userName = ?), " +
                    "        (SELECT classID " +
                    "         FROM Class " +
                    "         WHERE className = ?))";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("UserName"));
            preparedStatement.setString(2, apiParams.get("ClassName"));
            int rows = preparedStatement.executeUpdate();

            // Commit the transaction
            connection.commit();

            if (rows > 0) {
                System.out.println("Student added to the class successfully !");
                System.out.println("");
                return true;
            } else {
                System.out.println("Student addition to the class failed !");
                System.out.println("");
                return false;
            }
        } catch (SQLException e) {
            // Rollback the transaction in case of an error
            if (connection != null) {
                connection.rollback();
            }

            e.printStackTrace();
            return false;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (lockStatement != null) {
                lockStatement.close();
            }
            // Reset auto-commit mode
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    //////////////////////////////////////////////////////////////
    // COACHES //
    //////////////////////////////////////////////////////////////

    /**Insert newCoachInfo will add a new coach to the system with their contact information
     * @author Noa Uritsky
     * 
     * @param apiParams
     * @return
     * @throws SQLException
     */
    public static boolean addNewCoach(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        Statement lockStatement = null;

        try {
            // Get DB connection
            Connection connection = getConnection();

            // Start a transaction
            connection.setAutoCommit(false);

            // Lock the Student_EmergContact and Student tables
            lockStatement = connection.createStatement();
            lockStatement.execute("LOCK TABLE Coach IN EXCLUSIVE MODE");

            // SQL PreparedStatement
            String sql = "INSERT INTO Coach( Coach_Username, firstName, " +
            "lastName, Phonenumber, email) " +
            "Values( ?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("Username"));
            preparedStatement.setString(2, apiParams.get("FirstName"));
            preparedStatement.setString(3, apiParams.get("LastName"));
            preparedStatement.setString(4, apiParams.get("PhoneNumber"));
            preparedStatement.setString(5, apiParams.get("Email"));
            int rows = preparedStatement.executeUpdate();

            // Commit the transaction
            connection.commit();

            if (rows > 0) {
                System.out.println("New coach added successfully !");
                System.out.println("");
                return true;
            } else {
                System.out.println("Adding new coach failed !");
                System.out.println("");
                return false;
            }
    } catch (SQLException e) {
        // Rollback the transaction in case of an error
        if (connection != null) {
            connection.rollback();
        }

        e.printStackTrace();
        return false;
    } finally {
        if (preparedStatement != null) {
            preparedStatement.close();
        }
        if (lockStatement != null) {
            lockStatement.close();
        }
        // Reset auto-commit mode
        if (connection != null) {
            connection.setAutoCommit(true);
        }
    }
    }

    /**Create a specific coach’s schedule based on the availStartTime and
     * availEndTime that the coach provided. Meaning, a coach schedule is
     * made up from the available times that the coach enters in the program.
     * @author Noa Uritsky
     * 
     * @param apiParams
     * @return
     * @throws SQLException
     */
    public static boolean addNewCoachAvailability(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        Statement lockStatement = null;

        try {
            // Get DB connection
            Connection connection = getConnection();

            // Start a transaction
            connection.setAutoCommit(false);

            // Lock the Student_EmergContact and Student tables
            lockStatement = connection.createStatement();
            lockStatement.execute("LOCK TABLE Coach, Coach_Availability IN EXCLUSIVE MODE");

            // SQL PreparedStatement
            String sql = "INSERT INTO Coach_Availability( coachID, scheduleName, " +
            "availstartTime, availendTime) " +
            "Values( (SELECT coachID FROM Coach WHERE Coach_Username = ?), ?, " + 
             "?::timestamp, ?::timestamp)";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("Username"));
            preparedStatement.setString(2, apiParams.get("ScheduleName"));
            preparedStatement.setString(3, apiParams.get("StartTime"));
            preparedStatement.setString(4, apiParams.get("EndTime"));
            int rows = preparedStatement.executeUpdate();

            // Commit the transaction
            connection.commit();

            if (rows > 0) {
                System.out.println("New coach availability added successfully !");
                System.out.println("");
                return true;
            } else {
                System.out.println("Adding new coach availability failed !");
                System.out.println("");
                return false;
            }
    } catch (SQLException e) {
        // Rollback the transaction in case of an error
        if (connection != null) {
            connection.rollback();
        }

        e.printStackTrace();
        return false;
    } finally {
        if (preparedStatement != null) {
            preparedStatement.close();
        }
        if (lockStatement != null) {
            lockStatement.close();
        }
        // Reset auto-commit mode
        if (connection != null) {
            connection.setAutoCommit(true);
        }
    }
    }

    /**
     * Return list of all coaches
     * 
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
            String sql = "SELECT Coach.coach_userName, Coach.firstName, Coach.lastName, Coach.phoneNumber, Coach.email "
                    +
                    "FROM Coach " +
                    "ORDER BY Coach.lastName ASC";

            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            boolean gotRecords = false;

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;

                System.out.println("UserName: " + resultSet.getString("coach_userName"));
                System.out.println("First Name: " + resultSet.getString("firstName"));
                System.out.println("Last Name: " + resultSet.getString("lastName"));
                System.out.println("Phone: " + resultSet.getString("phoneNumber"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("");
            }

            if (!gotRecords) {
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
     * 
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

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;
                System.out.println("UserName: " + resultSet.getString("coach_userName"));
                System.out.println("First Name: " + resultSet.getString("firstName"));
                System.out.println("Last Name: " + resultSet.getString("lastName"));
                System.out.println("");
            }

            if (!gotRecords) {
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
     * 
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

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;
                System.out.println("First Name: " + resultSet.getString("firstName"));
                System.out.println("Last Name: " + resultSet.getString("lastName"));
                System.out.println("Phone: " + resultSet.getString("phoneNumber"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("");
            }

            if (!gotRecords) {
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
     * 
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
            String sql = "SELECT Coach_Availability.scheduleName ,Coach_Availability.availStartTime, Coach_Availability.availEndTime "
                    +
                    "FROM Coach_Availability " +
                    "  JOIN Coach ON Coach.coachID = Coach_Availability.coachID " +
                    "WHERE Coach.coach_userName = ? " +
                    " AND (Coach_Availability.availStartTime > TO_DATE(?,'YYYY-MM-DD') AND Coach_Availability.availStartTime < DATE_ADD(TO_DATE(?,'YYYY-MM-DD'), INTERVAL '1 DAY')) "
                    +
                    " AND (Coach_Availability.availEndTime   > TO_DATE(?,'YYYY-MM-DD') AND Coach_Availability.availEndTime   < DATE_ADD(TO_DATE(?,'YYYY-MM-DD'), INTERVAL '1 DAY'))";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("UserName"));
            preparedStatement.setString(2, apiParams.get("Date"));
            preparedStatement.setString(3, apiParams.get("Date"));
            preparedStatement.setString(4, apiParams.get("Date"));
            preparedStatement.setString(5, apiParams.get("Date"));
            resultSet = preparedStatement.executeQuery();

            boolean gotRecords = false;

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;
                System.out.println("Availability " + resultSet.getString("scheduleName") + " from "
                        + resultSet.getString("availStartTime") + " to " + resultSet.getString("availEndTime"));
                System.out.println("");
            }

            if (!gotRecords) {
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
     * 
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

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;
                System.out.println("UserName: " + resultSet.getString("coach_userName"));
                System.out.println("");
            }

            if (!gotRecords) {
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
     * This schedule will only show their available times and exclude the time they
     * are in their assigned classes.
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
            String sql = "SELECT " +
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
            List<String> availStartTime = new ArrayList<>();
            List<String> availEndTime = new ArrayList<>();
            while (coachesAvilability.next()) {
                gotRecords = true;
                availStartTime.add(coachesAvilability.getString("starttime"));
                availEndTime.add(coachesAvilability.getString("endtime"));
            }

            sql = "SELECT " +
                    "Class.startTime::time AS starttime, " +
                    "( Class.startTime + Class.duration )::time AS endtime, " +
                    "Class.className " +
                    "FROM " +
                    "Class " +
                    "JOIN Class_Coach ON ( Class.classID = Class_Coach.classID ) " +
                    "JOIN Coach ON ( Class_Coach.coachID = Coach.coachID ) " +
                    "WHERE " +
                    "Coach.Coach_userName = ? AND " +
                    "Class.startTime::date = TO_DATE(?,'YYYY-MM-DD') " +
                    "ORDER BY " +
                    "Class.startTime";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, coach_userName);
            preparedStatement.setString(2, date);

            // Execute query
            coachesClassesSchedule = preparedStatement.executeQuery();

            List<String> classStartTime = new ArrayList<>();
            List<String> classEndTime = new ArrayList<>();
            List<String> className = new ArrayList<>();
            while (coachesClassesSchedule.next()) {
                classStartTime.add(coachesClassesSchedule.getString("starttime"));
                className.add(coachesClassesSchedule.getString("className"));
                classEndTime.add(coachesClassesSchedule.getString("endtime"));
            }

            if (gotRecords) {
                int j = 0;
                for (int i = 0; i < availStartTime.size(); i++) {

                    if (i > 0) {
                        System.out.println("UNAVAILABLE");
                    }

                    System.out.println(availStartTime.get(i) + " Start of Availability");

                    for (; j < classStartTime.size(); j++) {

                        if (classEndTime.get(j).compareTo(availEndTime.get(i)) > 0) {
                            break;
                        }
                        System.out.println("=========");
                        System.out.println("Teaching: " + className.get(j));
                        System.out.println("Start: " + classStartTime.get(j));
                        System.out.println("End: " + classEndTime.get(j));
                        System.out.println("=========");
                    }

                    System.out.println(availEndTime.get(i) + " End of Availability");
                }

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

    /**
     * Change Coach schedule - coach_userName, scheduleName, availStartTime,
     * availEndTime
     * Check for class conflicts - update class status to Postponed if necessary
     * Returns boolean for success or failure
     * 
     * @author Nathaniel Fincham
     * @param apiParams
     * @throws SQLException
     */
    public static boolean changeCoachSchedule(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        Statement lockStatement = null;
        try {
            // Get DB connection
            Connection connection = getConnection();

            // Start a transaction
            connection.setAutoCommit(false);

            // Lock the Coach_Availability table
            lockStatement = connection.createStatement();
            lockStatement.execute("LOCK TABLE Coach_Availability IN EXCLUSIVE MODE");

            String checkClassesSql = "SELECT * FROM Class " +
                    "JOIN Class_Coach ON Class.classID = Class_Coach.classID " +
                    "JOIN coach_availability ON Class_Coach.coachID = coach_availability.coachID " +
                    "WHERE Class_Coach.coachID = (SELECT coachID FROM Coach WHERE coach_userName = ?) " +
                    "AND coach_availability.scheduleName = ? " +
                    "AND (Class.startTime, Class.startTime + interval '1 hour') OVERLAPS " +
                    "(coach_availability.availStartTime, coach_availability.availEndTime)";
            preparedStatement = connection.prepareStatement(checkClassesSql);
            preparedStatement.setString(1, apiParams.get("UserName"));
            preparedStatement.setString(2, apiParams.get("scheduleName"));
            ResultSet classesResultSet = preparedStatement.executeQuery();
            // For each class that might be affected, check if it overlaps with the new
            // schedule
            while (classesResultSet.next()) {
                // If a class does overlap, update its status to 'Postponed'
                String updateClassSql = "UPDATE Class SET " +
                        "statusID = (SELECT statusID FROM Class_Status WHERE statusName = 'Postponed') " +
                        " WHERE classID = ?";
                preparedStatement = connection.prepareStatement(updateClassSql);
                preparedStatement.setInt(1, classesResultSet.getInt("classID"));
                preparedStatement.executeUpdate();
            }

            // SQL PreparedStatement - Updating Coach Schedule
            String sql = "UPDATE Coach_Availability " +
                    "SET availStartTime = TO_TIMESTAMP(?,'YYYY-MM-DD HH24:MI'), " +
                    "    availEndTime = TO_TIMESTAMP(?,'YYYY-MM-DD HH24:MI') " +
                    "WHERE coachID = (SELECT coachID " +
                    "                 FROM Coach " +
                    "                 WHERE coach_userName = ?) " +
                    "  AND scheduleName = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("StartTime"));
            preparedStatement.setString(2, apiParams.get("EndTime"));
            preparedStatement.setString(3, apiParams.get("UserName"));
            preparedStatement.setString(4, apiParams.get("scheduleName"));
            int rows = preparedStatement.executeUpdate();

            // Commit the transaction
            connection.commit();

            if (rows > 0) {
                System.out.println("Coach's schedule updated successfully !");
                System.out.println("");
                return true;
            } else {
                System.out.println("Coach's schedule update failed !");
                System.out.println("");
                return false;
            }
        } catch (SQLException e) {
            // Rollback the transaction in case of an error
            if (connection != null) {
                connection.rollback();
            }

            e.printStackTrace();
            return false;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (lockStatement != null) {
                lockStatement.close();
            }
            // Reset auto-commit mode
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    //////////////////////////////////////////////////////////////
    // CLASSES //
    //////////////////////////////////////////////////////////////

    /**Insert newClassInfointo system which allows for scheduling and categorizing 
     * based on type and difficulty
     * @author Noa Uritsky
     * 
     * @param apiParams
     * @throws SQLException
     */
    public static boolean addNewClass(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        Statement lockStatement = null;

        try {
            // Get DB connection
            Connection connection = getConnection();

            // Start a transaction
            connection.setAutoCommit(false);

            // Lock the Student_EmergContact and Student tables
            lockStatement = connection.createStatement();
            lockStatement.execute("LOCK TABLE Class, DifficultyLevel, Event, Class_Status IN EXCLUSIVE MODE");

            // SQL PreparedStatement
            String sql = "INSERT INTO Class(className, startTime, duration, eventID, " +
            "difficultyID, statusID) " +
            "Values( ?, TO_TIMESTAMP(?,'YYYY-MM-DD HH24:MI'), ?::interval," +
            "(SELECT eventID FROM Event WHERE eventName = ?), " +
            "(SELECT difficultyID FROM DifficultyLevel WHERE difficultyName = ?), "+
            "(SELECT statusID FROM Class_Status WHERE statusName = ?)) ";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("className"));
            preparedStatement.setString(2, apiParams.get("startTime"));
            preparedStatement.setString(3, apiParams.get("duration"));
            preparedStatement.setString(4, apiParams.get("event"));
            preparedStatement.setString(5, apiParams.get("difficulty"));
            preparedStatement.setString(6, apiParams.get("status"));
            int rows = preparedStatement.executeUpdate();

            // Commit the transaction
            connection.commit();

            if (rows > 0) {
                System.out.println("New class added successfully !");
                System.out.println("");
                return true;
            } else {
                System.out.println("Adding new class failed !");
                System.out.println("");
                return false;
            }
    } catch (SQLException e) {
        // Rollback the transaction in case of an error
        if (connection != null) {
            connection.rollback();
        }

        e.printStackTrace();
        return false;
    } finally {
        if (preparedStatement != null) {
            preparedStatement.close();
        }
        if (lockStatement != null) {
            lockStatement.close();
        }
        // Reset auto-commit mode
        if (connection != null) {
            connection.setAutoCommit(true);
        }
    }
}

    /**
     * Return list of all active classes that don’t have a coach assigned
     * 
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
            String sql = "SELECT Class.className, Event.eventName, DifficultyLevel.difficultyName, Class_Status.statusName "
                    +
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

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;

                System.out.println("Class Name: " + resultSet.getString("className"));
                System.out.println("Event Name: " + resultSet.getString("eventName"));
                System.out.println("Difficulty Level: " + resultSet.getString("difficultyName"));
                System.out.println("Class Status: " + resultSet.getString("statusName"));
                System.out.println("");
            }

            if (!gotRecords) {
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
     * 
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
            String sql = "SELECT Class.className, Class.duration, Event.eventName, DifficultyLevel.difficultyName, Class_Status.statusName, Coach.firstName AS CfirstName, Coach.lastName AS ClastName "
                    +
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

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;
                System.out.println("Class Name: " + resultSet.getString("className"));
                System.out.println("Event Name: " + resultSet.getString("eventName"));
                System.out.println("Difficulty Level: " + resultSet.getString("difficultyName"));
                System.out.println("Class Status: " + resultSet.getString("statusName"));
                System.out.println("Duration: " + resultSet.getString("duration"));
                System.out.println(
                        "Coach: " + resultSet.getString("CfirstName") + " " + resultSet.getString("ClastName"));
                System.out.println("");
            }

            if (!gotRecords) {
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
     * 
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

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;
                System.out.println("UserName: " + resultSet.getString("student_userName"));
                System.out.println("First Name: " + resultSet.getString("firstName"));
                System.out.println("Last Name: " + resultSet.getString("lastName"));
                System.out.println("");
            }

            if (!gotRecords) {
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
     * 
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
            String sql = "SELECT Class.className, Class.duration, Event.eventName, DifficultyLevel.difficultyName, Class_Status.statusName, Coach.firstName AS CfirstName, Coach.lastName AS ClastName "
                    +
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

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;
                System.out.println("Class Name: " + resultSet.getString("className"));
                System.out.println("Event Name: " + resultSet.getString("eventName"));
                System.out.println("Difficulty Level: " + resultSet.getString("difficultyName"));
                System.out.println("Class Status: " + resultSet.getString("statusName"));
                System.out.println("Duration: " + resultSet.getString("duration"));
                System.out.println(
                        "Coach: " + resultSet.getString("CfirstName") + " " + resultSet.getString("ClastName"));
                System.out.println("");
            }

            if (!gotRecords) {
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
     * 
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
            String sql = "SELECT Class.className, Class.startTime, Class.duration, Event.eventName, DifficultyLevel.difficultyName, Class_Status.statusName, Coach.firstName AS CfirstName, Coach.lastName AS ClastName "
                    +
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

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;
                System.out.println("Class Name: " + resultSet.getString("className"));
                System.out.println("Event Name: " + resultSet.getString("eventName"));
                System.out.println("Difficulty Level: " + resultSet.getString("difficultyName"));
                System.out.println("Class Status: " + resultSet.getString("statusName"));
                System.out.println("Start Time: " + resultSet.getString("startTime"));
                System.out.println("Duration: " + resultSet.getString("duration"));
                System.out.println(
                        "Coach: " + resultSet.getString("CfirstName") + " " + resultSet.getString("ClastName"));
                System.out.println("");
            }

            if (!gotRecords) {
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
     * 
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
            String sql = "SELECT Class.className, Class.startTime, Class.duration, Event.eventName, DifficultyLevel.difficultyName, Class_Status.statusName, Coach.firstName AS CfirstName, Coach.lastName AS ClastName "
                    +
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

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;
                System.out.println("Class Name: " + resultSet.getString("className"));
                System.out.println("Event Name: " + resultSet.getString("eventName"));
                System.out.println("Difficulty Level: " + resultSet.getString("difficultyName"));
                System.out.println("Class Status: " + resultSet.getString("statusName"));
                System.out.println("Start Time: " + resultSet.getString("startTime"));
                System.out.println("Duration: " + resultSet.getString("duration"));
                System.out.println(
                        "Coach: " + resultSet.getString("CfirstName") + " " + resultSet.getString("ClastName"));
                System.out.println("");
            }

            if (!gotRecords) {
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
     * Retrieves active attendees from a specific class and sends a SMS notification
     * specifying the class status
     * 
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

            while (resultSet != null && resultSet.next()) {
                if (!gotRecords) {
                    System.out.println("Sending Notification that class: [" + resultSet.getString("className")
                            + "] has the status of [" + resultSet.getString("statusName") + "] to:");
                    System.out.println("");
                }
                gotRecords = true;
                System.out.println(resultSet.getString("firstName") + " " + resultSet.getString("lastName"));
                System.out.println("");
            }

            if (!gotRecords) {
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
     * 
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

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;
                System.out.println("Class Status: " + resultSet.getString("statusName"));
                System.out.println("");
            }

            if (!gotRecords) {
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
     * 
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

            while (resultSet != null && resultSet.next()) {
                gotRecords = true;
                System.out.println("Event Name: " + resultSet.getString("eventName"));
                System.out.println("");
            }

            if (!gotRecords) {
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
        ResultSet coachesClassesSchedule = null;
        ResultSet selectedClassTime = null;

        try {
            // Get DB connection
            connection = getConnection();

            // Extract parameters
            String coach_userName = apiParams.get("Coach UserName");
            String class_name = apiParams.get("Class Name");

            String sql = "SELECT " +
                    "Class.startTime::time AS startTime, " +
                    "( Class.startTime + Class.duration )::time AS endTime, " +
                    "Class.startTime::date AS date,  " +
                    "Class.ClassID " +
                    "FROM " +
                    "Class " +
                    "WHERE " +
                    "Class.className = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, class_name);

            // Execute query
            selectedClassTime = preparedStatement.executeQuery();
            boolean foundClass = false;
            String selectedClassStartTime = "";
            String selectedClassEndTime = "";
            String date = "";
            int classID = 0;
            if (selectedClassTime.next()) {
                foundClass = true;
                selectedClassStartTime = selectedClassTime.getString("starttime");
                selectedClassEndTime = selectedClassTime.getString("endtime");
                date = selectedClassTime.getString("date");
                classID = selectedClassTime.getInt("classid");
            }

            // Prepare SQL statement
            sql = "SELECT " +
                    "Coach_Availability.availStartTime::time AS starttime, " +
                    "Coach_Availability.availEndTime::time AS endtime, " +
                    "Coach.CoachID " +
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
            boolean coachAvailableForClass = false;
            List<String> availStartTime = new ArrayList<>();
            List<String> availEndTime = new ArrayList<>();
            int coachID = 0;
            while (coachesAvilability.next()) {
                coachAvailableForClass = true;
                availStartTime.add(coachesAvilability.getString("starttime"));
                availEndTime.add(coachesAvilability.getString("endtime"));
                coachID = coachesAvilability.getInt("coachid");
            }

            sql = "SELECT " +
                    "Class.startTime::time AS starttime, " +
                    "( Class.startTime + Class.duration )::time AS endtime, " +
                    "Class.className " +
                    "FROM " +
                    "Class " +
                    "JOIN Class_Coach ON ( Class.classID = Class_Coach.classID ) " +
                    "JOIN Coach ON ( Class_Coach.coachID = Coach.coachID ) " +
                    "WHERE " +
                    "Coach.Coach_userName = ? AND " +
                    "Class.startTime::date = TO_DATE(?,'YYYY-MM-DD') " +
                    "ORDER BY " +
                    "Class.startTime";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, coach_userName);
            preparedStatement.setString(2, date);

            // Execute query
            coachesClassesSchedule = preparedStatement.executeQuery();

            List<String> classStartTime = new ArrayList<>();
            List<String> classEndTime = new ArrayList<>();
            List<String> className = new ArrayList<>();
            while (coachesClassesSchedule.next()) {
                classStartTime.add(coachesClassesSchedule.getString("starttime"));
                className.add(coachesClassesSchedule.getString("className"));
                classEndTime.add(coachesClassesSchedule.getString("endtime"));
            }

            if (!foundClass) {
                System.out.println("Selected Class not found");
            }

            if (foundClass && !coachAvailableForClass) {
                System.out.println("Class outside of Coaches Availability");
            }

            if (coachAvailableForClass) {
                int j = 0;
                for (int i = 0; i < availStartTime.size() && coachAvailableForClass; i++) {

                    if (selectedClassStartTime.compareTo(availStartTime.get(0)) < 0) {
                        System.out.println("Selected Class is before Coach Availability");
                        coachAvailableForClass = false;
                        break;
                    }

                    for (; j < classStartTime.size(); j++) {

                        if (classEndTime.get(j).compareTo(availEndTime.get(i)) > 0) {
                            break;
                        }

                        if ((selectedClassEndTime.compareTo(classStartTime.get(j)) > 0 &&
                                selectedClassEndTime.compareTo(classEndTime.get(j)) < 0) ||
                                (selectedClassStartTime.compareTo(classStartTime.get(j)) > 0 &&
                                        selectedClassStartTime.compareTo(classEndTime.get(j)) < 0)
                                ||
                                selectedClassStartTime.compareTo(classStartTime.get(j)) == 0 ||
                                selectedClassEndTime.compareTo(classEndTime.get(j)) == 0) {
                            System.out.println("Selected Class overlaps with a Class already assigned to the Coach");
                            coachAvailableForClass = false;
                            break;
                        }

                    }

                    if (selectedClassEndTime.compareTo(availEndTime.get(availStartTime.size() - 1)) > 0) {
                        System.out.println("Selected Class is after Coach Availability");
                        coachAvailableForClass = false;
                        break;
                    }

                }

            }

            if (!coachAvailableForClass) {
                System.out.println("Coach not assigned to Class!");
                System.out.println("");
            } else {

                sql = "INSERT INTO Class_Coach (coachID,classID ) " +
                        "VALUES (?, ?)";

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, coachID);
                preparedStatement.setInt(2, classID);

                System.out.println("We can add the coach!");
                connection.setAutoCommit(false);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    connection.commit();
                    System.out.println("Coach assigned to Coarse");
                } else {
                    System.out.println("Failed to assign Coach to Coarse");
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

    /**
     * Updates the status of a specific class
     * 
     * @author Nathaniel Fincham
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static boolean updateClassStatus(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        Statement lockStatement = null;
        try {
            // Get DB connection
            Connection connection = getConnection();

            // Start a transaction
            connection.setAutoCommit(false);

            // Lock the Class and Class_Status tables
            lockStatement = connection.createStatement();
            lockStatement.execute("LOCK TABLE Class, Class_Status IN EXCLUSIVE MODE");

            // SQL PreparedStatement
            String sql = "UPDATE Class " +
                    "SET statusID = (SELECT statusID " +
                    "                FROM Class_Status " +
                    "                WHERE statusName = ?) " +
                    "WHERE className = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("ClassStatus"));
            preparedStatement.setString(2, apiParams.get("ClassName"));
            int rows = preparedStatement.executeUpdate();

            // Commit the transaction
            connection.commit();

            if (rows > 0) {
                System.out.println("Class status updated successfully !");
                System.out.println("");
                return true;
            } else {
                System.out.println("Class status update failed !");
                System.out.println("");
                return false;
            }
        } catch (SQLException e) {
            // Rollback the transaction in case of an error
            if (connection != null) {
                connection.rollback();
            }

            e.printStackTrace();
            return false;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (lockStatement != null) {
                lockStatement.close();
            }
            // Reset auto-commit mode
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    /**
     * Updates the event of a specific class to a NewEvent
     * return boolean for success or failure
     * 
     * @author Nathaniel Fincham
     * @param apiParams
     * @return boolean for success or failure
     * @throws SQLException
     */
    public static boolean updateClassEvent(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        Statement lockStatement = null;
        try {
            // Get DB connection
            Connection connection = getConnection();

            // Start a transaction
            connection.setAutoCommit(false);

            // Lock the Class and Event tables
            lockStatement = connection.createStatement();
            lockStatement.execute("LOCK TABLE Class, Event IN EXCLUSIVE MODE");

            // SQL PreparedStatement
            String sql = "UPDATE Class " +
                    "SET eventID = (SELECT eventID " +
                    "                FROM Event " +
                    "                WHERE eventName = ?) " +
                    "WHERE className = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("Event"));
            preparedStatement.setString(2, apiParams.get("ClassName"));
            int rows = preparedStatement.executeUpdate();

            // Commit the transaction
            connection.commit();

            if (rows > 0) {
                System.out.println("Class event updated successfully !");
                System.out.println("");
                return true;
            } else {
                System.out.println("Class event update failed !");
                System.out.println("");
                return false;
            }
        } catch (SQLException e) {
            // Rollback the transaction in case of an error
            if (connection != null) {
                connection.rollback();
            }

            e.printStackTrace();
            return false;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (lockStatement != null) {
                lockStatement.close();
            }
            // Reset auto-commit mode
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    /**
     * Update the start time of a specific class to a new start time
     * return boolean for success or failure
     * 
     * @author Nathaniel Fincham
     * @param apiParams
     * @return
     * @throws SQLException
     */
    public static boolean updateClassStartTime(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        Statement lockStatement = null;
        try {
            // Get DB connection
            Connection connection = getConnection();

            // Start a transaction
            connection.setAutoCommit(false);

            // Lock the Class table
            lockStatement = connection.createStatement();
            lockStatement.execute("LOCK TABLE Class IN EXCLUSIVE MODE");

            // SQL PreparedStatement
            String sql = "UPDATE Class " +
                    "SET startTime = TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI') " +
                    "WHERE className = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("StartTime"));
            preparedStatement.setString(2, apiParams.get("ClassName"));
            int rows = preparedStatement.executeUpdate();

            // Commit the transaction
            connection.commit();

            if (rows > 0) {
                System.out.println("Class start time updated successfully !");
                System.out.println("");
                return true;
            } else {
                System.out.println("Class start time update failed !");
                System.out.println("");
                return false;
            }
        } catch (SQLException e) {
            // Rollback the transaction in case of an error
            if (connection != null) {
                connection.rollback();
            }

            e.printStackTrace();
            return false;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (lockStatement != null) {
                lockStatement.close();
            }
            // Reset auto-commit mode
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }
    //////////////////////////////////////////////////////////////
    // Emergency Contact //
    //////////////////////////////////////////////////////////////

    /**Insert newEmerContactInfo and associates the EmergencyContact to a student
     * @Author Noa Uritsky
     * 
     * @param apiParams
     * @return
     * @throws SQLException
     */
    public static boolean addNewEmergencyContact(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        Statement lockStatement = null;
        try {
            // Get DB connection
            Connection connection = getConnection();

            // Start a transaction
            connection.setAutoCommit(false);

            // Lock the EmergencyContact table
            lockStatement = connection.createStatement();
            lockStatement.execute("LOCK TABLE Emergency_Contact, Student_EmergContact IN EXCLUSIVE MODE");

            // SQL PreparedStatement
            String sql = "INSERT INTO Emergency_Contact(emergcon_userName, " +
                        " firstName, lastName, phoneNumber, email) " +
                        "Values(?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setString(1, apiParams.get("EmergencyContact_Username"));
            preparedStatement.setString(2, apiParams.get("FirstName"));
            preparedStatement.setString(3, apiParams.get("LastName"));
            preparedStatement.setString(4, apiParams.get("PhoneNumber"));
            preparedStatement.setString(5, apiParams.get("Email"));
            int rows = preparedStatement.executeUpdate();

            sql = "INSERT INTO Student_EmergContact(Student_Username, Emergcon_Username) " +
                "Values(?, ?)";
            
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, apiParams.get("Student_Username"));
            preparedStatement.setString(2, apiParams.get("EmergencyContact_Username"));
            int rows2 = preparedStatement.executeUpdate();

            // Commit the transaction
            connection.commit();

            if (rows > 0 && rows2 > 0) {
                System.out.println("Emergency contact added successfully !");
                System.out.println("");
                return true;
            } else {
                System.out.println("Adding emergency contact failed !");
                System.out.println("");
                return false;
            }
        } catch (SQLException e) {
            // Rollback the transaction in case of an error
            if (connection != null) {
                connection.rollback();
            }

            e.printStackTrace();
            return false;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (lockStatement != null) {
                lockStatement.close();
            }
            // Reset auto-commit mode
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }
    /**
     * Updates the emegency contact information for a specfic contact
     * User will provide emergcon_username and the new contact information (first
     * name, last name, phone number, or email)
     * 
     * @author Nathaniel Fincham
     *
     * @apiParams Input parameter list
     * 
     * @throws SQLException Connection to database times out
     */
    public static boolean updateEmergencyContact(HashMap<String, String> apiParams) throws SQLException {
        PreparedStatement preparedStatement = null;
        Statement lockStatement = null;
        try {
            // Get DB connection
            Connection connection = getConnection();

            // Start a transaction
            connection.setAutoCommit(false);

            // Lock the EmergencyContact table
            lockStatement = connection.createStatement();
            lockStatement.execute("LOCK TABLE Emergency_Contact IN EXCLUSIVE MODE");

            // SQL PreparedStatement
            String sql = "UPDATE Emergency_Contact " +
                    "SET phoneNumber = ?, email = ? " +
                    "WHERE emergcon_userName = ?";

            preparedStatement = connection.prepareStatement(sql);
            // preparedStatement.setString(1, apiParams.get("NewFirstName"));
            // preparedStatement.setString(2, apiParams.get("NewLastName"));
            preparedStatement.setString(1, apiParams.get("PhoneNumber"));
            preparedStatement.setString(2, apiParams.get("Email"));
            preparedStatement.setString(3, apiParams.get("UserName"));
            int rows = preparedStatement.executeUpdate();

            // Commit the transaction
            connection.commit();

            if (rows > 0) {
                System.out.println("Emergency contact updated successfully !");
                System.out.println("");
                return true;
            } else {
                System.out.println("Emergency contact update failed !");
                System.out.println("");
                return false;
            }
        } catch (SQLException e) {
            // Rollback the transaction in case of an error
            if (connection != null) {
                connection.rollback();
            }

            e.printStackTrace();
            return false;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (lockStatement != null) {
                lockStatement.close();
            }
            // Reset auto-commit mode
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }
}
