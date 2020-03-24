package sample;


import Interfaces.ClientListener;
import Interfaces.ControllerListener;
import Messages.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Client implements Runnable, ControllerListener
{
    private ClientListener controller;
    private List<String> subscribedChannels;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Thread thread;
    private int port;

    public Client()
    {
        try
        {
            subscribedChannels = new ArrayList<>();
            port = 8000;
            clientSocket = new Socket("localhost", port);
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
                        notifyObserver(rm);
                        break;
                    case "PIC-MSG":
                        PictureMsg pm = (PictureMsg)p.getData();
                        notifyObserver(pm);
                        break;
                    case "CHG-MSG":
                        ChangeChannelMsg cm = (ChangeChannelMsg)p.getData();
                        notifyObserver(cm);
                        break;
                    case "TXT-MSG":
                        ChannelMsg tm = (ChannelMsg)p.getData();
                        notifyObserver(tm);
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

    public void addController(ClientListener c)
    {
        controller = c;
    }

    public void notifyObserver(Object arg)
    {
        controller.update(arg);
    }
}
