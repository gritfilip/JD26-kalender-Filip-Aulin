import java.awt.*;

/**
 * Klassen Activity, skapar en aktivitet som vet sitt namn
 */
public class Activity {
    private String nameOfActivity;

    /** Objekt Activity, har namnet på aktiviteten */
    public Activity(String nameOfActivity) {
        this.nameOfActivity = nameOfActivity;
    }

    /** Getter för namnet på aktiviteten */
    public String getNameOfActivity() {
        return nameOfActivity;
    }

    /**
     * Metod som ger färgen för aktivitet
     * @return Ger tillbaka färgen på aktiviteten
     */
    public Color getColor() {
        return Color.WHITE;
    }

    /**
     * Skriver ut namnet på aktiviteten istället för standardversionen
     *
     * @return Ger tillbaka namnet på aktivitet
     */
    @Override
    public String toString() {
        return nameOfActivity;
    }

}
