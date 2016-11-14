package bookdlab.bookd.models;

/**
 * Created by akhmedovi on 11/12/16.
 * Copyright - 2016
 */

public class Event {

    private String name;
    private String dates;
    private String color;

    public Event() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
