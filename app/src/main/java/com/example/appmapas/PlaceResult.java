package com.example.appmapas;

import com.google.gson.annotations.SerializedName;

public class PlaceResult {

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

    @SerializedName("address")
    private Address address;

    // Clase interna para la dirección
    public static class Address {

        @SerializedName("country")
        private String country;

        @SerializedName("country_code")
        private String countryCode;

        @SerializedName("state")
        private String state;

        @SerializedName("city")
        private String city;

        @SerializedName("town")
        private String town;

        @SerializedName("municipality")
        private String municipality;

        @SerializedName("postcode")
        private String postcode;

        // Getters
        public String getCountry() { return country; }
        public String getCountryCode() { return countryCode; }
        public String getState() { return state; }
        public String getPostcode() { return postcode; }

        // Ciudad con fallback por si viene vacío
        public String getCity() {
            if (city != null) return city;
            if (town != null) return town;
            if (municipality != null) return municipality;
            return "Desconocida";
        }
    }

    // Getters
    public String getDisplayName() { return displayName; }
    public String getLat() { return lat; }
    public String getLon() { return lon; }
    public String getType() { return type; }
    public double getImportance() { return importance; }
    public Address getAddress() { return address; }

    // Metodo de utilidad para obtener coordenadas como double
    public double getLatAsDouble() {
        try { return Double.parseDouble(lat); }
        catch (Exception e) { return 0.0; }
    }

    public double getLonAsDouble() {
        try { return Double.parseDouble(lon); }
        catch (Exception e) { return 0.0; }
    }

}