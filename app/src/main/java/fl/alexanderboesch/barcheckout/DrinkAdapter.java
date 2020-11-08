package fl.alexanderboesch.barcheckout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import fl.alexanderboesch.barcheckout.R;

public class DrinkAdapter extends BaseAdapter{
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    Context context;
    List<Drink> drinks;
    LayoutInflater layoutInflater;

    public DrinkAdapter(Context applicationContext, List<Drink> drinks){
        this.context = applicationContext;
        this.drinks = drinks;
        layoutInflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return drinks.size();
    }

    @Override
    public Object getItem(int position) {
        return drinks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return drinks.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.grid, null);
        }
        ImageView pic = (ImageView) view.findViewById(R.id.imageDrink);
        TextView text = (TextView) view.findViewById(R.id.drinkName);

       // pic.setImageResource(drinks.get(position).getImageResource());
        Bitmap bm = BitmapFactory.decodeFile(drinks.get(position).getImagePath());
        text.setText(drinks.get(position).getName());
        pic.setImageURI(Uri.parse(drinks.get(position).getImagePath()));
        return view;
    }
}