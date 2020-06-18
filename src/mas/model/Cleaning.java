package mas.model;

import java.time.Duration;

public class Cleaning extends Service {

    private static final double BASE_PRICE = 50;
    private static final Duration BASE_TIME = Duration.ofMinutes(20);
    private CarSize carSize;
    private Type type;

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

    public Cleaning(String catalogueNumber, CarSize carSize, Type type) throws Exception {
        super(catalogueNumber, BASE_PRICE);
        setCanHaveOnlyOneInCart(true);
        this.carSize = carSize;
        this.type = type;
    }

    public CarSize getCarSize() {
        return carSize;
    }

    public Type getType() {
        return type;
    }

    @Override
    public Duration getEstimatedRealizationTime() {
        return Duration.ofMinutes((long) (BASE_TIME.toMinutes() * carSize.getFactor()) * (getType() == Type.COMPREHENSIVE ? 2 : 1));
    }

    @Override
    public double getPrice() {
        return BASE_PRICE* carSize.getFactor();
    }

    @Override
    public String toString() {
        return "Czyszczenie: "+Localization.getLocalizedStringType(getType())+", "+getCarSize();
    }
}
