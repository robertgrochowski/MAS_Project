package mas.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
    private static double BASE_BONUS = 200;

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

    public void setSackingDate(LocalDate sackingDate) {
        this.sackingDate = sackingDate;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setNIP(String NIP) {
        this.NIP = NIP;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
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
