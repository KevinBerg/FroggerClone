package froggerclonehd;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Kevin Berg, Jan Olschewski, Niels Oldenburg
 */
public class Spiel extends JFrame {

    final private Spielfeld spielfeld;
    final private XMLReader xmlReader;
    
    private String modus = null; //Über diesen Modus wird das Spiel gesteuert.
    private Audio backgroundmusic = null;
    private Controller controller = null; //KeyListener
    private Ticker ticker = null; //Observable
    private Menu menu = null;
    private Timer timer = null; //Zeitgeber für die Punkteberechnung
    private Image dbImage = null; // DoubleBuffering
    private Graphics dbGraphics = null; // DoubleBuffering
    private int level = 1; //Start in Level 1
    private boolean running = false; // true = Spiel ist gestartet
    private int score = 0; //Punkte vom Spieler
    private int levelTime = 60; //Zeit in der es Punkte gibt in s

    private int fieldSize; //Größe eines Feldes auf dem Spielfeld
    private int resolution;

    private Player player = null; //Die Spielerinstanz

    private Walker[] enemies; //Passantenarray (Gegner)
    private Vehicle[] vehicleEnemies; //Fahrzeugarray (Gegner)

    /**
     * Berechnet die Größe eines Felds anhand der eingestellten Auflösung.
     */
    private void calculateGameSize() {
        resolution = xmlReader.getResolution();
        fieldSize = resolution / 20;
    }

    /**
     * Setzt die running Variable, welche aussagt, ob das Spiel gestartet wurde.
     * @param b 
     */
    private void setGameRunning(boolean b) {
        this.running = b;
    }

    /**
     * Gibt zurück ob das Spiel gestartet wurde.
     * @return 
     */
    public boolean isGameRunning() {
        return this.running;
    }

    /**
     * Konstruktor
     */
    public Spiel() {
        spielfeld = Spielfeld.getInstance();
        xmlReader = XMLReader.getInstance();
        modus = "MODUS_LOAD";
        calculateGameSize();
        controlModus(); 
    }

    /**
     * Paint Methode für DoubleBuffering
     * @param g
     */
    public void paint(Graphics g) {
        dbImage = createImage(getWidth(), getHeight());
        dbGraphics = dbImage.getGraphics();
        try {
            paintComponent(dbGraphics);
        } catch (IOException ex) {
            Logger.getLogger(Spiel.class.getName()).log(Level.SEVERE, null, ex);
        }
        g.drawImage(dbImage, 0, 0, this);
    }

    /**
     * Methode für das Zeichnen aller Spielkomponenten.
     *
     * @param g
     * @throws IOException
     */
    public void paintComponent(Graphics g) throws IOException { // Hier die Zeichnungen vornehmen!

        if (modus.equals("MODUS_SPIEL")) {

            //Hintergrundfelder zeichnen
            for (int i = 0; i < 20; i++) { 
                for (int j = 0; j < 20; j++) {
                    g.drawImage(spielfeld.felder[i][j].getImage(), i * fieldSize, j * fieldSize, fieldSize, fieldSize, this);
                }
            }//Hintergrundfelder zeichnen

            //Walkers zeichnen
            for (Walker enemie : enemies) {
                g.drawImage(enemie.getImage(), enemie.getxPos(), enemie.getyPos(), enemie.getWidth(), enemie.getHeight(), this);
            }

            //Vehicles zeichnen
            for (Vehicle car : vehicleEnemies) {
                g.drawImage(car.getImage(), car.getxPos(), car.getyPos(), car.getWidth(), car.getHeight(), this);
            }

            //Player zeichnen
            g.drawImage(player.getImage(), player.getxPos(), player.getyPos(), player.getWidth(), player.getHeight(), this);

            //Healthbar
            int nextPlace = 0;
            for (int i = 0; i < player.getMaximumHealth(); i++) {
                nextPlace += (int) (fieldSize * 1.1);
                if (player.getHealth() < i + 1) {
                    g.drawImage(player.getHealthlessImage(), (int) (resolution - (0.5 * fieldSize)) - nextPlace, (int) (resolution - (fieldSize)), player.getWidth(), player.getHeight(), this);
                } else {
                    g.drawImage(player.getHealthImage(), (int) (resolution - (0.5 * fieldSize)) - nextPlace, (int) (resolution - (fieldSize)), player.getWidth(), player.getHeight(), this);
                }
            } //Healthbar

            //Scorebar
            g.setColor(Color.black);
            g.setFont(new Font("TimesRoman", Font.BOLD, (int) (fieldSize * 0.68)));
            g.drawString("Punkte: " + Integer.toString(getScore()), fieldSize, (int) (resolution - (int) (0.25 * fieldSize)));
            //Scorebar

            //Pausenmenü
            if (Ticker.pause) {

                g.setColor(new Color(0.0f, 0.2f, 0.5f, 0.9f));
                g.fillRect(0, 0, getWidth(), getHeight());

                g.setColor(Color.black);

                g.setFont(new Font("TimesRoman", Font.BOLD, resolution / 20));
                g.drawString("P A U S E", getWidth() / 2 - (resolution / 10), getHeight() / 4); //zentrierung noch hardcodiert

                g.setFont(new Font("TimesRoman", Font.BOLD, resolution / 35));
                g.drawString("|Escape-Weiter|   |F2-Haumptmenü|   |F4-Quit|", resolution / 5 + resolution / 40, getHeight() / 2); //zentrierung noch hardcodiert

            } //Pausenmenü

        } //if MODUS_SPIEL

    } //PaintComponent

