package fl.alexanderboesch.barcheckout;

import androidx.room.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity (tableName = "invoice")
public class InvoiceDb implements Serializable {
    public static final String TBNAME = "invoice";

    public InvoiceDb(String invoiceNr, String drinkName, int count, double price_unit) {
        this.invoiceNr = invoiceNr;
        this.drinkName = drinkName;
        this.count = count;
        this.price_unit = price_unit;
    }

    @PrimaryKey(autoGenerate = true)
     public long ID;

    @ColumnInfo(name = "invoice_nr")
    public String invoiceNr;


    @ColumnInfo(name = "drink_name")
    public String drinkName;

    @ColumnInfo(name = "count")
    public int count;

    @ColumnInfo(name = "price_unit")
    public double price_unit;

    @ColumnInfo(name = "date")
    public Date date;



    // Getter
    public long getId() {
        return ID;
    }

    public String  getInvoiceNr() {
        return invoiceNr;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public int getCount() {
        return count;
    }

    public double getPrice_unit() {
        return price_unit;
    }

    public Date getDate() {
        return date;
   }


    @Dao
    public interface InvoiceDao {
         @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(InvoiceDb invoiceDb);

         @Delete
        void delete(InvoiceDb invoiceDb);

         @Delete
        void resert(List<InvoiceDb> invoicesList);

         @Query("UPDATE Invoice SET invoice_nr = :invoiceNr WHERE ID = :id")
        void update(int invoiceNr, long id);

         @Query("SELECT * FROM Invoice")
        List<InvoiceDb> getAll();


    }

}
