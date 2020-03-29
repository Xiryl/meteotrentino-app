package it.chiarani.meteotrentinoapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import it.chiarani.meteotrentinoapp.R;
import it.chiarani.meteotrentinoapp.api.AvalancheModel.AvActivityComment;
import it.chiarani.meteotrentinoapp.api.AvalancheModel.AvActivityHighlight;
import it.chiarani.meteotrentinoapp.api.AvalancheModel.AvalancheModel;
import it.chiarani.meteotrentinoapp.api.AvalancheModel.SnowpackStructureComment;

public class AvalancheAdapter extends RecyclerView.Adapter<AvalancheAdapter.ViewHolder> {
    private List<AvalancheModel> mItems;
    private Context ctx;
    private String date;

    public AvalancheAdapter(List<AvalancheModel> items, String date) {
        this.mItems = items;
        this.date = date;
    }

    @NonNull
    @Override
    public AvalancheAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_avalanche, parent, false);

        return new AvalancheAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtGradoPericolo, txtTendenza, txtWindometer, txtHeight, txtDescriptionTitle, txtDescriptionSubtitle, txtMantoNevoso, txtMantoNevosoDesc;
        ImageView imgMain;
        View borderView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtGradoPericolo = itemView.findViewById(R.id.item_avalanche_grado_pericolo);
            txtTendenza = itemView.findViewById(R.id.item_avalanche_tendenza);
            txtWindometer = itemView.findViewById(R.id.item_avalanche_txt_windometer);
            txtHeight = itemView.findViewById(R.id.item_avalanche_txt_height);
            txtDescriptionSubtitle = itemView.findViewById(R.id.item_avalanche_txt_description_subtitle);
            txtDescriptionTitle = itemView.findViewById(R.id.item_avalanche_txt_description_title);
            imgMain = itemView.findViewById(R.id.item_avalanche_img_main);
            borderView = itemView.findViewById(R.id.item_avalanche_color_bar);
            txtMantoNevoso = itemView.findViewById(R.id.item_avalanche_txt_description_situazione_tipo);
            txtMantoNevosoDesc = itemView.findViewById(R.id.item_avalanche_txt_description_manto_nevoso_desc);
            ctx = itemView.getContext();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AvalancheAdapter.ViewHolder holder, int position) {
        AvalancheModel model = mItems.get(position);

        String url = "https://avalanche.report/albina_files/"+date+"/"+model.getId()+".jpg";
        Glide.with(ctx)
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(holder.imgMain);


        if(model.getForenoon().getDangerRatingBelow() != null && model.getForenoon().getDangerRatingAbove() != null) {
            String tmpUnion = model.getForenoon().getDangerRatingBelow() + ";" + model.getForenoon().getDangerRatingAbove();
            if(tmpUnion.contains("low")) {
                holder.borderView.setBackgroundColor(Color.parseColor("#CEFA86"));
                holder.txtGradoPericolo.setText("Grado Pericolo 1 - Debole");
            }
            if (tmpUnion.contains("moderate")) {
                holder.borderView.setBackgroundColor(Color.parseColor("#FBF968"));
                holder.txtGradoPericolo.setText("Grado Pericolo 2 - Moderato");
            }
        } else {
            if(model.getForenoon().getDangerRatingAbove().contains("low")) {
                holder.borderView.setBackgroundColor(Color.parseColor("#CEFA86"));
                holder.txtGradoPericolo.setText("Grado Pericolo 1 - Debole");
            }
            if (model.getForenoon().getDangerRatingAbove().contains("moderate")) {
                holder.borderView.setBackgroundColor(Color.parseColor("#FBF968"));
                holder.txtGradoPericolo.setText("Grado Pericolo 2 - Moderato");
            }
        }

        if(model.getTendency().equals("steady")) {
            holder.txtTendenza.setText("Tendenza: Pericolo valanghe stabile");
        }

        List<String> aspects = model.getForenoon().getAvalancheSituation1().getAspects();
        String tmpAspects = "";
        if(aspects != null) {
            for(String direction : aspects) {
                tmpAspects += direction + ", ";
            }
            tmpAspects.substring(0, tmpAspects.length()-2);
        }


        holder.txtWindometer.setText(tmpAspects);

        String elevationLow = model.getForenoon().getAvalancheSituation1().getElevationLow() + "";
        String elevationAbove = "";
        if (model.getForenoon().getAvalancheSituation2() != null) {
            elevationAbove = model.getForenoon().getAvalancheSituation2().getElevationLow() + "";
        }

        if(elevationAbove.isEmpty()) {
            holder.txtHeight.setText(elevationLow);
        } else {
            holder.txtHeight.setText(String.format("1: %smt, 2: %smt", elevationAbove, elevationLow));
        }

        if( model.getAvActivityHighlights() != null) {
            for(AvActivityHighlight highlight : model.getAvActivityHighlights()) {
                if(highlight.getLanguageCode().equals("it")) {
                    holder.txtDescriptionTitle.setText(highlight.getText());
                }
            }

        }

        if(model.getAvActivityComment() != null) {
            for(AvActivityComment comment : model.getAvActivityComment()) {
                if(comment.getLanguageCode().equals("it")) {
                    holder.txtDescriptionSubtitle.setText(comment.getText());
                }
            }
        }



        String sts = "";
        if(model.getDangerPattern1() != null) {
            switch(model.getDangerPattern1()) {
                case "dp2": sts += "\n-st 2: valanga per scivolamento di neve"; break;
                case "dp6": sts += "\n-st 6: Neve fresca fredda a debole coesione e vento"; break;
            }
        }

        if(model.getDangerPattern2() != null) {
            switch(model.getDangerPattern2()) {
                case "dp2": sts += "\n-st 2: valanga per scivolamento di neve"; break;
                case "dp6": sts += "\n-st 6: Neve fresca fredda a debole coesione e vento"; break;
            }
        }

        holder.txtMantoNevoso.setText("Situazione Tipo:"+sts);

        if(model.getSnowpackStructureComment() != null) {
            for (SnowpackStructureComment comment : model.getSnowpackStructureComment()) {
                if (comment.getLanguageCode().equals("it")) {
                    holder.txtMantoNevosoDesc.setText(comment.getText());
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

}
