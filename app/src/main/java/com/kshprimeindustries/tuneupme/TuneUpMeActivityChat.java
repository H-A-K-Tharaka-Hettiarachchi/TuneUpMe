package com.kshprimeindustries.tuneupme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class TuneUpMeActivityChat extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tune_up_me_chat);
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
//            getSupportActionBar().setTitle("");
        }

        ImageView profileImage = findViewById(R.id.profileImage);
        TextView customerName = findViewById(R.id.customerName);


        Intent getIntent = getIntent();
        String guest_type = getIntent.getStringExtra("guest_type");
        String user_id = getIntent.getStringExtra("user_id");
        String guest_id = getIntent.getStringExtra("guest_id");


        assert guest_type != null;
        assert guest_id != null;

        if (guest_type.equals("band")) {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            firebaseFirestore.collection("customer")
                    .document(guest_id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    String imagePath = String.valueOf(documentSnapshot.get("profile_image_path"));
                                    int imageResId = getResources().getIdentifier(imagePath, "drawable", getPackageName());

                                    profileImage.setImageResource(imageResId);
                                    customerName.setText(String.valueOf(documentSnapshot.get("name")));
                                    phoneNumber = "tel:" + String.valueOf(documentSnapshot.get("mobile"));
                                }
                            }
                        }
                    });

        } else if (guest_type.equals("customer")) {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            firebaseFirestore.collection("band")
                    .document(guest_id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    String imagePath = String.valueOf(documentSnapshot.get("profile_image_path"));
                                    int imageResId = getResources().getIdentifier(imagePath, "drawable", getPackageName());

                                    profileImage.setImageResource(imageResId);
                                    customerName.setText(String.valueOf(documentSnapshot.get("name")));
                                    phoneNumber = "tel:" + String.valueOf(documentSnapshot.get("mobile"));
                                }
                            }
                        }
                    });

        } else if (guest_type.equals("admin")) {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            firebaseFirestore.collection("admin")
                    .document(guest_id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        }
                    });
        }


        ImageView callImageView = findViewById(R.id.callImageView);
        callImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);

                Uri uri = Uri.parse(phoneNumber);
                intent.setData(uri);
                startActivity(intent);
            }
        });


        RecyclerView recyclerViewTuneUpMeChat = findViewById(R.id.recyclerViewTuneUpMeChat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewTuneUpMeChat.setLayoutManager(linearLayoutManager);


    }
}