package bookdlab.bookd;

import android.app.Application;
import android.content.Context;

/**
 * Created by akhmedovi on 11/10/16.
 * Copyright - 2016
 */
public class BookdApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        //FontsOverride.setDefaultFont(this, "DEFAULT", "MyFontAsset.ttf");
        //FontsOverride.setDefaultFont(this, "MONOSPACE", "lineto-circular-pro-medium.ttf");
        //FontsOverride.setDefaultFont(this, "SERIF", "MyFontAsset3.ttf");
        //FontsOverride.setDefaultFont(this, "SANS_SERIF", "MyFontAsset4.ttf");
    }

    public static Context getContext() {
        return mContext;
    }
}