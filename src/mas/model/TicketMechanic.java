package mas.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Implemented "NaprawaMechanik" class from UML diagram
 * TicketMechanic is a class with attribute connecting the following classes
 * Mechanic and Ticket
 * @see Ticket
 * @see Mechanic
 *
 * @author Robert Grochowski
 * @since 1.0
 */
@Entity
public class TicketMechanic {

    // Fields
    private long id;
    private String workDescription;

    // Associations
    private Mechanic mechanic;
    private Ticket ticket;


    public TicketMechanic() { }

    public TicketMechanic(Mechanic mechanic, Ticket ticket) {
        setMechanic(mechanic);
        setTicket(ticket);
    }

    // Getters
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public long getId() {
        return id;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Mechanic getMechanic() {
        return mechanic;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Ticket getTicket() {
        return ticket;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }
}
