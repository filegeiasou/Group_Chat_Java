//package ChatApp;

import java.io.*;
import javax.swing.*;
import java.net.Socket;
import java.awt.*;
import java.awt.event.*;

public class Client extends JFrame implements ActionListener, MouseListener, KeyListener
{
    static final String msgPhrase = "Type your message";
    public int count = 0;

    // Networking
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter, bw;
    private String username, password, feedback;

    // GUI Components
    JLabel log_label;
    JButton send;
    JTextField message;
    JTextArea chat;
    JScrollPane scroll;

    /**
     * The constructor of the Client implements all the JFrame stuff.
     * It also initializes all the needed values for the client.
     *
     */

    public Client(String username, String password)
    {
        super("Chat Application");
        this.username = username;
        this.password = password;

        initializeNetworking();
        displayChatUI();
        // username = JOptionPane.showInputDialog("Enter your username");
    }

    public Client(String username, String password, String email, String sex)
    {
        try {
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            bw.write(username);
            bw.newLine();
            bw.write(password);
            bw.newLine();
            bw.write(email);
            bw.newLine();
            bw.write(sex);
            bw.newLine();
            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeNetworking()
    {
        try
        {
            socket = new Socket("localhost", 30000);

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            sendMessage();

        } catch (IOException e)
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    private void showInvalidCredentialsMessage()
    {
        JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again", feedback, JOptionPane.ERROR_MESSAGE);
        count++;
    }

    private void initializeGUI()
    {
        setLayout(new FlowLayout(FlowLayout.CENTER));

        // This displays the logged in user's username
        // E.g if someone connects with username Nick, it will display it on the chat window
        log_label = new JLabel("Logged in as : " + this.username);

        // A Text Area is initialized, and then is embedded in a Scroll Panel,
        // that lets us scroll the chat up, down, left and right.
        chat = new JTextArea(15, 30);
        scroll = new JScrollPane(chat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setPreferredSize(new Dimension(400, 300)); // sets dimensions for scroll panel
        chat.setEditable(false); // sets the text area to non-editable because we don't want to just write on the area

        send = new JButton("Send"); // button to send message
        message = new JTextField("Type your message", 15); // text field where you write the message you want to send

        // add all the elements to the frame, and to the appropriate action listeners
        add(scroll);
        add(message);
        add(send);
        add(log_label);
        send.addActionListener(this);
        message.addMouseListener(this);
        message.addKeyListener(this);

        pack(); // needed in order to show the windows realitively centered
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(500, 500); // set size of frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit when X is clicked
        this.setVisible(true); // show the frame
    }

    private void displayChatUI()
    {
        if(feedback.equals("true"))
        {
            initializeGUI();
            ListenForMessage();
        }else showInvalidCredentialsMessage();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // If the send button is pressed, then we send the message
        if(e.getSource() == send)
        {
            String msgToSend = message.getText();
            try
            {
                bufferedWriter.write(this.username + " : " + msgToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                message.setText("");
            }
            catch(IOException al)
            {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void sendMessage()
    {
        try
        {
            // In the code below, we send the username and the password to the server,
            // so it can check if the credentials (username, password) are valid.
            bufferedWriter.write(this.username);
            bufferedWriter.newLine();
            bufferedWriter.write(this.password);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            // Here we get the feedback from the server about the credentials
            feedback = bufferedReader.readLine();

        }
        catch (IOException message_send)
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    /**
     * This method implements a Runnable interface, and overrides the run method
     * in order to listen to oncoming messages from another client.
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
                    }
                    catch (IOException incoming_message)
                    {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        };

        // We assign a thread to run our runnable interface, and then we actually start it
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
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Calls the CredentialsHandler constructor, that actually is used for some GUI.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        new CredentialsHandler();
    }
}

class CredentialsHandler extends JFrame implements ActionListener, KeyListener, MouseListener
{
    private JLabel uname, pass, wrong, signup;
    private JTextField username, password;
    private JButton button;

    CredentialsHandler()
    {
        super("Authorization");
        setLayout(new FlowLayout());

        // Labels
        uname = new JLabel("Username ");
        pass = new JLabel("Password ");
        signup = new JLabel("<html><u>Sign up</u></html>");
        signup.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signup.setForeground(Color.BLUE);
        signup.setFont(new Font("JetBrains Mono", Font.BOLD, 13));

        signup.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    new RegistrationForm();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        username = new JTextField(10); // text field for username
        password = new JPasswordField(10); // used JPasswordField to censor password

        // Button to submit credentials
        button = new JButton("Login");

        wrong = new JLabel("");

        // Adding all the components to
        add(uname);
        add(username);
        add(pass);
        add(password);
        add(button);
        add(signup);
        add(wrong);
        button.addActionListener(this);
        password.addKeyListener(this);

        pack(); // same as the client
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(250, 250);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == button)
        {
            Client c1 = new Client(username.getText(), password.getText());

            if(c1.count == 0)
            {
                setVisible(false);
            }
            else
            {
                username.setText("");
                password.setText("");
                wrong.setText("Incorrect username or password");
            }

            c1.ListenForMessage();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if(!username.getText().equals(""))
            {
                Client c1 = new Client(username.getText(), password.getText());
                // c1.sendMessage();
                if(c1.count == 0)
                    setVisible(false);
                else
                {
                    username.setText("");
                    password.setText("");
                    wrong.setText("Incorrect username or password");
                }
                c1.ListenForMessage();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

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
    public void mouseExited(MouseEvent e) {

    }
}