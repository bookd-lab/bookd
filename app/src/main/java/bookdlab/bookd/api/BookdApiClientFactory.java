package bookdlab.bookd.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by akhmedovi on 11/26/16.
 * Copyright - 2016
 */

public class BookdApiClientFactory {
    private static final String BASE_URL = "https://bookd-backend.herokuapp.com/";

    private static BookdApiClient instance;

    public static BookdApiClient me() {
        if (null == instance) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            instance = retrofit.create(BookdApiClient.class);
        }

        return instance;
    }
}
