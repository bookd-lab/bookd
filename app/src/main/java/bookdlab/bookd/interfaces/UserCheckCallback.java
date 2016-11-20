package bookdlab.bookd.interfaces;

import bookdlab.bookd.models.User;

/**
 * Created by pranavkonduru on 11/19/16.
 */

public interface UserCheckCallback{
    void userIsPresent(User signedInUser);
    void userIsNotPresent();
}
