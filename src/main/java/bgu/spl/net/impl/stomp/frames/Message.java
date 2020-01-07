package bgu.spl.net.impl.stomp.frames;

import bgu.spl.net.impl.stomp.UsersControl;
import bgu.spl.net.srv.Connections;

/**
 * This abstract class is for all kinds of messages that client can send to the server.
 */
public interface Message {

    /**
     * Each message has it's own process that it will do.
     * @param connectionID
     * @param connections
     */
    public void process(int connectionID, Connections connections);

    /**
     * @return the string of the message
     */
    String getMessageData();


}
