package com.kanakai.ikanak;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 1000; //  1 seconds
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth= FirebaseAuth.getInstance();

        // Delay and open MainActivity
        new Handler().postDelayed(() -> {

            if(mAuth.getCurrentUser()!=null){
                //current user exist
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }
            else{
                //no current user
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
            }
            finish(); // close splash activity so user can't go back to it
        }, SPLASH_TIME_OUT);
    }
}