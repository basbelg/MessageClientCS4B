package sample;

import javax.swing.*;
import javax.swing.text.html.ImageView;

import Interfaces.ClientListener;
import Messages.ChannelMsg;
import Messages.Packet;
import Messages.RegistrationMsg;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.control.Button;

import java.util.List;
import java.util.Observable;


public class Controller extends Observable implements ClientListener //2 listeners client <-> controller
{
    private List<Client> clients;
    public TextField inputField;
    public Button sendMessageButton;
    public TextArea outField;

    public void sendButtonClicked()
    {
        String text = inputField.getText();
        inputField.clear();

        notifyObservers(text);
    }

    public void addClient(Client c)
    {
        clients.add(c);
    }

    public void removeClient(Client c)
    {
        clients.remove(c);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if(arg instanceof RegistrationMsg)
        {

        }
        else if(arg instanceof ChannelMsg)
        {
            String text = outField.getText();
            text += "\n" + ((ChannelMsg) arg).getSender() + ": " + ((ChannelMsg) arg).getTextMsg();
            outField.setText(text);
        }
    }

    @Override
    public void notifyObservers(Object arg)
    {

    }

    public void uploadPicClicked()
    {

    }
}
