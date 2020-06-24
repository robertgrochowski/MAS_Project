package mas.model;

import mas.model.utils.Localization;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cleaning extends Service {
    public enum CarSize {
        XS(1),
        S(1.5),
        M(2),
        L(2.5),
        XL(3);

        private final double factor;

        CarSize(double factor){
            this.factor = factor;
        }

        public double getFactor() {
            return factor;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public enum Type {
        INNER, OUTER, COMPREHENSIVE
    }

    private static final List<Cleaning> cleaningExtent = new ArrayList<>();
    private static final double BASE_PRICE = 50;
    private static final Duration BASE_TIME = Duration.ofMinutes(20);

    private CarSize carSize;
    private Type type;

    public Cleaning(){
        super();
        cleaningExtent.add(this);
    }

    public Cleaning(String catalogueNumber, CarSize carSize, Type type) throws Exception {
        super(catalogueNumber, BASE_PRICE);
        setHaveOnlyOneInCart(true);
        setCarSize(carSize);
        setType(type);
        cleaningExtent.add(this);
    }

    // Getters
    public CarSize getCarSize() {
        return carSize;
    }

    public Type getType() {
        return type;
    }

    @Override
    @Transient
    public Duration getEstimatedRealizationTime() {
        return Duration.ofMinutes((long) (BASE_TIME.toMinutes() * carSize.getFactor()) * (getType() == Type.COMPREHENSIVE ? 2 : 1));
    }

    @Override
    @Transient
    public double getPrice() {
        return BASE_PRICE * carSize.getFactor();
    }

    public static List<Cleaning> getExtent() {
        return cleaningExtent;
    }

    // Setters
    public void setCarSize(CarSize carSize) {
        this.carSize = carSize;
    }

    public void setType(Type type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "Czyszczenie: "+ Localization.getLocalizedStringType(getType())+", "+getCarSize();
    }
}
