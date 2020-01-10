package bgu.spl.net.impl.stomp.frames;

import bgu.spl.net.impl.stomp.UsersControl;
import bgu.spl.net.srv.Connections;

public class LogoutMsg implements Message {
    //------------------- start edit 7/1 ------------------------
    private String reciept;
    private String orig_msg_from_client;
    //------------------- end edit 7/1 --------------------------

    public LogoutMsg (String reciept, String orig_msg_from_client){
        //------------------- start edit 7/1 ------------------------
        this.reciept = reciept;
        this.orig_msg_from_client=orig_msg_from_client;
        //------------------- end edit 7/1 --------------------------
    }

    @Override
    public void process(int connectionID, Connections connections) {
        //------------------- start edit 7/1 ------------------------
        UsersControl.getInstance().logoutTopicReset(connectionID);      //Remove user from all topic
        connections.send(connectionID, new ReceiptMsg(reciept));        //Send receipt to client
        connections.disconnect(connectionID);                           //Disconnect from connections
        //------------------- end edit 7/1 --------------------------
    }

    @Override
    public String getMessageData() {
        return null;
    }
}
