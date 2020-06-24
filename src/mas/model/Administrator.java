package mas.model;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class Administrator extends Worker {
    private long totalWorkedHours;
    private long monthWorkedHours;

    public Administrator() { }

    public Administrator(User user, String NIP, LocalDate employmentDate, LocalDate sackingDate, double salary) throws Exception {
        super(user, NIP, employmentDate, sackingDate, salary);
    }

    public Administrator(User user, String NIP, LocalDate employmentDate, LocalDate sackingDate, double salary, long totalWorkedHours, long monthWorkedHours) throws Exception {
        super(user, NIP, employmentDate, sackingDate, salary);
        this.totalWorkedHours = totalWorkedHours;
        this.monthWorkedHours = monthWorkedHours;
    }

    public long getTotalWorkedHours() {
        return totalWorkedHours;
    }

    public long getMonthWorkedHours() {
        return monthWorkedHours;
    }

    public void setTotalWorkedHours(long totalWorkedHours) {
        this.totalWorkedHours = totalWorkedHours;
    }

    public void setMonthWorkedHours(long monthWorkedHours) {
        this.monthWorkedHours = monthWorkedHours;
    }
}
