package it.chiarani.meteotrentino.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.chiarani.meteotrentino.R;
import it.chiarani.meteotrentino.api.MeteoTrentinoProbabilisticModel.Giorni;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {
    private List<Giorni> mItems;
    private ItemClickListener mListener;
    private int selected = 0;

    public DaysAdapter(List<Giorni> items, ItemClickListener mListener) {
        this.mItems = items;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public DaysAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);

        return new DaysAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtDay, txtDate;
        ConstraintLayout cl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.item_day_txt_date);
            txtDay = itemView.findViewById(R.id.item_day_txt_day);
            cl = itemView.findViewById(R.id.item_day_cl);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(this.getAdapterPosition());
            selected = this.getAdapterPosition();
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull DaysAdapter.ViewHolder holder, int position) {
        if(position == selected) {
            holder.cl.setBackgroundResource(R.drawable.bg_item_gray_dark);
        } else {
            holder.cl.setBackgroundResource(R.drawable.bg_item_gray);
        }
        holder.txtDate.setText(mItems.get(position).getGiorno());
        holder.txtDay.setText(mItems.get(position).getNomeGiorno());
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

}
