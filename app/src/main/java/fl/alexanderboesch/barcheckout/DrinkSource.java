package fl.alexanderboesch.barcheckout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DrinkSource {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private SQLiteDatabase database;
    private DrinksDbHelper dbHelper;
    private String[] columns = {DrinksDbHelper.COLUMN_ID,
                                DrinksDbHelper.COLUMN_NAME,
                                DrinksDbHelper.COLUMM_PRICE_NORMAL,
                                DrinksDbHelper.COLUMN_PRICE_STAFF,
                                DrinksDbHelper.COLUMN_QUANTITY,
                                DrinksDbHelper.COLUMN_IMAGE};


    public DrinkSource(Context context){
        dbHelper = new DrinksDbHelper(context);
        Log.i(LOG_TAG, "Unserer DbHelper wurde erzeugt");
    }

    public void open(){
        //Log.i(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt");
        database = dbHelper.getWritableDatabase();
        //Log.i(LOG_TAG, "Anfrage an: " + database.getPath());
    }

    public void close(){
        dbHelper.close();
    }

    public Drink createDrink(String name, int priceNormal, int priceStaff, int quantity, String imagePath) {

        ContentValues values = new ContentValues();
        values.put(DrinksDbHelper.COLUMN_NAME, name);
        values.put(DrinksDbHelper.COLUMM_PRICE_NORMAL, priceNormal);
        values.put(DrinksDbHelper.COLUMN_PRICE_STAFF, priceStaff);
        values.put(DrinksDbHelper.COLUMN_QUANTITY,quantity);
        values.put(DrinksDbHelper.COLUMN_IMAGE, imagePath);

        long insertId = database.insert(DrinksDbHelper.TABLE_DRINKS, null, values);
        Log.i(LOG_TAG,"Database Insert");

        Cursor cursor = database.query(DrinksDbHelper.TABLE_DRINKS, columns,DrinksDbHelper.COLUMN_ID + "=" + insertId,
                                        null,null,null,null);
        Log.i(LOG_TAG, "databse.query");

        cursor.moveToFirst();
        Log.i(LOG_TAG,"curser.movetoForst");

        Drink drink = convertToDrink(cursor);
        cursor.close();
        return drink;
    }

    private Drink curserToDrink(Cursor cursor){
        int idIndex = cursor.getColumnIndex(DrinksDbHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(DrinksDbHelper.COLUMN_NAME);
        int idPriceNormal = cursor.getColumnIndex(DrinksDbHelper.COLUMM_PRICE_NORMAL);
        int idPriceStaff = cursor.getColumnIndex(DrinksDbHelper.COLUMN_PRICE_STAFF);
        int idQuantity = cursor.getColumnIndex(DrinksDbHelper.COLUMN_QUANTITY);
        int idImagePath = cursor.getColumnIndex(DrinksDbHelper.COLUMN_IMAGE);

        int id = cursor.getInt(idIndex);
        String name = cursor.getString(idName);
        int priceNormal = cursor.getInt(idPriceNormal);
        int priceStaff = cursor.getInt(idPriceStaff);
        int quantity = cursor.getInt(idQuantity);
        String imagePath = cursor.getString(idImagePath);

        Drink drink = new Drink(name,priceNormal,priceStaff,quantity, id, imagePath);

        return drink;
    }

    public List<Drink> getAllDrinks(){
        List<Drink> listAllDrinks = new ArrayList<>();

        Cursor cursor = database.query(DrinksDbHelper.TABLE_DRINKS,
                columns, null,null ,null, null, null);
        cursor.moveToFirst();
        Drink drink;

        while (!cursor.isAfterLast()){
            drink = curserToDrink(cursor);
            listAllDrinks.add(drink);
            Log.i(LOG_TAG, "ID:" + drink.getId() + " Inhalt:" + drink.toString());
            cursor.moveToNext();
        }
        cursor.close();
        return listAllDrinks;
    }

    private Drink convertToDrink(Cursor cursor){
        // Vom Cursor die ID der Tabellenspalten abfragen
        int idIndex = cursor.getColumnIndex(DrinksDbHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(DrinksDbHelper.COLUMN_NAME);
        int idPriceNormal = cursor.getColumnIndex(DrinksDbHelper.COLUMM_PRICE_NORMAL);
        int idPriceStaff = cursor.getColumnIndex(DrinksDbHelper.COLUMN_PRICE_STAFF);
        int idQuantity = cursor.getColumnIndex(DrinksDbHelper.COLUMN_QUANTITY);
        int idImagePath = cursor.getColumnIndex(DrinksDbHelper.COLUMN_IMAGE);

        // Mit der Spalten ID den Inhalt der Zeile auslesen
        int id = cursor.getInt(idIndex);
        String name = cursor.getString(idName);
        int priceNormal = cursor.getInt(idPriceNormal);
        int priceStaff = cursor.getInt(idPriceStaff);
        int quantity = cursor.getInt(idQuantity);
        String imagePath = cursor.getString(idImagePath);

        //Den Drink erstellen und zurückgeben
        Drink drink = new Drink(name,priceNormal,priceStaff,quantity, id,imagePath);
        return drink;
    }

    public void deleteDrink(Drink drink){
        long id = drink.getId();

        database.delete(DrinksDbHelper.TABLE_DRINKS, DrinksDbHelper.COLUMN_ID + "=" + id, null);
        Log.d(LOG_TAG, "Der eintrag mit id:" + id + " mit dem Inhalt:" + drink.toString() + " -- wurde gelöscht");
    }

    public Drink getDrink(long id){
        Cursor cursor = database.query(DrinksDbHelper.TABLE_DRINKS,
                columns,DrinksDbHelper.COLUMN_ID + "=" + id, null,null,null,null);
        cursor.moveToFirst();
        Drink drink = curserToDrink(cursor);
        cursor.close();
        return drink;

    }


}
