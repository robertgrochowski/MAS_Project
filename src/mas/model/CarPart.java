package mas.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CarPart {
    private static final List<CarPart> carPartExtent = new ArrayList<>();
    private long id;

    private String name;
    private double cost;
    private Duration avgReplaceTime;
    private List<TechnicalRepair> repairs = new ArrayList<>();

    public CarPart(){
        carPartExtent.add(this);
    }

    public CarPart(String name, double cost, Duration avgReplaceTime) {
        setName(name);
        setCost(cost);
        setAvgReplaceTime(avgReplaceTime);
        carPartExtent.add(this);
    }

    // Getters
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public long getId() {
        return id;
    }

    public double getCost() {
        return cost;
    }

    public Duration getAvgReplaceTime() {
        return avgReplaceTime;
    }

    public String getName() {
        return name;
    }

    public static List<CarPart> getExtent() {
        return carPartExtent;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    public List<TechnicalRepair> getRepairs() {
        return repairs;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setCost(double cost) {
        this.cost = cost;
    }

    private void setAvgReplaceTime(Duration avgReplaceTime) {
        this.avgReplaceTime = avgReplaceTime;
    }

    private void setRepairs(List<TechnicalRepair> repairs) {
        this.repairs = repairs;
    }

    public void addRepair(TechnicalRepair technicalRepair)
    {
        if(!getRepairs().contains(technicalRepair))
        {
            getRepairs().add(technicalRepair);
            technicalRepair.addCarPart(this);
        }
    }

    @Override
    public String toString() {
        return "Czesc{" +
                "nazwa='" + getName() + '\'' +
                ", koszt=" + getCost() +
                ", szacunkowCzasWymiany=" + getAvgReplaceTime().toMinutes() + "mins" +
                '}';
    }
}
