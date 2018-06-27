package net.nitratine.priceperunit;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings & About");

        // TODO Setup watchers (that save and export data)
    }

    @Override
    protected void onPause() {
        super.onPause();
        Settings.pushSettings();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    protected void rate(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.play_store_location)));
        startActivity(browserIntent);
    }

    protected void bugReport(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",getString(R.string.contact_email), null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Bug Report For Price Per Unit");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Describe the bug you found and how to reproduce it.");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    protected void developerSite(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.developer_site_url)));
        startActivity(browserIntent);
    }

    protected void licenses(View view) {
        WebView webView = (WebView) LayoutInflater.from(this).inflate(R.layout.dialog_licenses, null);
        webView.loadData(getString(R.string.licenses_html), "text/html", null);
        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        mAlertDialog.setTitle("Licenses");
        mAlertDialog.setView(webView);
        mAlertDialog.setPositiveButton(android.R.string.ok, null);
        mAlertDialog.show();
    }
}
