package ChatApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;


public class Client extends JFrame implements ActionListener, MouseListener, KeyListener
{
    static final String msgPhrase = "Type your message";

    // Non JFrame stuff
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    // JFrame stuff
    JLabel log_label;
    JButton send;
    JTextField message;
    JTextArea chat;
    JScrollPane scroll;

    /**
     * The constructor of the Client implements all the JFrame stuff.
     * It also initializes all the needed values for the client.
     * 
     * 
     * @param socket Used to pass a socket with an address and port number in the constructor.
     */
    
    public Client(Socket socket)
    {
        super("Chat Application");
        setLayout(new FlowLayout(FlowLayout.CENTER));
        try
        {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //this.username = username;

            username = JOptionPane.showInputDialog("Enter your username");

            log_label = new JLabel("Logged in as : " + this.username);
            
            // log.setBounds(150, 90, 200, 30);
            // log.setBounds(10, 10, 150, 20);
            // uname = new JTextField("Enter your username", 10);
            // username = userName;

            chat = new JTextArea(15, 30);
            scroll = new JScrollPane(chat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scroll.setPreferredSize(new Dimension(400, 300));
            chat.setEditable(false);

            send = new JButton("Send");
            message = new JTextField("Type your message", 15);

            // add(chat);
            add(scroll);
            add(message);
            add(send);
            add(log_label);
            send.addActionListener(this);
            message.addMouseListener(this);
            message.addKeyListener(this);
            // add(log);

            setSize(500, 500);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }
        catch (IOException constructor)
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == send)
        {
            String msgToSend = message.getText();
            try
            {
                bufferedWriter.write(username + " : " + msgToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                message.setText("");
            }
            catch(IOException actionlistener)
            {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }   
        }
    }

    public void sendMessage()
    {   
        try
        {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner msg = new Scanner(System.in);
            while(socket.isConnected())
            {
                String messageToSend = msg.nextLine();
                bufferedWriter.write(username + " : " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            msg.close();
        }
        catch (IOException message_send)
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    /**
     * This method implements a Runnable interface, and overrides the run method
     * in order to listen to oncoming messages from anothre client. 
     * 
     */
    public void ListenForMessage()
    {
        Runnable runnable1 = new Runnable()
        {
            @Override
            public void run()
            {
                String msgFromGroupChat;

                while(socket.isConnected())
                {
                    try
                    {
                        msgFromGroupChat = bufferedReader.readLine();
                        chat.append(msgFromGroupChat + "\n");
                        // System.out.println(msgFromGroupChat);
                    }
                    catch (IOException incoming_message)
                    {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        };

        Thread client_thread = new Thread(runnable1);
        client_thread.start();
    }

    /**
     * Almost identical as the Server's closeEverything method that closes the bufferedReader, bufferedWriter, and the socket
     * 
     * @param socket
     * @param bufferedReader
     * @param bufferedWriter
     */
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
        try
        {
            if(bufferedReader != null)
            {
                bufferedReader.close();
            }
            
            if(bufferedWriter != null)
            {
                bufferedWriter.close();
            }

            if(socket != null)
            {
                socket.close();
            }
        }
        catch(IOException close)
        {
            close.printStackTrace();
        }
    }

    /**
     * Initializes the socket and then passes it to the Client Constructor.
     * Also handles messages
     * 
     * @throws IOException
     */
    public static void createClient() throws IOException
    {
        // Scanner enter = new Scanner(System.in);
        // System.out.println("Enter your username for the group chat: ");
        // String username = enter.nextLine();
        Socket socket = new Socket("localhost", 30000);
        Client client = new Client(socket); /* , username*/
        client.ListenForMessage();
        client.sendMessage();
        // enter.close();
    }


    /**
     * Calls the createClient method that creates all the necessary stuff for a client
     * like initialize a socket, call the constructor and handle the messages.
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        createClient();
    }

    // Mouse Listeners for some extra stuff that i will probably use later on
    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(e.getSource() == message)
        {
            message.setText("");
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e){
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
             
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            String msgToSend = message.getText();
            try
            {
                bufferedWriter.write(username + " : " + msgToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                message.setText("");
            }
            catch(IOException actionlistener)
            {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

}
