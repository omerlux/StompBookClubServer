package bgu.spl.net.srv;


public class ConnectionsImpl<T> implements Connections<T>{


    @Override
    /**
     * Sends message T to client represented by the given connection ID.
     * when failing to send a message to client returns -> FALSE
     * else returns -> TRUE
     **/
    public boolean send(int connectionId, T msg) {
        //TODO: implement
        return false;
    }

    @Override
    /**
     * Sends message T to client subscribed to that spesific channel == topic.
     * **/
    public void send(String channel, T msg) {
        //TODO: implement
    }

    @Override
    /**
     * Removes an active client represented by the given connection ID from the map.
     * **/
    public void disconnect(int connectionId) {
        //TODO: implement
    }
}
