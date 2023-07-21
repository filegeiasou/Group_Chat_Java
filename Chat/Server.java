//package ChatApp;

import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.sql.*;


public class Server
{
    private ServerSocket serverSocket;
    String clientname, clientpass;

    public Server(ServerSocket serverSocket)
    {
        this.serverSocket = serverSocket;
    }

    /**
     * Accepts the incoming connection and displays which user has connected in the terminal
     */
    public void startServer()
    {
        try
        {
            System.out.println("\nServer is up and running. Waiting for clients.");
            while(!serverSocket.isClosed())
            {
                // accept() haults the program
                Socket ssocket = serverSocket.accept();

                ClientHandler client = new ClientHandler(ssocket);

                // Get the credentials from the user
                clientname = client.getName();
                clientpass = client.getPassword();

                System.out.println(client.getName() + " has connected");

                Thread server_thread = new Thread(client);
                server_thread.start();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Closes the server socket
     */
    public void closeServerSocket()
    {
        try
        {
            if(serverSocket != null)
            {
                serverSocket.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main (String [] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(30000);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}


class ClientHandler implements Runnable
{
    public static ArrayList<ClientHandler> clients = new ArrayList<>();
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String clientUsername, clientPassword;
    private String connected = "false";

    public String getName() {return clientUsername;}
    public String getPassword() {return clientPassword;}

    /**
     * Constructor for the ClientHandler class that is used to handle the clients
     * using other methods
     *
     * @param socket
     */
    public ClientHandler(Socket socket)
    {
        try
        {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            this.clientPassword = bufferedReader.readLine();

            check(this.clientUsername, this.clientPassword);
            if(connected.equals("true"))
            {
                clients.add(this);
                broadcastMessage("Server : " + clientUsername + " has joined the chat.");
            }
        }
        catch(IOException ioe)
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    /**
     * Checks if the credentials given by the user are valid.
     * If they are then set the connected String to true
     *
     * @param username
     * @param password
     */

    public void check(String username, String password)
    {
        try
        {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat", "root", "root");

            // Statement statement = connection.createStatement();
            // ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS");

            String query = "SELECT * FROM USERS WHERE USERNAME = ? AND PASSW = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
            {
                connected = "true";
                // System.out.println("Username : " + resultSet.getString("USERNAME") + " Password : " + resultSet.getString("PASSW"));
            }
            else
            {
                connected = "false";
            }

            try {
                bufferedWriter.write(connected);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Close resources
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method iterates through the clients ArrayList and
     * broadcast the message to every client that is in the ArrayList.
     *
     * @param messageToSend Message to broadcast to other clients connected.
     */
    public void broadcastMessage(String messageToSend)
    {
        for(ClientHandler client : clients)
        {
            try
            {
                // If i want to change whether the sender sees his own message,
                // then i need to use the command above

                client.bufferedWriter.write(messageToSend);
                client.bufferedWriter.newLine();
                client.bufferedWriter.flush();
            }
            catch (IOException e)
            {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    /**
     * Removes the client from the ArrayList and displays that specified client has left the chat
     */
    public void removeClient()
    {
        clients.remove(this);
        broadcastMessage("Server : " + clientUsername + " has left the chat.");;
    }

    /**
     * Closes everything related to a client.
     *
     * When a client disconnectd it prints it in the terminal, runs the removeClient method,
     * and then closes the socket, bufferedReader and bufferedWriter.
     *
     * @param socket
     * @param bufferedReader
     * @param bufferedWriter
     */
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
        System.out.println(clientUsername + " has disconnected");
        removeClient();
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
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }


    /**
     * Handles incoming messages from clients, and broadcasts the message while the socket is connected.
     */

    @Override
    public void run()
    {
        String messageFromClient;

        while(socket.isConnected())
        {
            try
            {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            }
            catch (IOException e)
            {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }
}
