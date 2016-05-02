package froggerclonehd;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Observer;

/**
 *
 * @author Kevin Berg, Jan Olschewski, Niels Oldenburg
 */
public class Player extends Zeichenobjekt implements Observer {

    Spielfeld spielfeld; //Instanz des Spielfelds
    final private XMLReader xmlReader; //Instanz des XMLReaders
    
    private Rectangle bounding ; //Boundingbox
    private int health; //Anzahl der Leben
    
    private Image healthImage; //Bild für ein Leben
    private Image healthlessImage; //Bild für ein abgezogenes Leben.

    private int startxPos; //Startposition
    private int startyPos; //Startposition
    private int maximumHealth = 4;

    /**
     * Konstruktor
     * @param lvl Startlevel in dem das Spiel startet.
     */
    public Player(String lvl) {
        spielfeld = Spielfeld.getInstance();
        xmlReader = XMLReader.getInstance();
        setStandardHealth();
        setWidth(spielfeld.getFieldSize());
        setHeight(spielfeld.getFieldSize());
        setStartPosition(lvl);
        setToStart();
        this.setImage(xmlReader.getPlayerImage());
        this.setHealthImage(xmlReader.getHealthImage());
        this.setHealthlessImage(xmlReader.getHealthLessImage());
    }
    
    /**
     * Setzt die Startposition anahand des Levels.
     * Die Startposition wird aus der config.xml über den XMLReader ausgelesen.
     * @param lvl Aktuelles Level
     */
    public void setStartPosition(String lvl){
        int[] startPos = xmlReader.getStartPosition(lvl); //Startposition des Spielers
        
        //Hier werden nun die angegebenen Spielfeldkoordinaten in Systemkoordinaten umgerechnet.
        setStartxPos((startPos[0] * spielfeld.fieldSize) - spielfeld.fieldSize);
        setStartyPos((startPos[1] * spielfeld.fieldSize) - spielfeld.fieldSize);
        
        setToStart(); //Spielerkoordinaten auf die Startposition setzen.
    }

    /**
     * Setzt den Player auf die aktuelle Startposition.
     */
    public void setToStart(){
        setxPos(getStartxPos());
        setyPos(getStartyPos());
    }
    
    /**
     * Update Methode die vom Observable-Objekt aufgerufen wird.
     * @param o
     * @param arg 
     */
    @Override
    public void update(java.util.Observable o, Object arg) {
        if(playerOnTarget()) { //Wenn der Player im Ziel ist.
           getPlayerField().setTargetReached(); //Feldtyp wird auf "fix" gesetzt.
           spielfeld.decreaseTargets(); //Anzahl der Targetfelder -1.
           setToStart(); //Player auf Startposition setzen.
        }
        calculateBounding(); //Berechnung der neuen Boundingbox
    }

    /**
     * Prüft ob eine bestimmte Bewegung möglich ist.
     * @param dX Bewegung in X-Richtung
     * @param dY Bewegung in Y-Richtung
     * @return Möglichkeit der Bewegung
     */
    private boolean isPossibleMove(int dX, int dY) {
        int testXPos = getxCoord() + dX;
        int testYPos = getyCoord() + dY;
        
        //Prüft ob das Feld auf das sich bewegt werden existieren kann.
        if ( ( (testXPos < spielfeld.fields) && (testYPos < spielfeld.fields)) ) { 
            if (((testXPos >= 0) && (testYPos >= 0))) {
                
                //Wenn das Feld auf das sich bewegt werden soll nicht vom typ fix ist, return true.
                return !"fix".equals(spielfeld.felder[testXPos][testYPos].getStatus()); 

            } else {
                return false;
            }

        } else {
            return false;
        }
       
    } //isPossibleMove
    
    /**
     * Bewegt den Player um die Koordinaten dX und dY.
     * @param dX Bewegung in X-Richtung.
     * @param dY Bewegung in Y-Richtung.
     */
    public void movePlayer(int dX, int dY){
        if(this.isPossibleMove(dX, dY)){
            this.setyPos(this.getyPos() + spielfeld.getFieldSize() * dY);
            this.setxPos(this.getxPos() + spielfeld.getFieldSize() * dX);
        } // else keine bewegung
    }
    
