package fr.univangers.client.service.dto;

import java.time.LocalDate;

/**
 * Classe contenant les informations d'un Lead.
 */
public class LeadDto {

    private int id;
    private String fullName;
    private Double annualRevenue;
    private String phone;
    private String street;
    private String postalCode;
    private String city;
    private String country;
    private LocalDate creationDate;
    private String company;
    private String state;
    // Coordonnées géographiques associées au lead
    private GeographicPointDto geographicPointDto;

    public LeadDto(int id, String fullName, Double annualRevenue,
            String phone, String street, String postalCode,
            String city, String country, LocalDate creationDate,
            String company, String state, GeographicPointDto geographicPointDto) {
        this.id = id;
        this.fullName = fullName;
        this.annualRevenue = annualRevenue;
        this.phone = phone;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.creationDate = creationDate;
        this.company = company;
        this.state = state;
        this.geographicPointDto = geographicPointDto;
    }

    public LeadDto() {
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public Double getAnnualRevenue() {
        return annualRevenue;
    }

    public String getPhone() {
        return phone;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public String getCompany() {
        return company;
    }

    public String getState() {
        return state;
    }

    /**
     * Retourne les coordonnées géographiques du lead.
     *
     * @return Les coordonnées géographiques.
     */
    public GeographicPointDto getGeographicPointDto() {
        return geographicPointDto;
    }

    /**
     * Retourne une représentation textuelle complète du lead.
     *
     * @return Une chaîne de caractères représentant le lead.
     */
    @Override
    public String toString() {
        return "VirtualLDto{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", annualRevenue=" + annualRevenue +
                ", phone='" + phone + '\'' +
                ", street='" + street + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", company='" + company + '\'' +
                ", state='" + state + '\'' +
                ", geographicPointDto=" + geographicPointDto +
                '}' + '\n';
    }
}
