package bookdlab.bookd.modules;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import bookdlab.bookd.api.BookdApiClient;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by akhmedovi on 11/29/16.
 * Copyright - 2016
 */

@Module
public class AppModule {

    private static final String BASE_URL = "https://bookd-backend.herokuapp.com/";

    private Application app;

    public AppModule(Application application) {
        this.app = application;
    }

    @Provides
    @Singleton
    public Application providesApplication() {
        return app;
    }

    @Provides
    @Singleton
    public Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(Cache cache) {
        return new OkHttpClient.Builder().cache(cache).build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public BookdApiClient getBookdApiClient(Retrofit retrofit) {
        return retrofit.create(BookdApiClient.class);
    }
}
