import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Gränssnittet för kalender, skapar och öppnar fönstret med hela veckan och knapparna för att lägga till och ta bort aktivitet
 */
public class GritCalendarGUI {
    // Mina färger jag har använt på portfolio sida, tänkte man kunde ju återanvända dem här
    private static final Color BACKGROUND_DARK = Color.decode("#171333");      // Fönstrets/skåpets bakgrund
    private static final Color DAY_LIST_BACKGROUND = Color.decode("#292052");  // Bakgrunden i dagarnas aktivitetslistor
    private static final Color HEADER_BACKGROUND = Color.decode("#392b70");    // veckodag/datum
    private static final Color TEXT_LIGHT = Color.decode("#f7f7ff");           // Ljus text
    private static final Color SELECTED_HIGHLIGHT = Color.decode("#18c4f4");   // Färgen när man markerar. Datum och dagens kolumn
    private static final Color COLUMN_BORDER = Color.decode("#544784");        // Ramarna runt varje dagkolumn
    private static final Color RED_DAY = Color.decode("#ff6b6b");              // Röd färg för ex. söndag

    private static final Font DATE_FONT = new Font("SansSerif", Font.PLAIN, 16);
    private static final Font WEEK_FONT = new Font("Times New Roman", Font.BOLD, 30);
    private static final Font ACTIVITY_FONT = new Font("Times New Roman", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 20);

    private static final int DAYS_IN_WEEK = 7;
    private static final int SPACE_BETWEEN = 45;
    private static final int WIDTH_WINDOW = 1000;
    private static final int HEIGHT_WINDOW = 500;
    private static final int MIN_WIDTH_WINDOW = 600;
    private static final int MIN_HEIGHT_WINDOW = 300;

    private static final String TRAINING = "Träning";
    private static final String MEETING = "Möte";
    private static final String SCHOOL = "Skola";
    private static final String STANDARD = "Standard";

    // Datum som svensk text
    private static final Locale SWEDISH = Locale.of("sv");
    // EEEE ger hela dagens namn, EEE ger förkortning ex. tors
    private static final DateTimeFormatter DAY_NAME = DateTimeFormatter.ofPattern("EEEE", SWEDISH);
    // "d" är dag i månaden, ex 24
    private static final DateTimeFormatter DAY_AND_MONTH = DateTimeFormatter.ofPattern("d MMMM", SWEDISH);

    private GritCalendar calendar = new GritCalendar();


    // sevenDayColumnsPanel blir "skåpet" för resten av boxarna
    private JPanel sevenDayColumnsPanel = new JPanel(new GridLayout(1, DAYS_IN_WEEK));
    // Label för att visa vilken vecka
    private JLabel weekNumberLabel;

    // Lista och variabler för att kunna "hoppa" mellan det användaren trycker på
    private ArrayList<JList<Activity>> allDayLists = new ArrayList<>();
    private Activity selectedActivity;
    private LocalDate selectedDate;

