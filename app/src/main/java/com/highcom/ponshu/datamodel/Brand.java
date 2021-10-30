package com.highcom.ponshu.datamodel;

import java.util.ArrayList;
import java.util.List;

public class Brand {
    private String title;
    private String subtitle;
    private String specific;
    private Long polishingRate;
    private String brewery;
    private String area;
    private String rawMaterial;
    private Long capacity;
    private Long storageTemperature;
    private String howToDrink;
    private List<Long> tasteList;
    private List<Aroma> aromaList;

    public Brand(String title,
                 String subtitle,
                 String specific,
                 Long polishingRate,
                 String brewery,
                 String area,
                 String rawMaterial,
                 Long capacity,
                 Long storageTemperature,
                 String howToDrink,
                 List<Long> tasteList,
                 List<Aroma> aromaList) {
        this.title = title;
        this.subtitle = subtitle;
        this.specific = specific;
        this.polishingRate = polishingRate;
        this.brewery = brewery;
        this.area = area;
        this.rawMaterial = rawMaterial;
        this.capacity = capacity;
        this.storageTemperature = storageTemperature;
        this.howToDrink = howToDrink;
        this.tasteList = tasteList;
        this.aromaList = aromaList;
    }

    public Brand() {
        this.title = "";
        this.subtitle = "";
        this.polishingRate = 0L;
        this.aromaList = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getSpecific() {
        return specific;
    }

    public Long getPolishingRate() {
        return polishingRate;
    }

    public String getBrewery() {
        return brewery;
    }

    public String getArea() {
        return area;
    }

    public String getRawMaterial() {
        return rawMaterial;
    }

    public Long getCapacity() {
        return capacity;
    }

    public Long getStorageTemperature() {
        return storageTemperature;
    }

    public String getHowToDrink() {
        return howToDrink;
    }

    public List<Long> getTasteList() {
        return tasteList;
    }

    public List<Aroma> getAromaList() {
        return aromaList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setSpecific(String specific) {
        this.specific = specific;
    }

    public void setPolishingRate(Long polishingRate) {
        this.polishingRate = polishingRate;
    }

    public void setBrewery(String brewery) {
        this.brewery = brewery;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setRawMaterial(String rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public void setStorageTemperature(Long storageTemperature) {
        this.storageTemperature = storageTemperature;
    }

    public void setHowToDrink(String howToDrink) {
        this.howToDrink = howToDrink;
    }

    public void setTasteList(List<Long> tasteList) {
        this.tasteList = tasteList;
    }

    public void setAromaList(List<Aroma> aromaList) {
        this.aromaList = aromaList;
    }
}
