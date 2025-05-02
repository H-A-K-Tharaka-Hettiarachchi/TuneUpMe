package com.kshprimeindustries.tuneupme;

import android.annotation.SuppressLint;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kshprimeindustries.tuneupme.adapters.TuneUpMeBandMyScheduleItemAdapter;
import com.kshprimeindustries.tuneupme.model.TuneUpMeBandMyScheduleItem;

import java.util.ArrayList;
import java.util.Objects;

public class TuneUpMeActivityBandMySchedule extends AppCompatActivity {

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
        setContentView(R.layout.activity_tune_up_me_band_my_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
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

        RecyclerView recyclerViewBandMyScheduleMain = findViewById(R.id.recyclerViewBandMyScheduleMain);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewBandMyScheduleMain.setLayoutManager(linearLayoutManager);

        ArrayList<TuneUpMeBandMyScheduleItem> tuneUpMeBandMyScheduleItemArrayList = new ArrayList<>();
        TuneUpMeBandMyScheduleItemAdapter tuneUpMeBandMyScheduleItemAdapter = new TuneUpMeBandMyScheduleItemAdapter(tuneUpMeBandMyScheduleItemArrayList);


        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("book_req_status")
                .whereEqualTo("status", "accepted")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                if (documentSnapshot.exists()) {
                                    firebaseFirestore.collection("band_booking_request")
                                            .whereEqualTo("book_req_status_id", documentSnapshot.getId())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        QuerySnapshot querySnaps = task.getResult();
                                                        if (querySnaps != null && !querySnaps.isEmpty()) {
                                                            for (DocumentSnapshot documentSnaps : querySnaps) {

                                                                firebaseFirestore.collection("band_schedule")
                                                                        .where(
                                                                                Filter.and(
                                                                                        Filter.equalTo("req_id", documentSnaps.getId()),
                                                                                        Filter.equalTo("band_id", getIntent().getStringExtra("id")
                                                                                        )
                                                                                )
                                                                        )
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @SuppressLint("NotifyDataSetChanged")
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    QuerySnapshot querySnap = task.getResult();
                                                                                    if (querySnap != null && !querySnap.isEmpty()) {
                                                                                        for (DocumentSnapshot documentSnap : querySnap) {

                                                                                            firebaseFirestore.collection("customer")
                                                                                                    .document(Objects.requireNonNull(documentSnap.getString("customer_id")))
                                                                                                    .get()
                                                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                DocumentSnapshot document = task.getResult();
                                                                                                                if (document.exists()) {
                                                                                                                    tuneUpMeBandMyScheduleItemArrayList.add(new TuneUpMeBandMyScheduleItem(
                                                                                                                            documentSnap.getId(),
                                                                                                                            document.getId(),
                                                                                                                            documentSnap.getString("band_id"),
                                                                                                                            document.getString("name"),
                                                                                                                            document.getString("email"),
                                                                                                                            document.getString("mobile"),
                                                                                                                            documentSnap.getString("start_date_time"),
                                                                                                                            documentSnap.getString("end_date_time"),
                                                                                                                            Boolean.TRUE.equals(documentSnap.getBoolean("status")),
                                                                                                                            documentSnaps.getString("latitude"),
                                                                                                                            documentSnaps.getString("longitude")
                                                                                                                    ));
                                                                                                                    tuneUpMeBandMyScheduleItemAdapter.notifyDataSetChanged();

                                                                                                                    if (tuneUpMeBandMyScheduleItemArrayList.size() != 0) {
                                                                                                                        ImageView imageViewEmpty = findViewById(R.id.imageViewBandMyScheduleEmpty);
                                                                                                                        imageViewEmpty.setVisibility(View.GONE);
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    });
                                                                                        }

                                                                                    }
                                                                                }
                                                                            }
                                                                        });


                                                            }
                                                        }
                                                    }
                                                }
                                            });

                                }
                            }
                        }
                    }
                });

        recyclerViewBandMyScheduleMain.setAdapter(tuneUpMeBandMyScheduleItemAdapter);


    }
}