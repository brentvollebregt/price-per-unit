package net.nitratine.priceperunit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ItemStorage extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "items";
    private static final String C1 = "id";
    private static final String C2 = "name";
    private static final String C3 = "price";
    private static final String C4 = "quantity";
    private static final String C5 = "size";
    private static final String C6 = "unit";

    public ItemStorage(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                C2 + " TEXT, " +
                C3 + " TEXT, " +
                C4 + " TEXT, " +
                C5 + " TEXT, " +
                C6 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(ArrayList<ItemStructure> data) {
        SQLiteDatabase db = this.getWritableDatabase();

        onUpgrade(db, 1, 1);

        ContentValues contentValues;
        for (ItemStructure item : data) {
            contentValues = new ContentValues();
            contentValues.put(C2, item.name);
            contentValues.put(C3, item.price);
            contentValues.put(C4, item.quantity);
            contentValues.put(C5, item.size);
            contentValues.put(C6, item.unit);

            long result = db.insert(TABLE_NAME, null, contentValues);

            if (result == -1) { // Dump everything if an error occurred
                onUpgrade(db, 1, 1);
                onCreate(db);
                return;
            }
        }

        db.close();

    }

    public ArrayList<ItemStructure> getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ItemStructure> data = new ArrayList<ItemStructure>();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        while(cursor.moveToNext()) {
            ItemStructure item = new ItemStructure();
            item.name = cursor.getString(cursor.getColumnIndex(C2));
            item.price = cursor.getString(cursor.getColumnIndex(C3));
            item.quantity = cursor.getString(cursor.getColumnIndex(C4));
            item.size = cursor.getString(cursor.getColumnIndex(C5));
            item.unit = cursor.getString(cursor.getColumnIndex(C6));
            data.add(item);
        }
        cursor.close();

        db.close();

        return data;
    }

}
