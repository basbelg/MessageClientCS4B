package sample;

import Messages.RegistrationMsg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Login implements BaseController
{
    private Client client = new Client(this);
    public Button loginButton;
    public TextField loginUserField;
    public CheckBox chat1;
    public CheckBox chat2;
    public CheckBox chat3;
    public CheckBox chat4;
    public CheckBox chat5;
    public CheckBox chat6;

    public void loginClicked() {
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

        if(channels.size() != 0) {
            RegistrationMsg rm = new RegistrationMsg(user, channels.get(0), channels);
            try {
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("chatrooms.fxml"));
                Parent root = loader.load();
                Controller con = loader.getController();
                con.getDataFromLogin(client, rm, channels.get(0), user);
                stage = new Stage();
                stage.setTitle("Chatrooms");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    @Override
    public void update(Serializable arg) {
        System.out.print("SHOULD NOT BE UPDATING LOGIN\n");
    }
}
