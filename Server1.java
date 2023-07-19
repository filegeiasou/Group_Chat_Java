import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
//import java.awt.*;

public class Server1 extends JFrame implements ActionListener {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    Server1(int port)
    {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() 
    {
        try {
            clientSocket = serverSocket.accept();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnknownHostException
    {
        Server1 server = new Server1(30000);
        server.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

}
