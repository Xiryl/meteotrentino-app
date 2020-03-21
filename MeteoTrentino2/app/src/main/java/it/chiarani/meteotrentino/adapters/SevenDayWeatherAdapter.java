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
import it.chiarani.meteotrentino.api.MeteoTrentinoForecastModel.Giorno;
import it.chiarani.meteotrentino.utils.DayConverter;
import it.chiarani.meteotrentino.utils.IconConverter;

public class SevenDayWeatherAdapter extends RecyclerView.Adapter<SevenDayWeatherAdapter.ViewHolder> {
    private List<Giorno> mItems;
    private ItemClickListener mListener;

    public SevenDayWeatherAdapter(List<Giorno> items, ItemClickListener itemClickListener) {
        this.mItems = items;
        this.mListener = itemClickListener;
    }

    @NonNull
    @Override
    public SevenDayWeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seven_day_weather, parent, false);

        return new SevenDayWeatherAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtDay, txtForecast, txtTemperatures, txtDate;
        ImageView icForecast;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDay = itemView.findViewById(R.id.item_seven_day_weather_txt_day);
            txtForecast = itemView.findViewById(R.id.item_seven_day_weather_txt_forecast);
            txtTemperatures = itemView.findViewById(R.id.item_seven_day_weather_txt_temperatures);
            txtDate = itemView.findViewById(R.id.item_seven_day_weather_txt_date);
            icForecast = itemView.findViewById(R.id.item_seven_day_weather_ic_weather);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(this.getAdapterPosition());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SevenDayWeatherAdapter.ViewHolder holder, int position) {
        holder.txtDay.setText(DayConverter.ExtractDayFromDate(mItems.get(position).getGiorno()));
        holder.txtForecast.setText(mItems.get(position).getDescIcona());
        holder.txtTemperatures.setText(String.format("%s °C / %s °C", mItems.get(position).getTMaxGiorno(), mItems.get(position).getTMinGiorno()));
        holder.txtDate.setText(DayConverter.convertDate(mItems.get(position).getGiorno()));
        holder.icForecast.setBackgroundResource(IconConverter.getIconFromId(mItems.get(position).getIdIcona()));
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

}
