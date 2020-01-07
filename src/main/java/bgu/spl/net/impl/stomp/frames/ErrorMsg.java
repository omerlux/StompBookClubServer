package bgu.spl.net.impl.stomp.frames;

import bgu.spl.net.impl.stomp.UsersControl;
import bgu.spl.net.srv.Connections;

public class ErrorMsg implements Message{
    //------------------- start edit 7/1 ------------------------
    private String receipt_id;
    private String message;
    private String errored_original_msg;
    private String details;
    //------------------- end edit 7/1 --------------------------

    public ErrorMsg(String receipt_id, String message, String errored_original_msg,String details) {
        //------------------- start edit 7/1 ------------------------
        this.receipt_id=receipt_id;
        this.message=message;
        this.errored_original_msg=errored_original_msg;
        this.details=details;
        //------------------- end edit 7/1 --------------------------
    }

    @Override
    public void process(int connectionID, Connections connections) {

    }

    @Override
    public String getMessageData() {
        return ("ERROR\n" +
                "receipt-id: "+receipt_id+"\n"+
                "message: "+message+"\n\n" +
                "The message:\n"+
                "-----\n"+
                errored_original_msg +
                "-----\n"+
                details+"\n"+
                "^@");
    }
}
