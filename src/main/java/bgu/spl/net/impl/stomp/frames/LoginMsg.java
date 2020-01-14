package bgu.spl.net.impl.stomp.frames;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.impl.stomp.UsersControl;
import bgu.spl.net.srv.Connections;

/**
 * this object will be created when client sends "CONNECT"
 */
public class LoginMsg implements Message {
    //------------------- start edit 7/1 ------------------------
    private String userName;
    private String userPassword;
    private String accept_version;
    private String orig_msg_from_client;
    //------------------- end edit 7/1 --------------------------

    public LoginMsg(String userName, String userPassword,String accept_version, String orig_msg_from_client) {
        //------------------- start edit 7/1 ------------------------
        this.userName=userName;
        this.userPassword=userPassword;
        this.accept_version=accept_version;
        this.orig_msg_from_client=orig_msg_from_client;
        //------------------- end edit 7/1 --------------------------
    }

    @Override
    public void process(int connectionID, Connections connections) {
        //------------------- start edit 7/1 ------------------------
        String server_ans = UsersControl.getInstance().login(connectionID,userName,userPassword);
        switch(server_ans) {
            case "connected" : {
                connections.send(connectionID, new AcknowledgeMsg(
                        "CONNECTED\n" +
                                "version: "+accept_version+"\n" +
                                "\n" +
                                "\u0000"    //sending ACK message to the client
                        ));
                break;
            }
            case "logged on" : {
                connections.send(connectionID, new ErrorMsg("","already logged on",
                        getMessageData(),"User already logged in."));
                break;
            }
            case "logged on with another username" :{           // 11/1
                connections.send(connectionID, new ErrorMsg("","logged in with another user",
                        getMessageData(),"Some user is already logged in from this client."));
                break;
            }
            case "wrong pass" : {
                connections.send(connectionID, new ErrorMsg("","wrong password",
                        getMessageData(),"User is exist, but trying to login with wrong password."));
                break;
            }
        }
        //------------------- end edit 7/1 --------------------------
    }

    @Override
    public String getMessageData() {
        //------------------- start edit 7/1 ------------------------
        return orig_msg_from_client;        // THIS IS AN OLD MESSAGE FROM CLIENT. NOT THIS MESSAGE ITSELF
        //------------------- end edit 7/1 --------------------------
    }
}
