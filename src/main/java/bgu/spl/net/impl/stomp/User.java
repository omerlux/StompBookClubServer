package bgu.spl.net.impl.stomp;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    /**
     * This class contains all the info about a single user
     */
    //------------------- start edit 4/1 ------------------------
    private String name;                    // user name
    private String password;                // user password
    private int connectionId;               // user connection id which used in the ConnectionImpl
    private ArrayList<String> topic_list;   // user lists of topics
    //------------------- end edit 4/1 --------------------------

    public User(String name, String password, int connectionId) {
        //------------------- start edit 4/1 ------------------------
        this.name = name;
        this.password = password;
        this.connectionId = connectionId;
        this.topic_list = new ArrayList<>();
        //------------------- end edit 4/1 --------------------------
    }

    /** user name getter **/
    public String getName() {
        //------------------- start edit 4/1 ------------------------
        return name;
        //------------------- end edit 4/1 --------------------------
    }

    /** user id getter **/
    public int getConnectionId() {
        //------------------- start edit 4/1 ------------------------
        return connectionId;
        //------------------- end edit 4/1 --------------------------
    }

    /** user password getter **/
    public String getPassword() {
        //------------------- start edit 4/1 ------------------------
        return password;
        //------------------- end edit 4/1 --------------------------
    }

    /** user topics getter **/
    public ArrayList<String> getTopic_list(){
        //------------------- start edit 4/1 ------------------------
        return topic_list;
        //------------------- end edit 4/1 --------------------------
    }

    /** Add topic to user's topic_list **/
    public void addTopic (String topic){
        //------------------- start edit 4/1 ------------------------
        if(!topic_list.contains(topic)){
            topic_list.add(topic);
        }
        //------------------- end edit 4/1 --------------------------
    }

    /** Remove all topics when user logout **/
    public void removeAllTopics (){
        //------------------- start edit 4/1 ------------------------
        topic_list.clear();
        //------------------- end edit 4/1 --------------------------
    }
}
