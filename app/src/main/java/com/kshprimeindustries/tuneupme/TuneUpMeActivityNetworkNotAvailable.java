package com.kshprimeindustries.tuneupme;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kshprimeindustries.tuneupme.listeners.NetworkChangeReceiver;

public class TuneUpMeActivityNetworkNotAvailable extends AppCompatActivity {


    NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (networkChangeReceiver.isNetworkAvailable(getApplicationContext())) {
//               try {
//                   Class<?> previousClass = Class.forName("com.kshprimeindustries.tuneupme." + networkChangeReceiver.context);
//
//                   Intent intent = new Intent(this, previousClass);
//                   startActivity(intent);
//               } catch (ClassNotFoundException  e) {
//                   throw new RuntimeException(e);
//               }
                finish();
            }else {
                finish();
            }
            finish(); // Close the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tune_up_me_network_not_available);
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
//            getSupportActionBar().setTitle("");
        }

    }


}