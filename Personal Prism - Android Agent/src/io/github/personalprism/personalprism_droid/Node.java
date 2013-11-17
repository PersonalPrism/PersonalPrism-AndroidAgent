package io.github.personalprism.personalprism_droid;

import java.util.ArrayList;
import android.location.Location;
import java.util.Date;

/**
 * Data storer for info that will be displayed on the map.
 * 
 * @author Stuart Harvey (stu)
 * @version 2013.11.16
 */
public class Node
{
    private Location location;
    private Date     timeStamp;
    private ArrayList<String> sms;


    /**
     * Constructs a node from latlng data.
     * 
     * @param location
     *            the location
     */
    public Node(Location location)
    {
        this.location = location;
        timeStamp = new Date();
        sms = new ArrayList<String>(0);
    }


    /**
     * Fetches the timestamp as a date.
     * 
     * @return the timestamp.
     */
    public Date getDate()
    {
        return timeStamp;
    }


    /**
     * Fetches the location data for this node.
     * 
     * @return location of the node.
     */
    public Location getLocation()
    {
        return location;
    }


    /**
     * Fetches the SMS message for this node.
     * 
     * @return sms message.
     */
    public ArrayList<String> getSMS()
    {
        return sms;
    }
    
    /**
     * Adds an SMS message to the list of messages.
     * @param message the sms message to add.
     */
    public void addSMS(String message)
    {
        sms.add(message);
    }
}
