import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;      

public class Client1 extends JFrame implements ActionListener
{
    private Socket socket;

    Client1(String serverAddress, int serverPort)
    {

        try{
            socket = new Socket(serverAddress, serverPort);
        }
        catch(Exception e)
        {

        }

    }
    public static void main(String[] args) 
    {
        new Client1("localhost", 30000);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
    
}
