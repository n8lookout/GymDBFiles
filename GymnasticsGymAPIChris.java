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
import java.util.Properties;


/***
 * Gymnastics Gym API that uses JDBC to connect to a PostgreSQL database.
 */
public class GymnasticsGymAPI {


    //////////////////////////////////////////////////////////////
    //                    GLOBAL VARIABLES                      //
    //////////////////////////////////////////////////////////////

    // JDBC URL, username, and password of PostgreSQL server
    private static final String URL = "jdbc:postgresql://localhost:5432/GymnasticsGym";
    private static final String USER = "postgres";
    private static final String PASSWORD = "net1net2";


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
        Connection connection = null;

        Properties connectionProps = new Properties();
        connectionProps.put("user", USER);
        connectionProps.put("password", PASSWORD);

        connection = DriverManager.getConnection(URL, connectionProps);

        return connection;
    }

    /**
     * Method to disconnect from the PostgreSQL database
     * @author The Team
     *
     * @post The input variables will be closed and set to null
     * @param connection to PostgreSQL database
     * @param preparedStatement contains the querry that was prepared by connection
     * @param resultSet contains the results of the query, provided by preparedStatement.
     * @throws SQLException Connection to database times out
     */
    public static void disconnect(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }
        if (preparedStatement != null) {
            preparedStatement.close();
        }
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }


    /**
     *
     *
     * @author Noa Uritsky
     *
     * @param className
     * @param startTime
     * @param duration
     * @param eventName
     * @param difficultyName
     * @param statusName
     */
    public static void addNewClass(String className, String startTime, String duration,
                                        String eventName, String difficultyName, String statusName){

    }

    /**
     *
     *
     * @author Noa Uritsky
     *
     * @param student_userName
     * @param firstName
     * @param lastName
     * @param birthDate
     * @param phoneNumber
     * @param email
     */
    public static void addNewStudent(String student_userName, String firstName, String lastName,
                                          String birthDate, String phoneNumber, String email ){
    }

    /**
     *
     *
     * @author Noa Uritsky
     *
     * @param coach_userName
     * @param firstName
     * @param lastName
     * @param phoneNumber
     * @param email
     */
    public static void addNewCoach(String coach_userName, String firstName, String lastName,
                                        String phoneNumber, String email ) {
    }

    /**
     *
     *
     * @author Noa Uritsky
     *
     * @param student_userName
     * @param emergcon_userName
     * @param firstName
     * @param lastName
     * @param phoneNumber
     * @param email
     */
    public static void addNewEmergencyContact( String student_userName, String emergcon_userName,
                                                    String firstName, String lastName, String phoneNumber, String email ) {
    }

    /**
     *
     *
     * @author Noa Uritsky
     *
     * @param coach_userName
     * @param scheduleName
     * @param StartTime
     * @param EndTime
     */
    public static void addNewCoachSchedule(String coach_userName, String scheduleName, String StartTime,
                                                String EndTime) {

    }

    /**
     *
     *
     * @author Nathaniel Fincham
     *
     * @param coach_userName
     * @param scheduleName
     * @param StartTime
     * @param EndTime
     */
    public static void changeCoachSchedule(String coach_userName, String scheduleName, String StartTime, String EndTime) {

    }

    /**
     *
     *
     * @author Nathaniel Fincham
     *
     * @param className
     * @param student_userName
     */
    public static void addStudentInClass(String className, String student_userName) {

    }

    /**
     * Retrieves the schedule for a specific coach on a specific date. This schedules are made up from their
     * availability time and the classes they’re assigned to
     * @author Christopher Long
     *
     * @param coach_userName The username of the coach to query for
     * @param date The specific date to query a schedule from
     * @throws SQLException Connection to database times out
     */
    public static void showCoachSchedule(String coach_userName, String date)  throws SQLException {
        // JDBC variables for opening, closing, and managing connection
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Open a connection
            connection = getConnection();

            // Start a transaction
            connection.setAutoCommit(false);

            // Prepare statement for retrieving coach schedules
            String sql = "QUERRY STUFF GOES HERE!!"; //TODO Fill this out when database is complete.

            preparedStatement = connection.prepareStatement(sql);

            // Supplying Values
            preparedStatement.setString(1, coach_userName);
            preparedStatement.setString(2, date);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Commit the transaction
            connection.commit();


        } catch (SQLException e) {
            // Rollback the transaction if an error occurs
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            disconnect(connection, preparedStatement, resultSet);
        }
    }

    /**
     * Assigns a coach to a class based on their schedule and class requirements
     * @author Christopher Long
     *
     * @param coach_userName The username of the coach to query for
     * @param className Class to assign coach to.
     * @throws SQLException Connection to database times out
     */
    public static void assignClassCoach(String coach_userName, String className)  throws SQLException {
        // JDBC variables for opening, closing, and managing connection
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Open a connection
            connection = getConnection();

            // Start a transaction
            connection.setAutoCommit(false);

            // Prepare statement for retrieving coach schedules
            String sql = "QUERRY STUFF GOES HERE!!"; //TODO Fill this out when database is complete.

            preparedStatement = connection.prepareStatement(sql);

            // Supplying Values
            preparedStatement.setString(1, coach_userName);
            preparedStatement.setString(2, className);

            // Execute the prepared statement
            int rowsAffected = preparedStatement.executeUpdate();

            // Commit the transaction if the insertion was successful
            if (rowsAffected > 0) {
                connection.commit();
                System.out.println("Coach assigned Class.");
            } else {
                System.out.println("Failed to Assign Coach to Class. No rows affected.");
            }


        } catch (SQLException e) {
            // Rollback the transaction if an error occurs
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            disconnect(connection, preparedStatement, resultSet);
        }
    }

    //////////////////////////////////////////////////////////////
    //                         MAIN                             //
    //////////////////////////////////////////////////////////////
    public static void main(String[] args) {


        // Depending on client input, select the appropriate function.
        try {
            switch (args[0]) {
                case "Create":
                    switch (args[1]) {
                        case "Class":
                            // addNewClass(className, startTime, duration, eventName, difficultyName, statusName)
                            break;
                        case "Student":
                            // addNewStudent( student_userName, firstName, lastName, birthDate, phoneNumber, email )
                            break;
                        case "Coach":
                            // addNewCoach(coach_userName, firstName, lastName, phoneNumber, email )
                            break;
                        case "EmergencyContact":
                            // addNewEmergencyContact (student_userName, emergcon_userName, firstName, lastName, phoneNumber, email )
                            break;
                        case "Coach Schedule":
                            // addNewCoachSchedule(coach_userName, scheduleName, StartTime, EndTime)
                            break;
                    }
                    break;

                case "Update":
                    break;

                case "Schedule":
                    switch (args[1]) {
                        case "Coaches":
                            showCoachSchedule(args[2], args[3]);
                            break;
                        case "Classes":
                            // assignClassCoach(coach_userName, className)
                            break;
                    }
                    break;

                case "List":
                    break;

                case "Get":
                    break;

                case "Notification":
                    break;

                default:
                    System.out.println("Invalid input");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