    /**
     * Skapar hela kalendern
     */
    public GritCalendarGUI() {
        // JFrame, hela fönstret
        JFrame window = new JFrame();
        window.setTitle("Grit Kalender");
        window.setSize(WIDTH_WINDOW, HEIGHT_WINDOW);
        // Fönstret öppnas i mitten av skärmen
        window.setLocationRelativeTo(null);
        // En minsta storlek
        window.setMinimumSize(new Dimension(MIN_WIDTH_WINDOW, MIN_HEIGHT_WINDOW));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // JPanel, en låda som håller andra boxar
        JPanel buttonPanel = new JPanel(new GridLayout(2,2));
        buttonPanel.setBackground(BACKGROUND_DARK);
        sevenDayColumnsPanel.setBackground(BACKGROUND_DARK);

        // JButton, knappar som man kan klicka på
        JButton addActivity = new JButton("Lägg till aktivitet");
        JButton removeActivity = new JButton("Ta bort aktivitet");
        JButton nextWeek = new JButton("Nästa vecka");
        JButton prevWeek = new JButton("Föregående vecka");

        weekNumberLabel = centeredLabel("", SELECTED_HIGHLIGHT);
        weekNumberLabel.setOpaque(true);
        weekNumberLabel.setBackground(HEADER_BACKGROUND);
        weekNumberLabel.setFont(WEEK_FONT);

        addActivity.addActionListener(e -> {
            String nameOfActivity = JOptionPane.showInputDialog("Vad vill du lägga till i kalendern?");
            if (nameOfActivity == null) {
                return;
            } else if (nameOfActivity.isBlank()) {
                JOptionPane.showMessageDialog(null, "Kan inte lägga in tom aktivitet");
                return;
            }
            String dateOfActivity = JOptionPane.showInputDialog("Ange datum (ÅÅÅÅ-MM-DD) för: " + nameOfActivity);
            if (dateOfActivity == null) {
                return;
            } else if (dateOfActivity.isBlank()) {
                JOptionPane.showMessageDialog(null, nameOfActivity + " behöver ett datum");
                return;
            } else {
                String[] typesOfActivity = {STANDARD, TRAINING, SCHOOL, MEETING};

                // Frågar användaren vad för typ av aktivitet det är
                String chosenActivity = (String) JOptionPane.showInputDialog(null, "Välj typ för aktivitet", "Typ",
                        JOptionPane.QUESTION_MESSAGE, null, typesOfActivity, STANDARD);

                if (chosenActivity == null) {
                    return;
                }

                Activity usersActivity = createActivity(chosenActivity, nameOfActivity);

                try {
                    calendar.addActivity(LocalDate.parse(dateOfActivity), usersActivity);
                    updateWeekView();

                } catch (DateTimeParseException e1) {
                    JOptionPane.showMessageDialog(null, "Skriv i detta format (ÅÅÅÅ-MM-DD)");
                }
            }
        });

        removeActivity.addActionListener(e -> {
            if (selectedActivity == null || selectedDate == null) {
                JOptionPane.showMessageDialog(null, "Klicka på en aktivitet i kalendern du vill ta bort");
                return;
            }
            int answer = JOptionPane.showConfirmDialog(null, "Vill du ta bort " + selectedActivity + "?", "Ta bort", JOptionPane.YES_NO_OPTION);
            if (answer != JOptionPane.YES_OPTION) {
                return;
            }
            calendar.removeActivityForDay(selectedDate, selectedActivity);
            selectedActivity = null;
            selectedDate = null;
            updateWeekView();
        });

        nextWeek.addActionListener(e -> {
            calendar.showNextWeek();
            updateWeekView();
        });

        prevWeek.addActionListener(e -> {
            calendar.showPreviousWeek();
            updateWeekView();
        });

        updateWeekView();

        // Lägger till knapparna i sitt box
        buttonPanel.add(prevWeek);
        buttonPanel.add(nextWeek);
        buttonPanel.add(addActivity);
        buttonPanel.add(removeActivity);

        // Färger för knapparna
        styleButton(addActivity, SELECTED_HIGHLIGHT, BACKGROUND_DARK);
        styleButton(removeActivity, RED_DAY, TEXT_LIGHT);
        styleButton(nextWeek, HEADER_BACKGROUND, TEXT_LIGHT);
        styleButton(prevWeek, HEADER_BACKGROUND, TEXT_LIGHT);

        // Lägger in "skåpet" och knapparna i JFrame fönstret
        window.add(sevenDayColumnsPanel, BorderLayout.CENTER);
        window.add(buttonPanel, BorderLayout.SOUTH);
        window.add(weekNumberLabel, BorderLayout.NORTH);

        // Gör allting synligt
        window.setVisible(true);
    }

