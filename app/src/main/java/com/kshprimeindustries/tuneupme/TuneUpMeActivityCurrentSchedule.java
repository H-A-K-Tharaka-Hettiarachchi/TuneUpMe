package com.kshprimeindustries.tuneupme;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kshprimeindustries.tuneupme.adapters.CurrentBandScheduleItemAdapter;
import com.kshprimeindustries.tuneupme.model.TuneUpMeCurrentBandScheduleItem;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TuneUpMeActivityCurrentSchedule extends AppCompatActivity {

    private CurrentBandScheduleItemAdapter currentBandScheduleItemAdapter;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tune_up_me_current_schedule);
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
        String band_id = getIntent.getStringExtra("band_id");

        Bundle bundle = new Bundle();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-hh:mm a", Locale.getDefault());
        String todayDateStr = sdf.format(new Date());

        RecyclerView recyclerView = findViewById(R.id.recyclerViewCurrentBandSchedule);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<TuneUpMeCurrentBandScheduleItem> tuneUpMeCurrentBandScheduleItemArrayList = new ArrayList<>();

        CurrentBandScheduleItemAdapter currentBandScheduleItemAdapter = new CurrentBandScheduleItemAdapter(tuneUpMeCurrentBandScheduleItemArrayList);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        assert band_id != null;
        firebaseFirestore.collection("band")
                .document(band_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                bundle.putString("name", documentSnapshot.getString("name"));

                                firebaseFirestore.collection("band_schedule")
                                        .whereEqualTo("band_id", documentSnapshot.getId())
                                        .whereGreaterThanOrEqualTo("start_date_time", todayDateStr)
                                        .orderBy("start_date_time", Query.Direction.ASCENDING)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    QuerySnapshot querySnapshot = task.getResult();
                                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                                        for (DocumentSnapshot document : querySnapshot) {
                                                            tuneUpMeCurrentBandScheduleItemArrayList.add(
                                                                    new TuneUpMeCurrentBandScheduleItem(
                                                                            bundle.getString("name"),
                                                                            String.valueOf(document.get("start_date_time")),
                                                                            String.valueOf(document.get("end_date_time")),
                                                                            Boolean.TRUE.equals(document.getBoolean("status"))
                                                                    )
                                                            );
                                                        }
                                                        if (tuneUpMeCurrentBandScheduleItemArrayList.size() != 0) {
                                                            ImageView imageViewEmpty = findViewById(R.id.imageViewCurrentScheduleEmpty);
                                                            imageViewEmpty.setVisibility(View.GONE);
                                                        }
                                                    }
                                                }
                                                currentBandScheduleItemAdapter.notifyDataSetChanged();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                        });

                            }
                        }
                    }
                });

        recyclerView.setAdapter(currentBandScheduleItemAdapter);


    }
}