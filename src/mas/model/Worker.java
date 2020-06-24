package mas.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Worker {
    protected static double BASE_BONUS = 200;

    private long id;

    private String NIP;
    private LocalDate employmentDate;
    private LocalDate sackingDate;
    private double salary;

    private User user;

    public Worker(){}

    public Worker(User user, String NIP, LocalDate employmentDate, LocalDate sackingDate, double salary) throws Exception {
        if(user == null) {
            throw new Exception("The whole (user) does not exists!");
        }
        this.user = user;
        this.NIP = NIP;
        this.employmentDate = employmentDate;
        this.sackingDate = sackingDate;
        this.salary = salary;
        this.user.setWorker(this);
    }

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

    @OneToOne
    public User getUser() {
        return user;
    }

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

    @Transient
    public String getNameAndSurname() {
        return user.getName() + " " + user.getSurname();
    }

    @Override
    public String toString() {
        return "Pracownik{" +
                "NIP='" + NIP + '\'' +
                ", employmentDate=" + employmentDate +
                ", sackingDate=" + (sackingDate==null?"brak":sackingDate.toString()) +
                ", salary=" + salary +
                "} " + super.toString();
    }
}
