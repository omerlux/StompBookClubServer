package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.MessageEncoderDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class StompEncoderDecoder implements MessageEncoderDecoder {

    /** Comment to us: Since we are reading and sending strings, this class is pretty much the same
     * as the encoder-decoder of the echo. Maybe still more things need to be add? 7/1
     */


    //------------------- start edit 7/1 ------------------------
    private byte[] bytes = new byte[1 << 10]; //Start with 1K
    private int len  = 0;
    //------------------- end edit 7/1 --------------------------

    /**
     * Checks the next byte reading
     * @param nextByte the next byte to consider for the currently decoded
     * message
     * @return String if we finish to read msg, or null otherwise
     */
    @Override
    public Object decodeNextByte(byte nextByte) {
        //------------------- start edit 7/1 ------------------------
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (nextByte=='@')                            //End of message
            return popString();
        pushByte(nextByte);
        return null;                                 //Still not the end
        //------------------- end edit 7/1 --------------------------
    }

    /**
     * Convert string to bytes[]
     * @param message the message to encode
     * @return The message in bytes
     */
    @Override
    public byte[] encode(Object message) {
        //------------------- start edit 7/1 ------------------------
        String msg = (String)message;
        return msg.getBytes();
        //------------------- end edit 7/1 --------------------------
    }

    /**
     * Make bytes[] bigger id needed, or just read the next byte
     * @param nextByte
     */
    private void pushByte(byte nextByte) {
        //------------------- start edit 7/1 ------------------------
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
        //------------------- end edit 7/1 --------------------------
    }

    /**
     * Convert bytes[] to string
     * @return The message
     */
    private String popString() {
        //------------------- start edit 7/1 ------------------------
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
        //------------------- end edit 7/1 --------------------------
    }
}

