package fr.univangers.model;

import java.time.LocalDate;

/**
 * DTO représentant un lead virtuel (unifié).
 */
public class VirtualLeadDto {

    private String id;
    private String firstName;
    private String lastName;
    private Double annualRevenue;
    private String phone;
    private String street;
    private String postalCode;
    private String city;
    private String country;
    private LocalDate creationDate;
    private String company;
    private String state;
    private GeographicPointDto geographicPointDto;

    public VirtualLeadDto(String id, String firstName, String lastName, Double annualRevenue,
            String phone, String street, String postalCode,
            String city, String country, LocalDate creationDate,
            String company, String state, GeographicPointDto geographicPointDto) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
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

    /**
     * Constructeur par défaut.
     */
    public VirtualLeadDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getAnnualRevenue() {
        return annualRevenue;
    }

    public void setAnnualRevenue(Double annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    } // TODO : régler problème de retour entre long et String

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public GeographicPointDto getGeographicPointDto() {
        return geographicPointDto;
    }

    public void setGeographicPointDto(GeographicPointDto geographicPointDto) {
        this.geographicPointDto = geographicPointDto;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "VirtualLDto{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", annualRevenue=" + annualRevenue +
                ", phone='" + phone + '\'' +
                ", street='" + street + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", company='" + company + '\'' +
                ", state='" + state + '\'' +
                // ", geographicPointDto=" + geographicPointDto + TODO
                '}' + '\n';
    }
}
