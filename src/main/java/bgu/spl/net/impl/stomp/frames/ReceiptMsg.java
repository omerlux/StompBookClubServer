package bgu.spl.net.impl.stomp.frames;

import bgu.spl.net.srv.Connections;

public class ReceiptMsg implements Message {
    //------------------- start edit 7/1 ------------------------
    private String receipt_id;
    private boolean disconnectMsg;
    //------------------- end edit 7/1 --------------------------

    public ReceiptMsg(String receipt_id, boolean disconnectMsg){
        //------------------- start edit 7/1 ------------------------
        this.receipt_id = receipt_id;
        this.disconnectMsg = disconnectMsg;
        //------------------- end edit 7/1 --------------------------
    }
    @Override
    public void process(int connectionID, Connections connections) {

    }

    @Override
    public String getMessageData() {
        return("RECEIPT\n" +
                "receipt-id: "+receipt_id+"\n\n"+
                "\u0000");
    }

    public boolean getDisconnectMsg(){
        //------------------- start edit 10/1 ------------------------
        return disconnectMsg;
        //------------------- end edit 10/1 --------------------------
    }
}
