import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Klassen GritCalendar, denna klassen har koll på veckor/dagar och var aktiviteter hör till
 * Hashmap med nyckel LocalDate och värde ArrayList<Activity> en lista med aktiviteter
 */
public class GritCalendar {
    // Static för att det finns alltid bara 7 dagar i en vecka och alla kalender objekt kan dela på den
    private static final int DAYS_IN_WEEK = 7;

    private LocalDate mondayOfShownWeek = getMondayOfWeek(LocalDate.now());
    private HashMap<LocalDate, ArrayList<Activity>> activitiesByDate = new HashMap<>();

    /**
     * Lägger till en aktivitet på ett datum.
     *
     * @param date     datumet aktiviteten ska läggas på
     * @param activity aktiviteten som ska läggas till
     */
    public void addActivity(LocalDate date, Activity activity) {
        // HashMap activitiesByDate lägger in datum och skapar en tom lista OM listan inte finns
        activitiesByDate.putIfAbsent(date, new ArrayList<>());
        // Hämtar datumets lista
        ArrayList<Activity> activitiesForDate = activitiesByDate.get(date);
        // Lägger in aktiviteten i listan
        activitiesForDate.add(activity);
    }


    /**
     * Hämtar aktiviteter för specifikt datum
     *
     * @param date Datumet som ska kollas efter aktiviteter
     * @return Ger tillbaka listan med aktiviteter för valt datum, eller en tom lista om inga aktiviteter finns
     */
    public ArrayList<Activity> getActivitiesForDay(LocalDate date) {
        return activitiesByDate.getOrDefault(date, new ArrayList<>());
    }


    /**
     * Tar bort en aktivitet från ett datum
     *
     * @param date     Datumet aktiviteten ska tas bort ifrån
     * @param activity Aktiviteten som ska tas bort
     */
    public void removeActivityForDay(LocalDate date, Activity activity) {
        // Tar bort aktivitet från listan
        // Använder metoden "getActivitiesForDay"
        getActivitiesForDay(date).remove(activity);
    }

    /**
     * Metoden ger måndagen i samma vecka som datumet som skickas in
     * Om datumet redan är en måndag får man tillbaka samma datum
     * Om datumet är en söndag går den tillbaka 6 dagar till måndag
     *
     * @param date Datumet som metoden ska räkna ifrån
     * @return Ger tillbaka måndagen i samma vecka som datumet
     */
    public LocalDate getMondayOfWeek(LocalDate date) {
        // daysBackToMonday får datumet som gavs, kollar vilken dag det är, ex. Torsdag och hämtar value (4) - 1 = 3 dagar bakåt
        int daysBackToMonday = date.getDayOfWeek().getValue() - 1;
        // bläddrar bakåt i kalendern, torsdag 19/11 minus 3 dagar = måndag 16/11.
        return date.minusDays(daysBackToMonday);
    }

    /**
     * Metoden ger veckans 7 datum måndag till söndag
     *
     * @param date Datumet i den veckan som gäller
     * @return Ger tillbaka 7 datum måndag till söndag
     */
    public ArrayList<LocalDate> getDatesOfWeek(LocalDate date) {
        // Skapar en ArrayList för att samla alla dagarna i veckan
        ArrayList<LocalDate> daysOfWeek = new ArrayList<>();
        // Använder metoden getMondayOfWeek så får vi starten på veckan
        LocalDate mondayOfWeek = getMondayOfWeek(date);
        // i = antal dagar efter måndag
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            // Lägger in varje dag i ArrayList, börjar på mondayOfWeek som är måndag pga. getMondayOfWeek() på veckan
            daysOfWeek.add(mondayOfWeek.plusDays(i));
        }
        return daysOfWeek;
    }

    /**
     * Metod som ger ut sju datumen i veckan som visas nu
     *
     * @return Ger tillbaka sju datumen i veckan som visas nu
     */
    public ArrayList<LocalDate> getDatesOfShownWeek() {
        // Använder metoden getDatesOfWeek och i parametern lägger vi in en variabel som
        // har värdet av veckans måndag
        return getDatesOfWeek(mondayOfShownWeek);
    }

    /**
     * Metoden flyttar visad vecka framåt, adderar 7 dagar på mondayOfShownWeek
     */
    public void showNextWeek() {
        mondayOfShownWeek = mondayOfShownWeek.plusDays(DAYS_IN_WEEK);
    }

    /**
     * Metoden flyttar visad vecka bakåt, subtraherar 7 dagar på mondayOfShownWeek
     */
    public void showPreviousWeek() {
        mondayOfShownWeek = mondayOfShownWeek.minusDays(DAYS_IN_WEEK);
    }

    /**
     * Metod ger ut veckonumret
     *
     * @return Ger tillbaka vilken vecka det är
     */
    public int getShownWeekNumber() {
        return mondayOfShownWeek.get(WeekFields.ISO.weekOfWeekBasedYear());
    }

    /**
     * Metod ger ut året för veckan
     *
     * @return Ger tillbaka vilket år det är
     */
    public int getShownYear() {
        return mondayOfShownWeek.get(WeekFields.ISO.weekBasedYear());
    }

}