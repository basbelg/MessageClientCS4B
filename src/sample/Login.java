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

    public void loginClicked() throws IOException {
        String user = loginUserField.getText();
        List<String> channels = new ArrayList<>();

        RegistrationMsg rm = new RegistrationMsg(user);
        try
        {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("chatrooms.fxml"));
            Parent root = loader.load();
            Controller con = loader.getController();
            stage = new Stage();
            stage.setTitle("Chatrooms");
            stage.setScene(new Scene(root));
            stage.show();
            con.getDataFromLogin(client, rm, channels.get(0));
        }
        catch(Exception e)
        {
            e.printStackTrace();
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
