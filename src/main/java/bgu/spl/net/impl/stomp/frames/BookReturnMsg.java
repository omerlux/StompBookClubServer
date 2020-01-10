package bgu.spl.net.impl.stomp.frames;

import bgu.spl.net.impl.stomp.StompMessagingProtocolImpl;
import bgu.spl.net.impl.stomp.UsersControl;
import bgu.spl.net.srv.Connections;

public class BookReturnMsg implements Message {
    //------------------- start edit 7/1 ------------------------
    private String destination_topic;
    private String bookname;
    private String book_loaner;
    private String original_msg;
    //------------------- end edit 7/1 --------------------------

    public BookReturnMsg(String destination_topic, String bookname, String book_loaner, String original_msg){
        //------------------- start edit 7/1 ------------------------
        this.destination_topic = destination_topic;
        this.bookname = bookname;
        this.book_loaner = book_loaner;
        this.original_msg=original_msg;
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

                        "Returning "+bookname+" to "+book_loaner+"\n"+

                        "^@"));                     // sending a message: the book is returned to the loaner
        //------------------- end edit 7/1 --------------------------
    }

    @Override
    public String getMessageData() {
        return original_msg;
    }
}
