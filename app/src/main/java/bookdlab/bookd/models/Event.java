package bookdlab.bookd.models;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by akhmedovi on 11/12/16.
 * Copyright - 2016
 */

@Parcel
public class Event {

    String id;              // Unique ID for every event.
    String creator;         // Which user created this. Useful for showing the events from a user
    String name;            // Event name
    Long startDate;         // Start date for the event
    Long endDate;           // End date for the event
    String backgroundURL;   // Optional: If we want to show a background URL
    String dates;           // ? Do we need this?
    String color;



    //String[] tags; - removed, not Firebase friendly
    List<String> tags;


    public Event() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getBackgroundURL() {
        return backgroundURL;
    }

    public void setBackgroundURL(String backgroundURL) {
        this.backgroundURL = backgroundURL;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

}
