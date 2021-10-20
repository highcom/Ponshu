package com.highcom.ponshu.datamodel;

public class BrandIdentifier {
    private String id;
    private String title;

    public BrandIdentifier(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
