package mas.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Implemented "Adres" class from UML diagram
 * DeliveryAddress is a class representing delivery address
 * associated with Ticket (embedded class)
 * @see Ticket
 *
 * @author Robert Grochowski
 * @since 1.0
 */
@Embeddable
public class DeliveryAddress {

    // Fields
    private String city;
    private String street;
    private String homeNumber;
    private String flatNumber;

    public DeliveryAddress(){}

    public DeliveryAddress(String city, String street, String homeNumber, String flatNumber) {
        setCity(city);
        setStreet(street);
        setHomeNumber(homeNumber);
        setFlatNumber(flatNumber);
    }

    // Getters
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

    // Setters
    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }
}
