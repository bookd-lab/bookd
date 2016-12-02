package bookdlab.bookd.modules;

import javax.inject.Singleton;

import bookdlab.bookd.activities.MainActivity;
import bookdlab.bookd.activities.SplashActivity;
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
}
