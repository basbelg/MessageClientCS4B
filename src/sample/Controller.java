package sample;

import Messages.*;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable, BaseController
{
    private Client client;
    public Button addPicButton;
    public TextField inputField;
    public Button sendMessageButton;
    public ListView outField;
    public ComboBox chatroomsBar;
    public Label userLabel;
    public Label serverLabel;
    private String currentChannel;
    public Label channelLabel;
    public Button joinButton;
    public Button createButton;
    public TextField channelNameField;
    public Button submitButton;
    public Button confirmJoinButton;
    public Button cancelCreateButton;
    public Button cancelJoinButton;
    public ListView channelView;


    public void sendButtonClicked() {
        String text = inputField.getText() + "\n";
        inputField.clear();
        ChannelMsg cm = new ChannelMsg(text, currentChannel);
        client.update(cm);
    }

    public void update(Serializable arg)
    {
        Platform.runLater(() -> {
            if(arg instanceof NewUserMsg)
            {
                if(currentChannel.equals(((NewUserMsg) arg).getToChannel()))
                {
                    outField.getItems().add(new Label(((NewUserMsg) arg).getNewUser() + " has joined the chat!"));
                }
            }
            else if(arg instanceof CreateChannelMsg)
            {

            }
            else if(arg instanceof JoinChannelMsg)
            {
                currentChannel = ((JoinChannelMsg) arg).getJoinChannel();
                channelLabel.setText("Channel: " + currentChannel);
                outField.getItems().clear();
                List<Serializable> history = ((JoinChannelMsg) arg).getChatHistory();

                initChatroomBar(((JoinChannelMsg) arg).getJoinChannel());

                for(int i = 0; i < history.size(); i++)
                {
                    if(history.get(i) instanceof ChannelMsg)
                    {
                        outField.getItems().add(new Label(((ChannelMsg) history.get(i)).getSender() + ": " + ((ChannelMsg) history.get(i)).getTextMsg()));
                    }
                    else if(history.get(i) instanceof NewUserMsg)
                    {
                        outField.getItems().add(new Label(((NewUserMsg) history.get(i)).getNewUser() + " has joined the chat!"));
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
            else if(arg instanceof ChannelMsg)
            {
                outField.getItems().add(new Label(((ChannelMsg) arg).getSender() + ": " + ((ChannelMsg) arg).getTextMsg()));
            }
            else if (arg instanceof PictureMsg) {
                try {
                    ByteArrayInputStream bis = new ByteArrayInputStream(((PictureMsg) arg).getPicData());
                    BufferedImage bufImg = ImageIO.read(bis);
                    Image image = SwingFXUtils.toFXImage(bufImg, null);
                    ImageView iv = new ImageView(image);
                    double oldVar;
                    if (image.getHeight() > outField.getHeight() / 4) {
                        oldVar = iv.getFitHeight();
                        iv.setFitHeight(outField.getHeight() / 4);
                        iv.setFitWidth(iv.getFitWidth() - (oldVar - iv.getFitHeight()));
                    }
                    if (image.getWidth() > outField.getWidth() / 4) {
                        oldVar = iv.getFitWidth();
                        iv.setFitWidth(outField.getWidth() / 4);
                        iv.setFitHeight(iv.getFitHeight() - (oldVar - iv.getFitWidth()));
                    }
                    outField.getItems().add(new Label(((PictureMsg) arg).getSender() + ":"));
                    outField.getItems().add(iv);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (arg instanceof ChangeChannelMsg) {
                currentChannel = ((ChangeChannelMsg) arg).getSwappedChannel();
                channelLabel.setText("Channel: " + currentChannel);
                outField.getItems().clear();
                List<Serializable> history = ((ChangeChannelMsg) arg).getChatHistory();
                for (int i = 0; i < history.size(); i++) {
                    if (history.get(i) instanceof ChannelMsg) {
                        outField.getItems().add(new Label(((ChannelMsg) history.get(i)).getSender() + ": " + ((ChannelMsg) history.get(i)).getTextMsg()));
                    }
                    else if(history.get(i) instanceof NewUserMsg)
                    {
                        outField.getItems().add(new Label(((NewUserMsg) history.get(i)).getNewUser() + " has joined the chat!"));
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
                            if (image.getHeight() > outField.getHeight() / 4) {
                                oldVar = iv.getFitHeight();
                                iv.setFitHeight(outField.getHeight() / 4);
                                iv.setFitWidth(iv.getFitWidth() - (oldVar - iv.getFitHeight()));
                            }
                            if (image.getWidth() > outField.getWidth() / 4) {
                                oldVar = iv.getFitWidth();
                                iv.setFitWidth(outField.getWidth() / 4);
                                iv.setFitHeight(iv.getFitHeight() - (oldVar - iv.getFitWidth()));
                            }
                            outField.getItems().add(new Label(((PictureMsg) history.get(i)).getSender() + ":"));
                            outField.getItems().add(iv);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else {
                System.out.println("Controller Update ERROR: No matching message type");
            }
        });
    }

    public void uploadPicClicked() throws IOException {
        Stage stage = (Stage) addPicButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("jpg files", "*.jpg"));
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(stage);
        BufferedImage bufImg = ImageIO.read(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bufImg, "jpg", bos);
        byte [] picData = bos.toByteArray();
        PictureMsg pm = new PictureMsg(picData, currentChannel);
        client.update(pm);
    }

    public void swapButtonClicked() {
        if(((String)chatroomsBar.getValue()) != null) {
            currentChannel = (String) chatroomsBar.getValue();
            ChangeChannelMsg cc = new ChangeChannelMsg(currentChannel);
            client.update(cc);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public void getDataFromLogin(Client c, RegistrationMsg rm, String username) {
        client = c;
        client.SetController(this);
        client.update(rm);
        JoinChannelMsg jcm = new JoinChannelMsg(rm.getUsername());
        jcm.setJoinChannel("welcome");
        client.update(jcm);
        client.setUsername(username);

        userLabel.setText(username);
        serverLabel.setText("Server: Connected!");
    }

    public void initChatroomBar(String channel)
    {
        chatroomsBar.getItems().add(channel);
    }

    public void joinButtonClicked() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("joinChannel.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Join Channel");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onJoinChannelClicked()
    {
        channelView.getSelectionModel();
    }

    public void createButtonClicked() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("createChannel.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Create Channel");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onSubmitClicked()
    {
        Stage stage = (Stage) submitButton.getScene().getWindow();
        CreateChannelMsg cm = new CreateChannelMsg(channelNameField.getText());
        client.update(cm);
        stage.close();
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

    public void onCancelJoinClicked()
    {
        Stage stage = (Stage) confirmJoinButton.getScene().getWindow();
        stage.close();
    }
}


