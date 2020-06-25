package mas.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;

/**
 * Implemented "Worker" class from UML diagram
 * Worker represents an employee in our system.
 *
 * @author Robert Grochowski
 * @since 1.0
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Worker {
    private static final double BASE_BONUS = 200;

    // Fields
    private long id;
    private String NIP;
    private LocalDate employmentDate;
    private LocalDate sackingDate;
    private double salary;

    // Associations
    private User user;

    public Worker(){}
    public Worker(User user, String NIP, LocalDate employmentDate, LocalDate sackingDate, double salary) throws Exception {
        if(user == null) {
            throw new Exception("The whole (user) does not exists!");
        }
        setUser(user);
        setNIP(NIP);
        setEmploymentDate(employmentDate);
        setSackingDate(sackingDate);
        setSalary(salary);
        getUser().setWorker(this);
    }

    // Getters
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public long getId() {
        return id;
    }

    public String getNIP() {
        return NIP;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public LocalDate getSackingDate() {
        return sackingDate;
    }

    public double getSalary() {
        return salary;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public User getUser() {
        return user;
    }

    @Transient
    public String getNameAndSurname() {
        return user.getName() + " " + user.getSurname();
    }

    public static double getBaseBonus() {
        return BASE_BONUS;
    }

    // Setters
    public void setUser(User user) {
        this.user = user;
    }

    public void setSackingDate(LocalDate sackingDate) throws Exception {
        if(sackingDate != null && getEmploymentDate() != null && Duration.between(getEmploymentDate(), sackingDate).toDays() < 0) {
            throw new Exception("Sacking date cannot be before employment! " +
                    "(employment=" + getEmploymentDate() + " sacking="+sackingDate+")");
        }
        this.sackingDate = sackingDate;
    }

    public void setSalary(double salary) throws Exception {
        if(salary < 0) {
            throw new Exception("Salary cannot be less than 0! (" + salary + " given)");
        }
        this.salary = salary;
    }

    public void setNIP(String NIP) {
        this.NIP = NIP;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEmploymentDate(LocalDate employmentDate) throws Exception {
        if(getSackingDate() != null && employmentDate != null && Duration.between(employmentDate, getEmploymentDate()).toDays() < 0) {
            throw new Exception("Employment date cannot be after sacking date! " +
                    "(employment=" + employmentDate + " sacking="+getSackingDate()+")");
        }
        this.employmentDate = employmentDate;
    }

    // Other
    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", NIP='" + NIP + '\'' +
                ", employmentDate=" + employmentDate +
                ", sackingDate=" + sackingDate +
                ", salary=" + salary +
                ", user=" + user +
                '}';
    }
}
