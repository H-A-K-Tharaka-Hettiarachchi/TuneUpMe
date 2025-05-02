package com.kshprimeindustries.tuneupme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kshprimeindustries.tuneupme.model.SQLiteHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TuneUpMeActivityCustomerProfile extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish(); // Close the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    Uri selectImage;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tune_up_me_customer_profile);
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

        EditText fullNameEditTextCustomerProfile = findViewById(R.id.fullNameEditTextCustomerProfile);
        EditText emailEditTextCustomerProfile = findViewById(R.id.emailEditTextCustomerProfile);
        EditText mobileEditTextCustomerProfile = findViewById(R.id.mobileEditTextCustomerProfile);
        EditText passwordEditTextCustomerProfile = findViewById(R.id.passwordEditTextCustomerProfile);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteHelper sqLiteHelper = new SQLiteHelper(TuneUpMeActivityCustomerProfile.this, "TuneUpMe", null, TuneUpMeApplication.sqliteVersion);
                SQLiteDatabase sqLiteDatabase = sqLiteHelper.getReadableDatabase();

                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM `customer`", new String[]{});

                while (cursor.moveToNext()) {
                    emailEditTextCustomerProfile.setText(cursor.getString(0));
                    mobileEditTextCustomerProfile.setText(cursor.getString(1));
                    fullNameEditTextCustomerProfile.setText(String.valueOf(cursor.getString(2)));
                    passwordEditTextCustomerProfile.setText(cursor.getString(3));
                }
            }
        }).start();

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {

                        ImageView imageView = findViewById(R.id.imageViewCustomerProfileImage);

                        if (imageView != null) {
                            imageView.setImageURI(uri);
                            selectImage = uri;
                            uploadImage(uri, "upload", "customer", Objects.requireNonNull(getIntent().getStringExtra("id")));
                        } else {
                            Log.e("PhotoPicker", "ImageView is null");
                        }

                        Log.d("PhotoPicker", "Selected URI: " + uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });


        CardView cardViewCustomerProfileImageMain = findViewById(R.id.cardViewCustomerProfileImageMain);
        @SuppressLint("CutPasteId") ImageView imageViewCustomerProfileImage = findViewById(R.id.imageViewCustomerProfileImage);


        View.OnClickListener clickListener = v -> {
            animateCardClick(cardViewCustomerProfileImageMain);
            if (v.getId() == R.id.cardViewCustomerProfileImageMain) {
                requestPermissions();
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            } else if (v.getId() == R.id.imageViewCustomerProfileImage) {
                requestPermissions();
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        };


        cardViewCustomerProfileImageMain.setOnClickListener(clickListener);
        imageViewCustomerProfileImage.setOnClickListener(clickListener);


        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("customer")
                .document(Objects.requireNonNull(getIntent().getStringExtra("id")))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                fullNameEditTextCustomerProfile.setText(documentSnapshot.getString("name"));
                                emailEditTextCustomerProfile.setText(documentSnapshot.getString("email"));
                                mobileEditTextCustomerProfile.setText(documentSnapshot.getString("mobile"));
                                passwordEditTextCustomerProfile.setText(documentSnapshot.getString("password"));
                                email = documentSnapshot.getString("email");
                                downloadImage("download", "customer", getIntent().getStringExtra("id"), documentSnapshot.getString("profile_image_path"), new TuneUpMeActivityCustomerProfile.ImageDownloadCallback() {
                                    @Override
                                    public void onSuccess(Uri imageUri) {
                                        runOnUiThread(() -> {
                                            ImageView imageView = findViewById(R.id.imageViewCustomerProfileImage);
                                            imageView.setImageURI(imageUri);
                                        });
                                    }

                                    @Override
                                    public void onFailure(String errorMessage) {
                                        Log.e("Download", errorMessage);
                                    }
                                });

                            }
                        }
                    }
                });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            Map<String, Integer> perms = new HashMap<>();
            perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_DENIED);
            perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_DENIED);

            for (int i = 0; i < permissions.length; i++) {
                perms.put(permissions[i], grantResults[i]);
            }

            if (perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q || perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                Log.d("Permissions", "All requested permissions granted!");
            } else {
                Log.e("Permissions", "Some permissions were denied.");
                Toast.makeText(this, "Permissions required for full functionality", Toast.LENGTH_LONG).show();
            }
        }
    }
    private static final int REQUEST_PERMISSIONS_CODE = 101;

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionsNeeded = new ArrayList<>();

            // Check Location Permission
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            }

            // Check Storage Permission
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) { // For Android 10 and below
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }

            // Request permissions if needed
            if (!permissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        permissionsNeeded.toArray(new String[0]),
                        REQUEST_PERMISSIONS_CODE);
            } else {
                Log.d("Permissions", "All permissions are already granted.");
            }
        }
    }
    private void uploadImage(Uri imageUri, String request_type, String guest_type, String id) {
        if (imageUri == null) {
            Log.e("Upload", "No image selected");
            return;
        }

        File file = getFileFromUri(imageUri); // Convert URI to File
        if (file == null) {
            Log.e("Upload", "Failed to get file from Uri");
            return;
        }

        OkHttpClient client = new OkHttpClient();

        // Prepare the request body
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("request_type", request_type)
                .addFormDataPart("guest_type", guest_type)
                .addFormDataPart("id", id)
                .addFormDataPart("image", file.getName(), // Add image file
                        RequestBody.create(file, MediaType.parse("image/*")))
                .build();

        // Prepare the request
        Request request = new Request.Builder()
                .url(TuneUpMeApplication.ngrock_url + "/TuneUpMe/TuneUpMeFTP")
                .post(requestBody)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Upload", "Upload failed: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String responseBody = response.body().string();

                    // Parse the response JSON
                    try {
                        JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                        boolean status = jsonResponse.get("status").getAsBoolean();
                        String message = jsonResponse.get("message").getAsString();

                        if (status) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String savedPath = jsonResponse.get("saved_path").getAsString();
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("profile_image_path", savedPath);
                                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                    firebaseFirestore.collection("customer")
                                            .document(Objects.requireNonNull(getIntent().getStringExtra("id")))
                                            .update(hashMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    SQLiteHelper sqLiteHelper = new SQLiteHelper(TuneUpMeActivityCustomerProfile.this, "TuneUpMe", null, 2);
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
                                                            ContentValues contentValues = new ContentValues();
                                                            contentValues.put("profile_image_path", savedPath);

                                                            String whereClause = "email = ?";
                                                            String[] whereArgs = {email};

                                                            sqLiteDatabase.update("customer", contentValues, whereClause, whereArgs);
                                                            removeImage("customer", Objects.requireNonNull(getIntent().getStringExtra("id")));
                                                            downloadImage("download", "customer", getIntent().getStringExtra("id"), savedPath, new TuneUpMeActivityCustomerProfile.ImageDownloadCallback() {
                                                                @Override
                                                                public void onSuccess(Uri imageUri) {
                                                                    runOnUiThread(() -> {
                                                                        ImageView imageView = findViewById(R.id.imageViewCustomerProfileImage);
                                                                        imageView.setImageURI(imageUri);
                                                                    });
                                                                }

                                                                @Override
                                                                public void onFailure(String errorMessage) {
                                                                    Log.e("Download", errorMessage);
                                                                }
                                                            });
                                                        }
                                                    }).start();
                                                    Toasty.success(TuneUpMeActivityCustomerProfile.this, message, Toast.LENGTH_LONG, true).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toasty.error(TuneUpMeActivityCustomerProfile.this, "Oops Something went wrong !", Toast.LENGTH_LONG, true).show();
                                                }
                                            });

                                }
                            });
                            Log.d("Upload", "Image uploaded successfully: " + message);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toasty.error(TuneUpMeActivityCustomerProfile.this, message, Toast.LENGTH_LONG, true).show();
                                }
                            });
                            Log.e("Upload", "Upload failed: " + message);
                        }

                    } catch (Exception e) {
                        Log.e("Upload", "Failed to parse JSON response: " + e.getMessage());
                    }

                } else {
                    Log.e("Upload", "Upload failed: " + response.code());
                }
            }
        });
    }


    private File getFileFromUri(Uri uri) {
        File file = null;
        try {
            ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
            if (parcelFileDescriptor != null) {
                FileInputStream inputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
                File tempFile = new File(getCacheDir(), "upload_image.jpg"); // Temp file
                FileOutputStream outputStream = new FileOutputStream(tempFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                inputStream.close();
                outputStream.close();
                file = tempFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private void downloadImage(String request_type, String guest_type, String id, String savedPath, ImageDownloadCallback callback) {
        OkHttpClient client = new OkHttpClient();

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

        // 🔥 Define image file location
        File imageFile = new File(guestFolder, "image_" + id + ".png");

        // 🔹 Check if the image already exists locally
        if (imageFile.exists()) {
            Log.d("Download", "Image already exists. Loading from local storage.");
            callback.onSuccess(Uri.fromFile(imageFile));
            return; // Stop here and return local image
        }

        // 🔥 Prepare the request body for POST
        FormBody.Builder formBodyBuilder = new FormBody.Builder()
                .add("request_type", request_type);

        if (savedPath != null && !savedPath.isEmpty()) {
            formBodyBuilder.add("saved_path", savedPath);
        } else {
            formBodyBuilder.add("guest_type", guest_type);
            formBodyBuilder.add("id", id);
        }

        RequestBody requestBody = formBodyBuilder.build();

        // 🔹 Create the request
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

    /**
     * 🔥 Method to save InputStream as an image file
     */
    private boolean saveImageToStorage(InputStream inputStream, File imageFile) {
        try {
            // 🔥 Ensure parent folders exist before writing the file
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


    public interface ImageDownloadCallback {
        void onSuccess(Uri imageUri);

        void onFailure(String errorMessage);
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