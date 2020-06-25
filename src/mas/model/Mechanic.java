package mas.model;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implemented "Mechanik" class from UML diagram
 * Mechanic is a worker who repairs and cleans cars
 *
 * @author Robert Grochowski
 * @since 1.0
 */
@Entity
public class Mechanic extends Worker {

    // Fields
    private Set<String> completedCourses = new HashSet<>();

    // Associations
    private List<TicketMechanic> mechanicTickets = new ArrayList<>();

    public Mechanic(){ }

    // For dynamic stereotype
    public Mechanic(Worker prevWorker) throws Exception {
        this(prevWorker.getUser(), prevWorker.getNIP(), prevWorker.getEmploymentDate(), prevWorker.getSackingDate(), prevWorker.getSalary());
    }

    public Mechanic(User user, String NIP, LocalDate employmentDate, LocalDate sackingDate, double salary) throws Exception {
        super(user, NIP, employmentDate, sackingDate, salary);
    }

    // Getters
    @OneToMany(fetch = FetchType.EAGER)
    public List<TicketMechanic> getMechanicTickets() {
        return mechanicTickets;
    }

    @ElementCollection
    public Set<String> getCompletedCourses() {
        return completedCourses;
    }

    // Setters
    public void setMechanicTickets(List<TicketMechanic> mechanicTickets) {
        this.mechanicTickets = mechanicTickets;
    }

    public void setCompletedCourses(Set<String> completedCourses) {
        this.completedCourses = completedCourses;
    }

    // Other

    public void addCompletedCourse(String course) {
        completedCourses.add(course);
    }

    public void addTicket(TicketMechanic ticket)
    {
        if(!getMechanicTickets().contains(ticket)) {
            getMechanicTickets().add(ticket);
        }
    }

    @Override
    public String toString() {
        return "Mechanic{" +
                "completedCourses=" + completedCourses +
                ", mechanicTickets=" + mechanicTickets +
                "} " + super.toString();
    }
}
