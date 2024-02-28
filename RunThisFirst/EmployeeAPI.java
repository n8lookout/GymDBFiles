import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class EmployeeAPI {
    // JDBC URL, username, and password of PostgreSQL server
    private static final String URL = "jdbc:postgresql://localhost:5432/db01";
    private static final String USER = "postgres";
    private static final String PASSWORD = "net1net2";

    /**
     * Method to connect to the PostgreSQL database
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

    // Method to query employees by department ID
    public void getEmployeesByDepartment(int departmentId) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            String sql = "SELECT * FROM Employee WHERE departmentid = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, departmentId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("Id");
                String employeeNum = resultSet.getString("employeeNum");
                String name = resultSet.getString("name");
                String phone = resultSet.getString("phone");
                String homePhone = resultSet.getString("homePhone");
                int departmentIdResult = resultSet.getInt("departmentid");

                System.out.println("ID: " + id + ", Employee Number: " + employeeNum + ", Name: " + name
                        + ", Phone: " + phone + ", Home Phone: " + homePhone + ", Department ID: " + departmentIdResult);
            }
        } finally {
            disconnect(connection, preparedStatement, resultSet);
        }
    }

    public static void main(String[] args) {
        try {
            // Example: Get employees by department with ID 1
            EmployeeAPI test = new EmployeeAPI();
            test.getEmployeesByDepartment(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
