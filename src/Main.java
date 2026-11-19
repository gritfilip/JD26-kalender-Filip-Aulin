/**
 * Startpunkten för programmet. Main gör inget annat än att starta kalenderfönstret.
 */
public class Main {

    /**
     * Startar programmet genom att skapa kalenderfönstret.
     *
     * @param args kommandoradsargument, används inte i det här programmet
     */
    public static void main(String[] args) {
        new GritCalendarGUI();
    }
}