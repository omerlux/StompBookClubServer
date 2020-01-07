package bgu.spl.net.impl.stomp;

import java.util.Queue;
import java.util.Stack;

public class Book_ON_CLIENT_ONLY {
    //------------------- start edit 4/1 --------------------------
    private String topic;
    private String name;
    private boolean borrowed;
    private Stack<User> originalOwner;
    //------------------- end edit 4/1 --------------------------

    public Book_ON_CLIENT_ONLY (String topic, String name){
        //------------------- start edit 4/1 --------------------------
        this.topic = topic;
        this.name = name;
        borrowed = false;
        originalOwner = new Stack<>();
        //------------------- end edit 4/1 --------------------------
    }

    public String getTopic() {
        //------------------- start edit 4/1 --------------------------
        return topic;
        //------------------- end edit 4/1 --------------------------
    }

    public String getName() {
        //------------------- start edit 4/1 ------------------------
        return name;
        //------------------- end edit 4/1 --------------------------
    }
}
