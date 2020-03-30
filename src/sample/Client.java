package sample;

import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Client implements Runnable
{
    private BaseController controller;
    private List<String> allChannels;
    private List<String> subscribedChannels;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Thread thread;
    private int port;
    private HashMap<String, List<Serializable>> chatHistory;
    private String username;

    public Client(BaseController controller)
    {
        allChannels = new ArrayList<>();
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
                        for(String sc : rm.getChannels())
                        {
                            allChannels.add(sc);
                        }
                        break;
                    case "PIC-MSG":
                        PictureMsg pm = (PictureMsg)p.getData();
                        chatHistory.get(pm.getPublishToChannel()).add(pm.getPicData());
                        controller.update(pm);
                        break;
                    case "TXT-MSG":
                        ChannelMsg tm = (ChannelMsg)p.getData();
                        chatHistory.get(tm.getPublishToChannel()).add(tm.getTextMsg());
                        controller.update(tm);
                        break;
                    case "CRT-MSG":
                        CreateChannelMsg crm = (CreateChannelMsg)p.getData();
                        allChannels.add(crm.getChannelName());
                        if(username.equals(crm.getChannelOwner()))
                        {
                            chatHistory.put(crm.getChannelName(), new ArrayList<Serializable>());
                            subscribedChannels.add(crm.getChannelName());
                        }
                        break;
                    case "JNC-MSG":
                        JoinChannelMsg jm = (JoinChannelMsg)p.getData();
                        chatHistory.put(jm.getJoinChannel(), jm.getChatHistory());
                        controller.update(jm);
                        break;
                    case "NWU-MSG":
                        NewUserMsg nm = (NewUserMsg)p.getData();
                        chatHistory.get(nm.getToChannel()).add(nm.getNewUser() + " has joined the chat!");
                        controller.update(nm);
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
            else if (arg instanceof PictureMsg) {
                Packet p = new Packet("PIC-MSG", arg);
                output.writeObject(p);
            }
            else if (arg instanceof CreateChannelMsg) {
                Packet p = new Packet("CRT-MSG", arg);
                output.writeObject(p);
            }
            else if (arg instanceof JoinChannelMsg) {
                Packet p = new Packet("JNC-MSG", arg);
                output.writeObject(p);
            }
            else if (arg instanceof NewUserMsg) {
                Packet p = new Packet("NWU-MSG", arg);
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

    public List<String> getAllChannels() {
        return allChannels;
    }

    public HashMap<String, List<Serializable>> getChatHistory() {
        return chatHistory;
    }
}
