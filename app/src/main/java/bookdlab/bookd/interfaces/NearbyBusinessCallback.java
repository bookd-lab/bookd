package bookdlab.bookd.interfaces;

import java.util.ArrayList;

import bookdlab.bookd.models.Business;

/**
 * Created by pranavkonduru on 11/20/16.
 */

public interface NearbyBusinessCallback {
    void onNearbyBusinessesFound(ArrayList<Business> businesses);
}
