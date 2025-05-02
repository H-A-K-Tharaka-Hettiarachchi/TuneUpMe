package com.kshprimeindustries.tuneupme;


import static com.kshprimeindustries.tuneupme.TuneUpMeApplication.ngrock_url;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kshprimeindustries.tuneupme.adapters.TuneUpMeSelectBandTypeItemAdapter;
import com.kshprimeindustries.tuneupme.model.TuneUpMeSelectBandTypeItem;
import com.kshprimeindustries.tuneupme.model.Validations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TuneUpMeCustomerActivityRequestBandPartnership extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tune_up_me_customer_request_band_partnership);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle bundle = new Bundle();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Spinner spinnerSelectBandType = findViewById(R.id.spinnerSelectBandType);

        ArrayList<TuneUpMeSelectBandTypeItem> tuneUpMeSelectBandTypeItemArrayList = new ArrayList<>();
        tuneUpMeSelectBandTypeItemArrayList.add(new TuneUpMeSelectBandTypeItem("0", "Select Type"));

        TuneUpMeSelectBandTypeItemAdapter tuneUpMeSelectBandTypeItemAdapter = new TuneUpMeSelectBandTypeItemAdapter(
                TuneUpMeCustomerActivityRequestBandPartnership.this,
                R.layout.spinner_select_band_type_item,
                tuneUpMeSelectBandTypeItemArrayList
        );

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("band_type")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                for (DocumentSnapshot documentSnapshot : querySnapshot) {
                                    tuneUpMeSelectBandTypeItemArrayList.add(new TuneUpMeSelectBandTypeItem(documentSnapshot.getId(), documentSnapshot.getString("type")));
                                }
                            }
                        }
                        tuneUpMeSelectBandTypeItemAdapter.notifyDataSetChanged();
                    }
                });

        spinnerSelectBandType.setSelection(0);
        spinnerSelectBandType.setAdapter(tuneUpMeSelectBandTypeItemAdapter);


        spinnerSelectBandType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                TuneUpMeSelectBandTypeItem selectedBandType = (TuneUpMeSelectBandTypeItem) parentView.getItemAtPosition(position);
                String selectedValue = selectedBandType.getId();
                bundle.putString("selectedBandTypeId", selectedValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        EditText editeTextRequestBandPartnershipEmail = findViewById(R.id.editeTextRequestBandPartnershipEmail);
        EditText editeTextRequestBandName = findViewById(R.id.editeTextRequestBandName);
        EditText editeTextRequestBandMobile = findViewById(R.id.editeTextRequestBandMobile);

        TextView buttonRequestBandPartnership = findViewById(R.id.buttonRequestBandPartnership);

        View.OnClickListener clickListener = v -> {
            if (v.getId() == R.id.buttonRequestBandPartnership) {
                animateCardClick(buttonRequestBandPartnership);

                String email = editeTextRequestBandPartnershipEmail.getText().toString();
                String name = editeTextRequestBandName.getText().toString();
                String mobile = editeTextRequestBandMobile.getText().toString();

                if (bundle.getString("selectedBandTypeId", null).equals("0") || bundle.getString("selectedBandTypeId", null) == null) {
                    Toasty.error(TuneUpMeCustomerActivityRequestBandPartnership.this, "Please Select Band Type.", Toast.LENGTH_SHORT, true).show();
                } else {
                    if (email.isEmpty() || email.trim().isEmpty()) {
                        Toasty.error(TuneUpMeCustomerActivityRequestBandPartnership.this, "Please Enter Email.", Toast.LENGTH_SHORT, true).show();
                    } else if (!Validations.isEmailValid(email)) {
                        Toasty.error(TuneUpMeCustomerActivityRequestBandPartnership.this, "Please Enter Valid Email.", Toast.LENGTH_SHORT, true).show();
                    } else if (name.isEmpty() || name.trim().isEmpty()) {
                        Toasty.error(TuneUpMeCustomerActivityRequestBandPartnership.this, "Please Enter Band Name.", Toast.LENGTH_SHORT, true).show();
                    } else if (mobile.isEmpty() || mobile.trim().isEmpty()) {
                        Toasty.error(TuneUpMeCustomerActivityRequestBandPartnership.this, "Please Enter Mobile Number.", Toast.LENGTH_SHORT, true).show();
                    } else if (!Validations.isMobileNumberValid(mobile)) {
                        Toasty.error(TuneUpMeCustomerActivityRequestBandPartnership.this, "Please Enter Valid Mobile Number.", Toast.LENGTH_SHORT, true).show();
                    } else {


                        firebaseFirestore.collection("band_request")
                                .where(
                                        Filter.or(
                                                Filter.equalTo("email", email),
                                                Filter.equalTo("mobile", mobile)
                                        )
                                )
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            QuerySnapshot snapshots = task.getResult();
                                            if (snapshots != null && !snapshots.isEmpty()) {
                                                Toasty.error(TuneUpMeCustomerActivityRequestBandPartnership.this, "Your Entered Email Already Exists.", Toast.LENGTH_SHORT, true).show();
                                            } else {

                                                firebaseFirestore.collection("band_req_status")
                                                        .whereEqualTo("status", "pending")
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    QuerySnapshot querySnapshot = task.getResult();
                                                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                                                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                                                        Intent getIntent = getIntent();
                                                                        String customer_id = getIntent.getStringExtra("id");
                                                                        HashMap<String, Object> hashMap = new HashMap<>();
                                                                        hashMap.put("email", email);
                                                                        hashMap.put("name", name);
                                                                        hashMap.put("mobile", mobile);
                                                                        hashMap.put("band_type_id", bundle.getString("selectedBandTypeId"));
                                                                        hashMap.put("band_req_status_id", documentSnapshot.getId());
                                                                        hashMap.put("customer_id", customer_id);

                                                                        firebaseFirestore.collection("band_request")
                                                                                .add(hashMap)
                                                                                .addOnSuccessListener(
                                                                                        new OnSuccessListener<DocumentReference>() {
                                                                                            @Override
                                                                                            public void onSuccess(DocumentReference documentReference) {
                                                                                                TuneUpMeSendEmailPartnershipRequestSubmitted(email, name);
//                                                                                                Toasty.success(TuneUpMeCustomerActivityRequestBandPartnership.this, "Request Send Successful !.", Toast.LENGTH_SHORT, true).show();
                                                                                                spinnerSelectBandType.setSelection(0);
                                                                                                editeTextRequestBandName.setText("");
                                                                                                editeTextRequestBandMobile.setText("");
                                                                                                editeTextRequestBandPartnershipEmail.setText("");

//                                                                                                onBackPressed();
                                                                                            }
                                                                                        }
                                                                                )
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        throw new RuntimeException(e);
                                                                                    }
                                                                                });

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
        };

        buttonRequestBandPartnership.setOnClickListener(clickListener);


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

    private void TuneUpMeSendEmailPartnershipRequestSubmitted(String email, String name) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("email", email);
//                jsonObject.addProperty("type", "customer");
                jsonObject.addProperty("name", String.valueOf(name));

                RequestBody body = RequestBody.create(
                        jsonObject.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(ngrock_url + "/TuneUpMe/TuneUpMeSendEmailPartnershipRequestSubmitted")
                        .post(body)
                        .build();


                try {
                    Response response = okHttpClient.newCall(request).execute();

                    if (response.isSuccessful() && response.body() != null) {

                        String resText = response.body().string();
                        JsonObject responseJson = JsonParser.parseString(resText).getAsJsonObject();

                        boolean status = responseJson.get("status").getAsBoolean();

                        if (status) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toasty.success(TuneUpMeCustomerActivityRequestBandPartnership.this, "Request Send Successful !.", Toast.LENGTH_SHORT, true).show();
                                    onBackPressed();
                                }
                            });
                        }
                    } else {
                        Log.e("Error", String.valueOf(response.code()));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();

    }

}