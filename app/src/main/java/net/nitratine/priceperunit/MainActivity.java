package net.nitratine.priceperunit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner UnitTypeSpnr = (Spinner) findViewById(R.id.unitTypeSpnr);
        String[] items = new String[]{"Weight", "Volume", "Length", "Pieces"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, items);
        UnitTypeSpnr.setAdapter(adapter);

        addItem();
        addItem();
        addItem();
        addItem();
    }

    protected void addItem() {
        // https://stackoverflow.com/questions/2395769/how-to-programmatically-add-views-to-views
        // https://stackoverflow.com/questions/2271570/android-findviewbyid-finding-view-by-id-when-view-is-not-on-the-same-layout-in
        View inflatedView = View.inflate(this, R.layout.item_tile, (LinearLayout) findViewById(R.id.mainLayout));
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        LinearLayout recentlyAdded = (LinearLayout) mainLayout.getChildAt(mainLayout.getChildCount() - 1);
        ((TextView) recentlyAdded.findViewById(R.id.nameEditText)).setText("Item 1");
        Log.d("addItem", "Inflated");
    }

    protected void setUnitOptions() {

    }

    protected void itemModified(View view) {

    }

    protected void moveItemUp(View view) {

    }

    protected void moveItemDown(View view) {

    }

    protected void deleteItem(View view) {

    }

    protected void generateResults() {

    }

    protected void clearItems(View view) {
        Log.d("Clear", "Entry");
    }

}
