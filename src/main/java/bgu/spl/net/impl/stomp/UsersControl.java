package bgu.spl.net.impl.stomp;

import bgu.spl.net.Pair;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class UsersControl {
    /**
     * The purpose of this class, is to manage the users database and its changes due to different commands
     * It's not connect directly to the server-client communication
     **/
    //------------------- start edit 11/1 ------------------------
    private CopyOnWriteArrayList<User> all_user_array;                                          // Global userId map
    private static AtomicInteger userCounter;                                                   // User counter, to track how many users are in the database
    private ConnectionsImpl connections;
    private ConcurrentHashMap<Integer, User> active_user_id_map;                                // Connected user's connectionId
    private ConcurrentHashMap <String, CopyOnWriteArrayList<Integer> > topic_connectionId_Map;  // Only active users are in this map
    private static class SingletonHolder{
        private static UsersControl UC_instance = new UsersControl();
    }
    //------------------- end edit 11/1 --------------------------

    public UsersControl(){
        //------------------- start edit 11/1 ------------------------
        all_user_array = new CopyOnWriteArrayList<>();
        active_user_id_map = new ConcurrentHashMap<>();
        connections = new ConnectionsImpl();
        topic_connectionId_Map = new ConcurrentHashMap<>();
        userCounter=new AtomicInteger(0);
        //------------------- end edit 11/1 --------------------------
    }

    public static UsersControl getInstance(){
        //------------------- start edit 7/1 ------------------------
        return SingletonHolder.UC_instance;
        //------------------- end edit 7/1 --------------------------
    }

    /**
     * login function will login the user to the system, only if userPassword = user.getPassword
     * @param connectionId
     * @param userName
     * @param userPassword
     *
     * @return "connected" if user added successfully\already exist and username+password are correct
     * @return "logged on" if user already logged on
     * @return "wrong pass" if password is incorrect
     */
    public String login(int connectionId, String userName, String userPassword){
        //------------------- start edit 4/1 ------------------------
        synchronized (all_user_array) {         // sync all users, because 2 may create the same username
            if(active_user_id_map.containsKey(connectionId) && !active_user_id_map.get(connectionId).getName().equals(userName)){         /** 11/1 - check for a new username for that connection Id **/
                return "logged on with another username";
            }

            for (User u : all_user_array) {
                if (u.getName().equals(userName)) {  // found the name of the user
                    if (connections.getActiveClientMap().containsKey((Integer)connectionId))      //User is already logged on
                        return "logged on";
                    else {
                        if (u.getPassword().equals(userPassword)) {                      //User name + password are correct
                            active_user_id_map.put(connectionId, u);                     //adding curr user & id to the map
                            u.setConnected_successfully(true);                           /**setting the connected_successfully to true - 10/1 **/
                            return "connected";
                        } else
                            return "wrong pass";                                //Password is incorrect
                    }
                }
            }
            int tmp_count = userCounter.incrementAndGet();                      //inc the counter of users
            User newUsr = new User(userName, userPassword, tmp_count);          //Create new user
            all_user_array.add(tmp_count-1, newUsr);                              //adding new user to the general user array, in the specified index
            active_user_id_map.put(connectionId, newUsr);                       //adding new user & connectionId to the ActiveUserMap
            newUsr.setConnected_successfully(true);                             /**setting the connected_successfully to true - 10/1 **/
            return "connected";
        }
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * This function is for subscribing a user to a topic
     * @param connectionId             The user current connectionId
     * @param topic
     * @param topic_idByUser           The user's id for the specific topic
     * @return true always.
     */
    public boolean joinGenre (Integer connectionId, String topic, Integer topic_idByUser){
        //------------------- start edit 7/1 ------------------------
        CopyOnWriteArrayList<Integer> tmp_array = new CopyOnWriteArrayList<>();
        tmp_array.add(connectionId);
        if(topic_connectionId_Map.putIfAbsent(topic, tmp_array) != null){        //PutIfAbsent returns null if there is no topic
            // that topic exists in the topic_map
            if (!topic_connectionId_Map.get(topic).contains(connectionId)) {            //checks if already contains id
                topic_connectionId_Map.get(topic).add(connectionId);                   // ADDING client id to the topic
                active_user_id_map.get(connectionId).addTopic(topic, topic_idByUser);   // ADDING topic_id by user to user-> USER
            } else
                return false; //this user has already subscribed to this topic
        }
        return true; // subscribed success
        //------------------- end edit 7/1 --------------------------
    }

    /**
     * This function will unsubscribe user (id) from the topic
     * @param connectionId
     * @param topic_idByUser
     * @return true if succeeded, false if no such topic
     */
    public boolean exitGenre (Integer connectionId, Integer topic_idByUser){       // Integer - for removing Object, NOT index
        //------------------- start edit 4/1 ------------------------
        String topic = active_user_id_map.get(connectionId).getTopic_id_map().get(topic_idByUser);
        if(topic!=null){        //The user has this topic in his subscription list
            topic_connectionId_Map.get(topic).remove(connectionId);                      // REMOVES the user (id) from the topic array
            active_user_id_map.get(connectionId).removeTopic(topic_idByUser);      // REMOVES the topic from the user -> USER
            return true;
        }
        return false;
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * This function will logout the user from the server, by resetting the topics subscriptions
     * Here we ONLY removes it from the topics it was registered to,
     * >>> send receipt frame, disconnect from Connections - Done in LogoutMsg
     * TODO: close socket in client
     * @param connectionID
     * @return
     */
    public boolean logout_TopicReset_ConnectionSuccessfullyFalse (Integer connectionID){                   // Integer - for removing Object, NOT index
        //------------------- start edit 4/1 ------------------------
        for(CopyOnWriteArrayList curr_topic_array : topic_connectionId_Map.values()){
            curr_topic_array.remove(connectionID);                              // removing the user (id) from each topic list
        }
        active_user_id_map.get(connectionID).removeAllTopics();                 // REMOVES ALL topics from the user ->
        active_user_id_map.get(connectionID).setConnected_successfully(false);  /**setting the connected_successfully to true - 10/1 **/
        active_user_id_map.remove(connectionID);                                /** 11/1 - removing the user from the active map **/
        return true;    //TODO: maybe a void function is enough
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * @param connectionId
     * @return USER by connection id
     */
    public User getUserByConnectionId(Integer connectionId){
        //------------------- start edit 4/1 ------------------------
        return this.active_user_id_map.get(connectionId);
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * @param globalSerialnumber
     * @return USER by global id
     */
    public User getUserByGlobalSerialnumber(Integer globalSerialnumber){
        //------------------- start edit 4/1 ------------------------
        return this.all_user_array.get(globalSerialnumber);
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * @param topic
     * @return a list of all the connectionsId which are subscribed to this topic
     */
    public CopyOnWriteArrayList<Integer> getTopicList (String topic){
        //------------------- start edit 7/1 ------------------------
        return topic_connectionId_Map.get(topic);
        //------------------- end edit 7/1 --------------------------
    }
}
