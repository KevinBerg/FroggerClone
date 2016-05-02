package froggerclonehd;

import java.util.Observable;

/**
 *
 * @author Kevin Berg, Jan Olschewski, Niels Oldenburg
 */
public class Walker extends Figur {
    
    String picPath; //Zwischenspeicherung des Bildpfads, notwendig für Bildwechsel bei Richtungsänderung
    Spielfeld spielfeld;

    @Override
    public void update(Observable o, Object arg) {
        calculateBounding();
        setBackToField();
        float systemTime = (Float)arg;
        if(systemTime > 1) return;//Wenn Systemzeit zu hoch, keine Berechnung vornehmen!
        int nextPos =  this.getxPos() + (this.getRightwards() * (int)( (this.getSpeed() * systemTime) * (XMLReader.getInstance().getResolution() / (float) 80)));  //Nächste Position mit Frametime errechnen
        this.setxPos(nextPos); //Koordinatenänderung
        try{
            if(walkerOnFixField()) toggleRightwards();
       } catch (ArrayIndexOutOfBoundsException e){}
    } // update
    
    /**
     * Konstruktor
     * @param xPos
     * @param yPos
     * @param speed
     * @param rightwards
     * @param type 
     */
    public Walker(int xPos, int yPos, int speed, int rightwards, String type){
        spielfeld = Spielfeld.getInstance();
        float x = (float)xPos;
        float y = (float)yPos;
        this.setxPos((int)(x/spielfeld.fields * XMLReader.getInstance().getResolution()));
        this.setyPos((int)(y/spielfeld.fields * XMLReader.getInstance().getResolution()));
        this.setSpeed((int)(speed*2));
        this.setWidth(spielfeld.getFieldSize());
        this.setHeight(spielfeld.getFieldSize());
        this.setRightwards(rightwards);//direction
        this.setImage(type, rightwards);
        picPath = type;
    }  
    
    /**
     * Ändert die Richtung und das Bild des Walkers.
     */
    private void toggleRightwards(){
        setRightwards(getRightwards() * -1);
        setImage(picPath, getRightwards());
    }
    
    /**
     * Gibt den Typ des Felds zurück auf dem der Walker steht.
     * @return 
     */
    public Feld getWalkerField()
    {
        return spielfeld.felder[getxCoord()][getyCoord()];
         
    }
    
    /**
     * Gibt das Feld rechts vom Walker zurück.
     * @return 
     */
    public Feld getFieldRightOfWalker(){
        return spielfeld.felder[getxCoord()+ 1][getyCoord()];
    }

    /**
     * Gibt true zurück, wenn der Walker sich auf einem Feld vom Typ fix befindet.
     * @return 
     */
    private boolean walkerOnFixField()
    {
        if(getRightwards() == 1){ //Wenn der Walker nach rechts läuft.
           return getFieldRightOfWalker().getStatus().equals("fix"); //Das Feld rechts von ihm auf fix prüfen
        } else {
            return getWalkerField().getStatus().equals("fix"); //Das Feld links von ihm prüfen
        }
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
}
