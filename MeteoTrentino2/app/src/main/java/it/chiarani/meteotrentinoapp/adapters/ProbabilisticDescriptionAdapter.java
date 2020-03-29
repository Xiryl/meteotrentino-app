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
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoProbabilisticModel.Fenomeni;

public class ProbabilisticDescriptionAdapter extends RecyclerView.Adapter<ProbabilisticDescriptionAdapter.ViewHolder> {
    private List<Fenomeni> mItems;

    public ProbabilisticDescriptionAdapter(List<Fenomeni> items) {
        this.mItems = items;
    }

    @NonNull
    @Override
    public ProbabilisticDescriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_probabilistic_description, parent, false);

        return new ProbabilisticDescriptionAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtValue = itemView.findViewById(R.id.item_probabilistic_description_value);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProbabilisticDescriptionAdapter.ViewHolder holder, int position) {
        holder.txtValue.setText(String.format("%s", mItems.get(position).getFenomeno().replaceFirst(" ", "\n")));
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

}
