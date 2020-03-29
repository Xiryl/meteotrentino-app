package it.chiarani.meteotrentinoapp.models;

public class AllertItem {
    private String name, day, month, year, link;

    public AllertItem(String name, String day, String month, String year, String link) {
        this.name = name;
        this.day = day;
        this.month = month;
        this.year = year;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
