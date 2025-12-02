package fr.univangers.model;

/**
 * DTO représentant un point géographique (latitude, longitude).
 */
public class GeographicPointDto {
    private Double latitude;
    private Double longitude;

    /**
     * Constructeur.
     *
     * @param latitude  Latitude.
     * @param longitude Longitude.
     */
    public GeographicPointDto(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
