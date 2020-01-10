package bgu.spl.net.impl.stomp;

import bgu.spl.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl.net.srv.Server;

public class StompServer {

    public static void main(String[] args) {

        int port = Integer.parseInt(args[0]);
        String serverType = args[1];

        switch (serverType){
            case("tcp"):{
                Server.threadPerClient(
                        port,
                        () -> new StompMessagingProtocolImpl(), //protocol factory
                        StompEncoderDecoder::new //stomp encoder decoder factory
                ).serve();
                break;
            }
            case("reactor"):{
                Server.reactor(
                        Runtime.getRuntime().availableProcessors(),
                        port,
                        () ->  new StompMessagingProtocolImpl(), //protocol factory
                        StompEncoderDecoder::new //stomp encoder decoder factory
                ).serve();
                break;
            }
        }
    }
}
