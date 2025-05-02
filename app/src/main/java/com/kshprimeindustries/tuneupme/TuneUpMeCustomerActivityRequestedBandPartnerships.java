package com.kshprimeindustries.tuneupme;

import android.annotation.SuppressLint;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kshprimeindustries.tuneupme.adapters.CustomerRequestedBandPartnershipItemAdapter;
import com.kshprimeindustries.tuneupme.model.TuneUpMeCustomerRequestedBandPartnershipItem;

import java.util.ArrayList;
import java.util.Objects;

public class TuneUpMeCustomerActivityRequestedBandPartnerships extends AppCompatActivity {


//    private ArrayList<TuneUpMeCustomerRequestedBandPartnershipItem> tuneUpMeCustomerRequestedBandPartnershipItemArrayList1;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish(); // Close the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    CustomerRequestedBandPartnershipItemAdapter customerRequestedBandPartnershipItemAdapter;
    ArrayList<TuneUpMeCustomerRequestedBandPartnershipItem> tuneUpMeCustomerRequestedBandPartnershipItemArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tune_up_me_customer_requested_band_partnerships);
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

        ImageView buttonNewRequestBandPartnership = findViewById(R.id.buttonNewRequestBandPartnership);


        View.OnClickListener clickListener = v -> {
            animateCardClick(buttonNewRequestBandPartnership);
            if (v.getId() == R.id.buttonNewRequestBandPartnership) {
                Intent intent = new Intent(TuneUpMeCustomerActivityRequestedBandPartnerships.this, TuneUpMeCustomerActivityRequestBandPartnership.class);
                Intent getIntent = getIntent();
                intent.putExtra("id", getIntent.getStringExtra("id"));
                startActivity(intent);
            }
        };

        buttonNewRequestBandPartnership.setOnClickListener(clickListener);


        RecyclerView recyclerViewCustomerRequestedBandPartnerships = findViewById(R.id.recyclerViewCustomerRequestedBandPartnerships);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewCustomerRequestedBandPartnerships.setLayoutManager(linearLayoutManager);

        tuneUpMeCustomerRequestedBandPartnershipItemArrayList = new ArrayList<>();

        customerRequestedBandPartnershipItemAdapter = new CustomerRequestedBandPartnershipItemAdapter(tuneUpMeCustomerRequestedBandPartnershipItemArrayList);

//        loadRecycler(customerRequestedBandPartnershipItemAdapter, tuneUpMeCustomerRequestedBandPartnershipItemArrayList);

        recyclerViewCustomerRequestedBandPartnerships.setAdapter(customerRequestedBandPartnershipItemAdapter);


    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        tuneUpMeCustomerRequestedBandPartnershipItemArrayList.clear();
        customerRequestedBandPartnershipItemAdapter.notifyDataSetChanged();
        loadRecycler(this.customerRequestedBandPartnershipItemAdapter, this.tuneUpMeCustomerRequestedBandPartnershipItemArrayList);
    }


    private void loadRecycler(CustomerRequestedBandPartnershipItemAdapter customerRequestedBandPartnershipItemAdapter, ArrayList<TuneUpMeCustomerRequestedBandPartnershipItem> tuneUpMeCustomerRequestedBandPartnershipItemArrayList) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("band_request")
                .whereEqualTo("customer_id", getIntent().getStringExtra("id"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                for (DocumentSnapshot documentSnapshot : querySnapshot) {

                                    firebaseFirestore.collection("band_req_status")
                                            .document(Objects.requireNonNull(documentSnapshot.get("band_req_status_id")).toString())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {


                                                            firebaseFirestore.collection("band_type")
                                                                    .document(Objects.requireNonNull(documentSnapshot.get("band_type_id")).toString())
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                        @SuppressLint("NotifyDataSetChanged")
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                DocumentSnapshot snapshot = task.getResult();
                                                                                if (snapshot.exists()) {
                                                                                    String band_req_status = document.getString("status");
                                                                                    String band_type = snapshot.getString("type");

                                                                                    tuneUpMeCustomerRequestedBandPartnershipItemArrayList.add(new TuneUpMeCustomerRequestedBandPartnershipItem(
                                                                                            documentSnapshot.getId(),
                                                                                            String.valueOf(documentSnapshot.get("name")),
                                                                                            band_type,
                                                                                            String.valueOf(documentSnapshot.get("email")),
                                                                                            String.valueOf(documentSnapshot.get("mobile")),
                                                                                            band_req_status,
                                                                                            getIntent().getStringExtra("id")
                                                                                    ));
                                                                                    customerRequestedBandPartnershipItemAdapter.notifyDataSetChanged();


                                                                                    if (tuneUpMeCustomerRequestedBandPartnershipItemArrayList.size() != 0) {
                                                                                        ImageView imageViewEmpty = findViewById(R.id.imageViewRequestedBandPartnershipEmpty);
                                                                                        imageViewEmpty.setVisibility(View.GONE);
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