    /**
     * Hier werden benötigte Objekte erzeugt, die nur einmalig erzeugt werden müssen.
     */
    public void initObjectsUnique(){
        backgroundmusic = new Audio(xmlReader.getBackgroundMusic()); //Name aus XML beziehen
        backgroundmusic.start();
    }
    
    /**
     * Erzeugt benötigte Objekte.
     */
    public void initObjects() {
        ticker = new Ticker(); //Observable für Player, Walker und Vehicle
        timer = new Timer(); //Zeitgeber für Scoreberechnung
        player = new Player(Integer.toString(level));
        controller = new Controller(this, player); //KeyListener
    }

    /**
     * Initialisiert die Gegner.
     *
     * @param lvl
     */
    public void initAdversary(int lvl) {
        enemies = xmlReader.getWalkers(Integer.toString(lvl));
        vehicleEnemies = xmlReader.getVehicles(Integer.toString(lvl));
        
        //Gegner beim Observable anmelden.
        for (Walker enemy : enemies) ticker.addObserver(enemy);
        for (Vehicle enemy : vehicleEnemies) ticker.addObserver(enemy);
    }

    /**
     * Initialisiert das Spielfeld (Frame).
     */
    public void initFrame() {
        setTitle("Froggerclone HD");
        setSize(resolution, (resolution)); //xml auslesen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addKeyListener(controller);
        setUndecorated(true);
        setVisible(false);
    }

    /**
     * Übernimmt die Kontrolle über das Spiel.
     */
    public void controlModus() {

        initObjectsUnique(); //Operationen die einmalig ausgeführt werden müssen.
        
        while (true) { //Hauptschleife, terminiert durch das Menü (klick auf Spiel beenden) oder im Spiel über F4.

            switch (modus) {

                case "MODUS_LOAD":
                    
                    initObjects();
                    initFrame();
                    timer.start();
                    ticker.addObserver(player); //Player beim Ticker anmelden
                    modus = "MODUS_START";
                    break;

                case "MODUS_START":
                    if (xmlReader.getResolution() == resolution) { //Wenn die Auflösung nicht verändert wurde.
                        if(isVisible()) setVisible(false); //Spielframe unsichtbar machen, falls es sichtbar ist.
                        resetGameData();

                        if (menu == null) menu = new Menu(); //Erstelle ein Startmenü falls es noch keins gibt

                        if(!menu.isVisible()) menu.setVisible(true);

                        if (menu.getStartGame()) { //Wenn das Spiel gestartet werden soll (nach Klick auf Spiel starten)
                            menu.setStartGame(false);
                            modus = "MODUS_SPIEL";
                            menu.setVisible(false);
                        }

                    } else {
                        dispose(); //Spielframe verwerfen
                        calculateGameSize(); //Auflösung u.s.w. neu auslesen
                        spielfeld.reloadSpielfeld(); //Neue Spielfeldinstanz (mit neuer Größe)
                        setModus("MODUS_LOAD"); //Spiel neu laden
                    }

                    break;

                case "MODUS_SPIEL":

                    if(!isGameEnded()){
                        setGameRunning(true);
                        loadLevel(); //Lädt die Map und die Gegner
                        setVisible(true);
                        timer.resetPlayedSeconds(); //Reset der gespielten Zeit. (Wichtig für Punkteberechnung)
                        player.setStartPosition(Integer.toString(level));
                    } else {
                        menu.setGameEndMenu(getScore());
                        setModus("MODUS_GAME_OVER");
                    }
                    
                    while (isGameRunning()) { //Hier läuft das Spiel.

                        ticker.tick(); //In dieser Methode werden die Observer benachrichtigt

                        if(playerIntersectsEnemy()) collision(); //Kollision

                        repaint(); //Spiel zeichnen

                        if (spielfeld.getTargets() == 0) { //Levelchange
                            setGameRunning(false);
                            level += 1;
                            new Audio(xmlReader.getLevelChangeSound(), 1).start(); //Nebenläufiges abspielen des Levelübergangsounds.
                        }

                        if (!player.isAlive()) {
                            setGameRunning(false);
                            menu.setGameEndMenu(getScore());
                            modus = "MODUS_GAME_OVER";
                        }

                        //Wenn im Spiel F2 gedrückt wurde, muss die While-Schleife terminieren.
                        if (modus.equals("MODUS_BACK_TO_MENU")) { //Der Controller setzt den Spiel Modus beim Drücke von F2 in diesen Modus.
                            setModus("MODUS_START");
                            Ticker.pause = false; //Damit der Ticker beim Spielstart wiederläuft.
                            setGameRunning(false); //Damit die while-Schleife nicht gestartet wird.
                        }

                    }//while isGameRunning

                    //Wenn das Level beendet wurde und der Player noch am Leben ist.
                    if (player.isAlive() && getModus().equals("MODUS_SPIEL")) {
                        setScore(getScore() + calculateScore(timer.getPlayedSeconds())); //Punkte aktualisieren
                    }
                    
                    levelTime = 60; //Levelzeit wieder auf 60 Sekunden setzen.

                    timer.setTimerRunning(false);
                    break;

                case "MODUS_GAME_OVER":

                    dispose(); //Spielframe verwerfen.

                    //Wenn nicht mehr das SubmitHighscore-Menü auf ist.
                    if (!menu.getTitle().equals("Froggerclone HD - Game Over")) {
                        setModus("MODUS_START"); // Zurück zum Hauptmenü
                        highscoreXMLReader.getInstance().submitHighscore(getScore()); //Highscore evtl. in die Highscoreliste eintragen.
                    }

                    break;
            }
        }//while
    } //controlModus

