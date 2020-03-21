package it.chiarani.meteotrentino.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.chiarani.meteotrentino.R;
import it.chiarani.meteotrentino.api.MeteoTrentinoForecastModel.Fascia;
import it.chiarani.meteotrentino.utils.IconConverter;

public class SlotWeatherAdapter extends RecyclerView.Adapter<SlotWeatherAdapter.ViewHolder>{
    private List<Fascia> mItems;
    private ItemClickListener mListener;

    public SlotWeatherAdapter(List<Fascia> items, ItemClickListener itemClickListener) {
        this.mItems = items;
        this.mListener = itemClickListener;
    }

    @NonNull
    @Override
    public SlotWeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_slot_weather, parent, false);

        return new SlotWeatherAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtTime, txtSlot, txtForecast;
        ImageView icWeather;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.item_seven_day_weather_txt_slot_time);
            txtSlot = itemView.findViewById(R.id.item_seven_day_weather_txt_slot_text);
            icWeather = itemView.findViewById(R.id.item_seven_day_weather_ic_weather);
            txtForecast = itemView.findViewById(R.id.item_seven_day_weather_txt_forecast);
           itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(this.getAdapterPosition());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SlotWeatherAdapter.ViewHolder holder, int position) {
        holder.txtTime.setText(mItems.get(position).getFasciaOre());
        holder.txtSlot.setText(mItems.get(position).getFasciaPer());
        String ico = mItems.get(position).getIcona().split("_")[1].substring(0, 3);
        holder.icWeather.setBackgroundResource(IconConverter.getIconFromId(Integer.parseInt(ico)));
        String perc = mItems.get(position).getDescPrecProb();
        holder.txtForecast.setText("Prob. prec.\n" + perc);
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

}
