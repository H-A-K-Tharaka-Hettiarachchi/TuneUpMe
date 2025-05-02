package com.kshprimeindustries.tuneupme;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kshprimeindustries.tuneupme.adapters.TuneUpMeMessengerAdapter;
import com.kshprimeindustries.tuneupme.model.TuneUpMeMessengerItem;

import java.util.ArrayList;

public class TuneUpMeActivityMessenger extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish(); // Close the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    view.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tune_up_me_messenger);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ArrayList<TuneUpMeMessengerItem> tuneUpMeMessengerItemArrayList = new ArrayList<>();
        tuneUpMeMessengerItemArrayList.add(new TuneUpMeMessengerItem(
                "",
                "Sarith Surith and The News",
                "Hello their",
                "1",
                "10:55 PM",
                String.valueOf(R.drawable.profile_image_sarith_surith_and_the_news),
                true));
        tuneUpMeMessengerItemArrayList.add(new TuneUpMeMessengerItem(
                "",
                "Wayo",
                "Hello wayo",
                "0",
                "11:05 PM",
                String.valueOf(R.drawable.profile_image_wayo),
                false));


        RecyclerView recyclerViewTuneUpMeMessenger = findViewById(R.id.recyclerViewTuneUpMeMessenger);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TuneUpMeActivityMessenger.this);
        recyclerViewTuneUpMeMessenger.setLayoutManager(linearLayoutManager);

        TuneUpMeMessengerAdapter tuneUpMeMessengerAdapter = new TuneUpMeMessengerAdapter(tuneUpMeMessengerItemArrayList);
        recyclerViewTuneUpMeMessenger.setAdapter(tuneUpMeMessengerAdapter);


    }
}