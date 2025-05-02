package com.kshprimeindustries.tuneupme;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class LogAsCustomerActivityResetPassword extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, LogAsCustomerActivityForgotPassword.class); // Change to your previous activity
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
        setContentView(R.layout.activity_log_as_customer_reset_password);
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

        Intent getIntent = getIntent();
        id = getIntent.getStringExtra("id");
        String email = getIntent.getStringExtra("email");


        EditText logAsCustomerResetPasswordPasswordEditeText = findViewById(R.id.logAsCustomerResetPasswordPasswordEditeText);
        EditText logAsCustomerResetPasswordConfirmPasswordEditeText = findViewById(R.id.logAsCustomerResetPasswordConfirmPasswordEditeText);

        Button logAsCustomerResetPasswordButton= findViewById(R.id.logAsCustomerResetPasswordButton);
        logAsCustomerResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = String.valueOf(logAsCustomerResetPasswordPasswordEditeText.getText());
                String confirmPassword = String.valueOf(logAsCustomerResetPasswordConfirmPasswordEditeText.getText());

                if (password.isEmpty() || password.trim().isEmpty()) {
                    Toasty.error(LogAsCustomerActivityResetPassword.this, "Please Enter Your New Password.", Toast.LENGTH_SHORT, true).show();
                } else if (confirmPassword.isEmpty() || confirmPassword.trim().isEmpty()) {
                    Toasty.error(LogAsCustomerActivityResetPassword.this, "Please Re-Enter Your Password.", Toast.LENGTH_SHORT, true).show();
                } else if (!password.equals(confirmPassword)) {
                    Toasty.error(LogAsCustomerActivityResetPassword.this, "Your New Password and Confirm Password Doesn't Match.", Toast.LENGTH_SHORT, true).show();
                }else {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("password", password);
                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    firebaseFirestore.collection("customer")
                            .document(id)
                            .update(hashMap);
                    Toasty.success(LogAsCustomerActivityResetPassword.this, "New Password Updated.", Toast.LENGTH_SHORT, true).show();
                    Intent intent = new Intent(LogAsCustomerActivityResetPassword.this, LogAsCustomerActivityEnterEmail.class);
                    startActivity(intent);
                    finish();
                }


            }
        });



    }
}