package fl.alexanderboesch.barcheckout;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class DrinkHashAdapter extends BaseAdapter{
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Long[] mKeys;
    Context context;
    HashMap<Long, Integer> drinks;
    LayoutInflater layoutInflater;
    final DrinkSource datasource;


    public DrinkHashAdapter(Context applicationContext, HashMap<Long, Integer> drinks){
        this.context = applicationContext;
        this.drinks = drinks;
        mKeys = drinks.keySet().toArray(new Long[drinks.size()]);

        datasource = new DrinkSource(applicationContext);
        layoutInflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return drinks.size();
    }

    @Override
    public Object getItem(int position) {
        return drinks.get(mKeys[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_view, null);
        }
        datasource.open();
        TextView name = (TextView) view.findViewById(R.id.textViewName);
        TextView count = (TextView) view.findViewById(R.id.textViewCount);

        name.setText(datasource.getDrink(mKeys[position]).getName());

        count.setText(String.valueOf(getItem(position)));
        datasource.close();
        return view;
    }
}