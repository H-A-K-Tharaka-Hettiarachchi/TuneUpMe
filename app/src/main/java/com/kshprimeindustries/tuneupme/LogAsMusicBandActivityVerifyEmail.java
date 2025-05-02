package com.kshprimeindustries.tuneupme;

import static com.kshprimeindustries.tuneupme.TuneUpMeApplication.ngrock_url;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kshprimeindustries.tuneupme.model.SQLiteHelper;

import java.io.IOException;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LogAsMusicBandActivityVerifyEmail extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, LogAsMusicBandActivityEnterEmail.class); // Change to your previous activity
            startActivity(intent);
            finish(); // Close the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    String otpCode;
    EditText[] otpFields;

    String id;
    String typedOtp;
    TextView countdownTextViewLogAsMusicBand;
    int countdownTime = 30;
    Button buttonViewLogAsMusicBandVerifyEmailReSendCodeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_as_music_band_verify_email);
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

        String email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");

        otpCode = sendOTP(email);

        otpFields = new EditText[]{
                findViewById(R.id.logAsMusicBandVerifyEmailOtp1),
                findViewById(R.id.logAsMusicBandVerifyEmailOtp2),
                findViewById(R.id.logAsMusicBandVerifyEmailOtp3),
                findViewById(R.id.logAsMusicBandVerifyEmailOtp4),
                findViewById(R.id.logAsMusicBandVerifyEmailOtp5),
                findViewById(R.id.logAsMusicBandVerifyEmailOtp6)
        };

        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;
            otpFields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpFields.length - 1) {
                        otpFields[index + 1].requestFocus(); // Move to next field
                    } else if (s.length() == 0 && index > 0) {
                        otpFields[index - 1].requestFocus(); // Move to previous field on backspace
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (isOtpFilled()) {
                        typedOtp = getOtp();
                    }
                }
            });
        }

        countdownTextViewLogAsMusicBand = findViewById(R.id.countdownTextViewLogAsMusicBand);
        buttonViewLogAsMusicBandVerifyEmailReSendCodeButton = findViewById(R.id.buttonViewLogAsMusicBandVerifyEmailReSendCodeButton);

        Button buttonLogAsMusicBandVerifyEmailSignIn = findViewById(R.id.buttonLogAsMusicBandVerifyEmailSignIn);
        buttonLogAsMusicBandVerifyEmailSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("band")
                        .document(id)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()) {

                                        String name = String.valueOf(documentSnapshot.get("name"));
                                        String mobile = String.valueOf(documentSnapshot.get("mobile"));
                                        String description = String.valueOf(documentSnapshot.get("description"));
                                        String price_per_hour = String.valueOf(documentSnapshot.get("price_per_hour"));
                                        String profile_image_path = String.valueOf(documentSnapshot.get("profile_image_path"));
                                        String band_type_id = String.valueOf(documentSnapshot.get("band_type_id"));
                                        String verification_code = String.valueOf(documentSnapshot.get("verification_code"));

                                        if (typedOtp.equals(verification_code)) {

                                            Toasty.success(LogAsMusicBandActivityVerifyEmail.this, "Verification Completed.", Toast.LENGTH_SHORT, true).show();

                                            SharedPreferences sharedPreferences = getSharedPreferences("com.kshprimeindustries.tuneupme.data", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean("logged", true);
                                            editor.putString("guestType", "band");
                                            editor.putString("id", id);
                                            editor.apply();


                                            SQLiteHelper sqLiteHelper = new SQLiteHelper(LogAsMusicBandActivityVerifyEmail.this, "TuneUpMe", null, 2);
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();

                                                    ContentValues contentValues = new ContentValues();
                                                    contentValues.put("name", name);
                                                    contentValues.put("mobile", mobile);
                                                    contentValues.put("description", description);
                                                    contentValues.put("price_per_hour", price_per_hour);
                                                    contentValues.put("profile_image_path", profile_image_path);
                                                    contentValues.put("band_type_id", band_type_id);
                                                    contentValues.put("verification_code", verification_code);
                                                    contentValues.put("email", email);

                                                    sqLiteDatabase.insert("band", null, contentValues);
                                                }
                                            }).start();


                                            Intent intent = new Intent(LogAsMusicBandActivityVerifyEmail.this, TuneUpMeActivityMusicBandHome.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toasty.error(LogAsMusicBandActivityVerifyEmail.this, "Invalid OTP.", Toast.LENGTH_SHORT, true).show();
                                        }
                                    }
                                }
                            }
                        });


            }
        });


    }

    private boolean isOtpFilled() {
        for (EditText otpField : otpFields) {
            if (otpField.getText().toString().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private String getOtp() {
        StringBuilder otp = new StringBuilder();
        for (EditText otpField : otpFields) {
            otp.append(otpField.getText().toString());
        }
        return otp.toString();
    }

    private String sendOTP(String email) {
        // Generate a random 6-digit OTP
        int otp = (int) (Math.random() * 900000) + 100000;

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("email", email);
//                jsonObject.addProperty("type", "customer");
                jsonObject.addProperty("otp", String.valueOf(otp));

                RequestBody body = RequestBody.create(
                        jsonObject.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(ngrock_url + "/TuneUpMe/TuneUpMeSendEmailOtp")
                        .post(body)
                        .build();


                try {
                    Response response = okHttpClient.newCall(request).execute();

                    if (response.isSuccessful() && response.body() != null) {

                        String resText = response.body().string();
                        JsonObject responseJson = JsonParser.parseString(resText).getAsJsonObject();

                        boolean status = responseJson.get("status").getAsBoolean();

                        if (status) {
                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("verification_code", otpCode);
                            firebaseFirestore.collection("band")
                                    .document(id)
                                    .update(hashMap);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toasty.error(LogAsMusicBandActivityVerifyEmail.this, "Oops Something went wrong,Please try again shortly", Toast.LENGTH_SHORT, true).show();
                                }
                            });
                        }

                        Log.e("Error", String.valueOf(responseJson));
                        Log.e("Error", String.valueOf(status));

                    } else {
                        Log.e("Error", String.valueOf(response.code()));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Thread timerThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                countdownTextViewLogAsMusicBand.setEnabled(false);
                            }
                        });
                        for (int i = countdownTime; i >= 0; i--) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                            final int timeLeft = i;
                            runOnUiThread(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    countdownTextViewLogAsMusicBand.setText("Time remaining: " + timeLeft + " seconds");
                                }
                            });
                        }


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                buttonViewLogAsMusicBandVerifyEmailReSendCodeButton.setEnabled(true);  // Re-enable the button
                            }
                        });
                    }
                });
                timerThread.start();

            }
        }).start();

        return String.valueOf(otp);
    }


}