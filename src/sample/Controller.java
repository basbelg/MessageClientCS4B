package sample;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import Messages.*;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;


public class Controller
{
    private Client client = new Client(this);
    public Button addPicButton;
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
        String text = inputField.getText();
        inputField.clear();
        ChannelMsg cm = new ChannelMsg(text, currentChannel);
        client.update(cm);
    }

    public void update(Serializable arg)
    {
        SwingUtilities.invokeLater(() -> {
            if(arg instanceof RegistrationMsg)
            {
                outField.getItems().add(new Label(((RegistrationMsg) arg).getUsername() + " has joined the chat!"));
                initChatroomBar(((RegistrationMsg) arg).getSubscribedChannels());
            }
            else if(arg instanceof ChannelMsg)
            {
                outField.getItems().add(new Label(((ChannelMsg) arg).getSender() + ": " + ((ChannelMsg) arg).getTextMsg()));
            }
            else if(arg instanceof PictureMsg)
            {
                try
                {
                    ByteArrayInputStream bis = new ByteArrayInputStream(((PictureMsg) arg).getPicData());
                    BufferedImage bufImg = ImageIO.read(bis);
                    Image image = SwingFXUtils.toFXImage(bufImg, null);
                    ImageView iv = new ImageView(image);
                    double oldVar;
                    if(image.getHeight() > outField.getHeight()/4)
                    {
                        oldVar = iv.getFitHeight();
                        iv.setFitHeight(outField.getHeight()/4);
                        iv.setFitWidth(iv.getFitWidth() - (oldVar - iv.getFitHeight()));
                    }
                    if(image.getWidth() > outField.getWidth()/4)
                    {
                        oldVar = iv.getFitWidth();
                        iv.setFitWidth(outField.getWidth()/4);
                        iv.setFitHeight(iv.getFitHeight() - (oldVar - iv.getFitWidth()));
                    }
                    outField.getItems().add(new Label(((PictureMsg) arg).getSender() + ":"));
                    outField.getItems().add(iv);
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
                        outField.getItems().add(new Label(((ChannelMsg) history.get(i)).getSender() + ": " + ((ChannelMsg) history.get(i)).getTextMsg()));
                    }
                    else if(history.get(i) instanceof RegistrationMsg)
                    {
                        outField.getItems().add(new Label(((RegistrationMsg) history.get(i)).getUsername() + " has joined the chat!"));
                    }
                    else if(history.get(i) instanceof PictureMsg)
                    {
                        try
                        {
                            ByteArrayInputStream bis = new ByteArrayInputStream(((PictureMsg) history.get(i)).getPicData());
                            BufferedImage bufImg = ImageIO.read(bis);
                            Image image = SwingFXUtils.toFXImage(bufImg, null);
                            ImageView iv = new ImageView(image);
                            double oldVar;
                            if(image.getHeight() > outField.getHeight()/4)
                            {
                                oldVar = iv.getFitHeight();
                                iv.setFitHeight(outField.getHeight()/4);
                                iv.setFitWidth(iv.getFitWidth() - (oldVar - iv.getFitHeight()));
                            }
                            if(image.getWidth() > outField.getWidth()/4)
                            {
                                oldVar = iv.getFitWidth();
                                iv.setFitWidth(outField.getWidth()/4);
                                iv.setFitHeight(iv.getFitHeight() - (oldVar - iv.getFitWidth()));
                            }
                            outField.getItems().add(new Label(((PictureMsg) arg).getSender() + ":"));
                            outField.getItems().add(iv);
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    public void uploadPicClicked() throws IOException
    {
        Stage stage = (Stage) addPicButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(stage);
        BufferedImage bufImg = ImageIO.read(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bufImg, "jpg", bos);
        byte [] picData = bos.toByteArray();
        PictureMsg pm = new PictureMsg(picData, currentChannel);
        client.update(pm);
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
        currentChannel = channels.get(0);
        client.update(rm);
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
    }

    public void initChatroomBar(List<String> channels)
    {
        for(int i = 0; i < channels.size(); i++)
        {
            chatroomsBar.getItems().add(channels.get(i));
        }
    }

    public void onNameEntered()
    {
        if(!loginUserField.getText().equals(""))
        {
            loginButton.setDisable(false);
        }
        else
        {
            loginButton.setDisable(true);
        }
    }
}


