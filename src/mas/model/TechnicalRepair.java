package mas.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TechnicalRepair extends Service
{
    private List<CarPart> carParts = new ArrayList<>();

    public TechnicalRepair(String catalogueNumber, String description, double price, List<CarPart> carParts) throws Exception {
        super(catalogueNumber, description, price);
        setCarParts(carParts);
    }

    public List<CarPart> getCarParts() {
        return carParts;
    }

    private void setCarParts(List<CarPart> carParts) {
        this.carParts = carParts;
    }

    @Override
    public Duration getEstimatedRealizationTime() {
        return getCarParts().stream()
                .map(CarPart::getAvgReplaceTime)
                .reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    public double getPrice() {
        return getBasePrice()+200;//todo
    }

    @Override
    public String toString() {
        return "Naprawa techniczna: " + getDescription();
    }
}
