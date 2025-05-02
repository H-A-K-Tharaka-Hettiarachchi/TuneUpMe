package com.kshprimeindustries.tuneupme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kshprimeindustries.tuneupme.model.SQLiteHelper;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class LogAsCustomerActivitySignInWithPassword extends AppCompatActivity {

    private EditText logAsCustomerPasswordEditeText;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_as_customer_sign_in_with_password);
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

        EditText logAsCustomerPasswordEditeText = findViewById(R.id.logAsCustomerPasswordEditeText);

        Button logAsCustomerForgotPasswordButton = findViewById(R.id.logAsCustomerForgotPasswordButton);
        logAsCustomerForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogAsCustomerActivitySignInWithPassword.this, LogAsCustomerActivityForgotPassword.class); // Change to your previous activity
                startActivity(intent);
                finish(); // Close the current activity
            }
        });

        Button logAsCustomerSignInWithPasswordSignInButton = findViewById(R.id.logAsCustomerSignInWithPasswordSignInButton);
        logAsCustomerSignInWithPasswordSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = logAsCustomerPasswordEditeText.getText().toString();
                if (password.isEmpty() || password.trim().isEmpty()) {
                    Toasty.error(LogAsCustomerActivitySignInWithPassword.this, "Please Enter Your Password.", Toast.LENGTH_SHORT, true).show();
                } else {
                    Intent getIntent = getIntent();
                    String email = getIntent.getStringExtra("email");

                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    firebaseFirestore.collection("customer")
                            .where(
                                    Filter.and(
                                            Filter.equalTo("email", email),
                                            Filter.equalTo("password", password)
                                    )
                            )
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        QuerySnapshot querySnapshot = task.getResult();
                                        if (querySnapshot != null && !querySnapshot.isEmpty()) {

                                            for (QueryDocumentSnapshot document : querySnapshot) {

                                                String id = document.getId();
                                                String email = document.getString("email");
                                                String mobile = document.getString("mobile");
                                                String name = document.getString("name");
                                                String verification_code = document.getString("verification_code");
                                                boolean status = Boolean.TRUE.equals(document.getBoolean("status"));


                                                if (status) {

                                                    SharedPreferences sharedPreferences = getSharedPreferences("com.kshprimeindustries.tuneupme.data", Context.MODE_PRIVATE);
                                                    boolean isLogged = sharedPreferences.getBoolean("logged", false);

                                                    if (isLogged) {
                                                        Toasty.success(LogAsCustomerActivitySignInWithPassword.this, "Sign In Success", Toast.LENGTH_SHORT, true).show();

                                                        Intent intent = new Intent(LogAsCustomerActivitySignInWithPassword.this, TuneUpMeActivityCustomerHome.class);
                                                        intent.putExtra("email",email);
                                                        intent.putExtra("name",name);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putBoolean("logged", true);
                                                        editor.putString("guestType", "customer");
                                                        editor.putString("id",id);
                                                        editor.apply();
                                                        Toasty.success(LogAsCustomerActivitySignInWithPassword.this, "Sign In Success", Toast.LENGTH_SHORT, true).show();

                                                        SQLiteHelper sqLiteHelper = new SQLiteHelper(LogAsCustomerActivitySignInWithPassword.this, "TuneUpMe", null, TuneUpMeApplication.sqliteVersion);
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
                                                                sqLiteDatabase.execSQL("INSERT INTO customer (email, mobile, name, password, profile_image_path, status, verification_code) \n" +
                                                                        "VALUES (\n" +
                                                                        "    '" + email + "', \n" +
                                                                        "    '"+mobile+"', \n" +
                                                                        "    '"+name+"', \n" +
                                                                        "    '"+password+"', \n" +
                                                                        "    '', \n" +
                                                                        "    1, \n" +
                                                                        "    '"+verification_code+"'\n" +
                                                                        ");\n");
                                                            }
                                                        }).start();

                                                        Intent intent = new Intent(LogAsCustomerActivitySignInWithPassword.this, TuneUpMeActivityCustomerHome.class);
                                                        intent.putExtra("email",email);
                                                        intent.putExtra("name",name);
                                                        startActivity(intent);
                                                        finish();
                                                    }

                                                } else {
                                                    LayoutInflater layoutInflater = LayoutInflater.from(LogAsCustomerActivitySignInWithPassword.this);
                                                    View view = layoutInflater.inflate(R.layout.layout_alert_confirmation, null, false);

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(LogAsCustomerActivitySignInWithPassword.this);
                                                    builder.setView(view);

                                                    TextView textViewAlertTitle = view.findViewById(R.id.textViewAlertTitle);
                                                    TextView textViewAlertContent = view.findViewById(R.id.textViewAlertContent);
                                                    Button buttonCancelAlert = view.findViewById(R.id.buttonCancelAlert);
                                                    Button buttonAcceptAlert = view.findViewById(R.id.buttonAcceptAlert);

                                                    textViewAlertTitle.setText("Confirmation");
                                                    textViewAlertContent.setText(
                                                            "Your account is not verified," +
                                                                    "You Need to verify your account to continue the SignIn." +
                                                                    "Do you need to verify your account ?");

                                                    buttonCancelAlert.setText("Cancel");
                                                    buttonAcceptAlert.setText("Verify");


                                                    AlertDialog alertDialog = builder.create();
                                                    Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_parent_background);
                                                    alertDialog.show();


                                                    buttonCancelAlert.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            alertDialog.dismiss();
                                                        }
                                                    });
                                                    buttonAcceptAlert.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(LogAsCustomerActivitySignInWithPassword.this, LogAsCustomerActivityVerifyEmail.class);
                                                            intent.putExtra("email", email);
                                                            intent.putExtra("id", id);
                                                            startActivity(intent);
                                                        }
                                                    });

                                                }

                                            }

                                        } else {
                                            Toasty.error(LogAsCustomerActivitySignInWithPassword.this, "Invalid Email or Password", Toast.LENGTH_SHORT, true).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });


    }
}