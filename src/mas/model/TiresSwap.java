package mas.model;

import java.time.Duration;
import java.time.LocalDate;

public class TiresSwap extends Service {

    public enum SeasonType {
        WINTER, SUMMER, ALLSEASON
    }

    private static final Duration AVERAGE_REPLACE_TIME = Duration.ofMinutes(30);
    private static final double BASE_PRICE = 150;
    private static final double PLN_PER_INCH = 30;
    private static final double DISCOUNT_PER_YEAR = 20;
    private static final double MAX_YEAR_DISCOUNT = 60;

    private SeasonType seasonType;
    private int size;
    private int yearOfManufacture;

    public TiresSwap(String catalogueNumber, SeasonType seasonType, int size, int yearOfManufacture) throws Exception {
        super(catalogueNumber, BASE_PRICE);
        setCanHaveOnlyOneInCart(true);
        this.seasonType = seasonType;
        this.size = size;
        this.yearOfManufacture = yearOfManufacture;
    }

    public SeasonType getSeasonType() {
        return seasonType;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getYearOfManufacture() {
        return yearOfManufacture;
    }

    @Override
    public Duration getEstimatedRealizationTime() {
        return AVERAGE_REPLACE_TIME;
    }

    @Override
    public double getPrice() {
        int age = LocalDate.now().getYear() - getYearOfManufacture();
        return getBasePrice() + (getSize() * PLN_PER_INCH) - Math.min(age * DISCOUNT_PER_YEAR, MAX_YEAR_DISCOUNT);
    }

    @Override
    public String toString() {
        return "Wymiana opon: " + getSize() + "'', " + Localization.getLocalizedSeasonType(getSeasonType()) + ", " + getYearOfManufacture() + "r";
    }
}
