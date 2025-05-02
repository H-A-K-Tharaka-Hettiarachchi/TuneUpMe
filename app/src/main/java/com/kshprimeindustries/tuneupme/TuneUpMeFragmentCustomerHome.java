package com.kshprimeindustries.tuneupme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kshprimeindustries.tuneupme.adapters.TuneUpMeCustomerHomeItemAdapter;
import com.kshprimeindustries.tuneupme.model.TuneUpMeCustomerHomeItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class TuneUpMeFragmentCustomerHome extends Fragment {

    ArrayList<TuneUpMeCustomerHomeItem> tuneUpMeCustomerHomeItemArrayList;
    String savedPath;

    Drawable bandImageDrawable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tune_up_me_customer_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("com.kshprimeindustries.tuneupme.data", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", null);


        tuneUpMeCustomerHomeItemArrayList = new ArrayList<>();

        RecyclerView recyclerViewCustomerHome = getView().findViewById(R.id.recyclerViewCustomerHome);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewCustomerHome.setLayoutManager(linearLayoutManager);

        TuneUpMeCustomerHomeItemAdapter tuneUpMeCustomerHomeItemAdapter = new TuneUpMeCustomerHomeItemAdapter(tuneUpMeCustomerHomeItemArrayList);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("band")
                .whereEqualTo("status", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                for (DocumentSnapshot documentSnapshot : querySnapshot) {

                                    String band_id = documentSnapshot.getId();

                                    firebaseFirestore.collection("fav_band")
                                            .whereEqualTo("customer_id", id)
                                            .whereEqualTo("band_id", band_id)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @SuppressLint("NotifyDataSetChanged")
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        QuerySnapshot snapshots = task.getResult();
                                                        removeImage("band", band_id);
                                                        downloadImage("download", "band", band_id, savedPath, new TuneUpMeActivityBandProfile.ImageDownloadCallback() {
                                                            @Override
                                                            public void onSuccess(Uri imageUri) {

                                                                try {
                                                                    bandImageDrawable = Drawable.createFromStream(
                                                                            view.getContext().getContentResolver().openInputStream(imageUri), imageUri.toString());

                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }

                                                            @Override
                                                            public void onFailure(String errorMessage) {
                                                                Log.e("Download", errorMessage);
                                                            }
                                                        });

                                                        if (snapshots != null && !snapshots.isEmpty()) {

                                                            tuneUpMeCustomerHomeItemArrayList.add(new TuneUpMeCustomerHomeItem(
                                                                    documentSnapshot.getId(),
                                                                    bandImageDrawable,
                                                                    savedPath,
                                                                    String.valueOf(documentSnapshot.get("name")),
                                                                    "Rs." + String.valueOf(documentSnapshot.get("price_per_hour")) + " Hour Rate",
                                                                    true
                                                            ));

                                                        } else {
                                                            tuneUpMeCustomerHomeItemArrayList.add(new TuneUpMeCustomerHomeItem(
                                                                    documentSnapshot.getId(),
                                                                    bandImageDrawable,
                                                                    savedPath,
                                                                    String.valueOf(documentSnapshot.get("name")),
                                                                    "Rs." + String.valueOf(documentSnapshot.get("price_per_hour")) + " Hour Rate",
                                                                    false
                                                            ));
                                                        }
                                                        tuneUpMeCustomerHomeItemAdapter.notifyDataSetChanged();

                                                    }
                                                }
                                            });

                                }
                            }
                        }
                    }
                });
        recyclerViewCustomerHome.setAdapter(tuneUpMeCustomerHomeItemAdapter);


    }

    private void removeImage(String guest_type, String id) {
        // 🔥 Get the app's external files directory (Works on Android 10+)
        File appFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "TuneUpMe");
        if (!appFolder.exists()) {
            appFolder.mkdirs(); // Create main app folder if it doesn't exist
        }

        // 🔥 Create subfolder based on guest type
        String guestFolderName = guest_type.equalsIgnoreCase("band") ? "Band Profile Photo" : "Customer Profile Photo";
        File guestFolder = new File(appFolder, guestFolderName);
        if (!guestFolder.exists()) {
            guestFolder.mkdirs(); // Create guest folder if it doesn't exist
        }

        // 🔥 Define the image file location
        File imageFile = new File(guestFolder, "image_" + id + ".png");

        // 🔹 Check if the image exists before attempting to delete it
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