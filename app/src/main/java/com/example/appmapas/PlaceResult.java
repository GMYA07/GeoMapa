package com.example.appmapas;
import com.google.gson.annotations.SerializedName;

public class PlaceResult {
    //SETTERS
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lon")
    private String lon;

    @SerializedName("type")
    private String type;

    @SerializedName("importance")
    private double importance;

    // Getters
    public String getDisplayName() { return displayName; }
    public String getLat() { return lat; }
    public String getLon() { return lon; }
    public String getType() { return type; }
    public double getImportance() { return importance; }
}
