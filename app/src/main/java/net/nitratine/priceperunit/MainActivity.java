package net.nitratine.priceperunit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
        ((TextView) recentlyAdded.findViewById(R.id.nameEditText)).setText("Item " + itemLayout.getChildCount());
        ImageButton deleteBtn = (ImageButton) recentlyAdded.findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(v);
            }
        });
        ImageButton moveUpBtn = (ImageButton) recentlyAdded.findViewById(R.id.moveUpBtn);
        moveUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveItemUp(v);
            }
        });
        ImageButton moveDownBtn = (ImageButton) recentlyAdded.findViewById(R.id.moveDownBtn);
        moveDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveItemDown(v);
            }
        });

    }

    protected void setUnitOptions(View view) {

    }

    protected void itemModified(View view) {

    }

    protected void moveItemUp(View view) {
        LinearLayout itemLayout = (LinearLayout) findViewById(R.id.itemLayout);
        LinearLayout itemBeingMoved = (LinearLayout) view.getParent().getParent().getParent();
        int index = itemLayout.indexOfChild(itemBeingMoved);
        itemLayout.removeView(itemBeingMoved);
        itemLayout.addView(itemBeingMoved, index - 1);
    }

    protected void moveItemDown(View view) {
        LinearLayout itemLayout = (LinearLayout) findViewById(R.id.itemLayout);
        LinearLayout itemBeingMoved = (LinearLayout) view.getParent().getParent().getParent();
        int index = itemLayout.indexOfChild(itemBeingMoved);
        itemLayout.removeView(itemBeingMoved);
        itemLayout.addView(itemBeingMoved, index + 1);
    }

    protected void deleteItem(View view) {
        LinearLayout itemLayout = (LinearLayout) findViewById(R.id.itemLayout);
        itemLayout.removeView((LinearLayout) view.getParent().getParent().getParent());
    }

    protected void generateResults() {

    }

    protected void clearItems(View view) {
        if  (((LinearLayout) findViewById(R.id.itemLayout)).getChildCount() > 0) {
            ((LinearLayout) findViewById(R.id.itemLayout)).removeAllViews();
        }
    }

}
