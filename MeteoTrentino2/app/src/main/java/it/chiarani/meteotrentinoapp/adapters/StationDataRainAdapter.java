package it.chiarani.meteotrentinoapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.chiarani.meteotrentinoapp.R;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoStationsModel.Precipitazione;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoStationsModel.TemperaturaAria;

public class StationDataRainAdapter extends RecyclerView.Adapter<StationDataRainAdapter.ViewHolder> {
    private List<Precipitazione> mItems;

    public StationDataRainAdapter(List<Precipitazione> items) {
        this.mItems = items;
    }

    @NonNull
    @Override
    public StationDataRainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_station_data, parent, false);

        return new StationDataRainAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtData, txtTime, txtValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtData = itemView.findViewById(R.id.item_row_station_data);
            txtTime = itemView.findViewById(R.id.item_row_station_time);
            txtValue = itemView.findViewById(R.id.item_row_station_value);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull StationDataRainAdapter.ViewHolder holder, int position) {
        holder.txtValue.setText(String.format("%s mm", mItems.get(position).getPioggia()));
        holder.txtTime.setText(mItems.get(position).getData().split("T")[1].substring(0, 5));
        holder.txtData.setText(mItems.get(position).getData().split("T")[0]);
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

}
