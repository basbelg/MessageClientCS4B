package sample;


import Interfaces.ControllerListener;
import Messages.*;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Observable;

public class Client extends Observable implements Runnable, ControllerListener
{
    private List<Controller> controllers;
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
                        RegistrationMsg rm = (RegistrationMsg)p.getData();
                        notifyObservers(rm);
                        break;
                    case "PIC-MSG":
                        PictureMsg pm = (PictureMsg)p.getData();
                        break;
                    case "CHG-MSG":
                        ChangeChannelMsg cm = (ChangeChannelMsg)p.getData();
                        break;
                    case "TXT-MSG":
                        ChannelMsg tm = (ChannelMsg)p.getData();
                        break;
                    default:
                        System.out.println("ERROR");
                }
            }
        }
        catch(IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }


    public List<String> getSubscribedChannels()
    {
        return subscribedChannels;
    }

    @Override
    public void update(Observable o, Object arg)
    {

    }

    public void addController(Controller c)
    {
        controllers.add(c);
    }

    public void removeController(Controller c)
    {
        controllers.remove(c);
    }


    @Override
    public void notifyObservers(Object arg)
    {
        super.notifyObservers(arg);
    }
}
