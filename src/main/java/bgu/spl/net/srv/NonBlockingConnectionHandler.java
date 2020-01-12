package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.impl.stomp.ConnectionsImpl;
import bgu.spl.net.impl.stomp.StompMessagingProtocolImpl;
import bgu.spl.net.impl.stomp.frames.Message;
import bgu.spl.net.impl.stomp.frames.ReceiptMsg;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NonBlockingConnectionHandler<T> implements ConnectionHandler<T> {

    private static final int BUFFER_ALLOCATION_SIZE = 1 << 13; //8k
    private static final ConcurrentLinkedQueue<ByteBuffer> BUFFER_POOL = new ConcurrentLinkedQueue<>();

    private final StompMessagingProtocol protocol;      //change to StompMessagingProtocol 10/1
    private final MessageEncoderDecoder<T> encdec;
    private final Queue<ByteBuffer> writeQueue = new ConcurrentLinkedQueue<>();
    private final SocketChannel chan;
    private final Reactor reactor;

    //------------------- start edit 11/1 ------------------------
    public NonBlockingConnectionHandler(
            MessageEncoderDecoder<T> reader,
            StompMessagingProtocol protocol,        //change to StompMessagingProtocol 10/1
            SocketChannel chan,
            Reactor reactor,
            Connections connections,
            int connectionId) {
        //------------------- start edit 11/1 ------------------------
        this.chan = chan;
        this.encdec = reader;
        this.protocol = protocol;
        this.reactor = reactor;
        this.protocol.start(connectionId,connections);
        //------------------- start edit 11/1 ------------------------
    }

    public Runnable continueRead() {
        ByteBuffer buf = leaseBuffer();

        boolean success = false;
        try {
            success = chan.read(buf) != -1;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (success) {
            buf.flip();
            return () -> {
                try {
                    while (buf.hasRemaining()) {
                        T nextMessage = encdec.decodeNextByte(buf.get());
                        if (nextMessage != null) {
                            protocol.process((Message)nextMessage);
                            /**                         //Won't happen - our process is a void function
                            T response = protocol.process(nextMessage);
                            if (response != null) {  Won't happen - our process is a void function
                                writeQueue.add(ByteBuffer.wrap(encdec.encode(response)));
                                reactor.updateInterestedOps(chan, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                            }
                            **/
                        }
                    }
                } finally {
                    releaseBuffer(buf);
                }
            };
        } else {
            releaseBuffer(buf);
            close();
            return null;
        }
    }

    public void close() {
        try {
            chan.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isClosed() {
        return !chan.isOpen();
    }

    public void continueWrite() {
        while (!writeQueue.isEmpty()) {
            try {
                ByteBuffer top = writeQueue.peek();
                chan.write(top);
                if (top.hasRemaining()) {
                    return;
                } else {
                    writeQueue.remove();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                close();
            }
        }

        if (writeQueue.isEmpty()) {
            if (protocol.shouldTerminate()) close();
            else reactor.updateInterestedOps(chan, SelectionKey.OP_READ);
        }
    }

    private static ByteBuffer leaseBuffer() {
        ByteBuffer buff = BUFFER_POOL.poll();
        if (buff == null) {
            return ByteBuffer.allocateDirect(BUFFER_ALLOCATION_SIZE);
        }

        buff.clear();
        return buff;
    }

    private static void releaseBuffer(ByteBuffer buff) {
        BUFFER_POOL.add(buff);
    }

    @Override
    /**
     * Sends message T to the client (through the socket ?)
     * should be used by the send commands in the connections implementations
     */
    public void send(T msg) {
        //------------------- start edit 10/1 ------------------------
        writeQueue.add(ByteBuffer.wrap(encdec.encode(msg)));                                    // adding the msg to write queue
        reactor.updateInterestedOps(chan,SelectionKey.OP_READ | SelectionKey.OP_WRITE);    // changing from R -> R/W
        if (msg instanceof ReceiptMsg)
            if (((ReceiptMsg) msg).getDisconnectMsg())
                ((StompMessagingProtocolImpl)protocol).terminate(); //termination for run() function
        //------------------- end edit 10/1 --------------------------
    }
}