    /**
     * Berechnet die Boundingbox.
     * Diese ist für eine kollisionsabfrage notwendig.
     */
    private void calculateBounding()
    {
        this.bounding = new Rectangle(getxPos(), getyPos(), getWidth(), getHeight());
    }
    
    /**
     * Gibt die Boundingbox zurück.
     * @return Rectangle Boundingbox
     */
    public Rectangle getBounding()
    {
        return this.bounding;
    }
    
    /**
     * Gibt das Feld zurück, auf welchem der Player steht.
     * @return Feld auf dem der Player steht.
     */
    public Feld getPlayerField()
    {
        return spielfeld.felder[getxCoord()][getyCoord()];
    }

    /**
     * Gibt zurück, ob der Player auf einem Targetfeld steht.
     * @return 
     */
    private boolean playerOnTarget()
    {
        return getPlayerField().getTyp().equals("t");
    }
    
    /**
     * Gibt die X-Spielfeldkoordinate zurück.
     * @return 
     */
    private int getxCoord()
    {
        return (getxPos() / spielfeld.fieldSize);
    }
    
    /**
     * Gibt die Y-Spielfeldkoordinate zurück.
     * @return 
     */
    private int getyCoord()
    {
        return (getyPos() / spielfeld.fieldSize);
    }
    
    /**
     * Gibt die Leben des Players zurück.
     * @return 
     */
    public int getHealth()
    {
        return this.health;
    }
    
    /**
     * Resetet die Playerleben.
     */
    public void setStandardHealth(){
        this.health = 4;
    }
    
    /**
     * Verringert die Playerleben um 1.
     */
    public void decreaseHealth()
    {
        this.health -= 1;
    }
    
    /**
     * Gibt die X-Startkoordinate zurück.
     * @return 
     */
    public int getStartxPos()
    {
        return this.startxPos;
    }
    
    /**
     * Gibt die Y-Startkoordinate zurück.
     * @return 
     */
    public int getStartyPos()
    {
        return this.startyPos;
    }
    
    /**
     * Setzt die X-Startkoordinate.
     * @param xPos 
     */
    public void setStartxPos(int xPos){
        this.startxPos = xPos;
    }
    
    /**
     * Setzt die Y-Startkoordinate.
     * @param yPos 
     */
    public void setStartyPos(int yPos){
        this.startyPos = yPos;
    }
    
    /**
     * Gibt das Bild für 1 Leben zurück.
     * @return 
     */
    public Image getHealthImage(){
        return this.healthImage;
    }
    
    /**
     * Setzt das Bild für 1 Leben fest.
     * @param imagePath 
     */
    public void setHealthImage(String imagePath){
        this.healthImage = Toolkit.getDefaultToolkit().getImage(imagePath); // Bild einlesen, auf diesem Wege ist es zum zeichnen bereits bereit
    }
    
    /**
     * Gibt das Bild für ein abgezogenes Leben zurück.
     * @return 
     */
    public Image getHealthlessImage(){
        return this.healthlessImage;
    }
    
    /**
     * Setzt das Bild für ein abgezogenes Bild.
     * @param imagePath 
     */
    public void setHealthlessImage(String imagePath){
        this.healthlessImage = Toolkit.getDefaultToolkit().getImage(imagePath); // Bild einlesen, auf diesem Wege ist es zum zeichnen bereits bereit
    }
    
    /**
     * Gibt die maximale Anzahl an Leben zurück.
     * @return 
     */
    public int getMaximumHealth() {
        /**
        * Diese Methode kann verwendet werden, wenn implementiert wird
        * das ein Player ein extra Leben erhalten kann.
        */
        return this.maximumHealth;
    }
    
    /**
     * Gibt zurück, ob der Player noch am Leben ist.
     * @return 
     */
    public boolean isAlive(){
        return this.getHealth() > 0;
    }
}

