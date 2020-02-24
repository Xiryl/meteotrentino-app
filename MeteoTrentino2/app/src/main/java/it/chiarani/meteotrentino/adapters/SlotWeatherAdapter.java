package it.chiarani.meteotrentino.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.chiarani.meteotrentino.R;
import it.chiarani.meteotrentino.api.ForecastModel.Fascia;
import it.chiarani.meteotrentino.api.ForecastModel.Giorno;
import it.chiarani.meteotrentino.utils.DayConverter;
import it.chiarani.meteotrentino.utils.IconConverter;

public class SlotWeatherAdapter extends RecyclerView.Adapter<SlotWeatherAdapter.ViewHolder>{
    private List<Fascia> mItems;

    public SlotWeatherAdapter(List<Fascia> items) {
        this.mItems = items;
    }

    @NonNull
    @Override
    public SlotWeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_slot_weather, parent, false);

        return new SlotWeatherAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTime, txtSlot, txtTemperatures;
        ImageView icWeather;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.item_seven_day_weather_txt_slot_time);
            txtSlot = itemView.findViewById(R.id.item_seven_day_weather_txt_slot_text);
            txtTemperatures = itemView.findViewById(R.id.item_seven_day_weather_txt_slot_temperatures);
            icWeather = itemView.findViewById(R.id.item_seven_day_weather_ic_weather);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SlotWeatherAdapter.ViewHolder holder, int position) {
        holder.txtTemperatures.setText(mItems.get(position).getFasciaOre());
        holder.txtTemperatures.setText(mItems.get(position).getFascia());
        String ico = mItems.get(position).getIcona().split("_")[1].substring(0, 3);
        holder.icWeather.setBackgroundResource(IconConverter.getIconFromId(Integer.parseInt(ico)));
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

}
