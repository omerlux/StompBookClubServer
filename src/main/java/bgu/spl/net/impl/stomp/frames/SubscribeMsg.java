package bgu.spl.net.impl.stomp.frames;

import bgu.spl.net.impl.stomp.UsersControl;
import bgu.spl.net.srv.Connections;

public class SubscribeMsg implements Message {
    //------------------- start edit 7/1 ------------------------
    private String destination_topic;
    private Integer id_givenByUser;
    private String receipt;
    private String orig_msg_from_client;
    //------------------- end edit 7/1 --------------------------

    public SubscribeMsg(String destination_topic, Integer id_givenByUser, String receipt,String orig_msg_from_client){
        //------------------- start edit 7/1 ------------------------
        this.destination_topic=destination_topic;
        this.id_givenByUser=id_givenByUser;
        this.receipt=receipt;
        this.orig_msg_from_client=orig_msg_from_client;
        //------------------- end edit 7/1 --------------------------
    }

    @Override
    public void process(int connectionID, Connections connections) {
        //------------------- start edit 7/1 ------------------------
        UsersControl.getInstance().joinGenre(connectionID,destination_topic,id_givenByUser);
        connections.send(connectionID,new ReceiptMsg(receipt));
        //------------------- end edit 7/1 --------------------------
    }

    @Override
    public String getMessageData() {
        return null;
    }
}
