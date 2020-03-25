package sample;

import Messages.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Client implements Runnable
{
    private Controller controller;
    private List<String> subscribedChannels;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Thread thread;
    private int port;
    private boolean isRunning = true;
    private Serializable latestMessage;

    public Client(Controller controller)
    {
        subscribedChannels = new ArrayList<>();
        port = 8000;
        this.controller = controller;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run()
    {
        try
        {
            clientSocket = new Socket("localhost", port);
            input = new ObjectInputStream(clientSocket.getInputStream());

            while(isRunning)
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
                        controller.update(rm);
                        break;
                    case "PIC-MSG":
                        PictureMsg pm = (PictureMsg)p.getData();
                        controller.update(pm);
                        break;
                    case "CHG-MSG":
                        ChangeChannelMsg cm = (ChangeChannelMsg)p.getData();
                        controller.update(cm);
                        break;
                    case "TXT-MSG":
                        ChannelMsg tm = (ChannelMsg)p.getData();
                        controller.update(tm);
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
        finally
        {
            isRunning = false;
        }
    }

    public List<String> getSubscribedChannels()
    {
        return subscribedChannels;
    }


    public void update(Serializable arg)
    {
        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());

            if (arg instanceof RegistrationMsg) {
                Packet p = new Packet("REG-MSG", arg);
                output.writeObject(p);
            }
            else if (arg instanceof ChannelMsg) {
                Packet p = new Packet("TXT-MSG", arg);
                output.writeObject(p);
            }
            else if (arg instanceof ChangeChannelMsg) {
                Packet p = new Packet("CHG-MSG", arg);
                output.writeObject(p);
            }
            else if (arg instanceof PictureMsg) {
                Packet p = new Packet("PIC-MSG", arg);
                output.writeObject(p);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
