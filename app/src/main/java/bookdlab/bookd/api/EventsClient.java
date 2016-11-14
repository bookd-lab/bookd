package bookdlab.bookd.api;

import java.util.ArrayList;
import java.util.List;

import bookdlab.bookd.models.Business;
import bookdlab.bookd.models.Event;

/**
 * Created by akhmedovi on 11/12/16.
 * Copyright - 2016
 */
public class EventsClient {

    //TODO: Mock data
    public List<Event> getMyEvents() {

        List<Event> eventsList = new ArrayList<>();

        eventsList.add(genEvent("#4286f4"));
        eventsList.add(genEvent("#9842f4"));
        eventsList.add(genEvent("#206b21"));
        eventsList.add(genEvent("#9aa09a"));
        eventsList.add(genEvent("#4286f4"));
        eventsList.add(genEvent("#9842f4"));
        eventsList.add(genEvent("#206b21"));
        eventsList.add(genEvent("#9aa09a"));

        return eventsList;
    }

    private Event genEvent(String color) {
        Event event = new Event();
        event.setName("My marriage");
        event.setDates("From Nov 5 To Nov 15, 2016");
        event.setColor(color);
        return event;
    }

}
