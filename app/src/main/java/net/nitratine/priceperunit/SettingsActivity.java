package net.nitratine.priceperunit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings & About");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO Write all to Settings
        Settings.pushSettings();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
