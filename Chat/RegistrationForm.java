//package ChatApp;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class RegistrationForm extends JFrame implements ActionListener
{
    private String choice;
    private JLabel usernameLabel, passwordLabel, emailLabel, check;
    private JTextField uname, email;
    private JPasswordField upass;
    private JButton register;
    private JComboBox<String> chooseSex;

    public RegistrationForm() throws IOException
    {
        super("Registration Window");
        initializeGUI();
    }

    private void initializeGUI()
    {
        setLayout(new FlowLayout());

        usernameLabel = new JLabel("Username ");
        passwordLabel = new JLabel("Password ");
        emailLabel = new JLabel("Email          "); // 10 spaces to align

        check = new JLabel("Already a user?");
        check.setFont(new Font("JetBrains Mono", Font.BOLD, 13));

        String [] names = {"Male", "Female", "Other"};
        chooseSex = new JComboBox<>(names);

        uname = new JTextField(12);
        email = new JTextField(12);
        upass = new JPasswordField(12);


        register = new JButton("Register");

        // Add the components to the main frame
        add(usernameLabel);
        add(uname);
        add(emailLabel);
        add(email);
        add(passwordLabel);
        add(upass);
        add(chooseSex);
        add(register);
        add(check);

        register.addActionListener(this);
        chooseSex.addActionListener(this);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(250,250);
        setResizable(false);
        setVisible(true);
    }

//    public void register()
//    {
//        try
//        {
////            username = uname.getText();
////            password = upass.getText();
//
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "root");
//
//            // using a string for the SQL query
//            String checkQuery = "SELECT * FROM users WHERE username = ? AND password = ?";
//
//            PreparedStatement preparedStatement = connection.prepareStatement(checkQuery);
//            preparedStatement.setString(1, uname.getText());
//            preparedStatement.setString(2, upass.getText());
//            preparedStatement.setString(3, email.getText());
//            preparedStatement.setString(4, );
//
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            if(resultSet.isBeforeFirst())
//            {
//                JOptionPane.showMessageDialog(this, "This user already exists");
//                // System.out.println("User with username " + username + " already exists");
//            }
//            else
//            {
//                // If no matching row is found, the username and password combination is not in the database
//                // Proceed with registration.
//
//                // In the insertQuery, we don't need to pass in the user_id,
//                // because it is an AUTO INCREMENT element in the SQL.
//                // So each user will have a unique ID that is assigned by SQL.
//                String insertQuery = "INSERT INTO users (username, password, email, sex) VALUES (?, ?, ?, ?)";
//                PreparedStatement register = connection.prepareStatement(insertQuery);
//                register.setString(1, username);
//                register.setString(2, password);
//                register.setString(3, email);
//                int rowsInserted = register.executeUpdate();
//
//                if (rowsInserted > 0) {
//                    System.out.println("Registration successful!");
//                } else {
//                    System.out.println("Registration failed!");
//                }
//                register.close();
//            }
//
//            resultSet.close();
//            preparedStatement.close();
//            connection.close();
//        }
//        catch (SQLIntegrityConstraintViolationException key)
//        {
//            System.out.println("Username already exists. Registration failed!");
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == register)
        {
            if(chooseSex.getItemAt(0).equals("Male"))
            {
                choice = "Male";
            }
            else if(chooseSex.getItemAt(1).equals("Female"))
            {
                choice = "Female";
            }
            else if(chooseSex.getItemAt(2).equals("Other"))
            {
                choice = "Other";
            }

//            if (!uname.getText().isEmpty() && !upass.getText().isEmpty() && email.getText().isEmpty())
            new Database(uname.getText(), upass.getText(), email.getText(), choice);
        }
    }
}
