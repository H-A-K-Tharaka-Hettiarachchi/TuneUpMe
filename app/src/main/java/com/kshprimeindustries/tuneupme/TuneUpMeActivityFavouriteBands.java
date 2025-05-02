package com.kshprimeindustries.tuneupme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kshprimeindustries.tuneupme.adapters.TuneUpMeFavouriteBandAdapter;
import com.kshprimeindustries.tuneupme.model.TuneUpMeFavouriteBandItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TuneUpMeActivityFavouriteBands extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish(); // Close the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    Drawable bandImageDrawable;

    @Override
    protected void onResume() {
        super.onResume();

        RecyclerView recyclerViewTuneUpMeFavouriteBands = findViewById(R.id.recyclerViewTuneUpMeFavouriteBands);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                TuneUpMeFavouriteBandAdapter.TuneUpMeFavouriteBandViewHolder tuneUpMeFavouriteBandViewHolder = (TuneUpMeFavouriteBandAdapter.TuneUpMeFavouriteBandViewHolder) viewHolder;

                TuneUpMeFavouriteBandAdapter tuneUpMeFavouriteBandAdapter = (TuneUpMeFavouriteBandAdapter) recyclerViewTuneUpMeFavouriteBands.getAdapter();
                tuneUpMeFavouriteBandAdapter.removeItemOnSwiped(viewHolder.getAdapterPosition());

            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewTuneUpMeFavouriteBands);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tune_up_me_favourite_bands);
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
            getSupportActionBar().setTitle("TuneUpMe - Favourites");
        }

        SharedPreferences sharedPreferences = getSharedPreferences("com.kshprimeindustries.tuneupme.data", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", null);

        ArrayList<TuneUpMeFavouriteBandItem> tuneUpMeFavouriteBandItemArrayList = new ArrayList<>();

        RecyclerView recyclerViewTuneUpMeFavouriteBands = findViewById(R.id.recyclerViewTuneUpMeFavouriteBands);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TuneUpMeActivityFavouriteBands.this);
        recyclerViewTuneUpMeFavouriteBands.setLayoutManager(linearLayoutManager);

        TuneUpMeFavouriteBandAdapter tuneUpMeFavouriteBandAdapter = new TuneUpMeFavouriteBandAdapter(tuneUpMeFavouriteBandItemArrayList);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("fav_band")
                .whereEqualTo("customer_id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {

                                List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();

                                for (DocumentSnapshot documentSnapshot : documentSnapshotList) {

                                    firebaseFirestore.collection("band")
                                            .document(String.valueOf(documentSnapshot.get("band_id")))
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @SuppressLint("NotifyDataSetChanged")
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            removeImage("band", documentSnapshot.getString("band_id"));
                                                            downloadImage("download", "band", documentSnapshot.getString("band_id"), document.getString("profile_image_path"), new TuneUpMeActivityBandProfile.ImageDownloadCallback() {
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

                                                            tuneUpMeFavouriteBandItemArrayList.add(new TuneUpMeFavouriteBandItem(
                                                                    document.getId(),
                                                                    bandImageDrawable,
                                                                    document.getString("profile_image_path"),
                                                                    String.valueOf(document.get("name")),
                                                                    true
                                                            ));
                                                            tuneUpMeFavouriteBandAdapter.notifyDataSetChanged();

                                                            if (tuneUpMeFavouriteBandItemArrayList.size() != 0) {
                                                                ImageView imageViewEmpty = findViewById(R.id.imageViewFavouriteBandEmpty);
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

        recyclerViewTuneUpMeFavouriteBands.setAdapter(tuneUpMeFavouriteBandAdapter);


    }

    private void removeImage(String guest_type, String id) {
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

        //  Define the image file location
        File imageFile = new File(guestFolder, "image_" + id + ".png");

        //  Check if the image exists before attempting to delete it
        if (imageFile.exists()) {
            boolean isDeleted = imageFile.delete(); // Deletes the image file

            if (isDeleted) {
                Log.d("Remove Image", "Image successfully removed.");
            } else {
                Log.e("Remove Image", "Failed to remove image.");
            }
        } else {
            Log.d("Remove Image", "Image not found, nothing to remove.");
        }
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

}