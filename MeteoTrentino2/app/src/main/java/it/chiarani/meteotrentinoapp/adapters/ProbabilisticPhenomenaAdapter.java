package it.chiarani.meteotrentinoapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.chiarani.meteotrentinoapp.R;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoProbabilisticModel.Fasce;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoProbabilisticModel.Fenomeni;

public class ProbabilisticPhenomenaAdapter extends RecyclerView.Adapter<ProbabilisticPhenomenaAdapter.ViewHolder> {
    private List<Fenomeni> mItems;

    public ProbabilisticPhenomenaAdapter(List<Fenomeni> items) {
        this.mItems = items;
    }

    @NonNull
    @Override
    public ProbabilisticPhenomenaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_probabilistic_square, parent, false);

        return new ProbabilisticPhenomenaAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtValue;
        ConstraintLayout cl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtValue = itemView.findViewById(R.id.item_probabilistic_square_value);
            cl = itemView.findViewById(R.id.item_probabilistic_square_cl);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProbabilisticPhenomenaAdapter.ViewHolder holder, int position) {
        int value = 0;
        try {
            value = mItems.get(position).getValore();
            holder.txtValue.setText(String.format("%s", value));
        }
        catch (Exception ex) {
            holder.txtValue.setText("-");
        }



        switch (value) {
            case 0:
                holder.cl.setBackgroundResource(R.drawable.bg_item_square_green);
                break;
            case 1:
                holder.cl.setBackgroundResource(R.drawable.bg_item_square_yellow);
                break;
            case 2:
                holder.cl.setBackgroundResource(R.drawable.bg_item_square_red);
                break;
            default:
                holder.cl.setBackgroundResource(R.drawable.bg_item_square_green);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

}
