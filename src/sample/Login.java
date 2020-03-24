package sample;

import Interfaces.ControllerListener;
import Messages.RegistrationMsg;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Login
{
    private ControllerListener client;
    public Button loginButton;
    public TextField loginUserField;
    public CheckBox chat1;
    public CheckBox chat2;
    public CheckBox chat3;
    public CheckBox chat4;
    public CheckBox chat5;
    public CheckBox chat6;

    public void addClient(Client c)
    {
        client = c;
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
        RegistrationMsg rm = new RegistrationMsg(user, channels);
        notifyObserver(rm);
    }

    public void notifyObserver(RegistrationMsg args)
    {
        client.update(args);
    }


}
