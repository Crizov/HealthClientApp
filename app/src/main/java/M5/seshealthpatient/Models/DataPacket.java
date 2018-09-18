package M5.seshealthpatient.Models;

import android.location.Location;

import java.util.Date;
import java.io.Serializable;
import java.util.LinkedList;

public class DataPacket implements Serializable {
    private String query;
    private String heartRate;
    private Location location;
    private LinkedList<String> files;
    private Date sentDate;

    public DataPacket(String query, String heartRate, Location location, LinkedList<String> files) {
        this.heartRate = heartRate;
        this.query = query;
        this.location = location;
        this.files = files;
    }

    public DataPacket() {
        this.location = null;
        this.query = null;
        this.heartRate = null;
        this.files = new LinkedList<>();
    }

    public void addFile(String file) {
        files.add(file);
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public LinkedList<String> getFiles() {
        return files;
    }
    
        public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }



}