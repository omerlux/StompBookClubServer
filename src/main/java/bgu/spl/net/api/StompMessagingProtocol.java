package bgu.spl.net.api;

import bgu.spl.net.impl.stomp.frames.Message;
import bgu.spl.net.srv.Connections;

public interface StompMessagingProtocol  {
	/**
	 * Used to initiate the current client protocol with it's personal connection ID and the connections implementation
	**/
    void start(int connectionId, Connections<Message> connections);

	/**
	 * @param message
	 * As in messaging protocol, processes a given message.
	 * Unlike messaging protocol, responses are sent via the connection objects send functions (if needed).
	 */
	void process(Message message);
	
	/**
     * @return true if the connection should be terminated
     */
    boolean shouldTerminate();
}
