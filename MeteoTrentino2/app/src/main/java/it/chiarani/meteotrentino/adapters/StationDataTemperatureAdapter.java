package it.chiarani.meteotrentino.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.chiarani.meteotrentino.R;
import it.chiarani.meteotrentino.api.MeteoTrentinoStationsModel.TemperaturaAria;

public class StationDataTemperatureAdapter extends RecyclerView.Adapter<StationDataTemperatureAdapter.ViewHolder> {
    private List<TemperaturaAria> mItems;

    public StationDataTemperatureAdapter(List<TemperaturaAria> items) {
        this.mItems = items;
    }

    @NonNull
    @Override
    public StationDataTemperatureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_station_data, parent, false);

        return new StationDataTemperatureAdapter.ViewHolder(view);
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
    public void onBindViewHolder(@NonNull StationDataTemperatureAdapter.ViewHolder holder, int position) {
        holder.txtValue.setText(String.format("%s °C", mItems.get(position).getTemperatura()));
        holder.txtTime.setText(mItems.get(position).getData().split("T")[1].substring(0, 5));
        holder.txtData.setText(mItems.get(position).getData().split("T")[0]);
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

}
