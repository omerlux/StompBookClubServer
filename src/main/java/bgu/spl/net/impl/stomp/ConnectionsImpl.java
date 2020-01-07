package bgu.spl.net.impl.stomp;

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
    //------------------- start edit 4/1 ------------------------
    //TODO: watch for THREAD SAFE
    private Map<Integer , ConnectionHandler<T>> active_client_map;
    private AtomicInteger id_count;
    private ReadWriteLock readWriteLock;
    //------------------- end edit 4/1 --------------------------

    public ConnectionsImpl(){
        //------------------- start edit 4/1 ------------------------
        active_client_map = new HashMap<>();
        id_count = new AtomicInteger(0);
        readWriteLock = new ReentrantReadWriteLock();
        //------------------- end edit 4/1 --------------------------
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
        //------------------- start edit 7/1 ------------------------
        for(Integer curr_id : UsersControl.getInstance().getTopicList(topic)){
            readWriteLock.readLock().lock();                //TODO: should we lock here?
            if(active_client_map.containsKey(curr_id)){
                active_client_map.get(curr_id).send(msg);
            }
            readWriteLock.readLock().unlock();
        }
        //------------------- end edit 7/1 --------------------------
    }

    @Override
    /**
     * Removes an active client represented by the given connection ID from the map.
     */
    public void disconnect(int connectionId) {
        //------------------- start edit 4/1 ------------------------
        readWriteLock.writeLock().lock();
        try {
            active_client_map.get(connectionId).close();        //TODO: maybe redundant
            active_client_map.remove(connectionId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        readWriteLock.writeLock().unlock();
        //------------------- end edit 4/1 --------------------------
    }

    /**
     *  Adding a new client to the Connections map
     *  increment the @id_count
     *  adding the {@param connectionHandler} to the map
     *  LOCKING writing to the connection map @active_clients_map
     */
    public void connect(ConnectionHandler<T> newConnectionHandler){
        //------------------- start edit 4/1 ------------------------
        readWriteLock.writeLock().lock();               //locking writing
        active_client_map.putIfAbsent(id_count.getAndIncrement(),newConnectionHandler);
        readWriteLock.writeLock().unlock();             //unlocking writing
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * A function to return the current id_count
     */
    public int getIdCount(){
        return id_count.get();
    }

    /**
     * A function to return the active_client_map
     */
    public Map<Integer, ConnectionHandler<T>> getActiveClientMap(){
        return active_client_map;
    }

}
