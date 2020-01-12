package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.impl.stomp.ConnectionsImpl;
import bgu.spl.net.impl.stomp.StompMessagingProtocolImpl;
import bgu.spl.net.impl.stomp.frames.Message;
import bgu.spl.net.impl.stomp.frames.ReceiptMsg;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final StompMessagingProtocol protocol;      //change to StompMessagingProtocol 10/1
    private final MessageEncoderDecoder<T> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;


    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader, StompMessagingProtocol protocol, Connections connections, int connectionId) {         //change to StompMessagingProtocol 10/1
        //------------------- start edit 11/1 ------------------------
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        this.protocol.start(connectionId,connections);
        //------------------- start edit 11/1 ------------------------
    }

    @Override
    public void run() {
        try (Socket sock = this.sock) { //just for automatic closing
            int read;

            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());

            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                T nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    protocol.process((Message)nextMessage);
                    /**                                     //NO response. this is a void function.
                    T response = protocol.process(nextMessage);
                    if (response != null) {
                        out.write(encdec.encode(response));
                        out.flush();
                    }
                     */
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }

    @Override
    /**
     * Sends message T to the client (through the socket ?)
     * should be used by the send commands in the connections implementations
     */
    public void send(T msg) {
        //------------------- start edit 10/1 ------------------------
        try {
            out.write(encdec.encode(msg));      //writing string to stream
            out.flush();                        //sending the msg
            if (msg instanceof ReceiptMsg)
                if (((ReceiptMsg) msg).getDisconnectMsg())
                    ((StompMessagingProtocolImpl)protocol).terminate(); //termination for run() function
        } catch (IOException e) {
            e.printStackTrace();
        }
        //------------------- end edit 10/1 --------------------------
    }
}
