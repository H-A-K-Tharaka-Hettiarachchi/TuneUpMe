package com.kshprimeindustries.tuneupme;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("com.kshprimeindustries.tuneupme.data", Context.MODE_PRIVATE);
        boolean isLogged = sharedPreferences.getBoolean("logged", false);
        String guestType = sharedPreferences.getString("guestType", null);


        ObjectAnimator textViewT = ObjectAnimator.ofFloat(findViewById(R.id.textViewT), "translationY", 0f, -30f, 0f);
        ObjectAnimator textViewU = ObjectAnimator.ofFloat(findViewById(R.id.textViewU), "translationY", 0f, -30f, 0f);
        ObjectAnimator textViewN = ObjectAnimator.ofFloat(findViewById(R.id.textViewN), "translationY", 0f, -30f, 0f);
        ObjectAnimator textViewE = ObjectAnimator.ofFloat(findViewById(R.id.textViewE), "translationY", 0f, -30f, 0f);
        ObjectAnimator textViewUU = ObjectAnimator.ofFloat(findViewById(R.id.textViewUU), "translationY", 0f, -30f, 0f);
        ObjectAnimator textViewP = ObjectAnimator.ofFloat(findViewById(R.id.textViewP), "translationY", 0f, -30f, 0f);
        ObjectAnimator textViewM = ObjectAnimator.ofFloat(findViewById(R.id.textViewM), "translationY", 0f, -30f, 0f);
        ObjectAnimator textViewEE = ObjectAnimator.ofFloat(findViewById(R.id.textViewEE), "translationY", 0f, -30f, 0f);

        textViewT.setDuration(500);
        textViewU.setDuration(500);
        textViewN.setDuration(500);
        textViewE.setDuration(500);
        textViewUU.setDuration(500);
        textViewP.setDuration(500);
        textViewM.setDuration(500);
        textViewEE.setDuration(500);

        textViewT.setStartDelay(150);
        textViewN.setStartDelay(300);
        textViewE.setStartDelay(500);
        textViewUU.setStartDelay(100);
        textViewP.setStartDelay(200);
        textViewEE.setStartDelay(450);

        textViewT.setRepeatCount(ValueAnimator.INFINITE);
        textViewU.setRepeatCount(ValueAnimator.INFINITE);
        textViewN.setRepeatCount(ValueAnimator.INFINITE);
        textViewE.setRepeatCount(ValueAnimator.INFINITE);
        textViewUU.setRepeatCount(ValueAnimator.INFINITE);
        textViewP.setRepeatCount(ValueAnimator.INFINITE);
        textViewM.setRepeatCount(ValueAnimator.INFINITE);
        textViewEE.setRepeatCount(ValueAnimator.INFINITE);


        int[] textViewIds = {R.id.textViewT, R.id.textViewU, R.id.textViewN, R.id.textViewE, R.id.textViewUU, R.id.textViewP, R.id.textViewM, R.id.textViewEE};

        // Assign random colors
        String[] colors = {"#2a3534", "#db3b5e", "#c42c54", "#394444", "#7d8b8c"};

        Random random = new Random();
        for (int id : textViewIds) {
            TextView textView = findViewById(id);
            int randomIndex = random.nextInt(colors.length); // Pick a random index
            textView.setTextColor(Color.parseColor(colors[randomIndex]));
        }

        textViewT.start();
        textViewU.start();
        textViewN.start();
        textViewE.start();
        textViewUU.start();
        textViewP.start();
        textViewM.start();
        textViewEE.start();


        new Handler().postDelayed(() -> {
            if (isLogged) {
                assert guestType != null;
                if (guestType.equals("customer")) {
                    Intent intent = new Intent(MainActivity.this, TuneUpMeActivityCustomerHome.class);
                    startActivity(intent);
                    finish();
                } else if (guestType.equals("band")) {
                    Intent intent = new Intent(MainActivity.this, TuneUpMeActivityMusicBandHome.class);
                    startActivity(intent);
                    finish();
                }

            } else {
                Intent intent = new Intent(MainActivity.this, GetStartActivity.class);
                startActivity(intent);
                finish();
            }

        }, 4000);


    }
}