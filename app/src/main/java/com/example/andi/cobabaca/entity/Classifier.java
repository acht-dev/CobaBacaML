package com.example.andi.cobabaca.entity;

public class Classifier {

    private String id;
    private String title;
    private float confidence;


    public Classifier(String id, String title, float confidence) {
        this.id = id;
        this.title = title;
        this.confidence = confidence;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public float getConfidence() {
        return confidence;
    }
}
