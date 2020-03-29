package it.chiarani.meteotrentinoapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Filter;

import androidx.recyclerview.widget.RecyclerView;

import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.util.ArrayList;

import it.chiarani.meteotrentinoapp.R;

public class CustomSuggestionsAdapter extends SuggestionsAdapter<String, CustomSuggestionsAdapter.SuggestionHolder> {

    SuggestionItemClickListener mListener;

    public CustomSuggestionsAdapter(LayoutInflater inflater, SuggestionItemClickListener listener) {
        super(inflater);
        this.mListener = listener;
    }

    @Override
    public int getSingleViewHeight() {
        return 80;
    }

    @Override
    public SuggestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_custom_suggestion, parent, false);
        return new SuggestionHolder(view);
    }

    @Override
    public void onBindSuggestionHolder(String suggestion, SuggestionHolder holder, int position) {
        String location = suggestion.split(";")[0];
        String comune = "Comune di " + suggestion.split(";")[1];
        holder.location.setText(location);
        holder.comune.setText(comune.toLowerCase());
    }


    class SuggestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView location, comune;

        public SuggestionHolder(View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.item_custom_suggestion_location);
            comune = itemView.findViewById(R.id.item_custom_suggestion_location_province);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onSuggestionItemClick(getAdapterPosition());
        }
    }

}