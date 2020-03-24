package sample;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.List;
import Interfaces.ClientListener;
import Interfaces.ControllerListener;
import Messages.*;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.MediaView;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;


public class Controller implements ClientListener //2 listeners client <-> controller
{
    private ControllerListener client;
    public TextField inputField;
    public Button sendMessageButton;
    public ListView outField;
    public ComboBox chatroomsBar;
    private String currentChannel;
    public Label channelLabel;

    public Controller()
    {

    }

    public void sendButtonClicked()
    {
        String text = inputField.getText() + "\n";
        inputField.clear();
        ChannelMsg cm = new ChannelMsg(text, currentChannel);
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
            outField.getItems().add(new Label(((RegistrationMsg) arg).getUsername() + " has joined the chat!\n"));
            //update chatbar
        }
        else if(arg instanceof ChannelMsg)
        {
            outField.getItems().add(new Label(((ChannelMsg) arg).getSender() + ": " + ((ChannelMsg) arg).getTextMsg() + "\n"));
        }
        else if(arg instanceof PictureMsg)
        {
            try {
                Label img = new Label();
                ByteArrayInputStream bis = new ByteArrayInputStream(((PictureMsg) arg).getPicData());
                BufferedImage bImage2 = ImageIO.read(bis);
                img.setGraphic(new ImageView(String.valueOf(bImage2)));
                outField.getItems().add(img);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else if(arg instanceof ChangeChannelMsg)
        {
            currentChannel = ((ChangeChannelMsg) arg).getSwappedChannel();
            channelLabel.setText("Channel: " + currentChannel);
            outField.getItems().clear();
            List<Serializable> history = ((ChangeChannelMsg) arg).getChatHistory();
            for(int i = 0; i < history.size(); i++)
            {
                if(history.get(i) instanceof ChannelMsg)
                {
                    outField.getItems().add(new Label(((ChannelMsg) history.get(i)).getSender() + ": " + ((ChannelMsg) history.get(i)).getTextMsg() + "\n"));
                }
                else if(history.get(i) instanceof RegistrationMsg)
                {
                    outField.getItems().add(new Label(((RegistrationMsg) history.get(i)).getUsername() + " has joined the chat!\n"));
                }
                else if(history.get(i) instanceof PictureMsg)
                {

                }

            }
        }
    }

    public void notifyObserver(Object arg)
    {
        client.update(arg);
    }

    public void uploadPicClicked() throws IOException
    {
        FileChooser fileChooser = new FileChooser();

    }

    public void swapButtonClicked()
    {
        String swapTo = chatroomsBar.getAccessibleText();
        ChangeChannelMsg cc = new ChangeChannelMsg(swapTo);
        notifyObserver(cc);
    }
}
