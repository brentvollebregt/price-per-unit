package net.nitratine.priceperunit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner UnitTypeSpnr = (Spinner) findViewById(R.id.UnitTypeSpnr);
        String[] items = new String[]{"Weight", "Volume", "Length", "Pieces"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, items);
        UnitTypeSpnr.setAdapter(adapter);
    }
}
