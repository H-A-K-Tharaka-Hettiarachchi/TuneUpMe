package com.kshprimeindustries.tuneupme;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class TuneUpMeCustomerActivitySingleBandView extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish(); // Close the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static String band_mobile;
    Drawable bandImageDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tune_up_me_customer_single_band_view);
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
        String id = sharedPreferences.getString("id", null);

        Intent getIntent = getIntent();
        String band_id = getIntent.getStringExtra("band_id");


        TextView textViewCustomerSingleBandViewBandName = findViewById(R.id.textViewCustomerSingleBandViewBandName);
        TextView textViewCustomerSingleBandViewPricePerHour = findViewById(R.id.textViewCustomerSingleBandViewPricePerHour);
        ImageView imageViewCustomerSingleBandViewBandImage = findViewById(R.id.imageViewCustomerSingleBandViewBandImage);
        TextView textViewCustomerSingleBandViewDescription = findViewById(R.id.textViewCustomerSingleBandViewDescription);
        ImageView imageViewCustomerSingleBandItemProfileAddToFavourite = findViewById(R.id.imageViewCustomerSingleBandItemProfileAddToFavourite);


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

                                band_mobile = String.valueOf(documentSnapshot.get("mobile"));

                                firebaseFirestore.collection("fav_band")
                                        .whereEqualTo("customer_id", id)
                                        .whereEqualTo("band_id", band_id)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    QuerySnapshot snapshots = task.getResult();

                                                    downloadImage("download", "band", band_id, documentSnapshot.getString("profile_image_path"), new TuneUpMeActivityBandProfile.ImageDownloadCallback() {
                                                        @Override
                                                        public void onSuccess(Uri imageUri) {

                                                            try {
                                                                bandImageDrawable = Drawable.createFromStream(
                                                                        getContentResolver().openInputStream(imageUri), imageUri.toString());

                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                        }

                                                        @Override
                                                        public void onFailure(String errorMessage) {
                                                            Log.e("Download", errorMessage);
                                                        }
                                                    });
                                                    textViewCustomerSingleBandViewBandName.setText(String.valueOf(documentSnapshot.get("name")));
                                                    textViewCustomerSingleBandViewPricePerHour.setText("Rs." + String.valueOf(documentSnapshot.get("price_per_hour")) + " Hour Rate");
                                                    imageViewCustomerSingleBandViewBandImage.setImageDrawable(bandImageDrawable);
                                                    textViewCustomerSingleBandViewDescription.setText(String.valueOf(documentSnapshot.get("description")));

                                                    if (snapshots != null && !snapshots.isEmpty()) {
                                                        imageViewCustomerSingleBandItemProfileAddToFavourite.setImageResource(R.drawable.ic_favourite_fill);
                                                    } else {
                                                        imageViewCustomerSingleBandItemProfileAddToFavourite.setImageResource(R.drawable.ic_favourite_fill);
                                                    }

                                                }
                                            }
                                        });


                            }
                        }
                    }
                });

        imageViewCustomerSingleBandItemProfileAddToFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCardClick(imageViewCustomerSingleBandItemProfileAddToFavourite);

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                firebaseFirestore.collection("fav_band")
                        .where(
                                Filter.and(
                                        Filter.equalTo("band_id", band_id),
                                        Filter.equalTo("customer_id", id)
                                )
                        )
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                        firebaseFirestore.collection("fav_band")
                                                .document(documentSnapshot.getId())
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        imageViewCustomerSingleBandItemProfileAddToFavourite.setImageResource(R.drawable.ic_favourite_line);
                                                        Toasty.success(v.getContext(), "Removed From Favourites !", Toast.LENGTH_LONG, true).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toasty.error(v.getContext(), "Oops Something Went Wrong!", Toast.LENGTH_LONG, true).show();
                                                    }
                                                });
                                    } else {
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("band_id", band_id);
                                        hashMap.put("customer_id", id);


                                        firebaseFirestore.collection("fav_band")
                                                .add(hashMap)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        imageViewCustomerSingleBandItemProfileAddToFavourite.setImageResource(R.drawable.ic_favourite_fill);
                                                        Toasty.success(v.getContext(), "Added To Favourites !", Toast.LENGTH_LONG, true).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toasty.error(v.getContext(), "Oops Something Went Wrong!", Toast.LENGTH_LONG, true).show();
                                                    }
                                                });
                                    }
                                }
                            }
                        });

            }
        });


        CardView cardViewCustomerSingleBandViewCallNow = findViewById(R.id.cardViewCustomerSingleBandViewCallNow);
        CardView cardViewCustomerSingleBandViewChatNow = findViewById(R.id.cardViewCustomerSingleBandViewChatNow);
        ImageView imageViewCustomerSingleBandViewCallNow = findViewById(R.id.imageViewCustomerSingleBandViewCallNow);
        ImageView imageViewCustomerSingleBandViewChatNow = findViewById(R.id.imageViewCustomerSingleBandViewChatNow);
        TextView textViewCustomerSingleBandViewCallNow = findViewById(R.id.textViewCustomerSingleBandViewCallNow);
        TextView textViewCustomerSingleBandViewChatNow = findViewById(R.id.textViewCustomerSingleBandViewChatNow);

        View.OnClickListener clickListener = v -> {

            if (v.getId() == R.id.cardViewCustomerSingleBandViewCallNow) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String mobile = "tel:" + String.valueOf(band_mobile);
                Uri uri = Uri.parse(mobile);
                intent.setData(uri);
                startActivity(intent);
                animateCardClick(cardViewCustomerSingleBandViewCallNow);

            } else if (v.getId() == R.id.cardViewCustomerSingleBandViewChatNow) {
                Intent intent = new Intent(TuneUpMeCustomerActivitySingleBandView.this, TuneUpMeActivityChat.class);
                intent.putExtra("user_id", id);
                intent.putExtra("guest_id", band_id);
                intent.putExtra("guest_type", "band");
                startActivity(intent);
                animateCardClick(cardViewCustomerSingleBandViewChatNow);

            } else if (v.getId() == R.id.imageViewCustomerSingleBandViewCallNow) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String mobile = "tel:" + String.valueOf(band_mobile);
                Uri uri = Uri.parse(mobile);
                intent.setData(uri);
                startActivity(intent);
                animateCardClick(cardViewCustomerSingleBandViewCallNow);

            } else if (v.getId() == R.id.imageViewCustomerSingleBandViewChatNow) {
                Intent intent = new Intent(TuneUpMeCustomerActivitySingleBandView.this, TuneUpMeActivityChat.class);
                intent.putExtra("user_id", id);
                intent.putExtra("guest_id", band_id);
                intent.putExtra("guest_type", "band");
                startActivity(intent);
                animateCardClick(cardViewCustomerSingleBandViewChatNow);

            } else if (v.getId() == R.id.textViewCustomerSingleBandViewCallNow) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String mobile = "tel:" + String.valueOf(band_mobile);
                Uri uri = Uri.parse(mobile);
                intent.setData(uri);
                startActivity(intent);
                animateCardClick(cardViewCustomerSingleBandViewCallNow);

            } else if (v.getId() == R.id.textViewCustomerSingleBandViewChatNow) {
                Intent intent = new Intent(TuneUpMeCustomerActivitySingleBandView.this, TuneUpMeActivityChat.class);
                intent.putExtra("user_id", id);
                intent.putExtra("guest_id", band_id);
                intent.putExtra("guest_type", "band");
                startActivity(intent);
                animateCardClick(cardViewCustomerSingleBandViewChatNow);
            }
        };


        cardViewCustomerSingleBandViewCallNow.setOnClickListener(clickListener);
        cardViewCustomerSingleBandViewChatNow.setOnClickListener(clickListener);
        imageViewCustomerSingleBandViewCallNow.setOnClickListener(clickListener);
        imageViewCustomerSingleBandViewChatNow.setOnClickListener(clickListener);
        textViewCustomerSingleBandViewCallNow.setOnClickListener(clickListener);
        textViewCustomerSingleBandViewChatNow.setOnClickListener(clickListener);

        Button buttonCustomerSingleBandItemProfileBookNow = findViewById(R.id.buttonCustomerSingleBandItemProfileBookNow);
        buttonCustomerSingleBandItemProfileBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TuneUpMeCustomerActivitySingleBandView.this, TuneUpMeActivityCustomerBandBookNow.class);
                intent.putExtra("band_id", band_id);
                startActivity(intent);
            }
        });

    }



    private void downloadImage(String request_type, String guest_type, String id, String savedPath, TuneUpMeActivityBandProfile.ImageDownloadCallback callback) {


        OkHttpClient client = new OkHttpClient();

        //  Get the app's external files directory (Works on Android 10+)
        File appFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "TuneUpMe");
        if (!appFolder.exists()) {
            appFolder.mkdirs(); // Create main app folder if it doesn't exist
        }

        //  Create subfolder based on guest type
        String guestFolderName = guest_type.equalsIgnoreCase("band") ? "Band Profile Photo" : "Customer Profile Photo";
        File guestFolder = new File(appFolder, guestFolderName);
        if (!guestFolder.exists()) {
            guestFolder.mkdirs(); // Create guest folder if it doesn't exist
        }

        //  Define image file location
        File imageFile = new File(guestFolder, "image_" + id + ".png");

        //  Check if the image already exists locally
        if (imageFile.exists()) {
            Log.d("Download", "Image already exists. Loading from local storage.");
            callback.onSuccess(Uri.fromFile(imageFile));
            return; // Stop here and return local image
        }

        //  Prepare the request body for POST
        FormBody.Builder formBodyBuilder = new FormBody.Builder()
                .add("request_type", request_type);

        if (savedPath != null && !savedPath.isEmpty()) {
            formBodyBuilder.add("saved_path", savedPath);
        } else {
            formBodyBuilder.add("guest_type", guest_type);
            formBodyBuilder.add("id", id);
        }

        RequestBody requestBody = formBodyBuilder.build();

        //  Create the request
        Request request = new Request.Builder()
                .url(TuneUpMeApplication.ngrock_url + "/TuneUpMe/TuneUpMeFTP")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Download", "Download failed: " + e.getMessage());
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    InputStream inputStream = response.body().byteStream();

                    // 🔹 Save image to storage
                    boolean isSaved = saveImageToStorage(inputStream, imageFile);

                    if (isSaved) {
                        callback.onSuccess(Uri.fromFile(imageFile));
                    } else {
                        callback.onFailure("Failed to save image.");
                    }
                } else {
                    callback.onFailure("Server error: " + response.code());
                }
            }
        });
    }


    private boolean saveImageToStorage(InputStream inputStream, File imageFile) {
        try {
            //  Ensure parent folders exist before writing the file
            File parentDir = imageFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
                return true;
            }
        } catch (IOException e) {
            Log.e("SaveImage", "Error saving image: " + e.getMessage());
            return false;
        }
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