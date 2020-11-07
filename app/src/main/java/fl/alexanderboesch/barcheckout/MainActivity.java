package fl.alexanderboesch.barcheckout;

import android.content.ContextWrapper;
import android.content.Intent;

import android.net.Uri;

import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private DrinkSource dataSource;
    private GridView grid;

    private static final int PICK_IMAGE = 100;
    public Uri imageUri;
    public ImageView imageView;

    private HashMap<Long,Integer> hashMap;

    public void onSaveInstanceState(@NonNull Bundle savedState) {
    super.onSaveInstanceState(savedState);
    // Die Konsumierten Getränke In ein ArrayList seichern und in der OnCreate Methode wieder auslesen
        Log.i(LOG_TAG, "CAll onSaveInstanceState");
        ArrayList<Integer> hashValue = new ArrayList<>();
        long[] hashKeyArray = new long[hashMap.size()];

        if (hashMap != null){
            int k = 0;
            for (long l : hashMap.keySet()){
                Log.i(LOG_TAG, "Einlesen der Hashmap");
                Log.i(LOG_TAG, "KEY: " + l + " = " + hashMap.get(l) );
                hashKeyArray[k] = l;
                hashValue.add(hashMap.get(l));
                Log.i(LOG_TAG, l + " = " + hashMap.get(l));
                k++;
            }
        }
        Log.i(LOG_TAG, "Alle key von der Hashmap im Array");
        Log.i(LOG_TAG, "Size von Array: " + hashValue.size());
        for (int i : hashValue){
            Log.i(LOG_TAG, "Key: " + i);
        }
        for (int i = 0; i < hashValue.size(); i++){
            Log.i(LOG_TAG, " for Key: " + hashValue.get(i));
        }



        savedState.putLongArray("HashKey", hashKeyArray);
        savedState.putIntegerArrayList("HashValue", hashValue);

    }
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "--Starten der onCreate Methode");
        setContentView(R.layout.activity_main);

        dataSource = new DrinkSource(this);
        if (hashMap == null){
            hashMap = new HashMap<>();
            }

        // Die Konsumierten Getränke Auslesen und wieder in die Hashmap eintragen
        if (savedInstanceState != null){
            ArrayList<Integer> hashValue = savedInstanceState.getIntegerArrayList("HashValue");
            long[] hashKey = savedInstanceState.getLongArray("HashKey");

            for (int i = 0; i < hashValue.size(); i++){
                hashMap.put(hashKey[i], hashValue.get(i));
            }
        }

        activateFAB();
        initAppBar();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();

        Log.i(LOG_TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
        showAllDrinkEntries();
        showAllUsedDrinks();
        activeShortClick();
        activateLongClick();
        calculateAllUsedDrinks();

    }

    private void initAppBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    private void showAllUsedDrinks(){
        ListView listview = (ListView) findViewById(R.id.list_view);
        Log.i(LOG_TAG, "showAllUsedDrinks");
        Log.i(LOG_TAG, "Es sind " + hashMap.size() + " auf der Liste");

        DrinkHashAdapter hashAdapter = new DrinkHashAdapter(getApplicationContext(),hashMap);
        listview.setAdapter(hashAdapter);
    }

    private void calculateAllUsedDrinks(){
        TextView textViewCount = (TextView) findViewById(R.id.textView);
        ArrayList<Double> pricePerDrink = new ArrayList<>();

        Log.i(LOG_TAG, "Es sind " + hashMap.size() + " Getränke auf der Liste");
        int total = 0;
        double summeryUsedDrinks = 0;
        for (long l : hashMap.keySet()){
            total += hashMap.get(l);
        }
        Log.i(LOG_TAG, "Es sind " + total + " gekauft worden");

        for (long l : hashMap.keySet()){
            Drink drink = dataSource.getDrink(l);
            double drinkPrice = drink.getPriceNormal(); // Price from a Drink
            Log.i(LOG_TAG,"Preis eines drinks ist: " + drinkPrice);
            int count = hashMap.get(l); // How much Drinks on the List
            pricePerDrink.add(drinkPrice * count);
            Log.i(LOG_TAG, "Das " + drink.getName() + " ist " + count + " in der Liste::: Dies macht: " + drinkPrice * count);
        }
        for (double d : pricePerDrink){
            summeryUsedDrinks += d;
        }
        textViewCount.setText(String.valueOf(summeryUsedDrinks));
        //textViewCount.setText("TEST");
    }

    private void showAllDrinkEntries(){
        Log.i(LOG_TAG, "set GridView");
        grid = (GridView) findViewById(R.id.grid_view);

        Log.i(LOG_TAG, "alle Drinks von der Datenbank holen");
        List<Drink> dbDrinks = dataSource.getAllDrinks();

        Log.i(LOG_TAG, "Adapter erstellen");
        DrinkAdapter drinkAdapter = new DrinkAdapter(getApplicationContext(), dbDrinks);

        Log.i(LOG_TAG, "Adapter setzen");
        grid.setAdapter(drinkAdapter);
    }



    private void activateFAB(){
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newDrinkPopUp();
                //showAllEntries();
            }
        });
    }

    private void activeShortClick(){
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "position:" + position + " id:" + id,Toast.LENGTH_LONG).show();
                //Put one Drink in the HashMap
                putDrinkOnList(id, 1, "yes");
                showAllUsedDrinks();
            }
        });
    }

    private void activateLongClick() {
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final long drinkId = id;
                Log.i(LOG_TAG, "LONG-KLICK");
                Drink drink = dataSource.getDrink(id);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final View newItemCountView = getLayoutInflater().inflate(R.layout.item_count,null);

                final EditText count = (EditText) newItemCountView.findViewById(R.id.item_count_edit_text);
                Button buttonOk = (Button) newItemCountView.findViewById(R.id.count_button_ok);
                Button buttonCancel = (Button) newItemCountView.findViewById(R.id.count_button_cancel);
                SeekBar seekBar = (SeekBar) newItemCountView.findViewById(R.id.item_count_seekBar);

                builder.setView(newItemCountView);
                builder.create();
                final AlertDialog dialog =  builder.show();
                seekBar.setProgress(getDrinkOnList(id));
                count.setText(String.valueOf(seekBar.getProgress()));

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    int seekbarval = 0;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        seekbarval = progress;
                        count.setText(String.valueOf(seekbarval));
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        count.setText(String.valueOf(seekbarval));
                    }
                });

                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        putDrinkOnList(drinkId, Integer.parseInt(count.getText().toString()),"no");
                        dialog.dismiss();
                        showAllUsedDrinks();
                    }
                });

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                return true;
            }
       });
    }


    private void putDrinkOnList(long id, int count, String addition){
        if (hashMap == null) {
            hashMap = new HashMap<>();
        }

        Drink drink = dataSource.getDrink(id);        // get the Drink
        Log.i(LOG_TAG, "putDrinkOnList:" + drink.getName());
        switch (addition){
            case "yes":
                if (hashMap.containsKey(id)){
                    hashMap.put(id,hashMap.get(id) + count); // Add the count to the list
                } else {
                    hashMap.put(id, count);
                }
                break;
            case "no":
                hashMap.put(id, count);
                break;
        }
        Log.i(LOG_TAG,"--------------------");
        calculateAllUsedDrinks();
    }

    private int getDrinkOnList(long id){
        try {
            return hashMap.get(id);
        } catch (NullPointerException ex){
            return 0;
        }
    }

    private void newDrinkPopUp(){
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
        final View newDrinkView = getLayoutInflater().inflate(R.layout.new_drink, null);

        imageView = (ImageView) newDrinkView.findViewById(R.id.NewDrinkimageView);
        final EditText nameId = (EditText) newDrinkView.findViewById(R.id.NewDrinkeditTextDrinkName);
        final EditText priceNormalId = (EditText) newDrinkView.findViewById(R.id.NewDrinkpriceNormalEditText);
        final EditText priceEmployeeId = (EditText) newDrinkView.findViewById(R.id.NewDrinkPriceEmployeeEditText);
        final EditText quantityId = (EditText) newDrinkView.findViewById(R.id.NewDrinkQuantityEditText);
        Button buttonAdd = (Button) newDrinkView.findViewById(R.id.buttonAddNew);
        Button buttonCancel = (Button) newDrinkView.findViewById(R.id.buttonCancel);

        dialogbuilder.setView(newDrinkView);
        final AlertDialog dialog = dialogbuilder.create();
        dialog.show();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "Neues Bild wird ausgewählt");
                openGallery();
             }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameId.getText().toString();
                int priceNormal = Integer.parseInt(priceNormalId.getText().toString());
                int priceEmployee = Integer.parseInt(priceEmployeeId.getText().toString());
                int quantity = Integer.parseInt(quantityId.getText().toString());

                ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                File file = wrapper.getDir("Images",MODE_PRIVATE);
                file = new File(file, name + ".jpg");
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    OutputStream outputStream = new FileOutputStream(file);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len=inputStream.read(buf))>0){
                        outputStream.write(buf,0,len);
                    }
                    inputStream.close();
                    outputStream.close();
                    Log.i(LOG_TAG, "Speichern des Bildes erfolgreich:" + file);
                } catch (IOException o) {
                    o.printStackTrace();
                    Log.i(LOG_TAG, "Speichern des Bildes NICHT erfolgreich");
                }
                dataSource.createDrink(name,priceNormal,priceEmployee,quantity,file.toString());
                dialog.dismiss();
                showAllDrinkEntries();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data.getData() != null){
                imageUri = data.getData();
            }
            else {
                Log.i(LOG_TAG, "Fehler bei Bildauswahl");
            }
            Log.i(LOG_TAG, "URI:" + imageUri);
           imageView.setImageURI(imageUri);
    }



}

