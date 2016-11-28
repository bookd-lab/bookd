package bookdlab.bookd.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import bookdlab.bookd.R;

/**
 * Created by akhmedovi on 11/10/16.
 * Copyright - 2016
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> startActivity(new Intent(SplashActivity.this, MainActivity.class)), 1000);
    }
}
