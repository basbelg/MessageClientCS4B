package sample;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.ArrayList;
import java.util.List;
import Messages.*;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Stage;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;


public class Controller
{
    private Client client = new Client(this);
    public TextField inputField;
    public Button sendMessageButton;
    public ListView outField;
    public ComboBox chatroomsBar;
    public Label userLabel;
    private String currentChannel;
    public Label channelLabel;
    public Button loginButton;
    public TextField loginUserField;
    public CheckBox chat1;
    public CheckBox chat2;
    public CheckBox chat3;
    public CheckBox chat4;
    public CheckBox chat5;
    public CheckBox chat6;


    public void sendButtonClicked()
    {
        String text = inputField.getText() + "\n";
        inputField.clear();
        ChannelMsg cm = new ChannelMsg(text, currentChannel);
    }

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


    public void uploadPicClicked() throws IOException
    {
        FileChooser fileChooser = new FileChooser();

    }

    public void swapButtonClicked()
    {
        String swapTo = chatroomsBar.getAccessibleText();
        ChangeChannelMsg cc = new ChangeChannelMsg(swapTo);
        client.update(cc);
    }


    public void loginClicked()
    {
        String user = loginUserField.getText();
        List<String> channels = new ArrayList<>();

        if(chat1.isSelected())
        {
            channels.add(chat1.getText());
        }
        if(chat2.isSelected())
        {
            channels.add(chat2.getText());
        }
        if(chat3.isSelected())
        {
            channels.add(chat3.getText());
        }
        if(chat4.isSelected())
        {
            channels.add(chat4.getText());
        }
        if(chat5.isSelected())
        {
            channels.add(chat5.getText());
        }
        if(chat6.isSelected())
        {
            channels.add(chat6.getText());
        }
        RegistrationMsg rm = new RegistrationMsg(user, channels.get(0), channels);
        try
        {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
            stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("chatrooms.fxml"));
            stage.setTitle("Chatrooms");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        client.update(rm);
    }
}
