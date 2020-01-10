package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.stomp.frames.*;

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
        if (nextByte=='\u0000')                            //End of message, ^@ = \u0000
            return popStringToMsg();
        pushByte(nextByte);
        return null;                                      //Still not the end
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
        String msg = ((Message)message).getMessageData();   // this message is Message for sure
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
     * Convert bytes[] to string and then create a message object
     * @return The message
     */
    private Message popStringToMsg() {
        //------------------- start edit 10/1 ------------------------
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        //We want to return a message object, so we will check what kind of message we received and create the right message object
        Message result = null;
        String resultStr = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        String [] lines = resultStr.split("[\n]");
        if (resultStr!=null) {
                switch (lines[0]) {
                    case ("CONNECTED"): {
                        String userName = lines[3].split("[:]")[1];
                        String password = lines[4].split("[:]")[1];
                        String accept_ver = lines[1].split("[:]")[1];
                        result = new LoginMsg(userName, password, accept_ver, resultStr);
                        break;
                    }
                    case ("SUBSCRIBE"): {
                        String destination_topic = lines[1].split("[:]")[1];
                        Integer id_givenByUser = Integer.parseInt(lines[2].split("[:]")[1]);
                        String receipt = lines[3].split("[:]")[1];
                        result = new SubscribeMsg(destination_topic, id_givenByUser, receipt, resultStr);
                        break;
                    }
                    case ("UNSUBSCRIBE"): {
                        Integer id_givenByUser = Integer.parseInt(lines[1].split("[:]")[1]);
                        String receipt = lines[2].split("[:]")[1];
                        result = new UnsubscribeMsg(id_givenByUser, receipt, resultStr);
                        break;
                    }
                    case ("SEND"): {
                        String destination_topic = lines[1].split("[:]")[1];
                        if (lines[2].contains("added")) {
                            String book_name = lines[2].split("has added the book ")[1];       //Splits the text from the word "book" and after
                            result = new BookAddMsg(destination_topic, book_name, resultStr);
                        } else if (lines[2].contains("borrow")) {
                            String book_name = lines[2].split("wish to borrow ")[1];          //Splits the text from the word "borrow" and after
                            result = new BookBorrowAsk(destination_topic, book_name, resultStr);
                        } else if (lines[2].contains("has")) {
                            String[] msgDetails = lines[2].split("[ ]");
                            String bookname = "";
                            for (int i = 2; i < msgDetails.length - 1; i++) {                             //If the book name is more that one word, then we will create a string for the name with for loop
                                bookname = msgDetails[i];
                                if (i < msgDetails.length - 2)
                                    bookname = bookname + " ";
                            }
                            String potential_giver = msgDetails[0];
                            result = new BookBorrowFound(destination_topic, bookname, potential_giver, resultStr);
                        } else if (lines[2].contains("Taking")) {
                            String[] msgDetails = lines[2].split("[ ]");
                            String bookname = "";
                            for (int i = 1; i < msgDetails.length - 2; i++) {                             //If the book name is more that one word, then we will create a string for the name with for loop
                                bookname = msgDetails[i];
                                if (i < msgDetails.length - 3)
                                    bookname = bookname + " ";
                            }
                            String book_giver = msgDetails[msgDetails.length - 1];
                            result = new BookBorrowSent(destination_topic, bookname, book_giver, resultStr);
                        } else if (lines[2].contains("Returning")) {
                            String[] msgDetails = lines[2].split("[ ]");
                            String bookname = "";
                            for (int i = 1; i < msgDetails.length - 2; i++) {                             //If the book name is more that one word, then we will create a string for the name with for loop
                                bookname = msgDetails[i];
                                if (i < msgDetails.length - 3)
                                    bookname = bookname + " ";
                            }
                            String book_loaner = msgDetails[msgDetails.length - 1];
                            result = new BookReturnMsg(destination_topic, bookname, book_loaner, resultStr);
                        } else if (lines[2].contains("status")) {
                            result = new BookStatusAsk(destination_topic, resultStr);
                        } else {
                            String bookList = lines[2].split("[:]")[1];
                            result = new BookStatusSent(destination_topic, bookList, resultStr);
                        }
                        break;
                    }
                    case ("DISCONNECT"): {
                        String receipt = lines[1].split("[:]")[1];
                        result = new LogoutMsg(receipt, resultStr);
                        break;
                    }
                }
        }
        return result;
        //------------------- end edit 10/1 --------------------------
    }
}

