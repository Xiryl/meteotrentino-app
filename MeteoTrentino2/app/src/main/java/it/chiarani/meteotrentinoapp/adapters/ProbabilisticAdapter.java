package it.chiarani.meteotrentinoapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.List;

import it.chiarani.meteotrentinoapp.R;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoProbabilisticModel.Fasce;
import it.chiarani.meteotrentinoapp.models.AllertItem;

public class ProbabilisticAdapter extends RecyclerView.Adapter<ProbabilisticAdapter.ViewHolder> {
    private List<Fasce> mItems;
    private Context ctx;

    public ProbabilisticAdapter(List<Fasce> items) {
        this.mItems = items;
    }

    @NonNull
    @Override
    public ProbabilisticAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_probabilistic_column, parent, false);

        return new ProbabilisticAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSlot;
        RecyclerView rv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSlot = itemView.findViewById(R.id.item_probabilistic_column_slot);
            rv = itemView.findViewById(R.id.item_probabilistic_column_rv);
            ctx = itemView.getContext();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProbabilisticAdapter.ViewHolder holder, int position) {
       holder.txtSlot.setText(mItems.get(position).getFasciaOre());

        LinearLayoutManager linearLayoutManagerTags = new LinearLayoutManager(ctx.getApplicationContext());
        linearLayoutManagerTags.setOrientation(RecyclerView.VERTICAL);
        holder.rv.setLayoutManager(linearLayoutManagerTags);

        ProbabilisticPhenomenaAdapter mAdapter = new ProbabilisticPhenomenaAdapter(mItems.get(position).getFenomeni());
        holder.rv.setAdapter(mAdapter);
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

}
