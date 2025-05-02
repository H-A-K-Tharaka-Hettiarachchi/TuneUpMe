package com.kshprimeindustries.tuneupme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.kshprimeindustries.tuneupme.model.SQLiteHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TuneUpMeActivityMusicBandHome extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setSubtitle("Home");

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Intent getIntent = getIntent();
        finish();
        startActivity(getIntent);
    }

    String savedPath;
    String band_id;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    view.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tune_up_me_music_band_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("com.kshprimeindustries.tuneupme.data", Context.MODE_PRIVATE);
        band_id = sharedPreferences.getString("id", null);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayoutBandHome);
        toolbar = findViewById(R.id.toolbarBandHome);
        toolbar.setTitle("TuneUpMe");
        toolbar.setSubtitle("Home");
        toolbar.setSubtitleTextColor(getColor(R.color.white));
        NavigationView navigationView = findViewById(R.id.navigationViewBandHome);

        @SuppressLint("InternalInsetResource") int statusBarHeight = getApplicationContext().getResources().getDimensionPixelSize(
                getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android")
        );

//        int safeWidth = getSafeAreaWidth();
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) navigationView.getLayoutParams();
        layoutParams.topMargin = statusBarHeight;
//        layoutParams.setMarginStart(safeWidth);
        navigationView.setLayoutParams(layoutParams);

        View headerView = navigationView.getHeaderView(0);


        TextView textViewProfileNameNavHeaderBand = headerView.findViewById(R.id.textViewProfileNameNavHeaderBand);
        TextView textViewProfileEmailNavHeaderBand = headerView.findViewById(R.id.textViewProfileEmailNavHeaderBand);
        ImageView navProfileImage = headerView.findViewById(R.id.imageViewProfilePictureNavHeaderBand);
        ImageView imageViewSignOutBandButton = headerView.findViewById(R.id.imageViewSignOutBandButton);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteHelper sqLiteHelper = new SQLiteHelper(TuneUpMeActivityMusicBandHome.this, "TuneUpMe", null, TuneUpMeApplication.sqliteVersion);
                SQLiteDatabase sqLiteDatabase = sqLiteHelper.getReadableDatabase();

                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM `band`", new String[]{});

                while (cursor.moveToNext()) {
                    textViewProfileNameNavHeaderBand.setText(String.valueOf(cursor.getString(0)));
                    textViewProfileEmailNavHeaderBand.setText(cursor.getString(7));
                    savedPath = String.valueOf(cursor.getString(4));

                    downloadImage("download", "band", band_id, savedPath, new TuneUpMeActivityBandProfile.ImageDownloadCallback() {
                        @Override
                        public void onSuccess(Uri imageUri) {
                            runOnUiThread(() -> {
                                navProfileImage.setImageURI(imageUri);
                            });
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Log.e("Download", errorMessage);
                        }
                    });

                }
            }
        }).start();

        imageViewSignOutBandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCardClick(v);

                SharedPreferences sharedPreferences = getSharedPreferences("com.kshprimeindustries.tuneupme.data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("logged", false);
                editor.putString("guestType", null);
                editor.apply();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SQLiteHelper sqLiteHelper = new SQLiteHelper(TuneUpMeActivityMusicBandHome.this, "TuneUpMe", null, TuneUpMeApplication.sqliteVersion);
                        SQLiteDatabase sqLiteDatabase = sqLiteHelper.getReadableDatabase();
                        sqLiteDatabase.delete("band", null, null);
                    }
                }).start();
                Intent intent = new Intent(TuneUpMeActivityMusicBandHome.this, GetStartActivity.class);
                startActivity(intent);
                finish();

            }
        });

        toolbar.setNavigationOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_menu_profile) {
                    Intent intent = new Intent(TuneUpMeActivityMusicBandHome.this, TuneUpMeActivityBandProfile.class);
                    intent.putExtra("id", band_id);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_menu_booking_request) {
                    Intent intent = new Intent(TuneUpMeActivityMusicBandHome.this, TuneUpMeActivityBandBookingRequest.class);
                    intent.putExtra("id", band_id);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_menu_my_schedule) {
                    Intent intent = new Intent(TuneUpMeActivityMusicBandHome.this, TuneUpMeActivityBandMySchedule.class);
                    intent.putExtra("id", band_id);
                    startActivity(intent);
                }


                toolbar.setSubtitle(item.getTitle());
                drawerLayout.closeDrawers();
                return true;
            }
        });

    }

    private void downloadImage(String request_type, String guest_type, String id, String savedPath, TuneUpMeActivityBandProfile.ImageDownloadCallback callback) {
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