package it.chiarani.meteotrentinoapp.api.MeteoTrentinoForecastModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import it.chiarani.meteotrentinoapp.api.MeteoReportForecastModel.MeteoReportForecast;
import it.chiarani.meteotrentinoapp.api.MeteoReportForecastModel.MeteoReportForecastEntry;

public class MeteoReportForecastMapper {
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ITALIAN);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALIAN);
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.ITALIAN);

    public static MeteoTrentinoForecast toMeteoTrentinoForecast(String location, MeteoReportForecast forecast) {
        MeteoTrentinoForecast model = new MeteoTrentinoForecast();
        Previsione previsione = new Previsione();
        previsione.setLocalita(location);
        previsione.setGiorni(buildDays(forecast));
        List<Previsione> previsioni = new ArrayList<>();
        previsioni.add(previsione);
        model.setPrevisione(previsioni);
        return model;
    }

    private static List<Giorno> buildDays(MeteoReportForecast forecast) {
        Map<String, List<Fascia>> fasceByDay = new HashMap<>();
        Map<String, List<MeteoReportForecastEntry>> entriesByDay = new HashMap<>();
        if (forecast != null && forecast.getHourly() != null) {
            for (MeteoReportForecastEntry entry : forecast.getHourly().values()) {
                String start = entry.getStart();
                String end = entry.getEnd();
                Date startDate = parseDate(start);
                Date endDate = parseDate(end);
                if (startDate == null) {
                    continue;
                }
                String dayKey = DATE_FORMAT.format(startDate);
                List<Fascia> fasce = fasceByDay.computeIfAbsent(dayKey, key -> new ArrayList<>());
                fasce.add(buildSlot(entry, startDate, endDate));
                List<MeteoReportForecastEntry> entries = entriesByDay.computeIfAbsent(dayKey, key -> new ArrayList<>());
                entries.add(entry);
            }
        }

        List<Map.Entry<String, MeteoReportForecastEntry>> dailyEntries = new ArrayList<>();
        if (forecast != null && forecast.getDaily() != null) {
            dailyEntries.addAll(forecast.getDaily().entrySet());
        }

        Collections.sort(dailyEntries, Comparator.comparing(entry -> {
            Date date = parseDate(entry.getValue().getStart());
            return date != null ? date : new Date(0);
        }));

        List<Giorno> giorni = new ArrayList<>();
        if (dailyEntries.isEmpty()) {
            List<String> sortedKeys = new ArrayList<>(entriesByDay.keySet());
            Collections.sort(sortedKeys);
            for (String dayKey : sortedKeys) {
                Giorno giorno = buildDayFromSlots(dayKey, entriesByDay.get(dayKey));
                List<Fascia> fasce = fasceByDay.getOrDefault(dayKey, new ArrayList<>());
                giorni.add(attachSlots(giorno, fasce));
            }
            return giorni;
        }
        for (Map.Entry<String, MeteoReportForecastEntry> entry : dailyEntries) {
            Giorno giorno = buildDay(entry.getValue());
            String dateKey = giorno.getGiorno();
            List<Fascia> fasce = fasceByDay.getOrDefault(dateKey, new ArrayList<>());
            giorni.add(attachSlots(giorno, fasce));
        }

        return giorni;
    }

    private static Giorno attachSlots(Giorno giorno, List<Fascia> fasce) {
        giorno.setFasce(fasce);
        return giorno;
    }

    private static Giorno buildDayFromSlots(String dayKey, List<MeteoReportForecastEntry> entries) {
        Giorno giorno = new Giorno();
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int iconId = 1;
        if (entries != null && !entries.isEmpty()) {
            MeteoReportForecastEntry first = entries.get(0);
            iconId = mapSkyConditionToIcon(first.getSkyCondition());
            for (MeteoReportForecastEntry entry : entries) {
                if (entry.getTemperatureMinimum() != null) {
                    min = Math.min(min, entry.getTemperatureMinimum());
                }
                if (entry.getTemperatureMaximum() != null) {
                    max = Math.max(max, entry.getTemperatureMaximum());
                }
            }
        }
        giorno.setGiorno(dayKey);
        giorno.setIdIcona(iconId);
        giorno.setIcona(formatIconCode(iconId));
        giorno.setDescIcona("Previsioni giornaliere");
        giorno.setTestoGiorno("Previsioni giornaliere");
        giorno.setTMinGiorno(min == Integer.MAX_VALUE ? null : min);
        giorno.setTMaxGiorno(max == Integer.MIN_VALUE ? null : max);
        return giorno;
    }

    private static Giorno buildDay(MeteoReportForecastEntry entry) {
        Giorno giorno = new Giorno();
        Date startDate = parseDate(entry.getStart());
        String day = startDate != null ? DATE_FORMAT.format(startDate) : "";
        int iconId = mapSkyConditionToIcon(entry.getSkyCondition());
        giorno.setGiorno(day);
        giorno.setIdIcona(iconId);
        giorno.setIcona(formatIconCode(iconId));
        giorno.setDescIcona(describeSkyCondition(entry.getSkyCondition()));
        giorno.setTestoGiorno(describeSkyCondition(entry.getSkyCondition()));
        giorno.setTMaxGiorno(entry.getTemperatureMaximum());
        giorno.setTMinGiorno(entry.getTemperatureMinimum());
        return giorno;
    }

    private static Fascia buildSlot(MeteoReportForecastEntry entry, Date startDate, Date endDate) {
        Fascia fascia = new Fascia();
        int iconId = mapSkyConditionToIcon(entry.getSkyCondition());
        fascia.setIcona(formatIconCode(iconId));
        fascia.setDescIcona(describeSkyCondition(entry.getSkyCondition()));
        fascia.setFasciaOre(startDate != null ? TIME_FORMAT.format(startDate) : "--");
        fascia.setFasciaPer(buildSlotRange(startDate, endDate));
        fascia.setDescPrecProb(formatPercent(entry.getRainProbability()));
        fascia.setDescPrecInten(formatMm(entry.getRainFall()));
        fascia.setDescTempProb(formatTemperatureRange(entry.getTemperatureMinimum(), entry.getTemperatureMaximum()));
        fascia.setDescVentoDirQuota(formatWindDirection(entry.getWindDirection()));
        fascia.setDescVentoIntQuota(formatWindSpeed(entry.getWindSpeed()));
        fascia.setDescVentoDirValle(formatWindDirection(entry.getWindDirection()));
        fascia.setDescVentoIntValle(formatWindSpeed(entry.getWindSpeed()));
        fascia.setZeroTermico(entry.getFreezingLevel());
        return fascia;
    }

    private static String buildSlotRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return "--";
        }
        return String.format(Locale.ITALIAN, "%s-%s", TIME_FORMAT.format(startDate), TIME_FORMAT.format(endDate));
    }

    private static String formatTemperatureRange(Integer min, Integer max) {
        if (min == null && max == null) {
            return "--";
        }
        if (min == null) {
            return String.format(Locale.ITALIAN, "Max %s°", max);
        }
        if (max == null) {
            return String.format(Locale.ITALIAN, "Min %s°", min);
        }
        return String.format(Locale.ITALIAN, "Min %s° / Max %s°", min, max);
    }

    private static String formatWindDirection(Integer direction) {
        if (direction == null) {
            return "--";
        }
        return String.format(Locale.ITALIAN, "%s°", direction);
    }

    private static String formatWindSpeed(Double speed) {
        if (speed == null) {
            return "--";
        }
        return String.format(Locale.ITALIAN, "%.1f km/h", speed);
    }

    private static String formatMm(Double value) {
        if (value == null) {
            return "--";
        }
        return String.format(Locale.ITALIAN, "%.1f mm", value);
    }

    private static String formatPercent(Integer value) {
        if (value == null) {
            return "--";
        }
        return String.valueOf(value);
    }

    private static String describeSkyCondition(String skyCondition) {
        if (skyCondition == null || skyCondition.isEmpty()) {
            return "Condizioni non disponibili";
        }
        switch (skyCondition.toUpperCase(Locale.ITALIAN)) {
            case "A": return "Sereno";
            case "B": return "Poco nuvoloso";
            case "C": return "Nuvoloso";
            case "D": return "Coperto con piogge deboli";
            case "E": return "Pioggia";
            case "F": return "Neve";
            case "G": return "Temporale";
            default: return "Condizioni variabili";
        }
    }

    private static int mapSkyConditionToIcon(String skyCondition) {
        if (skyCondition == null) {
            return 1;
        }
        switch (skyCondition.toUpperCase(Locale.ITALIAN)) {
            case "A": return 18;
            case "B": return 19;
            case "C": return 1;
            case "D": return 2;
            case "E": return 4;
            case "F": return 6;
            case "G": return 821;
            default: return 1;
        }
    }

    private static String formatIconCode(int iconId) {
        return String.format(Locale.ITALIAN, "ic_%03d_day", iconId);
    }

    private static Date parseDate(String value) {
        if (value == null) {
            return null;
        }
        try {
            return DATE_TIME_FORMAT.parse(value);
        } catch (ParseException ex) {
            return null;
        }
    }
}
