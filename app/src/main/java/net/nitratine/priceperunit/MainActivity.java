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

    }

    protected void addItem(View view) {
        View inflatedView = View.inflate(this, R.layout.item_tile, (LinearLayout) findViewById(R.id.itemLayout));
        LinearLayout itemLayout = (LinearLayout) findViewById(R.id.itemLayout);
        LinearLayout recentlyAdded = (LinearLayout) itemLayout.getChildAt(itemLayout.getChildCount() - 1);
        ((TextView) recentlyAdded.findViewById(R.id.nameEditText)).setText("Item 1");
        Log.d("addItem", "Inflated");
    }

    protected void setUnitOptions(View view) {

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
