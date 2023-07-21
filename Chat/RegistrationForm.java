package ChatApp;

import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;

public class RegistrationForm extends JFrame implements ActionListener
{
    private JLabel username, password;
    private JTextField uname, upass;
    private JButton register;

    public RegistrationForm()
    {
        super("Register");
        setLayout(new FlowLayout());

        username = new JLabel("Username ");
        password = new JLabel("Password ");

        uname = new JTextField(10);
        upass = new JTextField(10);

        register = new JButton("Register");

        add(username);
        add(uname);
        add(password);
        add(upass);
        add(register);
        register.addActionListener(this);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(250,250);
        setResizable(false);
        setVisible(true);
    }

    public void register()
    {
        try
        {
                
        }
        catch (Exception e)
        {
            
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == register)        
        {
            System.out.println("You registered");
        }
    }

    public static void main(String[] args)
    {
        new RegistrationForm();
    }
}
