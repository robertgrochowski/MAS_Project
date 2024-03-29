package mas.model.utils;

import mas.model.Cleaning;
import mas.model.TiresSwap;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Class representing translation for in-code stings.
 *
 * @since 1.0
 * @author Robert Grochowski
 */
public class Localization {

    public static final String CURRENCY_FORMAT = "%.2fzł";
    public static final String DURATION_FORMAT = "%dh %dmin";
    public final static String ESTIMATED_REALIZATION_FORMAT = "Szacowany czas realizacji: "+ DURATION_FORMAT;
    public final static String TOTAL_PRICE_FORMAT = "SUMA: %.2f PLN";
    public final static String CHOSEN_SERVICES_FORMAT = "Ilość wybranych usług: %d";
    public final static String REGEX_PL_LETTERS = "AaĄąBbCcĆćDdEeĘęFfGgHhIiJjKkLlŁłMmNnŃńOoÓóPpRrSsŚśTtUuWwYyZzŹźŻż";
    public static String REGEX_LETTERS = "a-zA-Z";
    public static Map<Locale, Map<TiresSwap.SeasonType, String>> tiresType = new HashMap<>();
    public static Map<Locale, Map<Cleaning.Type, String>> cleaningType = new HashMap<>();
    public static Locale appLocale;
    public static final Locale PL_LOCALE;

    static
    {
        // TODO add other locales
        PL_LOCALE = new Locale("pl");
        appLocale = PL_LOCALE;
        REGEX_LETTERS = REGEX_PL_LETTERS;
        Map<TiresSwap.SeasonType, String> tiresTypePolish = new HashMap<>();
        tiresTypePolish.put(TiresSwap.SeasonType.ALLSEASON, "Całoroczne");
        tiresTypePolish.put(TiresSwap.SeasonType.WINTER, "Zimowe");
        tiresTypePolish.put(TiresSwap.SeasonType.SUMMER, "Letnie");

        Map<Cleaning.Type, String> cleaningTypePolish = new HashMap<>();
        cleaningTypePolish.put(Cleaning.Type.INNER, "Wewnętrzne");
        cleaningTypePolish.put(Cleaning.Type.OUTER, "Zewnętrzne");
        cleaningTypePolish.put(Cleaning.Type.COMPREHENSIVE, "Kompleksowe");

        tiresType.put(PL_LOCALE, tiresTypePolish);
        cleaningType.put(PL_LOCALE, cleaningTypePolish);
    }

    public static String getLocalizedSeasonType(TiresSwap.SeasonType type) {
        return tiresType.get(appLocale).get(type);
    }

    public static String getLocalizedStringType(Cleaning.Type type) {
        return cleaningType.get(appLocale).get(type);
    }
}
