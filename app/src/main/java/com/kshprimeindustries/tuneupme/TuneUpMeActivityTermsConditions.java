package com.kshprimeindustries.tuneupme;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TuneUpMeActivityTermsConditions extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, LogAsCustomerActivitySignUp.class); // Change to your previous activity
            startActivity(intent);
            finish(); // Close the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tune_up_me_terms_conditions);
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
            getSupportActionBar().setTitle("TuneUpMe Terms & Conditions");
        }

        WebView webViewTuneUpMeTermsConditions =  findViewById(R.id.webViewTuneUpMeTermsConditions);
        webViewTuneUpMeTermsConditions.getSettings().setJavaScriptEnabled(true);

        // Set WebViewClient to handle links in WebView itself
        webViewTuneUpMeTermsConditions.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // Open the link inside the WebView
                view.loadUrl(request.getUrl().toString());
                return true; // Indicate that the URL was handled
            }
        });

        // Set WebChromeClient for handling page title and other features
        webViewTuneUpMeTermsConditions.setWebChromeClient(new WebChromeClient());

        webViewTuneUpMeTermsConditions.loadUrl("https://h-a-k-tharaka-hettiarachchi.github.io/TuneUpMeTermsConditions/");

    }
}