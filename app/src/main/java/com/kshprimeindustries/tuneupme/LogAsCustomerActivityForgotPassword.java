package com.kshprimeindustries.tuneupme;

import android.content.Intent;
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
import com.kshprimeindustries.tuneupme.model.Validations;

import es.dmoral.toasty.Toasty;

public class LogAsCustomerActivityForgotPassword extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, LogAsCustomerActivitySignInWithPassword.class); // Change to your previous activity
            startActivity(intent);
            finish(); // Close the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_as_customer_forgot_password);
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

        EditText logAsCustomerForgotPasswordEmailEditeText = findViewById(R.id.logAsCustomerForgotPasswordEmailEditeText);


        Button logAsCustomerForgotPasswordSendCodeButton = findViewById(R.id.logAsCustomerForgotPasswordSendCodeButton);
        logAsCustomerForgotPasswordSendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = String.valueOf(logAsCustomerForgotPasswordEmailEditeText.getText());

                if (email.isEmpty() || email.trim().isEmpty()) {
                    Toasty.error(LogAsCustomerActivityForgotPassword.this, "Please Enter Your Email.", Toast.LENGTH_SHORT, true).show();
                }else   if (!Validations.isEmailValid(email)) {
                    Toasty.error(LogAsCustomerActivityForgotPassword.this, "Invalid Email.", Toast.LENGTH_SHORT, true).show();
                }else {

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
                                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                            for (DocumentSnapshot documentSnapshot : querySnapshot) {
                                                id =documentSnapshot.getId();
                                            }

                                            Intent intent = new Intent(LogAsCustomerActivityForgotPassword.this, LogAsCustomerActivityResetPasswordVerifyEmail.class);
                                            intent.putExtra("id",id);
                                            intent.putExtra("email",email);
                                            startActivity(intent);


                                        }else {
                                            Toasty.error(LogAsCustomerActivityForgotPassword.this, "Invalid Email.", Toast.LENGTH_SHORT, true).show();
                                        }
                                    }
                                }
                            });


                }



            }
        });


    }


}