package bookdlab.bookd.ui;

import bookdlab.bookd.R;
import bookdlab.bookd.models.Event;

/**
 * Created by akhmedovi on 12/1/16.
 * Copyright - 2015
 */

public class EventUtils {

    public static int getEventBackgroundResouce(Event.Type type) {
        if (type == Event.Type.WEDDING) {
            return R.drawable.celebration;
        }
        if (type == Event.Type.BIRTHDAY) {
            return R.drawable.birthday_bg;
        }

        return R.drawable.christmas;
    }
}
