package fl.alexanderboesch.barcheckout;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DrinkAdapterRecyclerView extends RecyclerView.Adapter<DrinkAdapterRecyclerView.ViewHolder> {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private final List<Drink> mData;
    private final LayoutInflater mInflater;
    private View.OnClickListener onShortClickListener;
    private View.OnLongClickListener onLongClickListener;


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
            itemView.setOnClickListener(onShortClickListener);
            itemView.setOnLongClickListener(onLongClickListener);
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

    public void setOnShortClickListener(View.OnClickListener clickListener) {
        onShortClickListener = clickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener clickListener){
        onLongClickListener = clickListener;
    }

    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + getItem(position).toString() + ", which is at cell position " + position);
    }
}