package bgu.spl.net.impl.stomp.frames;

import bgu.spl.net.srv.Connections;

public class ReceiptMsg implements Message {
    //------------------- start edit 7/1 ------------------------
    private String receipt_id;
    //------------------- end edit 7/1 --------------------------

    public ReceiptMsg(String receipt_id){
        //------------------- start edit 7/1 ------------------------
        this.receipt_id = receipt_id;
        //------------------- end edit 7/1 --------------------------
    }
    @Override
    public void process(int connectionID, Connections connections) {

    }

    @Override
    public String getMessageData() {
        return("RECEIPT\n" +
                "receipt-id: "+receipt_id+"\n\n"+
                "^@");
    }
}
