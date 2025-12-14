package fr.univangers.client.service.dto;

/**
 * DTO représentant un point géographique à l’aide de coordonnées GPS
 */
public class GeographicPointDto {
    private Double latitude;
    private Double longitude;

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
