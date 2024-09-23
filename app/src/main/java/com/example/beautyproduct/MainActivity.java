package com.example.beautyproduct;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {

    ImageView ivlogo;
    TextView tvtitle;
    Animation translateanim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       ivlogo=findViewById(R.id.ivlogo);
       tvtitle=findViewById(R.id.tvTitle);
       translateanim= AnimationUtils.loadAnimation(MainActivity.this,R.anim.translate);
       ivlogo.startAnimation(translateanim);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
           Intent i=new Intent(MainActivity.this, LoginActivity.class);
           startActivity(i);
            }
        },3000);
    }
}