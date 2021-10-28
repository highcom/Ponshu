package com.highcom.ponshu.datamodel;

import java.util.ArrayList;
import java.util.List;

public class Brand {
    private String title;
    private String subtitle;
    private Long polishingRate;
    private List<Aroma> aromaList;
    public Brand(String title,
                 String subtitle,
                 Long polisingRate,
                 List<Aroma> aromaList) {
        this.title = title;
        this.subtitle = subtitle;
        this.polishingRate = polisingRate;
        this.aromaList = aromaList;
    }
    // TODO:引数変える
//    public Brand(String title,
//                 String subtitle,
//                 String specific,
//                 Long polisingRate,
//                 String brewery,
//                 String area,
//                 String rawMaterial,
//                 Long capacity,
//                 Long storageTemparture,
//                 String howToDrink,
//                 List<Long> taste,
//                 List<Aroma> aromaList) {
//    }

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

    public Long getPolishingRate() {
        return polishingRate;
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

    public void setPolishingRate(Long polishingRate) {
        this.polishingRate = polishingRate;
    }

    public void setAromaList(List<Aroma> aromaList) {
        this.aromaList = aromaList;
    }
}
