package mas.model;

import javax.persistence.Entity;
import java.time.LocalDate;

/**
 * Implemented "Administrator" class from UML diagram
 * Administrator is a person who can manage the system
 *
 * @author Robert Grochowski
 */
@Entity
public class Administrator extends Worker {

    private long totalWorkedHours;
    private long monthWorkedHours;

    public Administrator() { }

    // For dynamic stereotype
    public Administrator(Worker prevWorker) throws Exception {
        this(prevWorker.getUser(), prevWorker.getNIP(), prevWorker.getEmploymentDate(), prevWorker.getSackingDate(), prevWorker.getSalary());
    }

    public Administrator(User user, String NIP, LocalDate employmentDate, LocalDate sackingDate, double salary) throws Exception {
        super(user, NIP, employmentDate, sackingDate, salary);
    }

    public Administrator(User user, String NIP, LocalDate employmentDate, LocalDate sackingDate, double salary, long totalWorkedHours, long monthWorkedHours) throws Exception {
        super(user, NIP, employmentDate, sackingDate, salary);
        setTotalWorkedHours(totalWorkedHours);
        setMonthWorkedHours(monthWorkedHours);
    }

    // Getters
    public long getTotalWorkedHours() {
        return totalWorkedHours;
    }

    public long getMonthWorkedHours() {
        return monthWorkedHours;
    }

    // Setters
    public void setTotalWorkedHours(long totalWorkedHours) {
        this.totalWorkedHours = totalWorkedHours;
    }

    public void setMonthWorkedHours(long monthWorkedHours) {
        this.monthWorkedHours = monthWorkedHours;
    }
}
