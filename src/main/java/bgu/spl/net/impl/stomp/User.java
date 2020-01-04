package bgu.spl.net.impl.stomp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class User {
    /**
     * This class contains all the info about a single user
     */
    //------------------- start edit 4/1 ------------------------
    private String name;                                            // user name
    private String password;                                        // user password
    private int connectionId;                                       // user connection id which used in the ConnectionImpl
    private ArrayList<String> topic_list;                           // user lists of topics
    private HashMap<String, ArrayList<String>> book_inventory;      //user map of genre-bookList
    //------------------- end edit 4/1 --------------------------

    public User(String name, String password, int connectionId) {
        //------------------- start edit 4/1 ------------------------
        this.name = name;
        this.password = password;
        this.connectionId = connectionId;
        this.topic_list = new ArrayList<>();
        this.book_inventory = new HashMap<>();
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

    /** Remove topic from user's topic_list **/
    public void removeTopic (String topic){
        //------------------- start edit 4/1 ------------------------
        if(!topic_list.contains(topic)){
            topic_list.remove(topic);
        }
        //------------------- end edit 4/1 --------------------------
    }

    /** Remove all topics when user logout **/
    public void removeAllTopics (){
        //------------------- start edit 4/1 ------------------------
        topic_list.clear();
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * Add book to user's book map
     * @param book
     * @param topic
     * @return true if the book is new.
     *         false if the book is already in this user
     */
    public boolean addBook (String book, String topic){
        //------------------- start edit 4/1 ------------------------
        ArrayList<String> tmp_array = new ArrayList<>();
        tmp_array.add(book);
        if(book_inventory.putIfAbsent(topic, tmp_array) != null){        //PutIfAbsent returns null if there is no topic
            // that topic exists in the book_inventory map
            if(!book_inventory.get(topic).contains(book)) {              //checks if already contains book
                book_inventory.get(topic).add(book);                     // ADDING book to the topic array
            }
            return true;                                                 // added a new book
        }
        return false;                                                    // The book already exists
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * Remove book from user's book map
     * @param book
     * @param topic
     * @return true if the book has been removed
     *         false if the book isn't in the this user
     */
    public boolean removeBook (String book, String topic){
        //------------------- start edit 4/1 ------------------------
        if(book_inventory.containsKey(topic)){                    // checks if the topic exists in the map
            book_inventory.get(topic).remove(book);               // REMOVES the book from the topic array
            return true;                                          // book has been removed successfully
        }
        return false;                                             // No such book
        //------------------- end edit 4/1 --------------------------
    }


}
