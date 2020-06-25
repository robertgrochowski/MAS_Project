package mas.model;

import mas.model.enums.TicketStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implemented "Zgloszenie" class from UML diagram
 * Ticket is a class representing an commission made by Client
 *
 * @author Robert Grochowski
 * @since 1.0
 */
@Entity
public class Ticket {

    // Fields
    private long id;
    private String description;
    private LocalDate dateCreated;
    private LocalDate dateFinished;
    private DeliveryAddress deliveryAddress;

    // Associations
    private List<Service> services = new ArrayList<>();
    private Map<String, Service> servicesQualif = new HashMap<>();
    private List<TicketMechanic> ticketMechanics = new ArrayList<>();
    private Deliverer deliverer;
    private Client client;
    private Opinion opinion;
    private TicketStatus status;

    public Ticket(){}

    public Ticket(LocalDate dateCreated, LocalDate dateFinished, DeliveryAddress address) {
        setDateCreated(dateCreated);
        setDateFinished(dateFinished);
        setDeliveryAddress(address);
    }

    // Getters
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public LocalDate getDateFinished() {
        return dateFinished;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<TicketMechanic> getTicketMechanics() {
        return ticketMechanics;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Deliverer getDeliverer(){
        return deliverer;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Client getClient() {
        return client;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<Service> getServices() {
        return services;
    }

    @Embedded
    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public Opinion getOpinion() {
        return opinion;
    }

    @Enumerated
    public TicketStatus getStatus() {
        return status;
    }

    // Setters
    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public void setOpinion(Opinion opinion) throws Exception {
        if(this.opinion != null) {
            throw new Exception("Opinion already set!");
        }
        this.opinion = opinion;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateFinished(LocalDate dateFinished) {
        this.dateFinished = dateFinished;
    }

    public void setDeliverer(Deliverer deliverer) {
        this.deliverer = deliverer;
        if(deliverer != null)
            deliverer.addTicket(this);
    }

    public void setClient(Client client) {
        this.client = client;
        if(client != null)
            client.addTicket(this);
    }

    private void setServices(List<Service> services) {
        this.services = services;
    }

    private void setTicketMechanics(List<TicketMechanic> ticketMechanics) {
        this.ticketMechanics = ticketMechanics;
    }

    // Other
    public void addMechanic(TicketMechanic mechanic)
    {
        if(!getTicketMechanics().contains(mechanic)) {
            getTicketMechanics().add(mechanic);
        }
    }

    public Service findServiceQualif(String id) throws Exception {
        if (servicesQualif.containsKey(id)) {
            return servicesQualif.get(id);
        }

        throw new Exception("No qualified assosiation (Ticket -> Service) by qualification of ["+id+"] found");
    }

    public void addServiceQualif(Service service) {
        if (!servicesQualif.containsKey(service.getCatalogueNumber())) {
            servicesQualif.put(service.getCatalogueNumber(), service);
            if(!getServices().contains(service)) {
                services.add(service);
            }
            service.addTicket(this);
        }
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateFinished=" + dateFinished +
                ", deliveryAddress=" + deliveryAddress +
                ", services=" + services +
                ", servicesQualif=" + servicesQualif +
                ", ticketMechanics=" + ticketMechanics +
                ", deliverer=" + deliverer +
                ", client=" + client +
                ", opinion=" + opinion +
                ", status=" + status +
                '}';
    }
}
