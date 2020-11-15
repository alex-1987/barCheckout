package fl.alexanderboesch.barcheckout;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

import fl.alexanderboesch.barcheckout.InvoiceDb.InvoiceDao;

@Database(entities = {InvoiceDb.class}, version = 1, exportSchema = false)
@TypeConverters({InvoiceDbRoom.Converters.class})
public abstract class InvoiceDbRoom extends RoomDatabase {
    private static InvoiceDbRoom database;
    private static String DATABASE_NAME = "invoice";

    public synchronized static InvoiceDbRoom getInstance(Context context){
        if (database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    InvoiceDbRoom.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
    public abstract InvoiceDao invoiceDao();

    public static class Converters {
        @TypeConverter
        public Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public Long dateToTimestamp(Date date) {
            return date == null ? null : date.getTime();
        }
    }

}
