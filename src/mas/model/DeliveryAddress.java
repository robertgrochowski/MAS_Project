package mas.model;

public class DeliveryAddress {
    private String city;
    private String street;
    private String homeNumber;
    private String flatNumber;

    public DeliveryAddress(String city, String street, String homeNumber,String flatNumber) {
        this.city = city;
        this.street = street;
        this.homeNumber = homeNumber;
        this.flatNumber = flatNumber;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public String getFlatNumber() {
        return flatNumber;
    }
}
