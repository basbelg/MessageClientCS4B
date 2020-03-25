package sample;

import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client implements Runnable
{
    private BaseController controller;
    private List<String> subscribedChannels;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Thread thread;
    private int port;
    private String username;

    public Client(BaseController controller)
    {
        subscribedChannels = new ArrayList<>();
        port = 8000;
        this.controller = controller;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            clientSocket = new Socket("localhost", port);
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            input = new ObjectInputStream(clientSocket.getInputStream());

            while(!thread.isInterrupted()) {
                //read input from server
                Packet p = (Packet)input.readObject();
                String type = p.getType();
                switch(type) {
                    case "REG-MSG":
                        RegistrationMsg rm = (RegistrationMsg)p.getData();
                        for(String sc : rm.getSubscribedChannels()) {
                            subscribedChannels.add(sc);
                        }
                        controller.update(rm);
                        break;
                    case "PIC-MSG":
                        PictureMsg pm = (PictureMsg)p.getData();
                        controller.update(pm);
                        break;
                    case "CNG-MSG":
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
        catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("client thread terminated");
        }
    }

    // need a new button to call this
    public void terminateThread() {
        thread.interrupt();
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getSubscribedChannels() {
        return subscribedChannels;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void update(Serializable arg) {
        try {

            if (arg instanceof RegistrationMsg) {
                Packet p = new Packet("REG-MSG", arg);
                output.writeObject(p);
            }
            else if (arg instanceof ChannelMsg) {
                Packet p = new Packet("TXT-MSG", arg);
                output.writeObject(p);
            }
            else if (arg instanceof ChangeChannelMsg) {
                Packet p = new Packet("CNG-MSG", arg);
                output.writeObject(p);
            }
            else if (arg instanceof PictureMsg) {
                Packet p = new Packet("PIC-MSG", arg);
                output.writeObject(p);
            }
            else {
                System.out.println("Client Update - ERROR");
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void SetController(BaseController con) {
        controller = con;
    }
}
