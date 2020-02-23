package it.chiarani.meteotrentino.utils;

import it.chiarani.meteotrentino.R;

public class IconConverter {
    public static int getIconFromId(int id) {

        switch (id) {
            case 25: return R.drawable.ic_poco_nuvoloso;
            case 19: return R.drawable.ic_nuvoloso;
            case 11: return R.drawable.ic_molto_nuvoloso;
            case 12: return R.drawable.ic_molto_nuvoloso_piogge_deboli;
            default: return R.drawable.ic_cloud;
        }
    }
}
