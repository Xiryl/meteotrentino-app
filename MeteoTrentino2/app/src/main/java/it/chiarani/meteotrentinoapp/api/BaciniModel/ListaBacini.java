package it.chiarani.meteotrentinoapp.api.BaciniModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListaBacini {

        @SerializedName("bacini")
        @Expose
        private List<Bacini> bacini = null;

        public List<Bacini> getBacini() {
            return bacini;
        }

        public void setBacini(List<Bacini> bacini) {
            this.bacini = bacini;
        }
}
