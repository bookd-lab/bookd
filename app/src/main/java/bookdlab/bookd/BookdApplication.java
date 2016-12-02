package bookdlab.bookd;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import bookdlab.bookd.models.User;
import bookdlab.bookd.modules.AppComponent;
import bookdlab.bookd.modules.AppModule;
import bookdlab.bookd.modules.DaggerAppComponent;

/**
 * Created by akhmedovi on 11/10/16.
 * Copyright - 2016
 */
public class BookdApplication extends Application {
    private static final String TAG = BookdApplication.class.getSimpleName();

    private static User currentUser;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();

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

    public AppComponent getAppComponent() {
        return appComponent;
    }
}