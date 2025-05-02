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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kshprimeindustries.tuneupme.listeners.NetworkChangeReceiver;
import com.kshprimeindustries.tuneupme.model.Validations;

import java.util.HashMap;


import es.dmoral.toasty.Toasty;

public class LogAsCustomerActivitySignUp extends AppCompatActivity {

    NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, LogAsCustomerActivityEnterEmail.class); // Change to your previous activity
            startActivity(intent);
            finish(); // Close the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
//        if (!networkChangeReceiver.isNetworkAvailable(getApplicationContext())) {
//            Intent intent = new Intent(LogAsCustomerActivitySignUp.this, TuneUpMeActivityNetworkNotAvailable.class);
//            startActivity(intent);
//        }
//        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_as_customer_sign_up);
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


        TextView fullNameEditText = findViewById(R.id.fullNameEditText);
        TextView emailEditText = findViewById(R.id.emailEditText);
        TextView mobileEditText = findViewById(R.id.mobileEditText);
        TextView passwordEditText = findViewById(R.id.passwordEditText);
        TextView confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);


        Button logAsCustomerSignUpSignUpButton = findViewById(R.id.logAsCustomerSignUpSignUpButton);
        logAsCustomerSignUpSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullName = fullNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String mobile = mobileEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();


                if (fullName.isEmpty() || fullName.trim().isEmpty()) {
                    Toasty.error(LogAsCustomerActivitySignUp.this, "Please Enter Your Full Name.", Toast.LENGTH_SHORT, true).show();
                } else if (email.isEmpty() || email.trim().isEmpty()) {
                    Toasty.error(LogAsCustomerActivitySignUp.this, "Please Enter Your Email.", Toast.LENGTH_SHORT, true).show();
                } else if (mobile.isEmpty() || mobile.trim().isEmpty()) {
                    Toasty.error(LogAsCustomerActivitySignUp.this, "Please Enter Your Mobile.", Toast.LENGTH_SHORT, true).show();
                } else if (password.isEmpty() || password.trim().isEmpty()) {
                    Toasty.error(LogAsCustomerActivitySignUp.this, "Please Enter Your Password.", Toast.LENGTH_SHORT, true).show();
                } else if (confirmPassword.isEmpty() || confirmPassword.trim().isEmpty()) {
                    Toasty.error(LogAsCustomerActivitySignUp.this, "Please Re-Enter Your Password.", Toast.LENGTH_SHORT, true).show();
                } else if (!password.equals(confirmPassword)) {
                    Toasty.error(LogAsCustomerActivitySignUp.this, "Your Password and Confirm Password Doesn't Match.", Toast.LENGTH_SHORT, true).show();
                } else {

                    if (!Validations.isEmailValid(email)) {
                        Toasty.error(LogAsCustomerActivitySignUp.this, "Invalid Email.", Toast.LENGTH_SHORT, true).show();
                    } else if (!Validations.isMobileNumberValid(mobile)) {
                        Toasty.error(LogAsCustomerActivitySignUp.this, "Invalid Mobile.", Toast.LENGTH_SHORT, true).show();
                    } else {
                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        firebaseFirestore.collection("customer")
                                .where(
                                        Filter.or(
                                                Filter.equalTo("mobile", mobile),
                                                Filter.equalTo("email", email)
                                        )
                                )
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            QuerySnapshot querySnapshot = task.getResult();
                                            if (querySnapshot != null && !querySnapshot.isEmpty()){
                                                Toasty.warning(LogAsCustomerActivitySignUp.this, "Email or Mobile Already Exists.", Toast.LENGTH_SHORT, true).show();
                                            }else {
                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("name",String.valueOf(fullName));
                                                hashMap.put("email",String.valueOf(email));
                                                hashMap.put("mobile",String.valueOf(mobile));
                                                hashMap.put("password",String.valueOf(password));
                                                hashMap.put("profile_image_path","");
                                                hashMap.put("status",false);
                                                hashMap.put("verification_code","");

                                                firebaseFirestore.collection("customer")
                                                        .add(hashMap)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Toasty.success(LogAsCustomerActivitySignUp.this, "Registration Success.", Toast.LENGTH_SHORT, true).show();
                                                                Intent intent = new Intent(LogAsCustomerActivitySignUp.this,LogAsCustomerActivityEnterEmail.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toasty.error(LogAsCustomerActivitySignUp.this, "Oops Something went wrong,Please try again shortly", Toast.LENGTH_SHORT, true).show();
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                });
                    }

                }


            }
        });


        Button logAsCustomerSignUpSignInButton = findViewById(R.id.logAsCustomerSignUpSignInButton);
        logAsCustomerSignUpSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogAsCustomerActivitySignUp.this, LogAsCustomerActivityEnterEmail.class); // Change to your previous activity
                startActivity(intent);
            }
        });

        Button termsAndConditionButton = findViewById(R.id.termsAndConditionButton);
        termsAndConditionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogAsCustomerActivitySignUp.this, TuneUpMeActivityTermsConditions.class); // Change to your previous activity
                startActivity(intent);
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