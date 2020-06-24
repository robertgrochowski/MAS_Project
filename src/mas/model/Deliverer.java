package mas.model;

import mas.model.enums.DrivingLicense;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Deliverer extends Worker{

    private Set<DrivingLicense> drivingLicenses = new HashSet<>();
    private List<Ticket> tickets = new ArrayList<>();

    public Deliverer(){}

    // For dynamic stereotype
    public Deliverer(Worker prevWorker) throws Exception {
        this(prevWorker.getUser(), prevWorker.getNIP(), prevWorker.getEmploymentDate(), prevWorker.getSackingDate(), prevWorker.getSalary());
    }

    public Deliverer(User user, String NIP, LocalDate employmentDate, LocalDate sackingDate, double salary) throws Exception {
        super(user, NIP, employmentDate, sackingDate, salary);
    }

    public void addTicket(Ticket ticket)
    {
        if(!getTickets().contains(ticket)) {
            getTickets().add(ticket);

            ticket.setDeliverer(this);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @ElementCollection
    public Set<DrivingLicense> getDrivingLicenses() {
        return drivingLicenses;
    }

    public void setDrivingLicenses(Set<DrivingLicense> drivingLicenses) {
        this.drivingLicenses = drivingLicenses;
    }

    @Override
    public String toString() {
        String info = "Dostarczyciel: "+getNameAndSurname()+"\n";
        info += "\tNIP:"+getNIP()+"\n";
        info += "\tHiredate:"+getEmploymentDate()+"\n";
        info += "\tKategorie prawa jazdy:"+getDrivingLicenses()+"\n";
        info += "\tZgloszenia:\n";
        for (Ticket ticket : getTickets()) {
            info += "\t\t("+ticket.getDateCreated()+"): "+ticket.getDescription()+"\n";
        }
        return info;
    }
}
