import java.awt.*;

/**
 * En aktivitet av typen träning ärver Activity och har egen färg
 */
public class TrainingActivity  extends Activity{

    /**
     * Skapar en tränings aktivitet
     *
     * @param nameOfActivity namnet på träningspasset
     */
    public TrainingActivity(String nameOfActivity) {
        super(nameOfActivity);
    }

    /**
     * Färgen för en tränings aktivitet
     *
     * @return Ger färgen för träning
     */
    @Override
    public Color getColor() {
        return Color.ORANGE;
    }

    /**
     * Skriver ut typen av aktivitet innan namnet på aktivitet
     *
     * @return Vad det är för aktivitet + namnet på aktiviteten
     */
    @Override
    public String toString() {
        return "Träning: " + super.toString();
    }
}
