package sample;

import javax.imageio.ImageIO;
import javax.swing.*;

import Interfaces.ClientListener;
import Interfaces.ControllerListener;
import Messages.*;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.MediaView;
import javafx.scene.text.TextFlow;


import java.awt.image.BufferedImage;
import java.io.*;


public class Controller implements ClientListener //2 listeners client <-> controller
{
    private ControllerListener client;
    public TextField inputField;
    public Button sendMessageButton;
    public DialogPane outField;
    public ComboBox chatroomsBar;
    public String currentChannel;


    public void sendButtonClicked()
    {
        String text = inputField.getText() + "\n";
        inputField.clear();
        ChannelMsg cm = new ChannelMsg(text, currentChannel);
inputField.setText("cm message sent");
        notifyObserver(cm);
    }

    public void addClient(ControllerListener c)
    {
        client = c;
    }

    @Override
    public void update(Object arg)
    {
        if(arg instanceof RegistrationMsg)
        {
            String text = outField.getAccessibleText();
            text += ((RegistrationMsg) arg).getUsername() + " has joined the chat!\n";
            outField.setAccessibleText(text);
            //update chatbar
        }
        else if(arg instanceof ChannelMsg)
        {
            StringBuffer text = new StringBuffer(outField.getAccessibleText());
            text.append(((ChannelMsg) arg).getSender() + ": " + ((ChannelMsg) arg).getTextMsg());
            outField.setAccessibleText(text.toString());
        }
        else if(arg instanceof PictureMsg)
        {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(((PictureMsg) arg).getPicData());
                BufferedImage bImage2 = ImageIO.read(bis);
                //push to display
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else if(arg instanceof ChangeChannelMsg)
        {
            currentChannel = ((ChangeChannelMsg) arg).getSwappedChannel();
        }
    }

    public void notifyObserver(Object arg)
    {
        client.update(arg);
    }

    public void uploadPicClicked()
    {
        //open directory and load pic
    }

    public void swapButtonClicked()
    {
        //make changechannelmsg and notify
        String swapTo = chatroomsBar.getAccessibleText();
        ChangeChannelMsg cc = new ChangeChannelMsg(swapTo);
inputField.setText("ccm sent");
        notifyObserver(cc);
    }
}
