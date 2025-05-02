package com.kshprimeindustries.tuneupme;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class LogAsCustomerActivityEnterEmail extends AppCompatActivity {

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
        setContentView(R.layout.activity_log_as_customer_enter_email);
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

        TextView emailEditText = findViewById(R.id.logAsCustomerEmailEditeText);

        Button logAsCustomerSignInWithPasswordButton = findViewById(R.id.logAsCustomerSignInWithPasswordButton);
        logAsCustomerSignInWithPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEditText.getText().toString();

                if (email.isEmpty() || email.trim().isEmpty()) {
                    Toasty.error(LogAsCustomerActivityEnterEmail.this, "Please Enter Your Email First.", Toast.LENGTH_SHORT, true).show();
                } else if (!Validations.isEmailValid(email)) {
                    Toasty.error(LogAsCustomerActivityEnterEmail.this, "Invalid Email.", Toast.LENGTH_SHORT, true).show();
                } else {
                    Intent intent = new Intent(LogAsCustomerActivityEnterEmail.this, LogAsCustomerActivitySignInWithPassword.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }


            }
        });

        Button logAsCustomerSignUpNowButton = findViewById(R.id.logAsCustomerSignUpNowButton);
        logAsCustomerSignUpNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogAsCustomerActivityEnterEmail.this, LogAsCustomerActivitySignUp.class);
                startActivity(intent);

            }
        });

        Button logAsCustomerSendCodeButton = findViewById(R.id.logAsCustomerSendCodeButton);
        logAsCustomerSendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                if (email.isEmpty() || email.trim().isEmpty()) {
                    Toasty.error(LogAsCustomerActivityEnterEmail.this, "Please Enter Your Email.", Toast.LENGTH_SHORT, true).show();
                }
                if (!Validations.isEmailValid(email)) {
                    Toasty.error(LogAsCustomerActivityEnterEmail.this, "Invalid Email.", Toast.LENGTH_SHORT, true).show();
                } else {
                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    firebaseFirestore.collection("customer")
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
                                                Intent intent = new Intent(LogAsCustomerActivityEnterEmail.this,LogAsCustomerActivityVerifyEmail.class);
                                                intent.putExtra("id",id);
                                                intent.putExtra("email",email);
                                                startActivity(intent);
                                            }
                                        }else {
                                            Toasty.error(LogAsCustomerActivityEnterEmail.this, "Invalid Email.", Toast.LENGTH_SHORT, true).show();
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