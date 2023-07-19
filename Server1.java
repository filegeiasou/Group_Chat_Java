import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
//import java.awt.*;

public class Server1 extends JFrame implements ActionListener {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
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
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            Thread messageHandlerThread = new Thread(() -> handleIncomingMessages());
            messageHandlerThread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void handleIncomingMessages()
    {
        String message,message1;
        int cnt = 0;
        try {
            while ((message = in.readLine()) != null) {
                System.out.println("Client: " + message);
                if(cnt==0)
                {
                    message1 = message + " ";
                }  
            }
        } catch (IOException e) {
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
