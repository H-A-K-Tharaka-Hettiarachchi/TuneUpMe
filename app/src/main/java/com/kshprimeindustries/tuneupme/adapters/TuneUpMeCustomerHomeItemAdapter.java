package com.kshprimeindustries.tuneupme.adapters;


import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kshprimeindustries.tuneupme.LogAsCustomerActivityEnterEmail;
import com.kshprimeindustries.tuneupme.R;
import com.kshprimeindustries.tuneupme.TuneUpMeActivityBandProfile;
import com.kshprimeindustries.tuneupme.TuneUpMeActivityCustomerBandBookNow;
import com.kshprimeindustries.tuneupme.TuneUpMeApplication;
import com.kshprimeindustries.tuneupme.TuneUpMeCustomerActivitySingleBandView;
import com.kshprimeindustries.tuneupme.model.TuneUpMeCustomerHomeItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TuneUpMeCustomerHomeItemAdapter extends RecyclerView.Adapter<TuneUpMeCustomerHomeItemAdapter.TuneUpMeHomeCustomerItemViewHolder> {
    static String customer_id;
    String savedPath;

    static Context context;

    public static class TuneUpMeHomeCustomerItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageViewCustomerHomeItemProfilePicture;
        public TextView textViewCustomerHomeItemProfileName;
        public TextView textViewCustomerHomeItemPricePerHour;
        public Button buttonCustomerHomeItemProfileViewDetails;
        public Button buttonCustomerHomeItemProfileBookNow;
        public ImageView imageViewCustomerHomeItemProfileAddToFavourite;


        public TuneUpMeHomeCustomerItemViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("com.kshprimeindustries.tuneupme.data", Context.MODE_PRIVATE);
            customer_id = sharedPreferences.getString("id", null);

            imageViewCustomerHomeItemProfilePicture = itemView.findViewById(R.id.imageViewCustomerHomeItemProfilePicture);
            textViewCustomerHomeItemProfileName = itemView.findViewById(R.id.textViewCustomerHomeItemProfileName);
            textViewCustomerHomeItemPricePerHour = itemView.findViewById(R.id.textViewCustomerHomeItemPricePerHour);
            buttonCustomerHomeItemProfileViewDetails = itemView.findViewById(R.id.buttonCustomerHomeItemProfileViewDetails);
            buttonCustomerHomeItemProfileBookNow = itemView.findViewById(R.id.buttonCustomerHomeItemProfileBookNow);
            imageViewCustomerHomeItemProfileAddToFavourite = itemView.findViewById(R.id.imageViewCustomerHomeItemProfileAddToFavourite);
        }
    }

    public ArrayList<TuneUpMeCustomerHomeItem> tuneUpMeCustomerHomeItemArrayList;

    public TuneUpMeCustomerHomeItemAdapter(ArrayList<TuneUpMeCustomerHomeItem> tuneUpMeCustomerHomeItemArrayList) {
        this.tuneUpMeCustomerHomeItemArrayList = tuneUpMeCustomerHomeItemArrayList;
    }

    @NonNull
    @Override
    public TuneUpMeHomeCustomerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.customer_home_item, parent, false);

        TuneUpMeHomeCustomerItemViewHolder tuneUpMeHomeCustomerItemViewHolder = new TuneUpMeHomeCustomerItemViewHolder(view);

        return tuneUpMeHomeCustomerItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TuneUpMeHomeCustomerItemViewHolder holder, @SuppressLint("RecyclerView") int position) {

        downloadImage("download", "band", tuneUpMeCustomerHomeItemArrayList.get(position).getCustomerHomeItemProfileId(), savedPath, new TuneUpMeActivityBandProfile.ImageDownloadCallback() {
            @Override
            public void onSuccess(Uri imageUri) {

                try {
                    Drawable bandImageDrawable = Drawable.createFromStream(
                            context.getContentResolver().openInputStream(imageUri), imageUri.toString());
                    holder.imageViewCustomerHomeItemProfilePicture.setImageDrawable(bandImageDrawable);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("Download", errorMessage);
            }
        });
//        holder.imageViewCustomerHomeItemProfilePicture.setImageDrawable(tuneUpMeCustomerHomeItemArrayList.get(position).getCustomerHomeItemProfilePicture());
        holder.textViewCustomerHomeItemProfileName.setText(tuneUpMeCustomerHomeItemArrayList.get(position).getCustomerHomeItemProfileName());
        holder.textViewCustomerHomeItemPricePerHour.setText(tuneUpMeCustomerHomeItemArrayList.get(position).getCustomerHomeItemPricePerHour());
        if (tuneUpMeCustomerHomeItemArrayList.get(position).isCustomerHomeItemFav()) {
            holder.imageViewCustomerHomeItemProfileAddToFavourite.setImageResource(R.drawable.ic_favourite_fill);
        } else {
            holder.imageViewCustomerHomeItemProfileAddToFavourite.setImageResource(R.drawable.ic_favourite_line);
        }
        holder.buttonCustomerHomeItemProfileViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCardClick(v);
                Intent intent = new Intent(v.getContext(), TuneUpMeCustomerActivitySingleBandView.class);
                intent.putExtra("band_id", String.valueOf(tuneUpMeCustomerHomeItemArrayList.get(position).getCustomerHomeItemProfileId()));
                v.getContext().startActivity(intent);
            }
        });
        holder.buttonCustomerHomeItemProfileBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCardClick(v);
                Intent intent = new Intent(v.getContext(), TuneUpMeActivityCustomerBandBookNow.class);
                intent.putExtra("band_id", String.valueOf(tuneUpMeCustomerHomeItemArrayList.get(position).getCustomerHomeItemProfileId()));
                v.getContext().startActivity(intent);
            }
        });
        holder.imageViewCustomerHomeItemProfileAddToFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCardClick(v);

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                firebaseFirestore.collection("fav_band")
                        .where(
                                Filter.and(
                                        Filter.equalTo("band_id", String.valueOf(tuneUpMeCustomerHomeItemArrayList.get(position).getCustomerHomeItemProfileId())),
                                        Filter.equalTo("customer_id", customer_id)
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
                                                        holder.imageViewCustomerHomeItemProfileAddToFavourite.setImageResource(R.drawable.ic_favourite_line);
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
                                        hashMap.put("band_id", String.valueOf(tuneUpMeCustomerHomeItemArrayList.get(position).getCustomerHomeItemProfileId()));
                                        hashMap.put("customer_id", customer_id);


                                        firebaseFirestore.collection("fav_band")
                                                .add(hashMap)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        holder.imageViewCustomerHomeItemProfileAddToFavourite.setImageResource(R.drawable.ic_favourite_fill);
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
    }

    @Override
    public int getItemCount() {
        return tuneUpMeCustomerHomeItemArrayList.size();
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
