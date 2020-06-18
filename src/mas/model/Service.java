package mas.model;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public abstract class Service {
    private static final int MAX_TRIMMED_LENGTH = 350;

    private static Map<String, Service> services = new HashMap<>();
    private String catalogueNumber;
    private double basePrice;
    private boolean canHaveOnlyOneInCart;

    public Service(String catalogueNumber, double basePrice) throws Exception {
        canHaveOnlyOneInCart = false;
        setBasePrice(basePrice);
        setCatalogueNumber(catalogueNumber);
    }

    // Getters
    public String getCatalogueNumber() {
        return catalogueNumber;
    }
    public double getBasePrice() {
        return basePrice;
    }
    public static Service getService(String name) {
        return services.get(name);
    }

    public boolean canHaveOnlyOneInCart() {
        return canHaveOnlyOneInCart;
    }

    protected void setCanHaveOnlyOneInCart(boolean can)
    {
        canHaveOnlyOneInCart = can;
    }

    // UNIQUE
    public void setCatalogueNumber(String catalogueNumber) throws Exception {
        if(services.containsKey(catalogueNumber))
            throw new Exception("The object with given catalogue number already exists! (id=" + catalogueNumber + ")");

        services.put(catalogueNumber, this);
        this.catalogueNumber = catalogueNumber;
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
                ", cena=" + getBasePrice() +
                '}';
    }
}