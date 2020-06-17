package mas.model;

import java.time.Duration;

public class CarPart {
    private String name;
    private double cost;
    private Duration avgReplaceTime;

    public CarPart(String name, double cost, Duration avgReplaceTime) {
        setName(name);
        setCost(cost);
        setAvgReplaceTime(avgReplaceTime);
    }

    // Getters
    public double getCost() {
        return cost;
    }

    public Duration getAvgReplaceTime() {
        return avgReplaceTime;
    }

    public String getName() {
        return name;
    }

    // Setters
    private void setName(String name) {
        this.name = name;
    }

    private void setCost(double cost) {
        this.cost = cost;
    }

    private void setAvgReplaceTime(Duration avgReplaceTime) {
        this.avgReplaceTime = avgReplaceTime;
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
