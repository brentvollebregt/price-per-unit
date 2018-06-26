package net.nitratine.priceperunit;

import java.util.ArrayList;

public class Units {

    public ArrayList<String> unitTypes = new ArrayList<>();
    public ArrayList< ArrayList<String> > units = new ArrayList<>();
    private ArrayList< ArrayList<Double> > unitBaseRelativity = new ArrayList<>();

    public Units() {
        unitTypes.add("Weight");
        unitTypes.add("Volume");
        unitTypes.add("Length");
        unitTypes.add("Pieces");

        ArrayList<String> units_weight = new ArrayList<>();
        ArrayList<Double> units_weight_relativity = new ArrayList<>();
        units_weight.add("g");
        units_weight_relativity.add(1.0);
        units_weight.add("kg");
        units_weight_relativity.add(0.001); // 5kg / 0.001 = 5000g
        units_weight.add("tonne");
        units_weight_relativity.add(0.000001);
        units.add(units_weight); // Match the first index as "Weight" is in unitTypes
        unitBaseRelativity.add(units_weight_relativity);

        ArrayList<String> units_volume = new ArrayList<>();
        ArrayList<Double> units_volume_relativity = new ArrayList<>();
        units_volume.add("l");
        units_volume_relativity.add(1.0);
        units_volume.add("ml");
        units_volume_relativity.add(1000.0);
        units.add(units_volume);
        unitBaseRelativity.add(units_volume_relativity);

        ArrayList<String> units_length = new ArrayList<>();
        ArrayList<Double> units_length_relativity = new ArrayList<>();
        units_length.add("m");
        units_length_relativity.add(1.0);
        units_length.add("mm");
        units_length_relativity.add(1000.0);
        units_length.add("cm");
        units_length_relativity.add(100.0);
        units_length.add("km");
        units_length_relativity.add(0.001);
        units.add(units_length);
        unitBaseRelativity.add(units_length_relativity);

        ArrayList<String> units_pieces = new ArrayList<>();
        ArrayList<Double> units_pieces_relativity = new ArrayList<>();
        units_pieces.add("pcs");
        units_pieces_relativity.add(1.0);
        units.add(units_pieces);
        unitBaseRelativity.add(units_pieces_relativity);
    }

    public Double convert(String unitA, String unitB, Double value) {
        Double unitARelativity = 1.0;
        Double unitBRelativity = 1.0;
        for (ArrayList<String> unitGroup: units) {
            if (unitGroup.contains(unitA)) {
                // Get the item in unitTypes where the index matches the ArrayList we found the item in units
                int positionIndex = units.indexOf(unitGroup);
                int unitAIndex = unitGroup.indexOf(unitA);
                int unitBIndex = unitGroup.indexOf(unitB);
                unitARelativity = unitBaseRelativity.get(positionIndex).get(unitAIndex);
                unitBRelativity = unitBaseRelativity.get(positionIndex).get(unitBIndex);
            }
        }

        value = value / unitARelativity;
        value = value * unitBRelativity;

        return value;
    }

    public Double convertToBase(String unit, Double value) {
        for (ArrayList<String> unitGroup : units) {
            if (unitGroup.contains(unit)) {
                value = convert(unit, unitGroup.get(0),value);
            }
        }

        return value;
    }
}
