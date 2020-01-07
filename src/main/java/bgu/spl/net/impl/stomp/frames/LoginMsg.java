package bgu.spl.net.impl.stomp.frames;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.impl.stomp.UsersControl;
import bgu.spl.net.srv.Connections;

/**
 * this object will be created when cleint sends "CNNECT"
 */
public class LoginMsg implements Message {
    //------------------- start edit 7/1 ------------------------
    private String userName;
    private String userPassword;
    private double accept_version;
    private String original_msg;
    //------------------- end edit 7/1 --------------------------

    public LoginMsg(String userName, String userPassword,double accept_version, String original_msg) {
        //------------------- start edit 7/1 ------------------------
        this.userName=userName;
        this.userPassword=userPassword;
        this.accept_version=accept_version;
        this.original_msg=original_msg;
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
                                "^@"    //sending ACK message to the client
                        ));
            }
            case "logged on" : {
                connections.send(connectionID, new ErrorMsg("","User already logged in",
                        getMessageData(),"User already logged in."));

            }
            case "wrong pass" : {
                connections.send(connectionID, new ErrorMsg("","Wrong paswword",
                        getMessageData(),"User is exist, but trying to login with wrong password."));
            }
        }
        //------------------- end edit 7/1 --------------------------
    }

    @Override
    public String getMessageData() {
        //------------------- start edit 7/1 ------------------------
        return original_msg;
        //------------------- end edit 7/1 --------------------------
    }
}
