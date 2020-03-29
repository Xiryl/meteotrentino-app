package it.chiarani.meteotrentinoapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.security.spec.ECField;
import java.util.List;

import it.chiarani.meteotrentinoapp.R;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoStationsModel.TemperaturaAria;

public class BacinoDataAdapter extends RecyclerView.Adapter<BacinoDataAdapter.ViewHolder> {
    private List<String> mItems;

    public BacinoDataAdapter(List<String> items) {
        this.mItems = items;
    }

    @NonNull
    @Override
    public BacinoDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_station_data, parent, false);

        return new BacinoDataAdapter.ViewHolder(view);
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
    public void onBindViewHolder(@NonNull BacinoDataAdapter.ViewHolder holder, int position) {
        try {
            String data = mItems.get(position).split(";")[0];
            String value = mItems.get(position).split(";")[1];
            holder.txtValue.setText(value);
            holder.txtTime.setText("");
            holder.txtData.setText(data);
        } catch ( Exception ex) {
            int x = 1;
        }

    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

}
