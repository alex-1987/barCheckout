package fl.alexanderboesch.barcheckout;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DrinksDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String DB_NAME = "Drinks.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_DRINKS = "Drinks";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMM_PRICE_NORMAL = "price_normal";
    public static final String COLUMN_PRICE_STAFF = "price_staff";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_IMAGE = "image";

    public static final String CREATE_SQL =
            "CREATE TABLE " + TABLE_DRINKS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMM_PRICE_NORMAL + " INTEGER NOT NULL, " +
                    COLUMN_PRICE_STAFF + " INTEGER NOT NULL, " +
                    COLUMN_QUANTITY + " INTEGER, " +
                    COLUMN_IMAGE + " TEXT " + ");";


    public DrinksDbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        Log.i(LOG_TAG, "DbHelper für die Tabelle: " + getDatabaseName() + " initialisert");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.i(LOG_TAG, "Die Datenbank wird mit dem Befehl erstellt -- SQL: " + CREATE_SQL);
            db.execSQL(CREATE_SQL);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
