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
    private ArrayList<String> topic_subArray;                       // user lists of topics
    private HashMap<String, ArrayList<Book>> book_inventory;      //user map of genre-bookList
    //------------------- end edit 4/1 --------------------------

    public User(String name, String password, int connectionId) {
        //------------------- start edit 4/1 ------------------------
        this.name = name;
        this.password = password;
        this.connectionId = connectionId;
        this.topic_subArray = new ArrayList<>();
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
        return topic_subArray;
        //------------------- end edit 4/1 --------------------------
    }

    /** Add topic to user's topic_list **/
    public void addTopic (String topic){
        //------------------- start edit 4/1 ------------------------
        if(!topic_subArray.contains(topic)){
            topic_subArray.add(topic);
        }
        //------------------- end edit 4/1 --------------------------
    }

    /** Remove topic from user's topic_list **/
    public void removeTopic (String topic){
        //------------------- start edit 4/1 ------------------------
        if(!topic_subArray.contains(topic)){
            topic_subArray.remove(topic);
        }
        //------------------- end edit 4/1 --------------------------
    }

    /** Remove all topics when user logout **/
    public void removeAllTopics (){
        //------------------- start edit 4/1 ------------------------
        topic_subArray.clear();
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * Add book to user's book map
     * @param book
     * @return true if the book is new.
     *         false if the book is already in this user
     */
    public boolean addBook (Book book){         /** no book is multi genre **/
        //------------------- start edit 4/1 ------------------------
        ArrayList<Book> tmp_array = new ArrayList<>();
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

    /**
     * Remove book from user's book map
     * @param book
     * @return true if the book has been removed
     *         false if the book isn't in the this user
     */
    public boolean removeBook (Book book){
        //------------------- start edit 4/1 ------------------------
        if(book_inventory.containsKey(book.getTopic())){                    // checks if the topic exists in the map
            book_inventory.get(book.getTopic()).remove(book);               // REMOVES the book from the topic array
            return true;                                          // book has been removed successfully
        }
        return false;                                             // No such book
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * This function return true if the book exists in the user inventory
     * @param book
     * @return  true - book exists in the user's inventory
     *          false - book isn't here...
     */
    public boolean bookExist (Book book){
        //------------------- start edit 4/1 ------------------------
        if (book_inventory.containsKey(book.getTopic()))
            if (book_inventory.get(book.getTopic()).contains(book))
                return true;
        return false;
        //------------------- end edit 4/1 --------------------------
    }


}
