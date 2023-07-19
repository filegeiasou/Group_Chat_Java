import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;      

public class Client1 extends JFrame implements ActionListener
{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    Client1(String serverAddress, int serverPort)
    {

        try{

            socket = new Socket(serverAddress, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            Thread messageHandlerThread = new Thread(() -> handleIncomingMessages());
            messageHandlerThread.start();
        }
        catch(Exception e)
        {

        }

    }

    private void handleIncomingMessages()
    {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                System.out.println("Server: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
