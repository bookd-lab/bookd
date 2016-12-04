package bookdlab.bookd.models;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by akhmedovi on 11/12/16.
 * Copyright - 2016
 */

@Parcel
public class Event {

    public enum Type {
        DEFAULT,
        WEDDING,
        BIRTHDAY,
        ANNIVERSARY
    }

    private String _id;
    private String creator;
    private String name;
    private Long startDate;
    private Long endDate;
    private String dates;
    private Type type = Type.DEFAULT;

    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<String> businesses = new ArrayList<>();

    public Event() {
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
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

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ArrayList<String> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(ArrayList<String> businesses) {
        this.businesses = businesses;
    }
}
