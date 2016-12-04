package bookdlab.bookd.modules;

import javax.inject.Singleton;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.activities.BookBusinessActivity;
import bookdlab.bookd.activities.EventCreateActivity;
import bookdlab.bookd.activities.MainActivity;
import bookdlab.bookd.activities.SplashActivity;
import bookdlab.bookd.fragments.EventFragment;
import bookdlab.bookd.fragments.ExploreFragment;
import dagger.Component;

/**
 * Created by akhmedovi on 11/30/16.
 * Copyright - 2016
 */

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(SplashActivity activity);

    void inject(MainActivity activity);

    void inject(EventCreateActivity activity);

    void inject(BookBusinessActivity activity);

    void inject(EventFragment fragment);

    void inject(ExploreFragment fragment);

    void inject(BookdApplication application);

}
