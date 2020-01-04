package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.Connections;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class UsersControl {
    /**
     * The purpose of this class, is to manage the users database and its changes due to different commands
     * It's not connect directly to the server-client communication
     **/
    //------------------- start edit 4/1 ------------------------
    private CopyOnWriteArrayList<User> all_user_array;
    private ConcurrentHashMap<Integer, User> active_user_id_map;
    private ConnectionsImpl connections;
    private ConcurrentHashMap <String, CopyOnWriteArrayList<Integer> > topicArraysMap;
    //------------------- end edit 4/1 --------------------------

    public UsersControl(){
        //------------------- start edit 4/1 ------------------------
        all_user_array = new CopyOnWriteArrayList<>();
        active_user_id_map = new ConcurrentHashMap<>();
        connections = new ConnectionsImpl();
        topicArraysMap = new ConcurrentHashMap<>();
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * login function will login the user to the system, only if userPassword = user.getPassword
     * @param id
     * @param userName
     * @param userPassword
     *
     * @return "connected" if user added successfully\already exist and username+password are correct
     * @return "logged on" if user already logged on
     * @return "wrong pass" if password is incorrect
     */
    public String login(int id, String userName, String userPassword){
        //------------------- start edit 4/1 ------------------------
        for (User u: all_user_array) {
            if (u.getName().equals(userName)){
                if (connections.getActiveClientMap().containsKey(id))      //User is already logged on
                    return "logged on";
                else {
                    if (u.getPassword().equals(userPassword))                              //User name + password are correct
                        return "connected";
                    else
                        return "wrong pass";                                               //Password is incorrect
                }
            }
        }
        User newUsr = new User(userName,userPassword,id);              //Create new user
        all_user_array.add(newUsr);                                     //adding new user to the general user map
        active_user_id_map.put(id,newUsr);                                    //adding new user & id to the map
        return "connected";
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * This function is for subscribing a user to a topic
     * @param id
     * @param topic
     * @return true always.
     */
    public boolean joinGenre (int id, String topic){
        //------------------- start edit 4/1 ------------------------
        CopyOnWriteArrayList<Integer> tmp_array = new CopyOnWriteArrayList<>();
        tmp_array.add(id);
        if(topicArraysMap.putIfAbsent(topic, tmp_array) != null){        //PutIfAbsent returns null if there is no topic
            // that topic exists in the topic_map
            if(!topicArraysMap.get(topic).contains(id)) {                //checks if already contains id
                topicArraysMap.get(topic).add(id);                       // ADDING client id to the topic
                active_user_id_map.get(id).addTopic(topic);              // ADDING topic to user -> USER        //TODO: may be deleted
            }
        }
        return true;    //TODO: maybe a void function is enough
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * This function will unsubscribe user (id) from the topic
     * @param id
     * @param topic
     * @return true if succeeded, false if no such topic
     */
    public boolean exitGenre (Integer id, String topic){        // Integer - for removing Object, NOT index
        //------------------- start edit 4/1 ------------------------
        if(topicArraysMap.contains(topic)){                     // checks if the topic exists in the map
            topicArraysMap.get(topic).remove(id);               // REMOVES the user (id) from the topic array
            active_user_id_map.get(id).removeTopic(topic);      // REMOVES the topic from the user -> USER        //TODO: may be deleted
            return true;
        }
        return false;
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * This function will logout the user from the server, by resetting the topics subscriptions
     * Here we ONLY removes it from the topics it was registered to,
     * TODO: send receipt frame, close socket, disconnect from Connections
     * @param id
     * @return
     */
    public boolean logoutTopicReset (Integer id){                   // Integer - for removing Object, NOT index
        //------------------- start edit 4/1 ------------------------
        for(CopyOnWriteArrayList curr_topic_array : topicArraysMap.values()){
            curr_topic_array.remove(id);                            // removing the user (id) from each topic list
        }
        active_user_id_map.get(id).removeAllTopics();               // REMOVES ALL topics from the user -> USER        //TODO: may be deleted
        return true;    //TODO: maybe a void function is enough
        //------------------- end edit 4/1 --------------------------
    }



}
