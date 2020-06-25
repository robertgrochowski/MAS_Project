package mas.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Implemented "Usluga" class from UML diagram
 * Service is an abstract class representing single Service in our system
 *
 * @author Robert Grochowski
 * @since 1.0
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Service {
    private static final List<Service> serviceExtent = new ArrayList<>();

    // Fields
    private long id;
    private String catalogueNumber;
    private double basePrice;
    private boolean canHaveOnlyOneInCart;

    // Associations
    private List<Ticket> tickets;

    public Service(){
        serviceExtent.add(this);
    }

    public Service(String catalogueNumber, double basePrice) throws Exception {
        serviceExtent.add(this);
        setHaveOnlyOneInCart(false);
        setBasePrice(basePrice);
        setCatalogueNumber(catalogueNumber);
    }

    // Getters
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public long getId(){
        return id;
    }

    public String getCatalogueNumber() {
        return catalogueNumber;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public boolean isHaveOnlyOneInCart() {
        return canHaveOnlyOneInCart;
    }

    public static List<? extends Service> getExtent() {
        return serviceExtent;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<Ticket> getTickets() {
        return tickets;
    }

    // Setters
    public void setHaveOnlyOneInCart(boolean can) {
        canHaveOnlyOneInCart = can;
    }

    public void setId(long id) {
        this.id = id;
    }

    private void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void setCatalogueNumber(String catalogueNumber) throws Exception {
        if(getCatalogueNumber() != null &&
                serviceExtent.stream().map(Service::getCatalogueNumber).anyMatch(c->c.equals(catalogueNumber)))
            throw new Exception("The object with given catalogue number already exists! (id=" + catalogueNumber + ")");

        this.catalogueNumber = catalogueNumber;
    }

    public void setBasePrice(double basePrice) throws Exception {
        if(basePrice < 0)
            throw new Exception("Price can not be less than 0! (price="+ getBasePrice()+")");

        this.basePrice = basePrice;
    }

    // Abstract methods
    @Transient
    public abstract Duration getEstimatedRealizationTime();

    @Transient
    public abstract double getPrice();

    // Other
    public void addTicket(Ticket ticket) {
        if (!getTickets().contains(ticket)) {
            getTickets().add(ticket);

            ticket.addServiceQualif(this);
        }
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", catalogueNumber='" + catalogueNumber + '\'' +
                ", basePrice=" + basePrice +
                ", canHaveOnlyOneInCart=" + canHaveOnlyOneInCart +
                ", tickets=" + tickets +
                '}';
    }
}