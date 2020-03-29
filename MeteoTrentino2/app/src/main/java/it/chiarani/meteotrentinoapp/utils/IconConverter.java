package it.chiarani.meteotrentinoapp.utils;

import java.util.ArrayList;
import java.util.Arrays;

import it.chiarani.meteotrentinoapp.R;

public class IconConverter {
    // + 100 notte, + 800 temporale
    private final static ArrayList<Integer> nuvoloso = new ArrayList<>(Arrays.asList(1, 11));
    private final static ArrayList<Integer> poca_pioggia = new ArrayList<>(Arrays.asList(2,3));
    private final static ArrayList<Integer> pioggia = new ArrayList<>(Arrays.asList(4, 5, 10, 54));
    private final static ArrayList<Integer> neve = new ArrayList<>(Arrays.asList(6, 7, 8, 9));
    private final static ArrayList<Integer> nuvoloso_w_sole = new ArrayList<>(Arrays.asList(19, 25, 26));
    private final static ArrayList<Integer> sole_pioggia_poca = new ArrayList<>(Arrays.asList(12,13, 20, 21));
    private final static ArrayList<Integer> sole_neve = new ArrayList<>(Arrays.asList(14,15, 16, 17, 22, 23, 24, 52, 53));
    private final static ArrayList<Integer> sole_nebbia = new ArrayList<>(Arrays.asList(55));
    private final static ArrayList<Integer> sole = new ArrayList<>(Arrays.asList(18));
    private final static ArrayList<Integer> luna = new ArrayList<>(Arrays.asList(118));
    private final static ArrayList<Integer> temporale = new ArrayList<Integer>(Arrays.asList(821));
    private final static ArrayList<Integer> luna_nuvoloso = new ArrayList<>(Arrays.asList(111, 119, 125, 126, 111, 113, 112, 120, 121, 101));
    private final static ArrayList<Integer> luna_pioggia = new ArrayList<>(Arrays.asList(104, 105, 110, 154, 102, 103));

    public static int getAnimResFromId(int id) {
        if(nuvoloso.contains(id)) {
            return R.raw.anim_nuvoloso;
        }
        else if(poca_pioggia.contains(id)) {
            return R.raw.anim_poca_pioggia;
        }
        else if(pioggia.contains(id)) {
            return R.raw.anim_pioggia;
        }
        else if(neve.contains(id)) {
            return R.raw.anim_neve;
        }
        else if(nuvoloso_w_sole.contains(id)) {
            return R.raw.anim_sole_nuvole;
        }
        else if(sole_pioggia_poca.contains(id)) {
            return R.raw.anim_sole_pioggia;
        }
        else if(sole_neve.contains(id)) {
            return R.raw.anim_sole_neve;
        }
        else if(sole_nebbia.contains(id)) {
            return R.raw.sole_nebbia;
        }
        else if(sole.contains(id)) {
            return R.raw.anim_sole;
        }
        else if(luna.contains(id)) {
            return R.raw.anim_luna;
        }
        else if(temporale.contains(id)) {
            return R.raw.anim_temporale;
        }
        else if(luna_nuvoloso.contains(id)) {
            return R.raw.anim_luna_nuvoloso;
        }
        else if(luna_pioggia.contains(id)) {
            return R.raw.anim_luna_pioggia;
        }
        else {
            return R.raw.anim_sole_nuvole;
        }
    }

    public static int getDrawableFromId(int id) {
        if(nuvoloso.contains(id)) {
            return R.drawable.ic_nuvoloso;
        }
        else if(poca_pioggia.contains(id)) {
            return R.drawable.ic_nuvoloso_poca_pioggia;
        }
        else if(pioggia.contains(id)) {
            return R.drawable.ic_pioggia;
        }
        else if(neve.contains(id)) {
            return R.drawable.ic_neve;
        }
        else if(nuvoloso_w_sole.contains(id)) {
            return R.drawable.ic_nuvoloso_sole;
        }
        else if(sole_pioggia_poca.contains(id)) {
            return R.drawable.ic_nuvoloso_poca_pioggia;
        }
        else if(sole_neve.contains(id)) {
            return R.drawable.ic_nuvoloso_sole_neve;
        }
        else if(sole_nebbia.contains(id)) {
            return R.drawable.ic_nuvoloso_sole;
        }
        else if(sole.contains(id)) {
            return R.drawable.ic_sole;
        }
        else if(luna.contains(id)) {
            return R.drawable.ic_luna;
        }
        else if(temporale.contains(id)) {
            return R.drawable.ic_temporale;
        }
        else if(luna_nuvoloso.contains(id)) {
            return R.drawable.ic_luna_nuvoloso;
        }
        else if(luna_pioggia.contains(id)) {
            return R.drawable.ic_luna_pioggia;
        }
        else {
            return R.drawable.ic_nuvoloso_sole;
        }
    }
}
