package fl.alexanderboesch.barcheckout;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

public class DrinkAdapterRecyclerView extends RecyclerView.Adapter<DrinkAdapterRecyclerView.ViewHolder> {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private List<Drink> mData;
    private LayoutInflater mInflater;
    private View.OnClickListener onItemClickListener;


    // data is passed into the constructor
    DrinkAdapterRecyclerView(Context context, List<Drink> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;

    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.grid, parent, false);
        return new ViewHolder(view);
    }


    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.myTextView.setText(mData.get(position).getName());
        holder.imageView.setImageURI(Uri.parse(mData.get(position).getImagePath()));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.drinkName);
            imageView = itemView.findViewById(R.id.imageDrink);
            itemView.setTag(this);
            Log.i(LOG_TAG, "Item-View :");
            itemView.setOnClickListener(onItemClickListener);
            
        }


        @Override
        public void onClick(View view) {
            onItemClick(view, getAdapterPosition());
            Log.i(LOG_TAG, "OnClick: " + getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData.get(id).getName();
    }
    public long getItemIdR(int id) {
        return mData.get(id).getId();
    }

    public void setItemClickListener(View.OnClickListener clickListener) {
        onItemClickListener = clickListener;
    }


    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + getItem(position).toString() + ", which is at cell position " + position);
    }
}