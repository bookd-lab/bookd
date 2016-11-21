package bookdlab.bookd.interfaces;

import bookdlab.bookd.models.Event;

/**
 * Created by akhmedovi on 11/19/16.
 * Copyright - 2016
 */

public interface WizardNavigator {
    public void onNext();
    public void onPrev();

    public Event getEvent();
}
