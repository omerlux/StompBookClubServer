package bgu.spl.net.impl.stomp.frames;

import bgu.spl.net.impl.stomp.UsersControl;
import bgu.spl.net.srv.Connections;

public class AcknowledgeMsg implements Message {
    //------------------- start edit 7/1 ------------------------
    private int ackParam;
    private String msgData;
    //------------------- end edit 7/1 --------------------------

    public AcknowledgeMsg(String msgData){
        //------------------- start edit 7/1 ------------------------
        this.msgData=msgData;
        //------------------- end edit 7/1 --------------------------
    }

    @Override       /** Not in use **/
    public void process(int connectionID, Connections connections) {
    }

    @Override
    public String getMessageData() {
        //------------------- start edit 7/1 ------------------------
        return msgData;
        //------------------- end edit 7/1 --------------------------
    }
}
