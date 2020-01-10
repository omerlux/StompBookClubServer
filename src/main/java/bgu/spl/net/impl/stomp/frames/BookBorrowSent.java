package bgu.spl.net.impl.stomp.frames;

import bgu.spl.net.impl.stomp.StompMessagingProtocolImpl;
import bgu.spl.net.impl.stomp.UsersControl;
import bgu.spl.net.srv.Connections;

public class BookBorrowSent implements Message {
    //------------------- start edit 7/1 ------------------------
    private String destination_topic;
    private String bookname;
    private String book_giver;
    private String orig_msg_from_client;
    //------------------- end edit 7/1 --------------------------

    public BookBorrowSent (String destination_topic, String bookname, String book_giver, String orig_msg_from_client){
        //------------------- start edit 7/1 ------------------------
        this.destination_topic=destination_topic;
        this.bookname=bookname;
        this.book_giver=book_giver;
        this.orig_msg_from_client = orig_msg_from_client;
        //------------------- end edit 7/1 --------------------------
    }

    @Override
    public void process(int connectionID, Connections connections) {
        //------------------- start edit 7/1 ------------------------
        Integer userTopicSubNumber = UsersControl.getInstance().getUserByConnectionId(connectionID).get_SubNum_by_TopicName(destination_topic);
        String userName = UsersControl.getInstance().getUserByConnectionId(connectionID).getName();
        connections.send(destination_topic, new AcknowledgeMsg(
                "MESSAGE\n" +
                        "subscription:" + userTopicSubNumber + "\n" +
                        "Message-id:" + StompMessagingProtocolImpl.getNewMessageId() + "\n" +
                        "destination:" + destination_topic + "\n\n" +

                        "Taking "+bookname+" from "+book_giver+"\n"+

                        "^@"));                     // sending a message: a giver borrowed the user
        //------------------- end edit 7/1 --------------------------
    }

    @Override
    public String getMessageData() {
        return null;
    }
}
