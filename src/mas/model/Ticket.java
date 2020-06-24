package mas.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ticket {
    private long id;

    private String description;
    private LocalDate dateCreated;
    private LocalDate dateFinished;

    private List<Service> services = new ArrayList<>();
    private List<TicketMechanic> ticketMechanics = new ArrayList<>();
    private Deliverer deliverer;
    private Client client;
    private DeliveryAddress deliveryAddress;
    private Opinion opinion;

    public Ticket(){}

    public Ticket(String description, LocalDate dateCreated) {
        this(description, dateCreated, null);
    }

    public Ticket(String description, LocalDate dateCreated, LocalDate dateFinished) {
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateFinished = dateFinished;
    }

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

    @ManyToMany(fetch = FetchType.EAGER)
    public List<TicketMechanic> getTicketMechanics() {
        return ticketMechanics;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public Deliverer getDeliverer(){
        return deliverer;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public Client getClient() {
        return client;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    public List<Service> getServices() {
        return services;
    }

    @Embedded
    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    @OneToOne
    public Opinion getOpinion() {
        return opinion;
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
        deliverer.addTicket(this);
    }

    public void setClient(Client client) {
        this.client = client;
        client.addTicket(this);
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public void setTicketMechanics(List<TicketMechanic> ticketMechanics) {
        this.ticketMechanics = ticketMechanics;
    }

    public void addMechanic(TicketMechanic mechanic)
    {
        if(!getTicketMechanics().contains(mechanic)) {
            getTicketMechanics().add(mechanic);
        }
    }

    @Override
    public String toString() {
        String info = "Zgloszenie\n";
        info += "\tOpis: "+getDescription()+"\n";
        info += "\tData utworzenia:"+getDateCreated()+"\n";
        info += "\tData ukonczenia:"+ (getDateFinished() != null ? getDateFinished() : "brak")+"\n";

        /*info += "\tMechanicy:\n";
        for (Mechanic mechanic : getMechanics()) {
            info += "\t\t" + mechanic.getName() + " " + mechanic.getSurname()+ "\n";
        }*/

        info += "\tDostarczyciel: "+ getDeliverer().getNameAndSurname();
        return info;
    }
}
