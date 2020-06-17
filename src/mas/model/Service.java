package mas.model;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public abstract class Service {
    private static final int MAX_TRIMMED_LENGTH = 350;

    private static Map<String, Service> services = new HashMap<>();
    private String catalogueNumber;
    private String description;
    private double basePrice;

    public Service(String catalogueNumber, String description, double basePrice) throws Exception {
        setDescription(description);
        setBasePrice(basePrice);
        setCatalogueNumber(catalogueNumber);
    }

    // Getters
    public String getCatalogueNumber() {
        return catalogueNumber;
    }
    public String getDescription() {
        return description;
    }
    public double getBasePrice() {
        return basePrice;
    }
    public static Service getService(String name) {
        return services.get(name);
    }

    // UNIQUE
    public void setCatalogueNumber(String catalogueNumber) throws Exception {
        if(services.containsKey(catalogueNumber))
            throw new Exception("The object with given catalogue nubmber already exists! (id=" + catalogueNumber + ")");

        services.put(catalogueNumber, this);
        this.catalogueNumber = catalogueNumber;
    }

    public void setDescription(String description) throws Exception {
        String trimmed = description.trim();
        if(trimmed.length() < 1 || trimmed.length() > MAX_TRIMMED_LENGTH)
            throw new Exception("Trimmed description should have from 0 to "+MAX_TRIMMED_LENGTH+" characters! (trimmed length="+trimmed.length()+")");

        this.description = description;
    }

    public void setBasePrice(double basePrice) throws Exception {
        if(basePrice < 0)
            throw new Exception("Price can not be less than 0! (price="+ getBasePrice()+")");

        this.basePrice = basePrice;
    }


    public static int getMaxTrimmedLength() {
        return MAX_TRIMMED_LENGTH;
    }

    public abstract Duration getEstimatedRealizationTime();
    public abstract double getPrice();

    @Override
    public String toString() {
        return "Usluga{" +
                "numerKatalogowy='" + getCatalogueNumber() + '\'' +
                ", opis='" + getDescription() + '\'' +
                ", cena=" + getBasePrice() +
                '}';
    }
}