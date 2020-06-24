package mas.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TiresSwap extends Service {

    public enum SeasonType {
        WINTER, SUMMER, ALLSEASON
    }

    private static final List<TiresSwap> tiresSwapExtent = new ArrayList<>();
    private static final Duration AVERAGE_REPLACE_TIME = Duration.ofMinutes(30);
    private static final double BASE_PRICE = 150;
    private static final double PLN_PER_INCH = 30;
    private static final double DISCOUNT_PER_YEAR = 20;
    private static final double MAX_YEAR_DISCOUNT = 60;

    private SeasonType seasonType;
    private int size;
    private int yearOfManufacture;

    public TiresSwap(){
        super();
        tiresSwapExtent.add(this);
    }

    public TiresSwap(String catalogueNumber, SeasonType seasonType, int size, int yearOfManufacture) throws Exception {
        super(catalogueNumber, BASE_PRICE);
        setCanHaveOnlyOneInCart(true);
        this.seasonType = seasonType;
        this.size = size;
        this.yearOfManufacture = yearOfManufacture;
        tiresSwapExtent.add(this);
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
    @Transient
    public Duration getEstimatedRealizationTime() {
        return AVERAGE_REPLACE_TIME;
    }

    @Override
    @Transient
    public double getPrice() {
        int age = LocalDate.now().getYear() - getYearOfManufacture();
        return getBasePrice() + (getSize() * PLN_PER_INCH) - Math.min(age * DISCOUNT_PER_YEAR, MAX_YEAR_DISCOUNT);
    }

    public static List<TiresSwap> getExtent() {
        return tiresSwapExtent;
    }

    public void setSeasonType(SeasonType seasonType) {
        this.seasonType = seasonType;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

    @Override
    public String toString() {
        return "Wymiana opon: " + getSize() + "'', " + Localization.getLocalizedSeasonType(getSeasonType()) + ", " + getYearOfManufacture() + "r";
    }
}
