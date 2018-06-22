package net.nitratine.priceperunit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    LinearLayout itemLayout;
    Spinner unitTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemLayout = (LinearLayout) findViewById(R.id.itemLayout);
        unitTypeSpinner = (Spinner) findViewById(R.id.unitTypeSpnr);

        String[] items = new String[] {"Weight", "Volume", "Length", "Pieces"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, items);
        unitTypeSpinner.setAdapter(adapter);

    }

    protected void addItem(View view) {
        View inflatedView = View.inflate(this, R.layout.item_tile, itemLayout);
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
        setUnitOptions(recentlyAdded);

    }

    protected void setUnitOptions(View view) {
        // Set unit options for specific item (LinearLayout root)
        String unitType = unitTypeSpinner.getSelectedItem().toString();
        Spinner unitSpinner = (Spinner) view.findViewById(R.id.unitSpnr);

        // TODO If we already have the correct elements then don't change
        String[] items;
        if (unitType.compareTo("Weight") == 0) {
            items = new String[] {"g", "kg", "tonne"};
        } else if (unitType.compareTo("Volume") == 0) {
            items = new String[] {"ml", "l",};
        } else if(unitType.compareTo("Length") == 0) {
            items = new String[] {"mm", "cm", "m", "km"};
        } else {
            items = new String[] {"pcs"};
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        unitSpinner.setAdapter(adapter);
    }

    protected void itemModified(View view) {
        // When an item is modified, recalculate
        // TODO recalculate specific unit/$
        generateResults();
    }

    protected void moveItemUp(View view) {
        // Moves an item up. View passed in is the move up button.
        LinearLayout itemBeingMoved = (LinearLayout) view.getParent().getParent().getParent();
        int index = itemLayout.indexOfChild(itemBeingMoved);
        itemLayout.removeView(itemBeingMoved);
        itemLayout.addView(itemBeingMoved, index - 1);
    }

    protected void moveItemDown(View view) {
        // Moves an item down. View passed in is the move down button.
        LinearLayout itemBeingMoved = (LinearLayout) view.getParent().getParent().getParent();
        int index = itemLayout.indexOfChild(itemBeingMoved);
        itemLayout.removeView(itemBeingMoved);
        itemLayout.addView(itemBeingMoved, index + 1);
    }

    protected void deleteItem(View view) {
        // Removes an item. View passed in is the delete button.
        itemLayout.removeView((LinearLayout) view.getParent().getParent().getParent());
    }

    protected void clearItems(View view) {
        // Clear all items in the itemLayout
        if  (itemLayout.getChildCount() > 0) {
            itemLayout.removeAllViews();
        }
    }

    protected void unitTypeChanged(View view) {
        // When the main unit type is changed, assign new units to items
    }

    protected void generateResults() {
        // Recalculate the results tile
    }

}
