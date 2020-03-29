package it.chiarani.meteotrentinoapp.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateFormatter extends ValueFormatter {
    @Override
    public String getPointLabel(Entry entry) {
        return "X";
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:ss");
        String dateString = formatter.format(new Date((long)value));
        return dateString;
    }
}
