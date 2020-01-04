package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.Connections;

import java.util.ArrayList;

public class UsersControl {
    /**
     * The purpose of this class, is to manage the users database and its changes due to different commands
     * It's not connect directly to the server-client communication
     **/
    //------------------- start edit 4/1 ------------------------
    private ArrayList<User> userArrayList;
    private ConnectionsImpl connections;

    //------------------- end edit 4/1 --------------------------

    public UsersControl(){
        //------------------- start edit 4/1 ------------------------
        userArrayList = new ArrayList<>();
        connections = new ConnectionsImpl();
        //------------------- end edit 4/1 --------------------------
    }

    /**
     * login function will login the user to the system, only if userPassword = user.getPassword
     * @param userName
     * @param userPassword
     * @return "connected" if user added successfully\already exist and username+password are correct
     * @return "logged on" if user already logged on
     * @return "wrong pass" if password is incorrect
     */
    public String login(int id, String userName, String userPassword){
        //------------------- start edit 4/1 ------------------------
        for (User u: userArrayList) {
            if (u.getName().equals(userName)){
                if (connections.getActiveClientMap().containsKey(u.getConnectionId()))      //User is already logged on
                    return "logged on";
                else {
                    if (u.getPassword().equals(userPassword))                              //User name + password are correct
                        return "connected";
                    else
                        return "wrong pass";                                               //Password is incorrect
                }
            }
        }
        User newU = new User(userName,userPassword,connections.getIdCount());              //Create new user
        userArrayList.add(newU);
        return "connected";
        //------------------- end edit 4/1 --------------------------
    }








}
