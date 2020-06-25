package mas.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implemented "Klient" class from UML diagram
 * Client is a client(customer) in our system
 *
 * @author Robert Grochowski
 * @since 1.0
 */
@Entity
public class Client {

    // Fields
    private long id;
    private LocalDateTime registrationDate;

    // Associations
    private List<Ticket> tickets = new ArrayList<>();
    private User user;

    public Client() { }

    public Client(User user, LocalDateTime registrationDate) throws Exception {
        if(user == null) {
            throw new Exception("The whole (user) does not exists!");
        }
        setUser(user);
        getUser().setClient(this);
        setRegistrationDate(registrationDate);
    }

    // Getters
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public long getId() {
        return id;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    @OneToMany(fetch = FetchType.EAGER)
    public List<Ticket> getTickets() {
        return tickets;
    }

    @OneToOne(fetch = FetchType.EAGER)
    public User getUser() {
        return user;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    // Other
    public void addTicket(Ticket ticket) {
        if (!getTickets().contains(ticket)) {
            getTickets().add(ticket);

            ticket.setClient(this);
        }
    }
}
