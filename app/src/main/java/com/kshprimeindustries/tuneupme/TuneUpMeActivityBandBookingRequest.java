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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kshprimeindustries.tuneupme.adapters.TuneUpMeBandBookingRequestItemAdapter;
import com.kshprimeindustries.tuneupme.model.TuneUpMeBandBookingRequestItem;

import java.util.ArrayList;
import java.util.Objects;

public class TuneUpMeActivityBandBookingRequest extends AppCompatActivity {

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
        setContentView(R.layout.activity_tune_up_me_band_booking_request);
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

        RecyclerView recyclerViewBandBookingRequestMain = findViewById(R.id.recyclerViewBandBookingRequestMain);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewBandBookingRequestMain.setLayoutManager(linearLayoutManager);

        ArrayList<TuneUpMeBandBookingRequestItem> tuneUpMeBandBookingRequestItemArrayList = new ArrayList<>();
        TuneUpMeBandBookingRequestItemAdapter tuneUpMeBandBookingRequestItemAdapter = new TuneUpMeBandBookingRequestItemAdapter(tuneUpMeBandBookingRequestItemArrayList);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("book_req_status")
                .whereEqualTo("status", "pending")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnaps = task.getResult();
                            if (querySnaps != null && !querySnaps.isEmpty()) {
                                for (DocumentSnapshot snapshot : querySnaps) {
                                    firebaseFirestore.collection("band_booking_request")
                                            .where(
                                                    Filter.and(
                                                            Filter.equalTo("book_req_status_id", snapshot.getId()),
                                                            Filter.equalTo("band_id", getIntent().getStringExtra("id"))
                                                    )
                                            )
                                            .orderBy("start_date_time", Query.Direction.ASCENDING)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {

                                                        QuerySnapshot querySnapshot = task.getResult();
                                                        if (querySnapshot != null && !querySnapshot.isEmpty()) {

                                                            for (DocumentSnapshot documentSnapshot : querySnapshot) {

                                                                firebaseFirestore.collection("customer")
                                                                        .document(Objects.requireNonNull(documentSnapshot.getString("customer_id")))
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                            @SuppressLint("NotifyDataSetChanged")
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    DocumentSnapshot document = task.getResult();
                                                                                    if (document.exists()) {
                                                                                        tuneUpMeBandBookingRequestItemArrayList.add(new TuneUpMeBandBookingRequestItem(
                                                                                                documentSnapshot.getId(),
                                                                                                document.getId(),
                                                                                                documentSnapshot.getString("band_id"),
                                                                                                document.getString("name"),
                                                                                                document.getString("email"),
                                                                                                document.getString("mobile"),
                                                                                                documentSnapshot.getString("start_date_time"),
                                                                                                documentSnapshot.getString("end_date_time"),
                                                                                                documentSnapshot.getString("latitude"),
                                                                                                documentSnapshot.getString("longitude")
                                                                                        ));
                                                                                        tuneUpMeBandBookingRequestItemAdapter.notifyDataSetChanged();

                                                                                        if (tuneUpMeBandBookingRequestItemArrayList.size() != 0) {
                                                                                            ImageView imageViewEmpty = findViewById(R.id.imageViewBandBookingRequestEmpty);
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


        recyclerViewBandBookingRequestMain.setAdapter(tuneUpMeBandBookingRequestItemAdapter);

    }
}