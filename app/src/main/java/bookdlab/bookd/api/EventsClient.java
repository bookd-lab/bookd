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

        Event event = new Event();
        event.setName("My marriage");
        event.setDates("From Nov 5 To Nov 15, 2016");

        eventsList.add(event);
        eventsList.add(event);
        eventsList.add(event);
        eventsList.add(event);
        eventsList.add(event);

        return eventsList;
    }

}
