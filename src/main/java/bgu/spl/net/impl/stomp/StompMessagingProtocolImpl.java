package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.impl.stomp.frames.Message;
import bgu.spl.net.srv.Connections;

import java.util.concurrent.atomic.AtomicInteger;

public class StompMessagingProtocolImpl implements StompMessagingProtocol {

    //------------------- start edit 4/1 ------------------------
    private boolean shouldTerminate = false;
    private int connectionId;
    private ConnectionsImpl<Message> connections;
    private static AtomicInteger messageCounter=new AtomicInteger(0);
    //------------------- end edit 4/1 --------------------------

    public StompMessagingProtocolImpl(){
    }

    /**
     * Used to initiate the current client protocol with it's personal connection ID and the connections implementation
     **/
    @Override
    public void start(int connectionId, Connections<Message> connections) {
        //------------------- start edit 4/1 ------------------------
        this.connectionId = connectionId;
        this.connections = (ConnectionsImpl) connections;
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * @param message
     * As in messaging protocol, processes a given message.
     * Unlike messaging protocol, responses are sent via the connection objects send functions (if needed).
     */
    @Override
    public void process(Message message) {
        //------------------- start edit 4/1 ------------------------
        message.process(connectionId,connections);
        //------------------- end edit 4/1 --------------------------

    }

    /**
     * @return true if the connection should be terminated
     */
    @Override
    public boolean shouldTerminate() {
        //------------------- start edit 4/1 ------------------------
        return shouldTerminate;
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * change {@param terminate} to true - will happen at the Message DISCONNECT
     */

    public void terminate(){
        //------------------- start edit 4/1 ------------------------
        this.shouldTerminate=true;
        //------------------- end edit 4/1 --------------------------
    }

    public static int getNewMessageId(){
        return messageCounter.getAndIncrement();
    }
}
