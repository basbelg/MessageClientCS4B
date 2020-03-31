package sample;

import Messages.CreateChannelMsg;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateController {
    private Controller con;
    public TextField channelNameField;
    public Button submitButton;
    public Button cancelCreateButton;

    public void onSubmitClicked()
    {
        CreateChannelMsg cm = new CreateChannelMsg(channelNameField.getText());
        System.out.print(cm.getChannelName());

        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
        con.getClient().update(cm);
    }

    public void onChannelNameInChanged()
    {
        if(!channelNameField.getText().equals(""))
        {
            submitButton.setDisable(false);
        }
        else
        {
            submitButton.setDisable(true);
        }
    }

    public void onCancelCreateClicked()
    {
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

    public void connectController(Controller c)
    {
        con = c;
    }
}
