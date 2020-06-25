package mas.model;

import mas.model.enums.DrivingLicense;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implemented "Dostarczyciel" class from UML diagram
 * Deliverer is a person who provides car to pick up point
 *
 * @author Robert Grochowski
 * @since 1.0
 */
@Entity
public class Deliverer extends Worker{

    // Fields
    private Set<DrivingLicense> drivingLicenses = new HashSet<>();

    // Associations
    private List<Ticket> tickets = new ArrayList<>();

    public Deliverer(){}

    // For dynamic stereotype
    public Deliverer(Worker prevWorker) throws Exception {
        this(prevWorker.getUser(), prevWorker.getNIP(), prevWorker.getEmploymentDate(), prevWorker.getSackingDate(), prevWorker.getSalary());
    }

    public Deliverer(User user, String NIP, LocalDate employmentDate, LocalDate sackingDate, double salary) throws Exception {
        super(user, NIP, employmentDate, sackingDate, salary);
    }

    // Getters
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<Ticket> getTickets() {
        return tickets;
    }

    @ElementCollection
    public Set<DrivingLicense> getDrivingLicenses() {
        return drivingLicenses;
    }

    // Setters
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void setDrivingLicenses(Set<DrivingLicense> drivingLicenses) {
        this.drivingLicenses = drivingLicenses;
    }

    // Other
    public void addTicket(Ticket ticket) {
        if(!getTickets().contains(ticket)) {
            getTickets().add(ticket);

            ticket.setDeliverer(this);
        }
    }

    @Override
    public String toString() {
        return "Deliverer{" +
                "drivingLicenses=" + drivingLicenses +
                ", tickets=" + tickets +
                "} " + super.toString();
    }
}
