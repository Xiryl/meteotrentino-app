package it.chiarani.meteotrentinoapp.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.List;

import it.chiarani.meteotrentinoapp.R;
import it.chiarani.meteotrentinoapp.models.AllertItem;

public class AllertAdapter extends RecyclerView.Adapter<AllertAdapter.ViewHolder> {
    private List<AllertItem> mItems;
    private ItemClickListener mListener;

    public AllertAdapter(List<AllertItem> items, ItemClickListener itemClickListener) {
        this.mItems = items;
        this.mListener = itemClickListener;
    }

    @NonNull
    @Override
    public AllertAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_allert, parent, false);

        return new AllertAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtName, txtDate;
        ImageView icDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.item_allert_txt_name);
            txtDate = itemView.findViewById(R.id.item_allert_txt_date);
            icDate = itemView.findViewById(R.id.item_allert_ic_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(this.getAdapterPosition());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AllertAdapter.ViewHolder holder, int position) {
        holder.txtDate.setText(String.format("%s %s %s", mItems.get(position).getDay(), mItems.get(position).getMonth(), mItems.get(position).getYear()));
        holder.txtName.setText(mItems.get(position).getName());

        TextDrawable dateDrawable = TextDrawable
                .builder()
                .beginConfig()
                .fontSize(28)
                .endConfig()
                .buildRoundRect(mItems.get(position).getMonth().toUpperCase(), Color.parseColor("#350F4E"), 100);

        dateDrawable.setPadding(2,2,2,2);

        holder.icDate.setImageDrawable(dateDrawable);
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

}
