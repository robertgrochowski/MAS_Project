package mas.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Client {
    private long id;

    private LocalDateTime registrationDate;
    private List<Ticket> tickets = new ArrayList<>();
    private User user;

    public Client() {
    }

    public Client(User user, LocalDateTime registrationDate) throws Exception {
        if(user == null) {
            throw new Exception("The whole (user) does not exists!");
        }
        this.user = user;
        this.user.setClient(this);
        this.registrationDate = registrationDate;
    }

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

    public void addTicket(Ticket ticket) {
        if (!getTickets().contains(ticket)) {
            getTickets().add(ticket);

            ticket.setClient(this);
        }
    }

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
}