    private void updateWeekView() {
        // Tömmer "skåpet" på de gamla lådorna innan vi bygger upp de nya (annars läggs 7 nya på dom gamla)
        sevenDayColumnsPanel.removeAll();
        // Tömmer ArrayListen allDayLists
        allDayLists.clear();

        selectedActivity = null;
        selectedDate = null;

        weekNumberLabel.setText("Vecka " + calendar.getShownWeekNumber() + " | " + calendar.getShownYear());


        // Skapar en variabel "datesOfShownWeek" med typen ArrayList<LocalDate>.
        // Metoden "getDatesOfShownWeek" ger "datesOfShownWeek" listan
        //                   [Tar emot listan]  <--------  [lista med veckodagar]
        ArrayList<LocalDate> datesOfShownWeek = calendar.getDatesOfShownWeek();


        // EN for-loop för att gå igenom alla dagar för veckan
        for (int i = 0; i < DAYS_IN_WEEK; i++) {

            // Skapar en LocalDate variabel med namnet dateOfThisColumn för varje loop (7 gånger)
            // Ett datum plockas ut ur listan och läggs i variablen dateOfThisColumn
            // i/0 = måndag, i/1 = tisdag, i/2 = onsdag osv -> söndag
            LocalDate dateOfThisColumn = datesOfShownWeek.get(i);


            // JLabel, box med innehåll
            // Veckodagen som svensk text, ex "måndag", i vald färg
            // Etiketten som metoden skickar tillbaka sparas i weekdayLabel
            JLabel weekdayLabel = centeredLabel(dateOfThisColumn.format(DAY_NAME), TEXT_LIGHT);

            // Datumet med månad, ex "16 november", vald färg
            JLabel dateLabel = centeredLabel(dateOfThisColumn.format(DAY_AND_MONTH), SELECTED_HIGHLIGHT);

            // Söndagar får röd text
            if (dateOfThisColumn.getDayOfWeek() == DayOfWeek.SUNDAY) {
                weekdayLabel.setForeground(RED_DAY);
                dateLabel.setForeground(RED_DAY);
            }


            // Lådan som håller de två boxarna med veckodagar och datum (2 rader, 1 kolumn)
            // skapar en ny "låda" för varje varv i loopen
            JPanel weekdayAndDatePanel = new JPanel(new GridLayout(2, 1));
            weekdayAndDatePanel.setBackground(HEADER_BACKGROUND);

            // varje "låda" lägger in dem 2 boxarna i sig,
            weekdayAndDatePanel.add(weekdayLabel);
            weekdayAndDatePanel.add(dateLabel);

            // Den STORA lådan
            // En (blir 7) ny låda som ska hålla lådorna
            JPanel oneDayColumnPanel = new JPanel(new BorderLayout());

            // JList kan inte läsa vanliga listor direkt, utan behöver en mellanhands lista (DefaultListModel)
            DefaultListModel<Activity> activityDefaultListModel = new DefaultListModel<>();

            // For loop i en for loop, i denna for loopen stannar vi tills den är färdig
            // För varje activity som finns i "dateOfThisColumn"
            for (Activity activity : calendar.getActivitiesForDay(dateOfThisColumn)) {
                // hämtas och läggs in i activityDefaultListModel
                activityDefaultListModel.addElement(activity);
            }

            // JList, den synliga listan på skärmen, som hämtar sina aktiviteter från mellanhands listan ovan
            JList<Activity> activityJList = new JList<>(activityDefaultListModel);
            // Ger mellanrum mellan aktiviteterna
            activityJList.setFixedCellHeight(SPACE_BETWEEN);
            activityJList.setFont(ACTIVITY_FONT);
            activityJList.setSelectionBackground(SELECTED_HIGHLIGHT);
            activityJList.setForeground(TEXT_LIGHT);

            // Speciell färg på dagens kolumn
            if (dateOfThisColumn.equals(LocalDate.now())) {
                activityJList.setBackground(SELECTED_HIGHLIGHT);
            } else {
                activityJList.setBackground(DAY_LIST_BACKGROUND);
            }

            // Sparar dagens JList i samlingslistan så vi kan hålla koll på alla 7 dagars listor
            allDayLists.add(activityJList);
            // Lyssnar efter när användaren klickar på en aktivitet i just denna dags lista
            activityJList.addListSelectionListener(e -> {
                // om användaren har släppt musknappen
                if (!e.getValueIsAdjusting() && activityJList.getSelectedValue() != null) {
                    // Kommer ihåg vilken aktivitet och vilket datum som valdes
                    selectedActivity = activityJList.getSelectedValue();
                    selectedDate = dateOfThisColumn;

                    // Går igenom alla sju dagars listor i veckan
                    for (JList<Activity> list : allDayLists) {
                        // Om det är en annan dag än den vi precis klickade på
                        if (list != activityJList) {
                            // då tar vi bort den blå markeringen så bara en sak är vald åt gången
                            list.clearSelection();
                        }
                    }

                }
            });


            // Egen ritare för listan (JList cell renderer)
            // Mönstret kommer från Oracles guide "How to Use Lists" och codejava.net
            // "JList custom renderer example", skelettet har jag fått hjälp med av (AI)
            // Listan frågar ritaren för varje rad: hur ska den här raden se ut?
            activityJList.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    // Standardritaren gör en vanlig box för raden, den lägger jag i label så jag kan ändra den
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    // Texten i mitten
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    // value är aktiviteten på raden, gör om den till Activity så jag kan fråga efter färgen
                    Activity activity = (Activity) value;
                    // Om raden inte är vald får den sin färg, annars syns inte den blå markeringen
                    if (!isSelected) {
                        // Svart text så den syns på alla färger
                        label.setForeground(Color.BLACK);
                        // Bakgrunden får färgen från typen: träning orange, möte gul, skola grön
                        label.setBackground(activity.getColor());
                    }
                    // Ger tillbaka den färdiga boxen till listan som ritar ut den
                    return label;
                }
            });

            // Lägger den synliga listan (JList) i en ny låda som man kan scrolla i om det blir många aktiviteter
            JScrollPane activityJScrollPane = new JScrollPane(activityJList);
            activityJScrollPane.setBorder(BorderFactory.createLineBorder(COLUMN_BORDER, 1));

            // Lägger in lådan som har dem två lådorna med dagen och tillhörande datum i den STORA lådan,
            // placerar den i toppen av den STORA lådan
            oneDayColumnPanel.add(weekdayAndDatePanel, BorderLayout.NORTH);
            // Lägger in lådan som har listan med aktiviteter i den STORA lådan,
            // placerar den i mitten av den STORA lådan
            oneDayColumnPanel.add(activityJScrollPane, BorderLayout.CENTER);


            // Den STORA lådan läggs in i "skåpet"
            sevenDayColumnsPanel.add(oneDayColumnPanel);

            // efter 7 varv slutar for loopen här
        }

        // Säger åt Swing att räkna om platserna i "skåpet" och rita om skärmen så ändringarna syns direkt
        sevenDayColumnsPanel.revalidate();
        sevenDayColumnsPanel.repaint();
    }

    /**
     * Skapar en etikett med centrerad text, valt typsnitt och färg
     *
     * @param text  texten som ska stå på etiketten
     * @param color färgen på texten
     * @return den färdiga etiketten, redo att läggas in i en panel
     */
    private JLabel centeredLabel(String text, Color color) {
        // Skapar etiketten och ger den texten som kom in
        JLabel label = new JLabel(text);
        // Texten hamnar i mitten av etiketten
        label.setHorizontalAlignment(SwingConstants.CENTER);
        // Samma typsnitt på alla etiketter
        label.setFont(DATE_FONT);
        // Färgen som kom in
        label.setForeground(color);
        // Skickar tillbaka etiketten till den som anropade metoden
        return label;
    }

    /**
     * Metod för att skapa den aktivitet som väljs
     *
     * @param typeOfActivity typen som användaren valde
     * @param nameOfActivity namnet som användaren skrev
     * @return Ger tillbaka vald aktivitet
     */
    private Activity createActivity(String typeOfActivity, String nameOfActivity) {

        switch (typeOfActivity) {

            case TRAINING -> {
                return new TrainingActivity(nameOfActivity);
            }

            case MEETING -> {
                return new MeetingActivity(nameOfActivity);
            }

            case SCHOOL -> {
                return new SchoolActivity(nameOfActivity);
            }

            default -> {
                return new Activity(nameOfActivity);
            }

        }
    }

    /**
     * Metod för att färga buttons
     *
     * @param button          Button som ska få style
     * @param backgroundColor Bakgrundsfärgen på button
     * @param fontColor       Textfärgen på button
     */
    private void styleButton(JButton button, Color backgroundColor, Color fontColor) {
        // Gör knappen ogenomskinlig så hela blir i vald färg
        button.setOpaque(true);
        // Tar bort knappens border
        button.setBorderPainted(false);
        // Färgar bakgrunden på knappen
        button.setBackground(backgroundColor);
        // Färgar texten på knappen
        button.setForeground(fontColor);
        // Bestämmer storlek på knappens text
        button.setFont(BUTTON_FONT);
    }

}