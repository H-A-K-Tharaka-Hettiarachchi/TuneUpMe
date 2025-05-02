package com.kshprimeindustries.tuneupme;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class TuneUpMeActivityCustomerBandBookNow extends AppCompatActivity {

    private TextView textViewCustomerBandBookNowPickedEventDate;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish(); // Close the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static String pickedDate;
    static String todayDate;
    static String pickedTime;
    static String timeDurationHours;
    static String timeDurationMinutes;

    public static String latitude;
    public static String longitude;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tune_up_me_customer_band_book_now);
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

        SharedPreferences sharedPreferences = getSharedPreferences("com.kshprimeindustries.tuneupme.data", Context.MODE_PRIVATE);
        String customer_id = sharedPreferences.getString("id", null);


        CardView cardViewCustomerBandBookNowSeeCurrentSchedule = findViewById(R.id.cardViewCustomerBandBookNowSeeCurrentSchedule);
        CardView cardViewCustomerBandBookNowPickLocation = findViewById(R.id.cardViewCustomerBandBookNowPickLocation);
        CardView cardViewCustomerBandBookNowPickEventStartTimeIcon = findViewById(R.id.cardViewCustomerBandBookNowPickEventStartTimeIcon);
        @SuppressLint("CutPasteId") ImageView imageViewCustomerBandBookNowSeeCurrentScheduleIcon = findViewById(R.id.imageViewCustomerBandBookNowSeeCurrentScheduleIcon);
        @SuppressLint("CutPasteId") ImageView imageViewCustomerBandBookNowPickLocation = findViewById(R.id.imageViewCustomerBandBookNowPickLocation);
        @SuppressLint("CutPasteId") ImageView imageViewCustomerBandBookNowPickEventStartTimeIcon = findViewById(R.id.imageViewCustomerBandBookNowPickEventStartTimeIcon);

        CalendarView calendarViewCustomerBandBookNowPickEventDate = findViewById(R.id.calendarViewCustomerBandBookNowPickEventDate);
        TextView textViewCustomerBandBookNowPickedEventDate = findViewById(R.id.textViewCustomerBandBookNowPickedEventDate);
        TextView textViewCustomerBandBookNowPickedEventStartTime = findViewById(R.id.textViewCustomerBandBookNowPickedEventStartTime);

        TextView textViewCustomerBandBookNowPickedEventTimeDuration = findViewById(R.id.textViewCustomerBandBookNowPickedEventTimeDuration);

        EditText editeTextBandBookNowEventTimeDurationHours = findViewById(R.id.editeTextBandBookNowEventTimeDurationHours);
        EditText editeTextBandBookNowEventTimeDurationMinutes = findViewById(R.id.editeTextBandBookNowEventTimeDurationMinutes);

        View.OnClickListener clickListener = v -> {
            if (v.getId() == R.id.cardViewCustomerBandBookNowSeeCurrentSchedule) {
                animateCardClick(cardViewCustomerBandBookNowSeeCurrentSchedule);
                Intent intent = new Intent(TuneUpMeActivityCustomerBandBookNow.this, TuneUpMeActivityCurrentSchedule.class);
                Intent getIntent = getIntent();
                String band_id = getIntent.getStringExtra("band_id");
                intent.putExtra("band_id", band_id);
                startActivity(intent);
            } else if (v.getId() == R.id.imageViewCustomerBandBookNowSeeCurrentScheduleIcon) {
                animateCardClick(cardViewCustomerBandBookNowSeeCurrentSchedule);
                Intent intent = new Intent(TuneUpMeActivityCustomerBandBookNow.this, TuneUpMeActivityCurrentSchedule.class);
                Intent getIntent = getIntent();
                String band_id = getIntent.getStringExtra("band_id");
                intent.putExtra("band_id", band_id);
                startActivity(intent);
            } else if (v.getId() == R.id.cardViewCustomerBandBookNowPickLocation) {
                animateCardClick(cardViewCustomerBandBookNowPickLocation);
                showLocationDialog();
            } else if (v.getId() == R.id.imageViewCustomerBandBookNowPickLocation) {
                animateCardClick(cardViewCustomerBandBookNowPickLocation);
                showLocationDialog();
            } else if (v.getId() == R.id.cardViewCustomerBandBookNowPickEventStartTimeIcon) {
                animateCardClick(cardViewCustomerBandBookNowPickEventStartTimeIcon);
                showTimePickerDialog();
            } else if (v.getId() == R.id.imageViewCustomerBandBookNowPickEventStartTimeIcon) {
                animateCardClick(cardViewCustomerBandBookNowPickEventStartTimeIcon);
                showTimePickerDialog();
            }
        };


        cardViewCustomerBandBookNowSeeCurrentSchedule.setOnClickListener(clickListener);
        cardViewCustomerBandBookNowPickLocation.setOnClickListener(clickListener);
        cardViewCustomerBandBookNowPickEventStartTimeIcon.setOnClickListener(clickListener);
        imageViewCustomerBandBookNowPickLocation.setOnClickListener(clickListener);
        imageViewCustomerBandBookNowSeeCurrentScheduleIcon.setOnClickListener(clickListener);
        imageViewCustomerBandBookNowPickEventStartTimeIcon.setOnClickListener(clickListener);


        calendarViewCustomerBandBookNowPickEventDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // The selected date components are year, month (0-based), and dayOfMonth
                // Format the date to YYYY/MM/DD format
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String formattedDate = sdf.format(selectedDate.getTime());
                pickedDate = formattedDate;
                textViewCustomerBandBookNowPickedEventDate.setText(formattedDate);


                Calendar today = Calendar.getInstance();
                todayDate = sdf.format(today.getTime());

                // Compare picked date with today's date
                if (pickedDate.equals(todayDate)) {
                    Toasty.error(TuneUpMeActivityCustomerBandBookNow.this, "Your Can't Select Today Date", Toast.LENGTH_SHORT, true).show();
                }

            }
        });

        editeTextBandBookNowEventTimeDurationHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String hours = editeTextBandBookNowEventTimeDurationHours.getText().toString();
                if (!hours.isEmpty()) {
                    timeDurationHours = hours;
                    if (timeDurationMinutes == null) {
                        textViewCustomerBandBookNowPickedEventTimeDuration.setText(timeDurationHours + " H " + "00" + " M");
                    } else {
                        textViewCustomerBandBookNowPickedEventTimeDuration.setText(timeDurationHours + " H " + timeDurationMinutes + " M");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String hours = editable.toString();
                if (!hours.isEmpty()) {
                    timeDurationHours = hours;
                }
            }
        });
        editeTextBandBookNowEventTimeDurationMinutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String minutes = editeTextBandBookNowEventTimeDurationMinutes.getText().toString();
                if (!minutes.isEmpty()) {
                    timeDurationMinutes = minutes;
                    if (timeDurationHours == null) {
                        textViewCustomerBandBookNowPickedEventTimeDuration.setText("00" + " H " + timeDurationMinutes + " M");
                    } else {
                        textViewCustomerBandBookNowPickedEventTimeDuration.setText(timeDurationHours + " H " + timeDurationMinutes + " M");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String minutes = editable.toString();
                if (!minutes.isEmpty()) {
                    timeDurationMinutes = minutes;
                }
            }
        });

        @SuppressLint("WrongViewCast") TextView buttonBandBookNowSendRequest = findViewById(R.id.buttonBandBookNowSendRequest);
        buttonBandBookNowSendRequest.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                animateCardClick(v);
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


                if (latitude == null && longitude == null) {
                    Toasty.error(TuneUpMeActivityCustomerBandBookNow.this, "Please Select Event Location.", Toast.LENGTH_SHORT, true).show();
                } else if (latitude == null || longitude == null) {
                    Toasty.error(TuneUpMeActivityCustomerBandBookNow.this, "Oops Something Went Wrong While Selecting Event Location.", Toast.LENGTH_SHORT, true).show();
                } else {


                    if (pickedDate == null) {
                        Toasty.error(TuneUpMeActivityCustomerBandBookNow.this, "Please Enter Event Date.", Toast.LENGTH_SHORT, true).show();
                    } else if (pickedDate.equals(todayDate)) {
                        Toasty.error(TuneUpMeActivityCustomerBandBookNow.this, "You can't select today date.", Toast.LENGTH_SHORT, true).show();
                    } else if (pickedTime == null) {
                        Toasty.error(TuneUpMeActivityCustomerBandBookNow.this, "Please Enter Event Start Time.", Toast.LENGTH_SHORT, true).show();
                    } else if (timeDurationHours == null) {
                        Toasty.error(TuneUpMeActivityCustomerBandBookNow.this, "Please Enter Event Duration Hours.", Toast.LENGTH_SHORT, true).show();
                    } else if (timeDurationMinutes == null) {
                        timeDurationMinutes = "00";
                        textViewCustomerBandBookNowPickedEventTimeDuration.setText("00");

                    } else if (timeDurationHours.contains(":")) {

                        Toasty.error(TuneUpMeActivityCustomerBandBookNow.this, "Invalid Character In Event Duration Hours.", Toast.LENGTH_SHORT, true).show();

                    } else if (timeDurationMinutes.contains(":")) {

                        Toasty.error(TuneUpMeActivityCustomerBandBookNow.this, "Invalid Character In Event Duration Minutes.", Toast.LENGTH_SHORT, true).show();

                    } else {

                        try {
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-hh:mm a");
                            Date startDate = sdf.parse(String.valueOf(pickedDate + "-" + pickedTime));
                            Date endDate = sdf.parse(DateTimeCalculator());
                            assert endDate != null;
                            assert startDate != null;
                            long diffInMillis = endDate.getTime() - startDate.getTime();

                            long diffHours = TimeUnit.MILLISECONDS.toHours(diffInMillis);


                            if (diffHours < 1) {
                                Toasty.error(TuneUpMeActivityCustomerBandBookNow.this, "The minimum time difference must be 1 hour!", Toast.LENGTH_SHORT, true).show();
                            } else if (diffHours > 13) {
                                Toasty.error(TuneUpMeActivityCustomerBandBookNow.this, "The maximum time difference must be 12 hour!", Toast.LENGTH_SHORT, true).show();
                            } else {


                                Intent getIntent = getIntent();
                                String band_id = getIntent.getStringExtra("band_id");

                                String eventEndDateTime = DateTimeCalculator();

                                firebaseFirestore.collection("book_req_status")
                                        .whereEqualTo("status", "pending")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    QuerySnapshot querySnapshot = task.getResult();
                                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                                                        HashMap<String, Object> hashMap = new HashMap<>();
                                                        hashMap.put("band_id", band_id);
                                                        hashMap.put("customer_id", customer_id);
                                                        hashMap.put("book_req_status_id", documentSnapshot.getId());
                                                        hashMap.put("end_date_time", eventEndDateTime);
                                                        hashMap.put("start_date_time", pickedDate + "-" + pickedTime);
                                                        hashMap.put("latitude", latitude);
                                                        hashMap.put("longitude", longitude);

                                                        firebaseFirestore.collection("band_booking_request")
                                                                .add(hashMap)
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {

                                                                        HashMap<String, Object> objectHashMap = new HashMap<>();
                                                                        objectHashMap.put("band_id", band_id);
                                                                        objectHashMap.put("customer_id", customer_id);
                                                                        objectHashMap.put("end_date_time", eventEndDateTime);
                                                                        objectHashMap.put("start_date_time",pickedDate + "-" + pickedTime );
                                                                        objectHashMap.put("req_id", documentReference.getId());
                                                                        objectHashMap.put("status", false);


                                                                        firebaseFirestore.collection("band_schedule")
                                                                                .add(objectHashMap)
                                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                    @Override
                                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                                        Toasty.success(TuneUpMeActivityCustomerBandBookNow.this, "Request Sent Successful.", Toast.LENGTH_SHORT, true).show();
                                                                                        onBackPressed();
                                                                                    }
                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        Toasty.error(TuneUpMeActivityCustomerBandBookNow.this, "Oops Something wend wrong.", Toast.LENGTH_SHORT, true).show();
                                                                                        Log.w("TuneUpMe  Warning", "Band Schedule  not inserted to database");
                                                                                    }
                                                                                });

                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toasty.error(TuneUpMeActivityCustomerBandBookNow.this, "Oops Something wend wrong.", Toast.LENGTH_SHORT, true).show();
                                                                        Log.w("TuneUpMe  Warning", "Band Booking Request not inserted to database");
                                                                    }
                                                                });

                                                    }
                                                }
                                            }
                                        });


                            }

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }


                    }


                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {

            Bundle bundle = data.getExtras();
            assert bundle != null;
            latitude = bundle.getString("latitude", null);
            longitude = bundle.getString("longitude", null);
            Log.d("TuneUpMe", "Received Data: " + latitude);
            Log.d("TuneUpMe", "Received Data: " + longitude);

        }
    }


    private void showLocationDialog() {
        Intent intent = new Intent(TuneUpMeActivityCustomerBandBookNow.this, TuneUpMeActivityLocationPicker.class);
        startActivityForResult(intent, 100);
    }


    private void showTimePickerDialog() {

        Dialog timePickerDialog = new Dialog(this);
        timePickerDialog.setContentView(R.layout.dialog_time_picker);
        timePickerDialog.setCancelable(true);


        TimePicker timePickerCustomerBandBookNowPickEventStartTime = timePickerDialog.findViewById(R.id.timePickerCustomerBandBookNowPickEventStartTime);
        Button buttonCustomerBandBookNowPickEventStartTime = timePickerDialog.findViewById(R.id.buttonCustomerBandBookNowPickEventStartTime);
        TextView textViewCustomerBandBookNowPickedEventStartTime = findViewById(R.id.textViewCustomerBandBookNowPickedEventStartTime);

        // Set TimePicker to 12-hour format
        timePickerCustomerBandBookNowPickEventStartTime.setIs24HourView(false);

        // Handle "Set Time" Button Click
        buttonCustomerBandBookNowPickEventStartTime.setOnClickListener(v -> {
            int hour = timePickerCustomerBandBookNowPickEventStartTime.getHour();
            int minute = timePickerCustomerBandBookNowPickEventStartTime.getMinute();

            // Convert to 12-hour format
            String amPm = (hour >= 12) ? "PM" : "AM";
            int displayHour = (hour > 12) ? hour - 12 : hour;
            if (displayHour == 0) displayHour = 12;

            // Format selected time
            String selectedTime = String.format("%02d:%02d %s", displayHour, minute, amPm);
            pickedTime = selectedTime;
            textViewCustomerBandBookNowPickedEventStartTime.setText(selectedTime);


            timePickerDialog.dismiss();
        });

        timePickerDialog.setOnShowListener(dialog -> {

            Window window = timePickerDialog.getWindow();
            if (window != null) {

                window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));


                GradientDrawable shape = new GradientDrawable();
                shape.setColor(Color.WHITE);
                shape.setCornerRadius(30f);


                window.setBackgroundDrawable(shape);

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(window.getAttributes());
                layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(layoutParams);
            }
        });


        timePickerDialog.show();
    }


    public String DateTimeCalculator() {
        // Assume you have two separate variables for date and time
        String date = pickedDate; // date in YYYY/MM/DD format
        String time = pickedTime; // time in HH:mm AM/PM format
        String formattedDateTime = "";


        try {
            // Combine date and time into a single string with the desired format
            String dateTimeStr = date + "-" + time;

            // Define the date-time format as 'yyyy/MM/dd-hh:mm a'
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-hh:mm a");

            // Parse the date-time string to a Date object
            Date currentDateTime = sdf.parse(dateTimeStr);

            // Create a Calendar instance and set the parsed date
            assert currentDateTime != null;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDateTime);


            calendar.add(Calendar.HOUR_OF_DAY, Integer.parseInt(timeDurationHours));
            calendar.add(Calendar.MINUTE, Integer.parseInt(timeDurationMinutes));
            // Get the new date and time
            Date newDateTime = calendar.getTime();

            // Format the new date-time as desired
            formattedDateTime = sdf.format(newDateTime);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDateTime;
    }


    private void animateCardClick(View view) {
        view.setPressed(true);
        view.invalidate();
        view.postDelayed(() -> view.setPressed(false), 200);

        view.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction(() -> view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(100))
                .start();
    }
}