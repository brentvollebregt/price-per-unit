package net.nitratine.priceperunit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

        unitTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unitTypeChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    protected void addItem(View view) {
        // Inflate and get object
        View inflatedView = View.inflate(this, R.layout.item_tile, itemLayout);
        final LinearLayout recentlyAdded = (LinearLayout) itemLayout.getChildAt(itemLayout.getChildCount() - 1);

        // Set item name
        ((TextView) recentlyAdded.findViewById(R.id.nameEditText)).setText("Item " + itemLayout.getChildCount());

        // Link delete
        ImageButton deleteBtn = (ImageButton) recentlyAdded.findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(v);
            }
        });

        // Link move up
        ImageButton moveUpBtn = (ImageButton) recentlyAdded.findViewById(R.id.moveUpBtn);
        moveUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveItemUp(v);
            }
        });

        // Link move down
        ImageButton moveDownBtn = (ImageButton) recentlyAdded.findViewById(R.id.moveDownBtn);
        moveDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveItemDown(v);
            }
        });

        // Set unit options
        setUnitOptions(recentlyAdded);

        // Setup modification watcher for the three value inputs
        final EditText priceEditText = (EditText) recentlyAdded.findViewById(R.id.priceEditText);
        final EditText quantityEditText = (EditText) recentlyAdded.findViewById(R.id.quantityEditText);
        final EditText sizePerQtyEditText = (EditText) recentlyAdded.findViewById(R.id.sizePerQtyEditText);
        TextWatcher modificationWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                itemModified(recentlyAdded);
            }
        };
        priceEditText.addTextChangedListener(modificationWatcher);
        quantityEditText.addTextChangedListener(modificationWatcher);
        sizePerQtyEditText.addTextChangedListener(modificationWatcher);

        // Setup a watcher for the change of unit
        Spinner unitSpnr = (Spinner) recentlyAdded.findViewById(R.id.unitSpnr);
        unitSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemModified(recentlyAdded);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    protected void setUnitOptions(View view) {
        // Set unit options for specific item (LinearLayout root)
        String unitType = unitTypeSpinner.getSelectedItem().toString();
        Spinner unitSpinner = (Spinner) view.findViewById(R.id.unitSpnr);

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
        // When an item/unit is modified, recalculate
        LinearLayout itemTile = (LinearLayout) view;
        String priceText = ( (EditText) itemTile.findViewById(R.id.priceEditText) ).getText().toString();
        String quantityText = ( (EditText) itemTile.findViewById(R.id.quantityEditText) ).getText().toString();
        String amountPerQtyText = ( (EditText) itemTile.findViewById(R.id.sizePerQtyEditText) ).getText().toString();

        if (priceText.compareTo("") != 0 && quantityText.compareTo("") != 0 && amountPerQtyText.compareTo("") != 0 ) {
            Float price = Float.parseFloat( ( (EditText) itemTile.findViewById(R.id.priceEditText) ).getText().toString() );
            Float quantity = Float.parseFloat( ( (EditText) itemTile.findViewById(R.id.quantityEditText) ).getText().toString() );
            Float amountPerQty = Float.parseFloat( ( (EditText) itemTile.findViewById(R.id.sizePerQtyEditText) ).getText().toString() );
            Float unitPerDollar = (quantity * amountPerQty) / price; // TODO Rounding
            String unit = ((Spinner) itemTile.findViewById(R.id.unitSpnr)).getSelectedItem().toString();
            ((TextView) itemTile.findViewById(R.id.ratiotextView)).setText(unitPerDollar.toString() + unit + "/$");
        } else {
            String unit = ((Spinner) itemTile.findViewById(R.id.unitSpnr)).getSelectedItem().toString();
            ((TextView) itemTile.findViewById(R.id.ratiotextView)).setText(unit + "/$");
        }

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

    protected void unitTypeChanged() {
        // When the main unit type is changed, assign new units to items
        for (int i = 0; i < itemLayout.getChildCount(); i++) {
            setUnitOptions(itemLayout.getChildAt(i));
        }
    }

    protected void generateResults() {
        // Recalculate the results tile
    }

}