    /**
     * Setzt alle nötigen Spieldaten auf ihre Anfangswerte zurück.
     */
    public void resetGameData() {
        resetScore();
        player.setStandardHealth(); //Health des Spielers reseten
        level = 1; //reset level
        spielfeld.resetTargets();
    }

    /**
     * Setzt den Modus des Spiels.
     * @param modus 
     */
    public void setModus(String modus) {
        this.modus = modus;
    }

    /**
     * Lädt das aktuelle Level.
     * Initialisiert die neue Map und die Gegner.
     */
    private void loadLevel() {
        initAdversary(level);
        spielfeld.initMap(level);
    }
    
    /**
     * Gibt zurück ob alle Levels beendet wurden.
     * @return 
     */
    private boolean isGameEnded(){
        return this.level == xmlReader.countLevels(); 
    }

    /**
     * Berechnet die Punktzahl
     * @param playedSeconds
     * @return 
     */
    private int calculateScore(int playedSeconds) {

        this.levelTime--;

        if (!(playedSeconds >= this.levelTime)) {
            return (this.levelTime - playedSeconds);
        } else {
            return 0;
        }

    }

    /**
     * Führt die Operationen bei einer Kollision durch.
     */
    private void collision(){
        player.setToStart();//Player auf Startposition setzten.
        player.decreaseHealth(); //Players leben verringern
        new Audio(xmlReader.getCollisionSound(), 1).start(); //Nebenläufiger Kollisionssound
    }
    
    /**
     * Prüft ob eine Kollision stattfindet.
     * @return 
     */
    private boolean playerIntersectsEnemy() {

        Rectangle playerBounding = player.getBounding();

        boolean collision = false;
        
        //Walker
        for (Walker enemy : enemies) {
            Rectangle enemyBounding = enemy.getBounding();
            if (playerBounding != null && enemyBounding != null) {
                if (playerBounding.intersects(enemyBounding)) { //Kollisionserkennung
                    collision = true;
                    break;
                }

            }
        }
        
        //Vehicle
        for (Vehicle enemy : vehicleEnemies) {
            Rectangle enemyBounding = enemy.getBounding();
            if (playerBounding != null && enemyBounding != null) {
                if (playerBounding.intersects(enemyBounding)) { //Kollisionserkennung
                    collision = true;
                    break;
                }

            }
        }
        
        return collision;
    }

    /**
     * Gibt den Spielmodus zurück.
     * @return modus
     */
    public String getModus() {
        return this.modus;
    }

    /**
     * Setzt die Punkte auf 0.
     */
    public void resetScore() {
        this.score = 0;
    }

    /**
     * Gibt die Punkte zurück.
     * @return score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Setzt die Punkte.
     * @param score 
     */
    private void setScore(int score) {
        this.score = score;
    }

}
