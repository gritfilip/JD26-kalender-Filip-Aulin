import java.awt.*;

/**
 * En aktivitet av typen möte ärver Activity och har egen färg
 */
public class MeetingActivity extends Activity{

    /**
     * Skapar en mötes aktivitet
     *
     * @param nameOfActivity namnet på mötet
     */
    public MeetingActivity(String nameOfActivity) {
        super(nameOfActivity);
    }

    /**
     * Färgen för en mötes aktivitet
     *
     * @return Ger färgen för möte
     */
    @Override
    public Color getColor() {
        return Color.YELLOW;
    }

    /**
     * Skriver ut typen av aktivitet innan namnet på aktivitet
     *
     * @return Vad det är för aktivitet + namnet på aktiviteten
     */
    @Override
    public String toString() {
        return "Möte: " + super.toString();
    }
}
