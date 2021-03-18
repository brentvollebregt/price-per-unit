package net.nitratine.priceperunit;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.settings_and_about));

        final EditText currencyValueTextEdit = (EditText) findViewById(R.id.currencyValueTextEdit);
        final Spinner roundingValueSpinner = (Spinner) findViewById(R.id.roundingValueSpinner);
        final Switch showResultsValueSwitch = (Switch) findViewById(R.id.showResultsValueSwitch);
        final Switch rememberDataValueSwitch = (Switch) findViewById(R.id.rememberDataValueSwitch);
        final TextView versionTag = findViewById(R.id.versionTag);

        String[] items = new String[] {"1", "2", "3", "4", "5", "6"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, items);
        roundingValueSpinner.setAdapter(adapter);

        currencyValueTextEdit.setText(Settings.currencySymbol);
        roundingValueSpinner.setSelection(adapter.getPosition(String.valueOf(Settings.rounding)));
        showResultsValueSwitch.setChecked(Settings.showResultsTile);
        rememberDataValueSwitch.setChecked(Settings.rememberData);
        versionTag.setText(getString(R.string.version, BuildConfig.VERSION_NAME));

        currencyValueTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Settings.currencySymbol = currencyValueTextEdit.getText().toString();
                Settings.pushSettings();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        roundingValueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Settings.rounding = Integer.parseInt(roundingValueSpinner.getSelectedItem().toString());
                Settings.pushSettings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        showResultsValueSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.showResultsTile = showResultsValueSwitch.isChecked();
                Settings.pushSettings();
            }
        });

        rememberDataValueSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.rememberData = rememberDataValueSwitch.isChecked();
                Settings.pushSettings();
            }
        });

        findViewById(R.id.rateLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rate(view);
            }
        });

        findViewById(R.id.bugReportLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bugReport(view);
            }
        });

        findViewById(R.id.developerSiteLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                developerSite(view);
            }
        });

        findViewById(R.id.licensesLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                licenses(view);
            }
        });
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
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.bug_reporting_url)));
        startActivity(browserIntent);
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
