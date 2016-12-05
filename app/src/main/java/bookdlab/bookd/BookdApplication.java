package bookdlab.bookd;

import android.app.Application;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import bookdlab.bookd.models.Favorite.Favorite;
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
    private static HashMap<String, Favorite> favorites;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        appComponent.inject(this);

        Log.d(TAG, "onCreate");
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        BookdApplication.currentUser = currentUser;
    }

    public static HashMap<String, Favorite> getFavorites() {
        return favorites;
    }

    public static void setFavorites(HashMap<String, Favorite> favorites) {
        BookdApplication.favorites = favorites;
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
        currentUser = null;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}