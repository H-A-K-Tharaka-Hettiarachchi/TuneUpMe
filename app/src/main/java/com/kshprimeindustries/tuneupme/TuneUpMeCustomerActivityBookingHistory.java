package com.kshprimeindustries.tuneupme;

import static android.content.ContentValues.TAG;
import static com.kshprimeindustries.tuneupme.adapters.TuneUpMeCustomerBookingHistoryAdapter.PAYHERE_REQUEST;
import static com.kshprimeindustries.tuneupme.adapters.TuneUpMeCustomerBookingHistoryAdapter.itemId;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kshprimeindustries.tuneupme.adapters.TuneUpMeCustomerBookingHistoryAdapter;
import com.kshprimeindustries.tuneupme.model.TuneUpMeCustomerBookingHistoryItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;

public class TuneUpMeCustomerActivityBookingHistory extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
            finish(); // Close the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tune_up_me_customer_booking_history);
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


        RecyclerView recyclerViewCustomerBookingHistory = findViewById(R.id.recyclerViewCustomerBookingHistory);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewCustomerBookingHistory.setLayoutManager(linearLayoutManager);

        ArrayList<TuneUpMeCustomerBookingHistoryItem> tuneUpMeCustomerBookingHistoryItemArrayList = new ArrayList<>();
        TuneUpMeCustomerBookingHistoryAdapter tuneUpMeCustomerBookingHistoryAdapter = new TuneUpMeCustomerBookingHistoryAdapter(tuneUpMeCustomerBookingHistoryItemArrayList);


        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("band_booking_request")
                .whereEqualTo("customer_id", getIntent().getStringExtra("id"))
                .orderBy("start_date_time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                for (DocumentSnapshot documentSnapshot : querySnapshot) {

                                    firebaseFirestore.collection("band").document(Objects.requireNonNull(documentSnapshot.getString("band_id")))
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {


                                                            firebaseFirestore.collection("book_req_status")
                                                                    .document(Objects.requireNonNull(documentSnapshot.getString("book_req_status_id")))
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                        @SuppressLint("NotifyDataSetChanged")
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                DocumentSnapshot documentSnap = task.getResult();
                                                                                if (documentSnap.exists()) {


                                                                                    if (documentSnap.getString("status").equals("accepted")) {

                                                                                        firebaseFirestore.collection("band_schedule")
                                                                                                .where(
                                                                                                        Filter.and(
                                                                                                                Filter.equalTo("status", true),
                                                                                                                Filter.equalTo("req_id", documentSnapshot.getId())
                                                                                                        )
                                                                                                )
                                                                                                .get()
                                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                    @SuppressLint("NotifyDataSetChanged")
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            QuerySnapshot snapshot = task.getResult();
                                                                                                            if (snapshot != null && !snapshot.isEmpty()) {

                                                                                                                tuneUpMeCustomerBookingHistoryItemArrayList.add(new TuneUpMeCustomerBookingHistoryItem(
                                                                                                                        documentSnapshot.getId(),
                                                                                                                        document.getId(),
                                                                                                                        document.getString("name"),
                                                                                                                        documentSnapshot.getString("start_date_time"),
                                                                                                                        documentSnapshot.getString("end_date_time"),
                                                                                                                        "booked",
                                                                                                                        documentSnapshot.getString("latitude"),
                                                                                                                        documentSnapshot.getString("longitude")
                                                                                                                ));
                                                                                                                tuneUpMeCustomerBookingHistoryAdapter.notifyDataSetChanged();

                                                                                                                if (tuneUpMeCustomerBookingHistoryItemArrayList.size() != 0) {
                                                                                                                    ImageView imageViewEmpty = findViewById(R.id.imageViewBookingHistoryEmpty);
                                                                                                                    imageViewEmpty.setVisibility(View.GONE);
                                                                                                                }
                                                                                                            } else {


                                                                                                                tuneUpMeCustomerBookingHistoryItemArrayList.add(new TuneUpMeCustomerBookingHistoryItem(
                                                                                                                        documentSnapshot.getId(),
                                                                                                                        document.getId(),
                                                                                                                        document.getString("name"),
                                                                                                                        documentSnapshot.getString("start_date_time"),
                                                                                                                        documentSnapshot.getString("end_date_time"),
                                                                                                                        documentSnap.getString("status"),
                                                                                                                        documentSnapshot.getString("latitude"),
                                                                                                                        documentSnapshot.getString("longitude")
                                                                                                                ));
                                                                                                                tuneUpMeCustomerBookingHistoryAdapter.notifyDataSetChanged();

                                                                                                                if (tuneUpMeCustomerBookingHistoryItemArrayList.size() != 0) {
                                                                                                                    ImageView imageViewEmpty = findViewById(R.id.imageViewBookingHistoryEmpty);
                                                                                                                    imageViewEmpty.setVisibility(View.GONE);
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                });


                                                                                    } else {
                                                                                        tuneUpMeCustomerBookingHistoryItemArrayList.add(new TuneUpMeCustomerBookingHistoryItem(
                                                                                                documentSnapshot.getId(),
                                                                                                document.getId(),
                                                                                                document.getString("name"),
                                                                                                documentSnapshot.getString("start_date_time"),
                                                                                                documentSnapshot.getString("end_date_time"),
                                                                                                documentSnap.getString("status"),
                                                                                                documentSnapshot.getString("latitude"),
                                                                                                documentSnapshot.getString("longitude")
                                                                                        ));
                                                                                        tuneUpMeCustomerBookingHistoryAdapter.notifyDataSetChanged();

                                                                                        if (tuneUpMeCustomerBookingHistoryItemArrayList.size() != 0) {
                                                                                            ImageView imageViewEmpty = findViewById(R.id.imageViewBookingHistoryEmpty);
                                                                                            imageViewEmpty.setVisibility(View.GONE);
                                                                                        }

                                                                                    }


                                                                                }
                                                                            }
                                                                        }
                                                                    });


                                                        }
                                                    }
                                                }
                                            });


                                }
                            }
                        }
                    }
                });

        recyclerViewCustomerBookingHistory.setAdapter(tuneUpMeCustomerBookingHistoryAdapter);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYHERE_REQUEST && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
            if (resultCode == Activity.RESULT_OK) {
                String msg;
                if (response != null) {
                    if (response.isSuccess()) {

                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        firebaseFirestore.collection("band_schedule")
                                .whereEqualTo("req_id", itemId)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                            if (documentSnapshot.exists()) {

                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("status", true);

                                                firebaseFirestore.collection("band_schedule")
                                                        .document(documentSnapshot.getId())
                                                        .update(hashMap)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toasty.success(TuneUpMeCustomerActivityBookingHistory.this, "Payment Updated !", Toast.LENGTH_LONG, true).show();
                                                                Intent intent = new Intent(TuneUpMeCustomerActivityBookingHistory.this,TuneUpMeCustomerActivityBookingHistory.class);
                                                                SharedPreferences sharedPreferences = getSharedPreferences("com.kshprimeindustries.tuneupme.data", Context.MODE_PRIVATE);
                                                                String customer_id = sharedPreferences.getString("id", null);
                                                                intent.putExtra("id", customer_id);
                                                                startActivity(intent);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toasty.error(TuneUpMeCustomerActivityBookingHistory.this, "Oops Something went wrong, Payment Not Updated !", Toast.LENGTH_LONG, true).show();
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                });

                    } else {
                        Log.w("Payment Response", "Response Not Success");
                    }
                } else {
                    Log.w("Payment Response", "Response Null");
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (response != null) {
                    System.out.println(response.toString());
                } else {
                    System.out.println("User canceled the request");
                }
            }
        }
    }


}