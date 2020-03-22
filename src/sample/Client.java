package sample;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class Client implements Runnable
{
    private List<String> subscribedChannels;
    private Socket clientSocket;
    ObjectInputStream input;
    ObjectOutputStream output;
    private Thread thread;

    public Client(Socket socket)
    {
        try
        {
            clientSocket = socket;
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
            thread = new Thread(this);
            thread.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        try
        {
            while(true)
            {

            }
        }
        catch(Exception e)
        {

        }
    }

    public List<String> getSubscribedChannels()
    {
        return subscribedChannels;
    }
}
