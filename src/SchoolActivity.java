import java.awt.*;

/**
 * En aktivitet av typen skol aktivitet ärver Activity och har egen färg
 */
public class SchoolActivity extends Activity{

    /**
     * Skapar en skol aktivitet
     *
     * @param nameOfActivity namnet på skol aktiviteten
     */
    public SchoolActivity(String nameOfActivity) {
        super(nameOfActivity);
    }

    /**
     * Färgen för en skol aktivitet
     *
     * @return Ger färgen för skol aktiviteten
     */
    @Override
    public Color getColor() {
        return Color.GREEN;
    }

    /**
     * Skriver ut typen av aktivitet innan namnet på aktivitet
     *
     * @return Vad det är för aktivitet + namnet på aktiviteten
     */
    @Override
    public String toString() {
        return "Skola: " + super.toString();
    }
}
