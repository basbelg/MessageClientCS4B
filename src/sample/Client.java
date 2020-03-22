package sample;


import Messages.Packet;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class Client implements Runnable
{
    private List<String> subscribedChannels;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
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
                //read input from server
                Packet p = (Packet)input.readObject();
                String type = p.getType();
                switch(type)
                {
                    case "REG-MSG":

                    case "PIC-MSG":

                    case "CHG-MSG":

                    case "TXT-MSG":
                }
            }
        }
        catch(IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void sendMessageToServer()
    {}

    public List<String> getSubscribedChannels()
    {
        return subscribedChannels;
    }
}
