package Messages;

import java.io.Serializable;
import java.util.List;

public class RegistrationMsg implements Serializable {
    private String username;
    private List<String> channels;

    public RegistrationMsg(String username, List<String> subscribedChannels) {
        this.username = username;
        this.channels = subscribedChannels;
    }

    public String getUsername() {
        return username;
    }

    public String toString() {return username + " has connected";}

    public List<String> getSubscribedChannels() {
        return channels;
    }
}
