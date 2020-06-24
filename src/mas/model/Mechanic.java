package mas.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
public class Mechanic extends Worker {

    private Set<String> completedCourses = new HashSet<>();
    private List<TicketMechanic> mechanicTickets = new ArrayList<>();

    public Mechanic(){ }

    public Mechanic(User user, String NIP, LocalDate employmentDate, LocalDate sackingDate, double salary) throws Exception {
        super(user, NIP, employmentDate, sackingDate, salary);
    }

    public void addTicket(TicketMechanic ticket)
    {
        if(!getMechanicTickets().contains(ticket)) {
            getMechanicTickets().add(ticket);
        }
    }

    @OneToMany(fetch = FetchType.EAGER)
    public List<TicketMechanic> getMechanicTickets() {
        return mechanicTickets;
    }

    @ElementCollection
    public Set<String> getCompletedCourses() {
        return completedCourses;
    }

    public void setMechanicTickets(List<TicketMechanic> mechanicTickets) {
        this.mechanicTickets = mechanicTickets;
    }

    public void addCompletedCourse(String course) {
        completedCourses.add(course);
    }

    public void setCompletedCourses(Set<String> completedCourses) {
        this.completedCourses = completedCourses;
    }


    @Override
    public String toString() {
        String info = "Mechanik: "+getNameAndSurname()+"\n";
        info += "\tNIP:"+getNIP()+"\n";
        info += "\tHiredate:"+getEmploymentDate()+"\n";
        info += "\tUkonczone kursy:"+getCompletedCourses()+"\n";
        /*info += "\tZgloszenia:\n";
        for (Ticket ticket : getTickets()) {
            info += "\t\t("+ticket.getDateCreated()+"): "+ticket.getDescription()+"\n";
        }*/
        return info;
    }
}