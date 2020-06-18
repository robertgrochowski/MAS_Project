package mas.model;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Localization {

    public static Map<Locale, Map<TiresSwap.SeasonType, String>> tiresType = new HashMap<>();
    public static Map<Locale, Map<Cleaning.Type, String>> cleaningType = new HashMap<>();
    public static Locale appLocale;
    public static final Locale PL_LOCALE;

    static
    {
        PL_LOCALE = new Locale("pl");
        appLocale = PL_LOCALE;

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
