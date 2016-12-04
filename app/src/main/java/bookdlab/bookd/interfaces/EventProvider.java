package bookdlab.bookd.interfaces;

import java.util.ArrayList;
import java.util.List;

import bookdlab.bookd.models.Event;

/**
 * Created by akhmedovi on 12/3/16.
 * Copyright - 2016
 */

public interface EventProvider {
    List<EventAware> eventAwareList = new ArrayList<>();

    public Event getEvent();
    public void addEventAware(EventAware eventAware);
    public void removeEventAware(EventAware eventAware);
}
