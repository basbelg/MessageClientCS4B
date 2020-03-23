package sample;


import Interfaces.ControllerListener;
import Messages.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
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
    private int port;

    public Client(Socket socket)
    {
        super();
        try
        {
            subscribedChannels = new ArrayList<>();
            controllers = new ArrayList<>();
            controllers.add(new Controller());
            clientSocket = socket;
            port = socket.getPort();
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

    public Client()
    {
        super();
        try
        {
            subscribedChannels = new ArrayList<>();
            controllers = new ArrayList<>();
            controllers.add(new Controller());
            clientSocket = new Socket();
            port = 8000;
            input = new ObjectInputStream(clientSocket.getInputStream());
            output = new ObjectOutputStream(clientSocket.getOutputStream());
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
                        for(String sc : rm.getSubscribedChannels())
                        {
                            subscribedChannels.add(sc);
                        }
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
    public void update(Object arg)
    {
        try {
            if (arg instanceof RegistrationMsg) {
                Packet p = new Packet("REG-MSG", (RegistrationMsg) arg);
                output.writeObject(p);
            }
            else if (arg instanceof ChannelMsg) {
                Packet p = new Packet("TXT-MSG", (ChannelMsg) arg);
                output.writeObject(p);
            }
            else if (arg instanceof ChangeChannelMsg) {
                Packet p = new Packet("CHG-MSG", (ChangeChannelMsg) arg);
                output.writeObject(p);
            }
            else if (arg instanceof PictureMsg) {
                Packet p = new Packet("PIC-MSG", (PictureMsg) arg);
                output.writeObject(p);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
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
        for(Controller c : controllers)
        {
            c.update(arg);
        }
    }
}
