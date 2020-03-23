package sample;

import javax.swing.*;
import javax.swing.text.html.ImageView;

import Interfaces.ClientListener;
import Messages.*;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.media.MediaView;

import java.util.List;
import java.util.Observable;


public class Controller  implements ClientListener //2 listeners client <-> controller
{
    private List<Client> clients;
    public TextField inputField;
    public Button sendMessageButton;
    public TextArea outField;
    public ComboBox chatroomsBar;
    public String currentChannel;

    public void sendButtonClicked()
    {
        String text = inputField.getText();
        inputField.clear();
        ChannelMsg cm = new ChannelMsg(text, currentChannel);
        notifyObservers(cm);
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
    public void update(Object arg)
    {
        if(arg instanceof RegistrationMsg)
        {
            String text = outField.getText();
            text += ((RegistrationMsg) arg).getUsername() + " has joined the chat!\n";
            outField.setText(text);
            //update chatbar
        }
        else if(arg instanceof ChannelMsg)
        {
            String text = outField.getText();
            text += ((ChannelMsg) arg).getSender() + ": " + ((ChannelMsg) arg).getTextMsg();
            outField.setText(text);
        }
        else if(arg instanceof PictureMsg)
        {

        }
        else if(arg instanceof ChangeChannelMsg)
        {

        }
    }

    public void notifyObservers(Object arg)
    {
        for(Client c : clients)
        {
            c.update(arg);
        }
    }

    public void uploadPicClicked()
    {

    }
}
