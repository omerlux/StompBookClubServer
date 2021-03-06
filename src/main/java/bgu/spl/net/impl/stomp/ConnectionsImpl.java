package bgu.spl.net.impl.stomp;

import bgu.spl.net.impl.stomp.frames.AcknowledgeMsg;
import bgu.spl.net.impl.stomp.frames.Message;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.Connections;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConnectionsImpl<T> implements Connections<T> {

    /**
     * @param active_client_map - map of clients connection handler, by the key int id.
     * @param topic_map - map of the specified topic, with it's own clients array.
     * @param id_count - an Atomic Integer to count how many clients are active.
     * @param readWriteLock - a lock for read and write.
     */
    //------------------- start edit 14/1 ------------------------
    private Map<Integer , ConnectionHandler<T>> active_client_map;
    private AtomicInteger id_count;
    private ReadWriteLock readWriteLock;
    private static class SingletonHolder {
        private static ConnectionsImpl connections = new ConnectionsImpl<>();
    }
    //------------------- end edit 14/1 --------------------------

    public ConnectionsImpl(){
        //------------------- start edit 4/1 ------------------------
        active_client_map = new HashMap<>();
        id_count = new AtomicInteger(0);
        readWriteLock = new ReentrantReadWriteLock();
        //------------------- end edit 4/1 --------------------------
    }

    public static ConnectionsImpl getInstance(){
        //------------------- start edit 14/1 ------------------------
        return SingletonHolder.connections;
        //------------------- end edit 14/1 --------------------------
    }

    @Override
    /**
     * Sends message T to client represented by the given connection ID.
     * when failing to send a message to client returns -> FALSE
     * else returns -> TRUE
     */
    public boolean send(int connectionId, T msg) {
        //------------------- start edit 4/1 ------------------------
        readWriteLock.readLock().lock();        //Locking the readLock
        try {   // always happen
            if (active_client_map.containsKey(connectionId)) {        // searching if the client exists in the map
                active_client_map.get(connectionId).send(msg);      // send message through CH interface
                return true;
            } else
                return false;
        }finally {  //to unlock the readLock, after each return
            readWriteLock.readLock().unlock();
        }
        //------------------- end edit 4/1 --------------------------
    }

    @Override
    /**
     * Sends message T to client subscribed to that specific channel == topic.
     */
    public void send(String topic, T msg) {
        //------------------- start edit 11/1 ------------------------
        if(UsersControl.getInstance().getTopicList(topic)!=null) {
            for (Integer curr_id : UsersControl.getInstance().getTopicList(topic)) {
                readWriteLock.readLock().lock();
                if (active_client_map.containsKey(curr_id)) {
                    //those few actions are to change the subscription id that we send to.
                    changeSubUserId_msg((Message) msg, curr_id);
                    active_client_map.get(curr_id).send(msg);
                }
                readWriteLock.readLock().unlock();
            }
        }
        //------------------- end edit 11/1 --------------------------
    }

    @Override
    /**
     * Removes an active client represented by the given connection ID from the map.
     */
    public void disconnect(int connectionId) {
        //------------------- start edit 4/1 ------------------------
        readWriteLock.writeLock().lock();

        //try {
            //active_client_map.get(connectionId).close();
            active_client_map.remove(connectionId);
        //} catch (IOException e) {
         //   e.printStackTrace();
        //}

        readWriteLock.writeLock().unlock();
        //------------------- end edit 4/1 --------------------------
    }

    /**
     *  Adding a new client to the Connections map
     *  increment the @id_count
     *  adding the {@param connectionHandler} to the map
     *  LOCKING writing to the connection map @active_clients_map
     */
    public void connect(ConnectionHandler<T> newConnectionHandler, int connectionId){
        //------------------- start edit 4/1 ------------------------
        readWriteLock.writeLock().lock();               //locking writing
        active_client_map.putIfAbsent(connectionId,newConnectionHandler);
        readWriteLock.writeLock().unlock();             //unlocking writing
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * A function to return the current id_count
     */
    public int incAndGetIdCount(){
        return id_count.incrementAndGet();
    }

    /**
     * A function to return the active_client_map
     */
    public Map<Integer, ConnectionHandler<T>> getActiveClientMap(){
        return active_client_map;
    }

    private Message changeSubUserId_msg(Message orig_msg, int currId){
        //------------------- start edit 14/1 ------------------------
        String msg_str = orig_msg.getMessageData();
        msg_str = msg_str.replace("$",Integer.toString(currId));
        ((AcknowledgeMsg)orig_msg).changeMsgData(msg_str);
        return orig_msg;
        //------------------- end edit 14/1 --------------------------
    }
}
