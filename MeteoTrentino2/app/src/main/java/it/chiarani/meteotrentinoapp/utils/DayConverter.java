package it.chiarani.meteotrentinoapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DayConverter {
    public static String ExtractDayFromDate(String date) {
        Date tmpDate;
        try {
            tmpDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(tmpDate);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);


            switch (dayOfWeek){
                case 1: return "Domenica";
                case 2: return "Lunedì";
                case 3: return "Martedì";
                case 4: return "Mercoledì";
                case 5: return "Giovedì";
                case 6: return "Venerdì";
                case 7: return "Sabato";
                default: return "";
            }
        }
        catch (ParseException ex) {
            return "";
        }
    }

    public static String convertDate(String date) {
        Date tmpDate;
        try {
            tmpDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(tmpDate);
            return String.format("%s/%s/%s",  c.get(Calendar.DAY_OF_MONTH),  c.get(Calendar.MONTH),  c.get(Calendar.YEAR));
        }
        catch (ParseException ex) {
            return "";
        }
    }
}
