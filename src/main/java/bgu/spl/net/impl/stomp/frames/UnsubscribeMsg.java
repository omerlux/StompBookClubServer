package bgu.spl.net.impl.stomp.frames;

import bgu.spl.net.impl.stomp.User;
import bgu.spl.net.impl.stomp.UsersControl;
import bgu.spl.net.srv.Connections;

public class UnsubscribeMsg implements Message {
    //------------------- start edit 7/1 ------------------------
    private Integer id_givenByUser;
    private String receipt;
    private String orig_msg_from_client;
    //------------------- end edit 7/1 --------------------------

    public UnsubscribeMsg(Integer id_givenByUser, String receipt,String orig_msg_from_client){
        //------------------- start edit 7/1 ------------------------
        this.id_givenByUser=id_givenByUser;
        this.receipt=receipt;
        this.orig_msg_from_client=orig_msg_from_client;
        //------------------- end edit 7/1 --------------------------
    }

    @Override
    public void process(int connectionID, Connections connections) {
        User curr_user = UsersControl.getInstance().getUserByConnectionId(connectionID);
        if(!curr_user.getConnected_successfully()){
            connections.send(connectionID, new ErrorMsg("","Didn't logged in",
                    orig_msg_from_client,"The user isn't logged in yet."));
        }
        else {
            UsersControl.getInstance().exitGenre(connectionID, id_givenByUser);  //will remove the user from the genre (called by id)
            connections.send(connectionID, new ReceiptMsg(receipt, false));            //sending a receipt back to the client
        }
    }

    @Override
    public String getMessageData() {
        return null;
    }
}
