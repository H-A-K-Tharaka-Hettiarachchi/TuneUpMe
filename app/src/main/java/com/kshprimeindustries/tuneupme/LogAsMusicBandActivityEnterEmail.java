package com.kshprimeindustries.tuneupme;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kshprimeindustries.tuneupme.listeners.NetworkChangeReceiver;
import com.kshprimeindustries.tuneupme.model.Validations;

import es.dmoral.toasty.Toasty;

public class LogAsMusicBandActivityEnterEmail extends AppCompatActivity {

    NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, SelectCustomerTypeActivity.class); // Change to your previous activity
            startActivity(intent);
            finish(); // Close the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_as_music_band_enter_email);
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
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);

        EditText logAsMusicBandEmailEditeText = findViewById(R.id.logAsMusicBandEmailEditeText);

        Button logAsMusicBandSendCodeButton = findViewById(R.id.logAsMusicBandSendCodeButton);
        logAsMusicBandSendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = logAsMusicBandEmailEditeText.getText().toString();

                if (email.isEmpty() && email.trim().isEmpty()) {
                    Toasty.error(LogAsMusicBandActivityEnterEmail.this, "Please Enter Your Email.", Toast.LENGTH_SHORT, true).show();
                } else if (!Validations.isEmailValid(email)) {
                    Toasty.error(LogAsMusicBandActivityEnterEmail.this, "Invalid Email.", Toast.LENGTH_SHORT, true).show();
                } else {
                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    firebaseFirestore.collection("band")
                            .where(
                                    Filter.equalTo("email", email)
                            )
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        QuerySnapshot querySnapshot = task.getResult();
                                        if (querySnapshot != null && !querySnapshot.isEmpty()){
                                            for (DocumentSnapshot documentSnapshot :querySnapshot){
                                                String id = documentSnapshot.getId();
                                                Intent intent = new Intent(LogAsMusicBandActivityEnterEmail.this, LogAsMusicBandActivityVerifyEmail.class);
                                                intent.putExtra("id",id);
                                                intent.putExtra("email", email);
                                                startActivity(intent);
                                            }
                                        }else {
                                            Toasty.error(LogAsMusicBandActivityEnterEmail.this, "Entered Email Invalid.", Toast.LENGTH_SHORT, true).show();
                                        }
                                    }
                                }
                            });


                }


            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }
    }

}