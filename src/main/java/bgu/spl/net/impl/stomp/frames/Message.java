package bgu.spl.net.impl.stomp.frames;

/**
 * This abstract class is for all kinds of messages that client can send to the server.
 */
public interface Message {

    /**
     *  Each message has it's own process that it will do.
     */
    public void process();



}
