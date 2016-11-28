package bookdlab.bookd;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import bookdlab.bookd.models.User;

/**
 * Created by akhmedovi on 11/10/16.
 * Copyright - 2016
 */
public class BookdApplication extends Application {

    public final static String MAP_API_KEY = "AIzaSyCpTH0d_h-57BuI2f6UWaFe0aOsD1WDIq0";
    private static User currentUser;
    private static final String TAG = "BookdApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        currentUser = getCurrentUser();
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Log.d(TAG, "setCurrentUser: " + currentUser);
        BookdApplication.currentUser = currentUser;
        updateUserInfo(currentUser);
    }

    private static void updateUserInfo(User user) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getId());
        reference.setValue(user);
    }
}