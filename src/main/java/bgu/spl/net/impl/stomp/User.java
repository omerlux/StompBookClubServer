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





    //TODO: should be in CLIENT
    //TODO: should be in CLIENT
    //TODO: should be in CLIENT
    //TODO: should be in CLIENT


    /**
     * Add book to user's book map
     * @param book
     * @return true if the book is new.
     *         false if the book is already in this user
     */
//   public boolean addBook (Book book){         // no book is multi genre
        //------------------- start edit 4/1 ------------------------
/*      ArrayList<Book> tmp_array = new ArrayList<>();
        tmp_array.add(book);
        if(book_inventory.putIfAbsent(book.getTopic(), tmp_array) != null){        //PutIfAbsent returns null if there is no topic
            // that topic exists in the book_inventory map
            if(!book_inventory.get(book.getTopic()).contains(book)) {              //checks if already contains book
                book_inventory.get(book.getTopic()).add(book);                     // ADDING book to the topic array
            }
            return true;                                                 // added a new book
        }
        return false;                                                    // The book already exists
        //------------------- end edit 4/1 --------------------------
    }
*/
    /**
     * Remove book from user's book map
     * @param book
     * @return true if the book has been removed
     *         false if the book isn't in the this user
     */
/*    public boolean removeBook (Book book){
        //------------------- start edit 4/1 ------------------------
        if(book_inventory.containsKey(book.getTopic())){                    // checks if the topic exists in the map
            book_inventory.get(book.getTopic()).remove(book);               // REMOVES the book from the topic array
            return true;                                          // book has been removed successfully
        }
        return false;                                             // No such book
        //------------------- end edit 4/1 --------------------------
    }
*/
    /**
     * This function return true if the book exists in the user inventory
     * @param book
     * @return  true - book exists in the user's inventory
     *          false - book isn't here...
     */
/*    public boolean bookExist (Book book){
        //------------------- start edit 4/1 ------------------------
        if (book_inventory.containsKey(book.getTopic()))
            if (book_inventory.get(book.getTopic()).contains(book))
                return true;
        return false;
        //------------------- end edit 4/1 --------------------------
    }
*/

}
