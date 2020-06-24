package mas.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Service {
    private static final List<Service> serviceExtent = new ArrayList<>();
    private static final int MAX_TRIMMED_LENGTH = 350;
    private static Map<String, Service> services = new HashMap<>();

    private long id;

    private String catalogueNumber;
    private double basePrice;
    private boolean canHaveOnlyOneInCart;

    public Service(){
        serviceExtent.add(this);
    }

    public Service(String catalogueNumber, double basePrice) throws Exception {
        serviceExtent.add(this);
        setCanHaveOnlyOneInCart(false);
        setBasePrice(basePrice);
        setCatalogueNumber(catalogueNumber);
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public long getId(){
        return id;
    }

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

    protected void setCanHaveOnlyOneInCart(boolean can) {
        canHaveOnlyOneInCart = can;
    }

    public static int getMaxTrimmedLength() {
        return MAX_TRIMMED_LENGTH;
    }

    public static List<? extends Service> getExtent() {
        return serviceExtent;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setCatalogueNumber(String catalogueNumber) throws Exception {
        if(services.containsKey(catalogueNumber))
            throw new Exception("The object with given catalogue number already exists! (id=" + catalogueNumber + ")");

        //TODO
        //services.put(catalogueNumber, this);
        this.catalogueNumber = catalogueNumber;
    }

    public void setBasePrice(double basePrice) throws Exception {
        if(basePrice < 0)
            throw new Exception("Price can not be less than 0! (price="+ getBasePrice()+")");

        this.basePrice = basePrice;
    }

    // Abstract methods
    @Transient
    public abstract Duration getEstimatedRealizationTime();

    @Transient
    public abstract double getPrice();

    @Override
    public String toString() {
        return "Usluga{" +
                "numerKatalogowy='" + getCatalogueNumber() + '\'' +
                ", cena=" + getBasePrice() +
                '}';
    }
}