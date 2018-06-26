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
    Units unitWorker = new Units();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemLayout = (LinearLayout) findViewById(R.id.itemLayout);
        unitTypeSpinner = (Spinner) findViewById(R.id.unitTypeSpnr);

        String[] items = unitWorker.unitTypes.toArray(new String[0]);
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

        ItemStorage is = new ItemStorage(this);
        ArrayList<ItemStructure> data = is.getData();

        if (data.size() > 0) {
            String unit = data.get(0).unit;
            for (int i = 0 ; i < unitWorker.units.size(); i++) {
                if (unitWorker.units.get(i).contains(unit)) {
                    unitTypeSpinner.setSelection(getSpinnerIndex(unitTypeSpinner, unitWorker.unitTypes.get(i)));
                }
            }

            for (ItemStructure item : data) {
                addItemManual(item.name, item.price, item.quantity, item.size, item.unit);
            }
        }

        // Log.d("Units", "" + unitWorker.convert("m", "km", 555.0));
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
        final EditText nameEditText = (EditText) recentlyAdded.findViewById(R.id.nameEditText);
        final Spinner unitSpinner = (Spinner) recentlyAdded.findViewById(R.id.unitSpnr);
        final EditText priceEditText = (EditText) recentlyAdded.findViewById(R.id.priceEditText);
        final EditText quantityEditText = (EditText) recentlyAdded.findViewById(R.id.quantityEditText);
        final EditText sizePerQtyEditText = (EditText) recentlyAdded.findViewById(R.id.sizePerQtyEditText);

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
        unitSpinner.setSelection(getSpinnerIndex(unitSpinner, unit));

        // Setup modification watcher for the three value inputs
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
                if (value.compareTo("") == 0) {
                    return;
                }
                if (!value.startsWith("$")) {
                    priceEditText.setText("$" + value);
                    priceEditText.setSelection(value.length() + 1);
                }
                if (value.replace("$", "").compareTo("") == 0) {
                    priceEditText.setText("");
                }
                itemModified(recentlyAdded);

            }
        };
        priceEditText.addTextChangedListener(modificationWatcher);

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

        modificationWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String unit = unitSpinner.getSelectedItem().toString();
                String value = sizePerQtyEditText.getText().toString();
                if (value.compareTo("") == 0) {
                    return;
                }
                if (!value.endsWith(unit)) {

                    sizePerQtyEditText.setText(value.replaceAll("[a-z]|[A-Z]", "") + unit);
                    sizePerQtyEditText.setSelection(unitSpinner.getSelectedItem().toString().length() + 1 - unit.length());
                }
                if (value.replaceAll("[a-z]|[A-Z]", "").compareTo("") == 0) {
                    sizePerQtyEditText.setText("");
                }
                itemModified(recentlyAdded);
            }
        };
        sizePerQtyEditText.addTextChangedListener(modificationWatcher);

        // Setup a watcher for the change of unit
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemModified(recentlyAdded);
                if ("".compareTo(sizePerQtyEditText.getText().toString()) != 0) {
                    sizePerQtyEditText.setText(sizePerQtyEditText.getText().toString() + "Z");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Set passed values at the end to trigger everything
        if (name.compareTo("") != 0) {
            nameEditText.setText(name);
        }
        if (price.compareTo("") != 0) {
            priceEditText.setText(price);
        }
        if (quantity.compareTo("") != 0) {
            quantityEditText.setText(quantity);
        }
        if (size.compareTo("") != 0) {
            sizePerQtyEditText.setText(size);
        }
    }

    protected void setUnitOptions(View view) {
        // Set unit options for specific item (LinearLayout root)
        String unitType = unitTypeSpinner.getSelectedItem().toString();
        Spinner unitSpinner = (Spinner) view.findViewById(R.id.unitSpnr);

        int index = unitWorker.unitTypes.indexOf(unitType);
        String[] items = unitWorker.units.get(index).toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        unitSpinner.setAdapter(adapter);
    }

    protected void itemModified(View view) {
        // When an item/unit is modified, recalculate
        LinearLayout itemTile = (LinearLayout) view;

        if (getBasedItemValueValid(itemTile)) {
            Double baseUnitValue = getBasedItemValue(itemTile);
            String unit = ((Spinner) itemTile.findViewById(R.id.unitSpnr)).getSelectedItem().toString();
            ((TextView) itemTile.findViewById(R.id.ratiotextView)).setText(roundToString(unitWorker.convert(unitWorker.getBaseUnit(unit), unit, baseUnitValue)) + unit + "/$");
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
        generateResults();
    }

    protected void clearItems(View view) {
        // Clear all items in the itemLayout
        if  (itemLayout.getChildCount() > 0) {
            itemLayout.removeAllViews();
        }
        generateResults();
    }

    protected void unitTypeChanged() {
        // When the main unit type is changed, assign new units to items
        for (int i = 0; i < itemLayout.getChildCount(); i++) {
            setUnitOptions(itemLayout.getChildAt(i));
        }

        String unitType = unitTypeSpinner.getSelectedItem().toString();
        int index = unitWorker.unitTypes.indexOf(unitType);
        String[] items = unitWorker.units.get(index).toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.results_spinner_item, items);
        ( (Spinner) findViewById(R.id.resultsUnitSpinner) ).setAdapter(adapter);

    }

    protected void generateResults() {
        // Recalculate the results tile
        LinearLayout results = (LinearLayout) findViewById(R.id.results);
        results.removeAllViews();

        if (( (Spinner) findViewById(R.id.resultsUnitSpinner) ).getCount() < 1) {
            return;
        }

        String requestedUnit = ( (Spinner) findViewById(R.id.resultsUnitSpinner) ).getSelectedItem().toString();
        String baseUnit = unitWorker.getBaseUnit(requestedUnit);
        LinearLayout recentlyAdded;

        ArrayList<LinearLayout> itemTiles = new ArrayList<>();
        for (int i = 0; i < itemLayout.getChildCount(); i++) {
            if (getBasedItemValueValid(itemLayout.getChildAt(i))) {
                itemTiles.add((LinearLayout) itemLayout.getChildAt(i));
            }
        }

        Double lowestValue;
        int lowestItemIndex;
        while (itemTiles.size() > 0) {
            lowestValue = getBasedItemValue(itemTiles.get(0));
            lowestItemIndex = 0;
            for (int i = 1; i < itemTiles.size(); i++) {
                if (getBasedItemValue(itemTiles.get(i)) < lowestValue) {
                    lowestValue = getBasedItemValue(itemTiles.get(i));
                    lowestItemIndex = i;
                }
            }
            View inflatedView = View.inflate(this, R.layout.results_item, results);
            recentlyAdded = (LinearLayout) results.getChildAt(results.getChildCount() - 1);
            ( (TextView) recentlyAdded.findViewById(R.id.resultItemName) ).setText(( (EditText) itemTiles.get(lowestItemIndex).findViewById(R.id.nameEditText) ).getText().toString());
            ( (TextView) recentlyAdded.findViewById(R.id.resultItemValue) ).setText(roundToString(unitWorker.convert(baseUnit, requestedUnit, lowestValue)) + requestedUnit + "/$");
            itemTiles.remove(lowestItemIndex);
        }
    }

    private boolean getBasedItemValueValid(View view) {
        LinearLayout itemTile = (LinearLayout) view;
        String priceText = ( (EditText) itemTile.findViewById(R.id.priceEditText) ).getText().toString().replace("$", "");
        String quantityText = ( (EditText) itemTile.findViewById(R.id.quantityEditText) ).getText().toString();
        String amountPerQtyText = ( (EditText) itemTile.findViewById(R.id.sizePerQtyEditText) ).getText().toString().replaceAll("[a-z]|[A-Z]", "");
        return priceText.compareTo("") != 0 && quantityText.compareTo("") != 0 && amountPerQtyText.compareTo("") != 0;
    }

    private Double getBasedItemValue(View view) {
        if (!getBasedItemValueValid(view)) {
            return 0.0;
        }
        LinearLayout itemTile = (LinearLayout) view;
        String priceText = ( (EditText) itemTile.findViewById(R.id.priceEditText) ).getText().toString().replace("$", "");
        String quantityText = ( (EditText) itemTile.findViewById(R.id.quantityEditText) ).getText().toString();
        String amountPerQtyText = ( (EditText) itemTile.findViewById(R.id.sizePerQtyEditText) ).getText().toString().replaceAll("[a-z]|[A-Z]", "");

        Double price = Double.parseDouble(priceText);
        Double quantity = Double.parseDouble(quantityText);
        Double amountPerQty = Double.parseDouble(amountPerQtyText);
        Double unitPerDollar = (quantity * amountPerQty) / price;
        String unit = ((Spinner) itemTile.findViewById(R.id.unitSpnr)).getSelectedItem().toString();

        return unitWorker.convertToBase(unit, unitPerDollar);
    }

    private String roundToString(Double value) {
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(value);
    }

    private int getSpinnerIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    protected void openSettings(View view) {
        // TODO Settings activity
    }

}
