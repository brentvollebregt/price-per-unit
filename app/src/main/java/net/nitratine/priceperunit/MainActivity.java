package net.nitratine.priceperunit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

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

        // TODO Check if we have any previous data
        // TODO Set the global unit type before inserting (based off unit of first item)
        // TODO Put it in
        Log.d("onCreate", "Put data in");

        ItemStorage is = new ItemStorage(this);
        ArrayList<ItemStructure> data = is.getData();
        Log.d("onCreate", "Amount: " + data.size());
    }

    @Override
    protected void onStop() {
        super.onStop();

        ArrayList<ItemStructure> data = new ArrayList<ItemStructure>();
        for (int i = 0; i < itemLayout.getChildCount(); i++) {
            ItemStructure item = new ItemStructure();
            LinearLayout itemTile = (LinearLayout) itemLayout.getChildAt(i);
            item.name = ( (EditText) itemTile.findViewById(R.id.nameEditText) ).getText().toString();
            item.price = ( (EditText) itemTile.findViewById(R.id.priceEditText) ).getText().toString().replace("$", "");
            item.quantity = ( (EditText) itemTile.findViewById(R.id.quantityEditText) ).getText().toString();
            item.size = ( (EditText) itemTile.findViewById(R.id.sizePerQtyEditText) ).getText().toString();
            item.unit = ((Spinner) itemTile.findViewById(R.id.unitSpnr)).getSelectedItem().toString();
            data.add(item);
        }

        ItemStorage is = new ItemStorage(this);
        is.insertData(data);

    }

    protected void addItem(View view) {
        addItemManual("", "", "", "", "");
    }

    protected void addItemManual(String name, String price, String quantity, String size, String unit) {
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
        TextWatcher modificationWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = priceEditText.getText().toString();
                if (!value.startsWith("$")) {
                    priceEditText.setText("$" + value);
                    priceEditText.setSelection(value.length() + 1);
                }
                itemModified(recentlyAdded);

            }
        };
        priceEditText.addTextChangedListener(modificationWatcher);

        EditText quantityEditText = (EditText) recentlyAdded.findViewById(R.id.quantityEditText);
        modificationWatcher = new TextWatcher() {
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
        quantityEditText.addTextChangedListener(modificationWatcher);

        EditText sizePerQtyEditText = (EditText) recentlyAdded.findViewById(R.id.sizePerQtyEditText);
        modificationWatcher = new TextWatcher() {
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
        String priceText = ( (EditText) itemTile.findViewById(R.id.priceEditText) ).getText().toString().replace("$", "");
        String quantityText = ( (EditText) itemTile.findViewById(R.id.quantityEditText) ).getText().toString();
        String amountPerQtyText = ( (EditText) itemTile.findViewById(R.id.sizePerQtyEditText) ).getText().toString();

        if (priceText.compareTo("") != 0 && quantityText.compareTo("") != 0 && amountPerQtyText.compareTo("") != 0 ) {
            Float price = Float.parseFloat(priceText);
            Float quantity = Float.parseFloat(quantityText);
            Float amountPerQty = Float.parseFloat(amountPerQtyText);
            Float unitPerDollar = (quantity * amountPerQty) / price;
            String unit = ((Spinner) itemTile.findViewById(R.id.unitSpnr)).getSelectedItem().toString();
            ((TextView) itemTile.findViewById(R.id.ratiotextView)).setText(roundToString(unitPerDollar) + unit + "/$");

            generateResults();
        } else {
            String unit = ((Spinner) itemTile.findViewById(R.id.unitSpnr)).getSelectedItem().toString();
            ((TextView) itemTile.findViewById(R.id.ratiotextView)).setText(unit + "/$");
        }
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

    private String roundToString(Float value) {
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        Double d = value.doubleValue();
        return df.format(d);
    }

    protected void openSettings(View view) {
        // TODO Settings activity
    }

}
