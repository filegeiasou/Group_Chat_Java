import java.sql.*;
public class Database
{
    public Database(String username, String password, String email, String sex)
    {
        try
        {
//            username = uname.getText();
//            password = upass.getText();

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "root");

            // using a string for the SQL query
            String checkQuery = "SELECT * FROM users WHERE username = ? AND password = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(checkQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
//            preparedStatement.setString(3, email);
//            preparedStatement.setString(4, sex);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.isBeforeFirst())
            {
                // JOptionPane.showMessageDialog(this, "This user already exists");
                System.out.println("User with username " + username + " already exists");
            }
            else
            {
                // If no matching row is found, the username and password combination is not in the database
                // Proceed with registration.

                // In the insertQuery, we don't need to pass in the user_id,
                // because it is an AUTO INCREMENT element in the SQL.
                // So each user will have a unique ID that is assigned by SQL.
                String insertQuery = "INSERT INTO users (username, password, email, sex) VALUES (?, ?, ?, ?)";
                PreparedStatement register = connection.prepareStatement(insertQuery);
                register.setString(1, username);
                register.setString(2, password);
                register.setString(3, email);
                register.setString(4, sex);
                int rowsInserted = register.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Registration successful!");
                } else {
                    System.out.println("Registration failed!");
                }
                register.close();
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        }
        catch (SQLIntegrityConstraintViolationException key)
        {
            System.out.println("Username already exists. Registration failed!");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}