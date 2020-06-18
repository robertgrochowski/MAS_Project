package mas.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TechnicalRepair extends Service
{
    private List<CarPart> carParts = new ArrayList<>();
    private String description;

    public TechnicalRepair(String catalogueNumber, double price, String description, List<CarPart> carParts) throws Exception {
        super(catalogueNumber, price);
        setCarParts(carParts);
        this.description = description;
    }

    public List<CarPart> getCarParts() {
        return carParts;
    }

    private void setCarParts(List<CarPart> carParts) {
        this.carParts = carParts;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Duration getEstimatedRealizationTime() {
        return getCarParts().stream()
                .map(CarPart::getAvgReplaceTime)
                .reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    public double getPrice() {
        return getBasePrice() + getCarParts().stream()
                .map(CarPart::getCost)
                .reduce(0d, Double::sum);
    }

    @Override
    public String toString() {
        return "Naprawa techniczna: " + getDescription();
    }
}
