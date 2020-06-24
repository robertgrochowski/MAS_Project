package mas.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class TicketMechanic {
    private long id;

    private Mechanic mechanic;
    private Ticket ticket;
    private String workDescription;

    public TicketMechanic() { }

    public TicketMechanic(Mechanic mechanic, Ticket ticket) {
        setMechanic(mechanic);
        setTicket(ticket);
    }

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
