package sample;

import Messages.JoinChannelMsg;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class JoinController implements Initializable
{
    private Controller con;
    public Button cancelJoinButton;
    public ListView channelView;
    public Button confirmJoinButton;

    public void onCancelJoinClicked()
    {
        Stage stage = (Stage) confirmJoinButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void displayTable(List<String> subbedChannels, List<String> allChannels)
    {
        for(String channel : allChannels)
        {
            if(!subbedChannels.contains(channel))
            {
                channelView.getItems().add(new Label(channel));
            }
        }
    }

    public void onJoinChannelClicked()
    {
        if (!channelView.getSelectionModel().isEmpty())
        {
            String joinChannel = (String) channelView.getSelectionModel().getSelectedItems().get(0);
            JoinChannelMsg jm = new JoinChannelMsg(joinChannel);
            con.getClient().update(jm);
        }
    }

    public void connectController(Controller c)
    {
        con = c;
    }
}
