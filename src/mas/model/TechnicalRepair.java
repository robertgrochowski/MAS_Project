package mas.model;

import javax.persistence.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TechnicalRepair extends Service
{
    private static final List<TechnicalRepair> technicalRepairExtent = new ArrayList<>();
    private List<CarPart> carParts = new ArrayList<>();
    private String description;

    public TechnicalRepair() {
        technicalRepairExtent.add(this);
    }

    public TechnicalRepair(String catalogueNumber, double price, String description) throws Exception {
        super(catalogueNumber, price);
        setDescription(description);
        technicalRepairExtent.add(this);
    }

    @ManyToMany(fetch = FetchType.EAGER)
    public List<CarPart> getCarParts() {
        return carParts;
    }

    public String getDescription() {
        return description;
    }

    @Override
    @Transient
    public Duration getEstimatedRealizationTime() {
        return getCarParts().stream()
                .map(CarPart::getAvgReplaceTime)
                .reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    @Transient
    public double getPrice() {
        return getBasePrice() + getCarParts().stream()
                .map(CarPart::getCost)
                .reduce(0d, Double::sum);
    }

    public static List<TechnicalRepair> getExtent() {
        return technicalRepairExtent;
    }

    private void setCarParts(List<CarPart> carParts) {
        this.carParts = carParts;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addCarPart(CarPart part)
    {
        if (!getCarParts().contains(part))
        {
            getCarParts().add(part);
            part.addRepair(this);
        }
    }

    @Override
    public String toString() {
        return "Naprawa techniczna: " + getDescription();
    }
}
