package bgu.spl.net.impl.stomp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class User {
    /**
     * This class contains all the info about a single user
     */
    //------------------- start edit 7/1 ------------------------
    private String name;                                            // user name
    private String password;                                        // user password
    private int user_serialnumber;                                       // user connection id which used in the ConnectionImpl
    private HashMap<Integer, String> topic_id_map;                       // user lists of topics by id given by the user
    private boolean connected_successfully =false;                   /** for wrong password case -10/1 **/
    //------------------- end edit 7/1 --------------------------

    public User(String name, String password, int user_serialnumber) {
        //------------------- start edit 7/1 ------------------------
        this.name = name;
        this.password = password;
        this.user_serialnumber = user_serialnumber;
        this.topic_id_map = new HashMap<>();
        //------------------- end edit 7/1 --------------------------
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
        return user_serialnumber;
        //------------------- end edit 4/1 --------------------------
    }

    /** user password getter **/
    public String getPassword() {
        //------------------- start edit 4/1 ------------------------
        return password;
        //------------------- end edit 4/1 --------------------------
    }

    /** user topics getter **/
    public HashMap<Integer, String> getTopic_id_map(){
        //------------------- start edit 4/1 ------------------------
        return topic_id_map;
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * return the subscription number which saved to a specific topic "topic"
     * @param topic
     * @return
     */
    public Integer get_SubNum_by_TopicName (String topic){
        //------------------- start edit 7/1 ------------------------
        for(Map.Entry<Integer,String> entry : topic_id_map.entrySet()){
            if(entry.getValue().equals(topic)){
                return entry.getKey();      // this will be the SubNum
            }
        }
        return null;
        //------------------- end edit 7/1 ------------------------
    }

    /**
     * Inserting a topic by a special topic_idByUser given by the user
     * @param topic
     * @param topic_idByUser
     */
    public void addTopic (String topic, Integer topic_idByUser){
        //------------------- start edit 7/1 ------------------------
        if(!topic_id_map.containsValue(topic)){
            topic_id_map.put(topic_idByUser,topic);
        }
        //------------------- end edit 7/1 --------------------------
    }

    /**
     * Removing the topic which represented by the topic_idByUser
     * @param topic_idByUser
     */
    public void removeTopic (Integer topic_idByUser){   //removing topic by topic_idByUser
        //------------------- start edit 7/1 ------------------------
        if(!topic_id_map.containsValue(topic_idByUser)){
            topic_id_map.remove(topic_idByUser);
        }
        //------------------- end edit 7/1 --------------------------
    }

    /** Remove all topics when user logout **/
    public void removeAllTopics (){
        //------------------- start edit 4/1 ------------------------
        topic_id_map.clear();
        //------------------- end edit 4/1 --------------------------
    }

    /** Sets connected_successfully **/
    public void setConnected_successfully(boolean setter){
        //------------------- start edit 10/1 ------------------------
        connected_successfully=setter;
        //------------------- end edit 10/1 --------------------------
    }

    /** Gets connected_successfully**/
    public boolean getConnected_successfully(){
        //------------------- start edit 10/1 ------------------------
        return connected_successfully;
        //------------------- end edit 10/1 --------------------------

    }
}
