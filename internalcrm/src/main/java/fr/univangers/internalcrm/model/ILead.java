package fr.univangers.internalcrm.model;

/**
 * TODO
 */
public class ILead {
    int ID;
    String firstName;
    String lastName;
    double annualRevenue;
    String phone;
    String street;
    String postalCode;
    String city;
    String country;
    long creationDate;
    String company;
    String state;

    public ILead(int ID, String firstName, String lastName, double annualRevenue, String phone, String street,
                 String postalCode, String city, String country, long creationDate, String company, String state){
        this.ID = ID;
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

    }

    public ILead(){}

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

    public double getAnnualRevenue() {
        return annualRevenue;
    }

    public void setAnnualRevenue(double annualRevenue) {
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

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean sameAs(ILead l){
        System.out.println("l.toString() = " + l.toString());
        System.out.println("this.toString() = " + this.toString());

        return this.getAnnualRevenue() == l.getAnnualRevenue()
                && this.getFirstName().equalsIgnoreCase(l.getFirstName())
                && this.getLastName().equalsIgnoreCase(l.getLastName())
                && this.getStreet().equalsIgnoreCase(l.getStreet())
                && this.getPostalCode().equalsIgnoreCase(l.getPostalCode())
                && this.getCountry().equalsIgnoreCase(l.getCountry())
                && this.getCompany().equalsIgnoreCase(l.getCompany())
                && this.getState().equalsIgnoreCase(l.getState());
    }

    @Override
    public String toString() {
        return "ILead{" +
                "ID=" + ID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", annualRevenue=" + annualRevenue +
                ", phone='" + phone + '\'' +
                ", street='" + street + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", creationDate=" + creationDate +
                ", company='" + company + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
