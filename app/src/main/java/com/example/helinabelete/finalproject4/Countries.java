package com.example.helinabelete.finalproject4;

import java.io.Serializable;
import com.example.helinabelete.finalproject4.QuizViewModel;

public class Countries implements Serializable {
    private QuizViewModel quizViewModel;
    private QuizFragment quizFragment;
    private String country;
    private String url;
    private String nativeName;
    private String region;
    private String currency;
    private String name;  //this comes from inner jason object called "source"
    private String title;
    private boolean isSelected;
    private double lat;
    private double  lon;
    //String thumbnail = null;
    //int averageRating = 0;
    //double amount = 0;
    //String currencyCode = null;

    public Countries(String country, String url, String nativeName ,String region, String currency,
                     double lat, double lon, boolean isSelected) {
        this.country=country;
        this.url=url;
        this.nativeName=nativeName;
        this.region=region;
        this.currency=currency;
        this.isSelected = isSelected;
        this.lat = lat;
        this.lon = lon;
    }

    public Countries() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() { return title;}

    public void setTitle(String title) { this.title = title; }

    public double getLat() {
        return lat;
    }

    public void setLat(double type) {
        this.lat = type;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double type) {
        this.lon = type;}

    public String getCountry() {
        for (String str : quizFragment.countryList) {
            country = str.substring(str.indexOf('-') + 1).replace('_', ' ');
        }
        return country;
        }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public boolean isSelected() {

        return isSelected;
    }
    public void setSelected(boolean selected) {

        isSelected = selected;
    }


}


