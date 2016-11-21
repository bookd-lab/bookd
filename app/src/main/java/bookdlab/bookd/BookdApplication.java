package bookdlab.bookd;

import android.app.Application;
import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import bookdlab.bookd.models.User;

/**
 * Created by akhmedovi on 11/10/16.
 * Copyright - 2016
 */
public class BookdApplication extends Application {

    private static Context mContext;
    public final static String MAP_API_KEY = "AIzaSyCpTH0d_h-57BuI2f6UWaFe0aOsD1WDIq0";
    private static User currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        currentUser = getCurrentUser();
        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/lineto-circular-pro-medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/
    }

    public static Context getContext() {
        return mContext;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        BookdApplication.currentUser = currentUser;
        updateUserInfo(currentUser);
    }

    private static void updateUserInfo(User user){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getId());
        reference.setValue(user);
    }
}